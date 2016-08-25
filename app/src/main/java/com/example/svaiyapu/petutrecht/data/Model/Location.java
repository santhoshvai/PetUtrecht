package com.example.svaiyapu.petutrecht.data.Model;

/**
 * Created by svaiyapu on 8/19/16.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {

    private Metadata metadata;
    private Double latitude;
    private String _class;
    private List<String> categories = new ArrayList<String>();
    private String objectId;
    private Double longitude;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "metadata=" + metadata +
                ", latitude=" + latitude +
                ", _class='" + _class + '\'' +
                ", categories=" + categories +
                ", objectId='" + objectId + '\'' +
                ", longitude=" + longitude +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
