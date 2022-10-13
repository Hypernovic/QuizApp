package com.example.quizapp.Models.QAs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionAnswers {

    @SerializedName("id")
    private int questionId;

    @SerializedName("question")
    private String Question;

    @SerializedName("choices")
    private List<Answer> Answers;

    private int correctChoice=0;

    QuestionAnswers(int questionId, String question,List<Answer> answers) {
        this.Question = question;
        this.Answers = answers;
        this.questionId=questionId;
    }

    public String getQuestion(){
        return Question;
    }

    public int getQuestionId(){
        return questionId;
    }

    public List<Answer> getAnswers(){
        return Answers;
    }

    public void setCorrectChoice(int correctChoice) {
        this.correctChoice = correctChoice;
    }

    public int getCorrectChoice() {
        return correctChoice;
    }
//    public  int answerCount(){
//        return Answers.size();
//    };

}
