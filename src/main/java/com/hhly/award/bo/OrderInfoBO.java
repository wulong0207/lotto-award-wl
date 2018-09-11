package com.hhly.award.bo;

import java.util.Date;
import java.util.List;

import com.hhly.award.base.common.BaseBO;

/**
 * @author huangb
 *
 * @Date 2016年11月30日
 *
 * @Desc 订单信息
 */
@SuppressWarnings("serial")
public class OrderInfoBO extends BaseBO {

	/**
	 * 编号
	 */
	
	private Long id;
	/**
	 * 订单编号
	 */
	private String orderCode;
	/**
	 * 彩种代码
	 */
	
	private Integer lotteryCode;
	/**
	 * 彩种名称
	 */
	 
	private String lotteryName;
	/**
	 * 彩期
	 */
	
	private String lotteryIssue;
	/**
	 * 开奖号码
	 */
	 
	private String drawCode;
	/**
	 * 此订单完成开奖的时间
	 */
	
	private Date lotteryTime;
	/**
	 * 此订单完成派奖的时间
	 */
	 
	private Date sendTime;
	/**
	 * 用户
	 */

	private Long userId;
	
	/**
	 * 订单总金额
	 */
	private Double orderAmount;
	/**
	 * 数字彩：中奖等级，竞技彩：过关方式；高频彩：玩法名称
	 */
	 
	private String winningDetail;
	/**
	 * 税前奖金
	 */
	 
	private Double preBonus;
	/**
	 * 税后奖金
	 */

	private Double aftBonus;
	/**
	 * 1：代购；2：追号；3：合买
	 */
	 
	private Short buyType;
	/**
	 * 支付状态
	 */
	private Short payStatus;
	/**
	 * 1：待上传；2：待拆票；3：拆票中；4：待出票；5:出票中；6：已出票；7：出票失败
	 */

	private Short orderStatus;
	/**
	 * 1：未开奖；2：未中奖；3：已中奖；4：已派奖
	 */
	 
	private Short winningStatus;
	/**
	 * 渠道
	 */
	 
	private String channelId;
	/**
	 * 开奖后生成的加奖奖金
	 */
	 
	private Double addedBonus;
	/**
	 * 网站加奖
	 */
	private Double websiteBonus;
	
	/**
	 * 投注内容,拼接格式自定义(选球(根据单/复式/胆拖规则拼接):注数:倍数:金额:子玩法:选号方式:内容类型:投注模式)
	 */
	 
	private String betContent;
	
	/**
	 * 修改人
	 */

	private String modifyBy;
	/**
	 * 备注
	 */
	 
	private String remark;
	/**
	 * 开奖后生成的优惠券中的红包编号ID(系统自动发放的红包编号ID)
	 */
	 
	private String redCodeUsed;
	
	private String redCodeGet;
	
	/**
	 * 竞技彩购买的场次编号
	 */

	private String buyScreen;

	/**
	 * 大乐透追号:0否;1是
	 */
	
	private short lottoAdd;
	/**
	 * 活动表的活动ID
	 */
	private String activitySource;
	
	private List<TicketInfoBO> ticketInfoBOs;
	/**
	 * 最大购买场次编号
	 */
	private String maxBuyScreen;
	
	/**
	 * 是否派奖
	 */
	private boolean payoutAward;
	/**
	 * 追号主订单编号
	 */
	private String orderAddCode;
	/**
	 * 1、未推单 2、已推单 3、已跟单
	 */
	private Integer orderType;
	
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public List<TicketInfoBO> getTicketInfoBOs() {
		return ticketInfoBOs;
	}

	public void setTicketInfoBOs(List<TicketInfoBO> ticketInfoBOs) {
		this.ticketInfoBOs = ticketInfoBOs;
	}

	public String getMaxBuyScreen() {
		return maxBuyScreen;
	}

