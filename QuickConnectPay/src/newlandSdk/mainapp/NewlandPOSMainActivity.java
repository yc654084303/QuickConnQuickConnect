package newlandSdk.mainapp;

import java.util.List;

import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.R;

import newlandSdk.adapter.OptionListAdapter;
import newlandSdk.common.Const.MessageTag;
import newlandSdk.common.ListViewBtnKey;
import newlandSdk.listener.OperaListener;
import newlandSdk.resources.AudioImpl;
import newlandSdk.resources.BluetoothImpl;
import newlandSdk.resources.K21Impl;
import newlandSdk.resources.UsbImpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;



public class NewlandPOSMainActivity extends BaseActivity  {
	private Boolean processing = false;
	private String deviceInteraction = "", newstring;
	private ExpandableListView expandableList;
	private OptionListAdapter adapter;
	private TextView mTextView;
	private Button btnConnect, btnDisconnect, btnClear;
	private BluetoothImpl bluetoothTools;
	private AudioImpl audioTools;
	private K21Impl k21Tools;
	private UsbImpl usbTools;
	private NewlandPOSMainActivity mainActivity;
	private int FLAG = -1;
	private OperaListener operaListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newland_activity);

		mainActivity = this;
		bluetoothTools = new BluetoothImpl(mainActivity);
		audioTools = new AudioImpl(mainActivity);
		k21Tools = new K21Impl(mainActivity);
		usbTools = new UsbImpl(mainActivity);
		processingUnLock();
		initView();
	}
	@Override
	public String appendInteractiveInfoAndShow(final String string, final int messageTag) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (messageTag) {
				case MessageTag.NORMAL:
					newstring = "<font color='black'>" + string + "</font>";
					break;
				case MessageTag.ERROR:
					newstring = "<font color='red'>" + string + "</font>";
					break;
				case MessageTag.TIP:
					newstring = "<font color='blue'>" + string + "</font>";
					break;
				case MessageTag.DATA:
					String arr[] = string.split(":");
					newstring = "<font color='green'>" + arr[0] + "</font>" + ":" + arr[1];
					break;
				default:
					break;
				}
				deviceInteraction = newstring + "<br>" + deviceInteraction;
				mTextView.setText(Html.fromHtml(deviceInteraction));
			}
		});
		return string;
	}

	private void initView() {

		mTextView = (TextView) findViewById(R.id.test_info);
		btnConnect = (Button) findViewById(R.id.connect);
		btnDisconnect = (Button) findViewById(R.id.disconnect);
		btnDisconnect.setEnabled(false);
		btnClear = (Button) findViewById(R.id.clear);
		btnConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final Builder builder = new android.app.AlertDialog.Builder(NewlandPOSMainActivity.this);
				builder.setTitle("请选择设备的连接方式:");
				builder.setSingleChoiceItems(new String[] { "audio", "bluetooth", "im81Connector" , "usbConnector"}, 0, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: {
							new Thread(new Runnable() {

								@Override
								public void run() {
									FLAG = 0;
									audioTools.initController();
									operaListener = new OperaListener(audioTools, mainActivity);
									expandableList.setOnChildClickListener(operaListener);
								}
							}).start();
							break;
						}
						case 1: {
							new Thread(new Runnable() {

								@Override
								public void run() {
									FLAG = 1;
									bluetoothTools.startDiscovery();
									operaListener = new OperaListener(bluetoothTools, mainActivity);
									expandableList.setOnChildClickListener(operaListener);
								}
							}).start();
							break;
						}
						case 2: {
							new Thread(new Runnable() {

								@Override
								public void run() {
									FLAG = 2;
									k21Tools.initController();
									operaListener = new OperaListener(k21Tools, mainActivity);
									expandableList.setOnChildClickListener(operaListener);
								}
							}).start();

							break;
						}
						case 3: {
							new Thread(new Runnable() {

								@Override
								public void run() {
									FLAG = 3;
									usbTools.initController();
									operaListener = new OperaListener(usbTools, mainActivity);
									expandableList.setOnChildClickListener(operaListener);
								}
							}).start();

							break;
						}
						default: {
							appendInteractiveInfoAndShow("不支持的连接方式!", MessageTag.ERROR);
						}
						}
					}
				});
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Dialog dialog = builder.create();
						dialog.setCancelable(false);
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();
					}
				});
			}
		});
		btnDisconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (FLAG == 0) {
					audioTools.disconnect();
				} else if (FLAG == 1) {
					bluetoothTools.disconnect();
				} else if (FLAG == 2) {
					k21Tools.disconnect();
				} else if (FLAG == 3) {
					usbTools.disconnect();
				}
			}
		});

		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mTextView.setText("");
				deviceInteraction = "";
			}
		});

		expandableList = (ExpandableListView) findViewById(R.id.option_list);
		expandableList.setIndicatorBounds(0, 55);
		adapter = new OptionListAdapter(this, OptionListAdapter.PaddingLeft >> 1);
		List<OptionListAdapter.TreeNode> treeNode = adapter.GetTreeNode();
		for (int i = 0; i < ListViewBtnKey.LIST_VIEW_KEY.length; i++) {
			OptionListAdapter.TreeNode node = new OptionListAdapter.TreeNode();
			node.parent = ListViewBtnKey.LIST_VIEW_KEY[i];
			for (int j = 0; j < ListViewBtnKey.LIST_VIEW_CHILD_KEY[i].length; j++) {
				node.childs.add(ListViewBtnKey.LIST_VIEW_CHILD_KEY[i][j]);
			}
			treeNode.add(node);
		}

		adapter.UpdateTreeNode(treeNode);
		expandableList.setAdapter(adapter);
		expandableList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4) {
				appendInteractiveInfoAndShow("设备未初始化", MessageTag.TIP);
				return false;
			}
		});

	}

	/**
	 * 设置成处理中状态
	 * 
	 * @since ver1.0
	 */
	public void btnStateToProcessing() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(false);
				processing = true;
			}
		});
	}

	/**
	 * 设置成等待初始化结束状态
	 * 
	 * @since ver1.0
	 */
	public void btnStateToWaitingInitFinished() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(false);
				processing = true;
			}
		});
	}

	/**
	 * 设置成初始化结束状态
	 * 
	 * @since ver1.0
	 */
	public void btnStateInitFinished() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(true);
				processing = false;
			}
		});

	}

	/**
	 * 设置成设备销毁状态
	 * 
	 * @since ver1.0
	 */
	public void btnStateDestroyed() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnConnect.setEnabled(true);
				btnDisconnect.setEnabled(false);
				processing = true;
			}
		});

	}

	public void btnStateToWaitingConn() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(false);
			}
		});

	}

	public void btnStateConnectFinished() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(true);
			}
		});

	}

	public void btnStateDisconnected() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnConnect.setEnabled(true);
				btnDisconnect.setEnabled(false);
			}
		});

	}

	/**
	 * 显示消息
	 * */
	public void showToast(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(NewlandPOSMainActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public Button getBtnConnect() {
		return btnConnect;
	}

	public Boolean getProcessing() {
		return processing;
	}

	public void setProcessing(Boolean processing) {
		this.processing = processing;
	}

	public void processingLock() {
		SharedPreferences setting = getSharedPreferences("setting", 0);
		SharedPreferences.Editor editor = setting.edit();
		editor.putBoolean("PBOC_LOCK", true);
		editor.commit();
	}

	public boolean processingisLocked() {
		SharedPreferences setting = getSharedPreferences("setting", 0);
		if (setting.getBoolean("PBOC_LOCK", true)) {
			return true;
		} else {
			return false;
		}
	}

//	public void processingUnLock() {
//		SharedPreferences setting = getSharedPreferences("setting", 0);
//		SharedPreferences.Editor editor = setting.edit();
//		editor.putBoolean("PBOC_LOCK", false);
//		editor.commit();
//	}

	public void doPinInputShower(final boolean isNext) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (isNext) {
					deviceInteraction = "*" + deviceInteraction;
					mTextView.setText(Html.fromHtml(deviceInteraction));
				} else {
					deviceInteraction = deviceInteraction.substring(1, deviceInteraction.length());
					mTextView.setText(Html.fromHtml(deviceInteraction));
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (FLAG == 0) {
			audioTools.disconnect();
		} else if (FLAG == 1) {
			unregisterReceiver(bluetoothTools.getDiscoveryReciever());
			bluetoothTools.disconnect();
		} else if (FLAG == 2) {
			k21Tools.disconnect();
		}

	}
}
