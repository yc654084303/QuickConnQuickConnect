package com.huichuang.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.huichuang.log.L;
import com.huihuang.utils.NetUtils;
import com.newland.mtype.module.common.iccard.ICCardSlot;
import com.newland.mtype.module.common.iccard.ICCardSlotState;
import com.newland.mtype.module.common.swiper.SwipResult;

import android.app.Application;
import android.os.Environment;


/**
 * 
 * @author yangcan
 *
 */
public class KLApplication extends Application {
	private String NLDPath, NLDPath_Image; // APP更新文件夹路径
	private String NLDPathString = null; // APP更新文件路径
	private SwipResult SwipResult = null; // 刷卡结果
	private int Ic_pinInput_flag = 0; // 当前做出是否是IC外部输入密码模式，0否，1是
	private int Open_card_reader_flag = 0; // 当前操作是否是开启读卡器操作，0否，1是
	private BigDecimal amt;
	private byte[] result;
    private Map<ICCardSlot,ICCardSlotState> map=new HashMap<ICCardSlot, ICCardSlotState>(); //卡槽状态
	public static KLApplication klApplication(){
		
		return new KLApplication();
		
	}
	@Override
	public void onCreate() {
		
		L.isDebug=true;
		
		
		
		if (ifSDCardExit()) {
			NLDPath = "/sdcard/data/data/com.example.mainapp/update";
			NLDPath_Image = "/sdcard/data/data/com.example.mainapp/image";
		} else {
			NLDPath = "/data/data/com.example.mainapp/update";
			NLDPath_Image = "/data/data/com.example.mainapp/image";
		}

		File updateFile = new File(NLDPath);
		if (!updateFile.exists()) {
			updateFile.mkdirs();
			System.out.println("======= =====================");
		}
		File imageFile = new File(NLDPath_Image);
		if (!imageFile.exists()) {
			imageFile.mkdirs();
		} 
		try {
			InputStream is = this.getResources().getAssets().open("test_two.jpg");
			NLDPath_Image = NLDPath_Image + "/test_two.jpg";
			File file = new File(NLDPath_Image);
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			while (is.read(temp) != -1) {
				os.write(temp);
			}
			System.out.println("图片创建成功,位置"+NLDPath_Image);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		super.onCreate();
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public boolean ifSDCardExit() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public String getNLDPath() {
		return NLDPath;
	}

	public void setNLDPath(String nLDPath) {
		NLDPath = nLDPath;
	}

	public String getNLDPathString() {
		return NLDPathString;
	}

	public void setNLDPathString(String nLDPathString) {
		NLDPathString = nLDPathString;
	}

	public SwipResult getSwipResult() {
		return SwipResult;
	}

	public void setSwipResult(SwipResult swipResult) {
		SwipResult = swipResult;
	}

	public int getIc_pinInput_flag() {
		return Ic_pinInput_flag;
	}

	public void setIc_pinInput_flag(int ic_pinInput_flag) {
		Ic_pinInput_flag = ic_pinInput_flag;
	}

	public int getOpen_card_reader_flag() {
		return Open_card_reader_flag;
	}

	public void setOpen_card_reader_flag(int open_card_reader_flag) {
		Open_card_reader_flag = open_card_reader_flag;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public byte[] getResult() {
		return result;
	}

	public void setResult(byte[] result) {
		this.result = result;
	}

	public Map<ICCardSlot, ICCardSlotState> getMap() {
		return map;
	}

	public void setMap(Map<ICCardSlot, ICCardSlotState> map) {
		this.map = map;
	}

}
