package sblourg.sblourgraphy.model.bean;

import com.google.gson.annotations.SerializedName;



import java.util.List;



public class ShotBean {

    public long isoSelected;
    @SerializedName("iso")
    public List<Long> isoList;

    public String exposureSelected;
    @SerializedName("exposure")
    public List<String> exposureList;

    public double apertureSelected;
    @SerializedName("apertureShot")
    public List<Double> apertureList;


    public final static double K2=3.125;
    public final static String SEPARATOR = "/";


    public double Nv, Tv, Lv, Sv, L, apertureCalculated, isoCalculated, expoCalculated;

    public ShotBean() {
    }

    double parseStringFractionToDouble(String ratio) {
        if (ratio.contains(SEPARATOR)) {
            String[] rat = ratio.split(SEPARATOR);
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }


    public double calculateLv() {
        Lv = 0;
        this.Nv = 2 * log2(this.apertureSelected);
        this.Tv = - log2(parseStringFractionToDouble(this.exposureSelected));
        this.Sv = log2(this.isoSelected / K2);
        this.Lv = Nv + Tv - Sv;

        return Lv;
    }

    public double calculateISO() {
        isoCalculated = 0;
        Sv = - this.Lv + 2 * log2(this.apertureSelected) - log2(parseStringFractionToDouble(this.exposureSelected));
        isoCalculated = Math.pow (2, Sv) *K2;
        return Math.round(isoCalculated*1)/1;
    }

    public Double calculateEXPO() {
        expoCalculated = 0;
        Tv = log2(this.isoSelected / K2) + this.Lv - 2 * log2(this.apertureSelected);
        expoCalculated = 1/Math.pow (2,Tv);
        expoCalculated = Math.round(expoCalculated*100000.0)/100000.0;

        return expoCalculated;
    }

    public double calculateAPERTURE() {
        apertureCalculated = 0;
        Nv = this.Lv - (- log2(parseStringFractionToDouble(this.exposureSelected))) + (log2(this.isoSelected / K2));
        apertureCalculated = Math.pow(2, Nv / 2);
        return Math.round(apertureCalculated*10.0)/10.0;
    }

    // log2:  Logarithm base 2
    public static double log2(double d) {
        return Math.log(d)/Math.log(2.0);
    }

    public long getIsoSelected() {
        return isoSelected;
    }

    public void setIsoSelected(long isoSelected) {
        this.isoSelected = isoSelected;
    }

    public List<Long> getIsoList() {
        return isoList;
    }

    public void setIsoList(List<Long> isoList) {
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

    public double getApertureSelected() {
        return apertureSelected;
    }

    public void setApertureSelected(double apertureSelected) {
        this.apertureSelected = apertureSelected;
    }

    public List<Double> getApertureList() {
        return apertureList;
    }

    public void setApertureList(List<Double> apertureList) {
        this.apertureList = apertureList;
    }


    public double getNv() {
        return Nv;
    }

    public void setNv(double nv) {
        Nv = nv;
    }

    public double getTv() {
        return Tv;
    }

    public void setTv(double tv) {
        Tv = tv;
    }

    public double getLv() {
        return Lv;
    }

    public void setLv(double lv) {
        Lv = lv;
    }

    public double getSv() {
        return Sv;
    }

    public void setSv(double sv) {
        Sv = sv;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    @Override
    public String toString() {
        return "ShotBean{" +
                "isoSelected=" + isoSelected +
                ", isoList=" + isoList +
                ", exposureSelected='" + exposureSelected + '\'' +
                ", exposureList=" + exposureList +
                ", apertureSelected=" + apertureSelected +
                ", apertureList=" + apertureList +
                '}';
    }
}
