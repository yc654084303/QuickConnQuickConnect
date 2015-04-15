package newlandSdk.common;
public class ListViewBtnKey {

	public static String[] LIST_VIEW_KEY = { "POS初始化", "磁条卡/IC卡交易", "固件", "设备信息", "打印", "参数", "加密/解密", "IC卡模块", "非接卡模块" ,"存储模块"};
	public static String[][] LIST_VIEW_CHILD_KEY = { { "磁条卡初始化", "装载主密钥", "装载工作密钥", "IC卡初始化", "增加公钥", "设置交易属性", "增加AID", "删除公钥", "删除AID", "KSN装载", "装载公钥" }, 
		{ "消费", "撤销当前操作", "开启读卡器", "获取磁道信息", "输密码" }, 
		{ "固件选择", "固件更新" }, 
		{ "获取设备信息", "MAC计算" }, 
		{ "打印数据", "打印图片" , "打印脚本" }, 
		{ "设置参数", "获取参数" }, 
		{ "加密", "解密" }, 
		{ "检查IC卡状态", "卡槽上电", "IC卡通信请求", "卡槽下电" },
		{ "寻卡上电", "非接CPU卡通讯", "下电", "存储密钥", "加载密钥", "外部密钥认证","加载密钥认证",  "读块数据", "写块数据", "增量操作", "减量操作" },
		{ "初始化存储记录","获取存储记录数","增加存储记录","更新存储记录","获取存储记录"} };

	/**
	 * 第一级功能菜单
	 * 
	 * @author evil
	 * 
	 */
	public static class ListViewBtnParentKey {
		public static final int POS_INIT = 0;
		public static final int TRAN_PROC = 1;
		public static final int FIRMWARE = 2;
		public static final int SETTING = 3;
		public static final int PRINT = 4;
		public static final int PARAMETER = 5;
		public static final int ENCRYPT = 6;
		public static final int ICCardModule = 7;
		public static final int QPCardModule = 8;
		public static final int FileIoModule = 9;
	}

	/**
	 * 第二级功能菜单
	 * 
	 * @author evil
	 * 
	 */
	public static class ListViewBtnChildKey {
		public static final int MAGNETIC_CARD_INIT = 0;
		public static final int LOAD_MAIN_KEY = 1;
		public static final int LOAD_WORKKEY = 2;
		public static final int IC_CARD_INIT = 3;
		public static final int ADD_PUB_KEY = 4;
		public static final int SET_TERMINAL_PROP = 5;
		public static final int ADD_AID = 6;
		public static final int DEL_PUB_KEY = 7;
		public static final int DEL_AID = 8;
		public static final int LOAD_KSN = 9;
		public static final int LOAD_PK = 10;

		public static final int START_PBOC = 0;
		public static final int CLEAR_SCREEN = 1;
		public static final int OPEN_CARDREADER = 2;
		public static final int GET_MAGNETIC_INFO = 3;
		public static final int INPUT_PWD = 4;

		public static final int SELECT_FIRMWARE = 0;
		public static final int UPDATE_FIRMWARE = 1;

		public static final int FETCH_DEVICE_INFO = 0;
		public static final int CACL_MAC = 1;

		public static final int PRINT_STR = 0;
		public static final int PRINT_BITMAP = 1;
		public static final int PRINT_SCRIPT = 2;

		public static final int SET_PARAMETER = 0;
		public static final int GET_PARAMETER = 1;

		public static final int ENCRYPTION = 0;
		public static final int DECRYPT = 1;

		public static final int IC_STATE = 0;
		public static final int POWER_0N = 1;
		public static final int CALL = 2;
		public static final int POWER_OFF = 3;

		public static final int QC_SEARCH_POWER_ON = 0;
		public static final int QC_CALL = 1;
		public static final int QC_POWER_OFF = 2;
		public static final int QC_STORE_KEY = 3;
		public static final int QC_LOAD_KEY = 4;
		public static final int QC_AUTHENTICATE_BY_EXTEND_KEY = 5;
		public static final int QC_AUTHENTICATE_BY_LOADED_KEY = 6;
		public static final int QC_READ_DATA_BLOCK = 7;
		public static final int QC_WRITE_DATA_BLOCK = 8;
		public static final int QC_DECREMENT_OPERATION = 9;
		public static final int QC_INCREMENT_OPERATION = 10;
		
		public static final int INITIALIZERECORD = 0;
		public static final int FETCHRECORDCOUNT = 1;
		public static final int ADDRECORD = 2;
		public static final int UPDATERECORD = 3;
		public static final int FETCHRECORD = 4;
	}
}
