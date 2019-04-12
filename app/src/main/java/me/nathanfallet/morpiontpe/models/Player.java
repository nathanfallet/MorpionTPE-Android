package me.nathanfallet.morpiontpe.models;

public abstract class Player {

    public Sign sign;

    abstract public void play(Game game, GamePlayCallback callback);

}
