package zhaohg.api.image;

import zhaohg.json.JsonObject;

public class Image {

    private String imageId;
    private String imagePath;
    private String thumbnailPath;

    public Image() {
        this.imageId = "";
        this.imagePath = "";
        this.thumbnailPath = "";
    }

    public Image(JsonObject postObject) {
        this.imageId = postObject.getValue("image_id").getString();
        this.imagePath = postObject.getValue("image_path").getString();
        this.thumbnailPath = postObject.getValue("thumbnail_path").getString();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

}
