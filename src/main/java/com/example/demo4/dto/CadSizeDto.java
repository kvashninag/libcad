package com.example.demo4.dto;

import java.io.Serializable;

public class CadSizeDto implements Serializable {

    private double width;
    private double height;
    private double depth;

    public CadSizeDto() {
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "CadSizeDto{" +
                "width=" + width +
                ", height=" + height +
                ", depth=" + depth +
                '}';
    }
}
