package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.PlacementValidator;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSectionType;
import net.basilcam.core.tiles.TileStackFactory;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeFeatureManagerTest {
    private Board board;
    private CompositeFeatureManager featureManager;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.featureManager = new CompositeFeatureManager(board);
    }

    @Test
    public void shouldUpdateFeaturesForStartTile() {
        Collection<? extends Feature> features = this.featureManager.getFeatures();

        assertThat(features).hasSize(2); // city, road
        assertThat(features).allMatch(feature -> !feature.isComplete());
        assertThat(features.stream().map(Feature::getType).collect(Collectors.toList()))
                .containsExactlyInAnyOrder(TileSectionType.CITY, TileSectionType.ROAD);
    }

    @Test
    public void shouldUpdateFeatures_tilePlacedAboveStartTile() {
        Tile tile = TileStackFactory.getTile(20);
        tile.rotateClockwise();
        tile.rotateClockwise();

        assertThat(PlacementValidator.isValid(this.board, 0, 1, tile)).isTrue();
        this.board.placeTile(tile, 0, 1);

        this.featureManager.updateFeatures(tile, 0, 1);

        Collection<? extends Feature> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(2);
        assertThat(features.stream().map(Feature::getType).collect(Collectors.toList()))
                .containsExactlyInAnyOrder(TileSectionType.CITY, TileSectionType.ROAD);

        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.CITY
                        && feature.isComplete(),
                "city"));
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD && !feature.isComplete(),
                "road"));
    }
}