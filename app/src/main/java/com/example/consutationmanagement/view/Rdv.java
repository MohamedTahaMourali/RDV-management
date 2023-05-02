package com.example.consutationmanagement.view;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rdv {
    private String numTel,desc,date,time;

    public Rdv(String numTel, String desc, String date, String time) {
        this.numTel = numTel;
        this.desc = desc;
        this.date = date;
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return numTel+" "+desc+" "+date+" "+time;
    }
}
