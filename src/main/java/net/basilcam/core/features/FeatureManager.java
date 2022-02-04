package net.basilcam.core.features;

import net.basilcam.core.tiles.Tile;

import java.util.Collection;

public interface FeatureManager {

    void updateFeatures(Tile tile, int xPosition, int yPosition);

    void clear();

    Collection<? extends Feature> getFeatures();
}
