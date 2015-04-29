package newlandSdk.listener;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import newlandSdk.adapter.FileAdapter;
import newlandSdk.common.Const;
import newlandSdk.common.Const.DataEncryptWKIndexConst;
import newlandSdk.common.Const.MKIndexConst;
import newlandSdk.common.Const.MacWKIndexConst;
import newlandSdk.common.Const.MessageTag;
import newlandSdk.common.Const.PinWKIndexConst;
import newlandSdk.common.ListViewBtnKey;
import newlandSdk.resources.AbstractDevice;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.huichuang.application.KLApplication;
import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.R;
import com.newland.mtype.DeviceInfo;
import com.newland.mtype.DeviceInvokeException;
import com.newland.mtype.DeviceRTException;
import com.newland.mtype.ProcessTimeoutException;
import com.newland.mtype.UpdateAppListener;
import com.newland.mtype.module.common.cardreader.CardRule;
import com.newland.mtype.module.common.cardreader.OpenCardType;
import com.newland.mtype.module.common.emv.AIDConfig;
import com.newland.mtype.module.common.emv.CAPublicKey;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.emv.TerminalConfig;
import com.newland.mtype.module.common.iccard.ICCardSlot;
import com.newland.mtype.module.common.iccard.ICCardSlotState;
import com.newland.mtype.module.common.iccard.ICCardType;
import com.newland.mtype.module.common.pin.EncryptType;
import com.newland.mtype.module.common.pin.KSNKeyType;
import com.newland.mtype.module.common.pin.KSNLoadResult;
import com.newland.mtype.module.common.pin.KekUsingType;
import com.newland.mtype.module.common.pin.LoadPKResultCode;
import com.newland.mtype.module.common.pin.LoadPKType;
import com.newland.mtype.module.common.pin.MacAlgorithm;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.pin.WorkingKeyType;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.newland.mtype.module.common.rfcard.RFCardType;
import com.newland.mtype.module.common.rfcard.RFKeyMode;
import com.newland.mtype.module.common.rfcard.RFResult;
import com.newland.mtype.module.common.storage.StorageResult;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;



/**
 * 二级菜单操作
 * 
 * @author evil
 * 
 */
public class OperaListener implements OnChildClickListener {
	private String SDCardPath, path;
	AlertDialog showFile;
	private String NLDPathString;
	ListView fileListView;
	private int temp_param_index = PinWKIndexConst.DEFAULT_PIN_WK_INDEX;
	private EditText edit_param_tag, edit_param_value, edit_encryption_value, edit_caclmac_value, edit_amt_input, edit_qccard_block, edit_qccard_data, edit_qccard_key, edit_snr_no;
	private FileAdapter fileAdapter;
	private List<File> allFiles = new ArrayList<File>();
	private AbstractDevice connected_device;
	private BaseActivity mainActivity;
	private String TAG = BaseActivity.class.getName();
	private BigDecimal amt;
	private Dialog workingkey_dialog, param_dialog, encrypt_dialog, dialog_caclmac, amt_dialog, message_dialog, iccard_dialog, nccard_dialog;
	private Dialog ic_pinInput_dialog;
	private RadioGroup radioGroup_wk_index, radioGroup_encrypt_type1;
	private static final String[] IC_PIN_INPUT = { "pboc流程外发起密码输入", "pboc流程自行发起密码输入" ,"针对ME15"};
	private SwipResult swipResult = null;
	private byte[] result, decryresult;
	private String workingkey;
	private RadioButton radio_CBC, radio_ECB;
	private MacAlgorithm macAlgorithm;
	private CheckedChangeListener changeListener;
	private EncryptType encryptType = EncryptType.CBC;
	private CharSequence temp;
	private Button btn_sure, btn_cancel;
	// ---------------------------------emv--------------------------
	private EmvModule emv;
	private int keyIndex = 87;
	private byte[] rid = new byte[] { (byte) 0xA0, 0x00, 0x00, 0x03, 0x33 };
	private byte[] aid = new byte[] { (byte) 0xA0, 0x00, 0x00, 0x00, 0x03, 0x10, 0x10 };

	// --------------------------------main key-------------------------
	protected static final String MAINKEY = "253C9D9D7C2FBBFA253C9D9D7C2FBBFA";// 预设输入值
	// -------------------------------working key----------------------
	protected static final String WORKINGKEY_DATA_PIN = "D2CEEE5C1D3AFBAF00374E0CC1526C86";// 预设输入值
	protected static final String WORKINGKEY_DATA_TRACK = "DBFE96D0A5F09D24DBFE96D0A5F09D24";// 预设输入值
	protected static final String WORKINGKEY_DATA_MAC = "DBFE96D0A5F09D24";// 预设输入值

	// -------------------------------IC Card------------------------------
	private ICCardSlot icCardSlot;

	// --------------------------------NC Card------------------------------
	private RFCardType qpCardType = RFCardType.M1CARD;

