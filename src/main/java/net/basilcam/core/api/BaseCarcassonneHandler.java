package net.basilcam.core.api;

import net.basilcam.core.Player;

public abstract class BaseCarcassonneHandler implements CarcassonneHandler {
    @Override
    public void gameStarted() {
    }

    @Override
    public void turnStarted(Player player) {
    }

    @Override
    public void scoreUpdate(Player player) {
    }

    @Override
    public void gameEnded() {
    }

    @Override
    public void handleError(String error) {
    }
}
