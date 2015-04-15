package newlandSdk.listener;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import newlandSdk.common.Const;
import newlandSdk.common.Const.MessageTag;
import newlandSdk.resources.AbstractDevice;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huichuang.application.KLApplication;
import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.R;
import com.newland.mtype.DeviceRTException;
import com.newland.mtype.ModuleType;
import com.newland.mtype.ProcessTimeoutException;
import com.newland.mtype.conn.DeviceConnParams;
import com.newland.mtype.conn.DeviceConnType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.emv.EmvTransController;
import com.newland.mtype.module.common.emv.EmvTransInfo;
import com.newland.mtype.module.common.emv.SecondIssuanceRequest;
import com.newland.mtype.module.common.pin.MacAlgorithm;
import com.newland.mtype.module.common.pin.MacResult;
import com.newland.mtype.module.common.pin.PinInput;
import com.newland.mtype.module.common.pin.PinInputEvent;
import com.newland.mtype.module.common.pin.PinInputResult;
import com.newland.mtype.module.common.pin.PinInputEvent.NotifyStep;
import com.newland.mtype.module.common.pin.PinManageType;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.tlv.TLVPackage;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;

/**
 * 交易过程监听实现
 * 
 * @author evil
 * 
 */
public class SimpleTransferListener implements TransferListener {
	private String TAG = BaseActivity.class.getName();
	private AbstractDevice connected_device;
	private BaseActivity mainActivity;
	private Dialog message_dialog,pininput_dialog;
	private static List L_55TAGS = new ArrayList();
	private CharSequence temp;
	String pininputString;
	static {
		L_55TAGS.add(0x9f26);
		L_55TAGS.add(0x9F27);
		L_55TAGS.add(0x9F10);
		L_55TAGS.add(0x9F37);
		L_55TAGS.add(0x9F36);
		L_55TAGS.add(0x95);
		L_55TAGS.add(0x9A);
		L_55TAGS.add(0x9C);
		L_55TAGS.add(0x9F02);
		L_55TAGS.add(0x5F2A);
		L_55TAGS.add(0x82);
		L_55TAGS.add(0x9F1A);
		L_55TAGS.add(0x9F03);
		L_55TAGS.add(0x9F33);
		L_55TAGS.add(0x9F74);
		L_55TAGS.add(0x9F34);
		L_55TAGS.add(0x9F35);
		L_55TAGS.add(0x9F1E);
		L_55TAGS.add(0x84);
		L_55TAGS.add(0x9F09);
		L_55TAGS.add(0x9F41);
		L_55TAGS.add(0x91);
		L_55TAGS.add(0x71);
		L_55TAGS.add(0x72);
		L_55TAGS.add(0xDF31);
		L_55TAGS.add(0x9F63);
		L_55TAGS.add(0x8A);
		L_55TAGS.add(0xDF32);
		L_55TAGS.add(0xDF33);
		L_55TAGS.add(0xDF34);
		L_55TAGS.add(0xDF75);
	}

	public SimpleTransferListener(AbstractDevice connected_device, Activity mainActivity) {
		this.connected_device = connected_device;
		this.mainActivity = (BaseActivity) mainActivity;
	}

