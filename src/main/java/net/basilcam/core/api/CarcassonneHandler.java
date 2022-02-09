package net.basilcam.core.api;

import net.basilcam.core.Player;

public interface CarcassonneHandler {
    void gameStarted();

    void nextTurn(Player player);

    void scoreUpdate(Player player);

    void gameEnded();

    void handleError(String error);
}
