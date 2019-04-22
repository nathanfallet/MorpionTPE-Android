package me.nathanfallet.morpiontpe.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import me.nathanfallet.morpiontpe.R;

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

        PreferenceCategory groupe = new PreferenceCategory(activityContext);
        groupe.setTitle("Groupe MINASTE");

        getPreferenceScreen().addPreference(pro);
        getPreferenceScreen().addPreference(about);
        getPreferenceScreen().addPreference(groupe);

        // Create items
        Preference no_pro = new Preference(activityContext);
        no_pro.setTitle(R.string.no_pro);

        SwitchPreference isDarkMode = new SwitchPreference(activityContext);
        isDarkMode.setTitle(R.string.isDarkMode);
        isDarkMode.setKey("isDarkMode");

        SwitchPreference isHardcore = new SwitchPreference(activityContext);
        isHardcore.setTitle(R.string.isHardcore);
        isHardcore.setKey("isHardcore");

        Preference video = new Preference(activityContext);
        video.setTitle(R.string.video);
        video.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=f_URCGFxfzw")));

        Preference instagram = new Preference(activityContext);
        instagram.setTitle(R.string.instagram);
        instagram.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/nathanfallet/")));

        Preference moreApps = new Preference(activityContext);
        moreApps.setTitle(R.string.moreApps);
        moreApps.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7477103942295309472")));

        Preference donate = new Preference(activityContext);
        donate.setTitle(R.string.donate);
        donate.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/NathanFallet"))); // TODO: CHECK IF PAYPAL IS OK

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

        groupe.addPreference(moreApps);
        groupe.addPreference(donate);
    }

}
