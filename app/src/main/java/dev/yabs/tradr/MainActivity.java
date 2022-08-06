package dev.yabs.tradr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        // assign variables
        etInput = findViewById(R.id.et_input);
        btGenerate = findViewById(R.id.bt_generate);
        ivOutput = findViewById(R.id.iv_output);

        btGenerate.setOnClickListener(v -> {
            // get input value from edit text
            String sText = etInput.getText().toString().trim();
            // initialize multi format writer
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                // initialize bit matrix
                BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE, 350, 350);
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
