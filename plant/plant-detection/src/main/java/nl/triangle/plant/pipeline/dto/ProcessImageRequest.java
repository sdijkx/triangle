package nl.triangle.plant.pipeline.dto;

/**
 * Created by steven on 14-05-16.
 */
public class ProcessImageRequest {
    private final String imageUrl;
    private ProcessImageRequest(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
