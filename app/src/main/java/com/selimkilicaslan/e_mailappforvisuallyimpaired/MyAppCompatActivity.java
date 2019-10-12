package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;

public class MyAppCompatActivity extends AppCompatActivity {
    AsyncTasks asyncTasks;
    String _Host = "pop.gmail.com";
    Session emailSession;
    String email, password;
    ArrayList<MailItemParcelable> mailList;

    public static final int LISTEN=1;
    public static final int TOWHOM=2;
    public static final int SUBJECT=3;
    public static final int CONTENT=4;
    public static final int CONFIRM=5;
    private ArrayList<String> keywordsArrayList;
    SpeechText speechText;
    String toWhom, content, subject;

    public void onAsyncResponse(final int functionCode,final int response, Session emailSession){
        this.emailSession = emailSession;
        new Thread(new Runnable() {
            @Override
            public void run() {
                onAsyncReturn(functionCode, response);
            }
        }).start();
    }
    public void onAsyncReturn(int functionCode,int response){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTasks = new AsyncTasks();
        email = SharedPreferencesHandler.getEmailAddress(getApplicationContext());
        password = SharedPreferencesHandler.getPassword(getApplicationContext());
        String [] keywordsList={"create a new message","send","read unread messages","read messages","read","send a message","send message"};

        speechText=new SpeechText(getApplicationContext(),this);

        keywordsArrayList=new ArrayList<>(Arrays.asList(keywordsList));
    }

    public void writePart(Part part, MailItemParcelable item) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            //Log.i("Mail", "Text: " + part.getContent().toString());
            item.setContent(item.getContent() + part.getContent().toString());
        } else if (part.isMimeType("multipart/*")) {
            Multipart content = (Multipart) part.getContent();
            int partCount = content.getCount();
            for (int j = 0; j < partCount; j++) {
                writePart(content.getBodyPart(j), item);
            }
        }
    }

    public void onVoiceButtonClick(View view){
        speechText.listen(LISTEN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            final ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            final String speechInput = result.get(0);
            switch (requestCode) {
                case LISTEN:
                    if(keywordsArrayList.contains(speechInput)){
                        switch (speechInput){
                            case "create a new message": case "send a message": case "send message":
                                getMessageInfo();
                                break;
                            case "read unread messages": case "read messages": case "read":
                                if(mailList != null && mailList.size() > 0){
                                    for (MailItemParcelable item: mailList) {
                                        readMail(item);
                                    }
                                } else {
                                    speak("You don't have any new messages");
                                }
                                break;
                        }
                    }
                    break;

                case TOWHOM:
                    String label = speechInput;
                    toWhom = SharedPreferencesHandler.getContactAddress(getApplicationContext(), label);
                    if(toWhom == ""){
                        speak("You don't have a contact called " + label);
                    }
                    else {
                        speechText.speak("what is the subject of the message");
                        speechText.listen(SUBJECT);
                    }
                    break;

                case SUBJECT:
                    subject = speechInput;
                    speechText.speak("what is the content of the message");
                    speechText.listen(CONTENT);
                    break;

                case CONTENT:
                    content=speechInput;
                    String mes="for confirming, an email will be send to "+toWhom+" and the message is: "+content+", Please say yes to confirm and send the message, otherwise say no to cancel!";
                    speechText.speak(mes);
                    speechText.listen(CONFIRM);
                    break;

                case CONFIRM:
                    if(speechInput.equals("yes")){
                        //speechText.speak("message send successfully");
                        asyncTasks.SendMail(this, emailSession, email, toWhom, subject, content);
                    }else if(speechInput.equals("no")){
                        speechText.speak("message canceled");
                    }
                    break;

            }
        }
    }

    public void readMail(MailItemParcelable item){
        speak("Message sent from " + item.getSender());
        speak("Message subject is " + item.getSubject());
        speak("Message content is " + item.getContent());
    }

    public void getMessageInfo(){
        speechText.speak("to whom would you like to send ");
        speechText.listen(TOWHOM);
    }

    public void speak(String message){
        speechText.speak(message);
    }
}
