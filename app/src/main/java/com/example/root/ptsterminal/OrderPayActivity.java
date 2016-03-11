package com.example.root.ptsterminal;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.root.ptsterminal.UserEntryActivity.SpinnerAdapter;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderPayActivity extends Activity
	implements AdapterView.OnItemSelectedListener {

    private static final int IDM_REQUEST_PAYMENT = 101;
    private static final int IDM_REQUEST_BASKETCLOSE = 102;
    
	private static final int DIALOG_OK_ERROR = 1;
	private static final int DIALOG_YES_NO_MESSAGE = 2;

    private static int orderStore;
    private static int orderDispenser;
    private static int orderNozzle;
    private static int orderProductArticle;
    private static int orderProductAmount;
    private static String paySumm;
    private static String orderProductPrice;
    private boolean pressedKey = false;

    private TextView idPaySumm;
	private TextView idProductPrice;
	private TextView idProductUnit;
	private TextView idEnteredCash;
	Typeface fontFaceHelvetica;
	Typeface fontFaceDigital;
	int payBasket;
	String errorMessage;
	
	private Intent intent;
	private Bundle extras;
	private double paySum;
	private double itemSum;
	private double different;
	private double cashback;
	private String messagePaid;

	String[] listPayForms;
//	final String[] listPayForms = {"Наличные", "Карта QR-code", "Карта RFID"};
	private String idPayFormName;
	private int idPayFormNumber;
	
	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.order_pay);

		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_pay_order));

		fontFaceHelvetica = Typeface.createFromAsset(getAssets(), "Helvetica_Bold.ttf");
		fontFaceDigital = Typeface.createFromAsset(getAssets(), "DigitalBold.ttf");
		idPaySumm = (TextView) findViewById(R.id.idPaySumm);
//		idPaySumm.setTypeface(fontFaceHelvetica);
		idEnteredCash = (TextView) findViewById(R.id.idEnteredCash);
		idEnteredCash.setTypeface(fontFaceDigital);

        Bundle extras = getIntent().getExtras();
        paySumm = extras.getString("summ");
        payBasket = extras.getInt("basket");

        
		idPaySumm.setText(paySumm + " " + getResources().getText(R.string.textOrderProductSummUnit));
		if (!pressedKey) {
			idEnteredCash.setText(paySumm);
		}

		
