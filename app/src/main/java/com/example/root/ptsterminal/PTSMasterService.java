package com.example.root.ptsterminal;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import android.R.bool;
import android.R.integer;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import app.pts.terminal.adapter.ImageAdapter;

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
public class PTSMasterService extends Service {

//	public static String serverIP = "192.168.1.102"; 
	public static String serverIP = "192.168.1.90"; 
//	public static String serverIP = "192.168.1.35"; 
	public static int serverPORT = 29999;
	
	public static SocketChannel channel;
	public static String connectionState = "unknown";
  final static String LOG_TAG = "PTSMasterService";
  ExecutorService es;
  Timer timerCheckConnection;
		
	private static ReadThread	readThreadClient;
  private Messaging messagingClient;
  private static prepareConnection connection;
//  private Timer     timer;
  int buffLength;
  MyBinder binder = new MyBinder();
    
//  PTSMasterSerial serializableObject;
  byte buffOrder[] = new byte[20*1024];

  private static final int DIALOG_CANCEL = 1;

  public final static int STATUS_START = 100;
  public final static int STATUS_FINISH = 200;
  public final static int STATUS_IN_WORK= 300;
  public final static int STATUS_MUTEX_ENABLE= 400;
  public final static int STATUS_MUTEX_DISABLE= 500;

  public final static String PARAM_TASK = "task";
  public final static String PARAM_OWNER = "owner";
  public final static String PARAM_FUNCTION = "function";
  public final static String PARAM_PINTENT = "pendingIntent";
  public final static String PARAM_TASKRESULT = "result";
  public final static String PARAM_SERVICESTATUS = "status";
  public final static String PARAM_RECEIVERNAME = "receivername";
  public final static String PARAM_ARRAY = "array";
  public final static String PARAM_ORDER_BUFF = "order_buff";
  public final static String PARAM_ORDER_BUFF_LENGTH = "order_buff_length";
  public final static String PARAM_RESULT_BUFF = "result_buff";
  public final static String PARAM_RESULT_BUFF_LENGTH = "result_buff_length";
  public final static String PARAM_ID_PACKET = "id_packet";
  public final static String PARAM_THREADRESULT = "threadresult";
  public final static String PARAM_SIGNAL = "signal";
  
  public static final int FUNC_EMULSIGNAL = 0;
  public static final int FUNC_POSCLIENT = 1;
  public static final int FUNC_VERSION = 2;
  public static final int FUNC_PUMPSTATE = 4;
  public static final int FUNC_PAYFORMLIST = 5;
  public static final int FUNC_DISPENSERGET = 6;
  public static final int FUNC_STOREGET = 7;
  public static final int FUNC_CLERCLIST = 10;
  public static final int FUNC_CLERCREG = 11;
  public static final int FUNC_BASKETLIST = 20;
  public static final int FUNC_BASKETGET = 21;
  public static final int FUNC_BASKETADD = 22;
  public static final int FUNC_BASKETPAYMENT = 23;
  public static final int FUNC_BASKETCLOSE = 24;
  public static final int FUNC_BASKETCLEAR = 25;
  public static final int FUNC_PLUGET = 30;
  public static final int FUNC_PUMPRESUME = 40;
  public static final int FUNC_PUMPSTOP = 41;


  public final static int RESULT_SUCCESS = 0;
  public final static int RESULT_TIMEOUT = 1;
  public final static int RESULT_NULL = 2;
  public final static int RESULT_DAMAGED = 3;
  public final static int RESULT_CANCEL = 4;
  public final static int RESULT_NOCONNECT = 5;

//  private ArrayList<Integer> idPacketList;
  private ArrayList<ArrayList_ReceiveSemaphor> receiveSemaphor;
  
  public void onCreate() {
    super.onCreate();
    Log.d(LOG_TAG, "MyService onCreate");
    receiveSemaphor = new ArrayList<ArrayList_ReceiveSemaphor>();
    readThreadClient = new ReadThread(mHandler, receiveSemaphor);
    readThreadClient.start();

    checkConnection();
//    es = Executors.newFixedThreadPool(1);
//    someRes = new Object();
  }
  
  public void onDestroy() {
    super.onDestroy();
    if (timerCheckConnection != null) {
    	timerCheckConnection.cancel();
    }
    if (readThreadClient.isAlive()) {
    	readThreadClient.interrupt();
    }
    Log.d(LOG_TAG, "MyService onDestroy");
  }

  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(LOG_TAG, "MyService onStartCommand");
//    receiveSemaphor = new ArrayList<ArrayList_ReceiveSemaphor>();
//    readThreadClient = new ReadThread(mHandler, receiveSemaphor);
//    readThreadClient.start();
//
//    checkConnection();
//    readThreadClient.start();

    // Получение параметров 
//    int task = intent.getIntExtra(PARAM_TASK, 0);
//    byte buffOrder[] = intent.getByteArrayExtra(PARAM_ORDER_BUFF);
//    int buffOrderLength = intent.getIntExtra(PARAM_ORDER_BUFF_LENGTH, 0);
//    int function = intent.getIntExtra(PARAM_FUNCTION, 0);
//    int idPacket = intent.getIntExtra(PARAM_ID_PACKET, 0);
//    String owner = intent.getStringExtra(PARAM_OWNER);
//    Log.i("onStartCommand", "owner = " + owner);
//    Log.i("buffOrder", "0x" + myLib.byteArrayToHexString(buffOrder, buffOrderLength));

    // Создание объекта сервиса 
//    MyRun mr = new MyRun(startId, owner, function, task, buffOrder, buffOrderLength, idPacket);
//    MyRun mr = new MyRun(time, startId, pi);
    // Запуск сервиса 
//    es.execute(mr);
    return super.onStartCommand(intent, flags, startId);
  }
  
  boolean cancelTask(Messaging mess) {
		boolean	res = true;
		Log.d(LOG_TAG, "cancelTask");
		if (mess.isAlive()) {
			mess.interrupt();
		}
		return res;
  }
  
  
  public static boolean setConnection() {
	  
//	Timer timer = new Timer("ConnectionTimerTimeout");
//	connection = new prepareConnection(mHandlerConnect, receiveName, owner, task, timer);
//	  
//	// ---------------------------------- Запускаю таймер времени работы UDT-клиента  
//	TimerTask taskConnect = new PTSMaster_ConnectionTimerTimeout(mHandlerConnect, connection);
//	// 
//	connection.start();
//	timer.schedule( taskConnect, 15000 );
//	return connection;
	  
	if ((!connectionState.equals("ConnectOK")) || (PTSMasterService.channel == null) || (!PTSMasterService.channel.isConnected())) {
		Log.i(LOG_TAG, "setConnection: No connection to server.");
		Timer timer = new Timer("ConnectionTimerTimeout");
//		connection = new prepareConnection(mHandlerConnect, receiveName, owner, task, timer);
		connection = new prepareConnection(mHandlerConnect, timer);
		  
		// ---------------------------------- Запускаю таймер времени работы UDT-клиента  
		TimerTask taskConnect = new PTSMaster_ConnectionTimerTimeout(mHandlerConnect, connection);
		// 
		connectionState = "unknown";
		connection.start();
		timer.schedule( taskConnect, 15000 );
	    try {
				Log.d(LOG_TAG, "...");
				while (true) {
					if (connectionState.equals("ConnectOK")) {
						timer.cancel();
						Log.v(LOG_TAG, "Conection success");
						break;
					}
					if (connectionState.equals("noConnection")) {
						Log.e(LOG_TAG, "Connection failure");
						return false;
					}
					Thread.sleep(1000);
					Log.i(LOG_TAG, "wait connection ... ");
				}
	        // Do some stuff
	      } 
	      catch (Exception e) {
				Log.e("Exception", "e=" + e);
				return false;
	      }
	}
	return true;
  }
  
  Messaging sendPacket(String receiveName, String owner, int function, int task, int idPacket, byte buffOrder[], int buffOrderLength) {
	  
	  int requestMessage;
//	boolean	res = true;
//    Log.i("sendPacket", "--> 0x" + myLib.byteArrayToHexString(buffOrder, buffOrderLength));
	  
//	  Log.i("sendPacket", "connectionState: " + connectionState);
//	  Log.i("sendPacket", "channel: " + PTSMasterService.channel);
//	  if (PTSMasterService.channel != null) {
//		  Log.i("sendPacket", "channel state: " + PTSMasterService.channel.isConnected());
//	  }
	  
	  if (!setConnection()) {
			// -------------------------------- Ответ 
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_NOCONNECT);
			b.putString(PTSMasterService.PARAM_OWNER, owner);
			b.putInt(PTSMasterService.PARAM_TASK, task);
			b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
			msg.setData(b);
			mHandler.sendMessage(msg);

		  return null;
	  }
	  
//		if ((connectionState.equals("noConnection")) || (PTSMasterService.channel == null) || (!PTSMasterService.channel.isConnected())) {
//			Log.i("sendPacket", "No connection to server.");
//			Timer timer = new Timer("ConnectionTimerTimeout");
//			connection = new prepareConnection(mHandlerConnect, receiveName, owner, task, timer);
//			  
//			// ---------------------------------- Запускаю таймер времени работы UDT-клиента  
//			TimerTask taskConnect = new PTSMaster_ConnectionTimerTimeout(mHandlerConnect, connection);
//			// 
//			connection.start();
//			timer.schedule( taskConnect, 15000 );
//		    try {
//					Log.d("wait", "...");
//					while (true) {
//						if (connectionState.equals("ConnectOK")) {
//							Log.e(LOG_TAG, "Conection success");
//							break;
//						}
//						if (connectionState.equals("connect timeout")) {
//							connectionState = "noConnection";
//							Log.e(LOG_TAG, "TIMEOUT Conection process");
//							return null;
//						}
//						Thread.sleep(1000);
//						Log.i(LOG_TAG, "wait connection ... " + owner);
//					}
//		        // Do some stuff
//		      } 
//		      catch (Exception e) {
//					Log.e("Exception", "e=" + e);
//					return null;
//		      }
//		}
	  
	  if (function == FUNC_POSCLIENT) {
		  requestMessage = funcPTSMaster.REGISTER_MSG;
	  }
	  else {
		  requestMessage = funcPTSMaster.REQUEST_MSG;
	  }
	  
	Timer timer = new Timer("PTSMaster_TimerTimeout" + idPacket);
	// ---------------------------------- Активизирую связь с сервером  
	messagingClient = new Messaging(mHandler, receiveName, owner, function, task, idPacket, buffOrder, buffOrderLength, requestMessage, timer);
	
	if (function == FUNC_POSCLIENT) {
		messagingClient.start();
	}
	else {
		// ---------------------------------- Запускаю таймер времени работы UDT-клиента  
		TimerTask taskSend = new PTSMaster_TimerTimeout(mHandler, messagingClient);
		
	//	  idPacketList.add(idPacket);
		  receiveSemaphor.add(new ArrayList_ReceiveSemaphor(owner, task, function, idPacket, receiveName, timer)); //( String owner, int task, int function, int idPacket, String receiveName);
		  Log.d(LOG_TAG, "receiveSemaphor size: " + receiveSemaphor.size());
		  readThreadClient.setSemaphor(receiveSemaphor);
		// Устанавливаю время ожидания соединения (в милисекундах) 
		messagingClient.start();
		timer.schedule( taskSend, 15000 );
	}

//    try {
//			Log.d("wait", "...");
//			while (messagingClient.isAlive()) {
//				Thread.sleep(1000);
//				Log.i(LOG_TAG, "wait sendPacket ... " + owner);
//			}
//			Log.d("wake up", "!!!");
//        // Do some stuff
//      } 
//      catch (Exception e) {
//			Log.e("Exception", "e=" + e);
//      }

//    int y = 10;
//    while(y > 0) {
//		Log.i(LOG_TAG, "wait sendPacket ... " + owner);
//		y--;
//    	try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    }
	return messagingClient;
  }

