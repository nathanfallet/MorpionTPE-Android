package me.nathanfallet.morpiontpe.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.SwitchPreference;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import org.greenrobot.eventbus.EventBus;

import me.nathanfallet.morpiontpe.R;
import me.nathanfallet.morpiontpe.models.NotificationName;
import me.nathanfallet.myappsandroid.preferences.MyAppPreferences;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstance, String rootPreferenceKey) {
        Context activityContext = getActivity();

        PreferenceScreen rootPreferenceScreen = getPreferenceManager().createPreferenceScreen(activityContext);
        setPreferenceScreen(rootPreferenceScreen);

        // Create categories
        PreferenceCategory pro = new PreferenceCategory(activityContext);
        pro.setTitle(R.string.pro);

        PreferenceCategory about = new PreferenceCategory(activityContext);
        about.setTitle(R.string.about);

        //PreferenceCategory groupe = new PreferenceCategory(activityContext);
        //groupe.setTitle("Groupe MINASTE");

        getPreferenceScreen().addPreference(pro);
        getPreferenceScreen().addPreference(about);
        //getPreferenceScreen().addPreference(groupe);

        // Create items
        Preference no_pro = new Preference(activityContext);
        no_pro.setTitle(R.string.no_pro);

        SwitchPreference isDarkMode = new SwitchPreference(activityContext);
        isDarkMode.setTitle(R.string.isDarkMode);
        isDarkMode.setKey("isDarkMode");
        isDarkMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean enabled = (boolean) o;

                AppCompatDelegate.setDefaultNightMode(enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                EventBus.getDefault().post(new NotificationName.ThemeUpdated());

                return true;
            }
        });

        SwitchPreference isHardcore = new SwitchPreference(activityContext);
        isHardcore.setTitle(R.string.isHardcore);
        isHardcore.setKey("isHardcore");

        Preference video = new Preference(activityContext);
        video.setTitle(R.string.video);
        video.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=f_URCGFxfzw")));

        Preference instagram = new Preference(activityContext);
        instagram.setTitle(R.string.instagram);
        instagram.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/nathanfallet/")));

        //Preference moreApps = new Preference(activityContext);
        //moreApps.setTitle(R.string.moreApps);
        //moreApps.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7477103942295309472")));

        /*Preference donate = new Preference(activityContext);
        donate.setTitle(R.string.donate);
        donate.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/NathanFallet")));*/

        // And add them to categories
        boolean darkmodeUnlocked = getPreferenceScreen().getSharedPreferences().getBoolean("darkmodeUnlocked", false);
        boolean hardcoreUnlocked = getPreferenceScreen().getSharedPreferences().getBoolean("hardcoreUnlocked", false);

        if (darkmodeUnlocked) {
            pro.addPreference(isDarkMode);
        }
        if (hardcoreUnlocked) {
            pro.addPreference(isHardcore);
        }
        if (pro.getPreferenceCount() == 0) {
            pro.addPreference(no_pro);
        }

        about.addPreference(video);
        about.addPreference(instagram);

        //groupe.addPreference(moreApps);

        new MyAppPreferences().addPreferences(getPreferenceScreen(), requireActivity());
    }

}
