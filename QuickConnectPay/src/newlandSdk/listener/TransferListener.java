package newlandSdk.listener;

import java.math.BigDecimal;

import com.newland.mtype.module.common.emv.EmvControllerListener;
import com.newland.mtype.module.common.emv.EmvTransInfo;
import com.newland.mtype.module.common.swiper.SwipResult;

/**
 * 交易过程监听接口
 * 
 * @author evil
 * 
 */
public interface TransferListener extends EmvControllerListener {

	/**
	 * 当撤销刷卡时触发
	 */
	public void onOpenCardreaderCanceled();

	/**
	 * 当刷磁条卡时触发
	 * 
	 * @param swipRslt
	 *            磁道数据对象
	 * @param amt
	 *            金额
	 * @param swipFlag me15设备还是其他设备
	 */
	public void onSwipMagneticCard(SwipResult swipRslt, BigDecimal amt,int swipFlag);

	/**
	 * 当qpboc流程结束时触发
	 * 
	 * @param emvTransInfo
	 *            emv数据对象
	 */
	public void onQpbocFinished(EmvTransInfo emvTransInfo);
}
