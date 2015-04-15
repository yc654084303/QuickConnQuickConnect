package newlandSdk.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import newlandSdk.listener.TransferListener;

import android.content.Context;
import android.graphics.Bitmap;

import com.newland.me.DeviceManager.DeviceConnState;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.DeviceInfo;
import com.newland.mtype.UpdateAppListener;
import com.newland.mtype.conn.DeviceConnParams;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CardRule;
import com.newland.mtype.module.common.cardreader.OpenCardType;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.iccard.ICCardSlot;
import com.newland.mtype.module.common.iccard.ICCardSlotState;
import com.newland.mtype.module.common.iccard.ICCardType;
import com.newland.mtype.module.common.pin.AccountInputType;
import com.newland.mtype.module.common.pin.EncryptType;
import com.newland.mtype.module.common.pin.KSNKeyType;
import com.newland.mtype.module.common.pin.KSNLoadResult;
import com.newland.mtype.module.common.pin.KekUsingType;
import com.newland.mtype.module.common.pin.LoadPKResultCode;
import com.newland.mtype.module.common.pin.LoadPKType;
import com.newland.mtype.module.common.pin.MacAlgorithm;
import com.newland.mtype.module.common.pin.PinInputEvent;
import com.newland.mtype.module.common.pin.PinInputResult;
import com.newland.mtype.module.common.pin.PinManageType;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.pin.WorkingKeyType;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.newland.mtype.module.common.rfcard.RFCardType;
import com.newland.mtype.module.common.rfcard.RFKeyMode;
import com.newland.mtype.module.common.rfcard.RFResult;
import com.newland.mtype.module.common.storage.StorageResult;
import com.newland.mtype.module.common.swiper.Account;
import com.newland.mtype.module.common.swiper.SwipResult;

/**
 * 设备控制器
 * <p>
 * 
 * @author lance
 * @since ver1.0
 */
public interface DeviceController {

	/**
	 * 初始化设备控制器
	 * 
	 * @since ver1.0
	 * @param context
	 * @param params
	 */
	public void init(Context context, String driverName, DeviceConnParams params, DeviceEventListener<ConnectionCloseEvent> listener);

	/**
	 * 销毁连接控制器，释放相关资源
	 * <p>
	 * 
	 * @since ver1.0
	 */
	public void destroy();

	/**
	 * 连接设备
	 * <p>
	 * 
	 * @since ver1.0
	 * @throws Exception
	 */
	public void connect() throws Exception;

	/**
	 * 连接中断
	 * <p>
	 * 
	 * @since ver1.0
	 */
	public void disConnect();

	/**
	 * 判断连接是否正常
	 * <p>
	 * 
	 * @since ver1.0
	 */
	public void isConnected();

	/**
	 * 更新工作密钥
	 * <p>
	 * 
	 * @since ver1.0
	 * @param workingKeyType
	 *            工作密钥类型
	 * @param encryData
	 *            加密数据
	 * @param encryData
	 *            校验数据
	 * @return
	 */
	public void updateWorkingKey(WorkingKeyType workingKeyType, byte[] encryData, byte[] checkValue);

	/**
	 * 发起一个刷卡流程
	 * <p>
	 * 
	 * @since ver1.0
	 * @param msg
	 *            在设备界面显示信息
	 *            <p>
	 * @param timeout
	 *            刷卡超时时间
	 * @param timeUnit
	 *            超时时间单位
	 * @return
	 */
	public SwipResult swipCard(String msg, long timeout, TimeUnit timeUnit);

	/**
	 * 发起一个明文刷卡流程
	 * <p>
	 * 
	 * @since ver1.0
	 * @param msg
	 *            在设备界面显示信息
	 *            <p>
	 * @param timeout
	 *            刷卡超时时间
	 * @param timeUnit
	 *            超时时间单位
	 * @return
	 */
	public SwipResult swipCardForPlain(String msg, long timeout, TimeUnit timeUnit);

	/**
	 * 发起一个pin输入过程
	 * <p>
	 * 
	 * @since ver1.0
	 * @param acctHash
	 *            刷卡时返回的<tt>acctHash</tt>
	 * @param inputMaxLen
	 *            最大密码输入长度 [0,12]
	 * @param msg
	 *            界面显示数据
	 * @return
	 */
	public PinInputEvent startPininput(String acctHash, int inputMaxLen, String msg);
	
