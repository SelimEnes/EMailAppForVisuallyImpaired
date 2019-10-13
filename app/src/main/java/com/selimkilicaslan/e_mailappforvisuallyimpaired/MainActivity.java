package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuInflater;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class MainActivity extends MyAppCompatActivity {
    String email, password;
    RecyclerView recyclerView;
    MailAdapter mailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mailList = new ArrayList<>();
        email = SharedPreferencesHandler.getEmailAddress(getApplicationContext());
        password = SharedPreferencesHandler.getPassword(getApplicationContext());
        asyncTasks.Login(this, email, password, _Host);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_new_email:
                intent = new Intent(getApplicationContext(), SendMailActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add_contact:
                intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                SharedPreferencesHandler.setCredentials(getApplicationContext(), "","");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAsyncReturn(int functionCode, int response){
        if(functionCode == 1) {
            if (response == 1) {
                try {
                    Store store = emailSession.getStore("pop3s");
                    store.connect();
                    Folder emailFolder = store.getFolder("INBOX");
                    emailFolder.open(Folder.READ_ONLY);
                    int messageCount = emailFolder.getMessageCount();
                    Flags flags = new Flags();
                    flags.add(Flag.RECENT);
                    FlagTerm flagTerm = new FlagTerm(flags, true);
                    //Message[] messages = emailFolder.search(flagTerm);
                    Message[] messages;
                    if(messageCount >= 25){
                        messages = emailFolder.getMessages(messageCount - 24, messageCount);
                    }
                    else{
                        messages = emailFolder.getMessages();
                    }

                    for (int i = 0; i < messages.length; i++) {
                        Message message = messages[i];
                        String subject = message.getSubject();
                        SimpleDateFormat format=new SimpleDateFormat("dd MMM yyyy");
                        String sentDate = format.format(message.getSentDate());
                        String from = message.getFrom()[0].toString();
                        MailItemParcelable newMail = new MailItemParcelable(subject, from, "", sentDate);
                        writePart(message, newMail);
                        mailList.add(newMail);
                    }
                    mailAdapter = new MailAdapter(getApplicationContext(), mailList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(mailAdapter);
                        }
                    });
                    if(mailList.size() > 0){
                        speak("You have "+ mailList.size() + " new messages");
                    } else {
                        speak("You don't have any new messages");
                    }
                    emailFolder.close(false);
                    store.close();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(functionCode == 2){
            if(response == 1){
                speak("Message sent successfully");
            } else{
                speak("Couldn't send message");
            }
        }
    }

    @Override
    public void onVoiceButtonClick(View view) {
        super.onVoiceButtonClick(view);
    }

    @Override
    public void onBackPressed(){
    }

    public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MyViewHolder> {

        ArrayList<MailItemParcelable> MailList;
        LayoutInflater inflater;

        public MailAdapter(Context context, ArrayList<MailItemParcelable> mails) {
            inflater = LayoutInflater.from(context);
            this.MailList = mails;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_mail_card, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            MailItemParcelable selectedMail = MailList.get(position);
            holder.setData(selectedMail, position);

        }

        @Override
        public int getItemCount() {
            return MailList.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView Subject, Sender, Date;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                Subject = itemView.findViewById(R.id.subjectTextView);
                Sender = itemView.findViewById(R.id.senderTextView);
                Date = itemView.findViewById(R.id.dateTextView);

            }

            public void setData(MailItemParcelable selectedPlan, int position) {

                this.Subject.setText(selectedPlan.getSubject());
                this.Sender.setText(selectedPlan.getSender());
                this.Date.setText(selectedPlan.getDate());

            }


            @Override
            public void onClick(View v) {
                Log.i("Recycler click", String.valueOf(getAdapterPosition()));
                MailItemParcelable selectedItem = mailList.get(getAdapterPosition());
                Intent intent = new Intent(getApplicationContext(), SingleMailActivity.class);
                intent.putExtra("Mail", selectedItem);
                startActivity(intent);

            }


        }
    }


}
