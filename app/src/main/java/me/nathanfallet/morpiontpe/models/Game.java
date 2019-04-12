package me.nathanfallet.morpiontpe.models;

public class Game {

    // Game vars
    private Sign[][] table = {
            {Sign.empty, Sign.empty, Sign.empty},
            {Sign.empty, Sign.empty, Sign.empty},
            {Sign.empty, Sign.empty, Sign.empty}
    };
    private Player player1;
    private Player player2;
    private Sign current;
    private UIUpdater updater;

    // Init the game
    public Game(Player player1, Player player2, UIUpdater updater) {
        this.player1 = player1;
        this.player2 = player2;
        this.current = player1.sign;
        this.updater = updater;
    }

    // Getters
    public Sign[][] getTable() {
        return table;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Sign getCurrent() {
        return current;
    }

    // Gameplay
    public void nextMove() {
        Sign win = win(table);

        // Check that it's not the end of the game
        if (!full(table) && win == Sign.empty) {
            // Iterate the players
            Player[] players = {player1, player2};
            for (Player player : players) {
                if (player.sign == current) {
                    // Here the player plays
                    player.play(this, new GamePlayCallback() {
                        @Override
                        public void completion(int x, int y) {
                            // Set the move
                            if (play(x, y, current)) {
                                current = current == Sign.X ? Sign.O : Sign.X;
                            }

                            // Update UI
                            updater.updateUI();

                            // And go to next move
                            nextMove();
                        }
                    });
                    // Stop here
                    return;
                }
            }
        }

        // The game ended
        current = Sign.empty;
        updater.updateUI();
    }

    // Make a player plays in the board
    public boolean play(int x, int y, Sign sign) {
        if (x >= 0 && x < 3 && y >= 0 && y < 3 && table[x][y] == Sign.empty) {
            table[x][y] = sign;
            return true;
        }
        return false;
    }

    // Check is there a winner
    public Sign win(Sign[][] table) {
        for (int i = 0; i < 3; i++) {
            // Check if a line has a winner
            Sign line = line(table, i);
            if (line != Sign.empty) {
                return line;
            }

            // Check if a row has a winner
            Sign row = row(table, i);
            if (row != Sign.empty) {
                return row;
            }
        }

        for (int i = 0; i < 2; i++) {
            // Check if a dia has a winner
            Sign dia = dia(table, i);
            if (dia != Sign.empty) {
                return dia;
            }
        }

        return Sign.empty;
    }

    // Check if a line is full
    public Sign line(Sign[][] table, int y) {
        Sign sign = table[0][y];
        boolean changed = false;

        for (int x = 0; x < 3; x++) {
            if (table[x][y] != sign) {
                changed = true;
            }
        }

        if (changed) {
            return Sign.empty;
        }

        return sign;
    }

    // Check if a row is full
    public Sign row(Sign[][] table, int x) {
        Sign sign = table[x][0];
        boolean changed = false;

        for (int y = 0; y < 3; y++) {
            if (table[x][y] != sign) {
                changed = true;
            }
        }

        if (changed) {
            return Sign.empty;
        }

        return sign;
    }

    // Check if a dia is full
    public Sign dia(Sign[][] table, int d) {
        int i = d == 0 ? 0 : 2;
        Sign sign = table[i][0];
        boolean changed = false;

        for (int x = 0; x < 3; x++) {
            i = d == 0 ? x : 2 - x;
            if (table[i][x] != sign) {
                changed = true;
            }
        }

        if (changed) {
            return Sign.empty;
        }

        return sign;
    }

    // Check if the board is full
    public boolean full(Sign[][] table) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (table[x][y] == Sign.empty) {
                    return false;
                }
            }
        }
        return true;
    }

}
