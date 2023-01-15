package com.erenbelli.finalmessage;

import java.util.List;

public class GroupModel {
    private String name, explanation, img, uid;
    private List<String> numbers;


    public GroupModel(){

    }

    public GroupModel(String name,String explanation, String img, List<String> numbers, String uid){
        this.name = name;
        this.explanation = explanation;
        this.img = img;
        this.numbers = numbers;
        this.uid = uid;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;

    }

    public String getExplanation(){
        return explanation;
    }

    public void setExplanation(String explanation){
        this.explanation = explanation;
    }

    public String getImg(){
        return img;
    }

    public void setImg(String img){
        this.img = img;
    }

    public List<String> getNumbers(){
        return numbers;
    }

    public void setNumbers(List<String> numbers){
        this.numbers = numbers;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }



}
