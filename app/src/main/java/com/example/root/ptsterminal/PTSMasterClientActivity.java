package com.example.root.ptsterminal;

import android.app.Activity; 
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle; 
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import adapter.ImageAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class PTSMasterClientActivity extends Activity  {

    private Timer     timer;
    TimerTask task;
    boolean bound = false;
    ServiceConnection sConn;
    PTSMasterService ptrService;
    Messaging mess = null;
	
	private static String idClerk;
	private static String passClerk;

    private static final int DIALOG_OK_ERROR = 1;
    final String LOG_TAG = "ClientActivity_Log";
	// Создание объектов для запуска сервиса PTSMasterService
//    Intent intentService;

//    private Messaging messagingClient;
    private ProgressDialog pleaseWaitDialog;
//    private Timer updateTimer;
//    private TimerTask timeSheduler;
//    private Timer     timer;
    
    int partId;
    BroadcastReceiver br;
    
    Handler mHandler;
//    CharSequence  exchange;
    int requestCode;
    int buffLength;
    private int ptsMasterError = 0;
	private int parseLen;
	private String parseString;
	private int parseItem;
    private boolean localError = true;
    private int requestFunction;

//    static ArrayList<ArrayList_ProductAssortment> mListProductAssortment;
//    static ArrayList<ArrayList_Dispenser> mListDispenser;
//    static ArrayList<ArrayList_Order> mListOrder;

//    public enum RequestType {
//
//        API_OPEN("api_open"),
//        API_LOCK("api_lock"),
//        API_UNLOCK("api_unlock");
//        
//        private String typeValue;
//        
//        private RequestType(String type) {
//            typeValue = type;
//        }
//        
//        static public RequestType getType(String pType) {
//            for (RequestType type: RequestType.values()) {
//                if (type.getTypeValue().equals(pType)) {
//                    return type;
//                }
//            }
//            throw new RuntimeException("unknown type");
//        }
//        
//        public String getTypeValue() {
//            return typeValue;
//        }
//        
//    }

    @Override 
    public void onCreate(Bundle savedInstanceState) 
    { 
//    	try {
	        super.onCreate(savedInstanceState); 
	        setContentView(R.layout.ptsmaster_client); 
	        Log.d(LOG_TAG, "!!!------------!!! PTSMasterClientActivity START!!!");
//	        final Button button = (Button) findViewById(R.id.refrbutton); 
//	        final TextView tTemper = (TextView) findViewById(R.id.temper);
	        
//	        mListProductAssortment = new ArrayList<ArrayList_ProductAssortment>();
//	        mListDispenser = new ArrayList<ArrayList_Dispenser>();
//	        mListOrder = new ArrayList<ArrayList_Order>();
	        
			/* Установка конвейеров */
//	        final PipedOutputStream pout1 = new PipedOutputStream();
//	        final PipedInputStream pin1 = new PipedInputStream(pout1);
//	        final PipedOutputStream pout2 = new PipedOutputStream();
//	        final PipedInputStream pin2 = new PipedInputStream(pout2);

			partId = 0;

	        Bundle extras = getIntent().getExtras();
//	        exchange = extras.getCharSequence("Exchange");
	        requestFunction = extras.getInt("function");
	        
		    switch (requestFunction) {
			case PTSMasterService.FUNC_CLERCREG:
				idClerk = extras.getString("idClerk");
				passClerk = extras.getString("passClerk");
				break;
			}
	        
//	        Log.i("Exchange", "extras: " + exchange);
//	        Log.i("mList Amount", Integer.toString(mList.size()));
//	        Log.i("mList Name", mList.get(3).getName().toString());
//	        Log.i("mList Phone", mList.get(3).getPhone().toString());
//	        Log.i("mList Name", mList.get(mList.size()-1).getName().toString());

	        mHandler = new Handler() {
	        	int function;
	            @Override
	            public void handleMessage(Message msg) {
//                	Log.d("ClientHandler", "Message received");
	            	if (msg.getData().containsKey("ptsMasterAnswer")) {
//	                	messagingClient.interrupt();
		                pleaseWaitDialog.dismiss();
//		                timer.cancel();
		                int result = msg.getData().getInt("ptsMasterAnswer");
		            	if (msg.getData().containsKey("function")) {
			                function = msg.getData().getInt("function");
		            	}
		                if (result == funcPTSMaster.STATUS_PACKAGE_OK){
			                // --- Данные приняты
			        		switch (function) {
			        		case PTSMasterService.FUNC_CLERCLIST:
//			        			Log.d("Client function", "clerkList");
			        			break;
			        		case PTSMasterService.FUNC_DISPENSERGET:
//			        			Log.d("Client function", "dispenserGet");
			        			break;
			        		case PTSMasterService.FUNC_PLUGET:
//			        			Log.d("Client function", "pluGet");
			        			break;
			        		case PTSMasterService.FUNC_BASKETPAYMENT:
//			        			Log.d("Client function", "basketPayment");
			        			break;
			        		case PTSMasterService.FUNC_BASKETCLOSE:
//			        			Log.d("Client function", "basketClose");
			        			break;
			        		case PTSMasterService.FUNC_BASKETCLEAR:
//			        			Log.d("Client function", "basketClear");
			        			break;
			        		case PTSMasterService.FUNC_BASKETADD:
//			        			Log.d("Client function", "clerkReg");
			        			break;
			        		case PTSMasterService.FUNC_PUMPRESUME:
//			        			Log.d("Client function", "pumpResume");
			        			break;
			        		case PTSMasterService.FUNC_PUMPSTOP:
//			        			Log.d("Client function", "pumpStop");
			        			break;
			        		case PTSMasterService.FUNC_CLERCREG:
//			        			Log.d("Client function", "clerkReg");
			        			break;
			        		case PTSMasterService.FUNC_VERSION:
//			        			Log.d("Client function", "version");
			        			break;
			        		case PTSMasterService.FUNC_STOREGET:
//			        			Log.d("Client function", "storeGet");
			        			break;
			        		case PTSMasterService.FUNC_PUMPSTATE:
//			        			Log.d("Client function", "pumpState");
			        			break;
			        		case PTSMasterService.FUNC_PAYFORMLIST:
//			        			Log.d("Client function", "payFormList");
			        			break;
			        		default:
			        			Log.e("Client function", "unknown");
			        			break;
			        		}
//		                    Toast.makeText(getApplicationContext(), "Task is finished", Toast.LENGTH_SHORT).show();
		                    Intent intent = new Intent();
		                    setResult(RESULT_OK, intent);
		                    funcFinish();
		                }
		                if (result == funcPTSMaster.STATUS_PROTOCOL_ERROR){
		                	String error = msg.getData().getString("errorMessage");
		                	Log.e("Parsing", "RESULT_PROTOCOL_ERROR");
		                	Intent intentError = new Intent();
			                intentError.putExtra("errorMessage", error); 
			                setResult(defines.RESULT_PROTOCOL_ERROR, intentError);
			                funcFinish();
		                }
		                if (result == funcPTSMaster.STATUS_DAMAGED){
		                	Log.e("Parsing", "DAMAGED");
		                	Intent intentError = new Intent();
			                int er = 2;
			                intentError.putExtra("Error", er); 
			                setResult(defines.RESULT_ERROR, intentError);
			                funcFinish();
		                }
	            	}
	            	if (msg.getData().containsKey("timer")) {
		                String otal = msg.getData().getString("timer");
			            if (otal.equals("Timeout")) {
		                	Log.e("Parsing", "TIMEOUT");
			                pleaseWaitDialog.dismiss();
		                	Intent intentError = new Intent();
			                int er = 3;
			                intentError.putExtra("Error", er); 
			                setResult(defines.RESULT_ERROR, intentError);
			                funcFinish();
		                }
	            	}
//	            	if (msg.getData().containsKey("dialog")) {
//		                String otal = msg.getData().getString("dialog");
//			            if (otal.equals("cancel")) {
////		                	messagingClient.interrupt();
//			                pleaseWaitDialog.dismiss();
////			                timer.cancel();
////		                    Toast.makeText(getApplicationContext(), "Task is timeout", Toast.LENGTH_SHORT).show();
//		                    setResult(defines.RESULT_USERCANCEL); 
//		                    funcFinish();
//		                }
//	            	}
	            }
	        };
	        
	        PTSTerminal.setHandlerFromActivity(mHandler);
	        
//	        Intent intentTask = new Intent(PTSTerminal.BC_PTSTERMINAL);
//	        requestCode = extras.getInt("requestCode");
//            Log.i("Client", "requestCode: " + requestCode);
//	        intentTask.putExtra(PTSMasterService.PARAM_TASK, requestCode);
//            sendBroadcast(intentTask);

            pleaseWaitDialog = new ProgressDialog(PTSMasterClientActivity.this);
            pleaseWaitDialog.setTitle(R.string.title_info);
            pleaseWaitDialog.setMessage(getResources().getText(R.string.textConnectingToServer));
            pleaseWaitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pleaseWaitDialog.setMax(100);
            pleaseWaitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
//                	messagingClient.interrupt();
	                pleaseWaitDialog.dismiss();
//	                timer.cancel();
	                if (mess != null) {
	                	ptrService.cancelTask(mess);
	                }
//                	Toast.makeText(getApplicationContext(), "CANCEL is pressed", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED); 
                    funcFinish();
                }
            });
            pleaseWaitDialog.setCancelable(false); 
            pleaseWaitDialog.show();
	             
			String deviceId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);	             
	        byte buff[] = new byte[20*1024];

			int fileId = 0;
