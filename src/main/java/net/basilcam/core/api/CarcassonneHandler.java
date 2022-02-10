package net.basilcam.core.api;

import net.basilcam.core.Player;

public interface CarcassonneHandler {
    void gameStarted();

    void turnStarted(Player player);

    void scoreUpdate(Player player);

    void gameEnded();

    void handleError(String error);
}
