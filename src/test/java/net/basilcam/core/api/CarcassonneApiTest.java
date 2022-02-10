package net.basilcam.core.api;

import net.basilcam.core.Board;
import net.basilcam.core.Player;
import net.basilcam.core.tiles.TestTileManager;
import net.basilcam.core.tiles.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CarcassonneApiTest {
    public CarcassonneApi api;
    public CarcassonneHandler handler = mock(CarcassonneHandler.class);
    public TestTileManager tileManager;

    @BeforeEach
    public void beforeEach() {
        api = new CarcassonneApi();
        api.register(handler);
        // todo: using this to draw tiles won't work since actual tileManager won't have tile section mapping
        // todo: probably just inject it
        tileManager = new TestTileManager(new Board());
    }

    @Test
    public void shouldNotAddPlayer_gameIsNotInSetupPhase() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> api.addPlayer("cam"));

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.ADD_PLAYER_WRONG_PHASE);
    }

    @Test
    public void shouldNotAddPlayer_tooManyPlayers() {
        api.newGame();
        for (int i = 0; i < CarcassonneApi.MAX_PLAYERS; i++) {
            api.addPlayer("cam" + i);
        }

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> api.addPlayer("cam"));

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.ADD_PLAYER_TOO_MANY);
    }

    @Test
    public void shouldNotRemovePlayer_gameIsNotInSetupPhase() {
        api.newGame();
        api.addPlayer("cam");
        Player mina = api.addPlayer("mina");
        api.startGame();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> api.removePlayer(mina));
        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.REMOVE_PLAYER_WRONG_PHASE);
    }

    @Test
    public void shouldRemovePlayer() {
        api.newGame();
        api.addPlayer("cam");
        Player mina = api.addPlayer("mina");

        api.removePlayer(mina);
    }

    @Test
    public void shouldNotStartGame_gameIsNotInSetupPhase() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> api.startGame());
        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.START_GAME_WRONG_PHASE);
    }

    @Test
    public void shouldNotStartGame_tooFewPlayers() {
        api.newGame();
        api.addPlayer("cam");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> api.startGame());
        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.START_GAME_WRONG_PLAYER_COUNT);
    }

    @Test
    public void shouldStartGame_validNumberOfPlayers() {
        api.newGame();
        api.addPlayer("cam");
        api.addPlayer("mina");
        api.startGame();
    }

    @Test
    public void shouldNotTakeTurn_gameIsNotInPlayingPhase() {
        api.newGame();
        api.addPlayer("cam");
        api.addPlayer("mina");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> api.nextTurn());
        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.NEXT_TURN_WRONG_PHASE);
    }

    @Test
    public void shouldCycleThroughTurnForEachPlayer() {
        api.newGame();
        Player cam = api.addPlayer("cam");
        Player mina = api.addPlayer("mina");
        api.startGame();

        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(handler).turnStarted(captor.capture());
        assertThat(captor.getValue()).isEqualTo(cam);

        Tile tile10 = tileManager.drawTileById(10);
        tile10.rotateClockwise();
        assertThat(api.placeTile(tile10, 1, 0)).isTrue();
        api.scoreFeatures();

        api.nextTurn();
        verify(handler, times(2)).turnStarted(captor.capture());
        assertThat(captor.getValue()).isEqualTo(mina);
    }

    @Test
    public void shouldNotPlaceTile_gameNotInPlayingPhase() {

    }

    @Test
    public void shouldNotPlaceTile_tileAlreadyPlacedForTurn() {

    }

    @Test
    public void shouldNotPlaceTile_tilePlacementInvalid() {

    }

    @Test
    public void shouldPlaceTile_previousPlacementInvalid_currentPlacementValid() {

    }





    @Test
    public void shouldNotPlaceMeeple_gameNotInPlayingPhase() {

    }

    @Test
    public void shouldNotPlaceMeeple_meepleAlreadyPlacedForTurn() {

    }

    @Test
    public void shouldNotPlaceMeeple_tileNotJustPlaced() {

    }

    @Test
    public void shouldNotPlaceMeeple_featureAlreadyHasMeeple() {

    }

    @Test
    public void shouldPlaceMeeple() {

    }




    @Test
    public void shouldNotScoreFeatures_gameNotInPlayingPhase() {

    }

    @Test
    public void shouldNotScoreFeatures_tileHasNotBeenPlacedThisTurn() {

    }

    @Test
    public void shouldScoreFeatures() {

    }
}