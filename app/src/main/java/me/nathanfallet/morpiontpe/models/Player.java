package me.nathanfallet.morpiontpe.models;

public abstract class Player {

    public Sign sign;

    public Player(Sign sign) {
        this.sign = sign;
    }

    abstract public void play(Game game, GamePlayCallback callback);

}
