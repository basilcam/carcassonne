package net.basilcam.core.api;

import net.basilcam.core.Meeple;
import net.basilcam.core.Player;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;

public interface CarcassonneHandler {
    // todo: handlers for various updates to game state. to be implemented and registered by gui

    void gameStarted();

    void playerAdded(Player player);

    void playerRemoved(Player player);

    void nextTurn(Player player);

    void scoreUpdate(Player player);

    void tilePlaced(Tile tile, int xPosition, int yPosition);

    void meeplePlaced(TileSection tileSection, Meeple meeple);

    void gameEnded();

    void handleError(String error);
}
