package com.example.root.ptsterminal;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class DispenserControlActivity extends Activity {

    private static final int PAGE_ORDER_PAY = 101;
    private static final int PAGE_PUMP_RESUME = 102;
    private static final int PAGE_PUMP_STOP = 103;
    private static final int PAGE_REQUEST_BASKETCLEAR = 104;
    private static final int PAGE_REQUEST_BASKETCLOSE = 105;
	private static final int DIALOG_OK_ERROR = 1;
	private static final int DIALOG_YES_NO_MESSAGE = 2;

	private static final int STATE_EDLE = 0;
	private static final int STATE_PAY = 1;
	private static final int STATE_CLEAR_BASKET = 2;
	
	private static int state = STATE_EDLE;
    private static int orderStore;
    private static int orderDispenser;
    private static int orderNozzle;
    private static int orderProductArticle;
//    private static String orderProductName;
//    private static String orderProductAmount;

//    private TextView textProductSumm;
//	private TextView textProductAmount;
//	private TextView textProductPrice;
    private TextView idProductSumm;
	private TextView idProductAmount;
	private TextView idProductPrice;
	private TextView idOrderProductName;
	private TextView idOrderProductAmount;
	private TextView idTextDispenserControlMessage;
	private ImageView idImageOrderInfo;
	private ImageView idImageNozzleStatus;
	Typeface fontFaceHelvetica;
	Typeface fontFaceDigital;
	
	private Intent intent;
	private Bundle extras;
	
	private double orderVolume;
	private double volume = 0.0;
	private double paySum = 0.0;
	private double itemSum = 0.0;

	private int orderBasket = 0;
	private String summ = "0.0";
	private String cost = "0.0";
	String errorMessage;
	
	Button button_pay;
	Button button_clear;
	Button button_start;
	Button button_stop;
	boolean orderEnded; 
	Animation anim;
	Animation anim2;
	
	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.dispenser_control);

		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_disp_control));

		fontFaceHelvetica = Typeface.createFromAsset(getAssets(), "Helvetica_Bold.ttf");
		fontFaceDigital = Typeface.createFromAsset(getAssets(), "DigitalBold.ttf");
		
		idProductSumm = (TextView) findViewById(R.id.idDispenserControlSumm);
		idProductAmount = (TextView) findViewById(R.id.idDispenserControlLitres);
		idProductPrice = (TextView) findViewById(R.id.idDispenserControlPrice);
		idOrderProductName = (TextView) findViewById(R.id.idDispenserControlOrderName);
		idOrderProductAmount = (TextView) findViewById(R.id.idDispenserControlOrderAmount);
		idImageOrderInfo = (ImageView) findViewById(R.id.line_item_image);
		idImageNozzleStatus = (ImageView) findViewById(R.id.idImageNozzleStatus);
		idTextDispenserControlMessage = (TextView) findViewById(R.id.idTextDispenserControlMessage);
		
		anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(200); //You can manage the time of the blink with this parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);

		anim2 = new AlphaAnimation(0.0f, 1.0f);
		anim2.setDuration(1000); //You can manage the time of the blink with this parameter
		anim2.setStartOffset(20);
		anim2.setRepeatMode(Animation.REVERSE);
		anim2.setRepeatCount(Animation.INFINITE);

		idTextDispenserControlMessage.clearAnimation();
		idTextDispenserControlMessage.setText("");
		
//		textProductSumm = (TextView) findViewById(R.id.idDispenserControlSumm_text);
//		textProductAmount = (TextView) findViewById(R.id.idDispenserControlLitres_text);
//		textProductPrice = (TextView) findViewById(R.id.idDispenserControlPrice_text);
		idProductSumm.setTypeface(fontFaceDigital);
		idProductPrice.setTypeface(fontFaceDigital);
		idProductAmount.setTypeface(fontFaceDigital);
//		textProductSumm.setTypeface(fontFaceHelvetica);
//		textProductPrice.setTypeface(fontFaceHelvetica);
//		textProductAmount.setTypeface(fontFaceHelvetica);
		
		
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//            	Log.d("ClientHandler", "Message received");
            	if (msg.getData().containsKey("signal")) {
	                int result = msg.getData().getInt("signal");
	                updateView();
            	}
            }
        };
        
        PTSTerminal.setHandlerFromDispenserControlActivity(mHandler);
		
//
        Bundle extras = getIntent().getExtras();
        orderDispenser = extras.getInt("pump");
