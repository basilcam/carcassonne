package net.basilcam.core;

import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;

import java.util.Optional;

public enum PlacementValidator {

    ;

    public static boolean isValid(Board board, int xpos, int ypos, Tile tile) {

        TileSection topSection = tile.getTopSection();
        Optional<Tile> adjacentTile = board.getTile(xpos, ypos + 1);
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getBottomSection();
            if (adjacentSection.getType() != topSection.getType()) {
                return false;
            }
        }

        TileSection leftSection = tile.getLeftSection();
        adjacentTile = board.getTile(xpos - 1, ypos);
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getRightSection();
            if (adjacentSection.getType() != leftSection.getType()) {
                return false;
            }
        }

        TileSection bottomSection = tile.getBottomSection();
        adjacentTile = board.getTile(xpos, ypos - 1);
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getTopSection();
            if (adjacentSection.getType() != bottomSection.getType()) {
                return false;
            }
        }

        TileSection rightSection = tile.getRightSection();
        adjacentTile = board.getTile(xpos + 1, ypos);
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getLeftSection();
            if (adjacentSection.getType() != rightSection.getType()) {
                return false;
            }
        }

        return false;
    }

    public static boolean isValid(TileSection tileSection, Meeple meeple) {
        // todo
        return false;
    }
}
