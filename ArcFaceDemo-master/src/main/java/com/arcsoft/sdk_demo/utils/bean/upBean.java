package com.arcsoft.sdk_demo.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Administrator on 2018/7/3.
 */

public class upBean {
    @JSONField(name= "empid")
    private String empid;

    @JSONField(name= "empname")
    private String empname;

    @JSONField(name= "emphao")
    private String emphao;

    @JSONField(name= "faceimage")
    private String faceimage;

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public void setEmphao(String emphao) {
        this.emphao = emphao;
    }

    public void setFaceimage(String faceimage) {
        this.faceimage = faceimage;
    }
}