//  long downInterval(long gap) {
//    interval = interval - gap;
//    if (interval < 0) interval = 0;
//    return interval;
//  }
  
  // #####################################################################################################
  //	Задача периодического опроса состояния подключения  
  // #####################################################################################################
  public void checkConnection() {
  	
		timerCheckConnection = new Timer("TimerCheckConnection");
		TimerTask task = new TimerTask() { // Определяем задачу
			@Override
			public void run() {

				Log.v(LOG_TAG, "<<< RUN CHECK CONNECTION >>>");
		        setConnection();

			};
		};
      // Устанавливаю время: через сколько будет запущена задача task (в милисекундах) 
		timerCheckConnection.schedule( task, 0, 5000 );
  }
  
  

  public IBinder onBind(Intent arg0) {
    Log.d(LOG_TAG, "MyService onBind");
    return new MyBinder();
//    return binder;
  }
  
  public boolean onUnbind(Intent intent) {
    Log.d(LOG_TAG, "MyService onUnbind");
    return true;
  }

  class MyBinder extends Binder {
	  PTSMasterService getService() {
		  return PTSMasterService.this;
	  }
  }  
 
  Handler mHandler = new Handler() {
	  String owner;
	  int task;
	  int function;
	  int idPacket;
	  int resultThread;
	  String receiveName;
	  
      @Override
      public void handleMessage(Message msg) {
    		int i;
    		boolean idPacketFinded = false;
    		if (msg.getData().containsKey(PARAM_THREADRESULT)) {
    			resultThread = msg.getData().getInt(PARAM_THREADRESULT);
				if (resultThread == funcPTSMaster.STATUS_IS_DATA) {
	    			task = msg.getData().getInt(PARAM_TASK);
	    			function = msg.getData().getInt(PARAM_FUNCTION);
	    			owner = msg.getData().getString(PARAM_OWNER);
	    			receiveName = msg.getData().getString(PARAM_RECEIVERNAME);
	    			idPacket = msg.getData().getInt(PARAM_ID_PACKET);
	    			Intent intent = new Intent(receiveName);
					// --- Данные приняты
					int ptsMasterOffset = 0;

					byte buffResult[] = msg.getData().getByteArray(PTSMasterService.PARAM_RESULT_BUFF);
					
					if (buffResult == null) {
						Log.e(LOG_TAG, "Parsing - NULL");
						// сообщаем о пакете без данных 
						intent.putExtra(PARAM_TASK, task);
						intent.putExtra(PARAM_OWNER, owner);
						intent.putExtra(PARAM_FUNCTION, function);
						intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
						intent.putExtra(PARAM_TASKRESULT, RESULT_NULL);
						sendBroadcast(intent);
					}
					else {
//        					Log.d(LOG_TAG, "Принят пакет: " + myLib.byteArrayToHexString(buffResult, buffResult.length));
						// ========================================================================================================================================================
						//																	ПАРСИНГ 
						// ========================================================================================================================================================
						// сообщаем об успехе 
						intent.putExtra(PARAM_TASK, task);
						intent.putExtra(PARAM_OWNER, owner);
						intent.putExtra(PARAM_FUNCTION, function);
						intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
						intent.putExtra(PARAM_TASKRESULT, RESULT_SUCCESS);
						intent.putExtra(PARAM_RESULT_BUFF, buffResult);
						intent.putExtra(PARAM_RESULT_BUFF_LENGTH, buffResult.length);
						sendBroadcast(intent);
					}
				}
				if (resultThread == funcPTSMaster.STATUS_IS_SIGNAL) {
					Log.i(LOG_TAG, "Parsing - STATUS_IS_SIGNAL");
					byte buffResult[] = msg.getData().getByteArray(PTSMasterService.PARAM_RESULT_BUFF);
	    			task = msg.getData().getInt(PARAM_TASK);
	    			Intent intent = new Intent(PTSTerminal.BC_PTSTERMINAL);
					// сообщаем о таймауте 
					intent.putExtra(PARAM_TASK, task);
					intent.putExtra(PARAM_RESULT_BUFF, buffResult);
					intent.putExtra(PARAM_RESULT_BUFF_LENGTH, buffResult.length);
					sendBroadcast(intent);
				}
				if (resultThread == funcPTSMaster.STATUS_DAMAGED) {
					Log.e(LOG_TAG, "Parsing - STATUS_DAMAGED");
//    					// сообщаем о неправильном пакете  
//    					intent.putExtra(PARAM_TASK, task);
//    					intent.putExtra(PARAM_OWNER, owner);
//    					intent.putExtra(PARAM_FUNCTION, function);
//    					intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//    					intent.putExtra(PARAM_TASKRESULT, RESULT_DAMAGED);
//    					sendBroadcast(intent);
				}
				if (resultThread == funcPTSMaster.STATUS_TIMEOUT) {
	    			task = msg.getData().getInt(PARAM_TASK);
	    			function = msg.getData().getInt(PARAM_FUNCTION);
	    			owner = msg.getData().getString(PARAM_OWNER);
	    			receiveName = msg.getData().getString(PARAM_RECEIVERNAME);
	    			idPacket = msg.getData().getInt(PARAM_ID_PACKET);
	    			Intent intent = new Intent(receiveName);
					Log.e(LOG_TAG, "Parsing - STATUS_TIMEOUT");
//    	        		Log.e(LOG_TAG, "Parsing - STATUS_TIMEOUT");
					// сообщаем о таймауте 
					intent.putExtra(PARAM_TASK, task);
					intent.putExtra(PARAM_OWNER, owner);
					intent.putExtra(PARAM_FUNCTION, function);
					intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
					intent.putExtra(PARAM_TASKRESULT, RESULT_TIMEOUT);
					sendBroadcast(intent);
					for (int z = 0; z < receiveSemaphor.size(); z++) {
						if (receiveSemaphor.get(z).idPacket == idPacket)
						{
							Log.d(LOG_TAG, "<TIMEOUT> Удаляю семафор. idPacket = " + idPacket);
							receiveSemaphor.remove(z);
							break;
						}
					}
				}
				if (resultThread == funcPTSMaster.STATUS_NOCONNECT) {
	    			task = msg.getData().getInt(PARAM_TASK);
	    			function = msg.getData().getInt(PARAM_FUNCTION);
	    			owner = msg.getData().getString(PARAM_OWNER);
	    			receiveName = msg.getData().getString(PARAM_RECEIVERNAME);
	    			idPacket = msg.getData().getInt(PARAM_ID_PACKET);
	    			Intent intent = new Intent(receiveName);
					Log.e(LOG_TAG, "Parsing - STATUS_NOCONNECT");
					connectionState = "noConnection";
//    	         		Log.e(LOG_TAG, "Parsing - STATUS_NOCONNECT");
					// сообщаем о NOCONNECT 
					intent.putExtra(PARAM_TASK, task);
					intent.putExtra(PARAM_OWNER, owner);
					intent.putExtra(PARAM_FUNCTION, function);
					intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
					intent.putExtra(PARAM_TASKRESULT, RESULT_NOCONNECT);
					sendBroadcast(intent);
				}
				if (resultThread == funcPTSMaster.STATUS_CONECTIONLOST) {
	    			task = msg.getData().getInt(PARAM_TASK);
	    			function = msg.getData().getInt(PARAM_FUNCTION);
	    			owner = msg.getData().getString(PARAM_OWNER);
	    			receiveName = msg.getData().getString(PARAM_RECEIVERNAME);
	    			idPacket = msg.getData().getInt(PARAM_ID_PACKET);
	    			Intent intent = new Intent(receiveName);
					Log.e(LOG_TAG, "Parsing - STATUS_CONECTIONLOST");
					connectionState = "noConnection";
//    					if (!readThreadClient.isAlive()) {
//    						readThreadClient.start();
//    					}
////    	         		Log.e(LOG_TAG, "Parsing - STATUS_NOCONNECT");
//    					// сообщаем о NOCONNECT 
//    					intent.putExtra(PARAM_TASK, task);
//    					intent.putExtra(PARAM_OWNER, owner);
//    					intent.putExtra(PARAM_FUNCTION, function);
//    					intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//    					intent.putExtra(PARAM_TASKRESULT, RESULT_NOCONNECT);
//    					sendBroadcast(intent);
				}
				if (resultThread == 185) {
	    			task = msg.getData().getInt(PARAM_TASK);
	    			function = msg.getData().getInt(PARAM_FUNCTION);
	    			owner = msg.getData().getString(PARAM_OWNER);
	    			receiveName = msg.getData().getString(PARAM_RECEIVERNAME);
	    			idPacket = msg.getData().getInt(PARAM_ID_PACKET);
	    			Intent intent = new Intent(receiveName);
					Log.e(LOG_TAG, "TimerTask-run");
//    	        		Toast.makeText(getApplicationContext(), "TimerTask-run", Toast.LENGTH_SHORT).show();
				}
//    			else {
//    				Log.w(LOG_TAG, "По этому пакету уже ответ был выслан ранее: idPacket = " + idPacket);
//    			}
    		}
    	}

  };

  static Handler mHandlerConnect = new Handler() {
	  String owner;
	  int task;
	  int resultThread;
	  String receiveName;
	  
      @Override
      public void handleMessage(Message msg) {
    		int i;
    		boolean idPacketFinded = false;
    		if (msg.getData().containsKey(PARAM_THREADRESULT)) {
    			resultThread = msg.getData().getInt(PARAM_THREADRESULT);
//    			receiveName = msg.getData().getString(PARAM_RECEIVERNAME);
//    			task = msg.getData().getInt(PARAM_TASK);
//    			owner = msg.getData().getString(PARAM_OWNER);
//    			Intent intent = new Intent(receiveName);

				if (resultThread == funcPTSMaster.STATUS_CONNECTOK) {
					Log.i(LOG_TAG, "Connection - <<<OK>>>");
					connectionState = "ConnectOK";
//				    readThreadClient.start();
//					// сообщаем о таймауте 
//					intent.putExtra(PARAM_TASK, task);
//					intent.putExtra(PARAM_OWNER, owner);
//					intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//					intent.putExtra(PARAM_TASKRESULT, RESULT_SUCCESS);
//					sendBroadcast(intent);
				}
				if (resultThread == funcPTSMaster.STATUS_TIMEOUT) {
					Log.e(LOG_TAG, "Connection - STATUS_TIMEOUT");
					connectionState = "noConnection";
//					connectionState = "connect timeout";
//    	        		Log.e(LOG_TAG, "Parsing - STATUS_TIMEOUT");
//					// сообщаем о таймауте 
//					intent.putExtra(PARAM_TASK, task);
//					intent.putExtra(PARAM_OWNER, owner);
//					intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//					intent.putExtra(PARAM_TASKRESULT, RESULT_TIMEOUT);
//					sendBroadcast(intent);
				}
				if (resultThread == funcPTSMaster.STATUS_NOCONNECT) {
					Log.e(LOG_TAG, "Connection - STATUS_NOCONNECT");
					connectionState = "noConnection";
//					// сообщаем о NOCONNECT 
//					intent.putExtra(PARAM_TASK, task);
//					intent.putExtra(PARAM_OWNER, owner);
//					intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//					intent.putExtra(PARAM_TASKRESULT, RESULT_NOCONNECT);
//					sendBroadcast(intent);
				}
    		}
    	}
  };

//  class MyRun implements Runnable {
//    
//	  String owner;
//    int task;
//    int startId;
////    PendingIntent pi;
//    int function;
//    byte buffOrder[] = new byte[20*1024];
//    int buffOrderLength;
//    int idPacket;
//
//    public MyRun(int startId, String owner, int func, int task, byte buff[], int buffLength, int idPacket) {
//      this.startId = startId;
//      this.function = func;
//      this.task = task;
//      this.buffOrderLength = buffLength;
//      this.owner = owner;
//      this.idPacket = idPacket;
//      System.arraycopy(buff, 0, buffOrder, 0, buffLength);
//
//      Log.d(LOG_TAG, "MyRun#" + startId + " create");
//    }
//    
//	  Handler mHandler = new Handler() {
//          @Override
//          public void handleMessage(Message msg) {
//	        Intent intent = new Intent(PTSTerminal.BC_PTSTERMINAL);
//          	if (msg.getData().containsKey("ptsMasterAnswer")) {
////          		messagingClient.interrupt();
////                timer.cancel();
//                int result = msg.getData().getInt("ptsMasterAnswer");
//                if (result == funcPTSMaster.STATUS_IS_DATA){
//	                // --- Данные приняты
//                	int ptsMasterOffset = 0;
//                	if (msg.getData().getByteArray(PTSMasterService.PARAM_RESULT_BUFF) == null) {
//                		Log.e("Parsing", "NULL");
//                		// сообщаем о пакете без данных 
//                        intent.putExtra(PARAM_TASK, task);
//                        intent.putExtra(PARAM_OWNER, owner);
//                        intent.putExtra(PARAM_FUNCTION, function);
//                        intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//                        intent.putExtra(PARAM_TASKRESULT, RESULT_NULL);
//                        sendBroadcast(intent);
//                	}
//                	else {
//						// ========================================================================================================================================================
//						//																	ПАРСИНГ 
//						// ========================================================================================================================================================
//		                byte ptsMasterData[] = msg.getData().getByteArray(PTSMasterService.PARAM_RESULT_BUFF);
//                		// сообщаем об успехе 
//                        intent.putExtra(PARAM_TASK, task);
//                        intent.putExtra(PARAM_OWNER, owner);
//                        intent.putExtra(PARAM_FUNCTION, function);
//                        intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//                        intent.putExtra(PARAM_TASKRESULT, RESULT_SUCCESS);
//                        intent.putExtra(PARAM_RESULT_BUFF, ptsMasterData);
//                        intent.putExtra(PARAM_RESULT_BUFF_LENGTH, ptsMasterData.length);
//                        sendBroadcast(intent);
//                	}
//                }
//                if (result == funcPTSMaster.STATUS_DAMAGED){
//                	Log.e("Parsing", "PTSMasterService - DAMAGED");
//            		// сообщаем о неправильном пакете  
//                    intent.putExtra(PARAM_TASK, task);
//                    intent.putExtra(PARAM_OWNER, owner);
//                    intent.putExtra(PARAM_FUNCTION, function);
//                    intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//                    intent.putExtra(PARAM_TASKRESULT, RESULT_DAMAGED);
//                    sendBroadcast(intent);
//                }
//          	}
//          	if (msg.getData().containsKey("timer")) {
//	                String otal = msg.getData().getString("timer");
//		            if (otal.equals("Timeout")) {
//                		Log.e("timer", "Task is timeout");
//                		// сообщаем о таймауте 
//                        intent.putExtra(PARAM_TASK, task);
//                        intent.putExtra(PARAM_OWNER, owner);
//                        intent.putExtra(PARAM_FUNCTION, function);
//                        intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//                        intent.putExtra(PARAM_TASKRESULT, RESULT_TIMEOUT);
//                        sendBroadcast(intent);
//	                }
//          	}
//          	if (msg.getData().containsKey("dialog")) {
//                String otal = msg.getData().getString("dialog");
//	            if (otal.equals("cancel")) {
//                    Toast.makeText(getApplicationContext(), "dialog is cancel", Toast.LENGTH_SHORT).show();
//            		Log.e("Handler", "dialog is cancel");
//            		// сообщаем о таймауте 
//                    intent.putExtra(PARAM_TASK, task);
//                    intent.putExtra(PARAM_OWNER, owner);
//                    intent.putExtra(PARAM_FUNCTION, function);
//                    intent.putExtra(PARAM_SERVICESTATUS, STATUS_IN_WORK);
//                    intent.putExtra(PARAM_TASKRESULT, RESULT_CANCEL);
//                    sendBroadcast(intent);
//                }
//          	}
//          }
//      };

      
//    public void runServerTransaction() {
////      Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
//        Intent intent = new Intent("PTSMasterService");
//        Log.d(LOG_TAG, "MyRun#" + startId );
////      try {
////        TimeUnit.SECONDS.sleep(time);
//    	  
//        // сообщаем об старте задачи
//        intent.putExtra(PARAM_TASK, task);
//        intent.putExtra(PARAM_SERVICESTATUS, STATUS_START);
//        sendBroadcast(intent);
//          
//        
//		String deviceId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);	             
//		byte buff[] = new byte[20*1024];
//		
//		int fileId = 0;
//		Log.i("deviceId", deviceId);
//		// ---------------------------------- Активизирую связь с сервером  
//		messagingClient = new Messaging(mHandler, function, buffOrder, buffOrderLength, idPacket, funcPTSMaster.REQUEST_MSG);
//		messagingClient.start();
//		
//		// ---------------------------------- Запускаю таймер времени работы UDT-клиента  
//		timer = new Timer("PTSMaster_TimerTimeout");
//		TimerTask task = new PTSMaster_TimerTimeout(mHandler);
//		// Устанавливаю время ожидания соединения (в милисекундах) 
//		timer.schedule( task, 15000 );
//		
//      try {
//			Log.d("wait", "...");
//			while (messagingClient.isAlive()) {
//				Thread.sleep(1000);
//				Log.w("<->", "in progress..." + startId);
//			}
//			Log.d("wake up", "!!!");
//          // Do some stuff
//        } 
//        catch (Exception e) {
//			Log.e("Exception", "e=" + e);
//        }
//
//        // сообщаем об окончании задачи
//        intent.putExtra(PARAM_SERVICESTATUS, STATUS_FINISH);
//        sendBroadcast(intent);
//        
//      stop();
//    }
    