//        orderProductAmount = extras.getInt("amount");
//        orderNozzle = extras.getInt("nozzle");
////        orderNozzle = Integer.valueOf(PTSTerminal.ptsMaster.mListDispenser.get(orderDispenser).getDispenserNozzle());
//        if ((orderDispenser < 0) || (orderNozzle < 0)) {
//            setResult(RESULT_CANCELED, intent);
//            finish();
//        }
//        orderStore = Integer.valueOf(PTSTerminal.ptsMaster.mListDispenserCfg.get(orderDispenser).get(orderNozzle));
//        orderProductArticle = Integer.valueOf(PTSTerminal.ptsMaster.mListStore.get(orderStore).getArticleId());
//        orderProductName = PTSTerminal.ptsMaster.mListProductAssortment.get(orderProductArticle).getName();
//        orderProductPrice = PTSTerminal.ptsMaster.mListProductAssortment.get(orderProductArticle).getPrice();
//        
//        idProductName.setText(orderProductName);
//        idProductPrice.setText(orderProductPrice);
        
		final Button button1 = (Button) findViewById(R.id.button_1);
		button1.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("1");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("1");
					}
					else {
						idEnteredCash.setText("1");
					}
				}
		    }
		});
		final Button button2 = (Button) findViewById(R.id.button_2);
		button2.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("2");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("2");
					}
					else {
						idEnteredCash.setText("2");
					}
				}
		    }
		});
		final Button button3 = (Button) findViewById(R.id.button_3);
		button3.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("3");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("3");
					}
					else {
						idEnteredCash.setText("3");
					}
				}
		    }
		});
		final Button button4 = (Button) findViewById(R.id.button_4);
		button4.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("4");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("4");
					}
					else {
						idEnteredCash.setText("4");
					}
				}
		    }
		});
		final Button button5 = (Button) findViewById(R.id.button_5);
		button5.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("5");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("5");
					}
					else {
						idEnteredCash.setText("5");
					}
				}
		    }
		});
		final Button button6 = (Button) findViewById(R.id.button_6);
		button6.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("6");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("6");
					}
					else {
						idEnteredCash.setText("6");
					}
				}
		    }
		});
		final Button button7 = (Button) findViewById(R.id.button_7);
		button7.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("7");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("7");
					}
					else {
						idEnteredCash.setText("7");
					}
				}
		    }
		});
		final Button button8 = (Button) findViewById(R.id.button_8);
		button8.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("8");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("8");
					}
					else {
						idEnteredCash.setText("8");
					}
				}
		    }
		});
		final Button button9 = (Button) findViewById(R.id.button_9);
		button9.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("9");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("9");
					}
					else {
						idEnteredCash.setText("9");
					}
				}
		    }
		});
		
		final Button button0point = (Button) findViewById(R.id.button_0);
		button0point.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("0");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.length() < 8)
							idEnteredCash.append("0");
					}
					else {
						idEnteredCash.setText("0");
					}
				}
		    }
		});
		button0point.setOnLongClickListener(new Button.OnLongClickListener(){
		    @Override
		    public boolean onLongClick(View v) {
				if (!pressedKey) {
					idEnteredCash.setText("0.");
			    	pressedKey = true;
				}
				else {
					if (!(idEnteredCash.getText().toString().equals("0"))){
						if (idEnteredCash.getText().toString().indexOf(".") == -1) {
							if (idEnteredCash.length() < 8) idEnteredCash.append(".");
						}
					}
					else {
						idEnteredCash.setText("0.");
					}
				}
		        return true;
		    }
		});

		final Button buttonBackspace = (Button) findViewById(R.id.button_clear);
		buttonBackspace.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!pressedKey) {
			    	pressedKey = true;
				}
				if ((!(idEnteredCash.getText().toString().equals(""))) && (!(idEnteredCash.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					String temp = idEnteredCash.getText().toString();
					Log.i("text", temp);
					if (temp.length() > 1) {
						temp = temp.substring(0, temp.length()-1);
						Log.i("text_", temp);
						idEnteredCash.setText(temp);
					}
					else {
						idEnteredCash.setText("0");
					}
				}
				else {
					idEnteredCash.setText("0");
				}
			}
		});
		buttonBackspace.setOnLongClickListener(new Button.OnLongClickListener(){
		    @Override
		    public boolean onLongClick(View v) {
				if (!pressedKey) {
			    	pressedKey = true;
				}
				idEnteredCash.setText("0");
		        return true;
		    }
		});
		
		final Button buttonEnter = (Button) findViewById(R.id.button_enter);
		buttonEnter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!(idEnteredCash.getText().toString().equals(""))) {
//					startBasketClose();
					startOrderPay();
				}
			}
		});
		
	    listPayForms = new String[PTSTerminal.ptsMaster.mListPayForm.size()];
		for (int i = 0; i < PTSTerminal.ptsMaster.mListPayForm.size(); i++) {
			listPayForms[i] = PTSTerminal.ptsMaster.mListPayForm.get(i).getName();
		}
		
//		for (int i = 0; i < PTSTerminal.ptsMaster.mListPayForm.size(); i++) {
//			Log.d("OrderPayActivity", "PayForm[" + i + "] = " + listPayForms[i]);
//		}
		
		idPayFormName = new String(listPayForms[0]);
    	idPayFormNumber = Integer.valueOf(PTSTerminal.ptsMaster.mListPayForm.get(0).getId());
		
	    final Spinner spin = (Spinner)findViewById(R.id.SpinnerPayForm);
	    spin.setOnItemSelectedListener(this);
	    
	    SpinnerAdapter adapter = new SpinnerAdapter(this,
	            android.R.layout.simple_spinner_item, listPayForms);
	    spin.setAdapter(adapter);	    



		//(you can also pass in a string array resource like this:)
		/*SpinnerAdapter adapter = new SpinnerAdapter(this,  R.layout.simple_spinner_item, 
		   getResources().getStringArray(R.array.my_string_array_resource));*/
	    
