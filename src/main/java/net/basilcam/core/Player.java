package net.basilcam.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private static final int MEEPLE_PER_PLAYER = 7;

    private final String name;
    private int score;
    private Meeple[] meeples;

    public static Player createPlayer(String name) {
        Player player = new Player(name);
        player.createMeeples();
        return player;
    }

    public String getName() {
        return this.name;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return this.score == player.score
                && Objects.equals(this.name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.score);
    }

    private Player(String name) {
        this.name = name;
        this.score = 0;
        this.meeples = new Meeple[MEEPLE_PER_PLAYER];
    }

    private void createMeeples() {
        for (int i = 0; i < MEEPLE_PER_PLAYER; i++) {
            this.meeples[i] = new Meeple(this);
        }
    }
}
