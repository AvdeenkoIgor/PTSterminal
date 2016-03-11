package com.example.root.ptsterminal;

import java.math.BigDecimal;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

public class OrderProductActivity extends Activity {

//    private static final int PAGE_ORDER_PAY = 100;
    private static final int PTSMASTER_ADDBASKET = 101;
    private static final int PTSMASTER_ORDERCONFIRM = 102;
 
	private static final int DIALOG_OK_ERROR = 1;
	private static final int DIALOG_YES_NO_MESSAGE = 2;

    private static int orderStore;
    private static int orderDispenser;
    private static int orderNozzle;
    private static int orderProductArticle;
    private static String orderProductName;
    private static String orderProductPrice;

    private TextView idProduct;
	private TextView idProductAmount;
	private TextView idProductPrice;
	private TextView idProductUnit;
	private TextView idProductName;
	Typeface fontFaceHelvetica;
	Typeface fontFaceDigital;
	
//	private int state;
//	private int error;

	private Intent intent;
	private Bundle extras;
	
	private Double amount;
	private Double summ;
	private Double price;
	private String amountString;
	private Double fullTank = 99999.0;

	String errorMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_entry);

		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_make_order));

		fontFaceHelvetica = Typeface.createFromAsset(getAssets(), "Helvetica_Bold.ttf");
		fontFaceDigital = Typeface.createFromAsset(getAssets(), "DigitalBold.ttf");
		idProduct = (TextView) findViewById(R.id.idProduct);
		idProduct.setTypeface(fontFaceDigital);
		idProductName = (TextView) findViewById(R.id.idProductName);
//		idProductName.setTypeface(fontFaceHelvetica);
		idProductPrice = (TextView) findViewById(R.id.idProductPrice);
//		idProductPrice.setTypeface(fontFaceHelvetica);
		idProductAmount = (TextView) findViewById(R.id.idProductAmount);
		idProductAmount.setTypeface(fontFaceDigital);
		idProductUnit = (TextView) findViewById(R.id.idButtonLitresMoney);

        Bundle extras = getIntent().getExtras();
        orderDispenser = extras.getInt("pump");
        orderNozzle = extras.getInt("nozzle");