//			Log.i("deviceId", deviceId);
			
//	        // создаем BroadcastReceiver
//	        br = new BroadcastReceiver() {
//	          // действия при получении сообщений
//	          public void onReceive(Context context, Intent intent) {
////	          	String nameTask = intent.getStringExtra(PTSMasterService.PARAM_RECEIVERNAME);
////	          	if (!nameTask.equals(BC_CLIENT)) {
////	        		return;
////	        	}
//	            int task = intent.getIntExtra(PTSMasterService.PARAM_TASK, 0);
//	            int status = intent.getIntExtra(PTSMasterService.PARAM_STATUS, 0);
//	            int result = intent.getIntExtra(PTSMasterService.PARAM_RESULT, 0);
//	            Log.d(LOG_TAG, "onReceive: task = " + task + ", status = " + status);
//	            
//	    		if (task == PTSMASTER_TASK) {
//	                pleaseWaitDialog.dismiss();
//	    			switch (status) {
//	    	            // ===========================================================================================
//	    	            // ================================================= PTSMasterService.STATUS_HANDLER 
//	    	            // ===========================================================================================
//	    		       case PTSMasterService.STATUS_IN_WORK:
//	    		  	        Log.i("ClientStatus", "STATUS_IN_WORK...");
//	    		    	    switch (result) {
//	    		            	case PTSMasterService.RESULT_SUCCESS:
//	    		            		Log.i("ClientResult", "RESULT_SUCCESS");
//	    		              		break;
//	    		            	case PTSMasterService.RESULT_DAMAGED:
//	    		                	Toast.makeText(getApplicationContext(), "DAMAGED", Toast.LENGTH_SHORT).show();
//	    		        	        Log.e("ClientResult", "RESULT_DAMAGED");
//	    							// -------------------------------- Ответ 
//	    				            Message msg = mHandler.obtainMessage();
//	    				            Bundle b = new Bundle();
//	    				            b.putInt("ptsMasterAnswer", funcPTSMaster.STATUS_DAMAGED);
//	    				            msg.setData(b);
//	    				            mHandler.sendMessage(msg);
//	    		            		break;
//	    		            	case PTSMasterService.RESULT_NULL:
//	    		        	        Log.e("ClientResult", "RESULT_NULL");
//	    		            		break;
//	    		            	case PTSMasterService.RESULT_TIMEOUT:
//	    		        	        Log.e("ClientResult", "RESULT_TIMEOUT");
//	   		            		break;
//	    		  			}
//	    		  			break;
//	    			}
//	    		}
//	          }
//	        };
//	        // создаем фильтр для BroadcastReceiver
//	        IntentFilter intFilt = new IntentFilter(BC_CLIENT);
//	        // регистрируем (включаем) BroadcastReceiver
//	        registerReceiver(br, intFilt);
			
