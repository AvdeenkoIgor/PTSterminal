package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_PaymentAnswer extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String tag =			"tag";			//--tag
    public static final String paySum =			"paySum";		//--idpaySum
    public static final String payCount =		"payCount";		//--payCount
    public static final String itemSum =		"itemSum";		//--idpaySum
    public static final String itemCount =		"itemCount";	//--itemCount
    public static final String flags =			"flags";		//--flags
    public static final String clerk =			"clerk";		//--clerk
    public static final String billNum =		"billNum";		//--billNum
    public static final String basket =			"basket";		//--basket
  
    public ArrayList_PaymentAnswer() {
        super();
        super.put(tag,		"");
        super.put(paySum,	"");
        super.put(payCount,	"");
        super.put(itemSum,	"");
        super.put(itemCount,"");
        super.put(flags,	"");
        super.put(clerk,	"");
        super.put(billNum,	"");
        super.put(basket,	"");
    }   

    public ArrayList_PaymentAnswer(String TAG, String PAYSUM, String PAYCOUNT, String ITEMSUM, String ITEMCOUNT, String FLAGS, String CLERCK, String BILLNUM, String BASKET) {
        super();
        super.put(tag,		TAG);
        super.put(paySum,	PAYSUM);
        super.put(payCount,	PAYCOUNT);
        super.put(itemSum,	ITEMSUM);
        super.put(itemCount,ITEMCOUNT);
        super.put(flags,	FLAGS);
        super.put(clerk,	CLERCK);
        super.put(billNum,	BILLNUM);
        super.put(basket,	BASKET);
    }   

    // ------------------------------------------------------------

    public String getTag() {
        return super.get(tag);
    }

    public String getPaySum() {
        return super.get(paySum);
    }

    public String getPayCount() {
        return super.get(payCount);
    }

    public String getItemSum() {
        return super.get(itemSum);
    }

    public String getItemCount() {
        return super.get(itemCount);
    }

    public String getFlags() {
        return super.get(flags);
    }

    public String getClerk() {
        return super.get(clerk);
    }

    public String getBillNum() {
        return super.get(billNum);
    }

    public String getBasket() {
        return super.get(basket);
    }

    // ------------------------------------------------------------
    public void setTag(String VALUE) {
        super.put(tag, VALUE);
    }
    
    public void setPaySum(String VALUE) {
        super.put(paySum, VALUE);
    }
    
    public void setPayCount(String VALUE) {
        super.put(payCount, VALUE);
    }
    
    public void setItemSum(String VALUE) {
        super.put(itemSum, VALUE);
    }
    
    public void setItemCount(String VALUE) {
        super.put(itemCount, VALUE);
    }
    
    public void setFlags(String VALUE) {
        super.put(flags, VALUE);
    }
    
    public void setClerk(String VALUE) {
        super.put(clerk, VALUE);
    }
    
    public void setBillNum(String VALUE) {
        super.put(billNum, VALUE);
    }
    
    public void setBasket(String VALUE) {
        super.put(basket, VALUE);
    }
    
}
