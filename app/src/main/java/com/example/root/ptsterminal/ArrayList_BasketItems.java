package com.example.root.ptsterminal;

import android.R.bool;
import com.example.root.ptsterminal.ArrayList_BasketData.FlagsEnum;

public class ArrayList_BasketItems {
	
    /**���������� ID ������*/
	public int id;
    /**����� �������*/
	public int basket;
    /**����� � �������*/
	public int index;
    /**��� ��������*/
	public int itemType;
    /**�����*/
	public double sum;
    /**���������*/
	public double cost;
    /**�����*/
	public int flags;
    /**��� �������*/
	public int clerk;
    /**���������� ������������� �������*/
	public int tag;
    /**��� ����� ������*/
	public int payForm;
    /**��� �������� (�����������)*/
	public int person;
    /**������������*/
	public String name;
    /**����� �����, ��������� ...*/
	public String doc;
    /**������ ����� ...*/
	public String signature;
    /**����� ����*/
	public int billNum;
    /**������� ��������*/
	public bool ret;
    /**����������*/
	public double quantity;
    /**��� ������*/
	public int discountType;
    /**������� ��� �����*/
	public double rate;
    /**����� ������������ �����*/
	public int pump;
    /**����� ������������ �����*/
	public int nozzle;
    /** ��������*/
	public int store;
    /**��� ������*/
	public int product;
    /**���������� �����*/
	public double volume;
    /**���������� �����*/
	public double orderVolume;
    /**����*/
	public double price;
    /**time_t*/
	public int time;
    /**���������������� �������*/
	public int utag;
    /**Comment*/
	public String comment;
    
    ArrayList_BasketItems() {
//        itemType = 1;
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
//        comment = "";
//        signature = "";
        
        
        volume = 0.0;
        utag = 0;
        time = 0;
        tag = 0;
        sum = 0.0;
        signature = "";
        rate = 0.0;
        quantity = 0.0;
        pump =  0;//
        product = 0;
        price = -1.0;//
        person = 0;
        payForm = 0;
        orderVolume = 0.0;//
        nozzle = 0;//
        name = "";
        itemType = 1;//
        index = 0;
        id = 0;
        flags = 0;
        doc = "";
        discountType = 0;
        cost = 0.0;
        comment = "";
        clerk = 0;
        billNum = -1;
        basket = 0;

	}
	 
    ArrayList_BasketItems( int id,int basket,int index,int itemType,double sum,double cost,int flags,int clerk,int tag,int payForm,int person,
    		String name,String doc,String signature,int billNum,bool ret,double quantity,int discountType,double rate,int pump,int nozzle,int store,
    		int product,double volume,double orderVolume,double price,int time,int utag) {
    	this.itemType = itemType;
    	this.id = id;
    	this.basket = basket;
    	this.index = index;
    	this.flags = flags;
    	this.tag = tag;
    	this.clerk = clerk;
    	this.sum = sum;
    	this.cost = cost;
    	this.payForm = payForm;
    	this.person = person;
    	this.billNum = billNum;
    	this.time = time;
    	this.utag = utag;
    	this.product = product;
    	this.quantity = quantity;
    	this.price = price;
    	this.pump = pump;
    	this.nozzle = nozzle;
    	this.store = store;
    	this.orderVolume = orderVolume;
    	this.volume = volume;
    	this.rate = rate;
    	this.discountType = discountType;
    	this.ret = ret;
    	this.signature = signature;
    	this.doc = doc;
    	this.name = name;
	}

    /**���������� ������� ��������*/
    void setRet(Boolean r) {if(r) flags |= FlagsEnum.PayRet; else flags &= ~FlagsEnum.PayRet;}
}
