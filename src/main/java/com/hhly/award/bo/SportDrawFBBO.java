package com.hhly.award.bo;


import java.util.Date;
/**
 * 
 * @ClassName: SportDrawFBBO 
 * @Description: 竞彩篮球彩果表
 * @author wuLong
 * @date 2017年5月25日 下午5:19:25 
 *
 */
public class SportDrawFBBO {
    private Long id;

    private Long sportAgainstInfoId;

    private String halfScore;

    private String fullScore;

    private Integer fullSpf;

    private Integer letSpf;

    private Integer letNum;

    private String score;

    private Integer goalNum;

    private String hfWdf;

    private Date drawTime;

    private String modifyBy;

    private Date modifyTime;

    private Date updateTime;

    private Date createTime;

    public SportDrawFBBO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }


    public Integer getFullSpf() {
		return fullSpf;
	}

	public void setFullSpf(Integer fullSpf) {
		this.fullSpf = fullSpf;
	}

	public Integer getLetSpf() {
		return letSpf;
	}

	public void setLetSpf(Integer letSpf) {
		this.letSpf = letSpf;
	}

	public Integer getLetNum() {
		return letNum;
	}

	public void setLetNum(Integer letNum) {
		this.letNum = letNum;
	}

	public Integer getGoalNum() {
		return goalNum;
	}

	public void setGoalNum(Integer goalNum) {
		this.goalNum = goalNum;
	}

	public String getHfWdf() {
        return hfWdf;
    }

    public void setHfWdf(String hfWdf) {
        this.hfWdf = hfWdf == null ? null : hfWdf.trim();
    }

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
    }

    public Long getSportAgainstInfoId() {
        return sportAgainstInfoId;
    }

    public void setSportAgainstInfoId(Long sportAgainstInfoId) {
        this.sportAgainstInfoId = sportAgainstInfoId;
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