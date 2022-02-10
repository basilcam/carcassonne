package net.basilcam.core.features;

import com.google.common.collect.Lists;
import net.basilcam.core.Board;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileManager;
import net.basilcam.core.tiles.TileSection;
import net.basilcam.core.tiles.TileSectionType;

import java.util.*;

public class CompositeFeatureManager implements FeatureManager {
    private final List<FeatureManager> featureManagers;

    public CompositeFeatureManager(Board board, TileManager tileManager) {
        this.featureManagers = Lists.newArrayList(
                new GraphFeatureManager(board, tileManager),
                new MonasteryFeatureManager(board));
    }

    @Override
    public void updateFeatures(Tile tile, int xPosition, int yPosition) {
        for (FeatureManager featureManager : featureManagers) {
            featureManager.updateFeatures(tile, xPosition, yPosition);
        }
    }

    @Override
    public void reset() {
        for (FeatureManager featureManager : featureManagers) {
            featureManager.reset();
        }
    }

    @Override
    public Collection<? extends Feature> getFeatures() {
        List<Feature> features = new ArrayList<>();
        for (FeatureManager featureManager : featureManagers) {
            features.addAll(featureManager.getFeatures());
        }
        return features;
    }

    @Override
    public boolean canPlaceMeeple(Tile tile, TileSection section) {
        // todo: improve this. too explicit. check with each feature manager instead
        if (section.getType() != TileSectionType.CITY
                && section.getType() != TileSectionType.ROAD
                && section.getType() != TileSectionType.MONASTERY) {
            return false;
        }

        if (section.getMeeple().isPresent()) {
            return false;
        }

        for (TileSection anotherSection : tile.getSections()) {
            if (anotherSection.getMeeple().isPresent()) {
                return false;
            }
        }

        for (FeatureManager featureManager : this.featureManagers) {
            if (!featureManager.canPlaceMeeple(tile, section)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void scoreFeatures() {
        this.featureManagers.forEach(FeatureManager::scoreFeatures);
    }
}
