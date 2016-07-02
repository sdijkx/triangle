package nl.triangle.plant.pipeline.dto;

import java.util.List;

/**
 * Created by steven on 14-05-16.
 */
public class ProcessResult {

    private final String result;
    private final List<RootModel> rootModelList;
    private final List<DropModel> dropModelList;
    private final List<PlantModel> plantModelList;

    public ProcessResult(String result) {
        this.result = result;
        this.dropModelList = null;
        this.plantModelList = null;
        this.rootModelList = null;
    }

    public ProcessResult(String result, List<DropModel> dropModelList, List<PlantModel> plantModelList, List<RootModel> rootModelList) {
        this.result = result;
        this.rootModelList = rootModelList;
        this.dropModelList = dropModelList;
        this.plantModelList = plantModelList;
    }

    public String getResult() {
        return result;
    }

    public List<DropModel> getDropModelList() {
        return dropModelList;
    }

    public List<PlantModel> getPlantModelList() {
        return plantModelList;
    }

    public List<RootModel> getRootModelList() {
        return rootModelList;
    }
}
