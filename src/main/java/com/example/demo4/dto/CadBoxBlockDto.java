package com.example.demo4.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CadBoxBlockDto implements Serializable {

    private String name;

    private CadSizeDto size;
    private List<CadPointDto> points;

    public CadBoxBlockDto() {
        size = new CadSizeDto();
        points = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CadSizeDto getSize() {
        return size;
    }

    public void setSize(CadSizeDto size) {
        this.size = size;
    }

    public List<CadPointDto> getPoints() {
        return points;
    }

    public void setPoints(List<CadPointDto> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "CadBoxBlockDto{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", points=" + points +
                '}';
    }
}
