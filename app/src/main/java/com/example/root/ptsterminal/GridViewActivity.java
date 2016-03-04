package com.example.root.ptsterminal;

import java.util.ArrayList;

import com.example.root.ptsterminal.ArrayList_PTSMaster.StatusPump;
import adapter.ImageAdapter;
//import com.samples.button.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class GridViewActivity extends Activity {

    private static final int PAGE_ORDER_PRODUCT = 101;
    private static final int PAGE_DISPENSER_CONTROL = 102;
    private static final int PAGE_SELECT_PRODUCT = 103;
    
	private int error;

	private Intent intent;
	private Bundle extras;

	GridView gridView;
	
//	int currentBasket = -1;
	boolean orderedDispenser = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);

		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_pumps));

//		tf = Typeface.createFromAsset(getAssets(), "times.ttf");
//		TextView tv = (TextView) findViewById(R.id.idTextLogoTiT);
//		tv.setTypeface(tf);
//		tf = Typeface.createFromAsset(getAssets(), "verdana.ttf");
//		TextView tvPTSMAster = (TextView) findViewById(R.id.idTextLogoPTSMaster);
//		tvPTSMAster.setTypeface(tf);
//		tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
//		TextView tvPUMPS = (TextView) findViewById(R.id.idTextPUMP);
//		tvPUMPS.setTypeface(tf);
//		tf = Typeface.createFromAsset(getAssets(), "verdana.ttf");
//		TextView tvUser = (TextView) findViewById(R.id.idTextUser);
//		tvUser.setTypeface(tf);
//		
		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setNumColumns(4);
		gridView.setAdapter(PTSTerminal.ptsAdapter.mAdapter);
		

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				PTSTerminal.ptsAdapter.mAdapter1.set_ptsMaster(PTSTerminal.ptsMaster);
//				PTSTerminal.ptsAdapter.mAdapter1.changeDispNumber(0, String.valueOf(position + 1));
//				PTSTerminal.ptsAdapter.mAdapter1.setStatus(0, PTSTerminal.ptsAdapter.mAdapter.getStatus(position));
				StatusPump pumpStatus = StatusPump.getType(PTSTerminal.ptsMaster.mListDispenser.get(position).getDispenserStatus());
//				currentBasket = Integer.valueOf(PTSTerminal.ptsMaster.mListDispenser.get(position).getDispenserBasket());
				orderedDispenser = false;
				for (int i = 0; i < PTSTerminal.ptsMaster.mListBasket.size(); i++) {
					for (int n = 0; n < PTSTerminal.ptsMaster.mListBasket.get(i).basketItem.size(); n++) {
						if (PTSTerminal.ptsMaster.mListBasket.get(i).basketItem.get(n).pump == position) {
							if (PTSTerminal.ptsMaster.mListBasket.get(i).basketItem.get(n).itemType == 1) {
								orderedDispenser = true;
							}
						}
					}
				}

//		        PTSTerminal.gView.setAdapter(PTSTerminal.ptsAdapter.mAdapter1);
		        PTSTerminal.ptsAdapter.mAdapter1.set_ptsMaster(PTSTerminal.ptsMaster);
		        PTSTerminal.ptsAdapter.mAdapter1.setDispenserNumber(position);
				
		        switch (pumpStatus)
		        {
		        case DISP_CALL:
		        	if (orderedDispenser) {
		        		startDispenserControl(position);
		        	}
		        	else {
		        		startOrderProduct(position);
		        	}
		        	break;
		        case DISP_IDLE:
		        	if (orderedDispenser) {
		        		startDispenserControl(position);
		        	}
		        	else {
			        	startSelectProduct(position);
		        	}
		        	break;
		        case DISP_BUSY:
		        case DISP_NOTPAY:
		        case DISP_WORK:
		        case DISP_AUTH:
	        		startDispenserControl(position);
		        	break;
		        default:
		        	break;
		        }
				
//				mAdapter.openCell (position);
//				Toast.makeText(
//						getApplicationContext(),
//						((TextView) v.findViewById(R.id.grid_item_label))
//								.getText(), Toast.LENGTH_SHORT).show();

			}
		});

