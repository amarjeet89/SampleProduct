package com.prafull.product.pojo;

import java.util.ArrayList;

/**
 * Created by Shubhansu  on 2/8/2015.
 */
public class NavigationMenu {

    private int icon;
    private String title;
    private boolean isNavIconVisible = false;
    private ArrayList<String> children;

    public NavigationMenu(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    /*public NavigationMenu(String title, int icon, ArrayList<String> children){
        this.title = title;
        this.icon = icon;
        this.children = children;
    }*/
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isNavIconVisible() {
        return isNavIconVisible;
    }

    public void setNavIconVisible(boolean isNavIconVisible) {
        this.isNavIconVisible = isNavIconVisible;
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

}
