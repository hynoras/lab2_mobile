package com.example.exercise2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView colorDisplay;
    private RadioGroup colorRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorDisplay = findViewById(R.id.colorDisplay);
        colorRadioGroup = findViewById(R.id.colorRadioGroup);

        colorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String colorName = selectedRadioButton.getText().toString();
                int color = Color.BLACK;

                switch (colorName) {
                    case "Red":
                        color = Color.RED;
                        break;
                    case "Green":
                        color = Color.GREEN;
                        break;
                    case "Blue":
                        color = Color.BLUE;
                        break;
                    case "Gray":
                        color = Color.GRAY;
                        break;
                }

                colorDisplay.setBackgroundColor(color);
            }
        });
    }
}