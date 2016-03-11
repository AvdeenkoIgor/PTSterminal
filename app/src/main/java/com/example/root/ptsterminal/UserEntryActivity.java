package com.example.root.ptsterminal;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import adapter.ImageAdapter;

import  com.biggu.barcodescanner.client.android.Intents;

public class UserEntryActivity extends Activity implements AdapterView.OnItemSelectedListener {

    final String LOG_TAG = "UserEntryActivity_Log";
	private String nameClerk;
	private String idClerk;


//	private EditText mCard; 
	private TextView pinCode;
//    private EditText mPin;
	private static final int SCANNER_REQUEST_CODE = 0;
    private static final int IDM_REQUEST_AUTHORIZATION = 1;
    	
	private int error;
	
	String[] listClerks;
	private Intent intent;
	private Bundle extras;
   
	public final static int PTSMASTER_TASK_API_OPEN = 1;
	public final static int PTSMASTER_TASK_API_CLOSE = 2;
	public final static int PTSMASTER_TASK_API_STATUS = 3;
    		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_entry);
 
		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_authorisation));

//        mCard = (EditText)findViewById(R.id.edit_card);
//        mPin = (EditText)findViewById(R.id.edit_pin);
        pinCode = (TextView)findViewById(R.id.idClerkPinCode);
        pinCode.setText("");

//		PTSTerminal.ptsAdapter.mAdapter1 = new ImageAdapter(this, "Enable");
//        PTSTerminal.gView.setAdapter(PTSTerminal.ptsAdapter.mAdapter1);
//        PTSTerminal.ptsAdapter.mAdapter1.set_ptsMaster(PTSTerminal.ptsMaster);


        if (PTSTerminal.ptsMaster.mListClerks.size() == 0) {
            Intent intentError = new Intent();
            intentError.putExtra("Error", "no Clerks"); 
            setResult(defines.RESULT_ERROR, intentError);
        	finish();
        }
        Log.i("UserEntryActivity", "PTSTerminal.ptsMaster.mListClerks.size(): " + PTSTerminal.ptsMaster.mListClerks.size());
        nameClerk = new String(PTSTerminal.ptsMaster.mListClerks.get(0).getName());
		
		listClerks = new String[PTSTerminal.ptsMaster.mListClerks.size()];
		for (int i = 0; i < PTSTerminal.ptsMaster.mListClerks.size(); i++) {
			listClerks[i] = PTSTerminal.ptsMaster.mListClerks.get(i).getName();
		}
		
//		for (int i = 0; i < PTSTerminal.ptsMaster.mListClerks.size(); i++) {
//			Log.d(LOG_TAG, "Clerk = " + i + listClerks[i]);
//		}
		
	    final Spinner spin = (Spinner)findViewById(R.id.SpinnerClerks);
	    spin.setOnItemSelectedListener(this);
	    
	    SpinnerAdapter adapter = new SpinnerAdapter(this,
	            android.R.layout.simple_spinner_item, listClerks);
	    spin.setAdapter(adapter);	    


//        final Button btnOK = (Button)findViewById(R.id.button_send);
//        final Button btnCancel = (Button)findViewById(R.id.button_cancel);
        final Button btnScanCode = (Button)findViewById(R.id.button_scaner);
		final Button button1 = (Button) findViewById(R.id.button_1);
		final Button button2 = (Button) findViewById(R.id.button_2);
		final Button button3 = (Button) findViewById(R.id.button_3);
		final Button button4 = (Button) findViewById(R.id.button_4);
		final Button button5 = (Button) findViewById(R.id.button_5);
		final Button button6 = (Button) findViewById(R.id.button_6);
		final Button button7 = (Button) findViewById(R.id.button_7);
		final Button button8 = (Button) findViewById(R.id.button_8);
		final Button button9 = (Button) findViewById(R.id.button_9);
		final Button buttonBackspace = (Button) findViewById(R.id.button_backspace);
		final Button button0 = (Button) findViewById(R.id.button_0);
		final Button buttonEnter = (Button) findViewById(R.id.button_enter);
		
		
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("1");
				}
				else {
					pinCode.setText("1");
				}
			}
		});

		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
						pinCode.append("2");
				}
				else {
					pinCode.setText("2");
				}
			}
		});

		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("3");
				}
				else {
					pinCode.setText("3");
				}
			}
		});

		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("4");
				}
				else {
					pinCode.setText("4");
				}
			}
		});

		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("5");
				}
				else {
					pinCode.setText("5");
				}
			}
		});

		button6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("6");
				}
				else {
					pinCode.setText("6");
				}
			}
		});

		button7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("7");
				}
				else {
					pinCode.setText("7");
				}
			}
		});

		button8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("8");
				}
				else {
					pinCode.setText("8");
				}
			}
		});

		button9.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("9");
				}
				else {
					pinCode.setText("9");
				}
			}
		});

		button0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals("0"))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					if (pinCode.length() < 8)
					pinCode.append("0");
				}
				else {
					pinCode.setText("0");
				}
			}
		});

		buttonEnter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startRegClerk();
			}
		});

		buttonBackspace.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				if ((!(pinCode.getText().toString().equals(""))) && (!(pinCode.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
				if (!(pinCode.getText().toString().equals(""))) {
					String temp = pinCode.getText().toString();
//					Log.i("text", temp);
					if (temp.length() > 1) {
						temp = temp.substring(0, temp.length()-1);
//						Log.i("text_", temp);
						pinCode.setText(temp);
					}
					else {
						pinCode.setText("");
					}
				}
				else {
					pinCode.setText("");
				}
			}
		});
		
		
