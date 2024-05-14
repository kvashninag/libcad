package com.example.demo4.dto;

import java.io.Serializable;

public class CadPointDto implements Serializable {

    /*
    Left along axis x.
    */
    private double x;
    /*
    Top along axis y.
     */
    private double y;
    /*
    Angle relative to x-axis.
     */
    private double angle;

    public CadPointDto() {
    }

    public CadPointDto(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "CadPointDto{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                '}';
    }
}
