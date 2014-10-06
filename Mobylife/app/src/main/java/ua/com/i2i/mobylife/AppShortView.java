package ua.com.i2i.mobylife;

import android.graphics.Bitmap;


public class AppShortView {
    private String name;
    private Integer id;
    private String image_url;
    private Bitmap image;
    private String tags;

    public AppShortView(String nameP, Integer idP, String image_urlP, String tagsP){
        name = nameP;
        id = idP;
        image_url = image_urlP;
        tags = tagsP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
