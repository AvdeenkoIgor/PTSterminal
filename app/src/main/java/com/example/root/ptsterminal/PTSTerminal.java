package com.example.root.ptsterminal;

/**
 * Created by root on 25.02.16.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import android.support.v7.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.IBinder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import adapter.ImageAdapter;
import adapter.SelectProductAdapter;

public class PTSTerminal extends AppCompatActivity {

    // Создание объектов для запуска сервиса PTSMasterService
    static Intent intentService;
    boolean bound = false;
    ServiceConnection sConn;
    PTSMasterService myService;

    static ArrayList<OperatorItem> mListCardOperator;
    static ArrayList<CardHolderItem> mListCardClient;
    static ArrayList<ChequesItem> mListCheque;
    static ArrayList<HolderRequestItem> mListHolderRequest;
    // static ArrayList<ProductAssortmentItem> mListProductAssortment;
    static ArrayList<PurseItem> mListPurse;
    static ArrayList<BasketItem> mListBasket;
    // static ArrayList<ArrayList_ProductAssortment> mListProductAssortment;
    // static ArrayList<ArrayList_Dispenser> mListDispenser;
    // static ArrayList<ArrayList_Order> mListOrder;
    Integer[] queueCommands = null;
    Integer[] mQueueCommandsAll = {
            PTSMasterService.FUNC_PUMPSTATE,
            // PTSMasterService.FUNC_PAYFORMLIST,
            PTSMasterService.FUNC_BASKETLIST,
            PTSMasterService.FUNC_BASKETGET
            // PTSMasterService.FUNC_VERSION
    };
    Integer[] mQueueCommandsPump = {
            PTSMasterService.FUNC_PUMPSTATE
    };
    Integer[] mQueueCommandsBasket = {
            PTSMasterService.FUNC_BASKETLIST,
            PTSMasterService.FUNC_BASKETGET
    };
    int currentCommand = 0;

    static ArrayList_PTSMaster ptsMaster;
    static ArrayList_PTSMaster ptsMasterStatic;
    public static GridView gView;// = (GridView) findViewById(R.id.viewPump);

    final String LOG_TAG = "PTSTerminal_Log";

    Handler mHandler;
    private Timer timer;
    TimerTask task;
    public static PTSMaster ptsAdapter;
    public static SelectProductAdapter productAdapter;

    private Handler myHandler = new Handler();
    BroadcastReceiver br;
    Typeface tf;
    //	static GridView gridView1;
    static ImageView gridView1;
    //	public ImageView serverStatus;
    public TextView serverStatus;
    public TextView userName;
    public static TextView titlePage;

    public static int state;
    private int error;
    private String errorMessage;
    private int currentConfig;
    private EditText amountProduct;
    public static String BC_PTSTERMINAL = "PTSTerminal";// Broadcast name
    // private int ptsMasterFunction;

    public static final int PAGE_AUTHORIZATION = 101;
    public static final int PAGE_MAIN_MENU = 102;
    public static final int PTSMASTER_TASKCONNECT = 103;
    public static final int PTSMASTER_TASKTRANSMITT = 104;
    public static final int PTSMASTER_GETCLERKLIST = 105;
    public static final int PTSMASTER_GETDISPENSERLIST = 106;
    public static final int PTSMASTER_PLUGET = 107;
    public static final int PTSMASTER_STOREGET = 108;
    public static final int PTSMASTER_PAYFORMLIST = 109;
    public static final int PTSMASTER_SIGNALRECEIVED = 110;

    private static final int DIALOG_YES_NO_MESSAGE = 1;
    private static final int DIALOG_EXIT_MAIN_MENU = 2;
    private static final int DIALOG_OK_ERROR = 3;
    private static final int DIALOG_CONFIG_ENTRY = 4;
    private static final int DIALOG_RECONNECT_EXIT = 5;

    private String mutex_startTask = "NotActive";
    Message msgAnswer;
    Bundle b;
    static Handler hActivity;
    static Handler hDispenserControlActivity;

    private Intent intent;
    private Bundle extras;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//		tf = Typeface.createFromAsset(getAssets(), "times.ttf");
//		TextView tv = (TextView) findViewById(R.id.idTextLogoTiT);
//		tv.setTypeface(tf);
        tf = Typeface.createFromAsset(getAssets(), "verdana.ttf");
        TextView tvPTSMAster = (TextView) findViewById(R.id.idTextLogoPTSMaster);
        tvPTSMAster.setTypeface(tf);
        tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
        titlePage = (TextView) findViewById(R.id.idTitlePage);
        titlePage.setTypeface(tf);
        titlePage.setText("");
        tf = Typeface.createFromAsset(getAssets(), "verdana.ttf");
        TextView tvUser = (TextView) findViewById(R.id.idTextUser);
        tvUser.setTypeface(tf);
        tf = Typeface.createFromAsset(getAssets(), "times.ttf");
        serverStatus = (TextView) findViewById(R.id.server_status);
        serverStatus.setTypeface(tf);
        userName = (TextView) findViewById(R.id.idNameUser);
        userName.setTypeface(tf);


        mListCardOperator = new ArrayList<OperatorItem>();
        mListCardClient = new ArrayList<CardHolderItem>();
        mListCheque = new ArrayList<ChequesItem>();
        mListHolderRequest = new ArrayList<HolderRequestItem>();
        // mListProductAssortment = new ArrayList<ProductAssortmentItem>();
        mListPurse = new ArrayList<PurseItem>();
        mListBasket = new ArrayList<BasketItem>();
        // mListProductAssortment = new
        // ArrayList<ArrayList_ProductAssortment>();
        // mListDispenser = new ArrayList<ArrayList_Dispenser>();
        // mListOrder = new ArrayList<ArrayList_Order>();

        ptsMaster = new ArrayList_PTSMaster();

        ptsMaster.mListDispenser.add(new ArrayList_Dispenser());
        // ptsMaster.mListDispenser.get(0).setDispenseStatus("Idle");

        ptsAdapter = new PTSMaster();
        ptsAdapter.mAdapter = new ImageAdapter(this);
        ptsAdapter.mAdapter1 = new ImageAdapter(this, "Enable");

        productAdapter = new SelectProductAdapter(this);
        Typeface fontFaceDigital = Typeface.createFromAsset(getAssets(), "DigitalBold.ttf");
        Typeface fontFaceHelvetica = Typeface.createFromAsset(getAssets(), "Helvetica_Bold.ttf");
        PTSTerminal.productAdapter.setTextViewFace(fontFaceDigital);
        // ptsAdapter.mAdapter1.disactiveCell(0);

        gView = (GridView) findViewById(R.id.viewPump);
        gView.setNumColumns(1);
        gView.setAdapter(PTSTerminal.ptsAdapter.mAdapter1);

//		gridView1 = (ImageView) findViewById(R.id.gridViewInTitle);
//		gridView1.setNumColumns(1);
//		gridView1.set setAdapter(PTSTerminal.ptsAdapter.mAdapter1);

//		gridView1 = (GridView) findViewById(R.id.gridViewInTitle);
//		gridView1.setNumColumns(1);
//		gridView1.setAdapter(PTSTerminal.ptsAdapter.mAdapter1);

//		View viewPump = (View) findViewById(R.id.viewPump);
//		String srtS = "S";
//		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		viewPump = inflater.inflate(R.layout.mobile, null);
//
//		TextView textView = (TextView) viewPump.findViewById(R.id.grid_item_label);
//		ImageView imageView = (ImageView) viewPump.findViewById(R.id.grid_item_image);
//
//		textView.setTextColor(Color.parseColor("#FFFFFF"));
//		textView.setText(srtS);
//		imageView.setImageResource(R.drawable.key_pump_ordered);

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // Handler brHandler;
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                // Log.d(LOG_TAG, "BROADCAST RECEIVE");
                // String nameTask =
                // intent.getStringExtra(PTSMasterService.PARAM_RECEIVERNAME);
                // if (!nameTask.equals(BC_PTSTERMINAL)) {
                // return;
                // }
                // int function =
                // intent.getIntExtra(PTSMasterService.PARAM_FUNCTION, 0);
                int task = intent.getIntExtra(PTSMasterService.PARAM_TASK, 0);
                Log.i(LOG_TAG, "task = " + task);

                if (task == PTSMASTER_TASKCONNECT) {
                    int status = intent.getIntExtra(PTSMasterService.PARAM_SERVICESTATUS, 0);
                    int result = intent.getIntExtra(PTSMasterService.PARAM_TASKRESULT, 0);
                    String owner = intent.getStringExtra(PTSMasterService.PARAM_OWNER);
                    switch (status) {
                        case PTSMasterService.STATUS_IN_WORK:
                            switch (result) {
                                case PTSMasterService.RESULT_SUCCESS:
                                    Log.i(LOG_TAG, "TASK CONNECTION SUCCESS");
                                    Log.i(LOG_TAG, "owner: " + owner);
                            }
                            break;
                        default:
                            Log.e(LOG_TAG, "BAD SERVICE STATUS");
                            break;
                    }
                }
                if (task == PTSMASTER_SIGNALRECEIVED) {
                    byte answerData[] = intent.getByteArrayExtra(PTSMasterService.PARAM_RESULT_BUFF);
//					Log.i("SIGNAL", "<-- 0x" + myLib.byteArrayToHexString(answerData, answerData.length));
                    funcPTSMaster.parsePackage(PTSMasterService.FUNC_EMULSIGNAL, answerData, answerData.length, 4, ptsMaster);
                    Log.i("###","================================================================");
                    Log.i("###", "MsgId = " + ptsMaster.mListSignal.getMsgId() + "; Id = " + ptsMaster.mListSignal.getId());
                    switch (Integer.valueOf(ptsMaster.mListSignal.getMsgId())) {
                        case funcPTSMaster.BASKET_CHANGE_ID:
                            Log.i("###","BASKET_CHANGE_ID");
                            startTask(1, 0);
                            break;
                        case funcPTSMaster.PRODUCT_CHANGE_ID:
                            Log.i("###","PRODUCT_CHANGE_ID");
                            break;
                        case funcPTSMaster.VOLUME_CHANGE_ID:
                            Log.i("###","VOLUME_CHANGE_ID");
                            break;
                        case funcPTSMaster.DEVICE_STATUS_CHANGE_ID:
                            Log.i("###","DEVICE_STATUS_CHANGE_ID");
                            break;
                        case funcPTSMaster.SESSION_CHANGE_ID:
                            Log.i("###","SESSION_CHANGE_ID");
                            break;
                        case funcPTSMaster.POS_LOCK_ID:
                            Log.i("###","POS_LOCK_ID");
                            break;
                        case funcPTSMaster.POS_UNLOCK_ID:
                            Log.i("###","POS_UNLOCK_ID");
                            break;
                        case funcPTSMaster.PUMP_STATE_CHANGE_ID:
                            Log.i("###","PUMP_STATE_CHANGE_ID");
                            startTask(1, 0);
                            break;
                        case funcPTSMaster.PRICE_CHANGE_ID:
                            Log.i("###","PRICE_CHANGE_ID");
                            break;
                        case funcPTSMaster.SERVICE_MODE_CHANGE_ID:
                            Log.i("###","SERVICE_MODE_CHANGE_ID");
                            break;
                        default:
                            break;
                    }
                    Log.i("###","================================================================");
//					 startTask(0);
                }
                if (task == PTSMASTER_TASKTRANSMITT) {
                    int status = intent.getIntExtra(PTSMasterService.PARAM_SERVICESTATUS, 0);
                    int result = intent.getIntExtra(PTSMasterService.PARAM_TASKRESULT, 0);
                    int function = intent.getIntExtra(PTSMasterService.PARAM_FUNCTION, 0);
                    String owner = intent.getStringExtra(PTSMasterService.PARAM_OWNER);
                    Log.d(LOG_TAG, "function: " + function);
                    switch (status) {
                        // ===========================================================================================
                        // =================================================
                        // PTSMasterService.STATUS_START
                        // ===========================================================================================
                        case PTSMasterService.STATUS_START:
                            Log.i("PTSTerminalStatus", "TASK_API_OPEN start...");
                            break;
                        // ===========================================================================================
                        // =================================================
                        // PTSMasterService.STATUS_FINISH
                        // ===========================================================================================
                        case PTSMasterService.STATUS_FINISH:
                            Log.i("PTSTerminalStatus", "TASK_API_OPEN finish...");
                            break;
                        // ===========================================================================================
                        // =================================================
                        // PTSMasterService.STATUS_HANDLER
                        // ===========================================================================================
                        case PTSMasterService.STATUS_IN_WORK:
                            Log.i("PTSTerminalStatus", "STATUS_IN_WORK...");
                            switch (result) {
                                case PTSMasterService.RESULT_SUCCESS:
                                    Log.i("PTSTerminalResult", "RESULT_SUCCESS");
                                    serverStatus.setTextColor(Color.parseColor("#14FE14"));
                                    byte answerData[] = intent.getByteArrayExtra(PTSMasterService.PARAM_RESULT_BUFF);
//							 Log.i("answerData", "<-- 0x" + myLib.byteArrayToHexString(answerData, answerData.length));

                                    String error = funcPTSMaster.parsePackage(function, answerData,	answerData.length, 0, ptsMaster);
                                    if (error.equals("")) {
//								 Log.i("###",
//								 "================================================================");
//								 Log.i("###",
//								 "Index   Name   Permission   Password");
//								 Log.i("###",
//								 "================================================================");
//								 for (int i = 0; i < ptsMaster.mListClerks.size();
//								 i++) {
//								 Log.i("###", " " +
//								 ptsMaster.mListClerks.get(i).getIndex() + "  " +
//								 ptsMaster.mListClerks.get(i).getName() + "  " +
//								 ptsMaster.mListClerks.get(i).getPermissions() +
//								 "  " +
//								 ptsMaster.mListClerks.get(i).getPassword());
//								 }
//								 Log.i("###",
//								 "================================================================");
//								 "================================================================");
//								ptsMasterStatic = ptsMaster;
//								Log.d(LOG_TAG, "simply:");
//								Log.d(LOG_TAG, "ptsMasterStatic = " + ptsMasterStatic + "ptsMaster = " + ptsMaster);

                                        switch (function) {
                                            case PTSMasterService.FUNC_BASKETGET:
                                                if (ptsMasterStatic != null) {
                                                    Log.d(LOG_TAG, "ptsMasterStatic = " + ptsMasterStatic);
                                                    ptsMasterStatic = null;
                                                }
                                                ptsMasterStatic = ptsMaster.getCopy();
                                                break;
                                        }

//								if (ptsMaster.mListProductAssortment.size() > 0) {
//									ptsMaster.getSize();
//									Log.v("&&&", "ptsMaster.mListProductAssortment.size = " + ptsMaster.mListProductAssortment.size());

//									ptsMaster.mListProductAssortment.get(0).setRest("5.05");
////									ptsMasterStatic = new ArrayList_PTSMaster();
//
//									try {
//										while (ptsMasterStaticMutex != true) {
//											Thread.sleep(1000);
//											Log.i(LOG_TAG, "wait mutex ... ");
//										}
//										ptsMasterStaticMutex = false;
//										Log.d(LOG_TAG, "ptsMasterStaticMutex = false");
//									}
//									catch (Exception e) {
//										Log.e("Exception", "e=" + e);
//									}
//									ptsMasterStatic = ptsMaster.clone_();
//									Log.d(LOG_TAG, "ptsMasterStaticMutex = true");
//									ptsMasterStaticMutex = true;
//
////									Log.v("&&&", "ptsMaster.mListProductAssortment.size = " + ptsMaster.mListProductAssortment.size());
////									ptsMasterStatic.mListProductAssortment.addAll(ptsMaster.mListProductAssortment);
//									Log.d(LOG_TAG, "1) ptsMasterStatic.mListProductAssortment.rest = " + ptsMasterStatic.mListProductAssortment.get(0).getRest());
//									ptsMaster.mListProductAssortment.get(0).setRest("12.34");
//									Log.d(LOG_TAG, "2) ptsMasterStatic.mListProductAssortment.rest = " + ptsMasterStatic.mListProductAssortment.get(0).getRest());
//								}
                                        if ((owner.equals("Client")) && (hActivity != null)) {
                                            // -------------------------------- Ответ
                                            msgAnswer = hActivity.obtainMessage();
                                            b = new Bundle();
                                            b.putInt("ptsMasterAnswer",funcPTSMaster.STATUS_PACKAGE_OK);
                                            b.putInt("function", function);
                                            msgAnswer.setData(b);
                                            hActivity.sendMessage(msgAnswer);
                                            // Log.i("Ответ",
                                            // myLib.byteArrayToHexString(res.packetData,
                                            // res.packetData.length));
                                        }
//								 "================================================================");
                                        if (hDispenserControlActivity != null) {
//									try {
//										while (ptsMasterStaticMutex != true) {
//											Log.i(LOG_TAG, "wait mutex ... ");
//											Thread.sleep(1000);
//										}
//										ptsMasterStaticMutex = false;
//										Log.d(LOG_TAG, "ptsMasterStaticMutex = ЗАХВАТ");
//									}
//									catch (Exception e) {
//										Log.e("Exception", "e=" + e);
//									}
//									if (ptsMasterStatic != null) {
//										Log.d(LOG_TAG, "ptsMasterStatic = " + ptsMasterStatic);
//										ptsMasterStatic = null;
//									}
//									ptsMasterStatic = ptsMaster.clone_();

//									Log.v(LOG_TAG, "mListBasket.size = " + ptsMasterStatic.mListBasket.size());
//									for (int i = 0; i < PTSTerminal.ptsMasterStatic.mListBasket.size(); i++) {
//										Log.d(LOG_TAG, "i = " + i);
//										Log.v(LOG_TAG, "basketItem.size = " + PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.size());
//									}
//									Log.d(LOG_TAG, "ptsMasterStaticMutex = ОСВОБОЖДЕНИЕ");
//									ptsMasterStaticMutex = true;

                                            if (function == PTSMasterService.FUNC_BASKETGET) {
                                                // -------------------------------- Ответ
                                                msgAnswer = hDispenserControlActivity.obtainMessage();
                                                b = new Bundle();
                                                b.putInt("signal", funcPTSMaster.STATUS_PACKAGE_OK);
                                                msgAnswer.setData(b);
                                                hDispenserControlActivity.sendMessage(msgAnswer);
                                            }
                                        }
                                    }
                                    else {
                                        if ((owner.equals("Client")) && (hActivity != null)) {
                                            // -------------------------------- Ответ
                                            msgAnswer = hActivity.obtainMessage();
                                            b = new Bundle();
                                            b.putInt("ptsMasterAnswer",	funcPTSMaster.STATUS_PROTOCOL_ERROR);
                                            b.putInt("function", function);
                                            b.putString("errorMessage", error);
                                            // Log.i("Ответ",
                                            // myLib.byteArrayToHexString(res.packetData,
                                            // res.packetData.length));
                                            msgAnswer.setData(b);
                                            hActivity.sendMessage(msgAnswer);
                                        }
                                    }
                                    break;
                                case PTSMasterService.RESULT_DAMAGED:
                                    Toast.makeText(getApplicationContext(), "DAMAGED",
                                            Toast.LENGTH_SHORT).show();
                                    Log.e("PTSTerminalResult", "RESULT_DAMAGED");
                                    // ptsAdapter.mAdapter.dispNOZZLEUP(2);
                                    // ptsMaster.mListDispenser.get(0).setdispenseOrder_status("Open");
                                    serverStatus.setTextColor(Color.parseColor("#EA152B"));
//							serverStatus
//									.setImageResource(R.drawable.server_offline);
                                    if ((owner.equals("Client")) && (hActivity != null)) {
                                        // -------------------------------- Ответ
                                        msgAnswer = hActivity.obtainMessage();
                                        b = new Bundle();
                                        b.putInt("ptsMasterAnswer",
                                                funcPTSMaster.STATUS_DAMAGED);
                                        b.putInt("function", function);
                                        // Log.i("Ответ",
                                        // myLib.byteArrayToHexString(res.packetData,
                                        // res.packetData.length));
                                        msgAnswer.setData(b);
                                        hActivity.sendMessage(msgAnswer);
                                    }
                                    break;
                                case PTSMasterService.RESULT_NULL:
                                    Log.e("PTSTerminalResult", "RESULT_NULL");
                                    if ((owner.equals("Client")) && (hActivity != null)) {
                                        // -------------------------------- Ответ
                                        msgAnswer = hActivity.obtainMessage();
                                        b = new Bundle();
                                        b.putInt("ptsMasterAnswer",
                                                funcPTSMaster.STATUS_PACKAGE_OK);
                                        b.putInt("function", function);
                                        // Log.i("Ответ",
                                        // myLib.byteArrayToHexString(res.packetData,
                                        // res.packetData.length));
                                        msgAnswer.setData(b);
                                        hActivity.sendMessage(msgAnswer);
                                    }
                                    break;
                                case PTSMasterService.RESULT_TIMEOUT:
                                    Log.e("PTSTerminalResult", "RESULT_TIMEOUT");
                                    Toast.makeText(getApplicationContext(),
                                            "BROADCAST_TIMEOUT", Toast.LENGTH_SHORT)
                                            .show();
                                    serverStatus.setTextColor(Color.parseColor("#EA152B"));
//							serverStatus
//									.setImageResource(R.drawable.server_offline);
                                    // ptsAdapter.mAdapter.dispIDLE(2);
                                    // ptsAdapter.mAdapter.openCell(5);
                                    // ptsMaster.mListDispenser.get(0).setdispenseOrder_status("Close");
                                    if ((owner.equals("Client")) && (hActivity != null)) {
                                        // -------------------------------- Ответ
                                        msgAnswer = hActivity.obtainMessage();
                                        b = new Bundle();
                                        b.putString("timer", "Timeout");
                                        b.putInt("function", function);
                                        // Log.i("Ответ",
                                        // myLib.byteArrayToHexString(res.packetData,
                                        // res.packetData.length));
                                        msgAnswer.setData(b);
                                        hActivity.sendMessage(msgAnswer);
                                    }
                                    break;
                                case PTSMasterService.RESULT_NOCONNECT:
                                    Log.e("PTSTerminalResult", "RESULT_NOCONNECT");
                                    Toast.makeText(getApplicationContext(),
                                            "BROADCAST_NOCONNECT", Toast.LENGTH_SHORT)
                                            .show();
                                    serverStatus.setTextColor(Color.parseColor("#EA152B"));
//							serverStatus
//									.setImageResource(R.drawable.server_offline);
                                    if ((owner.equals("Client")) && (hActivity != null)) {
                                        // -------------------------------- Ответ
                                        msgAnswer = hActivity.obtainMessage();
                                        b = new Bundle();
                                        b.putString("timer", "Timeout");
                                        b.putInt("function", function);
                                        // Log.i("Ответ",
                                        // myLib.byteArrayToHexString(res.packetData,
                                        // res.packetData.length));
                                        msgAnswer.setData(b);
                                        hActivity.sendMessage(msgAnswer);
                                    }
                                    break;
                                case PTSMasterService.RESULT_CANCEL:
                                    Log.e("PTSTerminalResult", "RESULT_CANCEL");
                                    Toast.makeText(getApplicationContext(),
                                            "BROADCAST_CANCEL", Toast.LENGTH_SHORT)
                                            .show();
                                    serverStatus.setTextColor(Color.parseColor("#EA152B"));
//							serverStatus
//									.setImageResource(R.drawable.server_offline);
                                    // ptsAdapter.mAdapter.dispIDLE(2);
                                    // ptsAdapter.mAdapter.openCell(5);
                                    // ptsMaster.mListDispenser.get(0).setdispenseOrder_status("Close");
                                    if ((owner.equals("Client")) && (hActivity != null)) {
                                        // -------------------------------- Ответ
                                        msgAnswer = hActivity.obtainMessage();
                                        b = new Bundle();
                                        b.putString("dialog", "cancel");
                                        b.putInt("function", function);
                                        // Log.i("Ответ",
                                        // myLib.byteArrayToHexString(res.packetData,
                                        // res.packetData.length));
                                        msgAnswer.setData(b);
                                        hActivity.sendMessage(msgAnswer);
                                    }
                                    break;
                            }
                            if ((ptsAdapter.mAdapter.getCount() > 0)
                                    && (ptsAdapter.mAdapter1.getCount() > 0)
                                    && (state != getResources().getInteger(R.integer.state_unauthorized))
                                    && (state != getResources().getInteger(R.integer.state_main_menu))) {
                                ptsAdapter.mAdapter1.set_ptsMaster(ptsMaster);
                                // ptsAdapter.mAdapter1.setStatus(0,
                                // PTSTerminal.ptsAdapter.mAdapter.getStatus(Integer.valueOf(PTSTerminal.ptsAdapter.mAdapter1.getNumber(0))-1));
                            }
//						if (productAdapter.getCount() > 0) {
//						Log.i("---", "================================================================");
//						Log.i("---", " BASKETLIST");
//						Log.i("---", "================================================================");
//						Log.i("---", "basket  itemCount  payCount  clerk  tag  flags  itemSum  paySum  billNum");
//						Log.i("---", "================================================================");
//						for (int i = 0; i < ptsMaster.mListBasket.size(); i++) {
//							Log.i("---", " " + 	ptsMaster.mListBasket.get(i).basket + "  " +
//												ptsMaster.mListBasket.get(i).itemCount + "  " +
//												ptsMaster.mListBasket.get(i).payCount + "  " +
//												ptsMaster.mListBasket.get(i).clerk + "  " +
//												ptsMaster.mListBasket.get(i).tag + "  " +
//												ptsMaster.mListBasket.get(i).flags + "  " +
//												ptsMaster.mListBasket.get(i).itemSum + "  " +
//												ptsMaster.mListBasket.get(i).paySum + "  " +
//												ptsMaster.mListBasket.get(i).billNum );
//							Log.i("---", "..........................................................................");
//							for (int n = 0; n < ptsMaster.mListBasket.get(i).basketItem.size(); n++) {
//								Log.i("---", " " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).basket + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).billNum + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).clerk + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).cost + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).discountType + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).flags + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).id + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).index + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).itemType + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).name + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).nozzle + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).orderVolume + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).payForm + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).person + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).price + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).product + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).pump + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).quantity + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).rate + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).store + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).sum + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).tag + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).time + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).utag + "  " +
//										ptsMaster.mListBasket.get(i).basketItem.get(n).volume );
//							}
//							Log.i("---", "-----------------------------------------------------------------");
//						}
//						Log.i("---", "================================================================");
                            ptsAdapter.mAdapter.set_ptsMaster(ptsMaster);
                            productAdapter.set_ptsMaster(ptsMaster);
//						}
//						if (owner.equals("Timer")) {
//							if (function == PTSMasterService.FUNC_BASKETGET) {
//								startTask(30000);
//							} else {
//								startTask(0);
//							}
//						}
                            break;
                    }
                    // case PTSMasterService.FUNC_VERSION:
                    // switch (status) {
                    // //
                    // ===========================================================================================
                    // // =================================================
                    // PTSMasterService.STATUS_START
                    // //
                    // ===========================================================================================
                    // case PTSMasterService.STATUS_START:
                    // Log.i("PTSTerminalStatus", "TASK_API_OPEN start...");
                    // break;
                    // //
                    // ===========================================================================================
                    // // =================================================
                    // PTSMasterService.STATUS_FINISH
                    // //
                    // ===========================================================================================
                    // case PTSMasterService.STATUS_FINISH:
                    // Log.i("PTSTerminalStatus", "TASK_API_OPEN finish...");
                    // break;
                    // //
                    // ===========================================================================================
                    // // =================================================
                    // PTSMasterService.STATUS_HANDLER
                    // //
                    // ===========================================================================================
                    // case PTSMasterService.STATUS_IN_WORK:
                    // Log.i("PTSTerminalStatus", "STATUS_IN_WORK...");
                    // switch (result) {
                    // case PTSMasterService.RESULT_SUCCESS:
                    // Log.i("PTSTerminalResult", "RESULT_SUCCESS");
                    // serverStatus.setImageResource(R.drawable.server_online);
                    // break;
                    // case PTSMasterService.RESULT_DAMAGED:
                    // Toast.makeText(getApplicationContext(), "DAMAGED",
                    // Toast.LENGTH_SHORT).show();
                    // Log.e("PTSTerminalResult", "RESULT_DAMAGED");
                    // ptsAdapter.mAdapter.dispNOZZLEUP(2);
                    // ptsMaster.mListDispenser.get(0).setdispenseOrder_status("Open");
                    // serverStatus.setImageResource(R.drawable.server_offline);
                    // break;
                    // case PTSMasterService.RESULT_NULL:
                    // Log.e("PTSTerminalResult", "RESULT_NULL");
                    // break;
                    // case PTSMasterService.RESULT_TIMEOUT:
                    // Log.e("PTSTerminalResult", "RESULT_TIMEOUT");
                    // serverStatus.setImageResource(R.drawable.server_offline);
                    // ptsAdapter.mAdapter.dispIDLE(2);
                    // ptsAdapter.mAdapter.openCell(5);
                    // ptsMaster.mListDispenser.get(0).setdispenseOrder_status("Close");
                    // break;
                    // }
                    // if ((ptsAdapter.mAdapter.getCount() > 0)
                    // && (ptsAdapter.mAdapter1.getCount() > 0)
                    // && (state !=
                    // getResources().getInteger(R.integer.state_unauthorized))
                    // && (state !=
                    // getResources().getInteger(R.integer.state_main_menu))
                    // ) {
                    // ptsAdapter.mAdapter1.setStatus(0,
                    // PTSTerminal.ptsAdapter.mAdapter.getStatus(Integer.valueOf(PTSTerminal.ptsAdapter.mAdapter1.getNumber(0))-1));
                    // }
                    // startTask();
                    // break;
                    // }
                    // break;
                    // }
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BC_PTSTERMINAL);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        // state = getResources().getInteger(R.integer.state_unauthorized);
        stateChange(getResources().getInteger(R.integer.state_unauthorized));

        startPTSMasterService();

        // startConnection(0);
        try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        posClient();
        getStore();
    }

    // #####################################################################################################
    // startPTSMasterService
    // #####################################################################################################
    public void startPTSMasterService() {

        // Создаем intentService для вызова сервиса
        intentService = new Intent(this, PTSMasterService.class);
        sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                myService = ((PTSMasterService.MyBinder) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };


        startService(intentService);
        bindService(intentService, sConn, 0);// BIND_AUTO_CREATE
    }

    public ArrayList_PTSMaster cloneArray(ArrayList_PTSMaster obj)
    {
        Log.v("+++", "object.mListProductAssortment.size = " + obj.mListProductAssortment.size());
        ArrayList_PTSMaster r = new ArrayList_PTSMaster();
        Log.v("+++", "object.mListProductAssortment.size = " + obj.mListProductAssortment.size());
        r.mListProductAssortment = new ArrayList<ArrayList_ProductAssortment>();
        Log.v("%%%", "object.mListProductAssortment.size = " + obj.mListProductAssortment.size());
        for (int i = 0; i < obj.mListProductAssortment.size(); i++) {
            Log.v("%%%", "object.mListProductAssortment[" + i + "].size = " + obj.mListProductAssortment.size());
            r.mListProductAssortment.add((ArrayList_ProductAssortment)obj.mListProductAssortment.get(i).clone());
            Log.v("%%%", "object.mListProductAssortment[" + i + "].size = " + obj.mListProductAssortment.size());
        }

//		this.mListProductAssortment = new ArrayList<ArrayList_ProductAssortment>();
//		for (int i = 0; i < obj.mListProductAssortment.size(); i++) {
//			mListProductAssortment.addAll(new ArrayList<ArrayList_ProductAssortment>());
//		}
//		mListStore = new ArrayList<ArrayList_Store>();
//		mListDispenser = new ArrayList<ArrayList_Dispenser>();
//		mListOrder = new ArrayList<ArrayList_Order>();
//		mListClerks = new ArrayList<ArrayList_Clerks>();
//		mListDispenserCfg = new ArrayList<ArrayList<String>>();
//		mListPayForm = new ArrayList<ArrayList_PayForm>();
//		mListBasket = new ArrayList<ArrayList_BasketData>();
//		OrderBasketItem = new ArrayList_BasketItems();
//		mListSignal = new ArrayList_Signal();
//		idPacket = 0;
//		idUser = "";
//		name = "";
        return r;
    }



    // //
    // #####################################################################################################
    // // Задача периодического опроса POS-сервера
    // //
    // #####################################################################################################
    // public void startConnection(int timeoutValue) {
    //
    // Log.v(LOG_TAG, "<<< START TIMER TASK CONNECTION >>>  (" + timeoutValue +
    // "mS.)");
    // // Toast.makeText(getApplicationContext(), "TimerStartService",
    // Toast.LENGTH_SHORT).show();
    // timer = new Timer("TimerStartService");
    // task = new TimerTask() { // Определяем задачу
    // @Override
    // public void run() {
    //
    // int y = 10;
    // while(myService == null) {
    // Log.i(LOG_TAG, "wait myService");
    // if (y == 0) break;
    // y--;
    // try {
    // Thread.sleep(1000);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // myService.setConnection(BC_PTSTERMINAL,
    // "Timer",
    // PTSMASTER_TASKCONNECT
    // );
    //
    // };
    // };
    // // Устанавливаю время: через сколько будет запущена задача task (в
    // милисекундах)
    // timer.schedule( task, timeoutValue );
    // }

    // #####################################################################################################
    // Задача периодического опроса POS-сервера
    // #####################################################################################################
    public void startTask(int packetCommandNumber, int timeoutValue) {

        Log.v(LOG_TAG, "<<< START TIMER TASK >>>  (" + timeoutValue + "mS.)");
        if (timer != null) timer.cancel();
        if (task != null) task.cancel();

        switch (packetCommandNumber) {
            case 1:
                queueCommands = mQueueCommandsAll;
                break;
            case 2:
                queueCommands = mQueueCommandsPump;
                break;
            case 3:
                queueCommands = mQueueCommandsBasket;
                break;

            default:
                queueCommands = null;
                break;
        }
        // Toast.makeText(getApplicationContext(), "TimerStartService",
        // Toast.LENGTH_SHORT).show();
        timer = new Timer("TimerStartService");
        task = new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                byte buffOrder[] = null;
                if (queueCommands.length == 0) {
                    Log.e(LOG_TAG, "queueCommands.length == 0");
                    return;
                }
                // Log.v(LOG_TAG, "<<< RUN TASK >>>");
                for (int i = 0; i < queueCommands.length; i++) {
                    switch (queueCommands[i]) {
                        case PTSMasterService.FUNC_PUMPSTATE:
                        case PTSMasterService.FUNC_PAYFORMLIST:
                        case PTSMasterService.FUNC_VERSION:
                        case PTSMasterService.FUNC_DISPENSERGET:
                            buffOrder = funcPTSMaster.f_noargs();
                            break;
                        case PTSMasterService.FUNC_BASKETLIST:
                            buffOrder = funcPTSMaster.f_basketList("0", "0");
                            break;
                        case PTSMasterService.FUNC_BASKETGET:
                            buffOrder = funcPTSMaster.f_basketGet("-1", "0", "0");
                            break;
                        default:
                            Log.e(LOG_TAG, "UNKNOWN COMMAND: "
                                    + queueCommands[i]);
                    }

                    if (buffOrder == null) {
                        return;
                    }
                    int buffOrderLength = myLib.getIntFromByteArray(buffOrder, 0,
                            funcPTSMaster.DATA_LEN_i) + 4;

                    int y = 10;
                    while (myService == null) {
                        Log.i(LOG_TAG, "wait myService");
                        if (y == 0)
                            break;
                        y--;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myService.sendPacket(BC_PTSTERMINAL, "Timer",
                            queueCommands[i],
                            PTSMASTER_TASKTRANSMITT,
                            ptsMaster.idPacket++,
                            buffOrder, buffOrderLength);

                }
                queueCommands = mQueueCommandsAll;
            };
        };
        // Устанавливаю время: через сколько будет запущена задача task (в
        // милисекундах)
        timer.schedule(task, timeoutValue, 30000);
    }

    // #####################################################################################################
    // Получение списка резервуаров АЗС
    // #####################################################################################################
    public void posClient() {

        Log.i(LOG_TAG, "posClient Start");
        // Toast.makeText(getApplicationContext(), "TimerStartService",
        // Toast.LENGTH_SHORT).show();
        ptsAdapter.mAdapter.set_ptsMaster(ptsMaster);
        timer = new Timer("TimerStartService");
        task = new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                byte buffOrder[] = null;
                buffOrder = funcPTSMaster.f_posClient();
                if (buffOrder == null) {
                    return;
                }
                int buffOrderLength = myLib.getIntFromByteArray(buffOrder, 0, funcPTSMaster.DATA_LEN_i) + 4;

                int y = 10;
                while (myService == null) {
                    Log.i(LOG_TAG, "wait myService");
                    if (y == 0)
                        break;
                    y--;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                myService.sendPacket(BC_PTSTERMINAL,
                        "PosClient",
                        PTSMasterService.FUNC_POSCLIENT,
                        PTSMASTER_TASKTRANSMITT,
                        ptsMaster.idPacket++,
                        buffOrder,
                        buffOrderLength);
            };
        };
        // Устанавливаю время: через сколько будет запущена задача task (в
        // милисекундах)
        timer.schedule(task, 0);
    }

    // #####################################################################################################
    // finishProg
    // #####################################################################################################
    public void finishProg() {
        // ---------------------------------- Выключаю таймер
        // task.cancel();
        if (timer != null) {
            timer.cancel();
        }
        // if (intentService != null) {
        // Log.i("result", "stopService = " + stopService(intentService));
        // }
        // Log.i("result", "stopService = " + stopService(intentService));
        Log.i("result", "stopService = " + stopService(intentService));
        PTSTerminal.this.finish();
    }

    @Override
    protected void onStop() {
        Log.i("onStop()", "###############################");
        super.onStop();
        if (!bound)
            return;
        unbindService(sConn);
        bound = false;
//		Log.i("result", "stopService = " + stopService(intentService));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onStart()", "###############################");
//		startPTSMasterService();
        // startConnection(0);
//		try {
//			TimeUnit.SECONDS.sleep(2);
//		}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		posClient();
//		getStore();
    }

    // #####################################################################################################
    // startAuth
    // #####################################################################################################
    public void startAuth() {
        intent = new Intent();
        intent.setClass(this, UserEntryActivity.class);
        // эапускаем деятельнсть
        startActivityForResult(intent, PAGE_AUTHORIZATION);
    }

    // #####################################################################################################
    // Получение списка пользователей POS-сервер и их паролей
    // #####################################################################################################
    public void getClerkList() {
        Log.i(LOG_TAG, "getClerkList Start");
        intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
        intent.putExtra("function", PTSMasterService.FUNC_CLERCLIST);
        // эапускаем деятельнсть
        startActivityForResult(intent, PTSMASTER_GETCLERKLIST);
    }

    // #####################################################################################################
    // Получение конфигурации АЗС
    // #####################################################################################################
    public void getDispensers() {
        Log.i(LOG_TAG, "getDispenserkList Start");
        intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
        intent.putExtra("function", PTSMasterService.FUNC_DISPENSERGET);
        // эапускаем деятельнсть
        startActivityForResult(intent, PTSMASTER_GETDISPENSERLIST);
    }

    // #####################################################################################################
    // Получение списка продуктов АЗС
    // #####################################################################################################
    public void getProducts() {
        Log.i(LOG_TAG, "getProducts Start");
        intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
        intent.putExtra("function", PTSMasterService.FUNC_PLUGET);
        // эапускаем деятельнсть
        startActivityForResult(intent, PTSMASTER_PLUGET);
    }

    // #####################################################################################################
    // Получение списка резервуаров АЗС
    // #####################################################################################################
    public void getStore() {
        Log.i(LOG_TAG, "getStore Start");
        intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
        intent.putExtra("function", PTSMasterService.FUNC_STOREGET);
        // эапускаем деятельнсть
        startActivityForResult(intent, PTSMASTER_STOREGET);
    }

    // #####################################################################################################
    // Получение списка форм оплат
    // #####################################################################################################
    public void getPayFormList() {
        Log.i(LOG_TAG, "getPayFormList Start");
        intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
        intent.putExtra("function", PTSMasterService.FUNC_PAYFORMLIST);
        // эапускаем деятельнсть
        startActivityForResult(intent, PTSMASTER_PAYFORMLIST);
    }

    // #####################################################################################################
    // setHandlerFromActivity
    // #####################################################################################################
    public static void setHandlerFromActivity(Handler h) {
        hActivity = h;
    }

    // #####################################################################################################
    // clearHandlerFromActivity
    // #####################################################################################################
    public static void clearHandlerFromActivity() {
        hActivity = null;
    }

    // #####################################################################################################
    // setHandlerFromDispenserControlActivity
    // #####################################################################################################
    public static void setHandlerFromDispenserControlActivity(Handler h) {
        hDispenserControlActivity = h;
    }

    // #####################################################################################################
    // clearHandlerFromDispenserControlActivity
    // #####################################################################################################
    public static void clearHandlerFromDispenserControlActivity() {
        hDispenserControlActivity = null;
    }

    // #####################################################################################################
    // startMainMenu
    // #####################################################################################################
    public void startMainMenu() {
        Log.v(LOG_TAG,
                "mListDispenser.size() = " + ptsMaster.mListDispenser.size());
        ptsAdapter.mAdapter.set_ptsMaster(ptsMaster);
        // PTSTerminal.state =
        // getResources().getInteger(R.integer.state_main_menu);
        Log.i(BC_PTSTERMINAL, "start Main Menu...");
        // PTSMasterClientActivity.mListDispenser.get(0).setdispenseOrder_status("Close");
        // Log.i("DispenserStatus",
        // PTSMasterClientActivity.mListDispenser.get(0).getDispenserOrder_status());
        // Log.i("Status",
        // PTSMasterClientActivity.mListDispenser.get(0).getDispenserStatus());
        intent = new Intent();
        intent.setClass(this, GridViewActivity.class);
        // intent.setClass(this, MainMenuActivity.class);
        // эапускаем деятельнсть
        startActivityForResult(intent, PAGE_MAIN_MENU);
    }

    // protected Dialog onDismiss(DialogInterface dialog) {
    // switch (dialog) {
    // case DIALOG_CONFIG_ENTRY:
    // }
    // }

    protected void stateChange(int state) {
        PTSTerminal.state = state;
        if (state == getResources().getInteger(R.integer.state_unauthorized)) {
            ptsMaster.name = "";
        }
        userName.setText(ptsMaster.name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(br);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
//		case DIALOG_CONFIG_ENTRY:
//			Log.i("DIALOG_CONFIG_ENTRY", "Start...");
//			// This example shows how to add a custom layout to an AlertDialog
//			LayoutInflater factoryConfig = LayoutInflater.from(this);
//			final View textEntryViewConfig = factoryConfig.inflate(
//					R.layout.alert_dialog_config_entry, null);
//			amountProduct = (EditText) textEntryViewConfig
//					.findViewById(R.id.config_edit);
//			final TextView amountConfig = (TextView) textEntryViewConfig
//					.findViewById(R.id.config_enter);
//			amountConfig.setText("Config " + String.valueOf(currentConfig));
//			return new AlertDialog.Builder(PTSTerminal.this)
//					.setIcon(R.drawable.alert_dialog_icon)
//					.setTitle(R.string.title_dialog_enter_config)
//					.setView(textEntryViewConfig)
//					.setPositiveButton(R.string.btn_ok,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									if (!amountProduct.getText().toString()
//											.equals("")) {
//										currentConfig++;
//									} else {
//										Log.e("amountProduct", "NULL");
//									}
//									Log.i("currentConfig",
//											String.valueOf(currentConfig));
//									if (currentConfig < 3) {
//										removeDialog(DIALOG_CONFIG_ENTRY);
//										showDialog(DIALOG_CONFIG_ENTRY);
//									} else {
//										showDialog(DIALOG_YES_NO_MESSAGE);
//									}
//									/* User clicked OK so do some stuff */
//								}
//							})
//					.setNegativeButton(R.string.btn_cancel,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									showDialog(DIALOG_YES_NO_MESSAGE);
//									/* User clicked cancel so do some stuff */
//								}
//							}).create();

            case DIALOG_YES_NO_MESSAGE:
                Log.i("DIALOG_YES_NO_MESSAGE", " ");
                return new AlertDialog.Builder(PTSTerminal.this)
                        .setIcon(R.drawable.alert_dialog_icon)
                        .setTitle(R.string.alert_dialog_two_buttons_title)
                        .setPositiveButton(R.string.btn_yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {

                                        finishProg();
                                    }
                                })
                        .setNegativeButton(R.string.btn_no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        startAuth();
                                    }
                                }).create();
            case DIALOG_RECONNECT_EXIT:
                Log.i("DIALOG_RECONNECT_EXIT", " ");
                return new AlertDialog.Builder(PTSTerminal.this)
                        .setIcon(R.drawable.alert_dialog_icon)
                        .setTitle(R.string.alert_dialog_reconnect_title)
                        .setPositiveButton(R.string.btn_reconnect,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        getStore();
                                    }
                                })
                        .setNegativeButton(R.string.btn_exit,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        finishProg();
                                    }
                                }).create();
            case DIALOG_OK_ERROR:
                return new AlertDialog.Builder(PTSTerminal.this)
                        .setIcon(R.drawable.alert_dialog_icon)
                                // .setTitle(getResources().getStringArray(R.array.error_udt)[error])
                        .setTitle(errorMessage)
                        .setPositiveButton(R.string.btn_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        startAuth();
                                    }
                                }).create();
            case DIALOG_EXIT_MAIN_MENU:
                return new AlertDialog.Builder(PTSTerminal.this)
                        .setIcon(R.drawable.alert_dialog_icon)
                                // .setTitle(R.string.alert_dialog_two_buttons_title)
                        .setTitle(getResources().getText(R.string.alert_dialog_two_buttons_from_main_menu))
                        .setPositiveButton(R.string.btn_yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        // state =
                                        // getResources().getInteger(R.integer.state_unauthorized);
                                        stateChange(getResources().getInteger(
                                                R.integer.state_unauthorized));
                                        // ptsMaster.name = "";
                                        // userName.setText(ptsMaster.name);
                                        PTSTerminal.mListCardOperator.clear();
                                        startAuth();
                                    }
                                })
                        .setNegativeButton(R.string.btn_no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        startMainMenu();
                                    }
                                }).create();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        // userName.setText(ptsMaster.name);
        switch (resultCode) {
            // ===========================================================================================
            // ====================================================================
            // RESULT_OK
            // ===========================================================================================
            case RESULT_OK:
                switch (requestCode) {
                    case PAGE_AUTHORIZATION:
                        stateChange(getResources()
                                .getInteger(R.integer.state_main_menu));
                        startMainMenu();
                        // if (state ==
                        // getResources().getInteger(R.integer.state_unauthorized)) {
                        // state = getResources().getInteger(R.integer.state_main_menu);
                        // startMainMenu();
                        // }
                        break;
                    case PTSMASTER_STOREGET:
//				Log.i(LOG_TAG, "Stores amount: " + ptsMaster.mListStore.size());
//				Log.i("###",
//						"================================================================");
//				Log.i("###", "Index   ProductArticle   Size   Height ");
//				Log.i("###",
//						"================================================================");
//				for (int i = 0; i < ptsMaster.mListStore.size(); i++) {
//					Log.i("###", " " + ptsMaster.mListStore.get(i).getIndex()
//							+ "  " + ptsMaster.mListStore.get(i).getArticleId()
//							+ "  " + ptsMaster.mListStore.get(i).getSize()
//							+ "  " + ptsMaster.mListStore.get(i).getName()
//							+ "  " + ptsMaster.mListStore.get(i).getShortName()
//							+ "  " + ptsMaster.mListStore.get(i).getHeight());
//				}
//				Log.i("###",
//						"================================================================");
                        getProducts();
                        break;
                    case PTSMASTER_PLUGET:
//				Log.i(LOG_TAG, "Product amount: "
//						+ ptsMaster.mListProductAssortment.size());
//				Log.i("###",
//						"================================================================");
//				Log.i("###", "Article   Name   Price   Rest");
//				Log.i("###",
//						"================================================================");
//				for (int i = 0; i < ptsMaster.mListProductAssortment.size(); i++) {
//					Log.i("###", " "
//							+ ptsMaster.mListProductAssortment.get(i)
//									.getArticleId()
//							+ "  "
//							+ ptsMaster.mListProductAssortment.get(i).getName()
//							+ "  "
//							+ ptsMaster.mListProductAssortment.get(i)
//									.getPrice() + "  "
//							+ ptsMaster.mListProductAssortment.get(i).getRest());
//				}
//				Log.i("###",
//						"================================================================");
                        getPayFormList();
                        break;
                    case PTSMASTER_PAYFORMLIST:
//				Log.i(LOG_TAG,
//						"PayForm amount: " + ptsMaster.mListPayForm.size());
//				Log.i("###",
//						"================================================================");
//				Log.i("###", "id    Name    Type");
//				Log.i("###",
//						"================================================================");
//				for (int i = 0; i < ptsMaster.mListPayForm.size(); i++) {
//					Log.i("###", " " + ptsMaster.mListPayForm.get(i).getId()
//							+ "  " + ptsMaster.mListPayForm.get(i).getName()
//							+ "  " + ptsMaster.mListPayForm.get(i).getType());
//				}
//				Log.i("###",
//						"================================================================");
                        getDispensers();
                        break;
                    case PTSMASTER_GETDISPENSERLIST:
//				Log.i(LOG_TAG, "Dispenser amount: "
//						+ ptsMaster.mListDispenserCfg.size());
                        Log.i("###",
                                "================================================================");
                        Log.i("###", "List of stores:");
                        Log.i("###",
                                "================================================================");
                        for (int i = 0; i < ptsMaster.mListDispenserCfg.size(); i++) {
                            for (int n = 0; n < ptsMaster.mListDispenserCfg.get(i)
                                    .size(); n++) {
                                Log.i("###",
                                        "Dispenser-"
                                                + ptsMaster.mListDispenserCfg.get(i).get(n).getName()
                                                + ": Nozzle-"
                                                + ptsMaster.mListDispenserCfg.get(i).get(n).getNozzle()
                                                + ": store-"
                                                + ptsMaster.mListDispenserCfg.get(i).get(n).getStore()
                                                + ": Product-"
                                                + ptsMaster.mListProductAssortment
                                                .get(Integer
                                                        .valueOf(ptsMaster.mListStore
                                                                .get(Integer.valueOf(ptsMaster.mListDispenserCfg.get(i).get(n).getStore()))
                                                                .getArticleId()))
                                                .getName());
                            }
                        }
                        Log.i("###",
                                "================================================================");
                        getClerkList();
                        break;
                    case PTSMASTER_GETCLERKLIST:
//				Log.i("###",
//						"================================================================");
//				Log.i("###", "Index   Name   Permission   Password");
//				Log.i("###",
//						"================================================================");
//				for (int i = 0; i < ptsMaster.mListClerks.size(); i++) {
//					Log.i("###", " " + ptsMaster.mListClerks.get(i).getIndex()
//							+ "  " + ptsMaster.mListClerks.get(i).getName()
//							+ "  "
//							+ ptsMaster.mListClerks.get(i).getPermissions()
//							+ "  " + ptsMaster.mListClerks.get(i).getPassword());
//				}
//				Log.i("###",
//						"================================================================");
                        startAuth();
                        startTask(1, 0);// Первый запуск "тихого" обмена данными с ПТС-Мастером
                        break;
                    case PAGE_MAIN_MENU:
                        // Toast.makeText(getApplicationContext(), "Main menu finished",
                        // Toast.LENGTH_SHORT).show();
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        stateChange(getResources().getInteger(
                                R.integer.state_unauthorized));
                        startAuth();
                        break;
                }
                break;
            // ===========================================================================================
            // ====================================================================
            // RESULT_ERROR
            // ===========================================================================================
            case defines.RESULT_ERROR:
                extras = data.getExtras();
                error = extras.getInt("Error");
                Log.e(BC_PTSTERMINAL, "Error: " + error);
                switch (requestCode) {
                    case PTSMASTER_STOREGET:
                    case PTSMASTER_PLUGET:
                    case PTSMASTER_PAYFORMLIST:
                    case PTSMASTER_GETDISPENSERLIST:
                    case PTSMASTER_GETCLERKLIST:
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        Toast.makeText(getApplicationContext(), "RESULT_ERROR",
                                Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_RECONNECT_EXIT);
                        break;
                    case PAGE_AUTHORIZATION:
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        stateChange(getResources().getInteger(
                                R.integer.state_unauthorized));
                        userName.setText(ptsMaster.idUser);
                        PTSTerminal.mListCardOperator.clear();
                        startAuth();
                        break;
                    case PAGE_MAIN_MENU:
                        Toast.makeText(getApplicationContext(), "Main menu finished",
                                Toast.LENGTH_SHORT).show();
                        startAuth();
                        break;
                }
                break;
            // ====================================================================
            // RESULT_PROTOCOL_ERROR
            // ===========================================================================================
            case defines.RESULT_PROTOCOL_ERROR:
                extras = data.getExtras();
                errorMessage = extras.getString("errorMessage");
                Log.e(BC_PTSTERMINAL, "Error: " + error);
                switch (requestCode) {
                    case PTSMASTER_STOREGET:
                    case PTSMASTER_PLUGET:
                    case PTSMASTER_PAYFORMLIST:
                    case PTSMASTER_GETDISPENSERLIST:
                    case PTSMASTER_GETCLERKLIST:
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        Toast.makeText(getApplicationContext(), "RESULT_ERROR",
                                Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_OK_ERROR);
                        break;
                    case PAGE_AUTHORIZATION:
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        stateChange(getResources().getInteger(
                                R.integer.state_unauthorized));
                        userName.setText(ptsMaster.idUser);
                        PTSTerminal.mListCardOperator.clear();
                        startAuth();
                        break;
                    case PAGE_MAIN_MENU:
                        Toast.makeText(getApplicationContext(), "Main menu finished",
                                Toast.LENGTH_SHORT).show();
                        startAuth();
                        break;
                }
                break;
            // ===========================================================================================
            // RESULT_CANCELED
            // ===========================================================================================
            case RESULT_CANCELED:
                switch (requestCode) {
                    case PTSMASTER_GETCLERKLIST:
                        Log.i(LOG_TAG, "CANCELED PTSMASTER_GETCLERKLIST");
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        // Toast.makeText(getApplicationContext(), "RESULT_CANCELED",
                        // Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_RECONNECT_EXIT);
                        break;
                    case PTSMASTER_GETDISPENSERLIST:
                        Log.i(LOG_TAG, "CANCELED PTSMASTER_GETDISPENSERLIST");
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        // Toast.makeText(getApplicationContext(), "RESULT_CANCELED",
                        // Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_RECONNECT_EXIT);
                        break;
                    case PAGE_AUTHORIZATION:
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        stateChange(getResources().getInteger(
                                R.integer.state_unauthorized));
                        PTSTerminal.mListCardOperator.clear();
                        Toast.makeText(getApplicationContext(), "Canceled by user",
                                Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_YES_NO_MESSAGE);
                        // PTSTerminal.this.finish();
                        break;
                    case PAGE_MAIN_MENU:
                        Toast.makeText(getApplicationContext(), "Main menu finished",
                                Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_EXIT_MAIN_MENU);
                        break;
                    default:
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // ptsMaster.name = "";
                        PTSTerminal.mListCardOperator.clear();
                        Toast.makeText(getApplicationContext(), "Cancel PTSMaster",
                                Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_RECONNECT_EXIT);
                }
                break;
            // ===========================================================================================
            // ================================================= RESULT_TIMEOUT
            // ===========================================================================================
            case defines.RESULT_TIMEOUT:
                // state = getResources().getInteger(R.integer.state_unauthorized);
                // PTSTerminal.mListCardOperator.clear();
                Toast.makeText(getApplicationContext(), "Timeout",
                        Toast.LENGTH_SHORT).show();
                switch (requestCode) {
                    case PTSMASTER_GETCLERKLIST:
                        Log.e(LOG_TAG, "TIMEOUT PTSMASTER_GETCLERKLIST");
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // Toast.makeText(getApplicationContext(), "RESULT_TIMEOUT",
                        // Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_RECONNECT_EXIT);
                        break;
                    case PTSMASTER_GETDISPENSERLIST:
                        Log.e(LOG_TAG, "TIMEOUT PTSMASTER_GETDISPENSERLIST");
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // Toast.makeText(getApplicationContext(), "RESULT_TIMEOUT",
                        // Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_RECONNECT_EXIT);
                        break;
                    case PTSMASTER_STOREGET:
                        Log.e(LOG_TAG, "TIMEOUT PTSMASTER_STOREGET");
                        // state =
                        // getResources().getInteger(R.integer.state_unauthorized);
                        // Toast.makeText(getApplicationContext(), "RESULT_TIMEOUT",
                        // Toast.LENGTH_SHORT).show();
                        showDialog(DIALOG_RECONNECT_EXIT);
                        break;
                    default:
                        Log.e(LOG_TAG, "TIMEOUT UNKNOWN requestCode");
                        showDialog(DIALOG_RECONNECT_EXIT);
                }
                break;
            // ===========================================================================================
            // ================================================= RESULT_CUSTOM1
            // ===========================================================================================
            case defines.RESULT_CUSTOM1:
                startMainMenu();
                break;
            // ===========================================================================================
            // =======================================================================
            // default
            // ===========================================================================================
            default:
                // state = getResources().getInteger(R.integer.state_unauthorized);
                // PTSTerminal.mListCardOperator.clear();
                Toast.makeText(getApplicationContext(), "Unknown error",
                        Toast.LENGTH_SHORT).show();
                // PTSTerminal.this.finish();
                finishProg();
                break;

        }
    }

    // public void fillContacts() {
    // mList.add(new ContactItem("Jacob Anderson", "412412411"));
    // mList.add(new ContactItem("Emily Duncan", "161863187"));
    // mList.add(new ContactItem("Michael Fuller", "896443658"));
    // mList.add(new ContactItem("Emma Greenman", "964990543"));
    // mList.add(new ContactItem("Joshua Harrison", "759285086"));
    // mList.add(new ContactItem("Madison Johnson", "950285777"));
    // mList.add(new ContactItem("Matthew Cotman", "687699999"));
    // mList.add(new ContactItem("Olivia Lawson", "161863187"));
    // mList.add(new ContactItem("Andrew Chapman", "546599645"));
    // mList.add(new ContactItem("Daniel Honeyman", "876545644"));
    // mList.add(new ContactItem("Isabella Jackson", "907868756"));
    // mList.add(new ContactItem("William Patterson", "687699693"));
    // mList.add(new ContactItem("Joseph Godwin", "965467575"));
    // mList.add(new ContactItem("Samantha Bush", "907865645"));
    // mList.add(new ContactItem("Christopher Gateman", "896874556"));
    // }
}

