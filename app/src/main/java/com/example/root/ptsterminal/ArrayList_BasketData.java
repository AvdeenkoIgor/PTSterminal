package com.example.root.ptsterminal;

import java.util.ArrayList;

import android.R.bool;

public class ArrayList_BasketData {

    /**Номер корзины*/
	public int basket;
    /**Количество товарных позиций*/
	public int itemCount;
    /**Количество оплат*/
	public int payCount;
    /**Уникальный идентификатор продажи*/
	public int tag;
    /**Сумма товарных позиций*/
	public double itemSum;
    /**Сумма оплат*/
	public double paySum;
    /**Номер чека/ Если чек не закрыт = -1*/
	public int billNum;
    /**Код кассира*/
	public int clerk;
    /**Флаги*/
	public int flags;
    /**Количество элементов*/
	public int size;
    /**Пустая корзина*/
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
	* discountType - Тип скидки.
	*******************************/
	class DiscountTypeEnum {
	    public static final int SingleOpPerc = 0;		//	процентная скидка на последнюю товарную операцию.
	    public static final int SingleOpSum = 1;		//	суммовая скидка на последнюю товарную операцию.
	    public static final int SubtotPerc = 2;			//	процентная скидка на промежуточный итог.
	    public static final int SubtotSum = 3;			//	суммовая скидка на промежуточный итог.
	}
	//---------------------------------------------------
	/******************************
	* itemType - Тип элемента товарной корзины.
	*******************************/
	class ItemTypeEnum {
	    public static final int GoodsItemType = 0;		//	Товар.
	    public static final int DispenseItemType = 1;	//	Топливо
	    public static final int DiscountItemType = 2;	//	Скидка
	    public static final int CommentItemType = 3;	//	Коментарий
	    public static final int PaymentItemType = 4;	//	Оплата
	}
	//---------------------------------------------------

}
