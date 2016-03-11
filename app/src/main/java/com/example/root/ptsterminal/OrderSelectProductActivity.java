package com.example.root.ptsterminal;

import java.util.ArrayList;

import com.example.root.ptsterminal.ArrayList_PTSMaster.StatusPump;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderSelectProductActivity extends Activity {

	GridView gridView;

    private static final int PAGE_UDT_CLIENT = 101;
    private static final int PAGE_ORDER_PRODUCT = 102;
    
	private int error;

    private static int orderStore;
    private static int orderDispenser;
    private static int orderNozzle;
    private static int orderProductArticle;
    private static int orderProductAmount;
    private static String orderProductName;
    private static String orderProductPrice;

    private TextView idProduct;
	private TextView idProductAmount;
	private TextView idProductPrice;
	private TextView idProductUnit;
	private TextView idProductName;
	Typeface fontFaceHelvetica;
	Typeface fontFaceDigital;
	
	private Intent intent;
	private Bundle extras;

	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.order_select_product);
		
		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_select_product));

		Bundle extras = getIntent().getExtras();
		orderDispenser = extras.getInt("pump");
		PTSTerminal.productAdapter.set_ptsMaster(PTSTerminal.ptsMaster);
		PTSTerminal.productAdapter.setDispenser(orderDispenser);
		
		gridView = (GridView) findViewById(R.id.idGridView);
		gridView.setNumColumns(1);
		gridView.setAdapter(PTSTerminal.productAdapter);

		fontFaceHelvetica = Typeface.createFromAsset(getAssets(), "Helvetica_Bold.ttf");
		fontFaceDigital = Typeface.createFromAsset(getAssets(), "DigitalBold.ttf");
//		idProduct = (TextView) findViewById(R.id.idProduct);
		View listView = new View(gridView.getContext());
		LayoutInflater inflater = (LayoutInflater) gridView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		listView = inflater.inflate(R.layout.petrol_price, null);
		listView = View.inflate(gridView.getContext(), R.layout.petrol_price, null);

		
		
//		idProduct.setTypeface(fontFaceDigital);
//		idProductName = (TextView) findViewById(R.id.idProductName);
//		idProductName.setTypeface(fontFaceHelvetica);
//		idProductPrice = (TextView) findViewById(R.id.idProductPrice);
//		idProductPrice.setTypeface(fontFaceHelvetica);
		
//
//        Bundle extras = getIntent().getExtras();
//        orderDispenser = extras.getInt("pump");
//        orderProductAmount = extras.getInt("amount");
//        orderNozzle = Integer.valueOf(PTSTerminal.ptsMaster.mListDispenser.get(orderDispenser).getDispenserNozzle());
//        if ((orderDispenser < 0) || (orderNozzle < 0)) {
//            setResult(RESULT_CANCELED, intent);
//            finish();
//        }
//        orderStore = Integer.valueOf(PTSTerminal.ptsMaster.mListDispenserCfg.get(orderDispenser).get(orderNozzle));
//        orderProductArticle = Integer.valueOf(PTSTerminal.ptsMaster.mListStore.get(orderStore).getArticleId());
//        orderProductName = PTSTerminal.ptsMaster.mListProductAssortment.get(orderProductArticle).getName();
//        orderProductPrice = PTSTerminal.ptsMaster.mListProductAssortment.get(orderProductArticle).getPrice();
//        
//        idProduct.setText(String.valueOf(orderProductArticle));
//        idProductName.setText(orderProductName);
//        idProductPrice.setText(orderProductPrice);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				startOrderProduct(position);
			}
		});
		
	}
	
	@Override
   	public void onBackPressed() {
        PTSTerminal.productAdapter.stopTimer();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
   	}
    	
	// #####################################################################################################
	// Запуск активности заказа продукта 
	// #####################################################################################################
    public void startOrderProduct(int position) {
        PTSTerminal.productAdapter.stopTimer();
		PTSTerminal.state = getResources().getInteger(R.integer.state_order_product);
    	intent = new Intent();
    	intent.setClass(this, OrderProductActivity.class);
		intent.putExtra("pump", orderDispenser);
		intent.putExtra("nozzle", position);
    	// эапускаем деятельнсть 
    	startActivityForResult(intent, PAGE_ORDER_PRODUCT);
    }

	// #####################################################################################################
	// Анализ ответа активности 
	// #####################################################################################################
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_select_product));

        switch (resultCode) {
        // ===========================================================================================
        // ==================================================================== RESULT_OK 
  	  	// ===========================================================================================
          case RESULT_OK:
        	  switch (requestCode) {
                case PAGE_ORDER_PRODUCT:
					extras = data.getExtras();
            		intent = new Intent();
            		intent.putExtra("pump", extras.getInt("pump"));
					Log.v("SelectProduct", "pump = " + extras.getInt("pump"));
	                setResult(RESULT_OK, intent);
	                finish();
                	break;
                case PAGE_UDT_CLIENT:
//	                	if (state == getResources().getInteger(R.integer.state_unauthorized)) {
//	                		state = getResources().getInteger(R.integer.state_main_menu);
//	                		startMainMenu();
//	                	}
                	break;
        	  }
            break;
		// ===========================================================================================
		// ==================================================================== RESULT_ERROR 
		// ===========================================================================================
          case defines.RESULT_ERROR:
        	  extras = data.getExtras();
        	  switch (requestCode) {
                case PAGE_UDT_CLIENT:
                	Toast.makeText(getApplicationContext(), "UDT error", Toast.LENGTH_SHORT).show();
//	                	startAuth();
                    break;
        	  }
        	  break;
		  // ===========================================================================================
		  // ========================================================== RESULT_CANCELED
		  // ===========================================================================================
          case RESULT_CANCELED:
              switch (requestCode) {
	              case PAGE_ORDER_PRODUCT:
	              	Toast.makeText(getApplicationContext(), "Order product finished", Toast.LENGTH_SHORT).show();
	                Intent intent = new Intent();
	                setResult(RESULT_CANCELED, intent);
	                finish();
	              	break;
              }
              break;
              // ===========================================================================================
              // ================================================= RESULT_TIMEOUT 
              // ===========================================================================================
          case defines.RESULT_TIMEOUT:
//	          	  state = getResources().getInteger(R.integer.state_unauthorized);
//	          	  PTSTerminal.mListCardOperator.clear();
//	          	  Toast.makeText(getApplicationContext(), "Timeout", Toast.LENGTH_SHORT).show();
//	          	  startAuth();
          	  break;
        	  // ===========================================================================================
              // ======================================================================= default
        	  // ===========================================================================================
          default:
//	        	  state = getResources().getInteger(R.integer.state_unauthorized);
//	        	  PTSTerminal.mListCardOperator.clear();
//	        	  Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
//	        	  PTSTerminal.this.finish();
        	  break;
        		  
        }
    }

}