//        orderNozzle = extras.getInt("nozzle");
////        orderNozzle = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserNozzle());
//        if ((orderDispenser < 0) || (orderNozzle < 0)) {
//            setResult(RESULT_CANCELED, intent);
//            finish();
//        }
//        orderStore = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenserCfg.get(orderDispenser).get(orderNozzle));
//        orderProductArticle = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListStore.get(orderStore).getArticleId());
//        orderProductName = PTSTerminal.ptsMasterStatic.mListProductAssortment.get(orderProductArticle).getName();
//        orderProductPrice = PTSTerminal.ptsMasterStatic.mListProductAssortment.get(orderProductArticle).getPrice();
//        
//        idProduct.setText(String.valueOf(orderProductArticle));
//        idProductName.setText(orderProductName);
//        idProductPrice.setText(orderProductPrice);
//        
//
//		idPayForm = new String(listPayForms[0]);
		
		button_pay = (Button) findViewById(R.id.button_pay);
		button_pay.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((volume == 0.0) && ((orderVolume >= 99999.0) || (orderVolume == 0))) {
					showWarning();
				}
				else {
					Log.i("DispenserControl", "Pay");
   					startPayment();
				}
			}
		});

		button_pay_Disable();
//    	button_pay.setClickable(false);
//    	button_pay.setTextColor(Color.parseColor("#7A7E85"));
//    	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
//    	button_pay.setImageDrawable(getResources().getDrawable(R.drawable.payment_disable_48x48));
		
		button_clear = (Button) findViewById(R.id.button_clear);
		button_clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("DispenserControl", "Clear Basket");
				state = STATE_CLEAR_BASKET;
        		startBasketClose();
//				startBasketClear();
			}
		});
		button_clear_Enable();
//		button_clear.setClickable(true);
//		button_clear.setTextColor(Color.parseColor("#FFFFFF"));
//		button_clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_close));
//		button_clear.setImageDrawable(getResources().getDrawable(R.drawable.clear_48x48));
		
		button_start = (Button) findViewById(R.id.button_start);
		button_start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("DispenserControl", "Start");
				startPumpResume();
			}
		});
		button_start_Disable();
//		button_start.setClickable(false);
//    	button_start.setTextColor(Color.parseColor("#7A7E85"));
//    	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));

		button_stop = (Button) findViewById(R.id.button_stop);
		button_stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("DispenserControl", "Stop");
				startPumpStop();
			}
		});

		updateView();	

//		idImageOrderInfo.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Log.d("idImageOrderInfo", "PRESSED");
//				startPumpResume();
//			}
//		});
	}
	
	public void showWarning() {
    	// ================================================================ Диалоговое окно
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // Заголовок и текст
        alertbox.setTitle(R.string.title_warning);
        String TextToast = null;
    	TextToast = getResources().getText(R.string.textImposiblePayFullTank) + "\n\n";
        alertbox.setMessage(TextToast);
        // Добавляем кнопку 
        alertbox.setNeutralButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        // показываем окно
        alertbox.show();
     // ================================================================     	
	}

    public void funcFinish() {
    	PTSTerminal.clearHandlerFromDispenserControlActivity();
    	finish();
    }
    
    private void updateView() {
    	
		boolean productFined = false; 
		
		orderEnded = false;

//		Log.v("updateView", "mListBasket.size = " + PTSTerminal.ptsMasterStatic.mListBasket.size());
		for (int i = 0; i < PTSTerminal.ptsMasterStatic.mListBasket.size(); i++) {
//			Log.d("updateView", "i = " + i);
//			Log.v("updateView", "basketItem.size = " + PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.size());
			for (int n = 0; n < PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.size(); n++) {
//				Log.d("updateView", "n = " + n);
				if (PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).itemType == 1) {
//					Log.d("updateView", "itemType = 1");
					if (PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).pump == orderDispenser) {
//						Log.d("updateView", "pump = " + orderDispenser);
						productFined = true;
						// --------------				
						button_clear_Enable();
//						button_clear.setClickable(true);
//						button_clear.setTextColor(Color.parseColor("#FFFFFF"));
//						button_clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_close));
//						button_clear.setImageDrawable(getResources().getDrawable(R.drawable.clear_48x48));
						// --------------
						orderBasket = PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).basket;
						paySum = PTSTerminal.ptsMasterStatic.mListBasket.get(i).paySum;
						itemSum = PTSTerminal.ptsMasterStatic.mListBasket.get(i).itemSum;
//						summ = String.valueOf();
//						summ = String.valueOf(PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).sum);
						cost = String.valueOf(PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).cost);
						orderNozzle = PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).nozzle;
