package com.hhly.award.bo;

import java.util.Date;
/**
 * 
 * @ClassName: SportDrawBJBO 
 * @Description: 北京单场彩果表
 * @author wuLong
 * @date 2017年5月25日 下午5:19:10 
 *
 */
public class SportDrawBJBO {
    private Long id;

    private Long sportAgainstInfoId;

    private String halfScore;

    private String fullScore;

    private Integer letNum;

    private Integer letWdf;

    private Integer goalNum;

    private String hfWdf;

    private Integer udSd;

    private String score;

    private Double spLetWdf;

    private Double spGoalNum;

    private Double spHfWdf;

    private Double spUdSd;

    private Double spScore;

    private Date drawTime;

    private String modifyBy;

    private Date modifyTime;

    private Date updateTime;

    private Date createTime;

    public SportDrawBJBO() {
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

    public String getHalfScore() {
        return halfScore;
    }

    public void setHalfScore(String halfScore) {
        this.halfScore = halfScore == null ? null : halfScore.trim();
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore == null ? null : fullScore.trim();
    }


    public String getHfWdf() {
        return hfWdf;
    }

    public void setHfWdf(String hfWdf) {
        this.hfWdf = hfWdf == null ? null : hfWdf.trim();
    }


    public Integer getLetNum() {
		return letNum;
	}

	public void setLetNum(Integer letNum) {
		this.letNum = letNum;
	}

	public Integer getLetWdf() {
		return letWdf;
	}

	public void setLetWdf(Integer letWdf) {
		this.letWdf = letWdf;
	}

	public Integer getGoalNum() {
		return goalNum;
	}

	public void setGoalNum(Integer goalNum) {
		this.goalNum = goalNum;
	}

	public Integer getUdSd() {
		return udSd;
	}

	public void setUdSd(Integer udSd) {
		this.udSd = udSd;
	}

	public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public Double getSpLetWdf() {
        return spLetWdf;
    }

    public void setSpLetWdf(Double spLetWdf) {
        this.spLetWdf = spLetWdf;
    }

    public Double getSpGoalNum() {
        return spGoalNum;
    }

    public void setSpGoalNum(Double spGoalNum) {
        this.spGoalNum = spGoalNum;
    }

    public Double getSpHfWdf() {
        return spHfWdf;
    }

    public void setSpHfWdf(Double spHfWdf) {
        this.spHfWdf = spHfWdf;
    }

    public Double getSpUdSd() {
        return spUdSd;
    }

    public void setSpUdSd(Double spUdSd) {
        this.spUdSd = spUdSd;
    }

    public Double getSpScore() {
        return spScore;
    }

    public void setSpScore(Double spScore) {
        this.spScore = spScore;
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