package com.example.enviarcorreocontactos.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Contacto implements Parcelable {
    private String name;
    private String number;
    private String email;


    public Contacto(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }


    protected Contacto(Parcel in) {
        name = in.readString();
        number = in.readString();
        email = in.readString();
    }

    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(email);
    }
}
