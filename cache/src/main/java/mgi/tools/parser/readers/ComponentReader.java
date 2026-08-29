package mgi.tools.parser.readers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.tools.parser.TypeProperty;
import mgi.tools.parser.TypeReader;
import mgi.types.Definitions;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.ComponentType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tommeh | 01/02/2020 | 16:59
 * @author Jire
 */
public final class ComponentReader implements TypeReader {

    private static final Int2ObjectOpenHashMap<Object2ObjectOpenHashMap<String, ComponentDefinitions>> namedComponents = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectOpenHashMap<List<ComponentDefinitions>> unnamedComponents = new Int2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<String, Object2ObjectOpenHashMap<String, ComponentDefinitions>> namedInterfaces = new Object2ObjectOpenHashMap<>();
    private static final Map<String, String> HOOKS = ImmutableMap.<String, String>builder().put("onLoadListener", "onload").put("onMouseOverListener", "onmouseover").put("onMouseLeaveListener", "onmouseleave").put("onTargetLeaveListener", "onttargetleave").put("onTargetEnterListener", "onttargetenter").put("onVarTransmitListener", "onvartransmit").put("onInvTransmitListener", "oninvtransmit").put("onStatTransmitListener", "onstattransmit").put("onTimerListener", "ontimer").put("onOpListener", "onop").put("onMouseRepeatListener", "onmouserepeat").put("onClickListener", "onclick").put("onClickRepeatListener", "onclickrepeat").put("onReleaseListener", "onrelease").put("onHoldListener", "onhold").put("onDragListener", "ondrag").put("onDragCompleteListener", "ondragcomplete").put("onScrollWheelListener", "onscrollwheel").build();

    @Override
    public ArrayList<Definitions> read(final ObjectMapper mapper, final JsonNode tree) throws NoSuchFieldException, IllegalAccessException, CloneNotSupportedException {
        final ArrayList<Definitions> defs = new ArrayList<>();
        final int id = tree.has("id") ? tree.get("id").asInt(-1) : -1;
        System.out.println("Creating interface toml [" + id + "]");
        final String name = tree.has("name") ? tree.get("name").asText("") : "";
        if (tree.has("id")) {
            final ArrayList<ComponentDefinitions> interfaceComponents = new ArrayList<>();
            final ArrayList<JsonNode> subTomls = getComponents(tree);
            final ComponentDefinitions groupComponent = getComponent(mapper, subTomls.getFirst(), id);
            groupComponent.setInterfaceId(id);
            interfaceComponents.add(groupComponent);
            int componentId = 1;
            for (final JsonNode t : subTomls.subList(1, subTomls.size())) {
                final ComponentDefinitions component = getComponent(mapper, t, id);
                component.setComponentId(componentId++);
                interfaceComponents.add(component);
            }
            for (final ComponentDefinitions component : interfaceComponents) {
                applyHooks(component);
                if (!name.isEmpty()) {
                    namedInterfaces.computeIfAbsent(name, map -> new Object2ObjectOpenHashMap<>()).put(component.getName(), component);
                }
            }
            groupComponent.getChildren().addAll(interfaceComponents.subList(1, interfaceComponents.size()));
            defs.add(groupComponent);
        } else {
            final ArrayList<JsonNode> subTomls = getComponents(tree);
            for (final JsonNode subToml : subTomls) {
                final ComponentDefinitions component = getComponent(mapper, subToml, id);
                if (component == null) {
                    continue;
                }
                applyHooks(component);
                defs.add(component);
                ComponentDefinitions.add(component);
            }
        }
        return defs;
    }

    private int getHighestComponentId(final int interfaceId) {
        final ArrayList<ComponentDefinitions> components = new ArrayList<>();
        final List<ComponentDefinitions> unnamedComponents = ComponentReader.unnamedComponents.get(interfaceId);
        if (unnamedComponents != null) {
            components.addAll(unnamedComponents);
        }
        final Object2ObjectOpenHashMap<String, ComponentDefinitions> namedComponents = ComponentReader.namedComponents.get(interfaceId);
        if (namedComponents != null) {
            components.addAll(namedComponents.values());
        }
        components.addAll(ComponentDefinitions.getComponents(interfaceId));
        if (components == null) {
            return -1;
        }
        int componentId = -1;
        for (final ComponentDefinitions component : components) {
            if (component.getInterfaceId() != interfaceId) {
                continue;
            }
            if (componentId < component.getComponentId()) {
                componentId = component.getComponentId();
            }
        }
        return componentId;
    }

    private ArrayList<JsonNode> getComponents(final JsonNode tree) {
        final ArrayList<JsonNode> components = new ArrayList<>();
        for (Map.Entry<String, JsonNode> entry : tree.properties()) {
            if (entry.getKey().equals("id") || entry.getKey().equals("name")) {
                continue;
            }
            final JsonNode value = entry.getValue();
            if (value.isArray()) {
                for (final JsonNode node : value) {
                    components.add(node);
                }
            } else {
                components.add(value);
            }
        }
        return components;
    }