//	        // ---------------------------------- Добавляю заголовок  
//			funcPTSMaster.createPtsMasterMess(buff, request, partId);
//			buffLength = 9; 
//		    RequestType request = RequestType.getType(exchange.toString());
//
////		    Log.i("CardCode", mListCardOperator.get(0).getCardCode());
////			Log.i("CardPin", mListCardOperator.get(0).getCardPin());
//			// ========================================================================================================================================================
//			//																	СОСТАВЛЕНИЕ ПАКЕТОВ EXCHANGE  
//			// ========================================================================================================================================================
//		    switch(request) {
//		        case API_OPEN:
////		        Log.i("PTSMasterClient", "api_open");
//				buffLength += funcPTSMaster.prepareStruct_0(buff, "api", "open", buffLength);
//		        break;
//	        default:
////		        Log.i("PTSMaster_no", Integer.toString(exchange)); 
//                setResult(RESULT_CANCELED); 
//		        funcFinish();
//	        }
//	        Log.i("PTSMasterClient", "Header create begin");
////	        // ---------------------------------- Добавляю заголовок и расчитываю MD5-сумму с добавлением её в конец пакета 
////			createUdtHeader(buff, 1, deviceId, buffLength, fileId, partId);
////			buffLength += 16;// добавляю размер MD5-суммы 
//			
//	        // ---------------------------------- Активизирую связь с сервером  
//            messagingClient = new Messaging(mHandler, buff, buffLength);
//            messagingClient.start();
//
//            // ---------------------------------- Запускаю таймер времени работы UDT-клиента  
//            timer = new Timer("PTSMaster_TimerTimeout");
//            TimerTask task = new PTSMaster_TimerTimeout(mHandler);
//            // Устанавливаю время ожидания соединения (в милисекундах) 
//            timer.schedule( task, 15000 );
	        
