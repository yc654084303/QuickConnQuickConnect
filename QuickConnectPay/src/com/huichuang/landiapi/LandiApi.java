package com.huichuang.landiapi;

import com.huichuang.sharedPreferences.AipSharedPreferences;
import com.huihuang.utils.ByteArrayUtils;
import com.huihuang.utils.MainFrameTask;
import com.landicorp.android.mpos.reader.LandiMPos;
import com.landicorp.android.mpos.reader.PBOCOnlineDataProcessListener;
import com.landicorp.android.mpos.reader.PBOCStartListener;
import com.landicorp.android.mpos.reader.PBOCStopListener;
import com.landicorp.android.mpos.reader.model.InputPinParameter;
import com.landicorp.android.mpos.reader.model.OnlineDataProcessResult;
import com.landicorp.android.mpos.reader.model.PBOCOnlineData;
import com.landicorp.android.mpos.reader.model.StartPBOCParam;
import com.landicorp.android.mpos.reader.model.StartPBOCResult;
import com.landicorp.mpos.reader.BasicReaderListeners.CalcMacListener;
import com.landicorp.mpos.reader.BasicReaderListeners.CardType;
import com.landicorp.mpos.reader.BasicReaderListeners.CloseDeviceListener;
import com.landicorp.mpos.reader.BasicReaderListeners.EMVProcessListener;
import com.landicorp.mpos.reader.BasicReaderListeners.GetPANListener;
import com.landicorp.mpos.reader.BasicReaderListeners.GetTrackDataPlainListener;
import com.landicorp.mpos.reader.BasicReaderListeners.InputPinListener;
import com.landicorp.mpos.reader.BasicReaderListeners.OpenDeviceListener;
import com.landicorp.mpos.reader.BasicReaderListeners.WaitCardType;
import com.landicorp.mpos.reader.BasicReaderListeners.WaitingCardListener;
import com.landicorp.mpos.reader.model.MPosEMVProcessResult;
import com.landicorp.robert.comm.api.CommunicationManagerBase.CommunicationMode;
import com.landicorp.robert.comm.api.CommunicationManagerBase.DeviceCommunicationChannel;
import com.landicorp.robert.comm.api.DeviceInfo;

import android.content.Context;

public class LandiApi {

	private LandiMPos reader;
	private MainFrameTask mMainFrameTask = null;
	private Context context;
	private DeviceInfo deviceInfo;

	public LandiApi(Context context) {
		this.context = context;
		reader = LandiMPos.getInstance(context);
	}

	// ���豸
	public void openDevice() {
		deviceInfo = AipSharedPreferences.getInstance(context).getDeviceInfo();
		deviceInfo.setDevChannel(DeviceCommunicationChannel.BLUETOOTH);
		mMainFrameTask.startProgressDialog("���ڴ��豸���Ժ󡣡���");
		reader.openDevice(CommunicationMode.MODE_DUPLEX, deviceInfo,
				new OpenDeviceListener() {

					@Override
					public void openSucc() {
						// mainedittext.setText("���豸�ɹ���" +
						// deviceInfo.getName());
						mMainFrameTask.stopProgressDialog();
					}

					@Override
					public void openFail() {
						// mainedittext.setText("���豸ʧ��");
						mMainFrameTask.stopProgressDialog();
					}
				});
	}

	// �ر��豸
	public void closeDevice() {
		mMainFrameTask.startProgressDialog("���ڹر��豸������");
		reader.closeDevice(new CloseDeviceListener() {
			@Override
			public void closeSucc() {
				// s mainedittext.setText("�ر��豸�ɹ���");
				mMainFrameTask.stopProgressDialog();
			}
		});
	}

	// �ȴ�ˢ��
	public void waitCard() {
		reader.waitingCard(WaitCardType.MAGNETIC_IC_CARD_RFCARD,
				"000000000001", "��ˢ��1", 100, new WaitingCardListener() {
					@Override
					public void onError(int errCode, String errDesc) {
						// mainedittext.setText("����" + errDesc);
					}

					@Override
					public void onWaitingCardSucc(CardType cardType) {
						// mainedittext.setText("��Ƭ���ͣ�" + cardType.name());
					}
				});
	}

	// ��ȡpin
	public void readPin() {
		InputPinParameter inputPinParameter = new InputPinParameter();
		inputPinParameter.setCardNO("62270018230601407");
		inputPinParameter.setTimeout((byte) 60000);
		inputPinParameter.setAmount("999999990000");

		reader.inputPin(inputPinParameter, new InputPinListener() {
			@Override
			public void onError(int errCode, String errDesc) {
				// mainedittext.setText("��ȡPIN��Կʧ��" + errDesc);
			}

			@Override
			public void onInputPinSucc(byte[] pinblock) {
				// mainedittext.setText("��ȡPIN��Կ�ɹ�"
				// + ByteArrayUtils.byteArray2HexString(pinblock));
			}
		});
	}