//    void stop() {
//        Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult("
//            + startId + ") = " + stopSelfResult(startId));
//    }
}




///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class PTSMaster_TimerTimeout extends TimerTask
{
    Handler mHandler;
    Messaging message;

	public PTSMaster_TimerTimeout(Handler h, Messaging mess)
	{  
       mHandler = h;
       message = mess;
	}
	@Override
	public void run() {
		Log.d("PTSMaster_TimerTimeout", "TIMEOUT");
		// -------------------------------- Ответ 
        Message msg = mHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_TIMEOUT);
        b.putInt(PTSMasterService.PARAM_ID_PACKET, message.idPacket);
        b.putString(PTSMasterService.PARAM_RECEIVERNAME, message.receiveName);
        b.putString(PTSMasterService.PARAM_OWNER, message.owner);
        b.putInt(PTSMasterService.PARAM_TASK, message.task);
        b.putInt(PTSMasterService.PARAM_FUNCTION, message.function);
        msg.setData(b);
        mHandler.sendMessage(msg);
	}
}

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class PTSMaster_ConnectionTimerTimeout extends TimerTask
{
    Handler mHandler;
    prepareConnection connectionPTR;

	public PTSMaster_ConnectionTimerTimeout(Handler h, prepareConnection ptr)
	{  
       mHandler = h;
       connectionPTR = ptr;
	}
	@Override
	public void run() {
		Log.d("PTSMaster_ConnectionTimerTimeout", "TIMEOUT");
		if (connectionPTR.isAlive()) {
			// -------------------------------- Ответ 
            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_TIMEOUT);
//            b.putString(PTSMasterService.PARAM_OWNER, connectionPTR.owner);
//            b.putInt(PTSMasterService.PARAM_TASK, connectionPTR.task);
//            b.putString(PTSMasterService.PARAM_RECEIVERNAME, connectionPTR.receiveName);
            msg.setData(b);
            mHandler.sendMessage(msg);
            connectionPTR.interrupt();
		}
	}
}


///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class prepareConnection extends Thread
{ 
    Handler mHandler;
	Timer timer;
	String receiveName;
	String owner;
	int task;

	public prepareConnection(Handler h, String receiveName, String owner, int task, Timer timer) {
		this.mHandler = h;
		this.timer = timer;
		this.receiveName = receiveName;
		this.owner = owner;
		this.task = task;
	}
	
	public prepareConnection(Handler h, Timer timer) {
		this.mHandler = h;
		this.timer = timer;
	}
	
	public void run()
	{  
		try {
			PTSMasterService.channel = SocketChannel.open(new InetSocketAddress(PTSMasterService.serverIP, PTSMasterService.serverPORT));
			
			if (this.timer != null) {
				this.timer.cancel();
			}
			// -------------------------------- Ответ 
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_CONNECTOK);
//			b.putString(PTSMasterService.PARAM_OWNER, owner);
//			b.putInt(PTSMasterService.PARAM_TASK, task);
//			b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
			msg.setData(b);
			mHandler.sendMessage(msg);
			Log.i("prepareConnection", "CONNECTION...[OK]\n");
		}
		catch (IOException e) {
			Log.w("prepareConnection", "IOException = " + e + "\n");
			if (this.timer != null) {
				this.timer.cancel();
			}
			// -------------------------------- Ответ 
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_NOCONNECT);
//			b.putString(PTSMasterService.PARAM_OWNER, owner);
//			b.putInt(PTSMasterService.PARAM_TASK, task);
//			b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
			msg.setData(b);
			mHandler.sendMessage(msg);
		}
		finally {
//			PTSMasterService.channel.close();
			Log.i("prepareConnection", "Close socket");
		}
	}

}

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class Messaging extends Thread
{  
	/**
		Создание потока-фильтра
	   @param is the output stream
	   @param os the output stream
	*/
    Handler mHandler;
    byte buff[];
    int length;
    byte buffParam[];
    int buffParamLength;
//    int id;
    int requestType;
	String funcName;
    public funcPTSMaster_result	res;
    
	  String owner;
	  int task;
	  int function;
	  int idPacket;
	  String receiveName;
	  Timer timer;

	public Messaging(Handler h, String receiveName, String owner, int function, int task, int idPacket, byte buffOrder[], int buffOrderLength, int request, Timer timer)
	{  
		this.mHandler = h;
		this.buffParam = buffOrder;
		this.buffParamLength = buffOrderLength;
		this.idPacket = idPacket;
		this.requestType = request;
		this.function = function;
		this.owner = owner;
		this.receiveName = receiveName;
		this.task = task;
		this.timer = timer;

	}
	
	public void run()
	{  
//		String connectionState = "noConnect";
//		try {
////		  		SocketChannel channel = SocketChannel.open(new InetSocketAddress("192.168.1.13", 29999));
////				SocketChannel channel = SocketChannel.open(new InetSocketAddress("82.207.108.70", 47113));
////		  		SocketChannel channel = SocketChannel.open(new InetSocketAddress("192.168.1.35", 29999));
////		  		SocketChannel channel = SocketChannel.open(new InetSocketAddress("192.168.1.35", 47110));
//			SocketChannel channel = SocketChannel.open(new InetSocketAddress("192.168.1.90", 29999));
		try {
			
			if ((PTSMasterService.channel != null) && (PTSMasterService.channel.isConnected())) {
				Log.i("Messaging", "IS CONECTED");
//				connectionState = "ConnectOK";
				byte[] bytes = new byte[2*1024];
				ByteBuffer bufOut = ByteBuffer.wrap(bytes);
	
				// =========================================
				byte buff[] = new byte[20*1024];
				int length = 0; 
	
				// ---------------------------------- Добавляю заголовок  
				System.arraycopy(funcPTSMaster.PACKAGE_SIGN, 0, buff, 0, 4);
				length += 4;
				buff[length++] = (byte) requestType;
				
				if (requestType == funcPTSMaster.REGISTER_MSG) {
					System.arraycopy(funcPTSMaster.PACKAGE_POSCLIENT, 0, buff, length, funcPTSMaster.PACKAGE_POSCLIENT.length);
					length += funcPTSMaster.PACKAGE_POSCLIENT.length;
				}
				else {
					System.arraycopy(myLib.intToByteArray(idPacket), 0, buff, length, 4);
					length += 4;
		
					// ---------------------------------- Добавляю поле с названием функции   
					switch (function) {
						case PTSMasterService.FUNC_CLERCLIST:
							funcName = "clerkList";
							break;
						case PTSMasterService.FUNC_CLERCREG:
							funcName = "clerkReg";
							break;
						case PTSMasterService.FUNC_DISPENSERGET:
							funcName = "dispenserGet";
							break;
						case PTSMasterService.FUNC_BASKETLIST:
							funcName = "basketList";
							break;
						case PTSMasterService.FUNC_BASKETGET:
							funcName = "basketGet";
							break;
						case PTSMasterService.FUNC_PLUGET:
							funcName = "pluGet";
							break;
						case PTSMasterService.FUNC_BASKETADD:
							funcName = "basketAdd";
							break;
						case PTSMasterService.FUNC_PUMPRESUME:
							funcName = "pumpResume";
							break;
						case PTSMasterService.FUNC_PUMPSTOP:
							funcName = "pumpStop";
							break;
						case PTSMasterService.FUNC_BASKETCLOSE:
							funcName = "basketClose";
							break;
						case PTSMasterService.FUNC_BASKETCLEAR:
							funcName = "basketClear";
							break;
						case PTSMasterService.FUNC_BASKETPAYMENT:
							funcName = "basketPayment";
							break;
						case PTSMasterService.FUNC_STOREGET:
							funcName = "storeGet";
							break;
						case PTSMasterService.FUNC_POSCLIENT:
							funcName = "posClient";
							break;
						case PTSMasterService.FUNC_VERSION:
							funcName = "version";
							break;
						case PTSMasterService.FUNC_PUMPSTATE:
							funcName = "pumpState";
							break;
						case PTSMasterService.FUNC_PAYFORMLIST:
							funcName = "payFormList";
							break;
						default:
							funcName = "close";
							break;
					}
					length += funcPTSMaster.prepareFunctionName(buff, "posServer", funcName, length);
		
					// ---------------------------------- Добавляю тело сообщения  
					System.arraycopy(buffParam, 0, buff, length, buffParamLength);
					length += buffParamLength;
				}
	
				// =========================================
	
				// отправляем данные 
//			      Log.i("Messaging", "--> 0x" + myLib.byteArrayToHexString(buff, length));
	
				bufOut.put(buff, 0, length);
				bufOut.position(0);
				bufOut.limit(length);
				PTSMasterService.channel.write(bufOut);

//			int offsetAppend = 0;
//			int offsetbufStream = 0;
////	        	byte[] byteStream = new byte[20*1024];
//			byte[] bufStream = new byte[100*1024];
////		        StringBuffer strBuff = new StringBuffer(2*1024);
//			while (true) {
//				byte[] bytess = new byte[100*1024];
//				int offsetCmp = 0;
//				ByteBuffer buf = ByteBuffer.wrap(bytess);
//
//				// жду ответ
//				int size = PTSMasterService.channel.read(buf);
////		            Log.i("Messaging", "<-- 0x" + myLib.byteArrayToHexString(buf.array(), size));
//				if (size < 0) {
//					timer.cancel();
//					Log.e("Packet", "size < 0");
////					// -------------------------------- Ответ 
////					Message msg = mHandler.obtainMessage();
////					Bundle b = new Bundle();
////					b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_DAMAGED);
////					b.putInt(PTSMasterService.PARAM_ID_PACKET, idPacket);
////					b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
////					b.putString(PTSMasterService.PARAM_OWNER, owner);
////					b.putInt(PTSMasterService.PARAM_TASK, task);
////					b.putInt(PTSMasterService.PARAM_FUNCTION, function);
////					msg.setData(b);
////					mHandler.sendMessage(msg);
//					break;
//				}
//
//				//--Добавляю полученные данные в контейнер для обработки
//				System.arraycopy(buf.array(), 0, bufStream, offsetAppend, size);
//				offsetAppend += size;
//
//				//--Проверяем цельность пакета и парсим полученные данные
//				res = funcPTSMaster.collectPackage(bufStream, offsetAppend, idPacket);
//				if (res.result == funcPTSMaster.COLLECT_READY) {
//					timer.cancel();
////			            Log.i("Packet", "Пакет принят полностью");
//					// -------------------------------- Ответ 
//					Message msg = mHandler.obtainMessage();
//					Bundle b = new Bundle();
//					b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_IS_DATA);
//					b.putInt(PTSMasterService.PARAM_ID_PACKET, idPacket);
//					b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
//					b.putString(PTSMasterService.PARAM_OWNER, owner);
//					b.putInt(PTSMasterService.PARAM_TASK, task);
//					b.putInt(PTSMasterService.PARAM_FUNCTION, function);
//					b.putByteArray(PTSMasterService.PARAM_RESULT_BUFF, res.packetData);
////			            b.putByteArray("ptsMasterData", res.packetData);
////			            Log.i("Ответ", myLib.byteArrayToHexString(res.packetData, res.packetData.length));
////			            b.putInt("ptsMasterDataLength", funcPTSMaster.STATUS_IS_DATA);
//					msg.setData(b);
//					mHandler.sendMessage(msg);
//					break;
//				}
//				if (res.result == funcPTSMaster.COLLECT_DAMAGED) {
//					timer.cancel();
//					Log.e("Packet", "DAMAGED");
//					// -------------------------------- Ответ 
//					Message msg = mHandler.obtainMessage();
//					Bundle b = new Bundle();
//					b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_DAMAGED);
//					b.putInt(PTSMasterService.PARAM_ID_PACKET, idPacket);
//					b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
//					b.putString(PTSMasterService.PARAM_OWNER, owner);
//					b.putInt(PTSMasterService.PARAM_TASK, task);
//					b.putInt(PTSMasterService.PARAM_FUNCTION, function);
//					msg.setData(b);
//					mHandler.sendMessage(msg);
//					break;
//				}
//				if (res.result == funcPTSMaster.COLLECT_WAIT_FRAGMENT) {
//					offsetbufStream += res.packetLength;
//					Log.i("Packet", "COLLECT_WAIT_FRAGMENT: " + String.valueOf(offsetbufStream));
//					continue;
//				}
//
//				Thread.sleep(100);
//			}            
			}
			else {
				timer.cancel();
				Log.e("Messaging", "NO CONECTION!!!");
				// -------------------------------- Ответ 
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_NOCONNECT);
//				b.putInt(PTSMasterService.PARAM_ID_PACKET, idPacket);
//				b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
//				b.putString(PTSMasterService.PARAM_OWNER, owner);
//				b.putInt(PTSMasterService.PARAM_TASK, task);
//				b.putInt(PTSMasterService.PARAM_FUNCTION, function);
				msg.setData(b);
				mHandler.sendMessage(msg);
				return;
			}
		}
		catch (IOException e) {
			Log.w("WarningIOException", "IOException = " + e + "\n");
	
//				if (connectionState.equals("noConnect")) {
//					// -------------------------------- Ответ 
//					Message msg = mHandler.obtainMessage();
//					Bundle b = new Bundle();
//					b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_NOCONNECT);
//					b.putInt(PTSMasterService.PARAM_ID_PACKET, idPacket);
//					b.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
//					b.putString(PTSMasterService.PARAM_OWNER, owner);
//					b.putInt(PTSMasterService.PARAM_TASK, task);
//					b.putInt(PTSMasterService.PARAM_FUNCTION, function);
//					msg.setData(b);
//					mHandler.sendMessage(msg);
//				}
	          
	          
	          
//			        String err = "Error = " + e;
//					// -------------------------------- Ответ 
//			        Message msg3 = mHandler.obtainMessage();
//			        Bundle k= new Bundle();
//			        k.putInt(PTSMasterService.PARAM_SERVICESTATUS, 186);
//			        k.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
//			        k.putString(PTSMasterService.PARAM_OWNER, err);
//			        k.putInt(PTSMasterService.PARAM_TASK, task);
//			        k.putInt(PTSMasterService.PARAM_FUNCTION, function);
//			        msg3.setData(k);
//			        mHandler.sendMessage(msg3);
		}
//		catch (InterruptedException e) {
////			        String err2 = "Error = " + e;
////					// -------------------------------- Ответ 
////			        Message msg2 = mHandler.obtainMessage();
////			        Bundle k= new Bundle();
////			        k.putInt(PTSMasterService.PARAM_SERVICESTATUS, 187);
////			        k.putString(PTSMasterService.PARAM_RECEIVERNAME, receiveName);
////			        k.putString(PTSMasterService.PARAM_OWNER, err2);
////			        k.putInt(PTSMasterService.PARAM_TASK, task);
////			        k.putInt(PTSMasterService.PARAM_FUNCTION, function);
////			        msg2.setData(k);
////			        mHandler.sendMessage(msg2);
//			Log.w("WarningInterruptedException", "InterruptedException" + e + "\n");
//		}
		finally {
//			PTSMasterService.channel.close();
			Log.i("MessagingThread", "Socket closed\n");
		}
	}
