package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AsyncTasks {



    public void Login(MyAppCompatActivity activity,String email,String  password,String  host){
        LoginAsync loginAsync = new LoginAsync();
        loginAsync.setEmail(email);
        loginAsync.setHost(host);
        loginAsync.setPassword(password);
        loginAsync.execute(activity);
    }

    public class LoginAsync extends AsyncTask<MyAppCompatActivity, Void, String> {


        private String email, pass, host, message;
        private MyAppCompatActivity activity;
        private Session emailSession;
        private boolean loggedIn;


        @Override
        protected String doInBackground(MyAppCompatActivity... activities) {
            try {
                activity = activities[0];
                Properties properties = System.getProperties();
                //Properties properties = new Properties();
                final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

                properties.put("mail.pop3s.host", host);
                properties.put("mail.pop3s.port", "995");
                properties.put("mail.pop3s.starttls.enable", "true");
                properties.setProperty("mail.smtp.host", "smtp.gmail.com");
                properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
                properties.setProperty("mail.smtp.socketFactory.fallback", "false");
                properties.setProperty("mail.smtp.port", "465");
                properties.setProperty("mail.smtp.socketFactory.port", "465");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.debug", "false");
                properties.put("mail.store.protocol", "pop3s");
                properties.put("mail.transport.protocol", "smtp");

                emailSession = Session.getDefaultInstance(properties,
                        new Authenticator(){
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(email, pass);
                            }});

                setLoggedIn(false);

                Store store = emailSession.getStore("pop3s");
                //Store store = emailSession.getStore("pop3s");
                store.connect();

                setMessage("Successfully logged in");
                SharedPreferencesHandler.setCredentials(activity.getApplicationContext(), email, pass);

                setLoggedIn(true);


                store.close();


            } catch (NoSuchProviderException e) {
                Log.e("Mail", e.getLocalizedMessage());
            } catch (MessagingException e) {
                //Toast.makeText(getApplicationContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                setMessage("Unauthorized");
                SharedPreferencesHandler.setCredentials(activity.getApplicationContext(), "", "");
                //Log.e("Mail", e.getLocalizedMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            if(loggedIn){
                activity.onAsyncResponse(1, 1, emailSession);
            } else {
                activity.onAsyncResponse(1, 0, emailSession);
            }

        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.pass = password;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setLoggedIn(boolean loggedIn) {
            this.loggedIn = loggedIn;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    public void SendMail(MyAppCompatActivity activity, Session emailSession,String email, String recipient, String subject, String content){
        SendMailAsync sendMailAsync = new SendMailAsync();
        sendMailAsync.setEmail(email);
        sendMailAsync.setRecipient(recipient);
        sendMailAsync.setSubject(subject);
        sendMailAsync.setContent(content);
        sendMailAsync.setEmailSession(emailSession);
        sendMailAsync.execute(activity);
    }

    public class SendMailAsync extends AsyncTask<MyAppCompatActivity, Void, String> {


        private String Recipient, Subject, Content, Email;
        private MyAppCompatActivity activity;
        private Session emailSession;
        boolean _Sent = false;


        @Override
        protected String doInBackground(MyAppCompatActivity... activities) {
            activity = activities[0];
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(), "Sending E-mail...", Toast.LENGTH_LONG).show();
                    }
                });
                MimeMessage mm = new MimeMessage(emailSession);
                mm.setFrom(new InternetAddress(Email));
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(Recipient));
                mm.setSubject(Subject);
                mm.setText(Content);
                Transport.send(mm);
                _Sent = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            if(_Sent){
                activity.onAsyncResponse(2,1, emailSession);
            } else {
                activity.onAsyncResponse(2,0, emailSession);
            }
        }


        public void setRecipient(String recipient) {
            this.Recipient = recipient;
        }

        public void setSubject(String subject) {
            this.Subject = subject;
        }

        public void setContent(String content) {
            this.Content = content;
        }

        public void setEmail(String email) {
            this.Email = email;
        }

        public void setEmailSession(Session emailSession) {
            this.emailSession = emailSession;
        }
    }

}
