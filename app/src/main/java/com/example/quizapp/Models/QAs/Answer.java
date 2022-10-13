package com.example.quizapp.Models.QAs;

import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("choiceId")
    private int choiceid;

    @SerializedName("choice")
    private String Answer;

    @SerializedName("rightChoice")
    private int RightChoice;

    public int getChoiceid() {
        return choiceid;
    }

    public String getAnswer() {
        return Answer;
    }

    public int getRightChoice(){ return RightChoice; }


}
