package net.basilcam.core.features;

import net.basilcam.core.Meeple;
import net.basilcam.core.tiles.TileSection;
import net.basilcam.core.tiles.TileSectionType;

public interface Feature {
    boolean isComplete();
    TileSectionType getType();
}
