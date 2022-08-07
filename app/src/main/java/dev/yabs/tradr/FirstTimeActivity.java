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