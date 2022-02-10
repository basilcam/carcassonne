package net.basilcam.core;

import com.google.common.collect.ImmutableList;
import net.basilcam.core.api.CarcassonneApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private final List<Player> players;
    private final Map<Meeple, Player> meepleToPlayer;

    private int currentPlayerIndex;

    public PlayerManager() {
        this.players = new ArrayList<>();
        this.meepleToPlayer = new HashMap<>();
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }

    public ImmutableList<Player> getPlayers() {
        return ImmutableList.copyOf(this.players);
    }

    public int getNumberPlayers() {
        return this.players.size();
    }

    public void addPlayer(Player player) {
        assert this.players.size() < CarcassonneApi.MAX_PLAYERS;

        this.players.add(player);
        addMeepleToPlayer(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);

        for (Meeple meeple : player.getMeeples()) {
            this.meepleToPlayer.remove(meeple);
        }
    }

    public void nextTurn() {
        if (++this.currentPlayerIndex >= this.players.size()) {
            this.currentPlayerIndex = 0;
        }
    }

    public Player getMeepleOwner(Meeple meeple) {
        return this.meepleToPlayer.get(meeple);
    }

    private void addMeepleToPlayer(Player player) {
        for (Meeple meeple : player.getMeeples()) {
            this.meepleToPlayer.put(meeple, player);
        }
    }
}