//	private DataInputStream in;
//	private DataOutputStream out;
//    private String resume;

}

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class ReadThread extends Thread
{  
	/**
		Создание потока-фильтра
	   @param is the output stream
	   @param os the output stream
	*/
	final String LOG_TAG = "PTSMasterService[ReadThread]";

    Handler mHandler;
    byte buff[];
    int length;
    byte buffParam[];
    int buffParamLength;
//    int id;
    int requestType;
	String funcName;
    public funcPTSMaster_result	res;
    
    ArrayList<ArrayList_ReceiveSemaphor> receiveSemaphor;

	public ReadThread(Handler h, ArrayList<ArrayList_ReceiveSemaphor> receiveSemaphor)
	{  
		this.mHandler = h;
		this.receiveSemaphor = receiveSemaphor;
	}
	
	public void setSemaphor(ArrayList<ArrayList_ReceiveSemaphor> receiveSemaphor) {
		this.receiveSemaphor = receiveSemaphor;
	}
	
	public void run()
	{  
		while (true) {
			try {
			
				if ((PTSMasterService.connectionState.equals("ConnectOK")) && (PTSMasterService.channel != null) && (PTSMasterService.channel.isConnected())) {
					Log.i("ReadThread", "IS CONECTED");
					int offsetAppend = 0;
					int offsetbufStream = 0;
					byte[] bufStream = new byte[100*1024];
					boolean collectPacketFlag = true;
					while (collectPacketFlag == true) {
						byte[] bytess = new byte[100*1024];
						ByteBuffer buf = ByteBuffer.wrap(bytess);
		
						// жду входные данные 
						int size = PTSMasterService.channel.read(buf);
						if (size < 0) {
							Log.e("Packet", "size < 0");
							PTSMasterService.connectionState = "unknown";
							// -------------------------------- Ответ 
							Message msg = mHandler.obtainMessage();
							Bundle b = new Bundle();
							b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_CONECTIONLOST);
							msg.setData(b);
							mHandler.sendMessage(msg);
							break;
						}
//				        Log.i("ReadThread", "<-- 0x" + myLib.byteArrayToHexString(buf.array(), size));
		
						//--Добавляю полученные данные в контейнер для обработки
						System.arraycopy(buf.array(), 0, bufStream, offsetAppend, size);
						offsetAppend += size;
		
						//--Проверяем цельность пакета и парсим полученные данные
						while(true) {
							res = funcPTSMaster.collectPackage(bufStream, offsetAppend, this.receiveSemaphor);
							if (res.result == funcPTSMaster.COLLECT_WAIT_FRAGMENT) {
								offsetbufStream += res.packetLength;
								Log.i("Packet", "COLLECT_WAIT_FRAGMENT: " + String.valueOf(offsetbufStream));
								collectPacketFlag = true;
								break;
							}
							if (res.result == funcPTSMaster.COLLECT_SIGNAL) {
								offsetbufStream += res.packetLength;
								Log.i("Packet", "COLLECT_SIGNAL");
								// -------------------------------- Ответ 
								Message msg = mHandler.obtainMessage();
								Bundle b = new Bundle();
								b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_IS_SIGNAL);
								b.putInt(PTSMasterService.PARAM_TASK, PTSTerminal.PTSMASTER_SIGNALRECEIVED);
								b.putByteArray(PTSMasterService.PARAM_RESULT_BUFF, res.packetData);
								msg.setData(b);
								mHandler.sendMessage(msg);
								collectPacketFlag = false;
							}
							if (res.result == funcPTSMaster.COLLECT_READY) {
								if (res.semaphor.timer != null) {
									Log.d("ReadThread", "---------- timer stop ------------");
									res.semaphor.timer.cancel();
								}
								for (int i = 0; i < this.receiveSemaphor.size(); i++) {
									if (this.receiveSemaphor.get(i).idPacket == res.semaphor.idPacket)
									{
										Log.d(LOG_TAG, "Удаляю семафор. idPacket = " + res.semaphor.idPacket);
										this.receiveSemaphor.remove(i);
										break;
									}
								}
//					            Log.i("Packet", "Пакет принят полностью");
								// -------------------------------- Ответ 
								Message msg = mHandler.obtainMessage();
								Bundle b = new Bundle();
								b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_IS_DATA);
								b.putInt(PTSMasterService.PARAM_ID_PACKET, res.semaphor.idPacket);
								b.putString(PTSMasterService.PARAM_RECEIVERNAME, res.semaphor.receiveName);
								b.putString(PTSMasterService.PARAM_OWNER, res.semaphor.owner);
								b.putInt(PTSMasterService.PARAM_TASK, res.semaphor.task);
								b.putInt(PTSMasterService.PARAM_FUNCTION, res.semaphor.function);
								b.putByteArray(PTSMasterService.PARAM_RESULT_BUFF, res.packetData);
								msg.setData(b);
								mHandler.sendMessage(msg);
								collectPacketFlag = false;
							}
							if (res.result == funcPTSMaster.COLLECT_DAMAGED) {
								Log.e("Packet", "DAMAGED");
								collectPacketFlag = false;
							}
							if (res.offset == 0) {
								break;
							}
							System.arraycopy(bufStream, res.offset, bufStream, 0,(bufStream.length - res.offset));
							if (offsetAppend >= res.offset) {
								offsetAppend -= res.offset;
							}
						}
						Thread.sleep(100);
					}            
				}
				else {
					// -------------------------------- Ответ 
					Message msg = mHandler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_CONECTIONLOST);
					msg.setData(b);
					mHandler.sendMessage(msg);
					Thread.sleep(1000);
				}
			}
			catch (IOException e) {
				Log.w("ReadThread", "IOException = " + e + "\n");
				// -------------------------------- Ответ 
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_CONECTIONLOST);
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
			catch (InterruptedException e) {
				Log.w("ReadThread", "InterruptedException" + e + "\n");
				break;
//				// -------------------------------- Ответ 
//				Message msg = mHandler.obtainMessage();
//				Bundle b = new Bundle();
//				b.putInt(PTSMasterService.PARAM_THREADRESULT, funcPTSMaster.STATUS_CONECTIONLOST);
//				msg.setData(b);
//				mHandler.sendMessage(msg);
			}
			finally {
				Log.i("ReadThread", "Socket closed\n");
			}
		}
	}
