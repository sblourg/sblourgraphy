package sblourg.sblourgraphy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShotBean {
    public ObjectifBean objectif;
    public int isoSelected;
    @SerializedName("iso")
    public List<Integer> isoList;
    public String exposureSelected;
    @SerializedName("exposure")
    public List<String> exposureList;

    public ShotBean() {
    }

    public ObjectifBean getObjectif() {
        return objectif;
    }

    public void setObjectif(ObjectifBean objectif) {
        this.objectif = objectif;
    }

    public int getIsoSelected() {
        return isoSelected;
    }

    public void setIsoSelected(int isoSelected) {
        this.isoSelected = isoSelected;
    }

    public List<Integer> getIsoList() {
        return isoList;
    }

    public void setIsoList(List<Integer> isoList) {
        this.isoList = isoList;
    }

    public String getExposureSelected() {
        return exposureSelected;
    }

    public void setExposureSelected(String exposureSelected) {
        this.exposureSelected = exposureSelected;
    }

    public List<String> getExposureList() {
        return exposureList;
    }

    public void setExposureList(List<String> exposureList) {
        this.exposureList = exposureList;
    }
}