//        orderNozzle = Integer.valueOf(PTSTerminal.ptsMaster.mListDispenser.get(orderDispenser).getDispenserNozzle());
        if ((orderDispenser < 0) || (orderNozzle < 0)) {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        orderStore = Integer.valueOf(PTSTerminal.ptsMaster.mListDispenserCfg.get(orderDispenser).get(orderNozzle).getStore());
        orderProductArticle = Integer.valueOf(PTSTerminal.ptsMaster.mListStore.get(orderStore).getArticleId());
        orderProductName = PTSTerminal.ptsMaster.mListProductAssortment.get(orderProductArticle).getName();
        orderProductPrice = PTSTerminal.ptsMaster.mListProductAssortment.get(orderProductArticle).getPrice();
        
        idProduct.setText(String.valueOf(orderProductArticle));
        idProductName.setText(orderProductName);
		idProductPrice.setText(String.format("%5.2f", Double.valueOf(orderProductPrice)).replace(',', '.'));
        
//		if (idProductName.length() > 10) {
//			idProductName.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
//		}
//		else {
//			idProductName.setGravity(Gravity.CENTER);
//		}
			
//        idProduct.setText();
//        idProductPrice.setText(PTSTerminal.ptsMaster.mListDispenser.get(orderDispenser).getDispenserNozzle());
//		Log.i("###","Dispenser-" + i + ": Nozzle-" + n + ": store-" + ptsMaster.mListDispenserCfg.get(i).get(n)
//		+ ": Product-" + ptsMaster.mListProductAssortment.get(Integer.valueOf(ptsMaster.mListStore.get(Integer.valueOf(ptsMaster.mListDispenserCfg.get(i).get(n))).getArticleId())).getName());

		
		
//		mAdapter = new ImageAdapter(this, NOZZLE);
//		gridView = (GridView) findViewById(R.id.gridViewInTitle);
//		gridView.setNumColumns(1);
//		
//		gridView.setAdapter(mAdapter);
        
//			public void idProductUnitButton. onClick(View v) {
//				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
//					if (idProductAmount.length() < 8)
//					idProductAmount.append("1");
//				}
//				else {
//					idProductAmount.setText("1");
//				}
//			}
 
		final Button button1 = (Button) findViewById(R.id.button_1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("1");
				}
				else {
					idProductAmount.setText("1");
				}
			}
		});

		final Button button2 = (Button) findViewById(R.id.button_2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("2");
				}
				else {
					idProductAmount.setText("2");
				}
			}
		});

		final Button button3 = (Button) findViewById(R.id.button_3);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("3");
				}
				else {
					idProductAmount.setText("3");
				}
			}
		});

		final Button buttonLitres = (Button) findViewById(R.id.button_fulltank);
		buttonLitres.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				idProductAmount.setText(getString(R.string.stringProductAmountFull));
			}
		});

		final Button button4 = (Button) findViewById(R.id.button_4);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("4");
				}
				else {
					idProductAmount.setText("4");
				}
			}
		});

		final Button button5 = (Button) findViewById(R.id.button_5);
		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("5");
				}
				else {
					idProductAmount.setText("5");
				}
			}
		});

		final Button button6 = (Button) findViewById(R.id.button_6);
		button6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("6");
				}
				else {
					idProductAmount.setText("6");
				}
			}
		});

		final Button buttonCash = (Button) findViewById(R.id.idButtonCancelClear);
		buttonCash.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				idProductAmount.setText("0");
			}
		});

		final Button button7 = (Button) findViewById(R.id.button_7);
		button7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("7");
				}
				else {
					idProductAmount.setText("7");
				}
			}
		});

		final Button button8 = (Button) findViewById(R.id.button_8);
		button8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("8");
				}
				else {
					idProductAmount.setText("8");
				}
			}
		});

		final Button button9 = (Button) findViewById(R.id.button_9);
		button9.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("9");
				}
				else {
					idProductAmount.setText("9");
				}
			}
		});

		final Button buttonBackspace = (Button) findViewById(R.id.button_backspace);
		buttonBackspace.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals(""))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					String temp = idProductAmount.getText().toString();
					Log.i("text", temp);
					if (temp.length() > 1) {
						temp = temp.substring(0, temp.length()-1);
						Log.i("text_", temp);
						idProductAmount.setText(temp);
					}
					else {
						idProductAmount.setText("0");
					}
				}
				else {
					idProductAmount.setText("0");
				}
			}
		});

		final Button button00 = (Button) findViewById(R.id.button_00);
		button00.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 7)
					idProductAmount.append("00");
				}
				else {
					idProductAmount.setText("00");
				}
			}
		});

		final Button buttonPoint = (Button) findViewById(R.id.button_point);
		buttonPoint.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				String temp = "abcdefghi";
//				if(temp.indexOf("b")!=-1)
//				{
//				   System.out.println("there is 'b' in temp string");
//				}
//				else
//				{
//				   System.out.println("there is no 'b' in temp string");
//				}

				if (idProductAmount.getText().toString().indexOf(".")!=-1) {
					return;
				}
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append(".");
				}
				else {
					idProductAmount.setText("0.");
				}
			}
		});

		final Button button0 = (Button) findViewById(R.id.button_0);
		button0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((!(idProductAmount.getText().toString().equals("0"))) && (!(idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull)))) ){
					if (idProductAmount.length() < 8)
					idProductAmount.append("0");
				}
				else {
					idProductAmount.setText("0");
				}
			}
		});

		final Button buttonEnter = (Button) findViewById(R.id.button_enter);
		buttonEnter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!(idProductAmount.getText().toString().equals(""))) {
					Log.i("ProductAmount", idProductAmount.getText().toString());
					Log.i("ProductPrice", idProductPrice.getText().toString());
					
					amountString = idProductAmount.getText().toString();
					if (idProductAmount.getText().toString().equals(getString(R.string.stringProductAmountFull))) {
						summ = 0.0;
						amount = fullTank;
					}
					else {
						// Конвертация деньги-литры
						price = Double.valueOf(orderProductPrice);
						if (idProductUnit.getText().toString().equals(getString(R.string.order_unit_money))) {
							if (price > 0.0) {
								summ = myLib.round(Double.valueOf(amountString), 2, BigDecimal.ROUND_HALF_UP);
								amount = myLib.round((summ/price), 2, BigDecimal.ROUND_HALF_UP);
								summ = myLib.round((amount*price), 2, BigDecimal.ROUND_HALF_UP);
							}
							else {
								summ = 0.0;
								amount = 0.0;
							}
						}
						else {
							amount = myLib.round(Double.valueOf(amountString), 2, BigDecimal.ROUND_HALF_UP);
							summ = myLib.round((amount*price), 2, BigDecimal.ROUND_HALF_UP);
						}
					}
					
					showOrderConfirm();
				}
			}
		});
		
		final Button buttonLitresMoney = (Button) findViewById(R.id.idButtonLitresMoney);
		buttonLitresMoney.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (idProductUnit.getText().toString().equals(getString(R.string.order_unit_litres))) {
					idProductUnit.setText(getString(R.string.order_unit_money));
				}
				else {
					idProductUnit.setText(getString(R.string.order_unit_litres));
				}
			}
		});


	}