//	private DataInputStream in;
//	private DataOutputStream out;
//    private String resume;

}

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class funcPTSMaster
{
	final static String LOG_TAG = "funcPTSMaster";

	public static final byte PACKAGE_SIGN[]			= {(byte)0xDA, (byte)0x12, (byte)0x13, (byte)0x14};
	public static final byte PACKAGE_NULL[]			= {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
	public static final byte PACKAGE_POSCLIENT[] 	= {
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x19, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09, (byte)0x70, (byte)0x6F, (byte)0x73, (byte)0x43,
		(byte)0x6C, (byte)0x69, (byte)0x65, (byte)0x6E, (byte)0x74, (byte)0xFF, (byte)0xFF, (byte)0xFF,
		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF 
	};

	public static final int PACKAGE_LEN_OFFSET		= 61;// Позиция в пакете, в которой находится длина поля данных
	public static final int PACKAGE_RESPONSE_OFFSET	= 4;// Позиция в пакете, в которой находится тип пакета (RESPONSE или REQUEST)
	public static final int PACKAGE_ID_OFFSET		= 5;// Позиция в пакете, в которой находится id пакета 
	public static final int PACKAGE_HEADER_LEN		= 65;// Длина заголовка пакета
	public static final int PACKAGE_POSTFIX_LEN		= 16;
//	public static final int PACKAGE_DESCR_LEN		= (PACKAGE_PREFIX_LEN+PACKAGE_POSTFIX_LEN);
	public static final int PACKAGE_MAX_LEN			= (1024*1024);
	public static final int PACKAGE_LENGTH_POS		= 3;
	public static final String ANSWER_PREFIX			= "SRV";
//	public static final String ANSWER_POSTFIX		= new String(new byte[]{(byte)0x0D, (byte)0x0A});
	public static final String ANSWER_POSTFIX		= "\r\n";
	public static final int ANSWER_MAX_LEN			= 64;
	public static final int COMMAND_SIMPLE_DATA		= 1;
	public static final int COMMAND_PARTED_DATA		= 2;
	public static final int COMMAND_ASK_FOR_FILE		= 3;

	public static final int DATA_LEN_b    			= 1;
	public static final int DATA_LEN_s    			= 2;
	public static final int DATA_LEN_i    			= 4;
	public static final int DATA_LEN_l    			= 8;
	public static final int DATA_LEN_t_t  			= 4;
	
	public static final int VAR_LIST_AMOUNT = 99;
	public static final int VAR_POSCLIENT = 100;
	public static final int VAR_UNKNOWN = 0xFFFFFFFF;
	public static final int VAR_INVALID = 0;
	public static final int VAR_BOOL = 1;
	public static final int VAR_INT = 2;
	public static final int VAR_UINT = 3;
	public static final int VAR_INT64 = 4;
	public static final int VAR_UINT64 = 5;
	public static final int VAR_DOUBLE = 6;
	public static final int VAR_STRING = 10;
	public static final int VAR_BYTE_ARRAY = 12;
	public static final int VAR_MAP = 8;
	public static final int VAR_LIST = 9;
	public static final int VAR_HASH = 28;

	public static final int BASKET_CHANGE_ID = 1;
	public static final int PRODUCT_CHANGE_ID = 2;
	public static final int VOLUME_CHANGE_ID = 3;
	public static final int DEVICE_STATUS_CHANGE_ID = 4;
	public static final int SESSION_CHANGE_ID = 5;
	public static final int POS_LOCK_ID = 6;
	public static final int POS_UNLOCK_ID = 7;
	public static final int PUMP_STATE_CHANGE_ID = 8;
	public static final int PRICE_CHANGE_ID = 9;
	public static final int SERVICE_MODE_CHANGE_ID = 10;


	public static final String DATA_NAME_b   		= "b";
	public static final String DATA_NAME_s   		= "s";
	public static final String DATA_NAME_i   		= "i";
	public static final String DATA_NAME_l   		= "l";
	public static final String DATA_NAME_t_t 		= "t_t";
	public static final String DATA_NAME_su8 		= "su8";

	public static final int COLLECT_READY    		= 0;
	public static final int COLLECT_DAMAGED    		= 1;
	public static final int COLLECT_WAIT_FRAGMENT   = 2;
	public static final int COLLECT_SIGNAL		   	= 3;

	public static final int STATUS_IS_DATA    		= 0;
	public static final int STATUS_IS_SIGNAL   		= 1;
	public static final int STATUS_PACKAGE_OK    	= 2;
	public static final int STATUS_NEED_RESEND    	= 5;
	public static final int STATUS_NEXT_SERVER    	= 6;
	public static final int STATUS_UNKNOWN_STATUS   = 7;
	public static final int STATUS_TIMEOUT   		= 8;
	public static final int STATUS_NOCONNECT   		= 9;
	public static final int STATUS_CONNECTOK   		= 10;
	public static final int STATUS_CONECTIONLOST	= 11;
	public static final int STATUS_DAMAGED   		= 99;
	public static final int STATUS_PROTOCOL_ERROR   = 100;

	public static final int MSG_ADDR_SIZE = 24;
	public static final int MSG_FN_SIZE = 24;
	public static final int MSG_PORT = 29999;

	// --- Types of data bus messages (protocol PTSMaster)
	public static final int REGISTER_MSG = 1;
	public static final int ERROR_MSG = 2;
	public static final int REQUEST_MSG = 3;
	public static final int RESPONSE_MSG = 4;
	public static final int SIGNAL_MSG = 5;

	// =======================================================================================================
	//											collectPackage
	// =======================================================================================================
	public static final funcPTSMaster_result collectPackage(byte[] data, int size, ArrayList<ArrayList_ReceiveSemaphor> semaphor) {  

//		Log.i("collectPackage", "SIZE = " + size);
		funcPTSMaster_result	res = new funcPTSMaster_result();
		byte InPackage[] = new byte[size];
		System.arraycopy(data, 0, InPackage, 0, size);
		boolean idPacketFlag = false;
		
		if (InPackage.length < PACKAGE_LEN_OFFSET + 4) 
		{
			Log.i("collectPackage", "COLLECT_WAIT_FRAGMENT");
			res.result = COLLECT_WAIT_FRAGMENT;
			return res;
		}
		
		//=================================================================  Проверка целостности пакета 
		if (!myLib.memcmp(PACKAGE_SIGN, InPackage, PACKAGE_SIGN.length))
		{
			Log.e("collectPackage", "BAD PACKAGE_SIGN: " + myLib.getIntFromByteArray(InPackage, 0, 4));
			
			res.offset = 0;
			for (int i = 0; i < InPackage.length; i++) {
				if (myLib.memcmpOffset(PACKAGE_SIGN, 0, InPackage, i, PACKAGE_SIGN.length)) {
					Log.v("collectPackage", "FIND new SIGN, offset: " + i);
					res.offset = i;
					break;
				}
			}
			res.result = COLLECT_DAMAGED;
			return res;
		}
		Log.v("collectPackage", "GOOD SIGN");
		switch (myLib.getIntFromByteArray(InPackage, PACKAGE_RESPONSE_OFFSET, 1)) {
//		public static final int REGISTER_MSG = 1;
//		public static final int ERROR_MSG = 2;
//		public static final int REQUEST_MSG = 3;
//		public static final int RESPONSE_MSG = 4;
//		public static final int SIGNAL_MSG = 5;
		case RESPONSE_MSG:
			for (int i = 0; i < semaphor.size(); i++) {
	//			Log.v("semaphor", "function: " + semaphor.get(i).function);
	//			Log.v("semaphor", "idPacket: " + semaphor.get(i).idPacket);
	//			Log.v("semaphor", "task: " + semaphor.get(i).task);
	//			Log.v("semaphor", "owner: " + semaphor.get(i).owner);
	//			Log.v("semaphor", "receiveName: " + semaphor.get(i).receiveName);
				if (myLib.getIntFromByteArray(InPackage, PACKAGE_ID_OFFSET, 4) == semaphor.get(i).idPacket)
				{
					res.semaphor = new ArrayList_ReceiveSemaphor(semaphor.get(i).owner, semaphor.get(i).task, semaphor.get(i).function, semaphor.get(i).idPacket, semaphor.get(i).receiveName, semaphor.get(i).timer);
					idPacketFlag = true;
					break;
				}
			}
			if (!idPacketFlag) {
				Log.e("collectPackage", "BAD idPacket: " + myLib.getIntFromByteArray(InPackage, PACKAGE_ID_OFFSET, 4));
				res.result = COLLECT_DAMAGED;
				return res;
			}
			res.result = COLLECT_READY;
			break;
		case ERROR_MSG:
			break;
		case SIGNAL_MSG:
			res.result = COLLECT_SIGNAL;
			break;
		default:
			Log.e("collectPackage", "BAD RESPONSE: " + myLib.getIntFromByteArray(InPackage, PACKAGE_RESPONSE_OFFSET, 1));
			Log.e("collectPackage", "<-- 0x" + myLib.byteArrayToHexString(InPackage, InPackage.length));
			res.result = COLLECT_DAMAGED;
			return res;
		}
		
		res.packetLength = myLib.getIntFromByteArray(InPackage, PACKAGE_LEN_OFFSET, 4);
		res.packetData = new byte[res.packetLength];

		if (res.packetLength != InPackage.length - PACKAGE_HEADER_LEN)
		{
			Log.i("collectPackage", "BIG PACKET => InPackage.length= " + InPackage.length + "; res.packetLength= " + res.packetLength);
			if (res.packetLength < InPackage.length - PACKAGE_HEADER_LEN) {
//				Log.i("collectPackage", "InPackage 0x" + myLib.byteArrayToHexString(InPackage, InPackage.length));
				res.offset = PACKAGE_HEADER_LEN + res.packetLength;
				byte dbgPackage[] = new byte[InPackage.length - res.offset];
				System.arraycopy(InPackage, res.offset, dbgPackage, 0, dbgPackage.length);
//				Log.i("collectPackage", "res.offset = " + res.offset);
//				Log.i("collectPackage", "BIG 0x" + myLib.byteArrayToHexString(dbgPackage, dbgPackage.length));
			}
			else {
				res.result = COLLECT_WAIT_FRAGMENT;
				return res;
			}
		}
		
//        Log.i("collectPackage", "<-- 0x" + myLib.byteArrayToHexString(InPackage, InPackage.length));
		
		System.arraycopy(InPackage, PACKAGE_HEADER_LEN, res.packetData, 0, res.packetLength);
		

		return res;
	}

		
//		//--Проверяем пришло ли статусное сообщение
//		if(InPackageString.startsWith(ANSWER_PREFIX))
//		{
//			Log.i("collectPackage", "пришло статусное сообщение");
//			//--Пакет не заканчивается на \r\n добавляем к фрагментам
//			res.offset = InPackageString.indexOf(ANSWER_POSTFIX) + ANSWER_POSTFIX.length(); 
//			if(res.offset == -1)//ANSWER_POSTFIX))
//			{
//				//--Максимальная длинна больше чем 64 байта - неправильный статус
//				if(res.offset > ANSWER_MAX_LEN)
//				{
//					Log.e("collectPackage", "ANSWER_MAX_LEN: " + String.valueOf(res.offset));
//					res.result = COLLECT_DAMAGED;
//					return res;
//				}
//				else
//				{
//					Log.i("collectPackage", "COLLECT_WAIT_FRAGMENT");
//					Log.i("collectPackage", "offset: " + String.valueOf(res.offset));
//					res.result = COLLECT_WAIT_FRAGMENT;
//					return res;
//				}
//			}
//
//			//--Все проверки прошли - сообщение получено полностью
//			res.result = COLLECT_WAIT_FRAGMENT;
//			return res;
//		}
//		else	//--Сообщение данными
//		{
////			res.offset = InPackageString.indexOf(PACKAGE_SIGN) + PACKAGE_SIGN.length(); 
//			Log.i("collectPackage", "пришло сообщение c данными");
//			Log.i("collectPackage", "length_InPackage: " + String.valueOf(length_InPackage));
//			
//			//--Если признак пакета отсутствует - рвем коннект
//			if(!myLib.memcmp(PACKAGE_SIGN, InPackage, PACKAGE_SIGN.length))
//			{
//				Log.e("collectPackage", "InPackage: " + myLib.byteArrayToHexString(InPackage, length_InPackage));
//				Log.e("collectPackage", "PACKAGE_SIGN: " + myLib.byteArrayToHexString(PACKAGE_SIGN, PACKAGE_SIGN.length));
//				res.result = COLLECT_DAMAGED;
//				return res;
//			}
//			
//			//--Если длинна меньше чем длинна дескриптора ждем следующий фрагмент
//			if(length_InPackage < PACKAGE_DESCR_LEN)
//			{
//				res.result = COLLECT_WAIT_FRAGMENT;
//				return res;
//			}
//
//			//--Если данные о длинне пакета еще не получены - получаем
//			if (res.packetLength == 0) {
//				res.packetLength = myLib.getIntFromByteArray(InPackage, PACKAGE_LENGTH_POS, DATA_LEN_i);
//			}
//
//			//--Если длинна заявленных, или полученных данных больше максимальной длинны
//			if(res.packetLength > PACKAGE_MAX_LEN || length_InPackage > PACKAGE_MAX_LEN)
//			{
//				Log.e("collectPackage", "PACKAGE_LENGTH_POS: " + String.valueOf(PACKAGE_LENGTH_POS));
//				Log.e("collectPackage", "res.packetLength: " + String.valueOf(res.packetLength));
//				Log.e("collectPackage", "length_InPackage: " + String.valueOf(length_InPackage));
//				res.result = COLLECT_DAMAGED;
//				return res;
//			}
//
//			//--Если длинна полученных данных меньше чем заявленная длинна пакета, ждем следующий фрагмент
//			if(res.packetLength + PACKAGE_DESCR_LEN < length_InPackage)
//			{
//				res.result = COLLECT_WAIT_FRAGMENT;
//				return res;
//			}
//			
//			//--Если длинна полученных данных совпадает с заявленной длинна пакета, передаем на парс
//			if(res.packetLength + PACKAGE_DESCR_LEN == length_InPackage)
//			{
//				//--Проверяем MD5 всего пакета
//				if (myLib.md5(InPackage, length_InPackage - PACKAGE_POSTFIX_LEN)
//						.regionMatches(0, myLib.byteArrayToHexString(InPackage, length_InPackage), 
//								2 *(length_InPackage - PACKAGE_POSTFIX_LEN), 
//								PACKAGE_POSTFIX_LEN)) {
//					Log.i("collectPackage", "MD5 сравнилась!!!");
//					res.packetData = new byte[res.packetLength];
//					
//					System.arraycopy(InPackage, PACKAGE_PREFIX_LEN, res.packetData, 0, res.packetLength);
//					res.result = COLLECT_READY;
//				}
//				else {
//					Log.e("collectPackage", "MD5: " + myLib.md5(InPackage, length_InPackage - PACKAGE_POSTFIX_LEN));
//					Log.e("collectPackage", "md5: " + myLib.byteArrayToHexString(InPackage, length_InPackage).substring(2 * (length_InPackage - PACKAGE_POSTFIX_LEN)));
//					res.result = COLLECT_DAMAGED;
//				}
//				return res;
//			}
//			
//			//--Если длинна полученных данных больше заявленной длинны пакета
//			if(res.packetLength + PACKAGE_POSTFIX_LEN > length_InPackage)
//			{
//				res.result = COLLECT_DAMAGED;
//				return res;
//			}
//
//			res.result = COLLECT_DAMAGED;
//			return res;
//		}

	// =======================================================================================================
	//											prepareFunctionName
	// =======================================================================================================
	public static int prepareFunctionName(byte[] data, String msg_addr, String msg_fn, int offset) {
		byte buff[] = data;
		int buffOffset = offset;
		//  Добавляю идентификатор терминала  
		for (int i = 0; i < msg_addr.length(); i++) {
			buff[buffOffset+i] = (byte) msg_addr.charAt(i);
		}
		buffOffset += 24;
		//  Добавляю идентификатор терминала  
		for (int i = 0; i < msg_fn.length(); i++) {
			buff[buffOffset+i] = (byte) msg_fn.charAt(i);
		}
		buffOffset += 24;
		
		// Поле "subsys"
		for (int i = 0; i < 4; i++) {
			buff[buffOffset+i] = (byte) 0x00;
		}
		buffOffset += 4;
		return buffOffset - offset;
	}

	// =======================================================================================================
	//											f_login
	// =======================================================================================================
	public static byte[] f_login() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "13652", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_MAP, "8", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INVALID, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "3", "dispenser"));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "0", "nozzle"));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "1", "mode"));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "300", "volume"));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "0", "amount"));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "1234", "price"));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, "1880426614", "utag"));

		return varMapToByteArray(tVarMap);
	}

	// =======================================================================================================
	//											f_clerkreg
	// =======================================================================================================
	public static byte[] f_clerkreg(String user, String pass) {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, user, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, pass, ""));

		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_noargs
	// =======================================================================================================
	public static byte[] f_noargs() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));

		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_basketList
	// =======================================================================================================
	public static byte[] f_basketList(String basketIndex, String recordCount) {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, basketIndex, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, recordCount, ""));

		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_pluGet
	// =======================================================================================================
	public static byte[] f_pluGet(String pluIndex, String pluCount) {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, pluIndex, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, pluCount, ""));

		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_pumpResume
	// =======================================================================================================
	public static byte[] f_pumpResume() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, PTSTerminal.ptsMaster.idUser, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_MAP, "4", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.volume) , 	"volume"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.nozzle) , 		"nozzle"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.pump) , 		"index"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.basket) , 		"basket"));
		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_pumpStop
	// =======================================================================================================
	public static byte[] f_pumpStop() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "2", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.pump) , 		""));
		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_basketClose
	// =======================================================================================================
	public static byte[] f_basketClose() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, PTSTerminal.ptsMaster.idUser, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.basket) , 		""));
		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_basketClear
	// =======================================================================================================
	public static byte[] f_basketClear() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, PTSTerminal.ptsMaster.idUser, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.basket) , 		""));
		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_basketPayment
	// =======================================================================================================
	public static byte[] f_basketPayment() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "4", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, PTSTerminal.ptsMaster.idUser, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.basket), ""));
		tVarMap.add(new ArrayList_VarMap(VAR_MAP, "28", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.volume) , 	"volume"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.utag) , 		"utag"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.time) , 		"time"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.tag) , 		"tag"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.sum) , 		"sum"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.store) , 		"store"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.signature, 	"signature"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.rate) , 		"rate"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.quantity) ,	"quantity"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.pump) , 		"pump"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.product) , 	"product"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.price) , 		"price"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.person) , "person"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.payForm) , "payForm"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.orderVolume) , "orderVolume"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.nozzle) , "nozzle"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.name, "name"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.itemType) , "itemType"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.index) , "index"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.id) , "id"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.flags) , "flags"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.doc, "doc"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.discountType) , "discountType"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.cost) , "cost"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.comment, "comment"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.clerk) , "clerk"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.billNum) , "billNum"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.basket) , "basket"));
		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_basketAdd
	// =======================================================================================================
	public static byte[] f_basketAdd() {
		
		if (PTSTerminal.ptsMaster.OrderBasketItem == null) {
			return null;
		}
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "4", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, PTSTerminal.ptsMaster.idUser, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.basket), ""));
		tVarMap.add(new ArrayList_VarMap(VAR_MAP, "28", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.volume) , 	"volume"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.utag) , 		"utag"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.time) , 		"time"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.tag) , 		"tag"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.sum) , 		"sum"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.store) , 		"store"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.signature, 	"signature"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.rate) , 		"rate"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.quantity) ,	"quantity"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.pump) , 		"pump"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.product) , 	"product"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.price) , 		"price"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.person) , "person"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.payForm) , "payForm"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.orderVolume) , "orderVolume"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.nozzle) , "nozzle"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.name, "name"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.itemType) , "itemType"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.index) , "index"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.id) , "id"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.flags) , "flags"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.doc, "doc"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.discountType) , "discountType"));
		tVarMap.add(new ArrayList_VarMap(VAR_DOUBLE, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.cost) , "cost"));
		tVarMap.add(new ArrayList_VarMap(VAR_STRING, PTSTerminal.ptsMaster.OrderBasketItem.comment, "comment"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.clerk) , "clerk"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.billNum) , "billNum"));
		tVarMap.add(new ArrayList_VarMap(VAR_UINT, String.valueOf(PTSTerminal.ptsMaster.OrderBasketItem.basket) , "basket"));
		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_posClient
	// =======================================================================================================
	public static byte[] f_posClient() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_POSCLIENT, "", ""));

		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_storeGet
	// =======================================================================================================
	public static byte[] f_storeGet(String storeIndex, String storeCount) {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "3", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, storeIndex, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, storeCount, ""));

		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	//											f_basketGet
	// =======================================================================================================
	public static byte[] f_basketGet(String basketIndex, String basketItemIndex, String recordCount) {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "4", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, basketIndex, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, basketItemIndex, ""));
		tVarMap.add(new ArrayList_VarMap(VAR_INT, recordCount, ""));

		return varMapToByteArray(tVarMap);
	}
	
	// =======================================================================================================
	// =======================================================================================================
	//											f_pumpstate
	// =======================================================================================================
	public static byte[] f_pumpstate() {
		ArrayList<ArrayList_VarMap> tVarMap = new ArrayList<ArrayList_VarMap>();

		tVarMap.add(new ArrayList_VarMap(VAR_LIST_AMOUNT, "1", ""));
		tVarMap.add(new ArrayList_VarMap(VAR_BOOL, "1", ""));

		return varMapToByteArray(tVarMap);
	}
	
	//											varMapToByteArray
	// =======================================================================================================
	public static byte[] varMapToByteArray(ArrayList<ArrayList_VarMap> varMap) {
		byte buff[] = new byte[1*1024];
//		byte[] utf16le;
		int cnt = 4;

		for (int i = 0; i < varMap.size(); i++) {
			if (!varMap.get(i).mapKey.isEmpty()) {
				// Генерация поля ключа
				try {
					byte[] utf16le = varMap.get(i).mapKey.getBytes("UTF-16BE");
					// Устанавливаю "Размер поля "Ключ" (BE)"
					System.arraycopy(myLib.intToByteArrayBE(utf16le.length), 0, buff, cnt, 4);
					cnt += 4;
					System.arraycopy(utf16le, 0, buff, cnt, utf16le.length);
					cnt += utf16le.length;
	//			      Log.i("utf16le", "0x" + myLib.byteArrayToHexString(utf16le, utf16le.length));
				} catch (UnsupportedEncodingException e) {
					Log.e("varMapToByteArray", "unhappened convert UTF16 error: " + e);
//					e.printStackTrace();
					return null;
				}
			}
			switch (varMap.get(i).type) {
			case VAR_LIST_AMOUNT:
				System.arraycopy(myLib.intToByteArrayBE(Integer.parseInt(varMap.get(i).value)), 0, buff, cnt, 4);
				cnt += 4;
				break;
			case VAR_BOOL:
				System.arraycopy(myLib.intToByteArrayBE(VAR_BOOL), 0, buff, cnt, 4);
				cnt += 4;
				buff[cnt++] = (byte) 0x00;
				System.arraycopy(myLib.intToByteArray(Integer.parseInt(varMap.get(i).value)), 0, buff, cnt, 1);
				cnt += 1;
				break;
			case VAR_INT:
				System.arraycopy(myLib.intToByteArrayBE(VAR_INT), 0, buff, cnt, 4);
				cnt += 4;
				buff[cnt++] = (byte) 0x00;
				System.arraycopy(myLib.intToByteArrayBE(Integer.parseInt(varMap.get(i).value)), 0, buff, cnt, 4);
				cnt += 4;
				break;
			case VAR_UINT:
				System.arraycopy(myLib.intToByteArrayBE(VAR_UINT), 0, buff, cnt, 4);
				cnt += 4;
				buff[cnt++] = (byte) 0x00;
				System.arraycopy(myLib.uintToByteArrayBE(Long.parseLong(varMap.get(i).value)), 0, buff, cnt, 4);
				cnt += 4;
				break;
			case VAR_STRING:
				System.arraycopy(myLib.intToByteArrayBE(VAR_STRING), 0, buff, cnt, 4);
				cnt += 4;
				buff[cnt++] = (byte) 0x00;
				if (varMap.get(i).value.equals("")) {
					System.arraycopy(myLib.intToByteArrayBE(0), 0, buff, cnt, 4);
					cnt += 4;
				}
				else {
					// Генерация поля ключа
					try {
						byte[] utf16leValue = varMap.get(i).value.getBytes("UTF-16BE");
						// Устанавливаю "Размер поля "Ключ" (BE)"
						System.arraycopy(myLib.intToByteArrayBE(utf16leValue.length), 0, buff, cnt, 4);
						cnt += 4;
						System.arraycopy(utf16leValue, 0, buff, cnt, utf16leValue.length);
						cnt += utf16leValue.length;
//					    Log.d("utf16leValue", "0x" + myLib.byteArrayToHexString(utf16leValue, utf16leValue.length));
					} catch (UnsupportedEncodingException e) {
						Log.e("varMapToByteArray", "unhappened convert UTF16 error: " + e);
	//					e.printStackTrace();
						return null;
					}
				}
				break;
			case VAR_DOUBLE:
				System.arraycopy(myLib.intToByteArrayBE(VAR_DOUBLE), 0, buff, cnt, 4);
				cnt += 4;
				buff[cnt++] = (byte) 0x00;
				System.arraycopy(myLib.doubleToByteArray(Double.valueOf(varMap.get(i).value)), 0, buff, cnt, 8);
				cnt += 8;
				break;
			case VAR_MAP:
				System.arraycopy(myLib.intToByteArrayBE(VAR_MAP), 0, buff, cnt, 4);
				cnt += 4;
				buff[cnt++] = (byte) 0x00;
				System.arraycopy(myLib.intToByteArrayBE(Integer.parseInt(varMap.get(i).value)), 0, buff, cnt, 4);
				cnt += 4;
				break;
			case VAR_INVALID:
				System.arraycopy(PACKAGE_NULL, 0, buff, cnt, 4);
				cnt += 4;
				System.arraycopy(myLib.intToByteArrayBE(VAR_INVALID), 0, buff, cnt, 4);
				cnt += 4;
				buff[cnt++] = (byte) 0x01;
				System.arraycopy(PACKAGE_NULL, 0, buff, cnt, 4);
				cnt += 4;
				break;
			case VAR_POSCLIENT:
				System.arraycopy(PACKAGE_NULL, 0, buff, cnt, 4);
				cnt += 4;
				System.arraycopy(PACKAGE_NULL, 0, buff, cnt, 4);
				cnt += 4;
				break;
			default:
				Log.e("varMapToByteArray", "unknown type: " + Integer.toString(varMap.get(i).type));
				return null;
			}
			
		}

		cnt -= 4; // Удаляю из размера первые 4 байта (размер бинарника)
		// Устанавливаю "Размер бинарника (LE)"
		System.arraycopy(myLib.intToByteArray(cnt), 0, buff, 0, 4);

		
		return buff;
	}

