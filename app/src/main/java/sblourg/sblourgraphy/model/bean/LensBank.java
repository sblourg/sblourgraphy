package sblourg.sblourgraphy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LensBank {
    @SerializedName("lens")
    public List<ObjectifBean> lensBank = new ArrayList<ObjectifBean>();

    public LensBank() {
    }

    public List<String> getLensListName() {

        List<String> lensListName = new ArrayList<String>();
        for (ObjectifBean objectif : lensBank) {
            lensListName.add(objectif.name);
        }
        return lensListName;
    }


    public List<ObjectifBean> getLensBank() {
        return lensBank;
    }

    public void setLensBank(List<ObjectifBean> lensBank) {
        this.lensBank = lensBank;
    }

    @Override
    public String toString() {
        return "LensBank{" +
                "lensBank=" + lensBank +
                '}';
    }

}