//		final Button button1 = (Button) findViewById(R.id.button_pay);
//		button1.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Log.i("PayForm", idPayForm);
//			}
//		});
//
//		final Button button2 = (Button) findViewById(R.id.button_cancel);
//		button2.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//			}
//		});

	}

	public class SpinnerAdapter extends ArrayAdapter<String> {
	     Context context;
	     String[] items = new String[] {};
	     private int textSize=32; //initial default textsize  (might be a bit big)
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
    	idPayFormName = new String(listPayForms[position]);
    	idPayFormNumber = Integer.valueOf(PTSTerminal.ptsMaster.mListPayForm.get(position).getId());
    }
        
    public void onNothingSelected(AdapterView<?> parent) {
//        idPayForm.setText("");
    }

    public void startOrderPay() {
        if (Double.valueOf(idEnteredCash.getText().toString()) == 0) {
			errorMessage = getResources().getString(R.string.messErrror_null_pay);
			showDialog(DIALOG_OK_ERROR);
			return;
        }
		Log.i("OrderPay", "startOrderPay Start");
		Log.i("OrderPaymentActivity", "idPayFormNumber = " + idPayFormNumber);
		Log.i("idEnteredCash", idEnteredCash.getText().toString());
//        PTSTerminal.ptsMaster.OrderBasketItem.volume = 0.0;
//        PTSTerminal.ptsMaster.OrderBasketItem.utag = 0;
//        PTSTerminal.ptsMaster.OrderBasketItem.time = 0;
//        PTSTerminal.ptsMaster.OrderBasketItem.tag = 0;
        PTSTerminal.ptsMaster.OrderBasketItem.sum = Double.valueOf(idEnteredCash.getText().toString());
//        PTSTerminal.ptsMaster.OrderBasketItem.signature = "";
//        PTSTerminal.ptsMaster.OrderBasketItem.rate = 0.0;
//        PTSTerminal.ptsMaster.OrderBasketItem.quantity = 0.0;
//        PTSTerminal.ptsMaster.OrderBasketItem.pump =  orderDispenser;//
//        PTSTerminal.ptsMaster.OrderBasketItem.product = orderProductArticle;
//        PTSTerminal.ptsMaster.OrderBasketItem.price = -1.0;//
//        PTSTerminal.ptsMaster.OrderBasketItem.person = 0;
        PTSTerminal.ptsMaster.OrderBasketItem.payForm = idPayFormNumber;
//        PTSTerminal.ptsMaster.OrderBasketItem.orderVolume = amount;//
//        PTSTerminal.ptsMaster.OrderBasketItem.nozzle = orderNozzle;//
//        PTSTerminal.ptsMaster.OrderBasketItem.name = "";
        PTSTerminal.ptsMaster.OrderBasketItem.itemType = 4;//
//        PTSTerminal.ptsMaster.OrderBasketItem.index = 0;
//        PTSTerminal.ptsMaster.OrderBasketItem.id = 0;
//        PTSTerminal.ptsMaster.OrderBasketItem.flags = 0;
//        PTSTerminal.ptsMaster.OrderBasketItem.doc = "";
//        PTSTerminal.ptsMaster.OrderBasketItem.discountType = 0;
//        PTSTerminal.ptsMaster.OrderBasketItem.cost = 0.0;
//        PTSTerminal.ptsMaster.OrderBasketItem.comment = "";
//        PTSTerminal.ptsMaster.OrderBasketItem.clerk = 0;
//        PTSTerminal.ptsMaster.OrderBasketItem.billNum = -1;
        PTSTerminal.ptsMaster.OrderBasketItem.basket = payBasket;
		
		intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
//		intent.putExtra("idClerk", idClerk);
		intent.putExtra("function", PTSMasterService.FUNC_BASKETPAYMENT);
		// эапускаем деятельнсть
		startActivityForResult(intent, IDM_REQUEST_PAYMENT);
    }
 
    public void startBasketClose() {
		Log.i("OrderPay", "startBasketClose Start");
		
		intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
//		intent.putExtra("idClerk", idClerk);
		intent.putExtra("function", PTSMasterService.FUNC_BASKETCLOSE);
		// эапускаем деятельнсть
		startActivityForResult(intent, IDM_REQUEST_BASKETCLOSE);
    }
 
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_OK_ERROR:
				return new AlertDialog.Builder(this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle(getResources().getString(R.string.title_errror))
			        .setMessage(errorMessage)
					.setPositiveButton(R.string.btn_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).create();
		}
		return null;
	}
	
    
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_pay_order));

        switch (resultCode) {
        // ===========================================================================================
        // ==================================================================== RESULT_OK 
  	  	// ===========================================================================================
          case RESULT_OK:
    			Log.i("OrderProductActivity", "RESULT_OK");
        	  switch (requestCode) {
//                case PTSMASTER_ORDERCONFIRM:
//                	break;
                case IDM_REQUEST_PAYMENT:
                	// Оплачено клиентом
                	paySum = Double.valueOf(PTSTerminal.ptsMaster.PaymentAnswer.getPaySum());
                	// Требуется к оплате
                	itemSum = Double.valueOf(PTSTerminal.ptsMaster.PaymentAnswer.getItemSum()); 
                	if (paySum < itemSum) {
                		different = myLib.round((itemSum - paySum), 2, BigDecimal.ROUND_HALF_UP);
                		cashback = 0.0;
                	}
                	else {
                		cashback = myLib.round((paySum - itemSum), 2, BigDecimal.ROUND_HALF_UP);
                		different = 0.0;
                	}

    				Log.v("OrderProductActivity", "paySum: " + paySum);
    				Log.v("OrderProductActivity", "itemSum: " + itemSum);
                	if (paySum > itemSum) {
        				Log.v("OrderProductActivity", "Заказ оплачен\nНеобходимо дать сдачу: " + (paySum - itemSum));
    		        	messagePaid = getResources().getText(R.string.textOrderHasBeenPaid) + "\n"
    							+ getResources().getText(R.string.textNeedCashback) + " " + String.valueOf(cashback) + " " + getResources().getText(R.string.textOrderProductSummUnit) + "\n\n";
                	}
                	else {
                    	if (paySum == itemSum) {
                    		messagePaid = getResources().getText(R.string.textOrderHasBeenPaid) + "\n\n";
                    	}
                    	else {
                    		messagePaid = getResources().getText(R.string.textOrderNotHasBeenPaid) + "\n\n";
                    	}
                	}
    		    	// ================================================================ Диалоговое окно
    		        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    		        
    		        // Заголовок и текст
    		        alertbox.setTitle(R.string.title_order_paid);
    		        String TextToast = null;
    		        	TextToast = messagePaid;
    		        alertbox.setMessage(TextToast);
    		        
    		        // Добавляем кнопку 
    		        alertbox.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
    		            @Override
	    				public void onClick(DialogInterface arg0, int arg1) {
    		            	if (paySum >= itemSum) {
	    	            		intent = new Intent();
	    		                setResult(RESULT_OK, intent);
	    		                finish();
	    		            }
    		            	else {
    		            		idPaySumm.setText(String.valueOf(different) + " " + getResources().getText(R.string.textOrderProductSummUnit));
   		            			idEnteredCash.setText(String.valueOf(different));
   		            			pressedKey = false;
    		            	}
                    	}
    		        });

    		        // показываем окно
    		        alertbox.show();
    		        // ================================================================     	
    		        break;
