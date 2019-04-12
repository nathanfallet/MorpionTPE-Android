package me.nathanfallet.morpiontpe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import me.nathanfallet.morpiontpe.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        // Set content view
        setContentView(R.layout.activity_menu);

        // Listener for game
        View.OnClickListener gameListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                intent.putExtra("id", v.getId());
                startActivity(intent);
            }
        };

        // Listener for settings
        View.OnClickListener settingsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        };

        // Add actions to buttons
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button settings = findViewById(R.id.settings);

        button1.setOnClickListener(gameListener);
        button2.setOnClickListener(gameListener);
        button3.setOnClickListener(gameListener);
        settings.setOnClickListener(settingsListener);

    }
}
