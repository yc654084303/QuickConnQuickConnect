package newlandSdk.resources;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.controller.DeviceController;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import android.app.Activity;
import android.util.Log;


import com.newland.mtypex.k21.K21ConnParams;

public class K21Impl extends AbstractDevice {

	private static final String K21_DRIVER_NAME="com.newland.me.K21Driver";
	NewlandPOSMainActivity mainActivity;
	private DeviceController controller = null;
	private String TAG = "svsv";

	public K21Impl(NewlandPOSMainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public void initController() {
		mainActivity.btnStateToWaitingConn();
		Me3xDeviceDriver me3xDeviceController = new Me3xDeviceDriver(mainActivity);
		controller = me3xDeviceController.initMe3xDeviceController(K21_DRIVER_NAME,new K21ConnParams());
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
			mainActivity.appendInteractiveInfoAndShow("K21链接断开异常..."+e1.getMessage(),MessageTag.ERROR);
		}

	}

}
