package newlandSdk.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 自定义蓝牙设备类，封装地址和名称
 * 
 * @author Administrator
 * 
 */
public class BluetoothDeviceContext {
	public String name = "";
	public String address = "";
	private List<BluetoothDeviceContext> discoveredDevices = new ArrayList<BluetoothDeviceContext>();

	public BluetoothDeviceContext(String name, String address) {
		this.name = name;
		this.address = address;
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
}