	/**
	 * 无键盘密码输入
	 * 
	 */
	public PinInputResult startPinInputWithoutKeyboard(String acctSymbol, byte[] pin);

	/**
	 * 显示消息
	 * 
	 * @since ver1.0
	 * @param msg
	 */
	public void showMessage(String msg);

	/**
	 * 在规定的时间内显示消息
	 * 
	 * @since ver1.0
	 * @param msg
	 * @param showtime
	 */
	public void showMessageWithinTime(String msg, int showtime);

	/**
	 * pos清屏
	 * 
	 * @since ver1.0
	 */
	public void clearScreen();

	/**
	 * 获得设备信息
	 * 
	 * @since ver1.0
	 * @return
	 */
	public DeviceInfo getDeviceInfo();

	/**
	 * 撤消当前操作
	 */
	public void reset();

	/**
	 * 获得当前设备连接状态
	 * 
	 * @since ver1.0
	 * @return
	 */
	public DeviceConnState getDeviceConnState();

	/**
	 * 设置参数
	 * 
	 * @since ver1.0
	 * @param tag
	 *            设置的标签数值
	 * @param value
	 *            设置的数值
	 */
	public void setParam(int tag, byte[] value);

	/**
	 * 获得对应的参数
	 * 
	 * @since ver1.0
	 * @param tag
	 *            获得的参数数值
	 * @return
	 */
	public byte[] getParam(int tag);

	/**
	 * 开启密码输入
	 * 
	 * @param acctHash
	 *            账号hash
	 * @param inputMaxLen
	 *            最大输入长度
	 * @param msg
	 *            显示信息
	 * @param listener
	 *            响应监听
	 */
	public void startPininput(String acctHash, int inputMaxLen, String msg, DeviceEventListener<PinInputEvent> listener);

	/**
	 * 获取设备连接参数
	 * 
	 * @return
	 */
	public DeviceConnParams getDeviceConnParams();

	/**
	 * 打印图片
	 * 
	 * @param position
	 *            偏移量
	 * @param bitmap
	 *            位图
	 */
	public void printBitMap(int position, Bitmap bitmap);

	/**
	 * 打印字符
	 * 
	 * @param data
	 *            待打印数据
	 */
	public PrinterResult printString(String data);
	/**
	 * 打印脚本
	 * 
	 * @param data 脚本数据
	 */
	public PrinterResult printScript(String data);
	/**
	 * 加密接口(ECB方式)
	 * 
	 * @param wk工作密钥
	 * @param input待加密数据
	 * @return
	 */
	public byte[] encrypt(WorkingKey wk, EncryptType encryptType, byte[] input);

	/**
	 * 解密接口
	 * 
	 * @param wk
	 * @param encryptType
	 * @param input
	 * @return
	 */
	public byte[] decrypt(WorkingKey wk, EncryptType encryptType, byte[] input);

	/**
	 * 计算mac方法
	 * 
	 * @param input
	 * @return
	 */
	public byte[] caculateMac(MacAlgorithm macAlgorithm, byte[] input);

	/**
	 * 装载主秘钥
	 * 
	 * @param kekUsingType主密钥传输使用的方式
	 * @param mkIndex主秘钥索引
	 * @param keyData主密钥
	 * @param checkValue校验值
	 * @param transportKeyIndex传输密钥索引
	 *            (仅在KekUsingType.MAIN_KEY)时使用
	 */
	public void loadMainKey(KekUsingType kekUsingType, int mkIndex, byte[] keyData, byte[] checkValue);

	public String getCurrentDriverVersion();

	/**
	 * 获取emv模块
	 * 
	 * @param filePath
	 * @return
	 */
	public EmvModule getEmvModule();

