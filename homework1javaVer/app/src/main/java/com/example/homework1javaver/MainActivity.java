package com.example.homework1javaver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SentimentAnalysisTask";
    private EditText editText;
    private Button submitButton;
    private ImageView sentimentIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editText = findViewById(R.id.editText);
        submitButton = findViewById(R.id.submitBtn);
        sentimentIcon = findViewById(R.id.sentimentIcon);

        // Set click listener for submit button
        submitButton.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            new SentimentAnalysisTask(sentiment -> {
                Log.d(TAG, "Received sentiment: " + sentiment);
                switch (sentiment) {
                    case "POS":
                        Log.d(TAG, "Setting positive icon");
                        sentimentIcon.setImageResource(R.drawable.sentiment_positive);
                        break;
                    case "NEG":
                        Log.d(TAG, "Setting negative icon");
                        sentimentIcon.setImageResource(R.drawable.sentiment_negative);
                        break;
                    default:
                        Log.d(TAG, "Setting neutral icon");
                        sentimentIcon.setImageResource(R.drawable.sentiment_neutral);
                }
            }).execute(inputText);
        });
    }

    private static class SentimentAnalysisTask extends AsyncTask<String, Void, String> {

        private SentimentAnalysisListener listener;

        public interface SentimentAnalysisListener {
            void onSentimentAnalysisCompleted(String sentiment);
        }

        public SentimentAnalysisTask(SentimentAnalysisListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... strings) {
            String inputText = strings[0];
            String apiUrl = "https://api-inference.huggingface.co/models/wonrax/phobert-base-vietnamese-sentiment";
            String bearerToken = "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";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + bearerToken);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("inputs", inputText);

                conn.getOutputStream().write(jsonInput.toString().getBytes());

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                JSONArray data = new JSONArray(response.toString());
                String sentiment = "";
                double maxScore = 0.0;

                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    double score = item.getDouble("score");
                    if (score > maxScore) {
                        maxScore = score;
                        sentiment = item.getString("label");
                    }
                }

                conn.disconnect();
                return sentiment;

            } catch (Exception e) {
                Log.e(TAG, "Error querying API: " + e.getMessage());
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String sentiment) {
            Log.d(TAG, "Sentiment analysis completed. Result: " + sentiment);
            if (listener != null) {
                listener.onSentimentAnalysisCompleted(sentiment);
            }
        }
    }
}
