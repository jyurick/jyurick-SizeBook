package com.example.annaholowaychuk.clothingapp;

import java.util.Date;

/**
 * This class represents a Person object. This object will be used to store all data about
 * the Person's dimensions.
 */

public class Person {
    private String name = "";
    private Date date;
    private Float neck = Float.valueOf(0);
    private Float bust = Float.valueOf(0);;
    private Float chest = Float.valueOf(0);;
    private Float waist = Float.valueOf(0);;
    private Float hip = Float.valueOf(0);;
    private Float inseam = Float.valueOf(0);;
    private String comment = "";

    public Person(){
        this.date = new Date();
    }

    public Person(String name) {
        this.name = name;
        this.date = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNeck() {
        if (neck > 0) {
             return Float.toString(neck);
        }
        else {
            return "";
        }

    }

    public void setNeck(Float neck) {
        this.neck = neck;
    }

    public String getBust() {

        if (bust > 0) {
            return Float.toString(bust);
        }
        else {
            return "";
        }
    }

    public void setBust(Float bust) {
        this.bust = bust;
    }

    public String getChest() {

        if (chest > 0) {
            return Float.toString(chest);
        }
        else {
            return "";
        }
    }

    public void setChest(Float chest) {
        this.chest = chest;
    }

    public String getWaist() {

        if (waist > 0) {
            return Float.toString(waist);
        }
        else {
            return "";
        }
    }

    public void setWaist(Float waist) {
        this.waist = waist;
    }

    public String getHip() {

        if (hip > 0) {
            return Float.toString(hip);
        }
        else {
            return "";
        }
    }

    public void setHip(Float hip) {
        this.hip = hip;
    }

    public String getInseam() {

        if (inseam > 0) {
            return Float.toString(inseam);
        }
        else {
            return "";
        }
    }

    public void setInseam(Float inseam) {
        this.inseam = inseam;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        String s = this.name + " || ";
        if (this.bust > 0) {
            s = s + " Bust:" + String.format("%.1f",this.bust);
        }
        if (this.chest > 0) {
            s = s + " Chest:" + String.format("%.1f",this.chest);
        }
        if (this.waist > 0) {
            s = s + " Waist:" + String.format("%.1f",this.waist);
        }
        if (this.inseam > 0) {
            s = s + " Inseam:" + String.format("%.1f",this.inseam);
        }

        return s;}


}
