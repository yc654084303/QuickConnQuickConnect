package newlandSdk.resources;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.controller.DeviceController;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import android.app.Activity;
import android.util.Log;


import com.newland.mtypex.audioport.AudioPortV100ConnParams;

public class AudioImpl extends AbstractDevice {
	private static final String ME3X_DRIVER_NAME = "com.newland.me.ME3xDriver";
	NewlandPOSMainActivity mainActivity;
	private DeviceController controller = null;
	private String TAG = "cvcv";

	public AudioImpl(NewlandPOSMainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public void initController() {
		mainActivity.btnStateToWaitingConn();
		Me3xDeviceDriver me3xDeviceController = new Me3xDeviceDriver(mainActivity);
		controller = me3xDeviceController.initMe3xDeviceController(ME3X_DRIVER_NAME,new AudioPortV100ConnParams());
		mainActivity.appendInteractiveInfoAndShow("控制器已初始化!",MessageTag.NORMAL);
		connectDevice();
	}

	@Override
	public void disconnect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mainActivity.btnStateDisconnected();
				try {
					if (controller != null) {
						controller.disConnect();
						controller = null;
					}
					mainActivity.appendInteractiveInfoAndShow("控制器断开成功",MessageTag.NORMAL);
				} catch (Exception e) {
					Log.e(TAG, "deleteCSwiper failed!", e);
				}
			}
		}).start();

	}

	@Override
	public boolean isControllerAlive() {
		if (controller != null) {
			return true;
		}
		return false;
	}

	@Override
	public DeviceController getController() {
		return controller;
	}

	@Override
	public void connectDevice() {
		mainActivity.appendInteractiveInfoAndShow("设备连接中...",MessageTag.NORMAL);
		try {
			controller.connect();
			mainActivity.appendInteractiveInfoAndShow("设备连接成功...",MessageTag.NORMAL);
		} catch (Exception e1) {
			e1.printStackTrace();
			mainActivity.appendInteractiveInfoAndShow("音频链接断开异常...",MessageTag.ERROR);
		}
		
	}


}
