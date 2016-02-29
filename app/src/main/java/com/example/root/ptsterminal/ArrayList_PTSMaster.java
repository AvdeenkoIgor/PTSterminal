package com.example.root.ptsterminal;

import java.util.ArrayList;

import android.util.Log;

///////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////
//Qvector <some_class> vcec_test;
//Qvector <some_class> vcec_test2;
//
//for(int i=0; i < vcec_test.count(); i++)
//	vcec_test2.add(vcec_test[i]);

public class ArrayList_PTSMaster {
	//hello from home
	public int idPacket;
	public String idUser;
	public String name;
	public ArrayList<ArrayList_ProductAssortment> mListProductAssortment;
	public ArrayList<ArrayList_Store> mListStore;
	public ArrayList<ArrayList_PayForm> mListPayForm;
	public ArrayList<ArrayList_Dispenser> mListDispenser;
	public ArrayList<ArrayList_Order> mListOrder;
	public ArrayList<ArrayList_Clerks> mListClerks;
    public ArrayList<ArrayList<ArrayList_DispenserCfg>>mListDispenserCfg; 
    public ArrayList<ArrayList_BasketData> mListBasket;
    public ArrayList_Signal mListSignal;
    public ArrayList_BasketItems OrderBasketItem;
    public ArrayList_PaymentAnswer PaymentAnswer;

    public enum StatusPump {
    	DISP_UNDEFINED("DISP_UNDEFINED"),
    	DISP_OFF("DISP_OFF"),
    	DISP_IDLE("DISP_IDLE"),
    	DISP_CALL("DISP_CALL"),
    	DISP_WORK("DISP_WORK"),
    	DISP_AUTH("DISP_AUTH"),
    	DISP_BUSY("DISP_BUSY"),
    	DISP_NOTPAY("DISP_NOTPAY");
        private String typeValue;private StatusPump(String type) {typeValue = type;}public String getTypeValue() {return typeValue;}
        static public StatusPump getType(String pType) {for (StatusPump type: StatusPump.values()) {if (type.getTypeValue().equals(pType)) {return type;}}throw new RuntimeException("unknown type");}
    }

//	public Integer FlagsEnum(String name) {
//		int res = 0;
//		if (name.equals("AfterPayment")) {
//			res = 1;
//		} else 
//		if (name.equals("DispenceOpened")) {
//			res = 2;
//		} else 
//		if (name.equals("DispenceFinish")) {
//			res = 4;
//		} else 
//		if (name.equals("PayRet")) {
//			res = 16;
//		} else 
//		if (name.equals("AfterSplit")) {
//			res = 32;
//		}  
//		return res;
//	}

	
	public ArrayList_PTSMaster()
	{  
		this.mListProductAssortment = new ArrayList<ArrayList_ProductAssortment>();
		this.mListStore = new ArrayList<ArrayList_Store>();
		this.mListDispenser = new ArrayList<ArrayList_Dispenser>();
		this.mListOrder = new ArrayList<ArrayList_Order>();
		this.mListClerks = new ArrayList<ArrayList_Clerks>();
		this.mListDispenserCfg = new ArrayList<ArrayList<ArrayList_DispenserCfg>>();
		this.mListPayForm = new ArrayList<ArrayList_PayForm>();
		this.mListBasket = new ArrayList<ArrayList_BasketData>();
		this.OrderBasketItem = new ArrayList_BasketItems();
		this.PaymentAnswer = new ArrayList_PaymentAnswer();
		this.mListSignal = new ArrayList_Signal();
		this.idPacket = 0;
		this.idUser = "";
		this.name = "";
	}
	
//	protected void finalize () {
//		Log.d("Desctructor", "finalize...");
//	}
	
	public void getSize()
	{  
		Log.d("DDD", "mListProductAssortment.size = " + this.mListProductAssortment.size());
	}

	public ArrayList_PTSMaster getCopy()
	{  
		
		ArrayList_PTSMaster r = new ArrayList_PTSMaster();
		
		r.mListProductAssortment = new ArrayList<ArrayList_ProductAssortment>();
		for (int i = 0; i < this.mListProductAssortment.size(); i++) {
			r.mListProductAssortment.add((ArrayList_ProductAssortment)this.mListProductAssortment.get(i));		
		}
		r.mListStore = new ArrayList<ArrayList_Store>();
		for (int i = 0; i < this.mListStore.size(); i++) {
			r.mListStore.add((ArrayList_Store)this.mListStore.get(i));		
		}
		r.mListDispenser = new ArrayList<ArrayList_Dispenser>();
		for (int i = 0; i < this.mListDispenser.size(); i++) {
			r.mListDispenser.add((ArrayList_Dispenser)this.mListDispenser.get(i));		
		}
		r.mListOrder = new ArrayList<ArrayList_Order>();
		for (int i = 0; i < this.mListOrder.size(); i++) {
			r.mListOrder.add((ArrayList_Order)this.mListOrder.get(i));		
		}
		r.mListClerks = new ArrayList<ArrayList_Clerks>();
		for (int i = 0; i < this.mListClerks.size(); i++) {
			r.mListClerks.add((ArrayList_Clerks)this.mListClerks.get(i));		
		}
		r.mListDispenserCfg = new ArrayList<ArrayList<ArrayList_DispenserCfg>>();
		for (int i = 0; i < this.mListDispenserCfg.size(); i++) {
			r.mListDispenserCfg.add(new ArrayList<ArrayList_DispenserCfg>());
//			r.mListDispenserCfg.add((ArrayList<ArrayList_DispenserCfg>)this.mListDispenserCfg.get(i));		
			for (int n = 0; n < this.mListDispenserCfg.get(i).size(); n++) {
//				r.mListDispenserCfg.get(i).add(new ArrayList_DispenserCfg());
				r.mListDispenserCfg.get(i).add((ArrayList_DispenserCfg)this.mListDispenserCfg.get(i).get(n));		
			}
		}
		r.mListPayForm = new ArrayList<ArrayList_PayForm>();
		for (int i = 0; i < this.mListPayForm.size(); i++) {
			r.mListPayForm.add((ArrayList_PayForm)this.mListPayForm.get(i));		
		}
		r.mListBasket = new ArrayList<ArrayList_BasketData>();
		for (int i = 0; i < this.mListBasket.size(); i++) {
			r.mListBasket.add((ArrayList_BasketData)this.mListBasket.get(i));		
//			for (int n = 0; n < this.mListBasket.get(i).basketItem.size(); n++) {
//				r.mListBasket.get(i).basketItem.add((ArrayList_BasketItems)this.mListBasket.get(i).basketItem.get(n));
//			}
		}
		r.OrderBasketItem = new ArrayList_BasketItems();
		r.OrderBasketItem  = this.OrderBasketItem;		
		r.PaymentAnswer = new ArrayList_PaymentAnswer();
		r.PaymentAnswer = this.PaymentAnswer;
		r.mListSignal = new ArrayList_Signal();
		r.mListSignal = this.mListSignal;		
		r.idPacket = this.idPacket;
		r.idUser = this.idUser;
		r.name = this.name;
		return r;
	}

	public Object clone(ArrayList_PTSMaster object)
	{  
		ArrayList_PTSMaster r = new ArrayList_PTSMaster();
		
		return r;
	}


}

