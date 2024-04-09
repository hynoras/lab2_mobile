package com.example.exercise3;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String input = "";
    private double num1 = Double.NaN;
    private double num2 = Double.NaN;
    private char operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        findViewById(R.id.bt0).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt1).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt2).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt3).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt4).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt5).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt6).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt7).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt8).setOnClickListener(buttonClickListener);
        findViewById(R.id.bt9).setOnClickListener(buttonClickListener);
        findViewById(R.id.btdec).setOnClickListener(buttonClickListener);

        findViewById(R.id.btsub).setOnClickListener(operatorClickListener);
        findViewById(R.id.btmul).setOnClickListener(operatorClickListener);
        findViewById(R.id.btdiv).setOnClickListener(operatorClickListener);
        findViewById(R.id.btadd).setOnClickListener(operatorClickListener);

        findViewById(R.id.btclear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        findViewById(R.id.btequal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compute();
            }
        });
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            input += button.getText().toString();
            textView.setText(input);
        }
    };

    private View.OnClickListener operatorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (!input.isEmpty()) {
                num1 = Double.parseDouble(input);
                operator = button.getText().charAt(0);
                input = "";
                textView.setText(String.valueOf(num1) + operator);
            }
        }
    };

    private void compute() {
        if (!Double.isNaN(num1) && !input.isEmpty()) {
            num2 = Double.parseDouble(input);
            double result = 0;
            switch (operator) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case 'x':
                    result = num1 * num2;
                    break;
                case '/':
                    result = num1 / num2;
                    break;
            }
            textView.setText(String.valueOf(result));
            num1 = result;
            input = "";
        }
    }

    private void clear() {
        num1 = Double.NaN;
        num2 = Double.NaN;
        operator = '\0';
        input = "";
        textView.setText("");
    }
}