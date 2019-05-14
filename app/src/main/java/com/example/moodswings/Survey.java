package com.example.moodswings;

import java.util.Calendar;

public class Survey {
    private Integer moodEnum;
    private String diaryEntry;
    private byte[] activities;
    private String diaryDate;

    // Constructors
    public Survey(){
        this.updateDiaryDate();
    }
    public Survey(Integer moodEnum, String diaryEntry, byte[] activities){
        this.moodEnum = moodEnum;
        this.diaryEntry = diaryEntry;
        this.activities = activities;
        this.updateDiaryDate();
    }

    // Access Functions
    public Integer getMood(){
        return moodEnum;
    }
    public String getDiaryEntry(){
        return diaryEntry;
    }
    public byte[] getActivities(){
        return activities;
    }
    public String getDiaryDate(){
        return diaryDate;
    }

    // Modify Functions
    public void setMood(Integer newMood){
        moodEnum = newMood;
        this.updateDiaryDate();
    }
    public void setDiaryEntry(String newEntry){
        diaryEntry = newEntry;
        this.updateDiaryDate();
    }
    public void setActivities(byte[] newActivities){
        activities = newActivities;
        this.updateDiaryDate();
    }
    private void updateDiaryDate(){
        diaryDate = this.getTodaysDate();
    }
    public void updateSurvey(Integer newMood, String newEntry, byte[] newActivities){
        moodEnum = newMood;
        diaryEntry = newEntry;
        activities = newActivities;
        this.updateDiaryDate();
    }
    public String getTodaysDate(){
        return Calendar.getInstance().get(Calendar.DATE) + "/"
                + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/"
                + Calendar.getInstance().get(Calendar.YEAR);
    }
}