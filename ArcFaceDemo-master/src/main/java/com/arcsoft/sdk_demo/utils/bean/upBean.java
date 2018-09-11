package com.arcsoft.sdk_demo.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Administrator on 2018/7/3.
 */

public class upBean {
    @JSONField(name= "crime_id")
    private String crime_id;

    @JSONField(name= "crime_name")
    private String crime_name;

    @JSONField(name= "crime_xb")
    private String crime_xb;

    @JSONField(name= "crime_sfrq")
    private String crime_sfrq;

    @JSONField(name= "crime_jianqu")
    private String crime_jianqu;

    @JSONField(name= "crime_cjyy")
    private String crime_cjyy;

    @JSONField(name= "faceimage")
    private String faceimage;

    public void setCrime_id(String crime_id) {
        this.crime_id = crime_id;
    }

    public void setCrime_name(String crime_name) {
        this.crime_name = crime_name;
    }

    public void setCrime_xb(String crime_xb) {
        this.crime_xb = crime_xb;
    }

    public void setCrime_sfrq(String crime_sfrq) {
        this.crime_sfrq = crime_sfrq;
    }

    public void setCrime_jianqu(String crime_jianqu) {
        this.crime_jianqu = crime_jianqu;
    }

    public void setCrime_cjyy(String crime_cjyy) {
        this.crime_cjyy = crime_cjyy;
    }

    public void setFaceimage(String faceimage) {
        this.faceimage = faceimage;
    }
}
