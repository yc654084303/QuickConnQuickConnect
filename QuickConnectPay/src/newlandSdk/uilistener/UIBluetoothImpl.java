package newlandSdk.uilistener;

import android.app.Activity;
import newlandSdk.resources.AbstractDevice;
import newlandSdk.resources.BluetoothDeviceContext;
import newlandSdk.resources.BluetoothImpl;
import newlandSdk.resources.Me3xDeviceDriver;




import java.util.ArrayList;
import java.util.List;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.controller.DeviceController;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager.BadTokenException;

import com.huichuang.base.BaseActivity;
import com.newland.mtypex.bluetooth.BlueToothV100ConnParams;

/**
 * 蓝牙操作
 * 
 * @author evil
 * 
 */
public class UIBluetoothImpl extends AbstractDevice {
	private static final String ME3X_DRIVER_NAME = "com.newland.me.ME3xDriver";
	int REQUEST_ENABLE = 0;
	private String deviceToConnect;
	private DeviceController controller = null;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private BaseActivity mContext;
	private String TAG = "KL";
	private List<BluetoothDeviceContext> discoveredDevices = new ArrayList<BluetoothDeviceContext>();

	private final BroadcastReceiver discoveryReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (ifAddressExist(device.getAddress())) {
					return;
				}

				BluetoothDeviceContext tempDevice = new BluetoothDeviceContext(device.getName() == null ? device.getAddress() : device.getName(), device.getAddress());
				getDiscoveredDevices().add(tempDevice);
				mContext.appendInteractiveInfoAndShow("搜索到新设备 : " + tempDevice.name, MessageTag.DATA);
			}

		}
	};
	private Handler handler;
	public UIBluetoothImpl(Activity mContext, Handler handler) {
		super();
		this.mContext = (BaseActivity) mContext;
		this.handler=handler;
		/**
		 * 注册一个蓝牙发现监听器
		 */

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		mContext.registerReceiver(discoveryReciever, filter);
	}
	public UIBluetoothImpl(NewlandPOSMainActivity mContext) {
		super();
		this.mContext = mContext;
		/**
		 * 注册一个蓝牙发现监听器
		 */

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		mContext.registerReceiver(discoveryReciever, filter);
	}

	/**
	 * 检查是蓝牙地址是否已经存在
	 * 
	 * @return
	 */
	public boolean ifAddressExist(String addr) {
		for (BluetoothDeviceContext devcie : discoveredDevices) {
			if (addr.equals(devcie.address))
				return true;
		}
		return false;
	}

	public List<BluetoothDeviceContext> getDiscoveredDevices() {
		return discoveredDevices;
	}

	/**
	 * 启动蓝牙搜索
	 */
	public void startDiscovery() {
		if (bluetoothAdapter.isEnabled()) {
			mContext.btnStateToWaitingConn();
			if (discoveredDevices != null) {
				discoveredDevices.clear();
			}
			bluetoothAdapter.startDiscovery();

			mContext.appendInteractiveInfoAndShow("正在搜索...", MessageTag.NORMAL);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					} finally {
						mContext.appendInteractiveInfoAndShow("停止搜索...", MessageTag.NORMAL);
						bluetoothAdapter.cancelDiscovery();
						mContext.btnStateDisconnected();

					}
					selectBtAddrToInit();
				}
			}).start();
		} else {
			mContext.appendInteractiveInfoAndShow("蓝牙未打开", MessageTag.TIP);
			Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mContext.startActivityForResult(enabler, REQUEST_ENABLE);
		}

	}

	// 弹出已配对蓝牙对话框,点击链接相应设备
	public void selectBtAddrToInit() {

		int i = 0;
		String[] bluetoothName = new String[discoveredDevices.size()];
		for (BluetoothDeviceContext device : discoveredDevices) {
			bluetoothName[i++] = device.name;
		}

		final Builder builder = new android.app.AlertDialog.Builder(mContext);
		builder.setTitle("请选取已配对设备连接:");
		builder.setSingleChoiceItems(bluetoothName, 0, new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				deviceToConnect = discoveredDevices.get(which).address;
				new Thread(new Runnable() {
					@Override
					public void run() {
						initController();
					}
				}).start();
			}
		});
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					builder.create().show();
				} catch (BadTokenException e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}

	public void connectDevice() {
		mContext.appendInteractiveInfoAndShow("设备连接中...", MessageTag.NORMAL);
		try {
			controller.connect();
			mContext.appendInteractiveInfoAndShow("设备连接成功...", MessageTag.NORMAL);
		} catch (Exception e1) {
			e1.printStackTrace();
			mContext.appendInteractiveInfoAndShow("蓝牙链接异常,请检查设备或重新连接...", MessageTag.ERROR);
		}

	}

	@Override
	public void disconnect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mContext.btnStateDisconnected();
				try {
					if (controller != null) {
						controller.disConnect();
						controller = null;
					}
					mContext.appendInteractiveInfoAndShow("控制器断开成功", MessageTag.NORMAL);
				} catch (Exception e) {
					Log.e(TAG, "deleteCSwiper failed!", e);
				}
			}
		}).start();
	}

	@Override
	public DeviceController getController() {
		return controller;
	}

	@Override
	public boolean isControllerAlive() {

		if (controller != null) {
			return true;

		}
		return false;
	}

	@Override
	public void initController() {
		mContext.btnStateToWaitingConn();
		Me3xDeviceDriver me3xDeviceController = new Me3xDeviceDriver(mContext);
		controller = me3xDeviceController.initMe3xDeviceController(ME3X_DRIVER_NAME,new BlueToothV100ConnParams(deviceToConnect));
		mContext.appendInteractiveInfoAndShow("控制器已初始化!", MessageTag.NORMAL);
		//进行读卡操作
		handler.sendEmptyMessage(0);
		
	}

	public BroadcastReceiver getDiscoveryReciever() {
		return discoveryReciever;
	}

}

