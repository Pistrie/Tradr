package dev.yabs.tradr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class PreferencesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PreferencesFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (prefs.getString("bank_number", "").matches("^NL[0-9]{2} ?[A-Z0-9]{4} ?[0-9]{4} ?[0-9]{4} ?[0-9]{2}$")) {
                finish();
                return true;
            } else {
                Toast.makeText(this, R.string.invalid_iban, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}