	// ��ʼemv����
	public void beginEmv() {
		reader.waitingCard(WaitCardType.MAGNETIC_IC_CARD_RFCARD, "0000", "��ˢ��",
				60000, new WaitingCardListener() {
					@Override
					public void onError(int errCode, String errDesc) {
						// mainedittext.setText("����ʧ��" + errDesc);
					}

					@Override
					public void onWaitingCardSucc(CardType cardType) {
						if (cardType.equals(CardType.IC_CARD)
								|| cardType.equals(CardType.RF_CARD)) {
							final StartPBOCParam startPBOCParam = new StartPBOCParam();
							String amount;
							amount = "999999990000";
							byte emvTradeType = 0x00;
							startPBOCParam.setTransactionType(emvTradeType);
							startPBOCParam.setAuthorizedAmount(amount);
							startPBOCParam.setOtherAmount("000000000000");
							startPBOCParam.setDate("140611");
							startPBOCParam.setTime("204409"); // "pos_time":
							startPBOCParam.setForbidContactCard(false);
							startPBOCParam.setForceOnline(true);
							startPBOCParam.setForbidMagicCard(false);
							startPBOCParam.setForbidContactlessCard(false);
							reader.startPBOC(startPBOCParam,
									new EMVProcessListener() {
										@Override
										public void onError(int errCode,
												String errDesc) {
											// mainedittext.setText("��ȡ���ŵ�ʧ��:"+
											// errDesc);
										}

										@Override
										public void onEMVProcessSucc(
												MPosEMVProcessResult result) {
											// mainedittext.setText("����:"+result.getCardHolderName()+
											// "ʧЧ����:"
											// + result.getExpireData()
											// + "pan���к�"
											// + result.getPanSerial()+"���ŵ�:"
											// + result.getTrack2());
										}
									}, new PBOCStartListener() {
										@Override
										public void onError(int errCode,
												String errDesc) {
											// mainedittext.setText("����ʧ��:"
											// + errDesc);
										}

										@Override
										public void onPBOCStartSuccess(
												StartPBOCResult result) {
											// mainedittext.setText("���׳ɹ�"
											// + ByteArrayUtils
											// .byteArray2HexString(result
											// .getPwdData()));
										}
									});
						}
					}
				});
	}

	// PBOC��������
	public void emvProcess() {
		PBOCOnlineData onlineData = new PBOCOnlineData();
		String emvData = "950500000410009f1a0201569f2608ab481a11622715949f2701805f2a0201569f02060000000000019f360200739f3704541313459f3501229f34030203009f33036040c09f03060000000000009a031403229f090200209c01009f4104000000009f101307010103a0a802010a010000000000aa3fb5f19f1e08303331393638313582027c008408a000000333010101";
		String authRespCode = "91";
		byte[] bytes = ByteArrayUtils.toByteArray(emvData);
		onlineData.setAuthRespCode(ByteArrayUtils.getBytes(authRespCode));
		onlineData.setOnlineData(bytes);
		reader.onlineDataProcess(onlineData,
				new PBOCOnlineDataProcessListener() {
					@Override
					public void onError(int errCode, String errDesc) {
						// mainedittext.setText("����ʧ��" + errDesc);
					}

					@Override
					public void onPBOCOnlineDataProcess(
							OnlineDataProcessResult result) {
						// mainedittext.setText("emv��������ɹ�"
						// + ByteArrayUtils.byteArray2HexString(result
						// .getICCardData()));
					}
				});
	}

	// ֹͣ PBOC��������
	public void stopProcess() {
		reader.PBOCStop(new PBOCStopListener() {

			@Override
			public void onError(int errCode, String errDesc) {
				// mainedittext.setText("����ʧ��" + errDesc);
			}

			@Override
			public void onPBOCStopSuccess() {
				// s mainedittext.setText("emv���׽���");
			}
		});
	}

	// ��ȡ�ŵ�����
	public void readTrack() {
		reader.getTrackDataPlain(new GetTrackDataPlainListener() {

			@Override
			public void onError(int errCode, String errDesc) {
				// mainedittext.setText("��ȡ�ŵ�ʧ��" + errDesc);
			}

			@Override
			public void onGetTrackDataPlainSucc(String track1, String track2,
					String track3) {
				// mainedittext.setText("��ȡ�ŵ����ĳɹ�:�ŵ�1��" + track1 + "�ŵ�2��"
				// + track2 + "�ŵ�3" + track3);
			}
		});
	}

	// ��ȡ����mac
	public void calcuMac() {
		String macData = "85582245666161A7";
		reader.calculateMac(ByteArrayUtils.toBCDDataBytes(macData),
				new CalcMacListener() {
					@Override
					public void onError(int errCode, String errDesc) {
						// mainedittext.setText("����macʧ��" + errDesc);
					}

					@Override
					public void onCalcMacSucc(byte[] mac) {
						// mainedittext.setText("����mac���"+
						// ByteArrayUtils.byteArray2HexString(mac));
					}
				});
	}

	//ȡ��track
	public void cancelTrack() {
		reader.cancleTrade();
	}

	// ��ȡPANPlain
	public void getPan() {
		reader.getPANPlain(new GetPANListener() {

			@Override
			public void onError(int arg0, String arg1) {
				// mainedittext.setText("��ȡ����ʧ��");
			}

			@Override
			public void onGetPANSucc(String arg0) {
				// mainedittext.setText("���ţ�"+arg0);
			}
		});
	}
}