//						orderProductArticle = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListStore.get(Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenserCfg.get(orderDispenser).get(orderNozzle))).getArticleId());
//						idProductPrice.setText(PTSTerminal.ptsMasterStatic.mListProductAssortment.get(orderProductArticle).getPrice());
						idProductPrice.setText(String.format("%5.2f", PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).price).replace(',', '.'));
						volume = PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).volume;
						idProductAmount.setText(String.format("%5.2f", volume).replace(',', '.'));
//						idProductSumm.setText(String.valueOf(PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).cost));
						idProductSumm.setText(String.format("%5.2f", PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).cost).replace(',', '.'));
						idOrderProductName.setText(String.valueOf(PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).name));
						orderVolume = PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).orderVolume; 
						if ((orderVolume >= 99999.0) || (orderVolume == 0)) {
							idOrderProductAmount.setText(getResources().getText(R.string.textOrderFullTank));
						}
						else {
							idOrderProductAmount.setText(String.format("%5.2f", orderVolume).replace(',', '.'));
						}
						// Заказ неоплачен
						if ((PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).flags & getResources().getInteger(R.integer.flags_basket_after_payment)) == 0) {
							button_pay_Enable();
//		                	button_pay.setClickable(true);
//		                	button_pay.setTextColor(Color.parseColor("#FFFFFF"));
//		                	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_pay));
//		                	button_pay.setImageDrawable(getResources().getDrawable(R.drawable.payment_48x48));
						}
						else {
							button_pay_Disable();
//		                	button_pay.setClickable(false);
//		                	button_pay.setTextColor(Color.parseColor("#7A7E85"));
//		                	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
//		                	button_pay.setImageDrawable(getResources().getDrawable(R.drawable.payment_disable_48x48));
						}
						// Отпуск продукта окончен
						if ((PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).flags & getResources().getInteger(R.integer.flags_basket_dispence_finis)) > 0) {
							// Проверяю заказ на "Полный бак"
							if ((orderVolume >= 99999.0) || (orderVolume == 0)) {
//								button_start.setClickable(true);
//			                	button_start.setTextColor(Color.parseColor("#FFFFFF"));
//			                	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_enter));
			                	analizNozzlesStatus(i, n);
							}
							else {
								// Что такое "+ 0,05" ?
								// Допустим заказано 5.00 литров
								// При остановке отпуска на значении 4.95 и выше - принимаю решение о завершении отпуска 
								if ((volume + 0.05) < orderVolume) {
//									button_start.setClickable(true);
//				                	button_start.setTextColor(Color.parseColor("#FFFFFF"));
//				                	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_enter));
				                	analizNozzlesStatus(i, n);
								}
								else {// Отпущено столько, сколько было заказано
									button_start_Disable();
//									button_start.setClickable(false);
//				                	button_start.setTextColor(Color.parseColor("#7A7E85"));
//				                	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
									// Заказ неоплачен
									if ((PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).flags & getResources().getInteger(R.integer.flags_basket_after_payment)) == 0) { 
										idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.payment_64x64));
										idTextDispenserControlMessage.setText(getResources().getText(R.string.messErrror_need_order_pay));
										idTextDispenserControlMessage.startAnimation(anim);
									}
									else {
										orderEnded = true;
										idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.ok_64x64));
										idTextDispenserControlMessage.setText(getResources().getText(R.string.mess_dispenser_order_end));
										idTextDispenserControlMessage.startAnimation(anim2);
									}
								}
							}
//							orderEnded = true;
						}
						else {
//							button_start.setClickable(true);
//		                	button_start.setTextColor(Color.parseColor("#FFFFFF"));
//		                	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_enter));
		                	analizNozzlesStatus(i, n);
//							orderEnded = false;
//							// Не поднит ни один пистолет 
//							if (Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserNozzle()) == -1) {
//								idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.nozzle_close));
//								idTextDispenserControlMessage.setText(getResources().getText(R.string.messErrror_need_nozzle_up));
//							}
//							else {
//								// Поднят пистолет, нужно сравнить номера заказанного и поднятого пистолетов 
//								if (Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserNozzle()) == PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).nozzle) {
//									if (Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserOrder()) > 0) {
//										idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.run_64x64));
//										idTextDispenserControlMessage.setText(getResources().getText(R.string.mess_dispenser_order_make));
//									}
//									else {
//										idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.nozzle_up));
//										idTextDispenserControlMessage.setText(getResources().getText(R.string.mess_dispenser_ready));
//									}
//								}
//								else {
//									idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.nozzle_close));
//									idTextDispenserControlMessage.setText(getResources().getText(R.string.messError_nozzle_bad));
//								}
//							}
						}
