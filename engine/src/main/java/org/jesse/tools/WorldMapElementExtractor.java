package org.jesse.tools;

import org.jesse.game.GameConstants;
import mgi.Indice;
import mgi.types.worldmap.MapElementDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * Extracts information from the world map.
 * @author Kris | 5. march 2018 : 17:12.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WorldMapElementExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(WorldMapElementExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + "mapelement-list.txt")));
			final int len = CollectionUtils.getIndiceSize(Indice.AREA);
			for (int i = 0; i < len; i++) {
				final MapElementDefinitions defs = MapElementDefinitions.get(i);
				if (defs == null)
					continue;
				writer.write("[" + defs.getId() + "]");
				writer.newLine();
				writer.write("Sprite: " + defs.getSpriteId());
				writer.newLine();
				writer.write("Text: " + defs.getText());
				writer.newLine();
				writer.write("Size: " + defs.getTextSize());
				writer.newLine();
				writer.write("Color: " + defs.getColour());
				writer.newLine();
				writer.write("Options: " + Arrays.deepToString(defs.getOptions()));
				writer.newLine();
				writer.write("Option Name: " + defs.getOptionName());
				writer.newLine();
				writer.write("Vertical Align: " + defs.getVerticalAlignment());
				writer.newLine();
				writer.write("Horizontal Align: " + defs.getHorizontalAlignment());
				writer.newLine();
				writer.newLine();
				writer.newLine();
				/*final WorldMapDefinitions defs = WorldMapDefinitions.get(i);
				if (defs == null)
					continue;
				//writer.write(i + " - " + defs.getName() + " | " + defs.getFileName() + ": " + defs.getLocation());
				if (i < len)
					writer.newLine();*/
			}
			writer.flush();
			writer.close();
		} catch (final Exception e) {
            log.error("", e);
		}
	}
	
}
