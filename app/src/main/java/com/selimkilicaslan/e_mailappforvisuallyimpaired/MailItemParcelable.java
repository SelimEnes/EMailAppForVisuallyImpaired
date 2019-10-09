package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.os.Parcel;
import android.os.Parcelable;

public class MailItemParcelable implements Parcelable{
    private String Subject, Sender, Content, Date;
    public MailItemParcelable(String _Subject, String _Sender, String _Content, String _Date){
        setSubject(_Subject);
        setSender(_Sender);
        setContent(_Content);
        setDate(_Date);
    }

    public MailItemParcelable(Parcel in) {
        this.Subject = in.readString();
        this.Sender = in.readString();
        this.Content = in.readString();
        this.Date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Subject);
        dest.writeString(this.Sender);
        dest.writeString(this.Content);
        dest.writeString(this.Date);
    }

    public static final Parcelable.Creator<MailItemParcelable> CREATOR = new Parcelable.Creator<MailItemParcelable>() {

        public MailItemParcelable createFromParcel(Parcel in) {
            return new MailItemParcelable(in);
        }

        public MailItemParcelable[] newArray(int size) {
            return new MailItemParcelable[size];
        }
    };

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