//		    Log.i("DispenserPrice",PTSTerminal.ptsMaster.mListDispenser.get(0).getDispenserOrder_price());
//		    PTSTerminal.ptsMaster.id++;
//		    byte buffOrder[];
//		    switch (requestFunction) {
//			case PTSMasterService.FUNC_CLERCLIST:
//				buffOrder = funcPTSMaster.f_clercList();
//				break;
//			case PTSMasterService.FUNC_VERSION:
//		        buffOrder = funcPTSMaster.f_version();
//				break;
//			default:
//				Log.e("CleintActivity", "Unknown request function: " + requestFunction);
//				return;
//			}
//	        int buffOrderLength = myLib.getIntFromByteArray(buffOrder, 0, funcPTSMaster.DATA_LEN_i) + 4;

//		    // Создаем Intent для вызова сервиса, кладем туда имя объекта для приёма ответа (PARAM_RECEIVERNAME), 
//		    // параметр времени и код задачи
//	        intentService = new Intent(PTSMasterClientActivity.this, PTSMasterService.class)
//		    	.putExtra(PTSMasterService.PARAM_RECEIVERNAME, PTSTerminal.BC_PTSTERMINAL)
//		    	.putExtra(PTSMasterService.PARAM_ARRAY, PTSTerminal.ptsMaster )
//		    	.putExtra(PTSMasterService.PARAM_ORDER_BUFF, buffOrder )
//		    	.putExtra(PTSMasterService.PARAM_ORDER_BUFF_LENGTH, buffOrderLength )
//		    	.putExtra(PTSMasterService.PARAM_FUNCTION, requestFunction)
//		    	.putExtra(PTSMasterService.PARAM_OWNER, "Client")
//		        .putExtra(PTSMasterService.PARAM_TASK, PTSTerminal.PTSMASTER_TASK);
//		    // стартуем сервис
//		    startService(intentService);

	        Log.d(LOG_TAG, "!!!***********!!! sConn = new ServiceConnection()!!!");

		    sConn = new ServiceConnection() {

				public void onServiceConnected(ComponentName name, IBinder binder) {
//				  Log.d(LOG_TAG, "onServiceConnected");
				  ptrService = ((PTSMasterService.MyBinder) binder).getService(); 
				  bound = true;
				}
				
				public void onServiceDisconnected(ComponentName name) {
//				  Log.d(LOG_TAG, "onServiceDisconnected");
				      bound = false;
			    }
		    };         

		    bindService(PTSTerminal.intentService, sConn, 0);//BIND_AUTO_CREATE


