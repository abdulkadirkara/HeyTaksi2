package com.ismek.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class LoginResponse implements Parcelable {

    public long id;
    public String name;
    public Date birthday;
    public String lastName;
    public String email;
    public String password;
    public String plate;
    public String phone;
    public String userType;
    public String regId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.birthday != null ? this.birthday.getTime() : -1);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.plate);
        dest.writeString(this.phone);
        dest.writeString(this.userType);
        dest.writeString(this.regId);
    }

    public LoginResponse() {
    }

    protected LoginResponse(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        long tmpBirthday = in.readLong();
        this.birthday = tmpBirthday == -1 ? null : new Date(tmpBirthday);
        this.lastName = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.plate = in.readString();
        this.phone = in.readString();
        this.userType = in.readString();
        this.regId = in.readString();
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel source) {
            return new LoginResponse(source);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };
}
