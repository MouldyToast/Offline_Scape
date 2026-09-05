# OR2 Migration — STATE

**This file is the continuity anchor for the mgi.types → OR2 migration.**
Every session that completes migration work — planning or execution — updates this
file in the same commit as its work. Any fresh session reads this file plus
`PLAN_or2_definition_swap.md` and knows exactly where things stand. Specs and the
open-items ledger live in the PLAN (single source of truth); this file only tracks
position and next action.

## Position

- [ ] **Standalone fixes** (PLAN §5): op3/op43 encoder, NPC op122/123, 3g heap,
      403-missing-structs diagnosis — *Claude Code session 1*
- [ ] **Tier 1 — Struct** (PLAN §6.1, + APPENDIX_tier1_struct_sites.txt) —
      *Claude Code session 2*
- [ ] **Tier 1 closing step**: grep confirms zero server-module StructDefinitions
      readers → remove struct from `Definitions.serverLowPriorityDefinitions`
- [ ] **Tier 2 — Enum + Enums wrapper** — *needs a planning session first*
      (design decision: re-point the `Enums` object wholesale vs replace; 48 wrapper
      files + 49 direct importers). Planning session produces §6.2 in the PLAN.
- [ ] **Tier 3 — SpotAnim / Varbit / Inventory / Animation / Object** — near-pure
      swaps; plan then execute
- [ ] **Tier 4 — NPC** — coordinate with boss-migration Phases 5B–5F (see project
      phase docs); plan alongside those sessions
- [ ] **Tier 5 — Item** (188 files) — GATED on: osrsbox JSON deletion (PLAN §3) and
      Player field extraction Phases A/B (PLAN_player_field_extraction.md)
- [ ] **Tier 6 — Component + endgame cleanup** (mgi.types removal from server modules)

**NEXT ACTION:** run Claude Code session 1 (standalone fixes).

## Session protocol

**To start any migration session (planning or execution), paste this:**

> Read `docs/or2-migration/STATE.md`, then `docs/or2-migration/PLAN_or2_definition_swap.md`.
> Perform the NEXT ACTION. If it is an execution item with an existing spec, execute
> the spec exactly, respecting its non-goals; stop and ask if anything is ambiguous.
> If it is a planning item, investigate and write the tier's executable spec into the
> PLAN as a new §6.x, leaving no open questions for execution. Before your final
> commit: tick the completed item here, set the new NEXT ACTION, and record any new
> open items in the PLAN's ledger — the PLAN's ledger is the only ledger.

Rules that keep this working:
1. STATE.md and the PLAN are updated **in the same commit** as the work — never "later".
2. Planning happens in the Claude Project (web); execution in Claude Code. Both use
   the same paste-prompt above.
3. When a tier's spec changes an earlier decision, the PLAN is corrected immediately
   (stale plans are worse than no plans).
4. When the Project-knowledge copy of the PLAN drifts from the repo copy, the repo
   copy wins; refresh the Project copy after each tier.

## Log

- 2026-09-05 — Investigation complete (Turns 1–2 web session A; Turns A–C web
  session B). PLAN + appendix + gameval artifacts committed. State file created.
