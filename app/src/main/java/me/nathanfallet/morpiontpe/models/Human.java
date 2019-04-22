package me.nathanfallet.morpiontpe.models;

public class Human extends Player {

    private GamePlayCallback callback;

    public Human(Sign sign) {
        super(sign);
    }

    @Override
    public void play(Game game, GamePlayCallback callback) {
        this.callback = callback;
    }

    public GamePlayCallback getCallback() {
        return callback;
    }
}
