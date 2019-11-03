package com.meeting.meetingapp.Model;

public class Item {

    String name;
    String description;
    int price;
    int imgDrawable;

    public Item(String newName, String newDesc, int newPrice, int newImgDrawable){
        this.name = newName;
        this.description = newDesc;
        this.price = newPrice;
        this.imgDrawable = newImgDrawable;
    }

    public String getName(){
        return this.name;
    }

    public String getDesc(){
        return this.description;
    }

    public int getPrice(){
        return this.price;
    }

    public int getImgDrawable(){
        return this.imgDrawable;
    }
}