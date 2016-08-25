package com.example.svaiyapu.petutrecht.data.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteResponse {

    private Integer offset;
    private List<Pet> data = new ArrayList<Pet>();
    private String nextPage;
    private Integer totalObjects;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @Override
    public String toString() {
        return "RemoteResponse{" +
                "offset=" + offset +
                ", data=" + data +
                ", nextPage='" + nextPage + '\'' +
                ", totalObjects=" + totalObjects +
                ", additionalProperties=" + additionalProperties +
                '}';
    }

    public List<Pet> getData() {
        return data;
    }
}