    private ComponentDefinitions getComponent(final ObjectMapper mapper, final JsonNode toml, int id) throws CloneNotSupportedException, NoSuchFieldException, IllegalAccessException {
        ComponentDefinitions component;
        final String name = toml.has("name") ? toml.get("name").asText("") : "";
        if (toml.has("inherit")) {
            final String inherit = toml.get("inherit").asText("");
            final String[] split = inherit.split(":");
            final String inheritedInterface = split[0];
            final String inheritedComponent = split[1];
            final int interfaceId = (int) Double.parseDouble(inheritedInterface);
            final int componentId = Integer.parseInt(inheritedComponent);
            component = ComponentDefinitions.get(interfaceId, componentId).clone();
        } else {
            component = new ComponentDefinitions();
            component.setIf3(true);
        }
        final Map<String, Object> properties =
                mapper.convertValue(toml, new TypeReference<>() {
                });
        TypeReader.setFields(component, properties);
        if (toml.has("type")) {
            final String typeIdentifier = toml.get("type").asText("");
            final ComponentType type = ComponentType.get(typeIdentifier);
            if (type == null) {
                throw new IllegalStateException("Unknown component type: " + typeIdentifier);
            }
            if (type.equals(ComponentType.TEXT)) {
                //default properties for text components
                component.setColor("ff981f");
                component.setTextShadowed(true);
            }
            component.setType(type.getId());
        }
        if (toml.has("id")) {
            id = toml.get("id").intValue();
        }
        if (toml.has("parentid")) {
            final int parentId = toml.get("parentid").asInt(0);
            component.setParentId(component.getInterfaceId() << 16 | parentId);
        }
        if (toml.has("layer") && id > -1) {
            final String layer = toml.get("layer").asText("");
            final Object2ObjectOpenHashMap<String, ComponentDefinitions> components = namedComponents.get(id);
            final ComponentDefinitions parentComponent = components.get(layer);
            if (parentComponent == null) {
                throw new RuntimeException("Parent component: " + layer + " doesn't exist for interface " + id);
            }
            component.setInterfaceId(parentComponent.getInterfaceId());
            component.setComponentId(getHighestComponentId(parentComponent.getInterfaceId()) + 1);
            component.setParentId(parentComponent.getInterfaceId() << 16 | parentComponent.getComponentId());
        } else if (toml.has("generatecomponentid") && toml.get("generatecomponentid").asBoolean()) {
            component.setInterfaceId(id);
            component.setComponentId(getHighestComponentId(id) + 1);
            component.setParentId(component.getParentId());
        }
        if (toml.has("color")) {
            final String color = toml.get("color").asText("");
            component.setColor(color);
        }
        if (toml.has("shadowcolor")) {
            final String color = toml.get("shadowcolor").asText("");
            component.setShadowColor(color);
        }
        for (final TypeProperty property : TypeProperty.values) {
            final String identifier = property.getIdentifier();
            if (!toml.has(identifier)) {
                continue;
            }
            if (property.toString().startsWith("OP_")) {
                final int index = Integer.parseInt(identifier.substring(2)) - 1;
                component.setOption(index, toml.get(identifier).asText(""));
            }
        }
        if (component.getHooks() == null) {
            component.setHooks(new HashMap<>());
        }
        for (final String hook : HOOKS.values()) {
            if (toml.has(hook)) {
                final JsonNode hookNode = toml.get(hook);
                final ArrayList<Object> list = new ArrayList<>(hookNode.size());
                for (final JsonNode value : hookNode) {
                    final Object valueObject = mapper.convertValue(value, new TypeReference<>() {});
                    list.add(valueObject);
                }
                final Object[] arguments = list.toArray();
                component.getHooks().put(hook, arguments);
            }
        }
        if (id > -1) {
            if (name.isEmpty()) {
                unnamedComponents.computeIfAbsent(id, list -> new ArrayList<>()).add(component);
            } else {
                namedComponents.computeIfAbsent(id, map -> new Object2ObjectOpenHashMap<>()).put(name, component);
            }
        }
        return component;
    }

    private void applyHooks(final ComponentDefinitions component) throws IllegalAccessException {
        //TODO support all hooks
        final Map<String, Object[]> hooks = component.getHooks();
        if (hooks == null || hooks.isEmpty()) {
            return;
        }
        final Object2ObjectOpenHashMap<String, ComponentDefinitions> components = namedComponents.get(component.getInterfaceId());
        final Class<? extends ComponentDefinitions> clazz = component.getClass();
        for (final Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            if (HOOKS.containsKey(field.getName())) {
                final String identifier = HOOKS.get(field.getName());
                if (!hooks.containsKey(identifier)) {
                    continue;
                }
                final ArrayList<Object> transformed = new ArrayList<>();
                final Object[] arguments = hooks.get(identifier);
                for (Object arg : arguments) {
                    if (arg instanceof String) {
                        final String value = (String) arg;
                        if (value.startsWith("component:")) {
                            final String componentName = value.split(":")[1];
                            if (componentName.equals("self")) {
                                arg = -2147483645;
                            } else {
                                final ComponentDefinitions referredComponent = components.get(componentName);
                                if (referredComponent != null) {
                                    arg = referredComponent.getInterfaceId() << 16 | referredComponent.getComponentId();
                                }
                            }
                        } else if (value.startsWith("color:")) {
                            final String color = value.split(":")[1];
                            arg = Integer.parseInt(color, 16);
                        }
                    } else if (arg instanceof Long) {
                        arg = ((Long) arg).intValue();
                    }
                    transformed.add(arg);
                }
                field.set(component, transformed.toArray());
            }
        }
    }

    @Override
    public String getType() {
        return "component";
    }

}
