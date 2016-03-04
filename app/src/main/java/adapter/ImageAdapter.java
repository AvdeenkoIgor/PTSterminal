package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.root.ptsterminal.ArrayList_PTSMaster;
import com.example.root.ptsterminal.ArrayList_PTSMaster.StatusPump;
import com.example.root.ptsterminal.ArrayList_BasketData;
import com.example.root.ptsterminal.R;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private String mode;
    private int currentPosition;
    private int size;

    ArrayList_PTSMaster ptsMasterInner = new ArrayList_PTSMaster();
//	private final String[] mobileValues;
//	public static enum Status {DISP_UNDEFINED, DISP_OFF, DISP_IDLE, DISP_CALL, DISP_WORK, DISP_AUTH, DISP_BUSY, DISP_NOTPAY};
//	private ArrayList<ArrayList_PTSMaster.statusPump> arrStatus;
//	private ArrayList<String> arrNumber;

    public ImageAdapter(Context context) {
        this.context = context;
        this.mode = "Disable";
    }

    public ImageAdapter(Context context, String mode) {
        this.context = context;
        this.mode = mode;
        this.size = 0;
        this.currentPosition = 0;
    }

    public void setDispenserNumber (int currentPosition) {
        this.size = 1;
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public void setSize (int size) {
        this.size = size;
        notifyDataSetChanged();
    }


    public void set_ptsMaster(ArrayList_PTSMaster ptsMaster) {
        this.ptsMasterInner = ptsMaster;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {

//		int currentBasket = -1;
        boolean orderedDispenser = false;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.mobile, null);

        }
        else {
            gridView = (View) convertView;
        }

        if (this.mode.equals("Disable")) {
            this.currentPosition = position;
        }

//		Log.v("ImageAdaptor", "arrNumber.get(position): " + position);
//		String mobile = arrNumber.get(position);
//		String mobile = this.ptsMasterInner.mListDispenser.get(position).getDispenserIndex();
        String mobile = this.ptsMasterInner.mListDispenserCfg.get(this.currentPosition).get(0).getName();

        // set value into textview
        TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
        textView.setText(mobile);

        // set image based on selected text
        ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

//		currentBasket = Integer.valueOf(this.ptsMasterInner.mListDispenser.get(position).getDispenserBasket());
        for (int i = 0; i < this.ptsMasterInner.mListBasket.size(); i++) {
            for (int n = 0; n < this.ptsMasterInner.mListBasket.get(i).basketItem.size(); n++) {
                if ((this.ptsMasterInner.mListBasket.get(i).basketItem.get(n).pump == this.currentPosition) && (this.ptsMasterInner.mListBasket.get(i).basketItem.get(n).basket == (this.currentPosition + 1))) {
                    if ((this.ptsMasterInner.mListDispenser.get(this.currentPosition).getDispenserStatus().equals("DISP_IDLE")) || (this.ptsMasterInner.mListDispenser.get(this.currentPosition).getDispenserStatus().equals("DISP_CALL"))) {
                        if (((this.ptsMasterInner.mListBasket.get(i).basketItem.get(n).flags & context.getResources().getInteger(R.integer.flags_basket_after_payment)) == 0) // Заказ неоплачен
                                && ((this.ptsMasterInner.mListBasket.get(i).basketItem.get(n).flags & context.getResources().getInteger(R.integer.flags_basket_dispence_finis)) > 0)) {// Отпуск продукта окончен
                            this.ptsMasterInner.mListDispenser.get(this.currentPosition).setDispenserStatus("DISP_NOTPAY");
                        }
                    }
                    if (this.ptsMasterInner.mListBasket.get(i).basketItem.get(n).itemType == 1) {
                        orderedDispenser = true;
                    }
                }
            }
        }

//		Log.v("ImageAdaptor", "orderedDispenser[" + this.currentPosition + "] = " + orderedDispenser);
        StatusPump pumpStatus = StatusPump.getType(this.ptsMasterInner.mListDispenser.get(this.currentPosition).getDispenserStatus());

//		Log.v("ImageAdaptor", "pumpStatus (" + this.currentPosition + "): " + this.ptsMasterInner.mListDispenser.get(this.currentPosition).getDispenserStatus());
        switch (pumpStatus)
        {
            case DISP_UNDEFINED:
                imageView.setImageResource(R.drawable.key_pump_undefined);
                textView.setTextColor(Color.parseColor("#000000"));
                break;
            case DISP_OFF:
                imageView.setImageResource(R.drawable.key_pump_off);
                textView.setTextColor(Color.parseColor("#000000"));
                break;
            case DISP_IDLE:
                if (orderedDispenser) {
                    imageView.setImageResource(R.drawable.key_pump_ordered);
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                }
                else {
                    imageView.setImageResource(R.drawable.key_pump_idle);
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case DISP_CALL:
                imageView.setImageResource(R.drawable.key_pump_call);
                textView.setTextColor(Color.parseColor("#4A4545"));
                break;
            case DISP_WORK:
                imageView.setImageResource(R.drawable.key_pump_work);
                textView.setTextColor(Color.parseColor("#4A4545"));
                break;
            case DISP_AUTH:
                imageView.setImageResource(R.drawable.key_pump_auth);
                textView.setTextColor(Color.parseColor("#DEEB8E"));
                break;
            case DISP_BUSY:
                imageView.setImageResource(R.drawable.key_pump_busy);
                textView.setTextColor(Color.parseColor("#4A4545"));
                break;
            case DISP_NOTPAY:
                imageView.setImageResource(R.drawable.key_pump_not_pay);
                textView.setTextColor(Color.parseColor("#4A4545"));
                break;
            default:
                imageView.setImageResource(R.drawable.key_pump_off);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }

        return gridView;
    }

//	public StatusPump getStatus (int position) {
//		return arrStatus.get(position);
//    }

    public String getNumber (int position) {
        return this.ptsMasterInner.mListDispenser.get(position).getDispenserIndex();
//		return arrNumber.get(position);
    }

    @Override
    public int getCount() {
//		Log.v("ImageAdaptor", "getCount() = " + ptsMasterInner.mListDispenser.size());
        if (this.mode.equals("Disable")) {
            return this.ptsMasterInner.mListDispenser.size();
        }
        else {
            return this.size;
        }
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