//	// =======================================================================================================
//	//											resByteArrayToVarMap
//	// =======================================================================================================
//	class resByteArrayToVarMap {
//		int buffOffaset;
//		ArrayList_VarMap varMap;
//		resByteArrayToVarMap() {
//			buffOffaset = 0;
//			varMap = new ArrayList_VarMap(VAR_INVALID, "", "");
//		}
//	}
//
	// =======================================================================================================
	//											byteArrayToVarMap
	// =======================================================================================================
	public static ArrayList<ArrayList<ArrayList_VarMap>> byteArrayToVarMap(byte[] data, int size, int offset) {
		
		int varType;
		int cnt = 0;
		int intValue = 0;
		String varMapName = "";
		String varStringName = "";
		Double doubleVar = 0.0;
		int amountVarMap = 0;
		int amountVarList = 0;
		int stringLength = 0;
		int currentVarList = 0;
		int currentVarMap = 0;
		Charset UNICODE_CHARSET = Charset.forName("UTF-16BE");
		
		ArrayList<ArrayList<ArrayList_VarMap>> tVarList = new ArrayList<ArrayList<ArrayList_VarMap>>();

		int length;
		if (size > offset) {
			length = size - offset;
		}
		else {
			length = size;
		}
		byte[] buff = new byte[length]; 
		System.arraycopy(data, offset, buff, 0, length);
//	    Log.d("byteArrayToVarMap input", "0x" + myLib.byteArrayToHexString(buff, length));
		
		while (cnt < length) {
//		    Log.d("<->", "cnt= " + cnt + "; length = " + length);
//		    Log.d("<->", "currentVarList= " + currentVarList + "; currentVarMap = " + currentVarMap);
			varType = myLib.getIntFromByteArrayBE(buff, cnt, 4);
			cnt += 4;
//		    Log.d("<->", "varType= " + varType);
			if ((amountVarMap > 0) && (currentVarMap <= amountVarMap)) {
//			    Log.d("<->", "amountVarMap= " + amountVarMap);
				stringLength = varType;
				if (stringLength != VAR_UNKNOWN) {// Иногда по задумке режисёра вместо нормального ключа появляется 4 байта 0xFFFFFFFF (пустая строка)
					byte[] utf16le = new byte[stringLength];
					System.arraycopy(buff, cnt, utf16le, 0, stringLength);
					cnt += stringLength;
					varMapName = new String(utf16le, UNICODE_CHARSET);
//			        Log.d("<->", "varMapName = " + varMapName);
//			        amountVarMap--;
				}
//			    Log.d("<+>", "cnt= " + cnt + "; length = " + length);
				varType = myLib.getIntFromByteArrayBE(buff, cnt, 4);
				cnt += 4;
//			    Log.d("<+>", "varType= " + varType);
			}
			else {
				varMapName = new String();
			}
			cnt++;
			if (varType == VAR_LIST) {
				amountVarList = myLib.byteArrayToInt(buff, cnt) - 1;
				cnt += 4;
//		        Log.d("!!!", "amountVarList = " + amountVarList);
		        if (amountVarList != 0) {
			        for (int i = 0; i <= amountVarList; i++) {
			        	tVarList.add(new ArrayList<ArrayList_VarMap>());
					}
//			        Log.i("!!!", "tVarList size = " + tVarList.size());
			        currentVarList = 0;
		        }
		        continue;
			}
			if (varType == VAR_MAP) {
				amountVarMap = myLib.byteArrayToInt(buff, cnt) - 1;
				cnt += 4;
//			    Log.d("!!!", "amountVarMap= " + amountVarMap);
				if (tVarList.size() == 0) {
		        	tVarList.add(new ArrayList<ArrayList_VarMap>());
			        currentVarList = 0;
				}
		        if (amountVarMap != 0) {
			        currentVarMap = 0;
		        }
		        continue;
			}
			switch (varType) {
			case VAR_STRING:
				stringLength = myLib.byteArrayToInt(buff, cnt);
				cnt += 4;
				if (stringLength > 0) {
					byte[] utf16le = new byte[stringLength];
					System.arraycopy(buff, cnt, utf16le, 0, stringLength);
					cnt += stringLength;
					varStringName = new String(utf16le, UNICODE_CHARSET);
				}
				else {
					varStringName = new String("");
				}
//		        Log.i("byteArrayToVarMap", "VAR_STRING = " + varStringName);
		        tVarList.get(currentVarList).add(new ArrayList_VarMap(VAR_STRING, varStringName, varMapName));
//				Log.i("<->", " " + currentVarList +  "  " + tVarList.get(currentVarList).get(currentVarMap).mapKey + "  " + tVarList.get(currentVarList).get(currentVarMap).value);
				break;
			case VAR_BOOL:
				intValue = myLib.byteArrayToBool(buff, cnt);
				cnt += 1;
//		        Log.i("byteArrayToVarMap", "VAR_BOOL = " + intValue);
				if (tVarList.size() == 0) {
		        	tVarList.add(new ArrayList<ArrayList_VarMap>());
			        currentVarList = 0;
				}
		        tVarList.get(currentVarList).add(new ArrayList_VarMap(VAR_BOOL, Integer.toString(intValue), varMapName));
				break;
			case VAR_INT:
				intValue = myLib.byteArrayToInt(buff, cnt);
				cnt += 4;
//		        Log.i("byteArrayToVarMap", "VAR_INT = " + intValue);
		        tVarList.get(currentVarList).add(new ArrayList_VarMap(VAR_INT, Integer.toString(intValue), varMapName));
				break;
			case VAR_UINT:
//				intValue = myLib.byteArrayToInt(buff, cnt);
				long h = myLib.getUintFromByteArrayBE(buff, cnt, 4);
				cnt += 4;
//		        Log.i("byteArrayToVarMap", "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ VAR_UINT = " + h);
		        tVarList.get(currentVarList).add(new ArrayList_VarMap(VAR_UINT, Long.toString(h), varMapName));
				break;
			case VAR_DOUBLE:
				doubleVar = myLib.getDoubleFromByteArrayBE(buff, cnt);
				cnt += 8;
//		        Log.i("byteArrayToVarMap", "VAR_DOUBLE = " + doubleVar);
		        tVarList.get(currentVarList).add(new ArrayList_VarMap(VAR_DOUBLE, Double.toString(doubleVar), varMapName));
				break;
			case VAR_INVALID:
				intValue = myLib.byteArrayToInt(buff, cnt);
				cnt += 4;
//		        Log.i("byteArrayToVarMap", "VAR_INVALID = " + intValue);
		        tVarList.get(currentVarList).add(new ArrayList_VarMap(VAR_INVALID, Integer.toString(intValue), varMapName));
				break;
			default:
				Log.e("byteArrayToVarMap", "unknown type: " + varType);
				tVarList = null;
				return tVarList;
			} 
			if (currentVarMap < amountVarMap) {
				currentVarMap++;
			}
			else {
				currentVarMap = 0;
				amountVarMap = 0;
				if (currentVarList < amountVarList) {
//					Log.i("@@@", "================================================================");
//					Log.i("@@@", "ID   MapKey   Value");
//					Log.i("@@@", "================================================================");
//					for (int i = 0; i < tVarList.size(); i++) {
//						for (int n = 0; n < tVarList.get(i).size(); n++) {
//							Log.i("@@@", " " + i +  "  " + tVarList.get(i).get(n).mapKey + "  " + tVarList.get(i).get(n).value);
//						}
//					}
//					Log.i("@@@", "================================================================");
					currentVarList++;
				}
				else {
//					Log.e("byteArrayToVarMap", "currentVarList: " + currentVarList);
				}
			}
		}
		return tVarList;
	}

	// =======================================================================================================
	//											parsePackage
	// =======================================================================================================
//	public static void copy_ptsMaster(ArrayList_PTSMaster source, ArrayList_PTSMaster destination) {
//		source.
//		for (int i = 0; i < source.mListSignal.size(); i++) {
//		}
//	}


	// =======================================================================================================
	//											parsePackage
	// =======================================================================================================
	public static String parsePackage(int function, byte[] data, int size, int offsetbufStream, ArrayList_PTSMaster ptsMaster) {
	
		String errorMessage = null;
		String errorNumber = null;


		int length;
		if (size > offsetbufStream) {
			length = size - offsetbufStream;
		}
		else {
			length = size;
		}
		byte[] buff = new byte[length]; 
		System.arraycopy(data, offsetbufStream, buff, 0, length);
//	    Log.d("parsePackage input", "0x" + myLib.byteArrayToHexString(buff, length));
	    
	    ArrayList<ArrayList<ArrayList_VarMap>> tVarList = new ArrayList<ArrayList<ArrayList_VarMap>>();
	    tVarList = byteArrayToVarMap(buff, length, 0);
		if (tVarList == null) {
			errorMessage = "Unknown";
			return errorMessage;
		}
//		Log.i("$$$", "================================================================");
//		Log.i("$$$", "ID   MapKey   Value");
//		Log.i("$$$", "================================================================");
//		for (int i = 0; i < tVarList.size(); i++) {
//			for (int n = 0; n < tVarList.get(i).size(); n++) {
//				Log.i("$$$", " " + i +  "  " + tVarList.get(i).get(n).mapKey + "  " + tVarList.get(i).get(n).value);
//			}
//			Log.i("$$$", "----------------------------------------------------------------");
//		}
//		Log.i("$$$", "================================================================");

		// ------------------------------------------------------
		// Поиск ответа об ошибке 
		// ------------------------------------------------------
//		message  Permission denied
//		error  2
		errorMessage = "";
		errorNumber = "";
		for (int i = 0; i < tVarList.size(); i++) {
			for (int n = 0; n < tVarList.get(i).size(); n++) {
				if (tVarList.get(i).get(n).mapKey.equals("message")) {
					errorMessage = tVarList.get(i).get(n).value;
				}
				if (tVarList.get(i).get(n).mapKey.equals("error")) {
					errorNumber = tVarList.get(i).get(n).value;
				}
			}
		}
		if ((!errorMessage.equals("")) || (!errorNumber.equals(""))) {
			Log.e(LOG_TAG, "errorMessage: " + errorMessage);
			return errorMessage;
		}
		
		switch (function) {
		case PTSMasterService.FUNC_EMULSIGNAL:
//			Log.d("function", "clerkList");
			if (ptsMaster.mListSignal.size() > 0) {
				ptsMaster.mListSignal.clear();
			}
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListSignal = new ArrayList_Signal();
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("#msg_id")) {
						ptsMaster.mListSignal.setMsgId(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("#id")) {
						ptsMaster.mListSignal.setId(tVarList.get(i).get(n).value);
					}
				}
			}
			break;
		case PTSMasterService.FUNC_CLERCLIST:
