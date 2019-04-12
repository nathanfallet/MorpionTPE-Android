package me.nathanfallet.morpiontpe.models;

public class Human extends Player {

    public Human(Sign sign) {
        super(sign);
    }

    @Override
    public void play(Game game, GamePlayCallback callback) {
        callback.completion(0, 0);
    }
}
