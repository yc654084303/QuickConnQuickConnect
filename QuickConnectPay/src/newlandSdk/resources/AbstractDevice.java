package newlandSdk.resources;

import newlandSdk.controller.DeviceController;

public abstract class AbstractDevice {
	public abstract void initController();
	public abstract void disconnect();
	public abstract boolean isControllerAlive() ;
	public abstract DeviceController getController();
	public abstract void connectDevice();
}