//			Log.d("function", "clerkList");
			if (ptsMaster.mListClerks.size() > 0) {
				ptsMaster.mListClerks.clear();
			}
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListClerks.add(new ArrayList_Clerks(String.valueOf(i), "", "", ""));
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("name")) {
						ptsMaster.mListClerks.get(i).setName(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("permissions")) {
						ptsMaster.mListClerks.get(i).setPermissions(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("password")) {
						ptsMaster.mListClerks.get(i).setPassword(tVarList.get(i).get(n).value);
					}
				}
			}
			break;
		case PTSMasterService.FUNC_DISPENSERGET:
//			Log.d("function", "dispenserGet");
			if (ptsMaster.mListDispenserCfg.size() > 0) {
				ptsMaster.mListDispenserCfg.clear();
			}
			for (int i = 0; i < tVarList.size(); i++) {
				int count = 0;
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					ptsMaster.mListDispenserCfg.add(new ArrayList<ArrayList_DispenserCfg>());
					if (tVarList.get(i).get(n).mapKey.startsWith("store")) {
						if (tVarList.get(i).get(n).mapKey.equals("storeCount")) {
							count = Integer.valueOf(tVarList.get(i).get(n).value);
					        for (int k = 0; k < count; k++) {
								ptsMaster.mListDispenserCfg.get(i).add(new ArrayList_DispenserCfg());
								ptsMaster.mListDispenserCfg.get(i).get(k).setNozzle(String.valueOf(k));
								ptsMaster.mListDispenserCfg.get(i).get(k).setCount(tVarList.get(i).get(n).value);
							}
						}
						else {
							for (int k = 0; k < count; k++) {
								if (tVarList.get(i).get(n).mapKey.equals("store" + String.valueOf(k))) {
									ptsMaster.mListDispenserCfg.get(i).get(k).setStore(tVarList.get(i).get(n).value);
								}
							}
						}
					}
					else {
						if (tVarList.get(i).get(n).mapKey.equals("name")) {
					        for (int k = 0; k < count; k++) {
								ptsMaster.mListDispenserCfg.get(i).get(k).setName(tVarList.get(i).get(n).value);
							}
						}
						if (tVarList.get(i).get(n).mapKey.equals("index")) {
					        for (int k = 0; k < count; k++) {
								ptsMaster.mListDispenserCfg.get(i).get(k).setIndex(tVarList.get(i).get(n).value);
							}
						}
					}
				}
			}
			break;
		case PTSMasterService.FUNC_PUMPSTATE:
//			Log.d("function", "pumpState");
			if (ptsMaster.mListDispenser.size() > 0) {
				ptsMaster.mListDispenser.clear();
			}
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListDispenser.add(new ArrayList_Dispenser());
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("index")) {
						ptsMaster.mListDispenser.get(i).setDispenseIndex(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("basket")) {
						ptsMaster.mListDispenser.get(i).setDispenseBasket(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("nozzle")) {
						ptsMaster.mListDispenser.get(i).setDispenseNozzle(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("order")) {
						ptsMaster.mListDispenser.get(i).setDispenseOrder(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("orderNozzle")) {
						ptsMaster.mListDispenser.get(i).setDispenseOrder_nozzle(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("orderVolume")) {
						ptsMaster.mListDispenser.get(i).setDispenseOrder_volume(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("status")) {
						// DISP_UNDEFINED = 0 , DISP_OFF = 1 , DISP_IDLE = 2 , DISP_CALL = 3 , DISP_WORK = 4 , DISP_AUTH = 5 , DISP_BUSY = 6
						switch (Integer.valueOf(tVarList.get(i).get(n).value)) {
						case 0:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_UNDEFINED");
							break;
						case 1:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_OFF");
							break;
						case 2:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_IDLE");
							break;
						case 3:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_CALL");
							break;
						case 4:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_WORK");
							break;
						case 5:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_AUTH");
							break;
						case 6:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_BUSY");
							break;
						default:
							ptsMaster.mListDispenser.get(i).setDispenserStatus("DISP_UNDEFINED");
							break;
						}
					}
					if (tVarList.get(i).get(n).mapKey.equals("volume")) {
						ptsMaster.mListDispenser.get(i).setDispenseVolume(tVarList.get(i).get(n).value);
					}
				}
			}
			Log.i("###", "================================================================");
			Log.i("###", "Index   Status   Basket   Volume  Order");
			Log.i("###", "================================================================");
			for (int i = 0; i < ptsMaster.mListDispenser.size(); i++) {
				Log.i("###", " " + ptsMaster.mListDispenser.get(i).getDispenserIndex() + "  " + ptsMaster.mListDispenser.get(i).getDispenserStatus() + "  " + ptsMaster.mListDispenser.get(i).getDispenserBasket() + "  " + ptsMaster.mListDispenser.get(i).getDispenserVolume() + "  " + ptsMaster.mListDispenser.get(i).getDispenserOrder());
			}
			Log.i("###", "================================================================");
			break;
		case PTSMasterService.FUNC_PAYFORMLIST:
//			Log.d("function", "payFormList");
			if (ptsMaster.mListPayForm.size() > 0) {
				ptsMaster.mListPayForm.clear();
			}
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListPayForm.add(new ArrayList_PayForm());
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("type")) {
						ptsMaster.mListPayForm.get(i).setType(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("name")) {
						ptsMaster.mListPayForm.get(i).setName(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("id")) {
						ptsMaster.mListPayForm.get(i).setId(tVarList.get(i).get(n).value);
					}
				}
			}
			break;
		case PTSMasterService.FUNC_VERSION:
//			Log.d("function", "version");
			break;
		case PTSMasterService.FUNC_BASKETADD:
//			Log.d("function", "version");
			break;
		case PTSMasterService.FUNC_BASKETCLOSE:
			Log.d("function", "basketClose");
			Log.i("$$$", "================================================================");
			Log.i("$$$", "ID   MapKey   Value");
			Log.i("$$$", "================================================================");
			for (int i = 0; i < tVarList.size(); i++) {
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					Log.i("$$$", " " + i +  "  " + tVarList.get(i).get(n).mapKey + "  " + tVarList.get(i).get(n).value);
				}
				Log.i("$$$", "----------------------------------------------------------------");
			}
			Log.i("$$$", "================================================================");
			break;
		case PTSMasterService.FUNC_BASKETCLEAR:
			Log.d("function", "basketClear");
			Log.i("$$$", "================================================================");
			Log.i("$$$", "ID   MapKey   Value");
			Log.i("$$$", "================================================================");
			for (int i = 0; i < tVarList.size(); i++) {
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					Log.i("$$$", " " + i +  "  " + tVarList.get(i).get(n).mapKey + "  " + tVarList.get(i).get(n).value);
				}
				Log.i("$$$", "----------------------------------------------------------------");
			}
			Log.i("$$$", "================================================================");
			break;
		case PTSMasterService.FUNC_PUMPRESUME:
			Log.d("function", "pumpResume");
			Log.i("$$$", "================================================================");
			Log.i("$$$", "ID   MapKey   Value");
			Log.i("$$$", "================================================================");
			for (int i = 0; i < tVarList.size(); i++) {
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					Log.i("$$$", " " + i +  "  " + tVarList.get(i).get(n).mapKey + "  " + tVarList.get(i).get(n).value);
				}
				Log.i("$$$", "----------------------------------------------------------------");
			}
			Log.i("$$$", "================================================================");
			break;
		case PTSMasterService.FUNC_PUMPSTOP:
			Log.d("function", "pumpStop");
			Log.i("$$$", "================================================================");
			Log.i("$$$", "ID   MapKey   Value");
			Log.i("$$$", "================================================================");
			for (int i = 0; i < tVarList.size(); i++) {
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					Log.i("$$$", " " + i +  "  " + tVarList.get(i).get(n).mapKey + "  " + tVarList.get(i).get(n).value);
				}
				Log.i("$$$", "----------------------------------------------------------------");
			}
			Log.i("$$$", "================================================================");
			break;
		case PTSMasterService.FUNC_BASKETPAYMENT:
			Log.d("function", "basketPayment");
			Log.i("$$$", "================================================================");
			Log.i("$$$", "ID   MapKey   Value");
			Log.i("$$$", "================================================================");
			for (int i = 0; i < tVarList.size(); i++) {
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					Log.i("$$$", " " + i +  "  " + tVarList.get(i).get(n).mapKey + "  " + tVarList.get(i).get(n).value);
				}
				Log.i("$$$", "----------------------------------------------------------------");
			}
			Log.i("$$$", "================================================================");
			if (ptsMaster.PaymentAnswer.size() > 0) {
				ptsMaster.PaymentAnswer.clear();
			}
			ptsMaster.PaymentAnswer = new ArrayList_PaymentAnswer();
			for (int i = 0; i < tVarList.size(); i++) {
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("type")) {
						ptsMaster.PaymentAnswer.setTag(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("paySum")) {
						ptsMaster.PaymentAnswer.setPaySum(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("payCount")) {
						ptsMaster.PaymentAnswer.setPayCount(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("itemSum")) {
						ptsMaster.PaymentAnswer.setItemSum(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("itemCount")) {
						ptsMaster.PaymentAnswer.setItemCount(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("flags")) {
						ptsMaster.PaymentAnswer.setFlags(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("clerk")) {
						ptsMaster.PaymentAnswer.setClerk(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("billNum")) {
						ptsMaster.PaymentAnswer.setBillNum(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("basket")) {
						ptsMaster.PaymentAnswer.setBasket(tVarList.get(i).get(n).value);
					}
				}
			}
			break;
		case PTSMasterService.FUNC_CLERCREG:
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListPayForm.add(new ArrayList_PayForm());
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("handle")) {
						Log.d(LOG_TAG, "handle: " + tVarList.get(i).get(n).value);
						ptsMaster.idUser = tVarList.get(i).get(n).value;
					}
					if (tVarList.get(i).get(n).mapKey.equals("name")) {
						ptsMaster.name = tVarList.get(i).get(n).value;
					}
				}
			}
//			Log.d("function", "clerkReg");
			break;
		case PTSMasterService.FUNC_BASKETLIST:
			Log.d("function", "================= basketList");
			if (ptsMaster.mListBasket.size() > 0) {
				ptsMaster.mListBasket.clear();
			}
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListBasket.add(new ArrayList_BasketData());
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("tag")) {
						ptsMaster.mListBasket.get(i).tag = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("paySum")) {
						ptsMaster.mListBasket.get(i).paySum = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("payCount")) {
						ptsMaster.mListBasket.get(i).payCount = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("itemSum")) {
						ptsMaster.mListBasket.get(i).itemSum = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("itemCount")) {
						ptsMaster.mListBasket.get(i).itemCount = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("flags")) {
						ptsMaster.mListBasket.get(i).flags = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("clerk")) {
						ptsMaster.mListBasket.get(i).clerk = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("billNum")) {
						ptsMaster.mListBasket.get(i).billNum = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("basket")) {
						ptsMaster.mListBasket.get(i).basket = Integer.valueOf(tVarList.get(i).get(n).value);
					}
				}
			}
			break;
		case PTSMasterService.FUNC_BASKETGET:
			Log.d("function", "------------------ basketGet");
			for (int n = 0; n < ptsMaster.mListBasket.size(); n++) {
				if (ptsMaster.mListBasket.get(n).basketItem.size() > 0) {
					ptsMaster.mListBasket.get(n).basketItem.clear();
				}
			}
			for (int i = 0; i < tVarList.size(); i++) {
				ArrayList_BasketItems item = new ArrayList_BasketItems();
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("basket")) {
						item.basket = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("billNum")) {
						item.billNum = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("cost")) {
						item.cost = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("clerk")) {
						item.clerk = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("discountType")) {
						item.discountType = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("doc")) {
						item.doc = tVarList.get(i).get(n).value;
					}
					if (tVarList.get(i).get(n).mapKey.equals("flags")) {
						item.flags = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("id")) {
						item.id = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("index")) {
						item.index = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("itemType")) {
						item.itemType = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("name")) {
						item.name = tVarList.get(i).get(n).value;
					}
					if (tVarList.get(i).get(n).mapKey.equals("nozzle")) {
						item.nozzle = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("orderVolume")) {
						item.orderVolume = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("payForm")) {
						item.payForm = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("person")) {
						item.person = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("price")) {
						item.price = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("product")) {
						item.product = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("pump")) {
						item.pump = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("quantity")) {
						item.quantity = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("rate")) {
						item.rate = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("store")) {
						item.store = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("sum")) {
						item.sum = Double.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("tag")) {
						item.tag = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("time")) {
						item.time = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("utag")) {
						item.utag = Integer.valueOf(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("volume")) {
						item.volume = Double.valueOf(tVarList.get(i).get(n).value);
					}
				}
				for (int n = 0; n < ptsMaster.mListBasket.size(); n++) {
					if (ptsMaster.mListBasket.get(n).basket == item.basket) {
						ptsMaster.mListBasket.get(n).basketItem.add(new ArrayList_BasketItems( item.id,item.basket,item.index,item.itemType,item.sum,item.cost,item.flags,item.clerk,item.tag,item.payForm,item.person,
								item.name,item.doc,item.signature,item.billNum,item.ret,item.quantity,item.discountType,item.rate,item.pump,item.nozzle,item.store,
								item.product,item.volume,item.orderVolume,item.price,item.time,item.utag));
					}
				}

			}
			Log.i("###", "================================================================");
			Log.i("###", " BASKETLIST");
			Log.i("###", "================================================================");
			Log.i("###", "basket  itemCount  payCount  clerk  tag  flags  itemSum  paySum  billNum");
			Log.i("###", "================================================================");
			for (int i = 0; i < ptsMaster.mListBasket.size(); i++) {
				Log.i("###", " " + 	ptsMaster.mListBasket.get(i).basket + " , " + 
									ptsMaster.mListBasket.get(i).itemCount + " , " + 
									ptsMaster.mListBasket.get(i).payCount + " , " + 
									ptsMaster.mListBasket.get(i).clerk + " , " + 
									ptsMaster.mListBasket.get(i).tag + " , " + 
									ptsMaster.mListBasket.get(i).flags + " , " + 
									ptsMaster.mListBasket.get(i).itemSum + " , " + 
									ptsMaster.mListBasket.get(i).paySum + " , " + 
									ptsMaster.mListBasket.get(i).billNum );
				Log.i("###", "..........................................................................");
				for (int n = 0; n < ptsMaster.mListBasket.get(i).basketItem.size(); n++) {
					Log.i("###", " " + 	
							ptsMaster.mListBasket.get(i).basketItem.get(n).basket + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).billNum + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).clerk + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).cost + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).discountType + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).flags + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).id + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).index + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).itemType + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).name + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).nozzle + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).orderVolume + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).payForm + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).person + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).price + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).product + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).pump + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).quantity + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).rate + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).store + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).sum + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).tag + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).time + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).utag + " , " + 
							ptsMaster.mListBasket.get(i).basketItem.get(n).volume );
				}
				Log.i("###", "-----------------------------------------------------------------");
			}
			Log.i("###", "================================================================");
			Log.d("function", "basketGet");
			break;
		case PTSMasterService.FUNC_PLUGET:
