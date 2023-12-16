package com.example.languagestransaltor;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.languagestransaltor.models.Translate;
import com.example.languagestransaltor.models.TranslateData;
import com.example.languagestransaltor.models.WordData;
import com.example.languagestransaltor.network.ApiClient;
import com.example.languagestransaltor.network.ApiService;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText word;
    private TextView translate;
    private Button btnSpeech1, btnSpeech2, btnClear, btnTranslate, langBtn1, langBtn2;
    private ImageButton replaceLangBtn;
//    private SeekBar mSeekBarSpeed;

    private ApiService apiService;
    private String from = "en";
    private String to = "uz";
    private boolean langType = true;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //API service classiga apiService ozgaruvchisini tenglab oldim
        apiService = ApiClient.getRetrofit().create(ApiService.class);

        //I changed app names color that textColorPrimary here
        getSupportActionBar().setTitle(Html.fromHtml("<font color = \"white\">" + getString(R.string.app_name) + "</font>"));

        findViews();

        btnTranslate.setOnClickListener(v -> { //giving function to translate button that call to API and get response is from start here
            String word = this.word.getText().toString();
            if (word.isEmpty()) { // when input content= empty this massage will become on the display
                Toast.makeText(this, "Write something to translate", Toast.LENGTH_SHORT).show();
            } else {
                List<WordData> list = new ArrayList<>();
                list.add(new WordData(word));
                apiService.getTranslate(ApiClient.key, ApiClient.region, ApiClient.version, from, to, list)
                        .enqueue(new Callback<List<TranslateData>>() {
                            @Override
                            public void onResponse(Call<List<TranslateData>> call, Response<List<TranslateData>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    TranslateData translateData = response.body().get(0);
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (Translate translation : translateData.getTranslations()) {
                                        stringBuilder.append(translation.getText()).append("\n");
                                    }
                                    translate.setText(stringBuilder.toString());
                                }
                            }
                            @Override
                            public void onFailure(Call<List<TranslateData>> call, Throwable t) {

                            }
                        });
            }
        });
        //in this step btnSpeech1 get text which inputted then build text to speech
        btnSpeech1.setOnClickListener(v -> {
            String word = this.word.getText().toString();
            if (word.isEmpty()) {
                Toast.makeText(this, "Write something to translate", Toast.LENGTH_SHORT).show();
            } else {
                String speechLang;
                if (langType) {
                    speechLang = "en-US-ChristopherNeural"; // azure microsoft speech service 에서 지원되는
                } else {                                    //언어의 Neural voices list 에서 필요한 언어 code 을 받아 입력
                    speechLang = "uz-UZ-SardorNeural";
                }
                try {                                      // step that having 예외저리 function
                    speechText(word, speechLang);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnSpeech2.setOnClickListener(v -> {               //in this step btnSpeech1 get text which translated then build text to speech
            String word = translate.getText().toString();
            if (word.isEmpty()) {
                Toast.makeText(this, "Write something to translate", Toast.LENGTH_SHORT).show();
            } else {
                String speechLang;
                if (langType) {
                    speechLang = "uz-UZ-SardorNeural";
                } else {
                    speechLang = "en-US-ChristopherNeural";
                }
                try {
                    speechText(word, speechLang);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        replaceLangBtn.setOnClickListener(v -> {            //in this step language position change when you click forward button
            if (langType) {
                from = "uz";
                to = "en";
                langBtn1.setText("UZBEK");
                langBtn2.setText("ENGLISH");
                langType = false;
            } else {
                from = "en";
                to = "uz";
                langBtn1.setText("ENGLISH");
                langBtn2.setText("UZBEK");
                langType = true;
            }
            word.setText("");
            translate.setText("");
        });
    }
    // this function build text to speech
    private void speechText(String word, String speechLang) throws ExecutionException, InterruptedException {

        String speechKey = "0642a61ce3a8474e832a5574cd49df2c";  //API key: I created Text to Speech API in my MicrosoftAurePortal then got
        String speechRegion = "koreacentral";                   //API key and resource region to build TTS.
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
        speechConfig.setSpeechSynthesisVoiceName(speechLang);
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(speechConfig);
        // Get text from the console and synthesize to the default speaker.
        Toast.makeText(this,"Succeeded!", Toast.LENGTH_SHORT).show();

        SpeechSynthesisResult speechSynthesisResult = speechSynthesizer.SpeakTextAsync(word).get();

        if (speechSynthesisResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
//            Toast.makeText(this,"Speech synthesized to speaker for text [" + word + "]", Toast.LENGTH_SHORT).show();
        } else if (speechSynthesisResult.getReason() == ResultReason.Canceled) {
            SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(speechSynthesisResult);
            Toast.makeText(this,"CANCELED: Reason=" + cancellation.getReason(), Toast.LENGTH_SHORT).show();
        }
    }
 /*
    private void setSpeechRate(){
        mSeekBarSpeed.setOnSeekBarChangeListener(v ->{

            float speed = (float) mSeekBarSpeed.getProgress() /50;
            if (speed < 0.1) speed = 0.1f;

            translate.setSpeechRate(speed);
            translate.onRespons(word, TextToSpeech.QUEUE_FLUSH,null);
        });
    }
  */
    private void findViews() {
        word = findViewById(R.id.sourceLanguageEt);
        translate = findViewById(R.id.destinationLanguage);

        btnSpeech1 = findViewById(R.id.btn_speech1);
        btnSpeech2 = findViewById(R.id.btn_speech2);
        replaceLangBtn = findViewById(R.id.replace_language);
        btnTranslate = findViewById(R.id.translateBtn);
        langBtn1 = findViewById(R.id.sourceLanguageChooseBtn);
        langBtn2 = findViewById(R.id.destinationLanguageChooseBtn);

        /* 추가 될 function
        btnClear = findViewById(R.id.btn_clear);
        mSeekBarSpeed = findViewById(R.id.seek_bar_speed);
        */

    }
}