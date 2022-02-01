package net.basilcam.core.features;

import net.basilcam.core.tiles.Tile;

public interface FeatureManager {

    void updateFeatures(Tile tile, int xPosition, int yPosition);

    void clear();
}
