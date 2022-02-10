package net.basilcam.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CarcassonneApiTest {
    public CarcassonneApi api;
    public CarcassonneHandler handler = mock(CarcassonneHandler.class);

    @BeforeEach
    public void beforeEach() {
        api = new CarcassonneApi();
        api.register(handler);
    }

    @Test
    public void shouldNotAddPlayer_gameIsNotInSetupPhase() {

    }

    @Test
    public void shouldNotAddPlayer_tooManyPlayers() {

    }

    @Test
    public void shouldNotRemovePlayer_gameIsNotInSetupPhase() {

    }

    @Test
    public void shouldRemovePlayer() {

    }

    @Test
    public void shouldNotStartGame_gameIsNotInSetupPhase() {

    }

    @Test
    public void shouldNotStartGame_tooFewPlayers() {

    }

    @Test
    public void shouldNotStartGame_tooManyPlayers() {

    }

    @Test
    public void shouldStartGame_validNumberOfPlayers() {

    }





    @Test
    public void shouldNotTakeTurn_gameIsNotInPlayingPhase() {

    }

    @Test
    public void shouldEndGame_noMoreTiles() {

    }

    @Test
    public void shouldCycleThroughTurnForEachPlayer() {

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