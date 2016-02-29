package adapter;


import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.pts.terminal.ArrayList_PTSMaster;
import app.pts.terminal.PTSMasterService;
import app.pts.terminal.PTSTerminal;
import app.pts.terminal.myLib;
import app.pts.terminal.ArrayList_PTSMaster.StatusPump;
import app.pts.terminal.R;

public class SelectProductAdapter extends BaseAdapter {

    public TextView idProduct;
    Typeface fontProductId;
    Typeface fontProductName;
    Typeface fontProductPrice;
    private Context context;
    int orderDispenser;
    int currentNozzle;
    //    boolean stateNozzle = false;
    boolean blinkNozzle = false;
    Timer timer;
    TimerTask task;

    ArrayList_PTSMaster ptsMasterInner = new ArrayList_PTSMaster();
//	private final String[] mobileValues;
//	public static enum Status {DISP_UNDEFINED, DISP_OFF, DISP_IDLE, DISP_CALL, DISP_WORK, DISP_AUTH, DISP_BUSY, DISP_NOTPAY};
//	private ArrayList<ArrayList_PTSMaster.statusPump> arrStatus;
//	private ArrayList<String> arrNumber;

    public SelectProductAdapter(Context context) {
        this.context = context;
    }

    public void setTextViewFace(Typeface fontProductId) {
        this.fontProductId = fontProductId;
    }

    public void set_ptsMaster(ArrayList_PTSMaster ptsMaster) {
        this.ptsMasterInner = ptsMaster;
    }

    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public void checkNozzle() {
        StatusPump pumpStatus = StatusPump.getType(this.ptsMasterInner.mListDispenser.get(this.orderDispenser).getDispenserStatus());
//		Log.d("SelectProductAdapter", "DispenserStatus: " + this.ptsMasterInner.mListDispenser.get(this.orderDispenser).getDispenserStatus());

        switch (pumpStatus)
        {
            case DISP_CALL:
//			Log.d("SelectProductAdapter", "Nozzle UP");
//        	stateNozzle = true;
                notifyDataSetChanged();
                break;
            default:
//			Log.d("SelectProductAdapter", "Nozzle off");
                if (blinkNozzle) {
                    notifyDataSetChanged();
                }
//        	stateNozzle = false;
                break;
        }
    }