// public void startTask() {
// timer = new Timer("TimerTimeout");
// // ---------------------------------- Запускаю таймер времени работы
// PTSMaster-клиента
// final Handler uiHandler = new Handler();
// task = new TimerTask() { // Определяем задачу
// @Override
// public void run() {
// // final String result = doLongAndComplicatedTask();
// uiHandler.post(new Runnable() {
// @Override
// public void run() {
// Log.i(BC_PTSTERMINAL, "Task is run");
// String hh = ptsMaster.func();
// Log.i(BC_PTSTERMINAL, "func(): " + hh);
// Message mess = mHandler.obtainMessage();
// Bundle b = new Bundle();
// b.putString("task", "success");
// mess.setData(b);
// mHandler.sendMessage(mess);
// }
// });
// };
// };
// // Устанавливаю время ожидания соединения (в милисекундах)
// timer.schedule( task, 5000 );
// }

// myHandler = new Handler(); // автоматически привязывается к текущему потоку.
// Thread myThread = new Thread() {
// @Override
// public void run() {
// synchronized (this) {
// myHandler.post(new Runnable() { // используя Handler, привязанный к UI-Thread
// public void run() {
// Log.i("myThread", "...");
// // Log.i("myThread", "hh = " + hh);
// // ptsMaster.mListDispenser.add(new ArrayList_Dispenser());
// }
// });
// try {
// final String hh = ptsMaster.func();
// wait(1000);
// }
// catch (InterruptedException e) {
// e.printStackTrace();
// }
// }
// }
// };
//
// myThread.start();

// Thread thread = new Thread() {
// @Override
// public void run() {
// synchronized (this) {
// myHandler.post(new Runnable() {
// public void run() {
// Log.i("myThread", "...");
// }
// });
// try {
// wait(5000);
// }
// catch (InterruptedException e) {
// e.printStackTrace();
// }
// }
// }
// };
// thread.start();

// final Handler uiHandler = new Handler();
// task = new TimerTask() { // Определяем задачу
// @Override
// public void run() {
// // final String result = doLongAndComplicatedTask();
// uiHandler.post(new Runnable() {
// @Override
// public void run() {
// Log.i(BC_PTSTERMINAL, "Task is run");
// String hh = ptsMaster.func();
// Log.i(BC_PTSTERMINAL, "func(): " + hh);
// }
// });
// };
// };
// }, 0L, 5L * 1000); // интервал - 60000 миллисекунд, 0 миллисекунд до первого
// запуска.

// currentConfig = 0;
// showDialog(DIALOG_CONFIG_ENTRY);