//		final Button button1 = (Button) findViewById(R.id.button_exit);
//		button1.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
////				PTSTerminal.ptsAdapter.mAdapter.dispNOZZLEUP(2);
////				Toast.makeText(getApplicationContext(), "Start",
////						Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		final Button button2 = (Button) findViewById(R.id.button_help);
//		button2.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
////				PTSTerminal.ptsAdapter.mAdapter.dispCLOSE(2);
////				Toast.makeText(getApplicationContext(), "Stop",
////						Toast.LENGTH_SHORT).show();
//			}
//		});

	}

    public void startSelectProduct(int position) {
		PTSTerminal.state = getResources().getInteger(R.integer.state_select_product);
    	intent = new Intent();
    	intent.setClass(this, OrderSelectProductActivity.class);
		intent.putExtra("pump", position);
    	// эапускаем деятельнсть 
    	startActivityForResult(intent, PAGE_SELECT_PRODUCT);
    }
 
    public void startOrderProduct(int position) {
		PTSTerminal.state = getResources().getInteger(R.integer.state_order_product);
    	intent = new Intent();
    	intent.setClass(this, OrderProductActivity.class);
		intent.putExtra("pump", position);
		intent.putExtra("nozzle", Integer.valueOf(PTSTerminal.ptsMaster.mListDispenser.get(position).getDispenserNozzle()));
    	// эапускаем деятельнсть 
    	startActivityForResult(intent, PAGE_ORDER_PRODUCT);
    }
 
    public void startDispenserControl(int position) {
		PTSTerminal.state = getResources().getInteger(R.integer.state_order_pay);
		PTSTerminal.ptsMasterStatic = PTSTerminal.ptsMaster.getCopy(); 
    	intent = new Intent();
		intent.putExtra("pump", position);
//		intent.putExtra("nozzle", Integer.valueOf(PTSTerminal.ptsMaster.mListDispenser.get(position).getDispenserNozzle()));
    	intent.setClass(this, DispenserControlActivity.class);
    	// эапускаем деятельнсть 
    	startActivityForResult(intent, PAGE_DISPENSER_CONTROL);
    }
 
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		PTSTerminal.titlePage.setText(getResources().getText(R.string.titlePage_pumps));
		PTSTerminal.ptsAdapter.mAdapter1.setSize(0);

        switch (resultCode) {
        // ===========================================================================================
        // ==================================================================== RESULT_OK 
  	  	// ===========================================================================================
          case RESULT_OK:
        	  switch (requestCode) {
                case PAGE_ORDER_PRODUCT:
					extras = data.getExtras();
					Log.v("GrideView", "pump = " + extras.getInt("pump"));
					startDispenserControl(extras.getInt("pump"));
					PTSTerminal.ptsAdapter.mAdapter1.set_ptsMaster(PTSTerminal.ptsMaster);
					PTSTerminal.ptsAdapter.mAdapter1.setDispenserNumber(extras.getInt("pump"));
                	break;
                case PAGE_DISPENSER_CONTROL:
                	break;
                case PAGE_SELECT_PRODUCT:
					extras = data.getExtras();
					Log.v("GrideView", "pump = " + extras.getInt("pump"));
					startDispenserControl(extras.getInt("pump"));
					PTSTerminal.ptsAdapter.mAdapter1.set_ptsMaster(PTSTerminal.ptsMaster);
					PTSTerminal.ptsAdapter.mAdapter1.setDispenserNumber(extras.getInt("pump"));
                	break;
        	  }
            break;
		// ===========================================================================================
		// ==================================================================== RESULT_ERROR 
		// ===========================================================================================
          case defines.RESULT_ERROR:
        	  extras = data.getExtras();
        	  switch (requestCode) {
              case PAGE_ORDER_PRODUCT:
              	break;
              case PAGE_DISPENSER_CONTROL:
              	break;
              case PAGE_SELECT_PRODUCT:
              	break;
        	  }
        	  break;
		  // ===========================================================================================
		  // ========================================================== RESULT_CANCELED
		  // ===========================================================================================
          case RESULT_CANCELED:
              switch (requestCode) {
              case PAGE_ORDER_PRODUCT:
              	break;
              case PAGE_DISPENSER_CONTROL:
              	break;
              case PAGE_SELECT_PRODUCT:
              	break;
              }
              break;
              // ===========================================================================================
              // ================================================= RESULT_TIMEOUT 
              // ===========================================================================================
          case defines.RESULT_TIMEOUT:
              switch (requestCode) {
              case PAGE_ORDER_PRODUCT:
              	break;
              case PAGE_DISPENSER_CONTROL:
              	break;
              case PAGE_SELECT_PRODUCT:
              	break;
              }
          	  break;
        	  // ===========================================================================================
              // ======================================================================= default
        	  // ===========================================================================================
          default:
//        	  state = getResources().getInteger(R.integer.state_unauthorized);
//        	  PTSTerminal.mListCardOperator.clear();
//        	  Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
//        	  PTSTerminal.this.finish();
        	  break;
        		  
        }
    }
    

}