	/**
	 * 发起一个pin读取过程,并返回pin信息
	 * <p>
	 * 该方法需要在刷卡完成后进行,因为pin输入过程需要依赖上次刷卡的账号信息
	 * <p>
	 * 通过{@link SwipResult#getAccount()}可以获得刷卡时的账户信息，需要将
	 * {@link Account#getIdentityHash()}传入供设备校验。
	 * 
	 * @param acctHash上次刷卡时的账户hash
	 * @param inputMaxLen允许密码输入最大值
	 * @param isEnterEnabled密码输入时
	 *            ,是否通过回车返回，否则则输满即返回。
	 * @param msg输入信息
	 * @param timeout超时时间
	 * @return 若客户设备撤消,则返回nullF
	 */
	public PinInputEvent startPininput(AccountInputType acctInputType, String acctHash, int inputMaxLen, boolean isEnterEnabled, String msg, long timeout) throws InterruptedException;

	/**
	 * 更新固件
	 * 
	 * @param filePath固件路径
	 * @param listener 固件更新监听
	 * @throws Exception
	 */
	public void updateFirmware(String filePath,UpdateAppListener listener);

	/**
	 * 获取打印机状态
	 * 
	 * @return
	 */
	public PrinterStatus getPrinterStatus();
	/**
	 * 开启读卡器
	 * @param context
	 * @param cardReaders 读卡类型
	 * @param cardRule 读卡规则
	 * @param msg 屏显信息
	 * @param timeout 超时时间
	 * @param timeunit 超时单位
	 * @param transferListener 读卡监听
	 * @throws Exception
	 */
	public void OpenCardReader(Context context, OpenCardType[] cardReaders,CardRule cardRule, final String msg, final long timeout, final TimeUnit timeunit, TransferListener transferListener) throws Exception;

	/**
	 * 获取磁道信息
	 * 
	 * @param flag
	 *            标志为当前是IC卡还是磁条卡
	 * @return
	 * @throws InterruptedException
	 */
	public SwipResult getTrackText(int flag) throws InterruptedException;

	// ===================================IC卡操作模块============================================

	/**
	 * 发起一个IC卡通信请求
	 * 
	 * @param slot
	 *            卡槽类型
	 * @param cardType
	 * @param req
	 *            请求
	 * @param timeout
	 *            请求超时时间
	 * @param timeunit
	 *            请求超时时间单位
	 * @return 返回调用后的IC卡响应
	 */
	public byte[] call(ICCardSlot slot, ICCardType cardType, byte[] req, long timeout, TimeUnit timeunit);

	/**
	 * 获取当前IC卡状态
	 * 
	 * @return 当前各个IC卡槽状态
	 */
	public Map<ICCardSlot, ICCardSlotState> checkSlotsState();

	/**
	 * 卡槽下电
	 * 
	 * @param slot
	 *            卡槽类型
	 * @param cardType
	 */
	public void powerOff(ICCardSlot slot, ICCardType cardType);

	/**
	 * 卡槽上电
	 * 
	 * @param slot
	 *            卡槽类型
	 * @param cardType
	 * @return
	 */
	public byte[] powerOn(ICCardSlot slot, ICCardType cardType);

	// ===================================非接卡操作模块============================================
	/**
	 * 寻卡上电
	 * 
	 * @param qPCardType
	 *            卡类型，可为空
	 * @param timeout
	 *            超时时间
	 * @return
	 */
	public RFResult powerOn(RFCardType qPCardType, int timeout);

	/**
	 * 非接CPU卡通讯
	 * 
	 * @param req
	 *            APDU数据
	 * @param timeout
	 *            超时时间
	 * @param timeunit
	 *            时间单位
	 * @return
	 */
	public byte[] call(byte[] req, long timeout, TimeUnit timeunit);

	/**
	 * 下电
	 * 
	 * @param timeout
	 *            超时时间
	 */
	public void powerOff(int timeout);

	/**
	 * 使用外部的密钥进行认证
	 * 
	 * @param qpKeyMode
	 *            KEY模式
	 * @param SNR
	 * @param blockNo
	 *            要认证的块号
	 * @param key
	 *            外部密钥
	 */
	public void authenticateByExtendKey(RFKeyMode qpKeyMode, byte[] SNR, int blockNo, byte[] key);

	/**
	 * 使用加载的密钥进行认证
	 * 
	 * @param qpKeyMode
	 *            KEY模式
	 * @param SNR
	 * @param blockNo
	 *            要认证的块号
	 */
	public void authenticateByLoadedKey(RFKeyMode qpKeyMode, byte[] SNR, int blockNo);