    public void setDispenser(int orderDispenser) {
        this.orderDispenser = orderDispenser;
        timer = new Timer("TimerCheckNozzle");
        task = new TimerTask() { // Определяем задачу
            Handler mHandlerInner = mHandler;
            @Override
            public void run() {
                // -------------------------------- Ответ
                Message msg = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putInt("timer", 0);
                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        };
        // Устанавливаю время: через сколько будет запущена задача task (в
        // милисекундах)
        timer.schedule(task, 0, 500);
        notifyDataSetChanged();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().containsKey("timer")) {
                checkNozzle();
            }
        }
    };


    public View getView(int position, View convertView, ViewGroup parent) {

        String strIdProduct = "0";
        String strIdProductName;
        String strIdProductPrice;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView;
        if (convertView == null) {
            listView = new View(context);
            // get layout from order_select_product.xml
            listView = inflater.inflate(R.layout.petrol_price, null);
        }
        else {
            listView = (View) convertView;
        }

        // set value into textview
        idProduct = (TextView) listView.findViewById(R.id.idProduct);
        TextView idProductName = (TextView) listView.findViewById(R.id.idProductName);
        TextView idProductPrice = (TextView) listView.findViewById(R.id.idProductPrice);
        View idBackgroundColor = (View) listView.findViewById(R.id.idBackgroundColor);

        idProduct.setTypeface(this.fontProductId);
//		idProductName.setTypeface(this.fontProductName);
//		idProductPrice.setTypeface(this.fontProductPrice);

//		int productAmount = getCount();

//"Dispenser-" + i + ": Nozzle-" + n + ": store-" + ptsMaster.mListDispenserCfg.get(i).get(n)
//+ ": Product-" + ptsMaster.mListProductAssortment.get(Integer.valueOf(
//		ptsMaster.mListStore.get(Integer.valueOf(ptsMaster.mListDispenserCfg.get(i).get(n))).getArticleId()))
//		.getName());
//		Log.i("SelectProductAdapter", "mListStore.position: " + position);
        if (this.ptsMasterInner.mListDispenserCfg.size() <=  orderDispenser) {
            Log.e("SelectProductAdapter", "mListDispenserCfg.size() <=  orderDispenser");
        }
        else {
            if (this.ptsMasterInner.mListDispenserCfg.get(orderDispenser).size() <=  position) {
                Log.e("SelectProductAdapter", "mListDispenserCfg.get(orderDispenser).size() <=  position");
                Log.e("SelectProductAdapter", "mListDispenserCfg.get(orderDispenser).size()= " + this.ptsMasterInner.mListDispenserCfg.get(orderDispenser).size());
            }
            else {
                strIdProduct = this.ptsMasterInner.mListStore.get(Integer.valueOf(this.ptsMasterInner.mListDispenserCfg.get(orderDispenser).get(position).getStore())).getArticleId();
                strIdProductName = this.ptsMasterInner.mListStore.get(Integer.valueOf(this.ptsMasterInner.mListDispenserCfg.get(orderDispenser).get(position).getStore())).getName();
                strIdProductPrice = this.ptsMasterInner.mListProductAssortment.get(Integer.valueOf(strIdProduct)).getPrice();
//				Log.i("SelectProductAdapter", "idProduct: " + strIdProduct);
//				Log.i("SelectProductAdapter", "idProductName: " + strIdProductName);
//				Log.i("SelectProductAdapter", "idProductPrice: " + strIdProductPrice);
                idProduct.setText(strIdProduct);
                idProductName.setText(strIdProductName);
//				idProductPrice.setText(strIdProductPrice);
                idProductPrice.setText(String.format("%5.2f", Double.valueOf(strIdProductPrice)).replace(',', '.'));
            }
        }

        currentNozzle = Integer.valueOf(this.ptsMasterInner.mListDispenser.get(orderDispenser).getDispenserNozzle());
        if (currentNozzle == position) {
//			Log.d("SelectProductAdapter", "blinkNozzle: " + blinkNozzle);
            if (blinkNozzle) {
                idProduct.setBackgroundColor(Color.parseColor("#646464"));
                blinkNozzle = false;
            }
            else {
                idProduct.setBackgroundColor(Color.parseColor("#000000"));
                blinkNozzle = true;
            }
        }
        else {
            idProduct.setBackgroundColor(Color.parseColor("#646464"));
//			blinkNozzle = false;
        }


        if ((currentNozzle == position) && (blinkNozzle)) {
            idBackgroundColor.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else {
            switch (Integer.valueOf(strIdProduct))
            {
                case 0:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#11EE11"));
                    break;
                case 1:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#F4F424"));
                    break;
                case 2:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#32CDBE"));
                    break;
                case 3:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#84A0EC"));
                    break;
                case 4:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#F4F424"));
                    break;
                case 5:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#F4F424"));
                    break;
                case 6:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#F4F424"));
                    break;
                case 7:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#F4F424"));
                    break;
                default:
                    idBackgroundColor.setBackgroundColor(Color.parseColor("#F4F424"));
                    break;
            }
        }

        return listView;
    }


//	public void openCell(int position) {
////		switch (arrStatus.get(position)) {
////		case DISP_IDLE:
////			arrStatus.set(position, Status.DISP_NOZZLE_UP);
////			break;
////		case DISP_NOZZLE_UP:
////			arrStatus.set(position, Status.DISP_WORK);
////			break;
////		case DISP_WORK:
////			arrStatus.set(position, Status.DISP_BLOCKED);
////			break;
////		case DISP_BLOCKED:
////			arrStatus.set(position, Status.DISP_ALARM_STOP);
////			break;
////		case DISP_ALARM_STOP:
////			arrStatus.set(position, Status.DISP_CLOSE);
////			break;
////		case DISP_CLOSE:
////			arrStatus.set(position, Status.DISP_IDLE);
////			break;
////		}
////
//
//		notifyDataSetChanged();
//		return;
//	}
//
//
//	public void disactiveCell (int position) {
////		if (arrStatus.get(position) != ArrayList_PTSMaster.statusPump.DISP_UNDEFINED)
////			arrStatus.set(position, ArrayList_PTSMaster.statusPump.DISP_UNDEFINED);
//
//		this.ptsMasterInner.mListDispenser.get(position).setDispenserStatus("DISP_UNDEFINED");
//		notifyDataSetChanged();
//    }
//
//	public StatusPump getStatus (int position) {
//		return arrStatus.get(position);
//    }

//	public String getNumber (int position) {
//		return this.ptsMasterInner.mListDispenser.get(position).getDispenserIndex();
////		return arrNumber.get(position);
//    }

//	public void setStatus (int position, StatusPump status) {
//		if (arrStatus.get(position) != status)
//			arrStatus.set(position, status);
//		notifyDataSetChanged();
//    }

//	public void dispIDLE(int position) {
//
//		if (arrStatus.get(position) != ArrayList_PTSMaster.statusPump.DISP_IDLE)
//			arrStatus.set(position, ArrayList_PTSMaster.statusPump.DISP_IDLE);
//
//
//		notifyDataSetChanged();
//		return;
//	}

//	public void dispNOZZLEUP(int position) {
//
//		if (arrStatus.get(position) != Status.DISP_NOZZLE_UP)
//			arrStatus.set(position, Status.DISP_NOZZLE_UP);
//
//		notifyDataSetChanged();
//		return;
//	}

//	public void dispCLOSE(int position) {
//
//		if (arrStatus.get(position) != Status.DISP_CLOSE)
//			arrStatus.set(position, Status.DISP_CLOSE);
//
//		notifyDataSetChanged();
//		return;
//	}

//	public void changeDispNumber (int position, String number) {
////		arrNumber.set(position, number);
//		this.ptsMasterInner.mListDispenser.get(position).setDispenserIndex(number);
//    }

    @Override
    public int getCount() {
        return this.ptsMasterInner.mListDispenserCfg.get(orderDispenser).size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
