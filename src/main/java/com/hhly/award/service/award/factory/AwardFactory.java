package com.hhly.award.service.award.factory;

import com.hhly.award.service.award.lottery.sports.WinAward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.common.LotteryEnum.Lottery;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.AbstractAward;
import com.hhly.award.service.award.lottery.QlcAward;
import com.hhly.award.service.award.lottery.QxcAward;
import com.hhly.award.service.award.lottery.array.F3dAward;
import com.hhly.award.service.award.lottery.array.FiveAward;
import com.hhly.award.service.award.lottery.array.ThreeAward;
import com.hhly.award.service.award.lottery.ba.DltAward;
import com.hhly.award.service.award.lottery.ba.SsqAward;
import com.hhly.award.service.award.lottery.high.k3.Jsk3Award;
import com.hhly.award.service.award.lottery.high.k3.Jxk3Award;
import com.hhly.award.service.award.lottery.high.kl10.CqKl10Award;
import com.hhly.award.service.award.lottery.high.kl10.GdKl10Award;
import com.hhly.award.service.award.lottery.high.poker.SdPokerAward;
import com.hhly.award.service.award.lottery.high.ssc.CqSscAward;
import com.hhly.award.service.award.lottery.high.x115.GX11x5Award;
import com.hhly.award.service.award.lottery.high.x115.Gd11x5Award;
import com.hhly.award.service.award.lottery.high.x115.JX11x5Award;
import com.hhly.award.service.award.lottery.high.x115.Sd11x5Award;
import com.hhly.award.service.award.lottery.high.x115.XJ11x5Award;
import com.hhly.award.service.award.lottery.sports.BjdcAward;
import com.hhly.award.service.award.lottery.sports.OldZcAward;
import com.hhly.award.service.award.lottery.sports.SportsAward;

/**
 * @desc 获取开奖处理类
 * @author jiangwei
 * @date 2017年5月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class AwardFactory {

	@Autowired
	private SsqAward ssqAward;
	@Autowired
	private SportsAward sportsAward;
	@Autowired
	private Sd11x5Award sd11x5Award;
	@Autowired
	private Gd11x5Award gd11x5Award;
	@Autowired
	private DltAward dltAward;
	@Autowired
	private Jsk3Award jsk3Award;
	@Autowired
	private F3dAward f3dAward;
	@Autowired
	private ThreeAward threeAward;
	@Autowired
	private FiveAward fiveAward;
	@Autowired
	private OldZcAward oldZcAward;
	@Autowired
	private CqSscAward cqSscAward;
	@Autowired
	private SdPokerAward sdPokerAward;
	@Autowired
	private QxcAward qxcAward;
	@Autowired
	private QlcAward qlcAward;
	@Autowired
	private GdKl10Award gdKl10Award;
	@Autowired
	private CqKl10Award cqKl10Award;
	@Autowired
	private BjdcAward bjdcAward;
	@Autowired
	private JX11x5Award jx11x5Award;
	@Autowired
	private XJ11x5Award xj11x5Award;
	@Autowired
	private Jxk3Award jxk3Award;
	@Autowired
	private GX11x5Award gx11x5Award;
	@Autowired
	private WinAward winAward;

	/**
	 * 根据彩种获取
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午3:13:42
	 * @param lotteryCode
	 * @return
	 */
	public AbstractAward getAward(int lotteryCode){
		Lottery lottery = Lottery.getLottery(lotteryCode);
		AbstractAward award = null;
		switch (lottery) {
		case SSQ:
			award = ssqAward;
			break;
		case BB:
		case FB:
			award = sportsAward;
			break;
		case SD11X5:
			award = sd11x5Award;
			break;
		case D11X5:
			award = gd11x5Award;
			break;
		case JX11X5:
			award = jx11x5Award;
			break;
		case XJ11X5:
			award = xj11x5Award;
			break;
		case GX11X5:
			award = gx11x5Award;
			break;
		case DLT:
			award = dltAward;
			break;
		case JSK3:
			award = jsk3Award;
			break;
		case JXK3:
			award = jxk3Award;
			break;
		case F3D:
			award = f3dAward;
			break;
		case PL3:
			award = threeAward;
			break;
		case PL5:
			award = fiveAward;
			break;
		case ZC6:
		case JQ4:
		case SFC:
		case ZC_NINE:
			award = oldZcAward;
			break;
		case CQSSC:
			award = cqSscAward;
			break;
		case SDPOKER:
			award = sdPokerAward;
			break;
		case QXC:
			award  = qxcAward;
			break;
		case QLC:
			award = qlcAward;
			break;
		case DKL10:
			award = gdKl10Award;
			break;
		case CQKL10:
			award = cqKl10Award;
			break;
		case BJDC:
		case SFGG:
			award = bjdcAward;
			break;
			case GJJC:
			case GYJJC:
				award = winAward;
			break;
		default:
			break;
		}
		if(award == null){
			throw new ServiceRuntimeException("不存在开奖服务类{IAward}");
		}
		return award.clone();
	}
}
