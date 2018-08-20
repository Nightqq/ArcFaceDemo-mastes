package com.arcsoft.sdk_demo.utils.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PrisonerInfo {

    @Id
    private Long id;
    private String crime_id;
    private String crime_name;
    private String crime_jianqu;
    private String crime_xb;
    private String crime_featuredata;
    @Generated(hash = 1691585836)
    public PrisonerInfo(Long id, String crime_id, String crime_name,
            String crime_jianqu, String crime_xb, String crime_featuredata) {
        this.id = id;
        this.crime_id = crime_id;
        this.crime_name = crime_name;
        this.crime_jianqu = crime_jianqu;
        this.crime_xb = crime_xb;
        this.crime_featuredata = crime_featuredata;
    }
    @Generated(hash = 1587383974)
    public PrisonerInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCrime_id() {
        return this.crime_id;
    }
    public void setCrime_id(String crime_id) {
        this.crime_id = crime_id;
    }
    public String getCrime_name() {
        return this.crime_name;
    }
    public void setCrime_name(String crime_name) {
        this.crime_name = crime_name;
    }
    public String getCrime_jianqu() {
        return this.crime_jianqu;
    }
    public void setCrime_jianqu(String crime_jianqu) {
        this.crime_jianqu = crime_jianqu;
    }
    public String getCrime_xb() {
        return this.crime_xb;
    }
    public void setCrime_xb(String crime_xb) {
        this.crime_xb = crime_xb;
    }
    public String getCrime_featuredata() {
        return this.crime_featuredata;
    }
    public void setCrime_featuredata(String crime_featuredata) {
        this.crime_featuredata = crime_featuredata;
    }

    @Override
    public String toString() {
        return "PrisonerInfo{" +
                "id=" + id +
                ", crime_id='" + crime_id + '\'' +
                ", crime_name='" + crime_name + '\'' +
                ", crime_jianqu='" + crime_jianqu + '\'' +
                ", crime_xb='" + crime_xb + '\'' +
                ", crime_featuredata='" + crime_featuredata + '\'' +
                '}';
    }
}
