package net.basilcam.core;

import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;

import java.util.Optional;

public enum PlacementValidator {

    ;

    public static boolean isValid(Board board, int xPosition, int yPosition, Tile tile) {
        if (board.getTile(xPosition, yPosition).isPresent()) {
            return false;
        }

        TileSection topSection = tile.getTopSection();
        Optional<Tile> adjacentTile = board.getTile(xPosition, yPosition + 1);
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getBottomSection();
            if (adjacentSection.getType() != topSection.getType()) {
                return false;
            }
        }

        TileSection leftSection = tile.getLeftSection();
        adjacentTile = board.getTile(xPosition - 1, yPosition);
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getRightSection();
            if (adjacentSection.getType() != leftSection.getType()) {
                return false;
            }
        }

        TileSection bottomSection = tile.getBottomSection();
        adjacentTile = board.getTile(xPosition, yPosition - 1);
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getTopSection();
            if (adjacentSection.getType() != bottomSection.getType()) {
                return false;
            }
        }

        adjacentTile = board.getTile(xPosition + 1, yPosition);
        TileSection rightSection = tile.getRightSection();
        if (adjacentTile.isPresent()) {
            TileSection adjacentSection = adjacentTile.get().getLeftSection();
            if (adjacentSection.getType() != rightSection.getType()) {
                return false;
            }
        }

        // todo: ensure abutting at least one other tile

        return true;
    }

    public static boolean isValid(TileSection tileSection, Meeple meeple) {
        // todo
        return false;
    }
}