//		    int y = 10;
//	        while(ptrService == null) {
//	    		Log.i(LOG_TAG, "wait ptrService");
//	    		if (y == 0) break;
//	    		y--;
//	        	try {
//					TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//	        }
//		    ptrService.sendPacket(buffOrder, buffOrderLength);
		      
		      startTask(0);
		    
		      
		    // Создаем Intent для вызова сервиса, кладем туда имя объекта для приёма ответа (PARAM_RECEIVERNAME), 
		    // параметр времени и код задачи
//	        intentService = new Intent(this, PTSMasterService.class)
//		    	.putExtra(PTSMasterService.PARAM_RECEIVERNAME, PTSTerminal.BC_PTSTERMINAL)
//		    	.putExtra(PTSMasterService.PARAM_ID_PACKET, PTSTerminal.ptsMaster.id )
//		    	.putExtra(PTSMasterService.PARAM_ORDER_BUFF, buffOrder )
//		    	.putExtra(PTSMasterService.PARAM_ORDER_BUFF_LENGTH, buffOrderLength )
//		    	.putExtra(PTSMasterService.PARAM_FUNCTION, requestFunction)
//		    	.putExtra(PTSMasterService.PARAM_OWNER, "Client")
//		        .putExtra(PTSMasterService.PARAM_TASK, PTSTerminal.PTSMASTER_TASK);
//		    // стартуем сервис
//	        Log.d("intentService", "Start " + intentService);
//		    startService(intentService);
	                
//    	}
//    	catch (IOException e)
//    	{
//	    	  Log.i("Error", "IOException" + e + "\n");
//    	}
    };

    @Override
    protected void onStart() {
      super.onStart();
      Log.i(LOG_TAG, "onStart");
    }
    
    @Override
    protected void onStop() {
      super.onStop();
      if (!bound) return;
      unbindService(sConn);
      bound = false;
    }
    
