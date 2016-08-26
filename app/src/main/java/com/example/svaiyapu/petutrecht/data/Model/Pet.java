package com.example.svaiyapu.petutrecht.data.Model;


import java.util.HashMap;
import java.util.Map;

public class Pet {

    private String img_colour;
    private String Breed;
    private String Description;
    private Integer img_width;
    private Long created;
    private Integer img_height;
    private String Gender;
    private Object ownerId;
    private String Name;
    private String __meta;
    private String Img_secondary;
    private String Type;
    private String Img_primary;
    private String ___class;
    private Long updated;
    private String Age;
    private String objectId;
    private Location Location;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getImg_colour() {
        return img_colour.trim();
    }

    public String getBreed() {
        return Breed;
    }

    public String getDescription() {
        return Description;
    }

    public Integer getImg_width() {
        return img_width;
    }

    public Integer getImg_height() {
        return img_height;
    }

    public String getGender() {
        return Gender;
    }

    public String getName() {
        return Name;
    }

    public String getImg_secondary() {
        return Img_secondary;
    }

    public String getImg_primary() {
        return Img_primary;
    }

    public String getType() {
        return Type;
    }

    public String getAge() {
        return Age;
    }

    public com.example.svaiyapu.petutrecht.data.Model.Location getLocation() {
        return Location;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "img_colour='" + img_colour + '\'' +
                ", Breed='" + Breed + '\'' +
                ", Description='" + Description.substring(0, 10) + "..." + '\'' +
                ", img_width=" + img_width +
                ", created=" + created +
                ", img_height=" + img_height +
                ", Gender='" + Gender + '\'' +
                ", ownerId=" + ownerId +
                ", Name='" + Name + '\'' +
                ", __meta='" + __meta + '\'' +
                ", Img_secondary='" + Img_secondary + '\'' +
                ", Type='" + Type + '\'' +
                ", Img_primary='" + Img_primary + '\'' +
                ", ___class='" + ___class + '\'' +
                ", updated=" + updated +
                ", Age='" + Age + '\'' +
                ", objectId='" + objectId + '\'' +
                ", Location=" + Location +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}