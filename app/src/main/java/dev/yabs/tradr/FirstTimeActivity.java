package dev.yabs.tradr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FirstTimeActivity extends AppCompatActivity {

    Button btSetUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        btSetUp = findViewById(R.id.bt_set_up);

        btSetUp.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }
}