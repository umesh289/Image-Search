package com.umesh.flickrsearch;

//    Flickr Photo model
//    {
//        "id":"23451156376",
//        "owner":"28017113@N08",
//        "secret":"8983a8ebc7",
//        "server":"578",
//        "farm":1,
//        "title":"Merry Christmas!",
//        "ispublic":1,
//        "isfriend":0,
//        "isfamily":0
//    }

public class PhotoModel {

    private String id;
    private String owner;
    private String secret;

    private String server;
    private int farm;
    private String title;

    private int ispublic;
    private int isfriend;
    private int isfamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIspublic() {
        return ispublic;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }
}
