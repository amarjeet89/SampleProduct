package com.prafull.product.pojo;

/**
 * Created by Amar on 03-08-2015.
 */
public class Plate {
    String title;
    int qty;
    int price;
    public boolean box;

    public Plate(String title, int qty, int price,boolean box) {
        this.title = title;
        this.qty = qty;
        this.price = price;
        this.box=box;
    }

    public String getTitle() {
        return title;
    }

    public int getQty() {
        return qty;
    }

    public int getPrice() {
        return price;
    }
}
