package com.example.root.ptsterminal;

import java.util.ArrayList;

import android.R.bool;

public class ArrayList_BasketData {

    /**����� �������*/
	public int basket;
    /**���������� �������� �������*/
	public int itemCount;
    /**���������� �����*/
	public int payCount;
    /**���������� ������������� �������*/
	public int tag;
    /**����� �������� �������*/
	public double itemSum;
    /**����� �����*/
	public double paySum;
    /**����� ����/ ���� ��� �� ������ = -1*/
	public int billNum;
    /**��� �������*/
	public int clerk;
    /**�����*/
	public int flags;
    /**���������� ���������*/
	public int size;
    /**������ �������*/
	public bool isEmpty;
    public ArrayList<ArrayList_BasketItems> basketItem;
    
    ArrayList_BasketData() {
        basket = 0;
        itemCount = 0;
        payCount = 0;
        clerk = 0;
        tag = 0;
        flags = 0;
        itemSum = 0;
        paySum = 0;
        billNum = 0;
        basketItem = new ArrayList<ArrayList_BasketItems>();
    }
    
	/******************************
	* flags - Basket items flags
	*******************************/
	class FlagsEnum {
	    public static final int AfterPayment = 1;
	    public static final int DispenceOpened = 2;
	    public static final int DispenceFinish = 4;
	    public static final int PayRet = 16;
	    public static final int AfterSplit = 32;
	}
	//---------------------------------------------------
	/******************************
	* discountType - ��� ������.
	*******************************/
	class DiscountTypeEnum {
	    public static final int SingleOpPerc = 0;		//	���������� ������ �� ��������� �������� ��������.
	    public static final int SingleOpSum = 1;		//	�������� ������ �� ��������� �������� ��������.
	    public static final int SubtotPerc = 2;			//	���������� ������ �� ������������� ����.
	    public static final int SubtotSum = 3;			//	�������� ������ �� ������������� ����.
	}
	//---------------------------------------------------
	/******************************
	* itemType - ��� �������� �������� �������.
	*******************************/
	class ItemTypeEnum {
	    public static final int GoodsItemType = 0;		//	�����.
	    public static final int DispenseItemType = 1;	//	�������
	    public static final int DiscountItemType = 2;	//	������
	    public static final int CommentItemType = 3;	//	����������
	    public static final int PaymentItemType = 4;	//	������
	}
	//---------------------------------------------------

}
