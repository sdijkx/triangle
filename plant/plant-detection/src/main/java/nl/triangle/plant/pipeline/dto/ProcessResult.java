package nl.triangle.plant.pipeline.dto;

import java.util.List;

/**
 * Created by steven on 14-05-16.
 */
public class ProcessResult {

    private final String result;
    private final List<PlantImageModel> plantImageModelList;

    public ProcessResult(String result) {
        this.result = result;
        this.plantImageModelList = null;
    }

    public ProcessResult(String result,  List<PlantImageModel> plantImageModelList) {
        this.result = result;
        this.plantImageModelList = plantImageModelList;
    }

    public String getResult() {
        return result;
    }


    public List<PlantImageModel> getPlantImageModelList() {
        return plantImageModelList;
    }
}
