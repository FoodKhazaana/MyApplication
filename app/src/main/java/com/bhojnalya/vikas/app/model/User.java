package com.bhojnalya.vikas.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviAgrawal on 16-05-2017.
 */

public class User implements Parcelable {
    public int userId;
    public String userName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.userName);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.userId = in.readInt();
        this.userName = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
