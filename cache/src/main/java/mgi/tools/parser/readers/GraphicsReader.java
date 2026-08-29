package mgi.tools.parser.readers;

import mgi.tools.parser.TypeParser;
import mgi.tools.parser.TypeReader;
import mgi.types.Definitions;
import mgi.types.config.SpotAnimationDefinition;
import mgi.utilities.ColorUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Tommeh | 14/04/2020 | 22:46
 * @author Jire
 */
public final class GraphicsReader implements TypeReader {

    @Override
    public ArrayList<Definitions> read(final Map<String, Object> properties) throws NoSuchFieldException, IllegalAccessException {
        final ArrayList<Definitions> defs = new ArrayList<>();
        if (properties.containsKey("inherit")) {
            final Object inherit = properties.get("inherit");
            final SpotAnimationDefinition def = SpotAnimationDefinition.get(
                    inherit instanceof Long ? ((Long) inherit).intValue() : (int) inherit);
            defs.add(TypeParser.KRYO.copy(def));
        } else {
            defs.add(new SpotAnimationDefinition());
        }
        for (final Definitions definition : defs) {
            final SpotAnimationDefinition graphicsDef = (SpotAnimationDefinition) definition;
            TypeReader.setFields(graphicsDef, properties);
            if (properties.containsKey("replacementcolours")) {
                @SuppressWarnings("unchecked") final ArrayList<ArrayList<Integer>> list = (ArrayList<ArrayList<Integer>>) properties.get("replacementcolours");
                final short[] replacementColours = new short[list.size()];
                for (int index = 0; index < list.size(); index++) {
                    final ArrayList<Integer> colours = list.get(index);
                    replacementColours[index] = (short) ColorUtils.rgbToHSL16(colours.get(0), colours.get(1), colours.get(2));
                }
                graphicsDef.setRecolorTo(replacementColours);
            }
        }
        return defs;
    }

    @Override
    public String getType() {
        return "graphics";
    }

}
