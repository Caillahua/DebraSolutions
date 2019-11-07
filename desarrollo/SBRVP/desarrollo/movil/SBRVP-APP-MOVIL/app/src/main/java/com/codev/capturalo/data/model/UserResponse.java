package com.codev.capturalo.data.model;

import java.io.Serializable;

public class UserResponse implements Serializable {

    private String type;
    private String gender;
    private String birth_date;
    private String district;
    private String[] musicgenre_users;
    private String[] instrument_users;
    private boolean register_complete = true;

    public UserResponse(String type, String gender, String birth_date, String district, String[] musicgenre_users, String[] instrument_users) {
        this.type = type;
        this.gender = gender;
        this.birth_date = birth_date;
        this.district = district;
        this.musicgenre_users = musicgenre_users;
        this.instrument_users = instrument_users;
    }

    public boolean isRegister_complete() {
        return register_complete;
    }

    public void setRegister_complete(boolean register_complete) {
        this.register_complete = register_complete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String[] getMusicgenre_users() {
        return musicgenre_users;
    }

    public void setMusicgenre_users(String[] musicgenre_users) {
        this.musicgenre_users = musicgenre_users;
    }

    public String[] getInstrument_users() {
        return instrument_users;
    }

    public void setInstrument_users(String[] instrument_users) {
        this.instrument_users = instrument_users;
    }
}