//        btnOK.setText(R.string.btn_send);
//
//		// ------------------------------------------------------------------------------------------------------------
//		// 																	Button OK
//		// ------------------------------------------------------------------------------------------------------------
//        btnOK.setOnClickListener(new View.OnClickListener() {         
//            @Override
//            public void onClick(View v) {
////            	// Создание объектов для запуска сервиса PTSMasterService
////                PendingIntent pi;
////                Intent intent;
////                pi = createPendingResult(PTSMASTER_TASK_API_OPEN, null, 0);
////                intent = new Intent(GetCardPinActivity.this, PTSMasterService.class)
////                	.putExtra(PTSMasterService.PARAM_TIME, 6)
////                    .putExtra(PTSMasterService.PARAM_PINTENT, pi);
////                // Запуск сервиса PTSMasterService
////                startService(intent);
//
////            	startService(new Intent(GetCardPinActivity.this, PTSMasterService.class).putExtra("time", 10));
////            	Log.d("Hadler", "PTSTerminal.hActivity = " + PTSTerminal.hActivity);
////            	if (!(mCard.getText().toString().equals("")) && !(mPin.getText().toString().equals(""))) {
//            		// --- Передача через intent данных о номере карты и пин-коде в вызвавшую активность  
//					intent = new Intent();
////					intent.putExtra("Card", mCard.getText().toString());
////					intent.putExtra("Pin", mPin.getText().toString());
//					setResult(RESULT_OK, intent);
//					finish();
////          		PTSTerminal.mListCardOperator.clear();
////              	PTSTerminal.mListCardOperator.add(new OperatorItem(mCard.getText().toString(), mPin.getText().toString()));
////  				Intent intent = new Intent(v.getContext(), UdtClientActivity.class);
////                  intent.putExtra("Exchange", 0);
////                  // эапускаем деятельнсть
////                  startActivityForResult(intent, IDM_REQUEST_AUTHORIZATION);
////            	}
////            	else {
////            		Log.w("Auth", "mCard = 0");
////            	}
//            }
//        });
//        
//		// ------------------------------------------------------------------------------------------------------------
//		// 																	Button CANCEL
//		// ------------------------------------------------------------------------------------------------------------
//        btnCancel.setOnClickListener(new View.OnClickListener() {         
//            @Override
//            public void onClick(View v) {
//            	PTSTerminal.mListCardOperator.clear();
//// DEBUG ???            
//                setResult(defines.RESULT_CUSTOM1);
//// RELEASE !!!
////                setResult(RESULT_CANCELED); 
//                finish();
//            }
//        });
//        
		// ------------------------------------------------------------------------------------------------------------
		// 																	Button ScanCode
		// ------------------------------------------------------------------------------------------------------------
        btnScanCode.setOnClickListener(new View.OnClickListener() {         
            @Override
            public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ScannerActivity.class);
				intent.putExtra(Intents.Preferences.ENABLE_BEEP, true);
				intent.putExtra(Intents.Preferences.ENABLE_VIBRATE, true);

				((Activity)v.getContext()).startActivityForResult(intent, SCANNER_REQUEST_CODE);
            }
        });
        
    }   

    public void startRegClerk() {
		Log.i(LOG_TAG, "sendClerkData Start");
		Log.v(LOG_TAG, "nameClerk = " + nameClerk + "idClerk = " + idClerk + "; pinCode = " + pinCode.getText().toString());
		
		intent = new Intent();
        intent.setClass(UserEntryActivity.this, PTSMasterClientActivity.class);
		intent.putExtra("idClerk", idClerk);
		intent.putExtra("passClerk", pinCode.getText().toString());
		intent.putExtra("function", PTSMasterService.FUNC_CLERCREG);
		// эапускаем деятельнсть
		startActivityForResult(intent, IDM_REQUEST_AUTHORIZATION);
    }
 
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_authorisation));

		if (requestCode == SCANNER_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				String result = extras.getString("SCAN_RESULT");
				Toast.makeText(getApplicationContext(), "ScanCode" + result, Toast.LENGTH_SHORT).show();
				for (int i = 0; i < listClerks.length; i++) {
					if (result.equals(listClerks[i])) {
						idClerk = String.valueOf(i);
						startRegClerk();
					}
				}
//				pinCode.setText(result);
			}
		}
		if (requestCode == IDM_REQUEST_AUTHORIZATION) {
	        switch (resultCode) {
//	            Bundle extras = data.getExtras();
	          case RESULT_OK:
                Intent intent = new Intent();
//                intent.putExtra(OperatorItem.CardCode, mCard.getText().toString()); 
//                intent.putExtra(OperatorItem.CardPin, mPin.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
               	Toast.makeText(getApplicationContext(), "Authorization OK", Toast.LENGTH_SHORT).show();
	            break;
	          case defines.RESULT_TIMEOUT:
	          	Toast.makeText(getApplicationContext(), "Timeout", Toast.LENGTH_SHORT).show();
	        	  break;
	          case defines.RESULT_ERROR:
	        	  Bundle extras = data.getExtras();
//	        	  int error = extras.getInt("Error");
//	        	  Log.i("Authorization", "Error: " + String.valueOf(error));
	                Intent intentError = new Intent();
	                int er = extras.getInt("Error");
	                intentError.putExtra("Error", er); 
	                setResult(defines.RESULT_ERROR, intentError);
	                finish();
	        	  
	        	  break;
	          case RESULT_CANCELED:
//	        	  String deviceId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
//	        	  TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//	        	  deviceId = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
//	        	Toast.makeText(getApplicationContext(), "deviceId = " + deviceId, Toast.LENGTH_SHORT).show();
	        	Toast.makeText(getApplicationContext(), "Canceled by user", Toast.LENGTH_SHORT).show();
	        	  break;
	          default:
	        	Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
	        		  
	        }
		}
	}
	
	public class SpinnerAdapter extends ArrayAdapter<String> {
	     Context context;
	     String[] items = new String[] {};
	     private int textSize=30; //initial default textsize  (might be a bit big)
		Typeface fontFaceTimes = Typeface.createFromAsset(getAssets(), "times.ttf");;

       public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
           super(context, textViewResourceId, objects);
           this.items = objects;
           this.context = context;

       }
       public SpinnerAdapter(final Context context, final int resource, final int textViewResourceId ){
           super(context, resource, textViewResourceId);
           this.items = context.getResources().getStringArray(resource);

           Toast.makeText(context, String.valueOf(this.getSpinnerTextSize()), Toast.LENGTH_LONG).show();
       }


       @Override
       public View getDropDownView(int position, View convertView,
               ViewGroup parent) {

           if (convertView == null) {
               LayoutInflater inflater = LayoutInflater.from(context);
               convertView = inflater.inflate(
                       android.R.layout.simple_spinner_item, parent, false);
           }

           TextView tv = (TextView) convertView
                   .findViewById(android.R.id.text1);
   		tv.setTypeface(fontFaceTimes, 1);
           tv.setText(items[position]);
           tv.setTextColor(Color.BLUE);
           tv.setTextSize(textSize);
           tv.setPadding(15,20,20,15);
           return convertView;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           if (convertView == null) {
               LayoutInflater inflater = LayoutInflater.from(context);
               convertView = inflater.inflate(
                       android.R.layout.simple_spinner_item, parent, false);
           }

           // android.R.id.text1 is default text view in resource of the android.
           // android.R.layout.simple_spinner_item is default layout in resources of android.

           TextView tv = (TextView) convertView
                   .findViewById(android.R.id.text1);
   		tv.setTypeface(fontFaceTimes, 1);
   		tv.setGravity(0x11);
           tv.setText(items[position]);
           tv.setTextColor(Color.BLACK);
           tv.setTextSize(textSize);
           return convertView;
       }

           //set the textsize
       public void setSpinnerTextSize(int size){

           textSize= size;
       }

           //return the textsize
       public int getSpinnerTextSize(){
           return textSize;
       }
	}	

   public void onItemSelected(
           AdapterView<?> parent, View v, int position, long id) {
	   idClerk = String.valueOf(position);
	   nameClerk = new String(listClerks[position]);
       }
       
   public void onNothingSelected(AdapterView<?> parent) {
//       idClerk.setText("");
   }

}