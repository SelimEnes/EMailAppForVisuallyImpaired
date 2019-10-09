package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

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
                Properties properties = new Properties();

                properties.put("mail.pop3s.host", host);
                properties.put("mail.pop3s.port", "995");
                properties.put("mail.pop3s.starttls.enable", "true");


                emailSession = Session.getInstance(properties,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        email, pass);
                            }
                        });

                setLoggedIn(false);

                Store store = emailSession.getStore("pop3s");
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
                Log.e("Mail", e.getLocalizedMessage());
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
                activity.onAsyncResponse(1, emailSession);
            } else {
                activity.onAsyncResponse(0, emailSession);
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

}
