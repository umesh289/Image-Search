package com.umesh.flickrsearch;

import java.util.List;

public class Photo {

    //"page":1,"pages":10654,"perpage":15,"total":"159798","photo"

    private int page;
    private int pages;
    private int perpage;
    private String total;
    private List<PhotoModel> photo;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<PhotoModel> getPhoto() {
        return photo;
    }

    public void setPhoto(List<PhotoModel> photo) {
        this.photo = photo;
    }
}
