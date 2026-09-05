#!/usr/bin/env python3
"""
Generate rev-228 gameval/RSCM name tables for Offline_Scape.

Sources:
  symbols228/  = osrs-dumps @ f54d6da7 (rev-228) symbols/  -> ID universe + placeholder fallback
  symbols240/  = osrs-dumps @ master   (rev-240) symbols/  -> real names (Jagex gamevals era)
  config dumps at both revisions                           -> safety diffs (repurpose detection)

Rules:
  - Per table, the ID universe is exactly rev-228's (truncates rev-240-only IDs).
  - Name = rev-240 name when the ID exists at 240 AND passes the safety check:
      npc/obj/loc : normalized display-name equality between 228 and 240 dumps
      varbit      : structural (basevar,startbit,endbit) equality between dumps
      others      : no check available (documented residual risk)
    Otherwise name = rev-228 placeholder (npc_124 style).
  - Duplicate names within a table: first keeps the name, later IDs fall back to placeholder.

Outputs:
  out/gamevals/{table}.rscm            name=id  (OR2 RSCMProvider / OpenRune GameValProvider layer 5)
  out/gamevals-binary/gamevals.dat     GameValDat binary, Jagex-gameval-group tables only
  out/gamevals-binary/gamevals_generated.dat   component + dbcol (OpenRune parity)
  out/REPORT.tsv                       per-table counts
"""
import os, re, struct, sys, collections

ROOT = os.path.dirname(os.path.abspath(__file__))
S228 = os.path.join(ROOT, 'osrs-dumps', 'symbols')
S240 = os.path.join(ROOT, 'symbols-240')
D228 = os.path.join(ROOT, 'osrs-dumps', 'config')
OUT  = os.path.join(ROOT, 'out')

def read_sym(path):
    """id -> name ; skips malformed lines; keeps first field pair only."""
    d = {}
    if not os.path.exists(path): return d
    for line in open(path, encoding='utf-8', errors='replace'):
        parts = line.rstrip('\n').split('\t')
        if len(parts) >= 2 and parts[0] and parts[1]:
            d[parts[0]] = parts[1]
    return d

def read_dump_names(path, header_placeholder_re):
    """rev-228 dumps: [npc_0] headers + name= lines. Returns id -> display name."""
    d = {}; cur = None
    for line in open(path, encoding='utf-8', errors='replace'):
        m = re.match(header_placeholder_re, line)
        if m: cur = int(m.group(1)); continue
        if cur is not None and line.startswith('name='):
            d[cur] = line[5:].strip()
    return d

def read_dump240(path, want='name'):
    """rev-240 dumps: '// id' + [gameval_name] headers. want='name' -> display name; want='header' -> section name."""
    d = {}; cur = None
    for line in open(path, encoding='utf-8', errors='replace'):
        m = re.match(r'^// (\d+)\s*$', line)
        if m: cur = int(m.group(1)); continue
        if cur is None: continue
        if want == 'header':
            h = re.match(r'^\[(.+)\]\s*$', line)
            if h and cur not in d: d[cur] = h.group(1)
        elif line.startswith('name='):
            d[cur] = line[5:].strip()
    return d

def norm(s):
    s = re.sub(r'<[^>]*>', '', s).lower()
    return re.sub(r'[^a-z0-9]+', '', s)

def read_varbit_struct(path, varp_name_to_id):
    d = {}; cur = None; rec = {}
    for line in open(path, encoding='utf-8', errors='replace'):
        m = re.match(r'^// (\d+)\s*$', line)
        if m:
            if cur is not None and rec: d[cur] = tuple(sorted(rec.items()))
            cur = int(m.group(1)); rec = {}; continue
        for k in ('basevar', 'startbit', 'endbit'):
            if line.startswith(k + '='):
                v = line.split('=', 1)[1].strip()
                if k == 'basevar':
                    v = int(v.split('_')[1]) if v.startswith('varplayer_') else varp_name_to_id.get(v, -99)
                else:
                    v = int(v)
                rec[k] = v
    if cur is not None and rec: d[cur] = tuple(sorted(rec.items()))
    return d

# ---- safety-check data -------------------------------------------------------
def display_diff(table, ph_prefix):
    a = read_dump_names(os.path.join(D228, f'dump.{table}'), rf'^\[{ph_prefix}_(\d+)\]')
    b = read_dump240(f'/tmp/dump240.{table}', 'name')
    bad = set()
    for i in set(a) & set(b):
        if norm(a[i]) != norm(b[i]): bad.add(i)
    return bad

