package com.cyber.calculation.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Data {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "fs_up")
    public String fsUp;

    @ColumnInfo(name = "fs_x")
    public String fsX;

    @ColumnInfo(name = "fs_down")
    public String fsDown;

    @ColumnInfo(name = "fs_remark")
    public String fsRemark;

    @ColumnInfo(name = "bs_up")
    public String bsUp;

    @ColumnInfo(name = "bs_x")
    public String bsX;

    @ColumnInfo(name = "bs_down")
    public String bsDown;

    @ColumnInfo(name = "bs_remark")
    public String bsRemark;

    public Data() {
    }

    public Data(String fsUp, String fsX, String fsDown, String fsRemark, String bsUp, String bsX, String bsDown, String bsRemark) {
        this.fsUp = fsUp;
        this.fsX = fsX;
        this.fsDown = fsDown;
        this.fsRemark = fsRemark;
        this.bsUp = bsUp;
        this.bsX = bsX;
        this.bsDown = bsDown;
        this.bsRemark = bsRemark;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFsUp() {
        return fsUp;
    }

    public void setFsUp(String fsUp) {
        this.fsUp = fsUp;
    }

    public String getFsX() {
        return fsX;
    }

    public void setFsX(String fsX) {
        this.fsX = fsX;
    }

    public String getFsDown() {
        return fsDown;
    }

    public void setFsDown(String fsDown) {
        this.fsDown = fsDown;
    }

    public String getFsRemark() {
        return fsRemark;
    }

    public void setFsRemark(String fsRemark) {
        this.fsRemark = fsRemark;
    }

    public String getBsUp() {
        return bsUp;
    }

    public void setBsUp(String bsUp) {
        this.bsUp = bsUp;
    }

    public String getBsX() {
        return bsX;
    }

    public void setBsX(String bsX) {
        this.bsX = bsX;
    }

    public String getBsDown() {
        return bsDown;
    }

    public void setBsDown(String bsDown) {
        this.bsDown = bsDown;
    }

    public String getBsRemark() {
        return bsRemark;
    }

    public void setBsRemark(String bsRemark) {
        this.bsRemark = bsRemark;
    }
}