package com.hhly.award.persistence.po;

import java.util.Date;

public class SportDrawWFPO {
    private Long id;

    private Long sportAgainstInfoId;

    private String fullScore;

    private Double letNum;

    private Short letSf;

    private Double spLetWf;

    private Date drawTime;

    private String modifyBy;

    private Date modifyTime;

    private Date updateTime;

    private Date createTime;


    public SportDrawWFPO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSportAgainstInfoId() {
        return sportAgainstInfoId;
    }

    public void setSportAgainstInfoId(Long sportAgainstInfoId) {
        this.sportAgainstInfoId = sportAgainstInfoId;
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore == null ? null : fullScore.trim();
    }

    public Double getLetNum() {
        return letNum;
    }

    public void setLetNum(Double letNum) {
        this.letNum = letNum;
    }

    public Short getLetSf() {
        return letSf;
    }

    public void setLetSf(Short letSf) {
        this.letSf = letSf;
    }

    public Double getSpLetWf() {
        return spLetWf;
    }

    public void setSpLetWf(Double spLetWf) {
        this.spLetWf = spLetWf;
    }

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}