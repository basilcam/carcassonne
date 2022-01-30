package net.basilcam.core.api;

import net.basilcam.core.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CarcassonneApiTest {
    public CarcassonneApi api;
    public CarcassonneHandler handler = mock(CarcassonneHandler.class);

    @BeforeEach
    public void beforeEach() {
        api = new CarcassonneApi();
        api.register(handler);
    }

    @Test
    public void addPlayerShouldCallHandler() {
        String playerName = "cam";
        api.addPlayer(playerName);

        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(handler).playerAdded(playerArgumentCaptor.capture());
        assertThat(playerArgumentCaptor.getValue().getName()).isEqualTo(playerName);
        assertThat(playerArgumentCaptor.getValue().getScore()).isEqualTo(0);
    }

    @Test
    public void removePlayerShouldCallHandler() {
        String playerName = "cam";
        api.addPlayer(playerName);

        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(handler).playerAdded(playerArgumentCaptor.capture());
        Player player = playerArgumentCaptor.getValue();

        api.removePlayer(player);
        verify(handler).playerRemoved(eq(player));
    }

}