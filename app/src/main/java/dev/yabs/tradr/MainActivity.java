package dev.yabs.tradr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
        ivOutput = findViewById(R.id.iv_output);

        btGenerate.setOnClickListener(v -> {
            // get input value from edit text
            String sText = etInput
                    .getText()
                    .toString()
                    .trim();
            if (sText.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.enter_a_price, Toast.LENGTH_SHORT).show();
                return;
            }
            // initialize multi format writer
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                // build the EPC QR-Code
                String nameAccountOwner = prefs.getString("name_account_owner", "").trim();
                String accountNumber = prefs.getString("bank_number", "").trim().toUpperCase();
                String transferDescription = prefs.getString("transfer_description", "").trim();
                if (nameAccountOwner.equals("") || accountNumber.equals("")) {
                    Toast.makeText(this, R.string.required_info_missing, Toast.LENGTH_LONG).show();
                } else {
                    String epc = "BCD\n002\n1\nSCT\n\n" + nameAccountOwner + "\n" + accountNumber + "\nEUR" + sText;
                    if (!transferDescription.equals("")) {
                        epc += "\n\n\n" + transferDescription;
                    }
                    // initialize bit matrix
                    BitMatrix matrix = writer.encode(epc, BarcodeFormat.QR_CODE, 350, 350);
                    // initialize barcode encoder
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    // initialize bitmap
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    // set bitmap on image view
                    ivOutput.setImageBitmap(bitmap);
                    // initialize input manager
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // hide soft keyboard
                    manager.hideSoftInputFromWindow(etInput.getApplicationWindowToken(), 0);
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
    }

    // create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        }

        return super.onOptionsItemSelected(item);
    }
}