//						Log.d("DispenserControl", "orderDispenser = " + orderDispenser + "; orderNozzle = " + orderNozzle + "; orderProductArticle = " + orderProductArticle + "; Price = " + PTSTerminal.ptsMasterStatic.mListProductAssortment.get(orderProductArticle).getPrice());
					}
//					else {
//						Log.i("updateView", "pump = " + PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).pump);
//					}
				}
//				else {
//					Log.i("updateView", "itemType = " + PTSTerminal.ptsMasterStatic.mListBasket.get(i).basketItem.get(n).itemType);
//				}
			}
		}
		// В корзинах продукт не найден - значит по этому продукту заказ окончен и оплачен!!!
		if (!productFined) {
			Log.d("updateView", "ЗАКАЗ ВЫПОЛНЕН!!!");
			button_start_Disable();
//			button_start.setClickable(false);
//        	button_start.setTextColor(Color.parseColor("#7A7E85"));
//        	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
			orderEnded = true;
			button_clear_Disable();
//    		button_clear.setClickable(false);
//    		button_clear.setTextColor(Color.parseColor("#7A7E85"));
//    		button_clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
//    		button_clear.setImageDrawable(getResources().getDrawable(R.drawable.clear_disable_48x48));
    		button_pay_Disable();
//        	button_pay.setClickable(false);
//        	button_pay.setTextColor(Color.parseColor("#7A7E85"));
//        	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
//        	button_pay.setImageDrawable(getResources().getDrawable(R.drawable.payment_disable_48x48));
			idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.ok_64x64));
			idTextDispenserControlMessage.setText(getResources().getText(R.string.mess_dispenser_order_end));
			idTextDispenserControlMessage.startAnimation(anim2);
		}
    }

    private void analizNozzlesStatus(int basketNumber, int itemInBasketNumber) {
		// Не поднит ни один пистолет 
		if (Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserNozzle()) == -1) {
			idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.nozzle_close));
			idTextDispenserControlMessage.setText(getResources().getText(R.string.messErrror_need_nozzle_up));
			idTextDispenserControlMessage.startAnimation(anim);
			button_start_Disable();
//			button_start.setClickable(false);
//        	button_start.setTextColor(Color.parseColor("#7A7E85"));
//        	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
		}
		else {
			// Поднят пистолет, нужно сравнить номера заказанного и поднятого пистолетов 
			if (Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserNozzle()) == PTSTerminal.ptsMasterStatic.mListBasket.get(basketNumber).basketItem.get(itemInBasketNumber).nozzle) {
				if (Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserOrder()) > 0) {
					idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.run_64x64));
					idTextDispenserControlMessage.setText(getResources().getText(R.string.mess_dispenser_order_make));
					idTextDispenserControlMessage.clearAnimation();
					button_start_Disable();
					button_pay_Disable();
//					button_start.setClickable(false);
//                	button_start.setTextColor(Color.parseColor("#7A7E85"));
//                	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
				}
				else {
					idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.nozzle_up));
					idTextDispenserControlMessage.setText(getResources().getText(R.string.mess_dispenser_ready));
					idTextDispenserControlMessage.startAnimation(anim2);
					button_start_Enable();
