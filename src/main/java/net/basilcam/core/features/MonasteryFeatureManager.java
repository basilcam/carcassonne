package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.tiles.Tile;

import java.util.Collection;
import java.util.Collections;

public class MonasteryFeatureManager implements FeatureManager {
    private final Board board;

    public MonasteryFeatureManager(Board board) {
        this.board = board;
    }

    @Override
    public void updateFeatures(Tile tile, int xPosition, int yPosition) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Collection<? extends Feature> getFeatures() {
        return Collections.emptyList();
    }
}