unsafe = {}
unsafe['npc'] = display_diff('npc', 'npc')
unsafe['obj'] = display_diff('obj', 'obj')
unsafe['loc'] = display_diff('loc', 'loc')

vp240 = {name: int(i) for i, name in read_sym(os.path.join(S240, 'varp.sym')).items()}
vb228 = read_varbit_struct(os.path.join(D228, 'dump.varbit'), {})
vb240 = read_varbit_struct('/tmp/dump240.varbit', vp240)
unsafe['varbit'] = {i for i in set(vb228) & set(vb240) if vb228[i] != vb240[i]}

# ---- table build -------------------------------------------------------------
# table -> (228 sym file for universe, 240 name source, placeholder prefix)
PLACEHOLDER = {
    'npc': 'npc', 'obj': 'obj', 'loc': 'loc', 'seq': 'seq', 'inv': 'inv',
    'dbrow': 'dbrow', 'dbtable': 'dbtable', 'varbit': 'varplayerbit', 'varp': 'varplayer',
    'enum': 'enum', 'struct': 'struct', 'param': 'param', 'jingle': 'jingle',
    'midi': 'midi', 'synth': 'synth', 'category': 'category', 'stat': 'stat',
    'interface': 'interface',
}
SIMPLE_TABLES = ['npc','obj','loc','seq','inv','dbrow','dbtable','varbit','varp',
                 'enum','struct','param','jingle','midi','synth','category','stat','interface']
SYM_FILE = {t: t for t in SIMPLE_TABLES}
SYM_FILE['varbit'] = 'varbit'; SYM_FILE['varp'] = 'varp'

def is_placeholder(name, prefix):
    return bool(re.fullmatch(rf'{prefix}_\d+', name))

report = []
tables = {}          # table -> ordered list of (name, id)
for t in SIMPLE_TABLES:
    u228 = read_sym(os.path.join(S228, SYM_FILE[t] + '.sym'))
    n240 = read_sym(os.path.join(S240, SYM_FILE[t] + '.sym'))
    ph = PLACEHOLDER[t]
    rows, used = [], set()
    c_real = c_fallback_missing = c_fallback_unsafe = c_fallback_dup = 0
    for sid in sorted(u228, key=lambda x: int(x)):
        i = int(sid)
        cand = n240.get(sid)
        placeholder = f'{ph}_{i}'
        name = None
        if cand and not is_placeholder(cand, ph):
            if i in unsafe.get(t, ()):  c_fallback_unsafe += 1
            elif cand in used:          c_fallback_dup += 1
            else:                       name = cand; c_real += 1
        else:
            c_fallback_missing += 1
        if name is None:
            # second tier: a curated rev-228 name (e.g. midi 'scape main', stat)
            c228name = u228.get(sid)
            if c228name and not is_placeholder(c228name, ph) and c228name not in used:
                name = c228name
        if name is None:
            name = placeholder
            if name in used:  # cannot happen (ids unique) but keep invariant
                raise SystemExit(f'placeholder collision {t} {name}')
        used.add(name); rows.append((name, i))
    tables[t] = rows
    report.append((t, len(rows), c_real, c_fallback_missing, c_fallback_unsafe, c_fallback_dup))

# spotanim: universe + names from config dumps (no .sym exists)
sp228 = read_dump_names(os.path.join(D228, 'dump.spot'), r'^\[spotanim_(\d+)\]')  # display names absent; ids only via headers
sp_ids = set()
for line in open(os.path.join(D228, 'dump.spot'), encoding='utf-8', errors='replace'):
    m = re.match(r'^\[spotanim_(\d+)\]', line)
    if m: sp_ids.add(int(m.group(1)))
sp240 = read_dump240('/tmp/dump240.spot', 'header')
rows, used = [], set()
c_real = c_miss = c_dup = 0
for i in sorted(sp_ids):
    cand = sp240.get(i)
    name = None
    if cand and not is_placeholder(cand, 'spotanim'):
        if cand in used: c_dup += 1
        else: name = cand; c_real += 1
    else:
        c_miss += 1
    if name is None: name = f'spotanim_{i}'
    used.add(name); rows.append((name, i))
tables['spotanim'] = rows
report.append(('spotanim', len(rows), c_real, c_miss, 0, c_dup))

