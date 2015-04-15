package newlandSdk.common;

import com.newland.mtype.module.common.pin.WorkingKeyType;

public class Const {

	/**
	 * 主密钥索引
	 * <p>
	 * 
	 * 各索引若相同则表示使用同一组主密钥索引
	 * 
	 * @author lance
	 * 
	 */
	public static class MKIndexConst {

		/**
		 * 主密钥索引
		 */
		public static final int DEFAULT_MK_INDEX = 1;
	}

	/**
	 * 工作密钥类型:{@link WorkingKeyType#PININPUT}
	 * 
	 * @author lance
	 */
	public static class PinWKIndexConst {
		/**
		 * 默认PIN加密工作密钥索引
		 */
		public static final int DEFAULT_PIN_WK_INDEX = 2;
	}

	/**
	 * 工作密钥类型:{@link WorkingKeyType#MAC}
	 * 
	 * @author lance
	 */
	public static class MacWKIndexConst {
		/**
		 * 默认MAC加密工作密钥索引
		 */
		public static final int DEFAULT_MAC_WK_INDEX = 3;
	}

	/**
	 * 工作密钥类型:{@link WorkingKeyType#DATAENCRYPT}
	 * 
	 * @author lance
	 */
	public static class DataEncryptWKIndexConst {
		/**
		 * 默认磁道加密工作密钥索引
		 */
		public static final int DEFAULT_TRACK_WK_INDEX = 4;

		public static final int DEFAULT_MUTUALAUTH_WK_INDEX = 5;

	}

	/**
	 * 设备参数存放相关规格
	 * 
	 * @author lance
	 * 
	 */
	public static class DeviceParamsPattern {

		/**
		 * 默认存放编码集<p>
		 */
		public static final String DEFAULT_STORENCODING = "utf-8";

		/**
		 * 日期格式化规格<p>
		 */
		public static final String DEFAULT_DATEPATTERN = "yyyyMMddHHmmss";
	}

	/**
	 * 设备参数<tt>tag</tt>
	 * 
	 * @author lance
	 * 
	 */
	public static class DeviceParamsTag {

		/**
		 * 商户号存放<tt>tag</tt>
		 */
		public static final int MRCH_NO = 0xFF9F11;
		
		/**
		 * 终端号存放<tt>tag</tt>
		 */
		public static final int TRMNL_NO = 0xFF9F12;
		/**
		 * 工作密钥存放<tt>tag</tt>
		 */
		public static final int WK_UPDATEDATE = 0xFF9F13;
		/**
		 * pos标示存放<tt>tag</tt>
		 */
		public static final int DEVICE_TYPE = 0xFF9F14;
		/**
		 * 终端号存放<tt>tag</tt>
		 */
		public static final int MRCH_NAME = 0xFF9F15;

	}

	public static class MessageTag {
		/**
		 * 正常消息<tt>tag</tt>
		 */
		public static final int NORMAL = 0;
		/**
		 * 错误消息<tt>tag</tt>
		 */
		public static final int ERROR = 1;
		/**
		 * 提示消息<tt>tag</tt>
		 */
		public static final int TIP = 2;
		/**
		 * 数据<tt>tag</tt>
		 */
		public static final int DATA = 3;
	}

	public static class CardType{
		/**
		 * 磁条卡
		 */
		public static final int COMMON=0;
		/**
		 * IC卡
		 */
		public static final int ICCARD=1;
	}
	
	public static class DialogView{
		/**
		 * Mac计算对话框
		 */
		public static final int MAC_CACL_DIALOG=0;
		/**
		 * 非接卡密钥对话框
		 */
		public static final int NC_CARD_KEY_DIALOG=1;
		/**
		 * IC卡对话框
		 */
		public static final int IC_CARD_ICCardSlot_DIALOG=2;
	}
}
