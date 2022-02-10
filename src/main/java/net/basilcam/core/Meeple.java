package net.basilcam.core;

public class Meeple {
    private boolean isPlaced;

    public Meeple() {
        this.isPlaced = false;
    }

    public boolean isPlaced() {
        return this.isPlaced;
    }

    public void placeMeeple() {
        this.isPlaced = true;
    }

    public void removeMeeple() {
        this.isPlaced = false;
    }
}
