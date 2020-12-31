package me.nathanfallet.morpiontpe.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.nathanfallet.morpiontpe.R;
import me.nathanfallet.morpiontpe.models.Computer;
import me.nathanfallet.morpiontpe.models.Game;
import me.nathanfallet.morpiontpe.models.Human;
import me.nathanfallet.morpiontpe.models.Player;
import me.nathanfallet.morpiontpe.models.Sign;
import me.nathanfallet.morpiontpe.models.NotificationName;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private ImageButton box1;
    private ImageButton box2;
    private ImageButton box3;
    private ImageButton box4;
    private ImageButton box5;
    private ImageButton box6;
    private ImageButton box7;
    private ImageButton box8;
    private ImageButton box9;
    private TextView infos;
    private Button again;
    private Button back;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTheme();

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
        again = findViewById(R.id.again);
        back = findViewById(R.id.back);

        // Handle click
        box1.setOnClickListener(new BoxClickListener(0, 0));
        box2.setOnClickListener(new BoxClickListener(1, 0));
        box3.setOnClickListener(new BoxClickListener(2, 0));
        box4.setOnClickListener(new BoxClickListener(0, 1));
        box5.setOnClickListener(new BoxClickListener(1, 1));
        box6.setOnClickListener(new BoxClickListener(2, 1));
        box7.setOnClickListener(new BoxClickListener(0, 2));
        box8.setOnClickListener(new BoxClickListener(1, 2));
        box9.setOnClickListener(new BoxClickListener(2, 2));
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new game with same players
                game = new Game(game.getPlayer1(), game.getPlayer2(), game.isHardcore());
                updateUI();

                // And start it
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        game.nextMove();
                    }
                }).start();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Init the game object from the Intent
        Intent intent = getIntent();

        // We determine who play the game
        boolean isHardcore = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isHardcore", false);
        if (intent.getExtras().getInt("id") == R.id.button1) {
            game = new Game(new Human(Sign.X), new Human(Sign.O), isHardcore);
        } else if (intent.getExtras().getInt("id") == R.id.button2) {
            Player[] players_brut = {new Computer(Sign.X), new Human(Sign.O)};
            List<Player> players = Arrays.asList(players_brut);
            Collections.shuffle(players);
            game = new Game(players.get(0), players.get(1), isHardcore);
        } else {
            game = new Game(new Computer(Sign.X), new Computer(Sign.O), isHardcore);
        }

        // Load the empty grid
        updateUI();

        // Everything is up, start the game
        new Thread(new Runnable() {
            @Override
            public void run() {
                game.nextMove();
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBoardUpdated(NotificationName.BoardUpdated updater) {
        updateUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThemeUpdated(NotificationName.ThemeUpdated updated) {
        updateTheme();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameEnded(NotificationName.GameEnded updater) {
        updateUI();

        // Update game count
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int numberOfGamesPlayed = prefs.getInt("numberOfGamesPlayed", 0) + 1;
        prefs.edit().putInt("numberOfGamesPlayed", numberOfGamesPlayed).apply();

        // Check for new PRO features
        boolean darkmodeUnlocked = prefs.getBoolean("darkmodeUnlocked", false);
        boolean hardcoreUnlocked = prefs.getBoolean("hardcoreUnlocked", false);

        // Check for darkmode
        if (!darkmodeUnlocked && numberOfGamesPlayed >= 5) {
            // Save that the feature is unlocked
            prefs.edit().putBoolean("darkmodeUnlocked", true).apply();

            // And show alert
            new AlertDialog.Builder(this).setTitle(R.string.unlocked_title).setMessage(R.string.unlocked_darkmode).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            }).create().show();
        }

        // Check for hardcore
        if (!hardcoreUnlocked && ((game.getPlayer1() instanceof Human && game.getPlayer2() instanceof Computer && game.win(game.getTable()) == game.getPlayer1().sign) || (game.getPlayer1() instanceof Computer && game.getPlayer2() instanceof Human && game.win(game.getTable()) == game.getPlayer2().sign))) {
            // Save that the feature is unlocked
            prefs.edit().putBoolean("hardcoreUnlocked", true).apply();

            // And show alert
            new AlertDialog.Builder(this).setTitle(R.string.unlocked_title).setMessage(R.string.unlocked_hardcore).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            }).create().show();
        }
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
            again.setVisibility(View.INVISIBLE);
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
            again.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
        }

        // Update images
        ImageButton[][] boxes = {{box1, box4, box7}, {box2, box5, box8}, {box3, box6, box9}};

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ImageButton box = boxes[x][y];
                Sign sign = game.getTable()[x][y];

                if (sign != Sign.empty) {
                    box.setImageDrawable(getDrawable(sign == Sign.X ? R.drawable.x : R.drawable.o));
                } else {
                    box.setImageDrawable(null);
                }
            }
        }
    }

    public void updateTheme() {
        // Check for dark mode
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }
    }

    private class BoxClickListener implements View.OnClickListener {

        private int x;
        private int y;

        BoxClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View v) {
            // Iterate the players
            Player[] players = {game.getPlayer1(), game.getPlayer2()};
            for (Player player : players) {
                // Check if it's a human, and the current player
                if (player instanceof Human && game.getCurrent() == player.sign) {
                    final Human human = (Human) player;

                    // Play!
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            human.getCallback().completion(x, y);
                        }
                    }).start();
                }
            }
        }

    }

}
