package net.basilcam.core.features;

import com.google.common.collect.ImmutableMap;
import net.basilcam.core.Board;
import net.basilcam.core.Pair;
import net.basilcam.core.PlacementValidator;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSectionType;
import net.basilcam.core.tiles.TileStackFactory;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class MonasteryFeatureManagerTest {
    private Board board;
    private MonasteryFeatureManager featureManager;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.featureManager = new MonasteryFeatureManager(this.board);
    }

    @Test
    public void shouldInitiallyHaveNoFeatures() {
        assertThat(this.featureManager.getFeatures()).isEmpty();
    }

    @Test
    public void shouldAddFeatureWhenPlacingMonastery() {
        Tile tile18 = TileStackFactory.getTileById(18);
        tile18.rotateClockwise();

        placeTileAndUpdate(tile18, 1, 0);

        Collection<? extends Feature> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(1);

        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.MONASTERY
                        && !feature.isComplete(),
                "monastery"));
    }

    @Test
    public void shouldCompleteFeature() {
        Tile tile1 = TileStackFactory.getTileById(1);
        placeTileAndUpdate(tile1, 0, 1);

        Tile tile20 = TileStackFactory.getTileById(20);
        tile20.rotateClockwise();
        tile20.rotateClockwise();
        tile20.rotateClockwise();
        placeTileAndUpdate(tile20, 1, 1);

        Tile tile10 = TileStackFactory.getTileById(10);
        placeTileAndUpdate(tile10, 2, 1);

        Tile tile15 = TileStackFactory.getTileById(15);
        tile15.rotateClockwise();
        tile15.rotateClockwise();
        placeTileAndUpdate(tile15, 2, 0);

        Tile tile10_2 = TileStackFactory.getTileById(10);
        tile10_2.rotateClockwise();
        placeTileAndUpdate(tile10_2, 0, -1);

        Tile tile10_3 = TileStackFactory.getTileById(10);
        tile10_3.rotateClockwise();
        placeTileAndUpdate(tile10_3, 1, -1);

        Tile tile10_4 = TileStackFactory.getTileById(10);
        tile10_4.rotateClockwise();
        placeTileAndUpdate(tile10_4, 2, -1);

        Collection<? extends Feature> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(0);

        Tile tile18 = TileStackFactory.getTileById(18);
        tile18.rotateClockwise();

        placeTileAndUpdate(tile18, 1, 0);
        features = this.featureManager.getFeatures();
        assertThat(features).hasSize(1);
        assertThat(features).areExactly(1, new Condition<>(
                feature -> feature.getType() == TileSectionType.MONASTERY
                        && feature.isComplete(),
                "monastery"));
    }

    @Test
    public void shouldConvertTileCoordinatesToIndices() {

        ImmutableMap<Pair<Integer>, Pair<Integer>> offsetXYToIndicesIJ = new ImmutableMap.Builder<Pair<Integer>, Pair<Integer>>()
                .put(new Pair<>(-1, 1), new Pair<>(0, 0))
                .put(new Pair<>(0, 1), new Pair<>(0, 1))
                .put(new Pair<>(1, 1), new Pair<>(0, 2))
                .put(new Pair<>(-1, 0), new Pair<>(1, 0))
                .put(new Pair<>(0, 0), new Pair<>(1, 1))
                .put(new Pair<>(1, 0), new Pair<>(1, 2))
                .put(new Pair<>(-1, -1), new Pair<>(2, 0))
                .put(new Pair<>(0, -1), new Pair<>(2, 1))
                .put(new Pair<>(1, -1), new Pair<>(2, 2))
                .build();

        for (Map.Entry<Pair<Integer>, Pair<Integer>> entry : offsetXYToIndicesIJ.entrySet()) {
            Pair<Integer> tileOffsetFromMonasteryXY = entry.getKey();
            Pair<Integer> expectedTileIndicesIJ = entry.getValue();
            int xOffset = new Random().nextInt();
            int yOffset = new Random().nextInt();

            int monasteryX = xOffset;
            int monasteryY = yOffset;
            int tileX = xOffset + tileOffsetFromMonasteryXY.getFirst();
            int tileY = yOffset + tileOffsetFromMonasteryXY.getSecond();

            Pair<Integer> indices = MonasteryFeatureManager.convertPositionsToIndices(new Pair<>(monasteryX, monasteryY),
                    new Pair<>(tileX, tileY));

            assertThat(indices.getFirst()).isEqualTo(expectedTileIndicesIJ.getFirst());
            assertThat(indices.getSecond()).isEqualTo(expectedTileIndicesIJ.getSecond());
        }
    }

    private void placeTileAndUpdate(Tile tile, int xPosition, int yPosition) {
        assertThat(PlacementValidator.isValid(this.board, xPosition, yPosition, tile)).isTrue();
        this.board.placeTile(tile, xPosition, yPosition);
        this.featureManager.updateFeatures(tile, xPosition, yPosition);
    }
}