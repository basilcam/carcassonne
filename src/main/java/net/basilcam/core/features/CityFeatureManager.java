package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.tiles.TileSection;
import net.basilcam.core.tiles.TileSectionType;

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

    @Override
    public TileSectionType getTileSectionType() {
        return TileSectionType.CITY;
    }
}
