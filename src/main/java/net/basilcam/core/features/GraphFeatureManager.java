package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;

import java.util.Optional;

public abstract class GraphFeatureManager<T extends Feature> implements FeatureManager {
    private final Board board;

    GraphFeatureManager(Board board) {
        this.board = board;
    }

    @Override
    public void updateFeatures(Tile tile, int xPosition, int yPosition) {
        TileSection topSection = tile.getTopSection();
        Optional<Tile> abuttingTile = this.board.getTile(xPosition, yPosition + 1);

        TileSection leftSection = tile.getLeftSection();

        TileSection bottomSection = tile.getBottomSection();

        TileSection rightSection = tile.getRightSection();

        // todo: merge
    }

    @Override
    public void clear() {

    }
}
