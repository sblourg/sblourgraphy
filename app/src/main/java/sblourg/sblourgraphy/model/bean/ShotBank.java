package sblourg.sblourgraphy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ShotBank {

    @SerializedName("shot")
    public List<ShotBean> shotBank = new ArrayList<ShotBean>();

    public ShotBank() {
    }

    public List<ShotBean> getShotBank() {
        return shotBank;
    }

    public void setShotBank(List<ShotBean> shotBank) {
        this.shotBank = shotBank;
    }

    @Override
    public String toString() {
        return "ShotBank{" +
                "shotBank=" + shotBank +
                '}';
    }
}

