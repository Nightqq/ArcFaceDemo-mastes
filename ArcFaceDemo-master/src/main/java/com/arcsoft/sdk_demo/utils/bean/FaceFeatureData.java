package com.arcsoft.sdk_demo.utils.bean;

/**
 * Created by Administrator on 2018/6/29.
 */

public class FaceFeatureData {

    /**
     * id : 1
     * emp_id : 123456
     * emp_name : 警察1
     * emp_jhao : 1231111111
     * featuredata : ry6D1
     */

    private int id;
    private String crime_id;
    private String crime_name;
    private String crime_jianqu;
    private String crime_xb;
    private String crime_featuredata;

    @Override
    public String toString() {
        return "FaceFeatureData{" +
                "id=" + id +
                ", crime_id='" + crime_id + '\'' +
                ", crime_name='" + crime_name + '\'' +
                ", crime_jianqu='" + crime_jianqu + '\'' +
                ", crime_xb='" + crime_xb + '\'' +
                ", crime_featuredata='" + crime_featuredata + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCrime_id() {
        return crime_id;
    }

    public void setCrime_id(String crime_id) {
        this.crime_id = crime_id;
    }

    public String getCrime_name() {
        return crime_name;
    }

    public void setCrime_name(String crime_name) {
        this.crime_name = crime_name;
    }

    public String getCrime_jianqu() {
        return crime_jianqu;
    }

    public void setCrime_jianqu(String crime_jianqu) {
        this.crime_jianqu = crime_jianqu;
    }

    public String getCrime_xb() {
        return crime_xb;
    }

    public void setCrime_xb(String crime_xb) {
        this.crime_xb = crime_xb;
    }

    public String getCrime_featuredata() {
        return crime_featuredata;
    }

    public void setCrime_featuredata(String crime_featuredata) {
        this.crime_featuredata = crime_featuredata;
    }
}
