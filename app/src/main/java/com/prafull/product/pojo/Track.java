package com.prafull.product.pojo;

/**
 * Created by SHUBHANSU on 9/6/2015.
 */
public class Track {
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String publisher;
    String title;
    String url;

    public Track(String publisher, String title, String url){
        this.publisher = publisher;
        this.title = title;
        this.url = url;
    }


}
