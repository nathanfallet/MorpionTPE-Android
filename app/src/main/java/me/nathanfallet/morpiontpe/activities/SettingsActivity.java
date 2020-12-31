package me.nathanfallet.morpiontpe.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.nathanfallet.morpiontpe.R;
import me.nathanfallet.morpiontpe.models.NotificationName;

public class SettingsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_settings);

        updateTheme();

        // Set the title
        setTitle(R.string.settings);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThemeUpdated(NotificationName.ThemeUpdated updated) {
        updateTheme();
    }

    public void updateTheme() {
        // Check for dark mode
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }
    }

}