//	// #####################################################################################################
//	// Запуск активности оплаты заказа
//	// #####################################################################################################
//    public void startPayment(int dispenser, int nozzle) {
//    	intent = new Intent();
//    	intent.setClass(this, OrderPayActivity.class);
//		intent.putExtra("pump", dispenser);
//		intent.putExtra("nozzle", nozzle);
//        intent.putExtra("amount", idProductAmount.getText().toString()); 
//    	// эапускаем деятельнсть 
//    	startActivityForResult(intent, PAGE_ORDER_PAY);
//    }
 
	// #####################################################################################################
	// Добавление заказа в корзину
	// #####################################################################################################
	public void addBasket() {
		intent = new Intent();
		intent.setClass(this, PTSMasterClientActivity.class);
		intent.putExtra("function", PTSMasterService.FUNC_BASKETADD);
		// эапускаем деятельнсть
		startActivityForResult(intent, PTSMASTER_ADDBASKET);
	}

	// #####################################################################################################
	// Добавление заказа в корзину
	// #####################################################################################################
    private void showOrderConfirm () {
    	
    	// ================================================================ Диалоговое окно
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        
        if (amount == 0) {
			errorMessage = getResources().getString(R.string.messErrror_null_order);
			showDialog(DIALOG_OK_ERROR);
			return;
        }
        // Заголовок и текст
        alertbox.setTitle(R.string.title_order_confirm);
        String TextToast = null;
        if (amount == fullTank) {
        	TextToast = getResources().getText(R.string.textOrderProductName) + " " + orderProductName + "\n"
					+ getResources().getText(R.string.textOrderProductAmount) + " " + getResources().getText(R.string.textOrderFullTank) + "\n\n";
        }
        else {
            TextToast = getResources().getText(R.string.textOrderProductName) + " " + orderProductName + "\n"
					+ getResources().getText(R.string.textOrderProductAmount) + " " + String.valueOf(amount) + " " + getResources().getText(R.string.textOrderProductAmountUnit) + "\n\n" 
					+ getResources().getText(R.string.textOrderProductSumm) + " " + String.valueOf(summ) + " " + getResources().getText(R.string.textOrderProductSummUnit);
        }
        alertbox.setMessage(TextToast);
        
        // Добавляем кнопку 
        alertbox.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface arg0, int arg1) {
                // закрываем текущюю Activity
//		        PTSTerminal.ptsMaster.OrderBasketItem.volume = 0.0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.utag = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.time = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.tag = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.sum = 0.0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.signature = "";
//		        PTSTerminal.ptsMaster.OrderBasketItem.rate = 0.0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.quantity = 0.0;
		        PTSTerminal.ptsMaster.OrderBasketItem.pump =  orderDispenser;//
//		        PTSTerminal.ptsMaster.OrderBasketItem.product = orderProductArticle;
		        PTSTerminal.ptsMaster.OrderBasketItem.price = -1.0;//
//		        PTSTerminal.ptsMaster.OrderBasketItem.person = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.payForm = 0;
		        PTSTerminal.ptsMaster.OrderBasketItem.orderVolume = amount;//
		        PTSTerminal.ptsMaster.OrderBasketItem.nozzle = orderNozzle;//
//		        PTSTerminal.ptsMaster.OrderBasketItem.name = "";
		        PTSTerminal.ptsMaster.OrderBasketItem.itemType = 1;//
//		        PTSTerminal.ptsMaster.OrderBasketItem.index = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.id = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.flags = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.doc = "";
//		        PTSTerminal.ptsMaster.OrderBasketItem.discountType = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.cost = 0.0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.comment = "";
//		        PTSTerminal.ptsMaster.OrderBasketItem.clerk = 0;
//		        PTSTerminal.ptsMaster.OrderBasketItem.billNum = -1;
		        PTSTerminal.ptsMaster.OrderBasketItem.basket = orderDispenser + 1;
				addBasket();
            }
        });

        // Добавляем кнопку 
        alertbox.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        // показываем окно
        alertbox.show();
     // ================================================================     	
    }


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_OK_ERROR:
				return new AlertDialog.Builder(OrderProductActivity.this)
						.setIcon(R.drawable.alert_dialog_icon)
						// .setTitle(getResources().getStringArray(R.array.error_udt)[error])
						.setTitle(getResources().getString(R.string.title_errror))
				        .setMessage(errorMessage)

						.setPositiveButton(R.string.btn_ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).create();
			case DIALOG_YES_NO_MESSAGE:
				return new AlertDialog.Builder(OrderProductActivity.this)
						.setIcon(R.drawable.alert_dialog_icon)
						.setTitle(	"DIALOG_YES_NO_MESSAGE" 
									)
						.setPositiveButton(R.string.btn_ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								})
						.setNegativeButton(R.string.btn_cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										idProductAmount.setText("0");
									}
								}).create();
		}
		return null;
	}
	
	
	
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_make_order));

        switch (resultCode) {
        // ===========================================================================================
        // ==================================================================== RESULT_OK 
  	  	// ===========================================================================================
          case RESULT_OK:
    			Log.i("OrderProductActivity", "RESULT_OK");
        	  switch (requestCode) {
//                case PAGE_ORDER_PAY:
//					extras = data.getExtras();
//	                setResult(RESULT_OK, intent);
//	                finish();
//                	break;
                case PTSMASTER_ORDERCONFIRM:
                	break;
                case PTSMASTER_ADDBASKET:
            		intent = new Intent();
            		intent.putExtra("pump", orderDispenser);
					Log.v("OrderProduct", "pump = " + orderDispenser);
	                setResult(RESULT_OK, intent);
	                finish();
                	break;
        	  }
            break;
		// ===========================================================================================
		// ==================================================================== RESULT_ERROR 
		// ===========================================================================================
          case defines.RESULT_ERROR:
        	  extras = data.getExtras();
  			Log.e("OrderProductActivity", "RESULT_ERROR");
        	  switch (requestCode) {
                case PTSMASTER_ORDERCONFIRM:
                	break;
                case PTSMASTER_ADDBASKET:
                	break;
        	  }
        	  break;
      		// ===========================================================================================
      		// ==================================================================== RESULT_PROTOCOL_ERROR 
      		// ===========================================================================================
          case defines.RESULT_PROTOCOL_ERROR:
			extras = data.getExtras();
			errorMessage = extras.getString("errorMessage");
			Log.e("OrderProductActivity", "errorMessage: " + errorMessage);
			showDialog(DIALOG_OK_ERROR);
			break;
        	  
		  // ===========================================================================================
		  // ========================================================== RESULT_CANCELED
		  // ===========================================================================================
          case RESULT_CANCELED:
  			Log.w("OrderProductActivity", "RESULT_CANCELED");
              switch (requestCode) {
	              case PTSMASTER_ORDERCONFIRM:
//	            	  if (idProductAmount.getText().equals("99999.0")) {
//	            		  idProductAmount.setText(getString(R.string.stringProductAmountFull));
//	            	  }
		        	  break;
	              case PTSMASTER_ADDBASKET:
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