package com.example.evaluate;

public class Evaluate {
    private String name;
    private String theme;
    private String time;
    private String state;
    private float star;
    private String content;

    public Evaluate(String name, String theme, String time,String state,float star,String content) {
        this.name = name;
        this.theme = theme;
        this.time = time;
        this.state = state;
        this.star = star;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState(){
        return  state;
    }

    public void setState(String state){
        this.state = state;
    }

    public float getStar(){
        return star;
    }

    public void setStar(float star){
        this.star = star;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}

