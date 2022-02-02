package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.tiles.TileSection;

public class CityFeatureManager extends GraphFeatureManager<CityFeature> {
    CityFeatureManager(Board board) {
        super(board);
    }

    @Override
    public CityFeature createFeature(TileSection tileSection) {
        return new CityFeature(tileSection);
    }

    @Override
    public CityFeature createEmptyFeature() {
        return new CityFeature();
    }
}
