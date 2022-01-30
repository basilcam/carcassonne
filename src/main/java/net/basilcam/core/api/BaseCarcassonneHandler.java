package net.basilcam.core.api;

import net.basilcam.core.Meeple;
import net.basilcam.core.Player;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;

public abstract class BaseCarcassonneHandler implements CarcassonneHandler {
    @Override
    public void playerAdded(Player player) {
    }

    @Override
    public void playerRemoved(Player player) {
    }

    @Override
    public void nextTurn(Player player) {
    }

    @Override
    public void scoreUpdate(Player player) {
    }

    @Override
    public void tilePlaced(Tile tile, int xPosition, int yPosition) {
    }

    @Override
    public void meeplePlaced(TileSection tileSection, Meeple meeple) {
    }

    @Override
    public void gameEnded() {
    }
}
