package com.cliff.ozbargain.model;

/**
 * Created by Clifford on 20/11/2015.
 */
public class MenuItem {
    private int iconId;
    private String title;

    public MenuItem(String menu, int menuIcon) {
        setIconId(menuIcon);
        setTitle(menu);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }


}