//	@Override
//   	public void onBackPressed() {
//        Intent intent = new Intent();
//        setResult(RESULT_CANCELED, intent);
//        funcFinish();
//   	}

	@Override
   	public void onBackPressed() {
		Log.i(LOG_TAG, "onBackPressed");
   	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {
			Log.i(LOG_TAG, "LONG onBackPressed");
	        Intent intent = new Intent();
	        setResult(RESULT_CANCELED, intent);
	        funcFinish();
	        return true;
	    }
	    return super.onKeyLongPress(keyCode, event);
	}

	
    public void funcFinish() {
    	PTSTerminal.clearHandlerFromActivity();
    	finish();
    }

    // #####################################################################################################
    //	Задача периодического опроса POS-сервера 
    // #####################################################################################################
    public void startTask(int timeoutValue) {
    	
        timer = new Timer("TimerStartService");
		task = new TimerTask() { // Определяем задачу
			@Override
			public void run() {
				
			    PTSTerminal.ptsMaster.idPacket++;
			    byte buffOrder[];
			    switch (requestFunction) {
				case PTSMasterService.FUNC_CLERCLIST:
				case PTSMasterService.FUNC_DISPENSERGET:
				case PTSMasterService.FUNC_VERSION:
				case PTSMasterService.FUNC_PUMPSTATE:
				case PTSMasterService.FUNC_PAYFORMLIST:
			        buffOrder = funcPTSMaster.f_noargs();
					break;
				case PTSMasterService.FUNC_PLUGET:
			        buffOrder = funcPTSMaster.f_pluGet("0", "200");
					break;
				case PTSMasterService.FUNC_BASKETADD:
			        buffOrder = funcPTSMaster.f_basketAdd();
					break;
				case PTSMasterService.FUNC_BASKETCLOSE:
			        buffOrder = funcPTSMaster.f_basketClose();
					break;
				case PTSMasterService.FUNC_BASKETCLEAR:
			        buffOrder = funcPTSMaster.f_basketClear();
					break;
				case PTSMasterService.FUNC_PUMPRESUME:
			        buffOrder = funcPTSMaster.f_pumpResume();
					break;
				case PTSMasterService.FUNC_PUMPSTOP:
			        buffOrder = funcPTSMaster.f_pumpStop();
					break;
				case PTSMasterService.FUNC_BASKETPAYMENT:
			        buffOrder = funcPTSMaster.f_basketPayment();
					break;
				case PTSMasterService.FUNC_STOREGET:
			        buffOrder = funcPTSMaster.f_storeGet("0", "200");
					break;
				case PTSMasterService.FUNC_CLERCREG:
			        buffOrder = funcPTSMaster.f_clerkreg(idClerk, passClerk);
					break;
				default:
					Log.e("CleintActivity", "Unknown request function: " + requestFunction);
					return;
				}
		        int buffOrderLength = myLib.getIntFromByteArray(buffOrder, 0, funcPTSMaster.DATA_LEN_i) + 4;
		        
			    int y = 10;
		        while(ptrService == null) {
		    		Log.i(LOG_TAG, "wait ptrService");
		    		if (y == 0) break;
		    		y--;
		        	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
		        if (ptrService != null) {
		        	Log.i(LOG_TAG, "!!!.............!!!sendPacket: idPacket = " + PTSTerminal.ptsMaster.idPacket);
			        mess = ptrService.sendPacket(	PTSTerminal.BC_PTSTERMINAL, 
			        						"Client", 
			        						requestFunction, 
			        						PTSTerminal.PTSMASTER_TASKTRANSMITT, 
			        						PTSTerminal.ptsMaster.idPacket++, 
			        						buffOrder, 
			        						buffOrderLength
			        					);
		        }
		        else {
		        	Log.e(LOG_TAG, "!!!=====================!!!Ошибка подключения к сервису!!!");
	                pleaseWaitDialog.dismiss();
	                if (mess != null) {
	                	ptrService.cancelTask(mess);
	                }
                    setResult(RESULT_CANCELED); 
                    funcFinish();
		        }
			};
		};
        // Устанавливаю время: через сколько будет запущена задача task (в милисекундах) 
        timer.schedule( task, timeoutValue );
    }
    

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_OK_ERROR:
            return new AlertDialog.Builder(PTSMasterClientActivity.this)
                .setIcon(R.drawable.alert_dialog_icon)
				.setTitle(getResources().getString(R.string.title_errror))
//                .setTitle(getResources().getStringArray(R.array.error_ptsMaster)[ptsMasterError])
//                .setTitle("Error")
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
	                    Intent intent = new Intent();
		                intent.putExtra("Error", ptsMasterError); 
	                    setResult(defines.RESULT_ERROR, intent); 
	                    funcFinish();
                    }
                })
                .create();
        }
        return null;
    }
    
}
    