# component: universe from 228 pairs, names from 240 pairs, value = (if<<16)|comp
c228 = read_sym(os.path.join(S228, 'component.sym'))
c240 = read_sym(os.path.join(S240, 'component.sym'))
rows, used = [], set()
c_real = c_miss = c_dup = 0
for key in sorted(c228, key=lambda k: (int(k.split(':')[0]), int(k.split(':')[1]))):
    a, b = key.split(':'); packed = (int(a) << 16) | int(b)
    cand = c240.get(key)
    name = None
    if cand and not re.fullmatch(r'interface_\d+:com_\d+', cand):
        if cand in used: c_dup += 1
        else: name = cand; c_real += 1
    else:
        c_miss += 1
    if name is None: name = f'interface_{a}:com_{b}'
    used.add(name); rows.append((name, packed))
tables['component'] = rows
report.append(('component', len(rows), c_real, c_miss, 0, c_dup))

# dbcol: universe from 228 dbcolumn.sym (key 'table:colN'), value = (table<<16)|col
dc228 = read_sym(os.path.join(S228, 'dbcolumn.sym'))
dc240 = read_sym(os.path.join(S240, 'dbcolumn.sym'))
dbt240 = {name: i for i, name in read_sym(os.path.join(S240, 'dbtable.sym')).items()}
rows, used = [], set()
c_real = c_miss = c_dup = 0
dc228_2part = [k for k in dc228 if k.count(':') == 1]
for key in sorted(dc228_2part, key=lambda k: (int(k.split(':')[0]), int(k.split(':')[1]))):
    a, b = key.split(':'); col = int(b); packed = (int(a) << 16) | col
    cand = dc240.get(key)
    name = None
    if cand and not re.fullmatch(r'dbtable_\d+:col\d+', cand):
        if cand in used: c_dup += 1
        else: name = cand; c_real += 1
    else:
        c_miss += 1
    if name is None: name = f'dbtable_{a}:col{col}'
    used.add(name); rows.append((name, packed))
tables['dbcol'] = rows
report.append(('dbcol', len(rows), c_real, c_miss, 0, c_dup))

# ---- emit --------------------------------------------------------------------
os.makedirs(os.path.join(OUT, 'gamevals'), exist_ok=True)
os.makedirs(os.path.join(OUT, 'gamevals-binary'), exist_ok=True)

for t, rows in tables.items():
    with open(os.path.join(OUT, 'gamevals', f'{t}.rscm'), 'w', encoding='utf-8') as f:
        for name, i in rows:
            f.write(f'{name}={i}\n')

def write_dat(path, table_names):
    with open(path, 'wb') as f:
        f.write(struct.pack('>i', len(table_names)))
        for t in table_names:
            tb = t.encode('utf-8')
            f.write(struct.pack('>h', len(tb))); f.write(tb)
            rows = tables[t]
            f.write(struct.pack('>i', len(rows)))
            for name, i in rows:
                eb = f'{name}={i}'.encode('utf-8')
                f.write(struct.pack('>h', len(eb))); f.write(eb)

# Jagex gameval groups only (parity with GamevalDumper output); non-Jagex tables
# (enum/struct/param/stat/midi/synth/category) stay .rscm-only so GameValProvider's
# maxBaseID guard does not block naming vanilla IDs there later.
JAGEX_TABLES = ['obj','npc','inv','varp','varbit','loc','seq','spotanim','dbrow','dbtable','jingle','interface']
write_dat(os.path.join(OUT, 'gamevals-binary', 'gamevals.dat'), JAGEX_TABLES)
write_dat(os.path.join(OUT, 'gamevals-binary', 'gamevals_generated.dat'), ['component','dbcol'])

# round-trip verify (GameValDat.read reimplementation)
def read_dat(path):
    out = {}
    with open(path, 'rb') as f:
        (tc,) = struct.unpack('>i', f.read(4))
        for _ in range(tc):
            (nl,) = struct.unpack('>h', f.read(2)); tname = f.read(nl).decode()
            (ec,) = struct.unpack('>i', f.read(4)); rows = []
            for _ in range(ec):
                (el,) = struct.unpack('>h', f.read(2)); rows.append(f.read(el).decode())
            out[tname] = rows
    return out

rt = read_dat(os.path.join(OUT, 'gamevals-binary', 'gamevals.dat'))
assert list(rt) == JAGEX_TABLES
for t in JAGEX_TABLES:
    assert rt[t] == [f'{n}={i}' for n, i in tables[t]], t
print('round-trip OK:', {t: len(v) for t, v in rt.items()})

with open(os.path.join(OUT, 'REPORT.tsv'), 'w') as f:
    f.write('table\ttotal\treal_240_names\tfallback_no_240_name\tfallback_unsafe\tfallback_dup\n')
    for r in report:
        f.write('\t'.join(map(str, r)) + '\n')
print(open(os.path.join(OUT, 'REPORT.tsv')).read())