	public OperaListener(AbstractDevice connected_device, Activity mainActivity) {
		this.connected_device = connected_device;
		this.mainActivity = (BaseActivity) mainActivity;
		SDCardPath = Environment.getExternalStorageDirectory().toString();
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int parent, int children, long arg4) {
		if (!connected_device.isControllerAlive()) {
			mainActivity.appendInteractiveInfoAndShow("未连接设备", MessageTag.TIP);
		} else {
			switch (parent) {
			case ListViewBtnKey.ListViewBtnParentKey.TRAN_PROC: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.START_PBOC: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("设备当前仅能执行撤消操作...", MessageTag.TIP);
						break;
					} else {
						jiaoyi();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.CLEAR_SCREEN: {
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								connected_device.getController().reset();
								mainActivity.appendInteractiveInfoAndShow("撤消当前指令成功!", MessageTag.TIP);
								mainActivity.processingLock();
							} catch (Exception e) {
								Log.e(TAG, "清屏失败!", e);
								mainActivity.appendInteractiveInfoAndShow("撤消指令执行失败!" + e.getMessage(), MessageTag.ERROR);
							} finally {
								mainActivity.processingUnLock();
							}
						}
					}).start();
					break;
				}

				case ListViewBtnKey.ListViewBtnChildKey.OPEN_CARDREADER: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									connected_device.getController().OpenCardReader(mainActivity, new OpenCardType[] { OpenCardType.SWIPER,OpenCardType.ICCARD }, CardRule.UN_ALLOW_LOWER,"请刷卡或者插入IC卡", 60, TimeUnit.SECONDS, new SimpleTransferListener(connected_device, mainActivity));
									mainActivity.appendInteractiveInfoAndShow("读卡器开启成功", MessageTag.NORMAL);
								} catch (Exception e) {
									if (e instanceof DeviceRTException) {
										mainActivity.appendInteractiveInfoAndShow("读卡器开启异常", MessageTag.ERROR);
										mainActivity.appendInteractiveInfoAndShow(((DeviceRTException) e).getMessage(), MessageTag.ERROR);
									}
								} finally {
									mainActivity.processingUnLock();
								}

							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.GET_MAGNETIC_INFO: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								SwipResult swipResult = ((KLApplication) mainActivity.getApplication()).getSwipResult();
								if (swipResult != null) {
									byte[] secondTrack = swipResult.getSecondTrackData();
									byte[] thirdTrack = swipResult.getThirdTrackData();
									mainActivity.appendInteractiveInfoAndShow("二磁道:" + (secondTrack == null ? "null" : Dump.getHexDump(secondTrack)), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("三磁道:" + (thirdTrack == null ? "null" : Dump.getHexDump(thirdTrack)), MessageTag.DATA);
								} else {
									mainActivity.appendInteractiveInfoAndShow("请先刷磁条卡", MessageTag.TIP);
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;

				}
				case ListViewBtnKey.ListViewBtnChildKey.INPUT_PWD: {

					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						swipResult = ((KLApplication) mainActivity.getApplication()).getSwipResult();
						if (null == swipResult) {
							mainActivity.appendInteractiveInfoAndShow("请先刷卡再输入密码!", MessageTag.TIP);
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									connected_device.connectDevice();
									try {
										mainActivity.processingLock();
										Looper.prepare();
										mainActivity.appendInteractiveInfoAndShow("请输入密码...", MessageTag.NORMAL);
										SimpleTransferListener simpleTransferListener = new SimpleTransferListener(connected_device, mainActivity);
										simpleTransferListener.doPinInput(swipResult);
									} catch (Exception ex) {
										Log.e(TAG, "密码输入失败!", ex);
										mainActivity.appendInteractiveInfoAndShow("密码输入失败!" + ex.getMessage(), MessageTag.ERROR);
									} finally {
										mainActivity.processingUnLock();
									}
								}
							}).start();
						}
					}
				}
					break;

				}
				break;
			}
			case ListViewBtnKey.ListViewBtnParentKey.FIRMWARE: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.SELECT_FIRMWARE: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						mainActivity.processingLock();
						connected_device.connectDevice();
						selectFirmware();
						mainActivity.processingUnLock();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.UPDATE_FIRMWARE: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									mainActivity.processingLock();
									mainActivity.appendInteractiveInfoAndShow(((KLApplication) mainActivity.getApplication()).getNLDPathString(), MessageTag.NORMAL);
									connected_device.getController().updateFirmware(((KLApplication) mainActivity.getApplication()).getNLDPathString(),new UpdateAppListener() {
										
										@Override
										public void onUpdateProgress(long percent,long currentSize) {
											mainActivity.appendInteractiveInfoAndShow("固件更新进度:"+percent+"%"+" 当前完成文件大小:"+currentSize, MessageTag.NORMAL);
										}
										
										@Override
										public void onDownloadStart(long fileSize) {
											mainActivity.appendInteractiveInfoAndShow("开始更新固件,文件总大小:"+fileSize+" 字节", MessageTag.NORMAL);
										}
										
										@Override
										public void onDownloadError(String msg, Throwable e) {
											e.printStackTrace();
											mainActivity.appendInteractiveInfoAndShow("更新固件失败!"+e.getMessage(), MessageTag.ERROR);
											
										}
										
										@Override
										public void onDownloadComplete() {
											mainActivity.appendInteractiveInfoAndShow("更新固件成功!", MessageTag.NORMAL);
										}
									});
							
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
						break;
					}
				}
				}
				break;
			}
			case ListViewBtnKey.ListViewBtnParentKey.SETTING: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.FETCH_DEVICE_INFO: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									DeviceInfo deviceInfo = connected_device.getController().getDeviceInfo();
									mainActivity.appendInteractiveInfoAndShow("设备CSN号:" + deviceInfo.getCSN(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备KSN号:" + deviceInfo.getKSN(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备应用版本号:" + deviceInfo.getAppVer(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备Boot版本号:" + deviceInfo.getBootVersion(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备类型:" + deviceInfo.getPID(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备SN号:" + deviceInfo.getSN(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持音频:" + deviceInfo.isSupportAudio(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持蓝牙:" + deviceInfo.isSupportBlueTooth(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持接触式IC卡:" + deviceInfo.isSupportICCard(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持屏幕显示:" + deviceInfo.isSupportLCD(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持磁条卡:" + deviceInfo.isSupportMagCard(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持脱机交易:" + deviceInfo.isSupportOffLine(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持打印:" + deviceInfo.isSupportPrint(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持非接触IC卡:" + deviceInfo.isSupportQuickPass(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否支持USB:" + deviceInfo.isSupportUSB(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备默认初始状态:" + deviceInfo.isFactoryModel(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否安装主密钥:" + deviceInfo.isMainkeyLoaded(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否安装工作密钥:" + deviceInfo.isWorkingkeyLoaded(), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("设备是否安装dukpt密钥:" + deviceInfo.isDUKPTkeyLoaded(), MessageTag.DATA);
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("设备连接失败!" + e.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}

							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.CACL_MAC: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									Looper.prepare();
									final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_caclmac, null);
									builder.setView(view);
									edit_caclmac_value = (EditText) view.findViewById(R.id.edit_caclmac_value);
									edit_caclmac_value.setHint("test data");
									changeListener = new CheckedChangeListener(view, Const.DialogView.MAC_CACL_DIALOG);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											try {
												String string = edit_caclmac_value.getText().toString();
												byte[] input = string.getBytes("GBK");
												macAlgorithm = changeListener.getMacAlgorithm();
												byte[] ouput = connected_device.getController().caculateMac(macAlgorithm, input);
												mainActivity.appendInteractiveInfoAndShow("输入值:" + string, MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("计算方式:" + macAlgorithm, MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("MAC计算结果:" + new String(ouput, "GBK"), MessageTag.DATA);
											} catch (DeviceInvokeException e) {
												mainActivity.appendInteractiveInfoAndShow("输入值不能为空" + e.getMessage(), MessageTag.ERROR);
											} catch (UnsupportedEncodingException e) {
												mainActivity.appendInteractiveInfoAndShow("输入数值有误", MessageTag.ERROR);
												mainActivity.processingUnLock();
											} catch (Exception e) {
												mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
												mainActivity.processingUnLock();
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											dialog_caclmac.dismiss();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											dialog_caclmac = builder.create();
											dialog_caclmac.setTitle("计算MAC:");
											dialog_caclmac.show();
											dialog_caclmac.setCancelable(false);
											dialog_caclmac.setCanceledOnTouchOutside(false);
										}
									});
									mainActivity.processingUnLock();

								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("设备连接失败!" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}

							}
						}).start();
					}
					break;
				}
				}

				break;
			}
			case ListViewBtnKey.ListViewBtnParentKey.POS_INIT: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.LOAD_MAIN_KEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									connected_device.connectDevice();
									mainActivity.processingLock();
									connected_device.getController().loadMainKey(KekUsingType.ENCRYPT_TMK, MKIndexConst.DEFAULT_MK_INDEX, ISOUtils.hex2byte(MAINKEY), ISOUtils.hex2byte("82E13665"));
									mainActivity.appendInteractiveInfoAndShow("主密钥装载成功!", MessageTag.NORMAL);
									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("主密钥装载失败" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.LOAD_WORKKEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									final boolean[] arrayWorkingKeySelected = new boolean[] { false, false, false };
									final String[] arrayWorkingKey = new String[] { "PIN密钥", "磁道密钥", "MAC密钥" };
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									builder.setTitle("请选择需装载工作密钥类型:");
									builder.setMultiChoiceItems(arrayWorkingKey, arrayWorkingKeySelected, new OnMultiChoiceClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
											arrayWorkingKeySelected[arg1] = arg2;

										}
									});
									builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

											updataworkingkey(arrayWorkingKeySelected);
										}

										public  void updataworkingkey(
												final boolean[] arrayWorkingKeySelected) {
											new Thread(new Runnable() {

												@Override
												public void run() {
													try {
														mainActivity.appendInteractiveInfoAndShow("正在装载工作密钥...", MessageTag.NORMAL);
														if (arrayWorkingKeySelected[0] == true) {
															connected_device.getController().updateWorkingKey(WorkingKeyType.PININPUT, ISOUtils.hex2byte(WORKINGKEY_DATA_PIN), ISOUtils.hex2byte("58A2BBF9"));
														}
														if (arrayWorkingKeySelected[1] == true) {
															connected_device.getController().updateWorkingKey(WorkingKeyType.DATAENCRYPT, ISOUtils.hex2byte(WORKINGKEY_DATA_TRACK), ISOUtils.hex2byte("5B4C8BED"));
														}
														if (arrayWorkingKeySelected[2] == true) {
															connected_device.getController().updateWorkingKey(WorkingKeyType.MAC, ISOUtils.hex2byte(WORKINGKEY_DATA_MAC), ISOUtils.hex2byte("5B4C8BED"));
														}
														mainActivity.appendInteractiveInfoAndShow("工作密钥装载成功!", MessageTag.NORMAL);
													} catch (Exception e) {
														mainActivity.processingUnLock();
														mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
													}
												}
											}).start();
										}
									});

									builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											workingkey_dialog.dismiss();
										}
									});

									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											workingkey_dialog = builder.create();
											workingkey_dialog.setCancelable(false);
											workingkey_dialog.setCanceledOnTouchOutside(false);
											workingkey_dialog.show();
										}
									});
								} catch (Exception ex) {
									mainActivity.appendInteractiveInfoAndShow("工作密钥装载失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.ADD_PUB_KEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									emv = connected_device.getController().getEmvModule();

									int P9f22_1 = 1;
									keyIndex = P9f22_1;
									byte[] df02_2 = ISOUtils.hex2byte("BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93B");
									byte[] df04_2 = ISOUtils.hex2byte("000003");
									byte[] df03_2 = ISOUtils.hex2byte("E881E390675D44C2DD81234DCE29C3F5AB2297A0");
									CAPublicKey caKey2 = new CAPublicKey(P9f22_1, 1, 1, df02_2, df04_2, df03_2, "20091231");
									boolean issuccess2 = emv.addCAPublicKey(rid, caKey2);
									mainActivity.appendInteractiveInfoAndShow("添加公钥结果:" + issuccess2, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + P9f22_1, MessageTag.DATA);

									int P9f22_2 = 2;
									keyIndex = P9f22_2;
									byte[] df02 = ISOUtils.hex2byte("A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57");
									byte[] df04 = ISOUtils.hex2byte("000003");
									byte[] df03 = ISOUtils.hex2byte("03BB335A8549A03B87AB089D006F60852E4B8060");
									CAPublicKey caKey1 = new CAPublicKey(P9f22_2, 1, 1, df02, df04, df03, "20141231");
									boolean issuccess = emv.addCAPublicKey(rid, caKey1);
									mainActivity.appendInteractiveInfoAndShow("添加公钥结果:" + issuccess, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + P9f22_2, MessageTag.DATA);

									int P9f22_3 = 3;
									keyIndex = P9f22_3;
									byte[] df02_3 = ISOUtils.hex2byte("B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33D");
									byte[] df04_3 = ISOUtils.hex2byte("000003");
									byte[] df03_3 = ISOUtils.hex2byte("87F0CD7C0E86F38F89A66F8C47071A8B88586F26");
									CAPublicKey caKey3 = new CAPublicKey(P9f22_3, 1, 1, df02_3, df04_3, df03_3, "20171231");
									boolean issuccess3 = emv.addCAPublicKey(rid, caKey3);
									mainActivity.appendInteractiveInfoAndShow("添加公钥结果:" + issuccess3, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + P9f22_3, MessageTag.DATA);
									//
									int P9f22_4 = 4;
									keyIndex = P9f22_4;
									byte[] df02_4 = ISOUtils.hex2byte("BC853E6B5365E89E7EE9317C94B02D0ABB0DBD91C05A224A2554AA29ED9FCB9D86EB9CCBB322A57811F86188AAC7351C72BD9EF196C5A01ACEF7A4EB0D2AD63D9E6AC2E7836547CB1595C68BCBAFD0F6728760F3A7CA7B97301B7E0220184EFC4F653008D93CE098C0D93B45201096D1ADFF4CF1F9FC02AF759DA27CD6DFD6D789B099F16F378B6100334E63F3D35F3251A5EC78693731F5233519CDB380F5AB8C0F02728E91D469ABD0EAE0D93B1CC66CE127B29C7D77441A49D09FCA5D6D9762FC74C31BB506C8BAE3C79AD6C2578775B95956B5370D1D0519E37906B384736233251E8F09AD79DFBE2C6ABFADAC8E4D8624318C27DAF1");
									byte[] df04_4 = ISOUtils.hex2byte("000003");
									byte[] df03_4 = ISOUtils.hex2byte("F527081CF371DD7E1FD4FA414A665036E0F5E6E5");
									CAPublicKey caKey4 = new CAPublicKey(P9f22_4, 1, 1, df02_4, df04_4, df03_4, "20171231");
									boolean issuccess4 = emv.addCAPublicKey(rid, caKey4);
									mainActivity.appendInteractiveInfoAndShow("添加公钥结果:" + issuccess4, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + P9f22_4, MessageTag.DATA);
								} catch (Exception e) {
									e.printStackTrace();
									mainActivity.appendInteractiveInfoAndShow("添加公钥失败, 索引号为:" + keyIndex + e.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}

							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.DEL_PUB_KEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									emv = connected_device.getController().getEmvModule();
									keyIndex = 1;
									boolean issuccess = emv.deleteCAPublicKey(rid, keyIndex);
									mainActivity.appendInteractiveInfoAndShow("删除公钥结果:" + issuccess, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + keyIndex, MessageTag.DATA);
									keyIndex = 2;
									boolean issuccess_1 = emv.deleteCAPublicKey(rid, keyIndex);
									mainActivity.appendInteractiveInfoAndShow("删除公钥结果:" + issuccess_1, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + keyIndex, MessageTag.DATA);
									keyIndex = 3;
									boolean issuccess_2 = emv.deleteCAPublicKey(rid, keyIndex);
									mainActivity.appendInteractiveInfoAndShow("删除公钥结果:" + issuccess_2, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + keyIndex, MessageTag.DATA);
									keyIndex = 4;
									boolean issuccess_3 = emv.deleteCAPublicKey(rid, keyIndex);
									mainActivity.appendInteractiveInfoAndShow("删除公钥结果:" + issuccess_3, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("索引号为:" + keyIndex, MessageTag.DATA);
								} catch (Exception e) {
									e.printStackTrace();
									mainActivity.appendInteractiveInfoAndShow("删除公钥失败, 索引号为:" + keyIndex + e.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}

							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.ADD_AID: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									emv = connected_device.getController().getEmvModule();
									AIDConfig aidConfig = new AIDConfig();
									aidConfig.setAid(ISOUtils.hex2byte("A000000333010102"));// 0x9f06
									aidConfig.setAppSelectIndicator(0);// 0xDF01
									aidConfig.setAppVersionNumberTerminal(new byte[] { 0x00, (byte) 0x20 });// 0x9f09
									aidConfig.setTacDefault(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF11
									aidConfig.setTacOnLine(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF12
									aidConfig.setTacDenial(ISOUtils.hex2byte("0010000000"));// 0xDF13
									aidConfig.setTerminalFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x05 });// 0x9f1b
									aidConfig.setThresholdValueForBiasedRandomSelection(new byte[] { 0x00, 0x00, 0x00, (byte) 0x28 });// 0xDF15
									aidConfig.setMaxTargetPercentageForBiasedRandomSelection(32);// 0xDF16
									aidConfig.setTargetPercentageForRandomSelection(14);// 0xDF17
									aidConfig.setDefaultDDOL(ISOUtils.hex2byte("9F3704"));// 0xDF14
									aidConfig.setOnLinePinCapability(1);// 0xDF18
									aidConfig.setEcTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0x9F7B
									aidConfig.setNciccOffLineFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF19
									aidConfig.setNciccTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF20
									aidConfig.setNciccCVMLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, (byte) 0x80, 0x00 });// 0xDF21
									aidConfig.setEcCapability(0);// 0xDF24
									aidConfig.setCoreConfigType(2);// 0xDF25
									boolean issuccess = emv.addAID(aidConfig);
									mainActivity.appendInteractiveInfoAndShow("添加AID结果:" + issuccess, MessageTag.DATA);

									AIDConfig aidConfig2 = new AIDConfig();
									aidConfig2.setAid(ISOUtils.hex2byte("A000000333010101"));// 0x9f06
									aidConfig2.setAppSelectIndicator(0);// 0xDF01
									aidConfig2.setAppVersionNumberTerminal(new byte[] { 0x00, (byte) 0x20 });// 0x9f09
									aidConfig2.setTacDefault(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF11
									aidConfig2.setTacOnLine(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF12
									aidConfig2.setTacDenial(ISOUtils.hex2byte("0010000000"));// 0xDF13
									aidConfig2.setTerminalFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x05 });// 0x9f1b
									aidConfig2.setThresholdValueForBiasedRandomSelection(new byte[] { 0x00, 0x00, 0x00, (byte) 0x28 });// 0xDF15
									aidConfig2.setMaxTargetPercentageForBiasedRandomSelection(32);// 0xDF16
									aidConfig2.setTargetPercentageForRandomSelection(14);// 0xDF17
									aidConfig2.setDefaultDDOL(ISOUtils.hex2byte("9F3704"));// 0xDF14
									aidConfig2.setOnLinePinCapability(1);// 0xDF18
									aidConfig2.setEcTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0x9F7B
									aidConfig2.setNciccOffLineFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF19
									aidConfig2.setNciccTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF20
									aidConfig2.setNciccCVMLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, (byte) 0x80, 0x00 });// 0xDF21
									aidConfig2.setEcCapability(0);// 0xDF24
									aidConfig2.setCoreConfigType(2);// 0xDF25
									boolean issuccess2 = emv.addAID(aidConfig2);
									mainActivity.appendInteractiveInfoAndShow("添加AID结果:" + issuccess2, MessageTag.DATA);

									AIDConfig aidConfig3 = new AIDConfig();
									aidConfig3.setAid(ISOUtils.hex2byte("A000000333010103"));// 0x9f06
									aidConfig3.setAppSelectIndicator(0);// 0xDF01
									aidConfig3.setAppVersionNumberTerminal(new byte[] { 0x00, (byte) 0x20 });// 0x9f09
									aidConfig3.setTacDefault(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF11
									aidConfig3.setTacOnLine(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF12
									aidConfig3.setTacDenial(ISOUtils.hex2byte("0010000000"));// 0xDF13
									aidConfig3.setTerminalFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x05 });// 0x9f1b
									aidConfig3.setThresholdValueForBiasedRandomSelection(new byte[] { 0x00, 0x00, 0x00, (byte) 0x28 });// 0xDF15
									aidConfig3.setMaxTargetPercentageForBiasedRandomSelection(32);// 0xDF16
									aidConfig3.setTargetPercentageForRandomSelection(14);// 0xDF17
									aidConfig3.setDefaultDDOL(ISOUtils.hex2byte("9F3704"));// 0xDF14
									aidConfig3.setOnLinePinCapability(1);// 0xDF18
									aidConfig3.setEcTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0x9F7B
									aidConfig3.setNciccOffLineFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF19
									aidConfig3.setNciccTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF20
									aidConfig3.setNciccCVMLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, (byte) 0x80, 0x00 });// 0xDF21
									aidConfig3.setEcCapability(0);// 0xDF24
									aidConfig3.setCoreConfigType(2);// 0xDF25
									boolean issuccess3 = emv.addAID(aidConfig3);
									mainActivity.appendInteractiveInfoAndShow("添加AID结果:" + issuccess3, MessageTag.DATA);

									AIDConfig aidConfig4 = new AIDConfig();
									aidConfig4.setAid(ISOUtils.hex2byte("A000000333010106"));// 0x9f06
									aidConfig4.setAppSelectIndicator(0);// 0xDF01
									aidConfig4.setAppVersionNumberTerminal(new byte[] { 0x00, (byte) 0x20 });// 0x9f09
									aidConfig4.setTacDefault(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF11
									aidConfig4.setTacOnLine(ISOUtils.hex2byte("FC78FCF8F0"));// 0xDF12
									aidConfig4.setTacDenial(ISOUtils.hex2byte("0010000000"));// 0xDF13
									aidConfig4.setTerminalFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x05 });// 0x9f1b
									aidConfig4.setThresholdValueForBiasedRandomSelection(new byte[] { 0x00, 0x00, 0x00, (byte) 0x28 });// 0xDF15
									aidConfig4.setMaxTargetPercentageForBiasedRandomSelection(32);// 0xDF16
									aidConfig4.setTargetPercentageForRandomSelection(14);// 0xDF17
									aidConfig4.setDefaultDDOL(ISOUtils.hex2byte("9F3704"));// 0xDF14
									aidConfig4.setOnLinePinCapability(1);// 0xDF18
									aidConfig4.setEcTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0x9F7B
									aidConfig4.setNciccOffLineFloorLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF19
									aidConfig4.setNciccTransLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// 0xDF20
									aidConfig4.setNciccCVMLimit(new byte[] { 0x00, 0x00, 0x00, 0x00, (byte) 0x80, 0x00 });// 0xDF21
									aidConfig4.setEcCapability(0);// 0xDF24
									aidConfig4.setCoreConfigType(2);// 0xDF25
									boolean issuccess4 = emv.addAID(aidConfig4);
									mainActivity.appendInteractiveInfoAndShow("添加AID结果:" + issuccess4, MessageTag.DATA);
								} catch (Exception e) {
									e.printStackTrace();
									mainActivity.appendInteractiveInfoAndShow("添加AID失败!" + e.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}

							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.DEL_AID: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									emv = connected_device.getController().getEmvModule();
									boolean issuccess = emv.deleteAID(aid);
									mainActivity.appendInteractiveInfoAndShow("删除AID结果:" + issuccess, MessageTag.DATA);
								} catch (Exception e) {
									e.printStackTrace();
									mainActivity.appendInteractiveInfoAndShow("删除AID失败!" + e.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.LOAD_KSN:{
					if(mainActivity.processingisLocked()){
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					}else{
						new Thread(new Runnable(){

							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									 KSNLoadResult ksnloadresult = connected_device.getController().ksnLoad(KSNKeyType.TR31_TYPE, 0, 
											new byte[]{0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01}, null, 1, new byte[]{0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01});
									 mainActivity.appendInteractiveInfoAndShow("KSN装载结果:"+ksnloadresult.getResultCode(), MessageTag.DATA);
								} catch (Exception e) {
									e.printStackTrace();
								}finally{
									mainActivity.processingUnLock();
								}
							}}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.LOAD_PK:{
					if(mainActivity.processingisLocked()){
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					}else{
						new Thread(new Runnable(){

							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									String pkmodulestring = "c0 d8 5f 6a 86 a3 1e e6 14 9c 70 44 36 32 83 1a 75 b1 2b 69 0f a9 e3 7d 1b 2f 0c 5e 65 45 60 d0 44 3f 10 78 4e 61 cf c4 c7 9b 5e c6 39 da 9a c6 af 17 02 ec 71 7f 24 92 3d b2 da b7 60 d6 4f fb 57 22 99 ee 3c 52 c1 c9 99 c3 e7 e3 b4 a0 24 8d bd 8e 8d af fd 99 2c a2 c7 66 df 74 79 6d e9 7a c4 4c 48 23 83 22 31 15 89 f6 61 c6 f4 20 fe 42 99 05 08 a8 83 20 20 d9 0a bb 24 d1 7c a5 cb 2c cc ae a5 a4 8c ad e0 ed 51 fe 60 2b fa ef d7 c0 6b c2 7d f2 de a9 0e f6 2e 88 bc 49 4a 93 ca dd d2 08 78 e5 ff 4f 28 a0 c3 b9 fe e4 4d 20 78 e9 41 43 dc fb 02 20 96 1a 5d b5 7a 39 a0 64 4f fa 69 3c 0a fd 88 67 d3 33 6d 6b be 93 33 87 4e 55 1c 24 bd e9 70 1b f7 b3 85 5d 2d 36 5f 19 8b 4b f9 cc a0 ed 9e 4b a9 92 11 40 22 2c 85 12 db 97 48 68 c1 21 a9 61 47 d1 7f 81 2a 22 ec 87 df e9";
									String pkExponentstring = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01";
									byte[] pkModule = ISOUtils.hex2byte(pkmodulestring.replaceAll(" ", ""));
									byte[] pkExponent = ISOUtils.hex2byte(pkExponentstring.replaceAll(" ", ""));
									 LoadPKResultCode loadpkresult = connected_device.getController().LoadPublicKey(LoadPKType.NOKEY_TYPE, 1, "1024", pkModule, pkExponent, null, null);
									 mainActivity.appendInteractiveInfoAndShow("装载公钥的结果是:"+loadpkresult, MessageTag.DATA);
								} catch (Exception e) {
									e.printStackTrace();
								}finally{
									mainActivity.processingUnLock();
								}
							}}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.SET_TERMINAL_PROP: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {

									mainActivity.processingLock();
									emv = connected_device.getController().getEmvModule();
									TerminalConfig trmnlConfig = new TerminalConfig();
									trmnlConfig.setTrmnlICSConfig(new byte[] { (byte) 0xF4, (byte) 0xF0, (byte) 0xF0, (byte) 0xFA, (byte) 0xAF, (byte) 0xFE, (byte) 0xA0 });
									trmnlConfig.setTerminalType(0x22);
									trmnlConfig.setTerminalCapabilities(new byte[] { (byte) 0xE0, (byte) 0xF8, (byte) 0xC8 });
									trmnlConfig.setAdditionalTerminalCapabilities(new byte[] { (byte) 0xFF, (byte) 0x80, (byte) 0xF0, (byte) 0xB0, 0x01 });
									trmnlConfig.setPointOfServiceEntryMode(0x05);
									trmnlConfig.setTransactionCurrencyCode("156");
									trmnlConfig.setTransactionCurrencyExp("1");
									trmnlConfig.setTerminalCountryCode(new byte[] { 0x08, 0x40 });
									trmnlConfig.setInterfaceDeviceSerialNumber("11111111");
									trmnlConfig.setAidPartlyMatchSupported((byte) 0x01);
									boolean issuccess = emv.setTrmnlParams(trmnlConfig);
									mainActivity.appendInteractiveInfoAndShow("设置终端属性结果:" + issuccess, MessageTag.DATA);
								} catch (Exception e) {
									e.printStackTrace();
									mainActivity.appendInteractiveInfoAndShow("设置终端属性失败!" + e.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}

							}
						}).start();
					}
					break;
				}
				}
				break;
			}
			case ListViewBtnKey.ListViewBtnParentKey.PRINT: {

				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.PRINT_BITMAP: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();

								try {
									mainActivity.processingLock();
									mainActivity.appendInteractiveInfoAndShow("打印机状态:" + connected_device.getController().getPrinterStatus(), MessageTag.NORMAL);
									if (connected_device.getController().getPrinterStatus() != PrinterStatus.NORMAL) {
										mainActivity.appendInteractiveInfoAndShow("打印失败！打印状态:" + connected_device.getController().getPrinterStatus(), MessageTag.NORMAL);
										return;
									}
									mainActivity.appendInteractiveInfoAndShow("打印中...", MessageTag.NORMAL);
//									if (((MyApplication) mainActivity.getApplication()).ifSDCardExit()) {
//										path = "/sdcard/data/data/com.example.mainapp/image/test_two.jpg";
//									} else {
//										path = "/data/data/com.example.mainapp/image/test_two.jpg";
//									}
//									Bitmap bitmap = BitmapFactory.decodeFile(path);
									Bitmap bitmap = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.ic_launcher);
									connected_device.getController().printBitMap(0, bitmap);
									mainActivity.appendInteractiveInfoAndShow("打印成功!", MessageTag.NORMAL);
								} catch (Exception ex) {
									Log.e(TAG, "图片打印失败!", ex);
									mainActivity.appendInteractiveInfoAndShow("图片打印失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.PRINT_SCRIPT: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();

								try {
									mainActivity.processingLock();
									mainActivity.appendInteractiveInfoAndShow("打印机状态:" + connected_device.getController().getPrinterStatus(), MessageTag.NORMAL);
									if (connected_device.getController().getPrinterStatus() != PrinterStatus.NORMAL) {
										mainActivity.appendInteractiveInfoAndShow("打印失败！打印状态:" + connected_device.getController().getPrinterStatus(), MessageTag.NORMAL);
										return;
									}
									mainActivity.appendInteractiveInfoAndShow("打印中...", MessageTag.NORMAL);
									connected_device.getController().printScript("!hz l\n !asc sl\n *feedline 3\n *image c 384*71 #ums\n *feedline 1\n *line\n *text l 终端编号(TERMINAL NO.):\n *text l 00011001130000490868\n *text l 操作员号(OPERATOR NO.):\n *text l 01\n *text l 发卡行号(ISSUER NO.):\n *text l 00001\n *text l 收单行号(ACQUIRER NO.):\n *text l 00002\n *text l 有效期(EXP.DATE):\n *text l 2022/08\n *text l 卡号(CARD NO.):\n *text l 622821060028802716\n *text l 交易类型(TRANS TYPE):\n *text l SALE\n *text l 批次号(BATCH NO.):\n *text l 000001\n *text l 凭证号(VOUCHEE NO.):\n *text l 938302\n *text l 授权码(AUTH NO.):\n *text l 012345\n *text l 参考号(REFER NO.):\n *text l 190512928302\n *text l 日期时间(DATE/TIME):\n *text l 2014-06-09 19:05:12\n *text l 金额(AMOUNT):\n !gray 10\n !asc l\n *text l RMB:0.20\n !gray 5\n !asc sl\n *text l 备注(REFERENCE):\n *feedline 3\n *line\n !hz s\n !asc s\n *text l 服务热线：95534\n *text l PAX-P80-311002 持卡人存根CARDHOLDER COPY");
									mainActivity.appendInteractiveInfoAndShow("打印成功!", MessageTag.NORMAL);
								} catch (Exception ex) {
									Log.e(TAG, "打印失败!", ex);
									mainActivity.appendInteractiveInfoAndShow("打印失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.PRINT_STR: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									mainActivity.appendInteractiveInfoAndShow("打印机状态:" + connected_device.getController().getPrinterStatus(), MessageTag.NORMAL);
									mainActivity.processingUnLock();
//									if (connected_device.getController().getPrinterStatus() != PrinterStatus.NORMAL) {
//										mainActivity.appendInteractiveInfoAndShow("打印失败！打印状态：" + connected_device.getController().getPrinterStatus(), MessageTag.NORMAL);
//										return;
//									}
									mainActivity.appendInteractiveInfoAndShow("打印中...", MessageTag.NORMAL);
									String bill = "\n\n    " + "银联商务POS签购单" + "\n";// 交易明细信息
									bill += "商户名称(MERCHANT NAME)：钓鱼岛\n";
									bill += "商户编号(MERCHANT NO.)：805320000000002\n";
									bill += "终端编号(TERMINAL NO.)：80532021\n";
									bill += "操作员号(OPERATOR NO.)：001\n";
									bill += "卡类型：VISA\n";
									bill += "卡号(CARD NO.)：6222222222222222222\n";
									bill += "消费类型：消费\n";
									bill += "授权号(AUTH NO.)：000001\n";
									bill += "参考号(REFER NO.)：000000000000001\n";
									bill += "日期时间(DATE/TIME)：2014-03-24\n";
									bill += "交易金额(AMOUNT)：100RMB\n";
									bill += "---------------------------------------\n";
									bill += "本人确认以上交易，同意将其记入本卡账户\n";
									bill += "I ACKNOWLEDGE SATISFATORY RECEIPT OF RELATIVE GOODS/SERVICES\n";
									bill += "VFI-VX520-31990301 商户存根/MERCHANT COPY\n";
									bill += "----------x-----------------x----------\n\n\n\n\n";
									PrinterResult printerResult=connected_device.getController().printString(bill);
									mainActivity.appendInteractiveInfoAndShow("打印结果:"+printerResult.toString(), MessageTag.NORMAL);

								} catch (Exception ex) {
									ex.printStackTrace();
									Log.e(TAG, "打印失败!", ex);
									mainActivity.appendInteractiveInfoAndShow("打印失败!" + ex.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				}
				break;
			}
			case ListViewBtnKey.ListViewBtnParentKey.FileIoModule: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.INITIALIZERECORD: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									boolean issuccess = connected_device.getController().initializeRecord("FLOWLISTS", 256, 10, 20, 110, 20);
									mainActivity.appendInteractiveInfoAndShow(" 初始化存储记录结果：" + issuccess, MessageTag.NORMAL);
								} catch (Exception ex) {
									Log.e(TAG, " 初始化存储记录失败!", ex);

									mainActivity.appendInteractiveInfoAndShow("初始化存储记录失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.FETCHRECORDCOUNT: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									int count = connected_device.getController().fetchRecordCount("FLOWLISTS");
									mainActivity.appendInteractiveInfoAndShow("获取存储记录数失败:" + count, MessageTag.NORMAL);
								} catch (Exception ex) {
									Log.e(TAG, " 初始化存储记录失败!", ex);

									mainActivity.appendInteractiveInfoAndShow("获取存储记录数失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.ADDRECORD: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									StorageResult storageResult = connected_device.getController().addRecord("FLOWLISTS", "银联支付|20140715|000001".getBytes());
									mainActivity.appendInteractiveInfoAndShow(" 增加存储记录结果：" + StorageResult.getResultDesc(storageResult.getResultCode()), MessageTag.NORMAL);
								} catch (Exception ex) {
									Log.e(TAG, " 增加存储记录失败!", ex);

									mainActivity.appendInteractiveInfoAndShow("增加存储记录失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.UPDATERECORD: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									StorageResult storageResult = connected_device.getController().updateRecord("FLOWLISTS", 1, "银联", "2014", "信用卡".getBytes());
									mainActivity.appendInteractiveInfoAndShow("更新存储记录结果：" + StorageResult.getResultDesc(storageResult.getResultCode()), MessageTag.NORMAL);
								} catch (Exception ex) {
									Log.e(TAG, " 更新存储记录失败!", ex);

									mainActivity.appendInteractiveInfoAndShow("更新存储记录失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.FETCHRECORD: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									byte[] content = connected_device.getController().fetchRecord("FLOWLISTS", 1, "银联", "2014");
									mainActivity.appendInteractiveInfoAndShow("获取存储记录成功!" + new String(content), MessageTag.NORMAL);
								} catch (Exception ex) {
									Log.e(TAG, " 获取存储记录失败!", ex);

									mainActivity.appendInteractiveInfoAndShow("获取存储记录失败!" + ex.getMessage(), MessageTag.ERROR);
								} finally {
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				}
				break;
			}
			case ListViewBtnKey.ListViewBtnParentKey.PARAMETER: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.SET_PARAMETER: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
								LayoutInflater inflater = LayoutInflater.from(mainActivity);
								final View view = inflater.inflate(R.layout.dialog_param, null);
								edit_param_tag = (EditText) view.findViewById(R.id.edit_param_tag);
								edit_param_value = (EditText) view.findViewById(R.id.edit_param_value);
								builder.setView(view);
								builder.setPositiveButton("确定", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										try {
											int param_tag = Integer.parseInt(edit_param_tag.getText().toString().substring(2), 16);
											byte[] param_value = edit_param_value.getText().toString().getBytes("UTF-8");
											connected_device.getController().setParam(param_tag, param_value);
											mainActivity.appendInteractiveInfoAndShow("参数设置成功", MessageTag.NORMAL);
										} catch (Exception e) {
											mainActivity.processingUnLock();
											mainActivity.appendInteractiveInfoAndShow("输入数值有误" + e.getMessage(), MessageTag.ERROR);
										}
									}
								});
								builder.setNegativeButton("取消", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										mainActivity.processingUnLock();
										param_dialog.dismiss();
									}
								});
								mainActivity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										param_dialog = builder.create();
										param_dialog.setTitle("参数设置");
										param_dialog.show();
										param_dialog.setCancelable(false);
										param_dialog.setCanceledOnTouchOutside(false);
									}
								});
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.GET_PARAMETER: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
								LayoutInflater inflater = LayoutInflater.from(mainActivity);
								final View view = inflater.inflate(R.layout.dialog_param, null);
								edit_param_tag = (EditText) view.findViewById(R.id.edit_param_tag);
								LinearLayout linearlayout = (LinearLayout) view.findViewById(R.id.linearlayout);
								linearlayout.setVisibility(View.GONE);
								builder.setView(view);
								builder.setPositiveButton("确定", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										try {
											int param_tag = Integer.parseInt(edit_param_tag.getText().toString().substring(2), 16);
											byte[] param_value = connected_device.getController().getParam(param_tag);
											if (param_value != null) {
												mainActivity.appendInteractiveInfoAndShow("参数获取成功", MessageTag.NORMAL);
												mainActivity.appendInteractiveInfoAndShow("Tag:" + edit_param_tag.getText().toString(), MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("Value:" + new String(param_value, "UTF-8"), MessageTag.DATA);
											} else {
												mainActivity.appendInteractiveInfoAndShow("参数不存在", MessageTag.ERROR);
											}
										} catch (Exception e) {
											mainActivity.processingUnLock();
											mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
										}
									}
								});
								builder.setNegativeButton("取消", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										mainActivity.processingUnLock();
										param_dialog.dismiss();
									}
								});
								mainActivity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										param_dialog = builder.create();
										param_dialog.setTitle("获取参数");
										param_dialog.show();
										param_dialog.setCancelable(false);
										param_dialog.setCanceledOnTouchOutside(false);
									}
								});
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				}
				break;
			}
			case ListViewBtnKey.ListViewBtnParentKey.ENCRYPT: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.ENCRYPTION: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
								LayoutInflater inflater = LayoutInflater.from(mainActivity);
								final View view = inflater.inflate(R.layout.dialog_encryption, null);
								radioGroup_encrypt_type1 = (RadioGroup) view.findViewById(R.id.radioGroup_encrypt_type1);
								radio_CBC = (RadioButton) view.findViewById(R.id.radio_CBC);
								radio_ECB = (RadioButton) view.findViewById(R.id.radio_ECB);
								radioGroup_wk_index = (RadioGroup) view.findViewById(R.id.radioGroup_wk_index);
								radioGroup_wk_index.setOnCheckedChangeListener(new OnCheckedChangeListener() {

									@Override
									public void onCheckedChanged(RadioGroup arg0, int arg1) {
										if (arg1 == R.id.radio_mac) {
											temp_param_index = MacWKIndexConst.DEFAULT_MAC_WK_INDEX;
											workingkey = "MAC";
										} else if (arg1 == R.id.radio_track) {
											temp_param_index = DataEncryptWKIndexConst.DEFAULT_TRACK_WK_INDEX;
											workingkey = "TRACK";
										}

									}
								});
								edit_encryption_value = (EditText) view.findViewById(R.id.edit_encryption_value);
								edit_encryption_value.setText("BF20140804");
								TextView text_encrypt = (TextView) view.findViewById(R.id.text_encrypt);
								text_encrypt.setText("待加密数据：");
								encryptType = EncryptType.CBC;
								radioGroup_encrypt_type1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

									@Override
									public void onCheckedChanged(RadioGroup arg0, int arg1) {
										if (arg1 == radio_CBC.getId()) {
											encryptType = EncryptType.CBC;
										} else if (arg1 == radio_ECB.getId()) {
											encryptType = EncryptType.ECB;
										}
									}
								});
								builder.setView(view);
								builder.setPositiveButton("确定", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										try {
											byte[] input = edit_encryption_value.getText().toString().getBytes("utf_8");
											result = connected_device.getController().encrypt(new WorkingKey(temp_param_index), encryptType, input);
											mainActivity.appendInteractiveInfoAndShow("加密成功!", MessageTag.NORMAL);
											mainActivity.appendInteractiveInfoAndShow("加密数据:" + edit_encryption_value.getText().toString(), MessageTag.DATA);
											mainActivity.appendInteractiveInfoAndShow("加密密钥:" + workingkey, MessageTag.DATA);
											mainActivity.appendInteractiveInfoAndShow("加密模式:" + encryptType, MessageTag.DATA);
											mainActivity.appendInteractiveInfoAndShow("加密结果:" + ISOUtils.hexString(result), MessageTag.DATA);
											((KLApplication) mainActivity.getApplication()).setResult(result);
										} catch (Exception e) {
											mainActivity.appendInteractiveInfoAndShow("加密异常" + e.getMessage(), MessageTag.ERROR);
										}
									}
								});
								builder.setNegativeButton("取消", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										mainActivity.processingUnLock();
										encrypt_dialog.dismiss();
									}
								});
								mainActivity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										encrypt_dialog = builder.create();
										encrypt_dialog.setTitle("加密");
										encrypt_dialog.show();
										encrypt_dialog.setCancelable(false);
										encrypt_dialog.setCanceledOnTouchOutside(false);
									}
								});
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.DECRYPT: {

					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									Looper.prepare();
									final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_encryption, null);
									radioGroup_encrypt_type1 = (RadioGroup) view.findViewById(R.id.radioGroup_encrypt_type1);
									radio_CBC = (RadioButton) view.findViewById(R.id.radio_CBC);
									radio_ECB = (RadioButton) view.findViewById(R.id.radio_ECB);
									radioGroup_wk_index = (RadioGroup) view.findViewById(R.id.radioGroup_wk_index);
									radioGroup_wk_index.setOnCheckedChangeListener(new OnCheckedChangeListener() {

										@Override
										public void onCheckedChanged(RadioGroup arg0, int arg1) {
											if (arg1 == R.id.radio_mac) {
												temp_param_index = MacWKIndexConst.DEFAULT_MAC_WK_INDEX;
												workingkey = "MAC";
											} else if (arg1 == R.id.radio_track) {
												temp_param_index = DataEncryptWKIndexConst.DEFAULT_TRACK_WK_INDEX;
												workingkey = "TRACK";
											}

										}
									});
									edit_encryption_value = (EditText) view.findViewById(R.id.edit_encryption_value);
									if (((KLApplication) mainActivity.getApplication()).getResult() == null) {
										edit_encryption_value.setHint("请先加密或输入测试数据");
									} else {
										edit_encryption_value.setText(ISOUtils.hexString(((KLApplication) mainActivity.getApplication()).getResult()));
									}
									TextView text_encrypt = (TextView) view.findViewById(R.id.text_encrypt);
									text_encrypt.setText("待解密数据：");
									encryptType = EncryptType.CBC;
									radioGroup_encrypt_type1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

										@Override
										public void onCheckedChanged(RadioGroup arg0, int arg1) {
											if (arg1 == radio_CBC.getId()) {
												encryptType = EncryptType.CBC;
											} else if (arg1 == radio_ECB.getId()) {
												encryptType = EncryptType.ECB;
											}

										}
									});

									builder.setView(view);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											try {
												byte[] input = ISOUtils.hex2byte(edit_encryption_value.getText().toString());
												decryresult = connected_device.getController().decrypt(new WorkingKey(temp_param_index), encryptType, input);
												mainActivity.appendInteractiveInfoAndShow("解密数据:" + edit_encryption_value.getText().toString(), MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("解密密钥:" + workingkey, MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("解密模式:" + encryptType, MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("解密结果:" + new String(decryresult, "GBK"), MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("提示:若于加密前数据不一致，请检查解密密钥与模式是否与加密保持一致", MessageTag.TIP);
											} catch (UnsupportedEncodingException e) {
												mainActivity.appendInteractiveInfoAndShow("解密异常" + e.getMessage(), MessageTag.ERROR);
												mainActivity.processingUnLock();
											} catch (Exception e) {
												mainActivity.appendInteractiveInfoAndShow("解密异常" + e.getMessage(), MessageTag.ERROR);
												mainActivity.processingUnLock();
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											encrypt_dialog.dismiss();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											encrypt_dialog = builder.create();
											encrypt_dialog.setTitle("解密");
											encrypt_dialog.show();
											encrypt_dialog.setCancelable(false);
											encrypt_dialog.setCanceledOnTouchOutside(false);
										}
									});
									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.processingUnLock();
									mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
								}
							}
						}).start();
					}
					break;
				}
				}
				break;
			}

			case ListViewBtnKey.ListViewBtnParentKey.ICCardModule: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.CALL: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									Looper.prepare();
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_iccard, null);
									changeListener = new CheckedChangeListener(view, Const.DialogView.IC_CARD_ICCardSlot_DIALOG);
									builder.setTitle("IC卡通信请求:");
									builder.setView(view);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											try {
												icCardSlot = changeListener.getiCCardSlot();
												String str = "0084000004";
												byte req[] = ISOUtils.hex2byte(str);
												byte back[] = connected_device.getController().call(icCardSlot, ICCardType.CPUCARD, req, 30, TimeUnit.SECONDS);
												mainActivity.appendInteractiveInfoAndShow("发送数据:0084000004", MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow("接收数据:" + ISOUtils.hexString(back), MessageTag.DATA);
											} catch (Exception e) {
												mainActivity.appendInteractiveInfoAndShow("IC卡通信异常:" + e.getMessage(), MessageTag.ERROR);
												mainActivity.processingUnLock();
											}

										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											iccard_dialog.dismiss();
											mainActivity.processingUnLock();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											iccard_dialog = builder.create();
											iccard_dialog.show();
											iccard_dialog.setCancelable(false);
											iccard_dialog.setCanceledOnTouchOutside(false);
										}
									});

									mainActivity.processingUnLock();

								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
								}
							}
						}).start();
					}

					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.IC_STATE: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								try {
									mainActivity.processingLock();
									mainActivity.appendInteractiveInfoAndShow("开始检测...", MessageTag.NORMAL);
									Map<ICCardSlot, ICCardSlotState> map = new HashMap<ICCardSlot, ICCardSlotState>();
									map = connected_device.getController().checkSlotsState();
									for (Map.Entry<ICCardSlot, ICCardSlotState> entry : map.entrySet()) {
										if (entry.getKey() != null)
											if (!entry.getValue().toString().equals("NO_CARD")) {
												mainActivity.appendInteractiveInfoAndShow("卡槽:" + entry.getKey() + "--->" + "卡:" + entry.getValue(), MessageTag.TIP);
											} else {
												mainActivity.appendInteractiveInfoAndShow("卡槽:" + entry.getKey() + "--->" + "卡:" + entry.getValue(), MessageTag.NORMAL);
											}
									}
									mainActivity.appendInteractiveInfoAndShow("检测结束...", MessageTag.NORMAL);
									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.processingUnLock();
									mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
								}
							}
						}).start();
					}

					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.POWER_0N: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_iccard, null);
									LinearLayout ic_send_data = (LinearLayout) view.findViewById(R.id.ic_send_data);
									ic_send_data.setVisibility(View.GONE);
									changeListener = new CheckedChangeListener(view, Const.DialogView.IC_CARD_ICCardSlot_DIALOG);
									builder.setTitle("IC卡上电:");
									builder.setView(view);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											try {
												icCardSlot = changeListener.getiCCardSlot();
												byte result[] = connected_device.getController().powerOn(icCardSlot, ICCardType.CPUCARD);
												mainActivity.appendInteractiveInfoAndShow("result:" + Dump.getHexDump(result), MessageTag.DATA);
												mainActivity.appendInteractiveInfoAndShow(icCardSlot.toString() + "上电完成", MessageTag.NORMAL);
											} catch (Exception e) {
												mainActivity.appendInteractiveInfoAndShow("卡槽上电异常:" + e.getMessage(), MessageTag.ERROR);
												mainActivity.appendInteractiveInfoAndShow("请检查该卡槽是否已插入IC卡", MessageTag.TIP);
												mainActivity.processingUnLock();
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											iccard_dialog.dismiss();
											mainActivity.processingUnLock();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											iccard_dialog = builder.create();
											iccard_dialog.show();
											iccard_dialog.setCancelable(false);
											iccard_dialog.setCanceledOnTouchOutside(false);
										}
									});
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("卡槽上电异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}

					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.POWER_OFF: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_iccard, null);
									LinearLayout ic_send_data = (LinearLayout) view.findViewById(R.id.ic_send_data);
									ic_send_data.setVisibility(View.GONE);
									changeListener = new CheckedChangeListener(view, Const.DialogView.IC_CARD_ICCardSlot_DIALOG);
									builder.setTitle("IC卡下电:");
									builder.setView(view);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											try {
												icCardSlot = changeListener.getiCCardSlot();
												connected_device.getController().powerOff(icCardSlot, ICCardType.CPUCARD);
												mainActivity.appendInteractiveInfoAndShow(icCardSlot.toString() + "下电完成", MessageTag.NORMAL);
											} catch (Exception e) {
												mainActivity.appendInteractiveInfoAndShow("卡槽下电异常:" + e.getMessage(), MessageTag.ERROR);
												mainActivity.appendInteractiveInfoAndShow("请检查该卡槽是否已插入IC卡", MessageTag.TIP);
												mainActivity.processingUnLock();
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											iccard_dialog.dismiss();
											mainActivity.processingUnLock();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											iccard_dialog = builder.create();
											iccard_dialog.show();
											iccard_dialog.setCancelable(false);
											iccard_dialog.setCanceledOnTouchOutside(false);
										}
									});
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("卡槽下电异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}

					break;
				}
				}
				break;
			}

			case ListViewBtnKey.ListViewBtnParentKey.QPCardModule: {
				switch (children) {
				case ListViewBtnKey.ListViewBtnChildKey.QC_SEARCH_POWER_ON: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
								builder.setTitle("非接卡类型:");
								builder.setSingleChoiceItems(new String[] { "A卡", "B卡", "M1卡" }, 2, new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										switch (arg1) {
										case 0:
											qpCardType = RFCardType.ACARD;
											break;
										case 1:
											qpCardType = RFCardType.BCARD;
											break;
										case 2:
											qpCardType = RFCardType.M1CARD;
											break;
										}

									}
								});
								builder.setPositiveButton("确定", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										new Thread(new Runnable() {

											@Override
											public void run() {
												try {
													RFResult qPResult = connected_device.getController().powerOn(qpCardType, 5);
													mainActivity.appendInteractiveInfoAndShow("非接卡名:" + qPResult.getQpCardName(), MessageTag.DATA);
													mainActivity.appendInteractiveInfoAndShow("非接卡类型:" + qPResult.getQpCardType(), MessageTag.DATA);
													if (qPResult.getCardSerialNo() == null) {
														mainActivity.appendInteractiveInfoAndShow("非接卡序列号:null", MessageTag.DATA);
													} else {
														mainActivity.appendInteractiveInfoAndShow("非接卡序列号:" + Dump.getHexDump(qPResult.getCardSerialNo()), MessageTag.DATA);
													}

													if (qPResult.getATQA() == null) {
														mainActivity.appendInteractiveInfoAndShow("非接卡ATQA:null", MessageTag.DATA);
													} else {
														mainActivity.appendInteractiveInfoAndShow("非接卡ATQA:" + Dump.getHexDump(qPResult.getATQA()), MessageTag.DATA);
													}
													mainActivity.appendInteractiveInfoAndShow("寻卡上电完成", MessageTag.NORMAL);
												} catch (Exception e) {
													mainActivity.appendInteractiveInfoAndShow("非接卡寻卡上电异常:" + e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
												}
											}
										}).start();

										nccard_dialog.dismiss();
									}
								});
								builder.setNegativeButton("取消", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										mainActivity.processingUnLock();
										nccard_dialog.dismiss();
										mainActivity.processingUnLock();
									}
								});
								mainActivity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										nccard_dialog = builder.create();
										nccard_dialog.show();
										nccard_dialog.setCancelable(false);
										nccard_dialog.setCanceledOnTouchOutside(false);
									}
								});
								mainActivity.processingUnLock();

							}
						}).start();
					}

					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_CALL: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									String str = "0084000004";
									byte req[] = ISOUtils.hex2byte(str);
									byte result[] = connected_device.getController().call(req, 1200, TimeUnit.SECONDS);
									mainActivity.appendInteractiveInfoAndShow("发送数据:" + str, MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("接收数据:" + ISOUtils.hexString(result), MessageTag.DATA);
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("非接卡通信异常" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}

					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_POWER_OFF: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									connected_device.getController().powerOff(1200);
									mainActivity.appendInteractiveInfoAndShow("非接卡下电完成", MessageTag.NORMAL);
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("非接卡下电异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_LOAD_KEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_key, null);
									builder.setView(view);
									LinearLayout qc_data_area = (LinearLayout) view.findViewById(R.id.qc_data_area);
									qc_data_area.setVisibility(View.GONE);
									LinearLayout qc_snr_area = (LinearLayout) view.findViewById(R.id.qc_snr_area);
									qc_snr_area.setVisibility(View.GONE);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									changeListener = new CheckedChangeListener(view, Const.DialogView.NC_CARD_KEY_DIALOG);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null) {
												try {
													RFKeyMode qpKeyMode = changeListener.getRfKeyMode();
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													if (block >= 0 && block < 15) {
														mainActivity.appendInteractiveInfoAndShow("KEY模式:" + qpKeyMode, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("密钥存储区:" + block, MessageTag.DATA);
														connected_device.getController().loadKey(qpKeyMode, block);
														mainActivity.appendInteractiveInfoAndShow("非接卡加载密钥完成", MessageTag.NORMAL);
													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);
													}
													mainActivity.processingUnLock();
													nccard_dialog.dismiss();
												} catch (DeviceInvokeException e) {
													mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
												}
											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.setTitle("存储密钥:");
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("非接卡加载密钥异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_STORE_KEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {

									final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_key, null);
									builder.setView(view);
									LinearLayout qc_snr_area = (LinearLayout) view.findViewById(R.id.qc_snr_area);
									qc_snr_area.setVisibility(View.GONE);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									edit_qccard_key = (EditText) view.findViewById(R.id.edit_qccard_key);
									changeListener = new CheckedChangeListener(view, Const.DialogView.NC_CARD_KEY_DIALOG);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null && edit_qccard_key.getText().toString() != null) {
												try {
													RFKeyMode qpKeyMode = changeListener.getRfKeyMode();
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													byte key[] = edit_qccard_key.getText().toString().getBytes("GBK");
													if (block >= 0 && block < 15 && edit_qccard_key.getText().toString().length() == 6) {
														mainActivity.appendInteractiveInfoAndShow("KEY模式:" + qpKeyMode, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("密钥存储区:" + block, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("密钥:" + key, MessageTag.DATA);
														connected_device.getController().storeKey(qpKeyMode, block, key);
														mainActivity.appendInteractiveInfoAndShow("非接卡存储密钥完成", MessageTag.NORMAL);

													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);

													}
													mainActivity.processingUnLock();
													nccard_dialog.dismiss();

												} catch (DeviceInvokeException e) {
													mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
												} catch (UnsupportedEncodingException e) {
													mainActivity.appendInteractiveInfoAndShow("数据转换异常", MessageTag.ERROR);
													mainActivity.processingUnLock();
												}
											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.setTitle("存储密钥:");
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});

								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("非接卡存储密钥异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_WRITE_DATA_BLOCK: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									builder.setTitle("写块数据:");
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_data, null);
									builder.setView(view);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									edit_qccard_data = (EditText) view.findViewById(R.id.edit_qccard_data);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null && edit_qccard_data.getText().toString() != null) {
												try {
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													byte input[] = ISOUtils.hex2byte(edit_qccard_data.getText().toString());
													if (block >= 0 && block < 255 && edit_qccard_data.getText().toString().length() == 16) {
														connected_device.getController().writeDataBlock(block, input);
														mainActivity.appendInteractiveInfoAndShow("存储块:" + block, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("数据:" + input, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("写块数据完成", MessageTag.NORMAL);
													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);
													}
												} catch (Exception e) {
													mainActivity.appendInteractiveInfoAndShow("写块数据异常:" + e.getMessage(), MessageTag.ERROR);
													mainActivity.appendInteractiveInfoAndShow("请先确定非接卡已上电", MessageTag.TIP);
													mainActivity.processingUnLock();
												}
											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}

										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
											mainActivity.processingUnLock();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});

									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("写块数据异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_READ_DATA_BLOCK: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									builder.setTitle("读块数据:");
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_data, null);
									builder.setView(view);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									LinearLayout qc_data_area = (LinearLayout) view.findViewById(R.id.qc_data_area);
									qc_data_area.setVisibility(View.GONE);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null) {
												try {
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													if (block >= 0 && block < 255 && edit_qccard_data.getText().length() == 16) {
														byte output[] = connected_device.getController().readDataBlock(block);
														mainActivity.appendInteractiveInfoAndShow("存储块:" + block, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("数据:" + ISOUtils.hexString(output), MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("读块数据完成", MessageTag.NORMAL);
													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);
													}
												} catch (Exception e) {
													mainActivity.appendInteractiveInfoAndShow("读块数据异常:" + e.getMessage(), MessageTag.ERROR);
													mainActivity.appendInteractiveInfoAndShow("请先确定非接卡已上电或该数据块已写入数据", MessageTag.TIP);
													mainActivity.processingUnLock();
												}

											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}

										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
											mainActivity.processingUnLock();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});

									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("读块数据异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_DECREMENT_OPERATION: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									builder.setTitle("减量数据:");
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_data, null);
									builder.setView(view);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									edit_qccard_data = (EditText) view.findViewById(R.id.edit_qccard_data);
									edit_qccard_data.setText("0000");
									TextView text_qccard_data = (TextView) view.findViewById(R.id.text_qccard_data);
									text_qccard_data.setText("数据(4位):");
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null) {
												try {
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													if (block > 0 && block < 255 && edit_qccard_data.getText().length() == 4) {
														byte output[] = connected_device.getController().readDataBlock(block);
														mainActivity.appendInteractiveInfoAndShow("存储块:" + block, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("数据:" + new String(output, "GBK"), MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("减量操作完成", MessageTag.NORMAL);
													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);
													}
												} catch (UnsupportedEncodingException e) {
													mainActivity.appendInteractiveInfoAndShow("数据转换异常", MessageTag.ERROR);
													mainActivity.processingUnLock();
												} catch (Exception e) {
													mainActivity.appendInteractiveInfoAndShow("读块数据异常:" + e.getMessage(), MessageTag.ERROR);
													mainActivity.appendInteractiveInfoAndShow("请先确定非接卡已上电", MessageTag.TIP);
													mainActivity.processingUnLock();
												}

											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}

										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
											mainActivity.processingUnLock();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});

									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("读块数据异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_INCREMENT_OPERATION: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {
									final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
									builder.setTitle("增量数据:");
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_data, null);
									builder.setView(view);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									edit_qccard_data = (EditText) view.findViewById(R.id.edit_qccard_data);
									edit_qccard_data.setText("0000");
									TextView text_qccard_data = (TextView) view.findViewById(R.id.text_qccard_data);
									text_qccard_data.setText("数据(4位):");
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null) {
												try {
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													if (block > 0 && block < 255 && edit_qccard_data.getText().length() == 4) {
														byte output[] = connected_device.getController().readDataBlock(block);
														mainActivity.appendInteractiveInfoAndShow("存储块:" + block, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("数据:" + new String(output, "GBK"), MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("增量操作完成", MessageTag.NORMAL);
													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);
													}
												} catch (UnsupportedEncodingException e) {
													mainActivity.appendInteractiveInfoAndShow("数据转换异常", MessageTag.ERROR);
													mainActivity.processingUnLock();
												} catch (Exception e) {
													mainActivity.appendInteractiveInfoAndShow("读块数据异常:" + e.getMessage(), MessageTag.ERROR);
													mainActivity.appendInteractiveInfoAndShow("请先确定非接卡已上电", MessageTag.TIP);
													mainActivity.processingUnLock();
												}

											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}

										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
											mainActivity.processingUnLock();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});

									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("读块数据异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
								mainActivity.processingUnLock();
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_AUTHENTICATE_BY_EXTEND_KEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {

									final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_key, null);
									builder.setView(view);
									TextView text_key = (TextView) view.findViewById(R.id.text_key);
									text_key.setText("外部密钥(6位):");
									TextView text_block = (TextView) view.findViewById(R.id.text_block);
									text_block.setText("认证的块号(0-255):");
									edit_snr_no = (EditText) view.findViewById(R.id.edit_snr_no);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									edit_qccard_key = (EditText) view.findViewById(R.id.edit_qccard_key);
									changeListener = new CheckedChangeListener(view, Const.DialogView.NC_CARD_KEY_DIALOG);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null && edit_qccard_key.getText().toString() != null && edit_snr_no.getText().toString() != null) {
												try {
													RFKeyMode qpKeyMode = changeListener.getRfKeyMode();
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													byte snr[] = edit_snr_no.getText().toString().getBytes("GBK");
													byte key[] = edit_qccard_key.getText().toString().getBytes("GBK");
													if (block >= 0 && block < 255 && edit_qccard_key.getText().toString().length() == 6 && edit_snr_no.getText().toString().length() == 4) {
														mainActivity.appendInteractiveInfoAndShow("KEY模式:" + qpKeyMode, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("SNR序列号:" + snr, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("认证块号:" + block, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("外部密钥:" + key, MessageTag.DATA);
														connected_device.getController().authenticateByExtendKey(qpKeyMode, snr, block, key);
														mainActivity.appendInteractiveInfoAndShow("非接卡使用外部密钥认证完成", MessageTag.NORMAL);

													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);
													}
													mainActivity.processingUnLock();
													nccard_dialog.dismiss();

												} catch (DeviceInvokeException e) {
													mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
												} catch (UnsupportedEncodingException e) {
													mainActivity.appendInteractiveInfoAndShow("非接卡外部密钥认证异常", MessageTag.ERROR);
													mainActivity.processingUnLock();
												} catch (Exception e) {
													mainActivity.appendInteractiveInfoAndShow("非接卡外部密钥认证异常", MessageTag.ERROR);
													mainActivity.processingUnLock();
												}
											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.setTitle("外部密钥认证:");
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});
									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("非接卡外部密钥认证异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				case ListViewBtnKey.ListViewBtnChildKey.QC_AUTHENTICATE_BY_LOADED_KEY: {
					if (mainActivity.processingisLocked()) {
						mainActivity.appendInteractiveInfoAndShow("请先完成或撤销当前操作", MessageTag.TIP);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								connected_device.connectDevice();
								mainActivity.processingLock();
								Looper.prepare();
								try {

									final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									LayoutInflater inflater = LayoutInflater.from(mainActivity);
									final View view = inflater.inflate(R.layout.dialog_qc_key, null);
									builder.setView(view);
									LinearLayout qc_data_area = (LinearLayout) view.findViewById(R.id.qc_data_area);
									qc_data_area.setVisibility(View.GONE);
									TextView text_block = (TextView) view.findViewById(R.id.text_block);
									text_block.setText("认证的块号(0-255):");
									edit_snr_no = (EditText) view.findViewById(R.id.edit_snr_no);
									edit_qccard_block = (EditText) view.findViewById(R.id.edit_qccard_block);
									changeListener = new CheckedChangeListener(view, Const.DialogView.NC_CARD_KEY_DIALOG);
									builder.setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											if (edit_qccard_block.getText().toString() != null && edit_snr_no.getText().toString() != null) {
												try {
													RFKeyMode qpKeyMode = changeListener.getRfKeyMode();
													int block = Integer.valueOf(edit_qccard_block.getText().toString());
													byte snr[] = edit_snr_no.getText().toString().getBytes("GBK");
													if (block >= 0 && block < 255 && edit_snr_no.getText().toString().length() == 4) {
														mainActivity.appendInteractiveInfoAndShow("KEY模式:" + qpKeyMode, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("SNR序列号:" + snr, MessageTag.DATA);
														mainActivity.appendInteractiveInfoAndShow("认证块号:" + block, MessageTag.DATA);
														connected_device.getController().authenticateByLoadedKey(qpKeyMode, snr, block);
														mainActivity.appendInteractiveInfoAndShow("非接卡使用加载密钥认证完成", MessageTag.NORMAL);

													} else {
														mainActivity.appendInteractiveInfoAndShow("输入不合法", MessageTag.ERROR);
													}
													mainActivity.processingUnLock();
													nccard_dialog.dismiss();

												} catch (DeviceInvokeException e) {
													mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
												} catch (UnsupportedEncodingException e) {
													mainActivity.appendInteractiveInfoAndShow("非接卡使用加载密钥认证异常" + e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
												} catch (Exception e) {
													mainActivity.appendInteractiveInfoAndShow(e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
												}
											} else {
												mainActivity.appendInteractiveInfoAndShow("输入不为空", MessageTag.ERROR);
											}
										}
									});
									builder.setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											mainActivity.processingUnLock();
											nccard_dialog.dismiss();
										}
									});
									mainActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											nccard_dialog = builder.create();
											nccard_dialog.setTitle("加载密钥认证:");
											nccard_dialog.show();
											nccard_dialog.setCancelable(false);
											nccard_dialog.setCanceledOnTouchOutside(false);
										}
									});
									mainActivity.processingUnLock();
								} catch (Exception e) {
									mainActivity.appendInteractiveInfoAndShow("非接卡使用加载密钥认证异常:" + e.getMessage(), MessageTag.ERROR);
									mainActivity.processingUnLock();
								}
							}
						}).start();
					}
					break;
				}
				}
				break;
			}
			}
		}
		return false;

	}

	public void jiaoyi() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				connected_device.connectDevice();
				try {
					mainActivity.processingLock();
					Looper.prepare();
					final Builder builder_ic_pinInput = new android.app.AlertDialog.Builder(mainActivity);
					builder_ic_pinInput.setTitle("密码输入方式:【仅针对IC卡有效】");
					builder_ic_pinInput.setSingleChoiceItems(IC_PIN_INPUT, 0, new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							((KLApplication) mainActivity.getApplication()).setIc_pinInput_flag(arg1);
							ic_pinInput_dialog.dismiss();
							amt_dialog.show();
						}
					});

					mainActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ic_pinInput_dialog = builder_ic_pinInput.create();
							ic_pinInput_dialog.setCancelable(false);
							ic_pinInput_dialog.setCanceledOnTouchOutside(false);
							ic_pinInput_dialog.show();
						}
					});
					final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
					LayoutInflater inflater = LayoutInflater.from(mainActivity);
					final View view = inflater.inflate(R.layout.dialog_amtinput, null);
					builder.setTitle("请输入交易金额（最高12位）:");
					edit_amt_input = (EditText) view.findViewById(R.id.edit_amt_input);
					btn_sure = (Button) view.findViewById(R.id.btn_sure);
					btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
					builder.setView(view);
					edit_amt_input.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
							temp = arg0;

						}
						@Override
						public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						}

						@Override
						public void afterTextChanged(Editable arg0) {
							if (temp.length() > 12) {
								btn_sure.setEnabled(false);
							} else {
								btn_sure.setEnabled(true);
							}

						}
					});

					btn_sure.setOnClickListener(new android.view.View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (connected_device.isControllerAlive()) {
								Editable editable = edit_amt_input.getText();
								try {
									DecimalFormat df = new DecimalFormat("#0.00");
									amt = new BigDecimal(editable.toString());
									((KLApplication) mainActivity.getApplication()).setAmt(amt);
									mainActivity.appendInteractiveInfoAndShow("交易金额为:" + df.format(amt), MessageTag.DATA);
									mainActivity.appendInteractiveInfoAndShow("请刷卡或插入IC卡...", MessageTag.NORMAL);
									new Thread(new Runnable() {
										@Override
										public void run() {
											if (connected_device.isControllerAlive() == false) {
												connected_device.connectDevice();
											}
											try {
												DecimalFormat df = new DecimalFormat("#0.00");
												connected_device.getController().startTransfer(mainActivity, 
														new OpenCardType[]{OpenCardType.SWIPER,
														OpenCardType.ICCARD,
														OpenCardType.NCCARD}, 
														"交易金额为:" + df.format(amt) + "\n请刷卡或者插入IC卡", amt, 60,
														TimeUnit.SECONDS, 
														CardRule.ALLOW_LOWER,  
														new SimpleTransferListener(connected_device, mainActivity));
											} catch (Exception e) {
												if (e instanceof ProcessTimeoutException) {
													mainActivity.appendInteractiveInfoAndShow("swipe failed:超时!" + e.getMessage(), MessageTag.ERROR);
													mainActivity.processingUnLock();
													return;
												} else if (e instanceof DeviceRTException) {
													mainActivity.appendInteractiveInfoAndShow("swipe failed:交易失败" + e.getMessage(), MessageTag.ERROR);
													// mainActivity.appendInteractiveInfoAndShow("请重新刷卡或插卡",
													// MessageTag.TIP);
													final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
													builder.setTitle("swipe failed:").setMessage("是否重新刷卡或插卡?");
													builder.setPositiveButton("是", new OnClickListener() {

														@Override
														public void onClick(DialogInterface arg0, int arg1) {
															new Thread(new Runnable() {
																@Override
																public void run() {
																	message_dialog.dismiss();
																	reDoSwipeorInsertCard();
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
										}
									}).start();
									amt_dialog.dismiss();
								} catch (NumberFormatException e) {
									mainActivity.appendInteractiveInfoAndShow("输入金额有误", MessageTag.ERROR);
									mainActivity.processingUnLock();
								}

							} else {
								mainActivity.appendInteractiveInfoAndShow("设备断开", MessageTag.ERROR);
							}
						}
					});
					btn_cancel.setOnClickListener(new android.view.View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mainActivity.appendInteractiveInfoAndShow("交易取消", MessageTag.ERROR);
							mainActivity.processingUnLock();
							amt_dialog.dismiss();

						}
					});
					mainActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							amt_dialog = builder.create();
							amt_dialog.setCancelable(false);
							amt_dialog.setCanceledOnTouchOutside(false);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					mainActivity.appendInteractiveInfoAndShow("执行pboc交易失败，" + e.getMessage(), MessageTag.ERROR);
				}
			}
		}).start();
	}

	/**
	 * 选择固件
	 */
	public void selectFirmware() {

		fileListView = new ListView(mainActivity);
		allFiles = updatePath(((KLApplication) mainActivity.getApplication()).getNLDPath());
		if (allFiles.size() == 0) {
			mainActivity.appendInteractiveInfoAndShow("指定目录下无更新文件，请手动选择", MessageTag.TIP);
			allFiles = updatePath(SDCardPath);
		}
		fileAdapter = new FileAdapter(allFiles, mainActivity);
		fileListView.setAdapter(fileAdapter);
		fileListView.setPadding(5, 10, 0, 10);
		fileListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
				File file = (File) fileAdapter.getItem(position);
				if (file.isFile()) {
					if (!file.getName().endsWith(".NLP") && !file.getName().endsWith(".NLD")) {
						mainActivity.appendInteractiveInfoAndShow("请选择正确的固件进行更新", MessageTag.ERROR);
					} else {
						String file_name = file.getName().substring(0, file.getName().lastIndexOf("."));
						String temp[] = file_name.split("_");
						if (temp.length < 4 || !temp[3].matches("^\\d+(\\.\\d+)?$")) {
							mainActivity.appendInteractiveInfoAndShow("固件名称不正确,EXP:xxx_xxx_xxx_8.0.nld", MessageTag.ERROR);
							showFile.dismiss();
							return;
						}
						String newAppVer = temp[3];
						DeviceInfo deviceInfo = connected_device.getController().getDeviceInfo();
						String oldAppVer = deviceInfo.getAppVer();
//						if (newAppVer.compareTo(oldAppVer) > 0) {
							NLDPathString = file.getPath();
							mainActivity.appendInteractiveInfoAndShow("文件路径:" + NLDPathString, MessageTag.DATA);
							mainActivity.appendInteractiveInfoAndShow("文件大小:" + file.length(), MessageTag.DATA);
							((KLApplication) mainActivity.getApplication()).setNLDPathString(NLDPathString);
//						} else {
//							mainActivity.appendInteractiveInfoAndShow("当前设备应用版本已是最新", MessageTag.NORMAL);
//						}
					}
					showFile.dismiss();
				} else if (file.isDirectory()) {
					allFiles.clear();
					allFiles.addAll(updatePath(file.getPath()));
					fileAdapter.notifyDataSetChanged();
				}

			}
		});
		showFile = new AlertDialog.Builder(mainActivity).setTitle("请选择固件：").setView(fileListView).setNegativeButton("取消", null).create();
		showFile.show();
		((KLApplication) mainActivity.getApplication()).setNLDPathString(NLDPathString);

	}

	/**
	 * 固件更新路径
	 * 
	 * @param path
	 * @return
	 */
	private List<File> updatePath(String path) {
		File[] files = new File(path).listFiles();
		ArrayList<File> allFiles = new ArrayList<File>();
		if (files.length > 0) {
			ArrayList<File> isFiles = new ArrayList<File>();
			ArrayList<File> isFolder = new ArrayList<File>();
			for (File file : files) {
				if (file.isFile() && (file.getName().endsWith(".NLD")|| file.getName().endsWith(".NLP"))) {
					isFiles.add(file);
				} else if (file.isDirectory()) {
					isFolder.add(file);
				}
			}
			allFiles.addAll(isFiles);
			allFiles.addAll(isFolder);
		}
		return allFiles;
	}

	private void reDoSwipeorInsertCard() {
		try {
			DecimalFormat df = new DecimalFormat("#.00");
			connected_device.getController().startTransfer(mainActivity, new OpenCardType[]{OpenCardType.SWIPER,OpenCardType.ICCARD,OpenCardType.NCCARD}, "Amount：" + df.format(amt) + "\nSwipe or insert card", amt, 60, TimeUnit.SECONDS, CardRule.ALLOW_LOWER, new SimpleTransferListener(connected_device, mainActivity));
		
		} catch (Exception e) {
			if (e instanceof ProcessTimeoutException) {
				mainActivity.appendInteractiveInfoAndShow("swipe failed:超时!" + e.getMessage(), MessageTag.ERROR);
				mainActivity.processingUnLock();
				return;
			} else if (e instanceof DeviceRTException) {
				final Builder builder = new android.app.AlertDialog.Builder(mainActivity);
				builder.setTitle("swipe failed:").setMessage("是否重新刷卡或插卡?");
				builder.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								message_dialog.dismiss();
								reDoSwipeorInsertCard();
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

				mainActivity.appendInteractiveInfoAndShow("swipe failed:交易失败" + e.getMessage(), MessageTag.ERROR);
			}
		}
	}
}

