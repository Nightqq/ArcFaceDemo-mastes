package com.arcsoft.sdk_demo.utils.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class IsCallInfo {
    @Id
    private Long id;
    private String crime_id;
    private String crime_name;
    private String crime_jianqu;
    private String crime_xb;

    private boolean iscall;
    private String callphoto_time;
    private String crime_photo;
    @Generated(hash = 1895244628)
    public IsCallInfo(Long id, String crime_id, String crime_name,
            String crime_jianqu, String crime_xb, boolean iscall,
            String callphoto_time, String crime_photo) {
        this.id = id;
        this.crime_id = crime_id;
        this.crime_name = crime_name;
        this.crime_jianqu = crime_jianqu;
        this.crime_xb = crime_xb;
        this.iscall = iscall;
        this.callphoto_time = callphoto_time;
        this.crime_photo = crime_photo;
    }
    @Generated(hash = 167564180)
    public IsCallInfo() {
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
    public boolean getIscall() {
        return this.iscall;
    }
    public void setIscall(boolean iscall) {
        this.iscall = iscall;
    }
    public String getCallphoto_time() {
        return this.callphoto_time;
    }
    public void setCallphoto_time(String callphoto_time) {
        this.callphoto_time = callphoto_time;
    }
    public String getCrime_photo() {
        return this.crime_photo;
    }
    public void setCrime_photo(String crime_photo) {
        this.crime_photo = crime_photo;
    }
}