//                case IDM_REQUEST_BASKETCLOSE:
//					startOrderPay();					
//                	break;
        	  }
            break;
		// ===========================================================================================
		// ==================================================================== RESULT_ERROR 
		// ===========================================================================================
          case defines.RESULT_ERROR:
        	  extras = data.getExtras();
  			Log.e("OrderProductActivity", "RESULT_ERROR");
  			switch (requestCode) {
	          case IDM_REQUEST_PAYMENT:
	          case IDM_REQUEST_BASKETCLOSE:
	      		intent = new Intent();
	            setResult(RESULT_CANCELED, intent);
	            finish();
	          	break;
  			}
        	break;
      		// ===========================================================================================
      		// ==================================================================== RESULT_PROTOCOL_ERROR 
      		// ===========================================================================================
          case defines.RESULT_PROTOCOL_ERROR:
			extras = data.getExtras();
			errorMessage = extras.getString("errorMessage");
			Log.e("DispenserControlAct-ty", "errorMessage: " + errorMessage);
			showDialog(DIALOG_OK_ERROR);
			break;
        	  
		  // ===========================================================================================
		  // ========================================================== RESULT_CANCELED
		  // ===========================================================================================
          case RESULT_CANCELED:
  			Log.w("OrderProductActivity", "RESULT_CANCELED");
              switch (requestCode) {
	              case IDM_REQUEST_PAYMENT:
	              case IDM_REQUEST_BASKETCLOSE:
		        	  break;
              }
              break;
          // ===========================================================================================
          // ================================================= RESULT_TIMEOUT 
          // ===========================================================================================
          case defines.RESULT_TIMEOUT:
          	  break;
    	  // ===========================================================================================
          // ================================================= default
    	  // ===========================================================================================
          default:
        	  break;
        		  
        }
    }

}