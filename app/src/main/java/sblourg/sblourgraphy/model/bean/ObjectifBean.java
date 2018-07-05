package sblourg.sblourgraphy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class ObjectifBean {

    @SerializedName("lensName")
    public String name;
    @SerializedName("focaleSelected")
    public int focale;
    @SerializedName("focaleFixe")
    public boolean focaleFixe;
    @SerializedName("focaleMin")
    public int minFocaleZoom;
    @SerializedName("focaleMax")
    public int maxFocaleZoom;

    public double apertureSelected;
    @SerializedName("aperture")
    public List<Double> aperture;
    @SerializedName("minMap")
    public double mapMini;
    public double mapSelected;

    private final static double CERCLE_DE_CONFUSION = 0.033;

    public ObjectifBean() {
    }

    //zoom
    public ObjectifBean(String name, int focale, boolean focaleFixe, int minFocaleZoom, int maxFocaleZoom, double apertureSelected, List<Double> aperture) {
        this.name = name;
        this.focale = focale;
        this.focaleFixe = focaleFixe;
        this.minFocaleZoom = minFocaleZoom;
        this.maxFocaleZoom = maxFocaleZoom;
        this.apertureSelected = apertureSelected;
        this.aperture = aperture;
    }

    //fixe
    public ObjectifBean(String name, int focale, boolean focaleFixe, double apertureSelected, List<Double> aperture) {
        this.name = name;
        this.focale = focale;
        this.focaleFixe = focaleFixe;
        this.apertureSelected = apertureSelected;
        this.aperture = aperture;
    }

/*    public double calculateHyperfocale(double focale, double aperture){
        double hyperfocale = 0;
        //Calculate in mm
        hyperfocale = (focale * focale) / (aperture * CERCLE_DE_CONFUSION);
        hyperfocale = hyperfocale/1000;
        hyperfocale=  Math.round(hyperfocale * 10.0)/10.0;
        return hyperfocale;
    }*/

    public double calculateHyperfocale(){
        double hyperfocale = 0;
        //Calculate in mm
        hyperfocale = (this.focale * this.focale) / (this.apertureSelected * CERCLE_DE_CONFUSION);
        hyperfocale = hyperfocale/1000;
        hyperfocale=  Math.round(hyperfocale * 10.0)/10.0;
        return hyperfocale;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFocale() {
        return focale;
    }

    public void setFocale(int focale) {
        this.focale = focale;
    }

    public int getMinFocaleZoom() {
        return minFocaleZoom;
    }

    public void setMinFocaleZoom(int minFocaleZoom) {
        this.minFocaleZoom = minFocaleZoom;
    }

    public int getMaxFocaleZoom() {
        return maxFocaleZoom;
    }

    public void setMaxFocaleZoom(int maxFocaleZoom) {
        this.maxFocaleZoom = maxFocaleZoom;
    }

    public double getApertureSelected() {
        return apertureSelected;
    }

    public void setApertureSelected(double apertureSelected) {
        this.apertureSelected = apertureSelected;
    }

    public boolean isFocaleFixe() {
        return focaleFixe;
    }

    public void setFocaleFixe(boolean focaleFixe) {
        this.focaleFixe = focaleFixe;
    }

    public List<Double> getAperture() {
        return aperture;
    }

    public void setAperture(List<Double> aperture) {
        this.aperture = aperture;
    }

    public double getMapMini() {
        return mapMini;
    }

    public void setMapMini(double mapMini) {
        this.mapMini = mapMini;
    }

    public double getMapSelected() {
        return mapSelected;
    }

    public void setMapSelected(double mapSelected) {
        this.mapSelected = mapSelected;
    }

    @Override
    public String toString() {
        return "ObjectifBean{" +
                "name='" + name + '\'' +
                ", focale=" + focale +
                ", focaleFixe=" + focaleFixe +
                ", minFocaleZoom=" + minFocaleZoom +
                ", maxFocaleZoom=" + maxFocaleZoom +
                ", apertureSelected=" + apertureSelected +
                ", aperture=" + aperture +
                ", mapMini=" + mapMini +
                ", mapSelected=" + mapSelected +
                '}';
    }
}
