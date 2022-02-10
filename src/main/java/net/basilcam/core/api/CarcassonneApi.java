package net.basilcam.core.api;

import net.basilcam.core.*;
import net.basilcam.core.features.CompositeFeatureManager;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileManager;
import net.basilcam.core.tiles.TileSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarcassonneApi {
    private static final int MAX_PLAYERS = 5;
    private static final int MIN_PLAYERS = 2;
    private final Board board;
    private final List<Player> players;
    private final List<CarcassonneHandler> handlers;
    private CompositeFeatureManager featureManager;

    private GamePhase gamePhase;
    private int currentPlayerIndex;
    private Optional<TurnState> turnState;
    private TileManager tileManager;

    public CarcassonneApi() {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.tileManager = TileManager.create(this.board);
        this.featureManager = new CompositeFeatureManager(this.board, this.tileManager);
        this.gamePhase = GamePhase.SETUP;
        this.turnState = Optional.empty();
    }

    public void newGame() {
        this.players.clear();
        this.currentPlayerIndex = 0;
        this.board.clear();
        this.gamePhase = GamePhase.SETUP;
        this.featureManager.clear();
        this.turnState = Optional.empty();
        this.tileManager.reset();
    }

    public void register(CarcassonneHandler handler) {
        this.handlers.add(handler);
    }

    public boolean addPlayer(String name) {
        if (this.gamePhase != GamePhase.SETUP) {
            throw new IllegalStateException("can only add player when in game setup");
        }
        if (this.players.size() >= MAX_PLAYERS) {
            this.handlers.forEach(handler -> handler.handleError("must have only 2-5 players"));
            return false;
        }

        Player player = Player.createPlayer(name);
        this.players.add(player);
        return true;
    }

    public void removePlayer(Player player) {
        if (this.gamePhase != GamePhase.SETUP) {
            throw new IllegalStateException("can only remove player when in game setup");
        }
        if (!this.players.contains(player)) {
            throw new IllegalStateException("attempted to remove player that does not exist");
        }

        this.players.remove(player);
    }

    public void startGame() {
        if (this.gamePhase != GamePhase.SETUP) {
            throw new IllegalStateException("can only start game when in game setup");
        }
        if (this.players.size() < MIN_PLAYERS || this.players.size() >= MAX_PLAYERS) {
            this.handlers.forEach(handler -> handler.handleError("must have 2-5 players"));
            return;
        }

        this.gamePhase = GamePhase.PLAYING;
        this.turnState = Optional.of(new TurnState(this.players));
        this.handlers.forEach(CarcassonneHandler::gameStarted);
    }

    public void nextTurn() {
        if (this.gamePhase != GamePhase.PLAYING) {
            throw new IllegalStateException("can only take turn when game is running");
        }
        if (this.turnState.isEmpty() || !this.turnState.get().hasPlacedTile()) {
            throw new IllegalStateException("must place tile before moving to next turn");
        }
        if (!this.tileManager.hasMoreTiles()) {
            this.gamePhase = GamePhase.ENDED;
            this.handlers.forEach(CarcassonneHandler::gameEnded);
            return;
        }

        this.currentPlayerIndex++;
        if (this.currentPlayerIndex > this.players.size()) {
            this.currentPlayerIndex = 0;
        }
        this.turnState.get().nextTurn();
        this.handlers.forEach(handler -> handler.nextTurn(this.turnState.get().getCurrentPlayer()));
    }

    public boolean placeTile(Tile tile, int xPosition, int yPosition) {
        if (this.gamePhase != GamePhase.PLAYING) {
            throw new IllegalStateException("can only place tile when game is running");
        }
        assert this.turnState.isPresent();
        if (this.turnState.get().hasPlacedTile()) {
            throw new IllegalStateException("can only place one tile per turn");
        }
        if (!PlacementValidator.isValid(this.board, xPosition, yPosition, tile)) {
            return false;
        }

        this.turnState.get().placedTile();

        this.board.placeTile(tile, xPosition, yPosition);

        this.featureManager.updateFeatures(tile, xPosition, yPosition);

        return true;
    }

    public boolean placeMeeple(Tile tile, TileSection tileSection, Meeple meeple) {
        if (this.gamePhase != GamePhase.PLAYING) {
            throw new IllegalStateException("can only place meeples when game is running");
        }
        assert this.turnState.isPresent();
        if (this.turnState.get().hasPlacedMeeple()) {
            throw new IllegalStateException("can only place one meeple per turn");
        }
        if (!meeple.getOwner().equals(this.turnState.get().getCurrentPlayer())) {
            throw new IllegalStateException("can only place meeple belonging to player who's turn it is");
        }

        // todo: only place meeple on tile just placed


        if (!this.featureManager.canPlaceMeeple(tile, tileSection)) {
            return false;
        }

        this.turnState.get().placedMeeple();

        tileSection.placeMeeple(meeple);
        meeple.setTileSection(tileSection);

        return true;
    }

    public void scoreFeatures() {
        if (this.gamePhase != GamePhase.PLAYING) {
            throw new IllegalStateException("can only score features when actually playing the game");
        }
        assert this.turnState.isPresent();
        if (!this.turnState.get().hasPlacedTile()) {
            throw new IllegalStateException("can only score features after placing tile");
        }

        this.featureManager.scoreFeatures();

        for (Player player : this.players) {
            this.handlers.forEach(handler -> handler.scoreUpdate(player));
        }
    }
}
