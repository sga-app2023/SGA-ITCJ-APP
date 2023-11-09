package com.example.rsu_itcjapp.listView;

public class DatosItemMenu {

    private String name;
    private int iconImage;

    public DatosItemMenu(String name, int iconImage) {
        this.name = name;
        this.iconImage = iconImage;
    }

    public String getName() {
        return name;
    }

    public int getIconImage() {
        return iconImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }
}
