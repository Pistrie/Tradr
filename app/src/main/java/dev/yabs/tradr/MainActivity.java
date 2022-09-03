/*
 * Tradr
 * Copyright (c) 2022-2022, Sylvester Roos <pistrie@duck.com>
 *
 * Tradr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Tradr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Tradr.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.yabs.tradr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import dev.yabs.tradr.activities.FirstTimeActivity;
import dev.yabs.tradr.activities.InfoActivity;
import dev.yabs.tradr.activities.PreferencesActivity;
import dev.yabs.tradr.activities.QRCodeActivity;
import dev.yabs.tradr.utils.CheckIban;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    // initialize variables
    EditText etInput;
    Button btGenerate;
    ImageView ivOutput;

    // creating the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate called");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        // check if application has been started before
        boolean firstTime = prefs.getBoolean("FirstTimeInstall", true);
        if (firstTime) {
            editor.putBoolean("FirstTimeInstall", false);
            editor.apply();
            startActivity(new Intent(this, FirstTimeActivity.class));
        }

        // assign variables
        etInput = findViewById(R.id.et_input);
        btGenerate = findViewById(R.id.bt_generate);

        btGenerate.setOnClickListener(v -> {
            // get input value from edit text
            String sPrice = etInput
                    .getText()
                    .toString()
                    .trim();
            // quick checks to see if the string is correct
            if (sPrice.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.enter_a_price, Toast.LENGTH_SHORT).show();
                return;
            }
            if (sPrice.equals(".")) {
                Toast.makeText(MainActivity.this, R.string.enter_a_price, Toast.LENGTH_SHORT).show();
                return;
            }
            // format the string so that it looks like money
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.DOWN); // simply cut off excess numbers for EPC string
            sPrice = df.format(Double.parseDouble(sPrice)).replace(",", ".");
            // build the EPC string
            String nameAccountOwner = prefs.getString("name_account_owner", "").trim();
            String accountNumber = prefs.getString("bank_number", "").trim().toUpperCase();
            String transferDescription = prefs.getString("transfer_description", "").trim();
            if (!CheckIban.checkIban(prefs.getString("bank_number", ""))) {
                Toast.makeText(this, R.string.invalid_iban, Toast.LENGTH_LONG).show();
            } else if (nameAccountOwner.equals("") || accountNumber.equals("")) {
                Toast.makeText(this, R.string.required_info_missing, Toast.LENGTH_LONG).show();
            } else {
                String epc = "BCD\n002\n1\nSCT\n\n" + nameAccountOwner + "\n" + accountNumber + "\nEUR" + sPrice;
                if (!transferDescription.equals("")) {
                    epc += "\n\n\n" + transferDescription;
                }
                // initialize input manager
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // hide soft keyboard
                manager.hideSoftInputFromWindow(etInput.getApplicationWindowToken(), 0);

                // start new activity
                Intent qrCodeIntent = new Intent(this, QRCodeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("epc", epc);
                bundle.putString("price", sPrice);
                qrCodeIntent.putExtras(bundle);
                startActivity(qrCodeIntent);
            }
        });
    }

    // create the menu
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // menu click handling
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int clickedItem = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected item clicked -> " + clickedItem);

        if (clickedItem == R.id.action_preferences) {
            startActivity(new Intent(this, PreferencesActivity.class));

            return true;
        } else if (clickedItem == R.id.action_info) {
            startActivity(new Intent(this, InfoActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