	public void setMaxBuyScreen(String maxBuyScreen) {
		this.maxBuyScreen = maxBuyScreen;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	

	public Integer getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getLotteryIssue() {
		return lotteryIssue;
	}

	public void setLotteryIssue(String lotteryIssue) {
		this.lotteryIssue = lotteryIssue;
	}

	public String getDrawCode() {
		return drawCode;
	}

	public void setDrawCode(String drawCode) {
		this.drawCode = drawCode;
	}

	public Date getLotteryTime() {
		return lotteryTime;
	}

	public void setLotteryTime(Date lotteryTime) {
		this.lotteryTime = lotteryTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getWinningDetail() {
		return winningDetail;
	}

	public void setWinningDetail(String winningDetail) {
		this.winningDetail = winningDetail;
	}

	public Double getPreBonus() {
		return preBonus;
	}

	public void setPreBonus(Double preBonus) {
		this.preBonus = preBonus;
	}

	public Double getAftBonus() {
		return aftBonus;
	}

	public void setAftBonus(Double aftBonus) {
		this.aftBonus = aftBonus;
	}

	public Short getBuyType() {
		return buyType;
	}

	public void setBuyType(Short buyType) {
		this.buyType = buyType;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Short getWinningStatus() {
		return winningStatus;
	}

	public void setWinningStatus(Short winningStatus) {
		this.winningStatus = winningStatus;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Double getAddedBonus() {
		return addedBonus;
	}

	public void setAddedBonus(Double addedBonus) {
		this.addedBonus = addedBonus;
	}

	public String getBetContent() {
		return betContent;
	}

	public void setBetContent(String betContent) {
		this.betContent = betContent;
	}

	public String getBuyScreen() {
		return buyScreen;
	}

	public void setBuyScreen(String buyScreen) {
		this.buyScreen = buyScreen;
	}

	
	public String getRedCodeUsed() {
		return redCodeUsed;
	}

	public void setRedCodeUsed(String redCodeUsed) {
		this.redCodeUsed = redCodeUsed;
	}

	public String getRedCodeGet() {
		return redCodeGet;
	}

	public void setRedCodeGet(String redCodeGet) {
		this.redCodeGet = redCodeGet;
	}

	public short getLottoAdd() {
		return lottoAdd;
	}

	public void setLottoAdd(short lottoAdd) {
		this.lottoAdd = lottoAdd;
	}

	public String getActivitySource() {
		return activitySource;
	}

	public void setActivitySource(String activitySource) {
		this.activitySource = activitySource;
	}

	public Short getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Short payStatus) {
		this.payStatus = payStatus;
	}

	@Override
	public String toString() {
		return "OrderInfoBO [id=" + id + ", orderCode=" + orderCode + ", lotteryCode=" + lotteryCode + ", lotteryName="
				+ lotteryName + ", lotteryIssue=" + lotteryIssue + ", drawCode=" + drawCode + ", lotteryTime="
				+ lotteryTime + ", sendTime=" + sendTime + ", userId=" + userId + ", winningDetail=" + winningDetail
				+ ", preBonus=" + preBonus + ", aftBonus=" + aftBonus + ", buyType=" + buyType + ", payStatus="
				+ payStatus + ", orderStatus=" + orderStatus + ", winningStatus=" + winningStatus + ", channelId="
				+ channelId + ", addedBonus=" + addedBonus + ", betContent=" + betContent + ", modifyBy=" + modifyBy
				+ ", remark=" + remark + ", redCodeUsed=" + redCodeUsed + ", redCodeGet=" + redCodeGet + ", buyScreen="
				+ buyScreen + ", lottoAdd=" + lottoAdd + ", activitySource=" + activitySource + ", ticketInfoBOs="
				+ ticketInfoBOs + ", maxBuyScreen=" + maxBuyScreen + "]";
	}

	public boolean isPayoutAward() {
		return payoutAward;
	}

	public void setPayoutAward(boolean payoutAward) {
		this.payoutAward = payoutAward;
	}

	/**
	 * @return the orderAddCode
	 */
	public String getOrderAddCode() {
		return orderAddCode;
	}

	/**
	 * @param orderAddCode the orderAddCode to set
	 */
	public void setOrderAddCode(String orderAddCode) {
		this.orderAddCode = orderAddCode;
	}

	public Double getWebsiteBonus() {
		return websiteBonus;
	}

	public void setWebsiteBonus(Double websiteBonus) {
		this.websiteBonus = websiteBonus;
	}
	
	
}