//					button_start.setClickable(true);
//                	button_start.setTextColor(Color.parseColor("#FFFFFF"));
//                	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_enter));
				}
			}
			else {
				idImageNozzleStatus.setImageDrawable(getResources().getDrawable(R.drawable.nozzle_close));
				idTextDispenserControlMessage.setText(getResources().getText(R.string.messError_nozzle_bad));
				idTextDispenserControlMessage.startAnimation(anim);
				button_start_Disable();
//				button_start.setClickable(false);
//            	button_start.setTextColor(Color.parseColor("#7A7E85"));
//            	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
			}
		}
    }
    
    private void button_start_Disable() {
		button_start.setClickable(false);
		button_start.setTextColor(Color.parseColor("#7A7E85"));
		button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
    }
    
    private void button_start_Enable() {
		button_start.setClickable(true);
    	button_start.setTextColor(Color.parseColor("#FFFFFF"));
    	button_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_enter));
    }
    
    private void button_pay_Disable() {
    	button_pay.setClickable(false);
    	button_pay.setTextColor(Color.parseColor("#7A7E85"));
    	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
    }
    
    private void button_pay_Enable() {
    	button_pay.setClickable(true);
    	button_pay.setTextColor(Color.parseColor("#FFFFFF"));
    	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_pay));
    }
    
    private void button_clear_Disable() {
    	button_clear.setClickable(false);
    	button_clear.setTextColor(Color.parseColor("#7A7E85"));
    	button_clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
    }
    
    private void button_clear_Enable() {
    	button_clear.setClickable(true);
    	button_clear.setTextColor(Color.parseColor("#FFFFFF"));
    	button_clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_close));
    }
    
	@Override
   	public void onBackPressed() {
		Log.i("DispenserControlAct-ty", "onBackPressed");
    	if (orderEnded) {
    		state = STATE_CLEAR_BASKET;
    		startBasketClose();
    	}
    	else {
	        Intent intent = new Intent();
	        setResult(RESULT_CANCELED, intent);
	        funcFinish();
    	}
   	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {
			Log.i("DispenserControlAct-ty", "LONG onBackPressed");
	        // do your stuff here
	    	if (orderEnded) {
	    		state = STATE_CLEAR_BASKET;
	    		startBasketClose();
	    	}
	    	else {
		        Intent intent = new Intent();
		        setResult(RESULT_CANCELED, intent);
		        funcFinish();
	    	}
	        return true;
	    }
	    return super.onKeyLongPress(keyCode, event);
	}

    public void startPayment() {
//        basket = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserBasket());
//		Log.i("DispenserControlActivity", "startPayment || paySum = " + paySum + "orderBasket = " + orderBasket);
    	summ = "0.0";
		if ((orderVolume >= 99999.0) || (orderVolume == 0)) {
//			summ = idProductSumm.getText().toString();
			// Если оплачено меньше, чем отпущено (т.е. необходимо оплачивать заказ) 
			if (paySum < Double.valueOf(idProductSumm.getText().toString())) {
				summ = String.valueOf(Double.valueOf(idProductSumm.getText().toString()) - paySum);
			}
			else {
				Log.i("DispenserControlAct-ty", "Заказ оплачен\nНеобходимо дать сдачу: " + (paySum - Double.valueOf(idProductSumm.getText().toString())));
			}
		}
		else {
			if (paySum < itemSum) {
				summ = String.valueOf(itemSum - paySum);
			}
			else {
				Log.i("DispenserControlAct-ty", "Заказ оплачен\nНеобходимо дать сдачу: " + (paySum - itemSum));
			}
		}
//		if (summ.equals("0.0")) {
//			summ = cost;
//		}
		if (!summ.equals("0.0")) {
			PTSTerminal.state = getResources().getInteger(R.integer.state_order_pay);
	    	intent = new Intent();
			intent.putExtra("summ", summ);
			intent.putExtra("basket", orderBasket);
	    	intent.setClass(this, OrderPayActivity.class);
	    	// эапускаем деятельнсть 
	    	startActivityForResult(intent, PAGE_ORDER_PAY);
		}
    }

    public void startPumpResume() {
		Log.i("OrderPay", "startPumpResume || orderNozzle = " + orderNozzle + "; orderDispenser = " + orderDispenser + "; orderBasket = " + orderBasket);
		PTSTerminal.ptsMaster.OrderBasketItem.volume = 0.0;
		PTSTerminal.ptsMaster.OrderBasketItem.nozzle = orderNozzle;
		PTSTerminal.ptsMaster.OrderBasketItem.pump = orderDispenser;
        PTSTerminal.ptsMaster.OrderBasketItem.basket = orderBasket;
//		PTSTerminal.ptsMaster.OrderBasketItem.nozzle = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserNozzle());
//		PTSTerminal.ptsMaster.OrderBasketItem.pump = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserIndex());
//        PTSTerminal.ptsMaster.OrderBasketItem.basket = Integer.valueOf(PTSTerminal.ptsMasterStatic.mListDispenser.get(orderDispenser).getDispenserBasket());
		intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
		intent.putExtra("function", PTSMasterService.FUNC_PUMPRESUME);
		// эапускаем деятельнсть
		startActivityForResult(intent, PAGE_PUMP_RESUME);
    }
 
    public void startBasketClear() {
		Log.i("OrderPay", "startBasketClear || orderBasket = " + orderBasket);
        PTSTerminal.ptsMaster.OrderBasketItem.basket = orderBasket;
		intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
//		intent.putExtra("idClerk", idClerk);
		intent.putExtra("function", PTSMasterService.FUNC_BASKETCLEAR);
		// эапускаем деятельнсть
		startActivityForResult(intent, PAGE_REQUEST_BASKETCLEAR);
    }
 
    public void startBasketClose() {
		Log.i("OrderProductActivity", "---===--- startBasketClose() ---===---");
        PTSTerminal.ptsMaster.OrderBasketItem.basket = orderBasket;
		intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
//		intent.putExtra("idClerk", idClerk);
		intent.putExtra("function", PTSMasterService.FUNC_BASKETCLOSE);
		// эапускаем деятельнсть
		startActivityForResult(intent, PAGE_REQUEST_BASKETCLOSE);
    }
 
    public void startPumpStop() {
		Log.i("OrderPay", "startPumpStop || orderDispenser = " + orderDispenser);
		PTSTerminal.ptsMaster.OrderBasketItem.pump = orderDispenser;
		intent = new Intent();
        intent.setClass(this, PTSMasterClientActivity.class);
		intent.putExtra("function", PTSMasterService.FUNC_PUMPSTOP);
		// эапускаем деятельнсть
		startActivityForResult(intent, PAGE_PUMP_STOP);
    }
 
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_OK_ERROR:
				return new AlertDialog.Builder(this)
					.setIcon(R.drawable.alert_dialog_icon)
					// .setTitle(getResources().getStringArray(R.array.error_udt)[error])
					.setTitle(errorMessage)
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
  
		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_disp_control));

        updateView();
        switch (resultCode) {
        // ===========================================================================================
        // ==================================================================== RESULT_OK 
  	  	// ===========================================================================================
          case RESULT_OK:
    			Log.i("OrderProductActivity", "RESULT_OK");
        	  switch (requestCode) {
//                case PTSMASTER_ORDERCONFIRM:
//                	break;
                case PAGE_ORDER_PAY:
                	button_pay_Disable();
//                	button_pay.setClickable(false);
//                	button_pay.setTextColor(Color.parseColor("#7A7E85"));
//                	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
//                	button_pay.setImageDrawable(getResources().getDrawable(R.drawable.payment_disable_48x48));
                	if (orderEnded) {
//                		startBasketClose();
	            		intent = new Intent();
		                setResult(RESULT_OK, intent);
		                finish();
                	}
                	break;
                case PAGE_PUMP_RESUME:
//            		intent = new Intent();
//	                setResult(RESULT_OK, intent);
//	                finish();
                	break;
                case PAGE_PUMP_STOP:
//            		intent = new Intent();
//	                setResult(RESULT_OK, intent);
//	                finish();
                	break;
                case PAGE_REQUEST_BASKETCLOSE:
                	switch (state) {
                	case STATE_CLEAR_BASKET:
                		state = STATE_EDLE;
                		startBasketClear();
                		break;
                	default:
                		break;
                	}
        	        break;
                case PAGE_REQUEST_BASKETCLEAR:
                	button_clear_Disable();
//            		button_clear.setClickable(false);
//            		button_clear.setTextColor(Color.parseColor("#7A7E85"));
//            		button_clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
//            		button_clear.setImageDrawable(getResources().getDrawable(R.drawable.clear_disable_48x48));
            		button_pay_Disable();
//                	button_pay.setClickable(false);
//                	button_pay.setTextColor(Color.parseColor("#7A7E85"));
//                	button_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button));
//                	button_pay.setImageDrawable(getResources().getDrawable(R.drawable.payment_disable_48x48));
            		intent = new Intent();
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
//                case PTSMASTER_ORDERCONFIRM:
//                	break;
                case PAGE_ORDER_PAY:
                case PAGE_REQUEST_BASKETCLEAR:
                case PAGE_PUMP_RESUME:
                case PAGE_PUMP_STOP:
                case PAGE_REQUEST_BASKETCLOSE:
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
	              case PAGE_ORDER_PAY:
	              case PAGE_PUMP_RESUME:
	              case PAGE_PUMP_STOP:
	              case PAGE_REQUEST_BASKETCLEAR:
	              case PAGE_REQUEST_BASKETCLOSE:
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