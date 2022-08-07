package com.example.quanlysinhvien.DTO;

public class Account {
    private String user_Name,pass_Word;

    public Account(String user_Name, String pass_Word) {
        this.user_Name = user_Name;
        this.pass_Word = pass_Word;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public String getPass_Word() {
        return pass_Word;
    }

    public void setPass_Word(String pass_Word) {
        this.pass_Word = pass_Word;
    }
}
