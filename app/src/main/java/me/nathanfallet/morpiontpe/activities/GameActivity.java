package me.nathanfallet.morpiontpe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.nathanfallet.morpiontpe.R;
import me.nathanfallet.morpiontpe.models.Computer;
import me.nathanfallet.morpiontpe.models.Game;
import me.nathanfallet.morpiontpe.models.Human;
import me.nathanfallet.morpiontpe.models.Player;
import me.nathanfallet.morpiontpe.models.Sign;
import me.nathanfallet.morpiontpe.models.UIUpdater;

public class GameActivity extends AppCompatActivity implements UIUpdater {

    private Game game;
    private ImageView box1;
    private ImageView box2;
    private ImageView box3;
    private ImageView box4;
    private ImageView box5;
    private ImageView box6;
    private ImageView box7;
    private ImageView box8;
    private ImageView box9;
    private TextView infos;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        // Set content view
        setContentView(R.layout.activity_game);

        // Get views
        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        box4 = findViewById(R.id.box4);
        box5 = findViewById(R.id.box5);
        box6 = findViewById(R.id.box6);
        box7 = findViewById(R.id.box7);
        box8 = findViewById(R.id.box8);
        box9 = findViewById(R.id.box9);
        infos = findViewById(R.id.infos);
        back = findViewById(R.id.back);

        // Init the game object from the Intent
        Intent intent = getIntent();

        // We determine who play the game
        if (intent.getExtras().getInt("id") == R.id.button1) {
            game = new Game(new Human(Sign.X), new Human(Sign.O), this);
        } else if (intent.getExtras().getInt("id") == R.id.button2) {
            Player[] players_brut = {new Computer(Sign.X), new Human(Sign.O)};
            List<Player> players = Arrays.asList(players_brut);
            Collections.shuffle(players);
            game = new Game(players.get(0), players.get(1), this);
        } else {
            game = new Game(new Computer(Sign.X), new Computer(Sign.O), this);
        }

        // Load the empty grid
        updateUI();

        // Everything is up, start the game
        game.nextMove();
    }

    public void updateUI() {
        // Update infos label
        if (game.getCurrent() != Sign.empty) {
            // Get the player object
            Player current = null;
            Player[] players = {game.getPlayer1(), game.getPlayer2()};
            for (Player player : players) {
                if (player.sign == game.getCurrent()) {
                    current = player;
                }
            }

            // Differentiate human and computer in text
            if (current instanceof Computer) {
                infos.setText(getString(R.string.playing_computer, game.getCurrent().toString()));
            } else {
                infos.setText(getString(R.string.playing_human, game.getCurrent().toString()));
            }
            back.setVisibility(View.INVISIBLE);
        } else {
            // Game has ended
            Sign win = game.win(game.getTable());
            Player current = null;
            Player[] players = {game.getPlayer1(), game.getPlayer2()};
            for (Player player : players) {
                if (player.sign == win) {
                    current = player;
                }
            }

            // Differentiate human and computer in text
            if (current instanceof Computer) {
                infos.setText(getString(R.string.ended_computer, win.toString()));
            } else if (current instanceof Human) {
                infos.setText(getString(R.string.ended_human, win.toString()));
            } else {
                infos.setText(getString(R.string.ended_empty));
            }
            back.setVisibility(View.VISIBLE);
        }

        // Update images
        ImageView[][] boxes = {{box1, box4, box7}, {box2, box5, box8}, {box3, box6, box9}};

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ImageView box = boxes[x][y];
                Sign sign = game.getTable()[x][y];

                if (sign != Sign.empty) {
                    box.setImageDrawable(getDrawable(sign == Sign.X ? R.drawable.x : R.drawable.o));
                } else {
                    box.setImageDrawable(null);
                }
            }
        }
    }

}
