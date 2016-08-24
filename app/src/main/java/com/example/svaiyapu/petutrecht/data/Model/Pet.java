package com.example.svaiyapu.petutrecht.data.Model;


import java.util.HashMap;
import java.util.Map;

public class Pet {

    private String Breed;
    private String Description;
    private Long created;
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

    @Override
    public String toString() {
        return "Pet{" +
                "Breed='" + Breed + '\'' +
                // truncate description to 10 chars
                ", Description='" + Description.substring(0, Math.min(Description.length(), 10)) + '\'' +
                ", created=" + created +
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