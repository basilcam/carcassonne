package net.basilcam.core.api;

import net.basilcam.core.*;
import net.basilcam.core.features.CompositeFeatureManager;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileManager;
import net.basilcam.core.tiles.TileSection;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarcassonneApi {
    public static final int MAX_PLAYERS = 5;
    public static final int MIN_PLAYERS = 2;

    private final List<CarcassonneHandler> handlers;

    private final List<Player> players;
    private final TileManager tileManager;
    private final Board board;
    private final CompositeFeatureManager featureManager;

    private GamePhase gamePhase;
    private Optional<TurnState> turnState;

    public CarcassonneApi() {
        this(new TileManager());
    }

    @TestOnly
    public CarcassonneApi(TileManager tileManager) {
        this.handlers = new ArrayList<>();
        this.players = new ArrayList<>();
        this.tileManager = tileManager;
        this.board = new Board(this.tileManager.getStartTile());
        this.gamePhase = GamePhase.SETUP;
        this.featureManager = new CompositeFeatureManager(this.board, this.tileManager);
        this.turnState = Optional.empty();
    }

    public void register(CarcassonneHandler handler) {
        this.handlers.add(handler);
    }

    public Player addPlayer(String name) {
        if (this.gamePhase != GamePhase.SETUP) {
            throw new IllegalStateException(ErrorMessages.ADD_PLAYER_WRONG_PHASE);
        }
        if (this.players.size() >= MAX_PLAYERS) {
            throw new IllegalStateException(ErrorMessages.ADD_PLAYER_TOO_MANY);
        }

        Player player = Player.createPlayer(name);
        this.players.add(player);
        return player;
    }

    public void removePlayer(Player player) {
        if (this.gamePhase != GamePhase.SETUP) {
            throw new IllegalStateException(ErrorMessages.REMOVE_PLAYER_WRONG_PHASE);
        }

        this.players.remove(player);
    }

    public void startGame() {
        if (this.gamePhase != GamePhase.SETUP) {
            throw new IllegalStateException(ErrorMessages.START_GAME_WRONG_PHASE);
        }
        if (this.players.size() < MIN_PLAYERS || this.players.size() >= MAX_PLAYERS) {
            throw new IllegalStateException(ErrorMessages.START_GAME_WRONG_PLAYER_COUNT);
        }

        this.gamePhase = GamePhase.PLAYING;
        this.turnState = Optional.of(new TurnState(this.players));
        this.handlers.forEach(CarcassonneHandler::gameStarted);
        this.handlers.forEach(handler -> handler.turnStarted(this.turnState.get().getCurrentPlayer()));
    }

    public void nextTurn() {
        if (this.gamePhase != GamePhase.PLAYING) {
            throw new IllegalStateException(ErrorMessages.NEXT_TURN_WRONG_PHASE);
        }
        assert this.turnState.isPresent();
        if (!this.turnState.get().hasPlacedTile()) {
            throw new IllegalStateException(ErrorMessages.NEXT_TURN_NO_TILE_PLACED);
        }
        if (!this.turnState.get().hasScored()) {
            throw new IllegalStateException(ErrorMessages.NEXT_TURN_NOT_SCORED);
        }
        if (!this.tileManager.hasMoreTiles()) {
            this.gamePhase = GamePhase.ENDED;
            this.handlers.forEach(CarcassonneHandler::gameEnded);
            return;
        }

        this.turnState.get().nextTurn();
        this.handlers.forEach(handler -> handler.turnStarted(this.turnState.get().getCurrentPlayer()));
    }

    public boolean placeTile(Tile tile, int xPosition, int yPosition) {
        if (this.gamePhase != GamePhase.PLAYING) {
            throw new IllegalStateException(ErrorMessages.PLACE_TILE_WRONG_PHASE);
        }
        assert this.turnState.isPresent();
        if (this.turnState.get().hasPlacedTile()) {
            throw new IllegalStateException(ErrorMessages.PLACE_TILE_ALREADY_PLACED);
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
            throw new IllegalStateException(ErrorMessages.PLACE_MEEPLE_WRONG_PHASE);
        }
        assert this.turnState.isPresent();
        if (this.turnState.get().hasPlacedMeeple()) {
            throw new IllegalStateException(ErrorMessages.PLACE_MEEPLE_ALREADY_PLACED);
        }
        if (this.turnState.get().hasScored()) {
            throw new IllegalStateException(ErrorMessages.PLACE_MEEPLE_ALREADY_SCORED);
        }
        if (!meeple.getOwner().equals(this.turnState.get().getCurrentPlayer())) {
            throw new IllegalStateException("can only place meeple belonging to player who's turn it is");
            // todo: i don't like that the meeple is passed in. should just grab it from the current player
            // throw an error if there are no more meeples
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
            throw new IllegalStateException(ErrorMessages.SCORE_WRONG_PHASE);
        }
        assert this.turnState.isPresent();
        if (!this.turnState.get().hasPlacedTile()) {
            throw new IllegalStateException(ErrorMessages.SCORE_NO_TILE_PLACED);
        }

        this.featureManager.scoreFeatures();

        this.turnState.get().scored();

        for (Player player : this.players) {
            this.handlers.forEach(handler -> handler.scoreUpdate(player));
        }
    }
}
