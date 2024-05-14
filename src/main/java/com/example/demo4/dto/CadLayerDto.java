package com.example.demo4.dto;

import java.io.Serializable;

public class CadLayerDto implements Serializable {

    private String name;
    private boolean isVisible;

    public CadLayerDto() {
    }

    public CadLayerDto(String name, boolean isVisible) {
        this.name = name;
        this.isVisible = isVisible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