	@Override
	public void onQpbocFinished(EmvTransInfo context) {
		mainActivity.appendInteractiveInfoAndShow("qpboc交易结束:" + context.externalToString(), MessageTag.DATA);
		// ExecuteRslt结果集： 0x00/*成功*/0x01/*交易授受*/0x02/*交易拒绝*/0x03/*联机*/
	if (context.getExecuteRslt() == 0x02) {
		mainActivity.appendInteractiveInfoAndShow("交易失败：【交易拒绝】！", MessageTag.ERROR);
		} else if (context.getExecuteRslt() == 0x03) {
			mainActivity.appendInteractiveInfoAndShow("联机：【电子现金余额不足，请发起联机交易】！", MessageTag.ERROR);
			//todo 联机交易操作
		} else if (context.getExecuteRslt() == 0x00||context.getExecuteRslt() == 0x01) {
			//交易成功、交易授受
			mainActivity.appendInteractiveInfoAndShow(">>>>交易完成，卡号:" + context.getCardNo(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>交易完成，Serial number:" + context.getCardSequenceNumber(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("----8583 IC卡55域数据---表16　基本信息子域列表----", MessageTag.NORMAL);
			mainActivity.appendInteractiveInfoAndShow(">>>>应用密文(9f26):" + context.getAppCryptogram(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>密文信息数据(9F27):" + context.getCryptogramInformationData(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>发卡行应用数据(9F10):" + context.getIssuerApplicationData(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>不可预知数(9F37):" + context.getUnpredictableNumber(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>应用交易计数器(9F36):" + context.getAppTransactionCounter(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>终端验证结果(95):" + context.getTerminalVerificationResults(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>交易日期(9A):" + context.getTransactionDate(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>交易类型(9C):" + context.getTransactionType(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>授权金额(9F02):" + context.getAmountAuthorisedNumeric(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>交易货币代码(5F2A):" + context.getTransactionCurrencyCode(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>应用交互特征(82):" + context.getApplicationInterchangeProfile(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>终端国家代码(9F1A):" + context.getTerminalCountryCode(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>其它金额(9F03):" + context.getAmountOtherNumeric(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>终端性能(9F33):" + context.getTerminal_capabilities(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>电子现金发卡行授权码(9F74):" + context.getEcIssuerAuthorizationCode(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("----8583 IC卡55域数据---可选信息子域列表----", MessageTag.NORMAL);
			mainActivity.appendInteractiveInfoAndShow(">>>>持卡人验证方法结果(9F34):" + context.getCvmRslt(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>终端类型(9F35):" + context.getTerminalType(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>接口设备序列号(9F1E):" + context.getInterface_device_serial_number(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>专用文件名称(84):" + context.getDedicatedFileName(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>软件版本号(9F09):" + context.getAppVersionNumberTerminal(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>交易序列计数器(9F41):" + context.getTransactionSequenceCounter(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>发卡行认证数据(91):" + context.getIssuerAuthenticationData(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>发卡行脚本1(71):" + context.getIssuerScriptTemplate1(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>发卡行脚本2(72):" + context.getIssuerScriptTemplate2(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>发卡方脚本结果(DF31):" + context.getScriptExecuteRslt(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>卡产品标识信息(9F63):" + context.getCardProductIdatification(), MessageTag.DATA);
			TLVPackage tlvPackage = context.setExternalInfoPackage(L_55TAGS);
			mainActivity.appendInteractiveInfoAndShow(">>>>55域打包集合:" + ISOUtils.hexString(tlvPackage.pack()), MessageTag.DATA);
		}else{
			mainActivity.appendInteractiveInfoAndShow("错误的qpboc状态返回！"+context.getExecuteRslt(), MessageTag.ERROR);
		}
		mainActivity.processingUnLock();
	}

	@Override
	public void onEmvFinished(boolean isSuccess, EmvTransInfo context) throws Exception {
		mainActivity.appendInteractiveInfoAndShow("emv交易结束:" + context.externalToString(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>交易完成，交易结果(DF75):" + context.getExecuteRslt(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>交易完成，卡号:" + context.getCardNo(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>交易完成，Serial number:" + context.getCardSequenceNumber(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow("----8583 IC卡55域数据---表16　基本信息子域列表----", MessageTag.NORMAL);
		mainActivity.appendInteractiveInfoAndShow(">>>>应用密文(9f26):" + context.getAppCryptogram(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>密文信息数据(9F27):" + context.getCryptogramInformationData(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>发卡行应用数据(9F10):" + context.getIssuerApplicationData(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>不可预知数(9F37):" + context.getUnpredictableNumber(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>应用交易计数器(9F36):" + context.getAppTransactionCounter(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>终端验证结果(95):" + context.getTerminalVerificationResults(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>交易日期(9A):" + context.getTransactionDate(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>交易类型(9C):" + context.getTransactionType(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>授权金额(9F02):" + context.getAmountAuthorisedNumeric(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>交易货币代码(5F2A):" + context.getTransactionCurrencyCode(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>应用交互特征(82):" + context.getApplicationInterchangeProfile(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>终端国家代码(9F1A):" + context.getTerminalCountryCode(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>其它金额(9F03):" + context.getAmountOtherNumeric(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>终端性能(9F33):" + context.getTerminal_capabilities(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>电子现金发卡行授权码(9F74):" + context.getEcIssuerAuthorizationCode(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow("----8583 IC卡55域数据---可选信息子域列表----", MessageTag.NORMAL);
		mainActivity.appendInteractiveInfoAndShow(">>>>持卡人验证方法结果(9F34):" + context.getCvmRslt(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>终端类型(9F35):" + context.getTerminalType(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>接口设备序列号(9F1E):" + context.getInterface_device_serial_number(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>专用文件名称(84):" + context.getDedicatedFileName(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>软件版本号(9F09):" + context.getAppVersionNumberTerminal(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>交易序列计数器(9F41):" + context.getTransactionSequenceCounter(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>发卡行认证数据(91):" + context.getIssuerAuthenticationData(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>发卡行脚本1(71):" + context.getIssuerScriptTemplate1(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>发卡行脚本2(72):" + context.getIssuerScriptTemplate2(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>发卡方脚本结果(DF31):" + context.getScriptExecuteRslt(), MessageTag.DATA);
		mainActivity.appendInteractiveInfoAndShow(">>>>卡产品标识信息(9F63):" + context.getCardProductIdatification(), MessageTag.DATA);
		if (isSuccess) {
			TLVPackage tlvPackage = context.setExternalInfoPackage(L_55TAGS);
			mainActivity.appendInteractiveInfoAndShow(">>>>55域打包集合:" + ISOUtils.hexString(tlvPackage.pack()), MessageTag.DATA);
		}
		mainActivity.processingUnLock();
	}

	@Override
	public void onError(EmvTransController arg0, Exception arg1) {
		mainActivity.appendInteractiveInfoAndShow("emv交易失败", MessageTag.ERROR);
		mainActivity.appendInteractiveInfoAndShow(arg1.getMessage(), MessageTag.ERROR);
		mainActivity.processingUnLock();
	}

	@Override
	public void onFallback(EmvTransInfo arg0) throws Exception {
		mainActivity.appendInteractiveInfoAndShow("交易降级", MessageTag.NORMAL);
		startSwipTransfer();
		mainActivity.processingUnLock();
	}

	@Override
	public void onRequestOnline(EmvTransController controller, EmvTransInfo context) throws Exception {
		mainActivity.appendInteractiveInfoAndShow(">>>>交易完成，交易结果(DF75):" + context.getExecuteRslt(), MessageTag.DATA);
		TLVPackage tlvPackage = context.setExternalInfoPackage(L_55TAGS);
		mainActivity.appendInteractiveInfoAndShow(">>>>55域打包集合:" + ISOUtils.hexString(tlvPackage.pack()), MessageTag.DATA);
		// 此处判断是开启度开启操作card_reader_flag=0，PBOC流程 card_reader_flag=1
		if (((KLApplication) mainActivity.getApplication()).getOpen_card_reader_flag() != 1) {

			mainActivity.appendInteractiveInfoAndShow("开启联机交易:" + context.externalToString(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>请求在线交易处理", MessageTag.NORMAL);
			mainActivity.appendInteractiveInfoAndShow("终端验证结果(95):" + (context.getTerminalVerificationResults() == null ? "无返回" : Dump.getHexDump(context.getTerminalVerificationResults())), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("应用密文(9f26):" + (context.getAppCryptogram() == null ? "无返回" : Dump.getHexDump(context.getAppCryptogram())), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("持卡人验证方法结果(9f34):" + (context.getCvmRslt() == null ? "无返回" : Dump.getHexDump(context.getCvmRslt())), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>卡号:" + context.getCardNo(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow(">>>>卡序列号:" + context.getCardSequenceNumber(), MessageTag.DATA);
			if (null != context.getTrack_2_eqv_data()) {
				mainActivity.appendInteractiveInfoAndShow(">>>>二磁道明文:" + ISOUtils.hexString(context.getTrack_2_eqv_data()), MessageTag.DATA);
			}

			// 获取IC卡磁道信息
			SwipResult swipResult = connected_device.getController().getTrackText(Const.CardType.ICCARD);
			if (null != swipResult.getSecondTrackData()) {
				mainActivity.appendInteractiveInfoAndShow(">>>>二磁道密文:" + ISOUtils.hexString(swipResult.getSecondTrackData()), MessageTag.DATA);
			}
			((KLApplication) mainActivity.getApplication()).setSwipResult(swipResult);

			// 此处判断是为外部输入密码操作还是执行流程输入密码操作Ic_pinInput_flag=1为流程输入密码
			// Ic_pinInput_flag=0为外部输入密码
			if (((KLApplication) mainActivity.getApplication()).getIc_pinInput_flag() == 0) {
				mainActivity.appendInteractiveInfoAndShow("<br>请输入交易密码...", MessageTag.NORMAL);
				doPinInput(swipResult);
			} else if(((KLApplication) mainActivity.getApplication()).getIc_pinInput_flag() == 2){
				mainActivity.appendInteractiveInfoAndShow("<br>请输入交易密码...", MessageTag.NORMAL);
				me15doPinInput(swipResult);
			}else{
				mainActivity.appendInteractiveInfoAndShow(">>>>密码:" + context.getOnLinePin(), MessageTag.DATA);
			}

			// todo !!!!!!!!!!从该处context中获取ic卡卡片信息后，发送银联8583交易

			SecondIssuanceRequest request = new SecondIssuanceRequest();
			request.setAuthorisationResponseCode("00");// 取自银联8583规范 39域值,该参数按交易实际值填充
			// request.setIssuerAuthenticationData(arg0);//取自银联8583规范 55域 0x91值,该参数按交易实际值填充
			// request.setIssuerScriptTemplate1(arg0);//取自银联8583规范 55域 0x71值,该参数按交易实际值填充
			// request.setIssuerScriptTemplate2(arg0);//取自银联8583规范 55域 0x72值,该参数按交易实际值填充
//			request.setAuthorisationCode(authorisationCode);//取自银联8583规范 38域值,该参数按交易实际值填充
			controller.secondIssuance(request);
		} else {
			// 获取IC卡刷卡结果
			SwipResult swipResult = connected_device.getController().getTrackText(Const.CardType.ICCARD);
			((KLApplication) mainActivity.getApplication()).setSwipResult(swipResult);

		}
	}
	@Override
	public void onRequestPinEntry(EmvTransController arg0, EmvTransInfo arg1) throws Exception {
		mainActivity.appendInteractiveInfoAndShow("错误的事件返回，不可能要求密码输入！", MessageTag.ERROR);
		arg0.cancelEmv();

	}

	@Override
	public void onRequestSelectApplication(EmvTransController arg0, EmvTransInfo arg1) throws Exception {
		mainActivity.appendInteractiveInfoAndShow("错误的事件返回，不可能要求应用选择！", MessageTag.ERROR);
		arg0.cancelEmv();

	}

	@Override
	public void onRequestTransferConfirm(EmvTransController arg0, EmvTransInfo arg1) throws Exception {
		mainActivity.appendInteractiveInfoAndShow("错误的事件返回，不可能要求交易确认！", MessageTag.ERROR);
		arg0.cancelEmv();

	}

	@Override
	public void onOpenCardreaderCanceled() {
		mainActivity.appendInteractiveInfoAndShow("用户撤销刷卡操作！", MessageTag.TIP);
		mainActivity.processingUnLock();
	}

	@Override
	public void onSwipMagneticCard(SwipResult swipRslt, BigDecimal amt,int swipFlag) {
		startSwipTransfer(swipRslt, amt,swipFlag);
	}

	public void startSwipTransfer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				connected_device.connectDevice();
				try {
					// Input amount
					BigDecimal amt = ((KLApplication) mainActivity.getApplication()).getAmt();
					DecimalFormat df = new DecimalFormat("#0.00");
					mainActivity.appendInteractiveInfoAndShow("输入金额为:" + df.format(amt).toString() + "<br>请刷卡...", MessageTag.NORMAL);
					connected_device.getController().clearScreen();
					// 刷卡
					try {
						SwipResult	swipRslt = connected_device.getController().swipCard("输入金额为:" + df.format(amt).toString() + "\n请刷卡", 30000L, TimeUnit.MILLISECONDS);
						if (swipRslt == null) {
							connected_device.getController().clearScreen();
							mainActivity.appendInteractiveInfoAndShow("刷卡撤销", MessageTag.NORMAL);
							return;
						}
						byte[] secondTrack = swipRslt.getSecondTrackData();
						byte[] thirdTrack = swipRslt.getThirdTrackData();
						mainActivity.appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getValidDate(), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getServiceCode(), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("刷卡成功", MessageTag.NORMAL);
						mainActivity.appendInteractiveInfoAndShow("二磁道:" + (secondTrack == null ? "null" : Dump.getHexDump(secondTrack)), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("三磁道:" + (thirdTrack == null ? "null" : Dump.getHexDump(thirdTrack)), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("<br>请输入密码:", MessageTag.NORMAL);
						// 密码输入
						doPinInput(swipRslt);
					} catch (Exception e) {
						if (e instanceof ProcessTimeoutException) {
							mainActivity.appendInteractiveInfoAndShow("swipe failed:超时！", MessageTag.ERROR);
							mainActivity.processingUnLock();
							return;
						} else if (e instanceof DeviceRTException) {
							mainActivity.appendInteractiveInfoAndShow("swipe failed:交易失败", MessageTag.ERROR);
							final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
							builder.setTitle("swipe failed:").setMessage("是否重新刷卡或插卡?");
							builder.setPositiveButton("是", new OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									new Thread(new Runnable() {
										@Override
										public void run() {
											message_dialog.dismiss();
											reDoSwipeCard();
										}
									}).start();
								}
							});
							builder.setNegativeButton("否", new OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									message_dialog.dismiss();
									mainActivity.processingUnLock();
								}
							});
							mainActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									message_dialog = builder.create();
									message_dialog.setCancelable(false);
									message_dialog.setCanceledOnTouchOutside(false);
									message_dialog.show();
								}
							});

						}
					}

				} catch (Exception e) {
					mainActivity.appendInteractiveInfoAndShow("消费处理异常:" + e.getMessage(), MessageTag.ERROR);
					e.printStackTrace();
				} finally {
					mainActivity.processingUnLock();
				}
			}
		}).start();
	}

	public void startSwipTransfer(final SwipResult swipRslt, final BigDecimal amt,final int swipFlag) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				connected_device.connectDevice();
				try {
					connected_device.getController().clearScreen();
					if (swipRslt == null) {
						connected_device.getController().clearScreen();
						mainActivity.appendInteractiveInfoAndShow("刷卡撤销", MessageTag.TIP);
						return;
					}
					byte[] secondTrack = swipRslt.getSecondTrackData();
					byte[] thirdTrack = swipRslt.getThirdTrackData();
					mainActivity.appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getValidDate(), MessageTag.DATA);
					mainActivity.appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getServiceCode(), MessageTag.DATA);
					mainActivity.appendInteractiveInfoAndShow("刷卡成功", MessageTag.NORMAL);
					mainActivity.appendInteractiveInfoAndShow("二磁道:" + (secondTrack == null ? "null" : Dump.getHexDump(secondTrack)), MessageTag.DATA);
					mainActivity.appendInteractiveInfoAndShow("三磁道:" + (thirdTrack == null ? "null" : Dump.getHexDump(thirdTrack)), MessageTag.DATA);
					mainActivity.appendInteractiveInfoAndShow("<br>请输入密码...", MessageTag.NORMAL);
					// 密码输入
					if(swipFlag == 0 ){
						doPinInput(swipRslt);
					}else{
						me15doPinInput(swipRslt);
					}
				} catch (Exception e) {
					mainActivity.appendInteractiveInfoAndShow("消费处理异常:" + e.getMessage(), MessageTag.ERROR);
					e.printStackTrace();
				} finally {
					mainActivity.processingUnLock();
				}
			}
		}).start();
	}

	private PinInputEvent inputPwd(String acctHash, BigDecimal amt, SwipResult swipRslt, DeviceEventListener<PinInputEvent> listener) throws Exception {
		DecimalFormat df = new DecimalFormat("#.00");
		String msg = "消费金额为:" + df.format(amt) + "\n请输入交易密码:";
		if (listener == null)
			return connected_device.getController().startPininput(acctHash, 6, msg);
		else
			connected_device.getController().startPininput(acctHash, 6, msg, listener);
		return null;
	}
	public void doPinInput(SwipResult swipRslt) throws Exception {
		PinInputEvent event = null;
		DeviceConnParams connParams = connected_device.getController().getDeviceConnParams();
		if (connParams == null) {
			mainActivity.appendInteractiveInfoAndShow("无法获得设备连接方式!密码输入停止!", MessageTag.ERROR);
			return;
		}

		if (DeviceConnType.IM81CONNECTOR_V100 != connParams.getConnectType()) {
			BigDecimal amt = ((KLApplication) mainActivity.getApplication()).getAmt();
			event = inputPwd(swipRslt.getAccount().getAcctHashId(), amt, swipRslt, null);
			if (event == null) {
				mainActivity.appendInteractiveInfoAndShow("密码输入撤销", MessageTag.TIP);
				mainActivity.btnStateInitFinished();
				return;
			}
			mainActivity.appendInteractiveInfoAndShow("密码输入完成", MessageTag.NORMAL);
			mainActivity.appendInteractiveInfoAndShow("ksn:" + Dump.getHexDump(event.getKsn()), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("密码:" + Dump.getHexDump(event.getEncrypPin()), MessageTag.DATA);

		} else {// IM81连接方式
			event = inputPwd(swipRslt.getAccount().getAcctHashId(), new BigDecimal(100),
					swipRslt, new DeviceEventListener<PinInputEvent>() {

				@Override
				public Handler getUIHandler() {
					return null;
				}

				@Override
				public void onEvent(PinInputEvent event, Handler arg1) {
					if (event.isProcessing()) {
						NotifyStep step = event.getNotifyStep();
						if (step == NotifyStep.ENTER) {
							mainActivity.doPinInputShower(true);
						} else {
							mainActivity.doPinInputShower(false);
						}
					} else if (event.isUserCanceled()) {
						mainActivity.appendInteractiveInfoAndShow("密码输入撤销", MessageTag.TIP);
						mainActivity.processingUnLock();
					} else if (event.isSuccess()) {
						mainActivity.appendInteractiveInfoAndShow("密码输入完成", MessageTag.NORMAL);
						mainActivity.appendInteractiveInfoAndShow("ksn:" + Dump.getHexDump(event.getKsn()), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("密码:" + Dump.getHexDump(event.getEncrypPin()), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("交易完成", MessageTag.NORMAL);
						mainActivity.processingUnLock();
					} else {
						Log.e(TAG, "密码输入失败!", event.getException());
						mainActivity.appendInteractiveInfoAndShow("密码输入失败!" + event.getException(), MessageTag.ERROR);
						mainActivity.processingUnLock();
					}
				}
			});
		}
	}
	
	public void me15doPinInput(final SwipResult swipRslt){
		DeviceConnParams connParams = connected_device.getController().getDeviceConnParams();
		if (connParams == null) {
			mainActivity.appendInteractiveInfoAndShow("无法获得设备连接方式!密码输入停止!", MessageTag.ERROR);
			return;
		}
		
		Looper.prepare();
		final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
		LayoutInflater inflater = LayoutInflater.from(mainActivity);
		final View view = inflater.inflate(R.layout.dialog_pininput, null);
		final EditText pininput_value = (EditText) view.findViewById(R.id.password);
		builder.setView(view);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				pininputString = pininput_value.getText().toString();
//				inputPwd(swipRslt.getAccount().getAcctHashId(), amt, swipRslt, null);
				if(pininputString == null){
					mainActivity.appendInteractiveInfoAndShow("输入密码为空!", MessageTag.ERROR);
					return ;
				}
				try {
					PinInputResult result = connected_device.getController()
							.startPinInputWithoutKeyboard(
									swipRslt.getAccount().getAcctHashId(), 
									pininputString.getBytes());
					if(result!=null){
						mainActivity.appendInteractiveInfoAndShow("密码输入完成", MessageTag.NORMAL);
						mainActivity.appendInteractiveInfoAndShow("ksn:" + Dump.getHexDump(result.getKSN()), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("密码:" + Dump.getHexDump(result.getPinblock()), MessageTag.DATA);
						mainActivity.appendInteractiveInfoAndShow("交易完成", MessageTag.NORMAL);
					}else{
						mainActivity.appendInteractiveInfoAndShow("输入密码为空!", MessageTag.ERROR);
					}
				} catch (Exception e) {
					Log.e(TAG, "密码输入失败!", e);
					mainActivity.processingUnLock();
					mainActivity.appendInteractiveInfoAndShow("密码输入失败!" + e.getLocalizedMessage(), MessageTag.ERROR);
				}
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mainActivity.appendInteractiveInfoAndShow("输入密码为空!", MessageTag.ERROR);
				mainActivity.processingUnLock();
				pininput_dialog.dismiss();
			}
		});
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pininput_dialog = builder.create();
				pininput_dialog.setTitle("密码输入");
				pininput_dialog.show();
				pininput_dialog.setCancelable(false);
				pininput_dialog.setCanceledOnTouchOutside(false);
			}
		});
		mainActivity.processingUnLock();
	}
	
	private void reDoSwipeCard() {
		mainActivity.processingLock();
		BigDecimal amt = ((KLApplication) mainActivity.getApplication()).getAmt();
		DecimalFormat df = new DecimalFormat("#.00");
		mainActivity.appendInteractiveInfoAndShow("Amount:" + df.format(amt).toString() + "<br>Please swipe card", MessageTag.NORMAL);
		connected_device.getController().clearScreen();
		// swipe card
		try {
			SwipResult	swipRslt = connected_device.getController().swipCard("Amount:" + df.format(amt).toString() + "\nPlease swipe card", 30, TimeUnit.MILLISECONDS);
			if (swipRslt == null) {
				connected_device.getController().clearScreen();
				mainActivity.appendInteractiveInfoAndShow("刷卡撤销", MessageTag.NORMAL);
				return;
			}
			byte[] secondTrack = swipRslt.getSecondTrackData();
			byte[] thirdTrack = swipRslt.getThirdTrackData();
			mainActivity.appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getValidDate(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getServiceCode(), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("刷卡成功", MessageTag.NORMAL);
			mainActivity.appendInteractiveInfoAndShow("二磁道:" + (secondTrack == null ? "null" : Dump.getHexDump(secondTrack)), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("三磁道:" + (thirdTrack == null ? "null" : Dump.getHexDump(thirdTrack)), MessageTag.DATA);
			mainActivity.appendInteractiveInfoAndShow("<br>请输入密码...", MessageTag.NORMAL);

			// 密码输入
			doPinInput(swipRslt);

		} catch (Exception e) {
			if (e instanceof ProcessTimeoutException) {
				mainActivity.appendInteractiveInfoAndShow("swipe failed:超时！", MessageTag.ERROR);
				mainActivity.processingUnLock();
				return;
			} else if (e instanceof DeviceRTException) {
				mainActivity.appendInteractiveInfoAndShow("swipe failed:" + e.getMessage(), MessageTag.ERROR);
				final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
				builder.setTitle("swipe failed:").setMessage("是否重新刷卡或插卡?");
				builder.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								message_dialog.dismiss();
								reDoSwipeCard();
							}
						}).start();
					}
				});
				builder.setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						message_dialog.dismiss();
						mainActivity.processingUnLock();
					}
				});
				mainActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						message_dialog = builder.create();
						message_dialog.setCancelable(false);
						message_dialog.setCanceledOnTouchOutside(false);
						message_dialog.show();
					}
				});
				mainActivity.appendInteractiveInfoAndShow("swipe failed:交易失败", MessageTag.ERROR);
			}
		}
	}

}