//			Log.d("function", "pluGet");
			if (ptsMaster.mListProductAssortment.size() > 0) {
				ptsMaster.mListProductAssortment.clear();
			}
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListProductAssortment.add(new ArrayList_ProductAssortment(String.valueOf(i), "", "", "", "", "", ""));
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("index")) {
						ptsMaster.mListProductAssortment.get(i).setArticleId(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("name")) {
						ptsMaster.mListProductAssortment.get(i).setName(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("shortName")) {
						ptsMaster.mListProductAssortment.get(i).setShortName(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("unit")) {
						ptsMaster.mListProductAssortment.get(i).setUnitName(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("price")) {
						ptsMaster.mListProductAssortment.get(i).setPrice(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("productType")) {
						ptsMaster.mListProductAssortment.get(i).setType(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("rest")) {
						ptsMaster.mListProductAssortment.get(i).setRest(tVarList.get(i).get(n).value);
					}
				}
			}
			break;
		case PTSMasterService.FUNC_STOREGET:
//			Log.d("function", "storeGet");
			for (int i = 0; i < tVarList.size(); i++) {
				ptsMaster.mListStore.add(new ArrayList_Store(String.valueOf(i), "", "", "", "", "", ""));
				for (int n = 0; n < tVarList.get(i).size(); n++) {
					if (tVarList.get(i).get(n).mapKey.equals("index")) {
						ptsMaster.mListStore.get(i).setIndex(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("product")) {
						ptsMaster.mListStore.get(i).setArticleId(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("size")) {
						ptsMaster.mListStore.get(i).setSize(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("height")) {
						ptsMaster.mListStore.get(i).setHeight(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("unit")) {
						ptsMaster.mListStore.get(i).setUnit(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("shortName")) {
						ptsMaster.mListStore.get(i).setShortName(tVarList.get(i).get(n).value);
					}
					if (tVarList.get(i).get(n).mapKey.equals("name")) {
						ptsMaster.mListStore.get(i).setName(tVarList.get(i).get(n).value);
					}
				}
			}
			break;
		case PTSMasterService.FUNC_POSCLIENT:
			break;
		default:
			Log.e("function", "unknown");
			break;
		}
		
//
//		//--Если пакет статусный
//		if (InPackage.package.startsWith(ANSWER_PREFIX)) {
//			qDebug() << "messagingClient::parsePackage STATUS";
//			int posStatusEnd = InPackage.package.indexOf(QByteArray::fromHex("0D0A"));
//			QList<QByteArray> temp = InPackage.package.mid(0, posStatusEnd).split('_');
//
//			if (temp.count() < 6) return STATUS_UNKNOWN_STATUS;
//
//			InPackage.prefix.commandId  = atoi(temp.at(1).data());
//			InPackage.prefix.packageId  = atoi(temp.at(2).data());
//			InPackage.prefix.partId     = atoi(temp.at(3).data());
//			InPackage.data              = temp.at(4) + "_" + temp.at(5);
//
//			//--Удаляем парсенные данные из пакета
//			qDebug() << "InPackage.package.remove1 " << InPackage.package.toHex();
//			InPackage.package.remove(0, posStatusEnd+2);
//			qDebug() << "InPackage.package.remove2 " << InPackage.package.toHex();
//
//			switch (answerStatusList.indexOf(InPackage.data)) {
//			case 0:	//--"SUCCESS_MESS"
//				return STATUS_PACKAGE_OK;
//
//			case 1:	//--"SUCCESS_PART":
//				return STATUS_PART_OK;
//
//			case 2:	//--"SUCCESS_MESSSENDFILE"
//			case 3:	//--"SUCCESS_FILEn"
//				return STATUS_WAIT_DATA;
//
//			case 4:	//--"FAILED_NOFILE"
//			case 5:	//--"FAILED_LOSTFILE"
//				return STATUS_NO_DATA;
//
//			case 6:	//--"FAILED_PREFIX"
//			case 7:	//--"FAILED_COMMAND"
//			case 8:	//--"FAILED_LENGTH"
//			case 9:	//--"FAILED_OVERFLOW"
//			case 10: //--"FAILED_MD5"
//				return STATUS_NEED_RESEND;
//
//			case 11: //--"FAILED_UDTAUTORIZATION"
//				return STATUS_NEXT_SERVER;
//
//			case 12: //--"FAILED_UNKNOWN";
//			case -1: //--Не известный статус
//			default: //--Супер неизвустный статус :)
//				return STATUS_UNKNOWN_STATUS;
//			}
//			return STATUS_UNKNOWN_STATUS;
//		}
//
//		//--Если пакет с данными
//		if (InPackage.package.startsWith(PACKAGE_SIGN)) {
//			qDebug() << "messagingClient::parsePackage DATA";
//			memcpy(&InPackage.prefix, InPackage.package.data(), PACKAGE_PREFIX_LEN);
//			InPackage.data = InPackage.package.mid(PACKAGE_PREFIX_LEN, InPackage.prefix.length);
//
//			InPackage.package.remove(0, PACKAGE_DESCR_LEN + InPackage.prefix.length);
//
//			return STATUS_IS_DATA;
//		}
//
//		return STATUS_UNKNOWN_STATUS;
		return errorMessage;
	}
	//---------------------------------------------------------------------------

}

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class funcPTSMaster_result {
	int offset;
	int result;
	int packetLength;
	byte packetData[];
	ArrayList_ReceiveSemaphor semaphor;
	funcPTSMaster_result () {
		offset = 0;
		result = 0;
		packetLength = 0;
		packetData = null;
	}
}

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class ArrayList_VarMap {
	int type;
	String value;
	String mapKey;
	ArrayList_VarMap ( int t, String v, String k) {
		type = t;
		value = v;
		mapKey = k;
	}
    public void setValue( int t, String v, String k) {
		type = t;
		value = v;
		mapKey = k;
    }

}

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class ArrayList_ReceiveSemaphor {
	  String owner;
	  int task;
	  int function;
	  int idPacket;
	  String receiveName;
	  Timer timer;
	  ArrayList_ReceiveSemaphor( String owner, int task, int function, int idPacket, String receiveName, Timer timer) {
		  this.owner = owner;
		  this.task = task;
		  this.function = function;
		  this.idPacket = idPacket;
		  this.receiveName = receiveName;
		  this.timer = timer;
	}

}

///******************************
//* flags - Basket items flags
//*******************************/
//class FlagsEnum {
//    public static final int AfterPayment = 1;
//    public static final int DispenceOpened = 2;
//    public static final int DispenceFinish = 4;
//    public static final int PayRet = 16;
//    public static final int AfterSplit = 32;
//}
////---------------------------------------------------
///******************************
//* discountType - Тип скидки.
//*******************************/
//class DiscountTypeEnum {
//    public static final int SingleOpPerc = 0;		//	процентная скидка на последнюю товарную операцию.
//    public static final int SingleOpSum = 1;		//	суммовая скидка на последнюю товарную операцию.
//    public static final int SubtotPerc = 2;			//	процентная скидка на промежуточный итог.
//    public static final int SubtotSum = 3;			//	суммовая скидка на промежуточный итог.
//}
////---------------------------------------------------
///******************************
//* itemType - Тип элемента товарной корзины.
//*******************************/
//class ItemTypeEnum {
//    public static final int GoodsItemType = 0;		//	Товар.
//    public static final int DispenseItemType = 1;	//	Топливо
//    public static final int DiscountItemType = 2;	//	Скидка
//    public static final int CommentItemType = 3;	//	Коментарий
//    public static final int PaymentItemType = 4;	//	Оплата
//}
////---------------------------------------------------
//
/////////////////////////////////////////////////////////////////////////////////////////
////
/////////////////////////////////////////////////////////////////////////////////////////
//class ArrayList_BasketData {
//
//    /**Номер корзины*/
//    int basket;
//    /**Количество товарных позиций*/
//    int itemCount;
//    /**Количество оплат*/
//    int payCount;
//    /**Уникальный идентификатор продажи*/
//    int tag;
//    /**Сумма товарных позиций*/
//    double itemSum;
//    /**Сумма оплат*/
//    double paySum;
//    /**Номер чека/ Если чек не закрыт = -1*/
//    int billNum;
//    /**Код кассира*/
//    int clerk;
//    /**Флаги*/
//    int flags;
//    /**Количество элементов*/
//    int size;
//    /**Пустая корзина*/
//    bool isEmpty;
//    ArrayList<ArrayList_BasketItems> basketItem;
//    
//    ArrayList_BasketData() {
//        basket = 0;
//        itemCount = 0;
//        payCount = 0;
//        clerk = 0;
//        tag = 0;
//        flags = 0;
//        itemSum = 0;
//        paySum = 0;
//        billNum = 0;
//        basketItem = new ArrayList<ArrayList_BasketItems>();
//    }
//}
//
/////////////////////////////////////////////////////////////////////////////////////////
////	
/////////////////////////////////////////////////////////////////////////////////////////
//class ArrayList_BasketItems {
//	
//			
//    /**Уникальный ID записи*/
//    int id;
//    /**Номер корзины*/
//    int basket;
//    /**Номер в корзине*/
//    int index;
//    /**Тип єлемента*/
//    int itemType;
//    /**Сумма*/
//    double sum;
//    /**Стоимость*/
//    double cost;
//    /**Флаги*/
//    int flags;
//    /**Код кассира*/
//    int clerk;
//    /**Уникальный идентификатор продажи*/
//    int tag;
//    /**Код формы оплаты*/
//    int payForm;
//    /**Код эмитента (контрагента)*/
//    int person;
//    /**Наименование*/
//    String name;
//    /**Номер карты, ведомости ...*/
//    String doc;
//    /**Строка НСМЭП ...*/
//    String signature;
//    /**Номер чека*/
//    int billNum;
//    /**Признак возврата*/
//    bool ret;
//    /**Количество*/
//    double quantity;
//    /**Тип скидки*/
//    int discountType;
//    /**Процент или сумма*/
//    double rate;
//    /**Номер заправочного места*/
//    int pump;
//    /**Номер заправочного крана*/
//    int nozzle;
//    /** езервуар*/
//    int store;
//    /**Код товара*/
//    int product;
//    /**Отпущенный объем*/
//    double volume;
//    /**Заказанный объем*/
//    double orderVolume;
//    /**Цена*/
//    double price;
//    /**time_t*/
//    int time;
//    /**Пользовательская отметка*/
//    int utag;
//    
//    ArrayList_BasketItems() {
//        itemType = -1;
//        id = 0;
//        basket = 0;
//        index = 0;
//        flags = 0;
//        tag = 0;
//        clerk = 0;
//        sum = 0;
//        cost = 0;
//        payForm = 0;
//        person = 0;
//        billNum = 0;
//        time = 0;
//        utag = 0;
//        product = 0;
//        quantity = 0;
//        pump = 0;
//        nozzle = 0;
//        store = 0;
//        price = 0;
//        orderVolume = 0;
//        volume = 0;
//        rate = 0;
//	}
//	 
//    ArrayList_BasketItems( int id,int basket,int index,int itemType,double sum,double cost,int flags,int clerk,int tag,int payForm,int person,
//    		String name,String doc,String signature,int billNum,bool ret,double quantity,int discountType,double rate,int pump,int nozzle,int store,
//    		int product,double volume,double orderVolume,double price,int time,int utag) {
//    	this.itemType = itemType;
//    	this.id = id;
//    	this.basket = basket;
//    	this.index = index;
//    	this.flags = flags;
//    	this.tag = tag;
//    	this.clerk = clerk;
//    	this.sum = sum;
//    	this.cost = cost;
//    	this.payForm = payForm;
//    	this.person = person;
//    	this.billNum = billNum;
//    	this.time = time;
//    	this.utag = utag;
//    	this.product = product;
//    	this.quantity = quantity;
//    	this.price = price;
//    	this.pump = pump;
//    	this.nozzle = nozzle;
//    	this.store = store;
//    	this.orderVolume = orderVolume;
//    	this.volume = volume;
//    	this.rate = rate;
//    	this.discountType = discountType;
//    	this.ret = ret;
//    	this.signature = signature;
//    	this.doc = doc;
//    	this.name = name;
//	}
//
//    /**Установить признак возврата*/
//    void setRet(Boolean r) {if(r) flags |= FlagsEnum.PayRet; else flags &= ~FlagsEnum.PayRet;}
//}


/////////////////////////////////////////////////////////////////////////////////////////
////
/////////////////////////////////////////////////////////////////////////////////////////
//class PTSMasterSerial {
//	
//	int id;
//	public static ArrayList<ArrayList_ProductAssortment> mListProductAssortment;
//	public static ArrayList<ArrayList_Dispenser> mListDispenser;
//	public static ArrayList<ArrayList_Order> mListOrder;
//	public static ArrayList<ArrayList_Clerks> mListClerks;
//
//	public PTSMasterSerial()
//	{  
//		mListProductAssortment = new ArrayList<ArrayList_ProductAssortment>();
//		mListDispenser = new ArrayList<ArrayList_Dispenser>();
//		mListOrder = new ArrayList<ArrayList_Order>();
//		mListClerks = new ArrayList<ArrayList_Clerks>();
//		id = 0;
//	}
//}
//
///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
class PTSMaster 
{
	public ImageAdapter mAdapter;
	public ImageAdapter mAdapter1;
}



