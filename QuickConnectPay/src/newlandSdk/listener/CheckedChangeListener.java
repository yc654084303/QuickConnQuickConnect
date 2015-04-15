package newlandSdk.listener;


import newlandSdk.common.Const;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.huichuang.quickconnectpay.R;
import com.newland.mtype.module.common.iccard.ICCardSlot;
import com.newland.mtype.module.common.pin.MacAlgorithm;
import com.newland.mtype.module.common.rfcard.RFKeyMode;

/**
 * 多个RadioGroup操作监听
 * 
 * @author evil
 * 
 */
public class CheckedChangeListener implements OnCheckedChangeListener {

	private Boolean changeGroup = false;
	private RadioButton radio_MAC_ECB, radio_MAC_X99, radio_MAC_X919, radio_MAC_9606, radio_KEYA_0X60, radio_KEYA_0X00, radio_KEYA_0X61, radio_KEYA_0X01, radio_IC1, radio_IC2, radio_IC3, radio_SAM1, radio_SAM2, radio_SAM3;
	private RadioGroup radioGroup_encrypt_type1, radioGroup_encrypt_type2;
	private MacAlgorithm macAlgorithm = MacAlgorithm.MAC_ECB;
	private RFKeyMode rfKeyMode =RFKeyMode.KEYA_0X60;
	private ICCardSlot iCCardSlot = ICCardSlot.IC1;
	private int flag;

	public CheckedChangeListener(View view, int flag) {
		this.flag = flag;
		if (flag == Const.DialogView.MAC_CACL_DIALOG) {
			radio_MAC_ECB = (RadioButton) view.findViewById(R.id.radio_MAC_ECB);
			radio_MAC_X99 = (RadioButton) view.findViewById(R.id.radio_MAC_X99);
			radio_MAC_X919 = (RadioButton) view.findViewById(R.id.radio_MAC_X919);
			radio_MAC_9606 = (RadioButton) view.findViewById(R.id.radio_MAC_9606);
		} else if (flag == Const.DialogView.NC_CARD_KEY_DIALOG) {
			radio_KEYA_0X60 = (RadioButton) view.findViewById(R.id.radio_KEYA_0X60);
			radio_KEYA_0X00 = (RadioButton) view.findViewById(R.id.radio_KEYA_0X00);
			radio_KEYA_0X61 = (RadioButton) view.findViewById(R.id.radio_KEYA_0X61);
			radio_KEYA_0X01 = (RadioButton) view.findViewById(R.id.radio_KEYA_0X01);
		} else if (flag == Const.DialogView.IC_CARD_ICCardSlot_DIALOG) {
			radio_IC1 = (RadioButton) view.findViewById(R.id.radio_IC1);
			radio_IC2 = (RadioButton) view.findViewById(R.id.radio_IC2);
			radio_IC3 = (RadioButton) view.findViewById(R.id.radio_IC3);
			radio_SAM1 = (RadioButton) view.findViewById(R.id.radio_SAM1);
			radio_SAM2 = (RadioButton) view.findViewById(R.id.radio_SAM2);
			radio_SAM3 = (RadioButton) view.findViewById(R.id.radio_SAM3);
		}
		radioGroup_encrypt_type1 = (RadioGroup) view.findViewById(R.id.radioGroup_encrypt_type1);
		radioGroup_encrypt_type2 = (RadioGroup) view.findViewById(R.id.radioGroup_encrypt_type2);
		radioGroup_encrypt_type1.setOnCheckedChangeListener(this);
		radioGroup_encrypt_type2.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group != null && checkedId > -1 && changeGroup == false) {
			if (flag == Const.DialogView.MAC_CACL_DIALOG) {
				if (group == radioGroup_encrypt_type1) {
					changeGroup = true;
					radioGroup_encrypt_type2.clearCheck();
					if (checkedId == radio_MAC_ECB.getId()) {
						macAlgorithm = MacAlgorithm.MAC_ECB;
					} else if (checkedId == radio_MAC_X99.getId()) {
						macAlgorithm = MacAlgorithm.MAC_X99;
					}
					changeGroup = false;
				} else if (group == radioGroup_encrypt_type2) {
					changeGroup = true;
					radioGroup_encrypt_type1.clearCheck();
					if (checkedId == radio_MAC_X919.getId()) {
						macAlgorithm = MacAlgorithm.MAC_X919;
					} else if (checkedId == radio_MAC_9606.getId()) {
						macAlgorithm = MacAlgorithm.MAC_9606;
					}
					changeGroup = false;
				}
			} else if (flag == Const.DialogView.NC_CARD_KEY_DIALOG) {
				if (group == radioGroup_encrypt_type1) {
					changeGroup = true;
					radioGroup_encrypt_type2.clearCheck();
					if (checkedId == radio_KEYA_0X60.getId()) {
						rfKeyMode = RFKeyMode.KEYA_0X60;
					} else if (checkedId == radio_KEYA_0X00.getId()) {
						rfKeyMode = RFKeyMode.KEYA_0X00;
					}
					changeGroup = false;
				} else if (group == radioGroup_encrypt_type2) {
					changeGroup = true;
					radioGroup_encrypt_type1.clearCheck();
					if (checkedId == radio_KEYA_0X61.getId()) {
						rfKeyMode = RFKeyMode.KEYB_0X61;
					} else if (checkedId == radio_KEYA_0X01.getId()) {
						rfKeyMode = RFKeyMode.KEYB_0X01;
					}
					changeGroup = false;
				}
			} else if (flag == Const.DialogView.IC_CARD_ICCardSlot_DIALOG) {
				if (group == radioGroup_encrypt_type1) {
					changeGroup = true;
					radioGroup_encrypt_type2.clearCheck();
					if (checkedId == radio_IC1.getId()) {
						iCCardSlot = ICCardSlot.IC1;
					} else if (checkedId == radio_IC2.getId()) {
						iCCardSlot = ICCardSlot.IC2;
					} else if (checkedId == radio_IC3.getId()) {
						iCCardSlot = ICCardSlot.IC3;
					}
					changeGroup = false;
				} else if (group == radioGroup_encrypt_type2) {
					changeGroup = true;
					radioGroup_encrypt_type1.clearCheck();
					if (checkedId == radio_SAM1.getId()) {
						iCCardSlot = ICCardSlot.SAM1;
					} else if (checkedId == radio_SAM2.getId()) {
						iCCardSlot = ICCardSlot.SAM2;
					} else if (checkedId == radio_SAM3.getId()) {
						iCCardSlot = ICCardSlot.SAM3;
					}
					changeGroup = false;
				}
			}
		}

	}

	public MacAlgorithm getMacAlgorithm() {
		return macAlgorithm;
	}

	public void setMacAlgorithm(MacAlgorithm macAlgorithm) {
		this.macAlgorithm = macAlgorithm;
	}


	public RFKeyMode getRfKeyMode() {
		return rfKeyMode;
	}

	public void setRfKeyMode(RFKeyMode rfKeyMode) {
		this.rfKeyMode = rfKeyMode;
	}

	public ICCardSlot getiCCardSlot() {
		return iCCardSlot;
	}

	public void setiCCardSlot(ICCardSlot iCCardSlot) {
		this.iCCardSlot = iCCardSlot;
	}

}
