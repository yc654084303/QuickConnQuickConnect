package newlandSdk.resources;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.controller.DeviceController;
import newlandSdk.impl.DeviceControllerImpl;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import android.app.Activity;
import android.os.Handler;


import com.huichuang.base.BaseActivity;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.conn.DeviceConnParams;
import com.newland.mtype.event.DeviceEventListener;

public class Me3xDeviceDriver {

	private BaseActivity mainActivity;
	private DeviceController controller;

//	public Me3xDeviceDriver(NewlandPOSMainActivity mainActivity) {
//		this.mainActivity = mainActivity;
//	}
	public Me3xDeviceDriver(Activity mainActivity) {
		this.mainActivity = (BaseActivity) mainActivity;
	}
	public DeviceController initMe3xDeviceController(String driverPath,DeviceConnParams params) {
		controller = DeviceControllerImpl.getInstance(driverPath);
		controller.init(mainActivity, driverPath, params, new DeviceEventListener<ConnectionCloseEvent>() {
			@Override
			public void onEvent(ConnectionCloseEvent event, Handler handler) {
				if (event.isSuccess()) {
					mainActivity.appendInteractiveInfoAndShow("设备被客户主动断开！",MessageTag.TIP);
				}
				if (event.isFailed()) {
					mainActivity.appendInteractiveInfoAndShow("设备链接异常断开！" + event.getException().getMessage(),MessageTag.ERROR);
				}
			}

			@Override
			public Handler getUIHandler() {
				return null;
			}
		});
		mainActivity.appendInteractiveInfoAndShow("驱动版本号：" + controller.getCurrentDriverVersion(),MessageTag.NORMAL);
//		mainActivity.btnStateConnectFinished();
		return controller;

	}

}
