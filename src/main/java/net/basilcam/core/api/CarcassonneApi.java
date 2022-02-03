package net.basilcam.core.api;

import net.basilcam.core.*;
import net.basilcam.core.features.CompositeFeatureManager;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;
import net.basilcam.core.tiles.TileStackFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CarcassonneApi {
    private final Board board;
    private final List<Player> players;
    private final List<CarcassonneHandler> handlers;
    private int currentPlayerIndex;
    private Stack<Tile> tileStack;
    private CompositeFeatureManager featureManager;

    private GameState gameState;

    // todo: some of these callbacks are unnecessary

    public CarcassonneApi() {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.tileStack = new Stack<>();
        this.gameState = GameState.SETUP;
        this.featureManager = new CompositeFeatureManager(this.board);
    }

    public void newGame() {
        this.players.clear();
        this.currentPlayerIndex = 0;
        this.board.clear();
        this.tileStack = TileStackFactory.createTileStack();
        this.gameState = GameState.SETUP;
        this.featureManager.clear();
    }

    public void register(CarcassonneHandler handler) {
        this.handlers.add(handler);
    }

    public void addPlayer(String name) {
        if (this.gameState != GameState.SETUP) {
            throw new IllegalStateException("can only add player when in game setup");
        }
        if (this.players.size() >= 5) {
            this.handlers.forEach(handler -> handler.handleError("must have only 2-5 players"));
            return;
        }

        Player player = Player.createPlayer(name);
        this.players.add(player);

        this.handlers.forEach(handler -> handler.playerAdded(player));
    }

    public void removePlayer(Player player) {
        if (this.gameState != GameState.SETUP) {
            throw new IllegalStateException("can only remove player when in game setup");
        }
        if (!this.players.contains(player)) {
            throw new IllegalStateException("attempted to remove player that does not exist");
        }

        this.players.remove(player);
        this.handlers.forEach(handler -> handler.playerRemoved(player));
    }

    public void startGame() {
        if (this.gameState != GameState.SETUP) {
            throw new IllegalStateException("can only start game when in game setup");
        }
        if (this.players.size() < 2 || this.players.size() >= 5) {
            this.handlers.forEach(handler -> handler.handleError("must have 2-5 players"));
            return;
        }

        this.gameState = GameState.PLAYING;
        this.handlers.forEach(CarcassonneHandler::gameStarted);
    }

    public void nextTurn() {
        if (this.gameState != GameState.PLAYING) {
            throw new IllegalStateException("can only take turn when game is running");
        }

        this.currentPlayerIndex++;
        if (this.currentPlayerIndex > this.players.size()) {
            this.currentPlayerIndex = 0;
        }
        this.handlers.forEach(handler -> handler.nextTurn(this.players.get(this.currentPlayerIndex)));

        if (this.tileStack.isEmpty()) {
            this.gameState = GameState.ENDED;
            this.handlers.forEach(CarcassonneHandler::gameEnded);
        }
    }

    public void placeTile(Tile tile, int xPosition, int yPosition) {
        if (this.gameState != GameState.PLAYING) {
            throw new IllegalStateException("can only place tile when game is running");
        }

        if (!PlacementValidator.isValid(this.board, xPosition, yPosition, tile)) {
            // todo: call handler
            return;
        }

        this.board.placeTile(tile, xPosition, yPosition);

        this.featureManager.updateFeatures(tile, xPosition, yPosition);

        scoreFeatures();

        this.handlers.forEach(handler -> handler.tilePlaced(tile, xPosition, yPosition));
    }

    public void placeMeeple(TileSection tileSection, Meeple meeple) {
        if (this.gameState != GameState.PLAYING) {
            throw new IllegalStateException("can only place meeples when game is running");
        }

        if (!PlacementValidator.isValid(tileSection, meeple)) {
            throw new IllegalStateException("tile can not be placed there");
        }

        this.handlers.forEach(handler -> handler.meeplePlaced(tileSection, meeple));
    }

    private void scoreFeatures() {

        // todo

        this.handlers.forEach(handler -> handler.scoreUpdate(null));
    }
}
