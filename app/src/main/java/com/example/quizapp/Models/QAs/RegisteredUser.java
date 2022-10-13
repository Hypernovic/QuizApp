package com.example.quizapp.Models.QAs;

public class RegisteredUser {

    private String userName,school,city,firebaseUID;
    private Integer grade;


    public RegisteredUser(String userName, String school, String city, String firebaseUID, Integer grade) {
        this.userName = userName;
        this.school = school;
        this.city = city;
        this.firebaseUID = firebaseUID;
        this.grade = grade;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirebaseUID() {
        return firebaseUID;
    }

    public void setFirebaseUID(String firebaseUID) {
        this.firebaseUID = firebaseUID;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
