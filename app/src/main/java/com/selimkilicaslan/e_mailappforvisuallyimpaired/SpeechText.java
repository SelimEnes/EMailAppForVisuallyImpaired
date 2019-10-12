package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class SpeechText implements TextToSpeech.OnInitListener {

    private Activity activity;
    private Context context;
    private TextToSpeech textToSpeech;


    public SpeechText(Context cx,Activity ac) {
        context = cx;
        activity=ac;
        textToSpeech = new TextToSpeech(context,this );
    }
    public void listen(int requestCode){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException a) {

        }
    }

    public void speak(String input){
        textToSpeech.speak(input, TextToSpeech.QUEUE_FLUSH, null);
        while (textToSpeech.isSpeaking()){

        }
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
        }
    }
}