	/**
	 * 存储密钥
	 * 
	 * @param qpKeyMode
	 *            KEY模式
	 * @param keyIndex
	 *            密钥存储区(接口芯片中)
	 * @param key
	 *            密钥
	 */
	public void storeKey(RFKeyMode qpKeyMode, int keyIndex, byte[] key);

	/**
	 * 加载密钥
	 * 
	 * @param qpKeyMode
	 *            KEY模式
	 * @param keyIndex
	 *            密钥存储区(接口芯片中)
	 */
	public void loadKey(RFKeyMode qpKeyMode, int keyIndex);

	/**
	 * 读块数据
	 * 
	 * @param blockNo
	 *            块号
	 * @return
	 */
	public byte[] readDataBlock(int blockNo);

	/**
	 * 写块数据
	 * 
	 * @param blockNo
	 *            块号
	 * @param data
	 *            块数据
	 */
	public void writeDataBlock(int blockNo, byte[] data);

	/**
	 * 增量操作
	 * 
	 * @param blockNo
	 *            块号
	 * @param data
	 *            值
	 */
	public void incrementOperation(int blockNo, byte[] data);

	/**
	 * 减量操作
	 * 
	 * @param blockNo
	 * @param data
	 */
	public void decrementOperation(int blockNo, byte[] data);

	/**
	 * 获取存储记录
	 * 
	 * @param recordName
	 *            记录名
	 * @param recordNo
	 *            记录号
	 * @param checkParams1
	 *            检索字段1
	 * @param checkParams2
	 *            检索字段2
	 * @return byte[]
	 */
	public byte[] fetchRecord(String recordName, int recordNo, String checkParams1, String checkParams2);

	/**
	 * 更新存储记录数
	 * 
	 * @param recordName
	 *            记录名
	 * @param recordNo
	 *            记录号
	 * @param checkParams1
	 *            检索字段1
	 * @param checkParams2
	 *            检索字段2
	 * @param content
	 *            记录内容
	 * @return StorageResult
	 */
	public StorageResult updateRecord(String recordName, int recordNo, String checkParams1, String checkParams2, byte[] content);

	/**
	 * 获取存储记录数
	 * 
	 * @param recordName
	 *            记录名
	 * @return StorageResult
	 */
	public StorageResult addRecord(String recordName, byte[] content);

	/**
	 * 获取存储记录数
	 * 
	 * @param recordName
	 *            记录名
	 * @return int
	 */
	public int fetchRecordCount(String recordName);

	/**
	 * 初始化存储记录
	 * 
	 * @param recordName
	 *            记录名
	 * @param recordLength
	 *            每条记录长度
	 * @param params1Offset
	 *            检索字段 1 在记录中的偏移
	 * @param params1Length
	 *            检索字段 1 的长度
	 * @param params2Offset
	 *            检索字段 2 在记录中的偏移
	 * @param params2Length
	 *            检索字段 2 的长度
	 * @return boolean
	 */
	public boolean initializeRecord(String recordName, int recordLength, int params1Offset, int params1Length, int params2Offset, int params2Length);
	/**
	 * 打开读卡器，并使用一个设备监听器异步监听打开事件（支持非接寻卡、降级pos阻止提示)
	 * @param openCardType 打开的读卡类型
	 * @param timeout 超时时间(单位:秒)
	 * @param screenShow 屏显
	 * @param opencardrule 刷卡规则
	 * @param effectivetimes 非接寻卡有效次数
	 * @param intervaltimes 寻卡间隔时间
	 * @param listener
	 */
	public void startTransfer(Context context, OpenCardType[] cardReaders, String msg, BigDecimal amt, long timeout, TimeUnit timeunit, CardRule opencardrule,TransferListener transferListener) throws Exception;

	/**
	 * 装载DUKPT的KNS和初始密钥
	 */
	public KSNLoadResult ksnLoad(KSNKeyType keytype,int KSNIndex,byte[] ksn,byte[] defaultKeyData,int mainKeyIndex,byte[] checkValue);
	
	/**
	 * 装载公钥
	 */
	public LoadPKResultCode LoadPublicKey(LoadPKType keytype,int pkIndex,String pkLength,byte[] pkModule,byte[] pkExponent,byte[] index,byte[] mac);
}