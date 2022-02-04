package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.PlacementValidator;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileStackFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class GraphFeatureManagerTest<T extends GraphFeature> {
    private Board board;
    private GraphFeatureManager<T> featureManager;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.featureManager = createFeatureManager(this.board);
    }

    public abstract GraphFeatureManager<T> createFeatureManager(Board board);

    @Test
    public void shouldUpdateFeaturesForStartTile() {
        Collection<T> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(3); // city, road, and field
        assertThat(features).allMatch(feature -> !feature.isComplete());

        // todo: not done yet
    }

    @Test
    public void test() {
        Tile tile = TileStackFactory.getTile(20);
        tile.rotateClockwise();
        tile.rotateClockwise();

        assertThat(PlacementValidator.isValid(this.board, 0, 1, tile)).isTrue();
        this.board.placeTile(tile, 0, 1);

        this.featureManager.updateFeatures(tile, 0, 1);

        Collection<T> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(3);

        // todo: not done yet
    }
}