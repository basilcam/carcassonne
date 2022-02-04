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

        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.CITY
                        && !feature.isComplete(),
                "city"));
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD
                        && !feature.isComplete(),
                "road"));
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
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.CITY
                        && feature.isComplete(),
                "city"));
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD && !feature.isComplete(),
                "road"));
    }

    @Test
    public void shouldUpdateFeatures_tilePlacedBesideTwoTiles() {
        Tile tile15 = TileStackFactory.getTile(15);
        Tile tile11 = TileStackFactory.getTile(11);
        tile11.rotateClockwise();
        tile11.rotateClockwise();
        Tile tile20 = TileStackFactory.getTile(20);
        tile20.rotateClockwise();
        tile20.rotateClockwise();
        tile20.rotateClockwise();

        placeTileAndUpdate(tile15, 1, 0);

        Collection<? extends Feature> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(2);
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.CITY
                        && !feature.isComplete(),
                "city"));
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD
                        && !feature.isComplete(),
                "road"));

        placeTileAndUpdate(tile11, 0, 1);

        features = this.featureManager.getFeatures();
        assertThat(features).hasSize(2);
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.CITY
                        && !feature.isComplete(),
                "city"));
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD
                        && !feature.isComplete(),
                "road"));

        placeTileAndUpdate(tile20, 1, 1);

        features = this.featureManager.getFeatures();
        assertThat(features).hasSize(2);
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.CITY
                        && feature.isComplete(),
                "city"));
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD
                        && !feature.isComplete(),
                "road"));
    }

    @Test
    public void shouldUpdateFeatures_circleRoad() {
        Tile tile15 = TileStackFactory.getTile(15);
        Tile tile2 = TileStackFactory.getTile(2);
        Tile tile10 = TileStackFactory.getTile(10);
        tile10.rotateClockwise();
        Tile tile7 = TileStackFactory.getTile(7);
        tile7.rotateClockwise();
        tile7.rotateClockwise();
        Tile tile14 = TileStackFactory.getTile(14);

        placeTileAndUpdate(tile15, 1, 0);
        placeTileAndUpdate(tile2, 1, -1);
        placeTileAndUpdate(tile10, 0, -1);
        placeTileAndUpdate(tile7, -1, -1);
        placeTileAndUpdate(tile14, -1, 0);

        Collection<? extends Feature> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(7);

        assertThat(features).areExactly(2, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD
                && feature.isComplete(),
                "road"));
        assertThat(features).areExactly(3, new Condition<>(
                feature -> feature.getType() == TileSectionType.ROAD
                        && !feature.isComplete(),
                "road"));
        assertThat(features).areExactly(2, new Condition<>(
                feature -> feature.getType() == TileSectionType.CITY
                && !feature.isComplete(),
                "city"));
    }

    private void placeTileAndUpdate(Tile tile, int xPosition, int yPosition) {
        assertThat(PlacementValidator.isValid(this.board, xPosition, yPosition, tile)).isTrue();
        this.board.placeTile(tile, xPosition, yPosition);
        this.featureManager.updateFeatures(tile, xPosition, yPosition);
    }
}