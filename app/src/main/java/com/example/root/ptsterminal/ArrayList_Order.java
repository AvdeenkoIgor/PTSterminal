package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_Order extends HashMap<String, String> {
//    private static final long serialVersionUID = 1L;
    public static final String articleId =		"articleId";		//--Product article
    public static final String amount =			"amount";			//--Order amount (in money)
    public static final String volume =			"volume";			//--Order volume (in litres)
    public static final String type =			"type";				//--Order type ("litres" or "money" or "full")
    public static final String price =			"price";			//--Product price
    public static final String cost =			"cost";				//--Product cost
    public static final String color =			"color";			//--Product color
   
    public ArrayList_Order(
    		String ARTICLEID, 
    		String AMOUNT,
    		String VOLUME,
    		String TYPE, 
    		String PRICE,
    		String COST,
    		String COLOR  		) {
        super();
        super.put(articleId,	ARTICLEID);
        super.put(amount,		AMOUNT);
        super.put(volume,		VOLUME);
        super.put(type,			TYPE);
        super.put(price,		PRICE);
        super.put(cost,			COST);
        super.put(color,		COLOR);
    }   
    
    public ArrayList_Order() {
        super();
        super.put(articleId,	"");
        super.put(amount,		"");
        super.put(volume,		"");
        super.put(type,			"");
        super.put(price,		"");
        super.put(cost,			"");
        super.put(color,		"");
    }   

    // ------------------------------------------------------------

    public String getArticleId() {
        return super.get(articleId);
    }

    public String getAmount() {
        return super.get(amount);
    }

    public String getVolume() {
        return super.get(volume);
    }

    public String getType() {
        return super.get(type);
    }
    
    public String getPrice() {
        return super.get(price);
    }
    
    public String getCost() {
        return super.get(cost);
    }
    
    public String getColor() {
        return super.get(color);
    }
    
    // ------------------------------------------------------------

    public void setArticleId(String ARTICLEID) {
        super.put(articleId, ARTICLEID);
    }
    
    public void setAmount(String AMOUNT) {
        super.put(amount, AMOUNT);
    }
    
    public void setVolume(String VOLUME) {
        super.put(volume, VOLUME);
    }
    
    public void setType(String TYPE) {
        super.put(type, TYPE);
    }
    
    public void setPrice(String PRICE) {
        super.put(price, PRICE);
    }
    
    public void setCost(String COST) {
        super.put(cost, COST);
    }
    
    public void setColor(String COLOR) {
        super.put(color, COLOR);
    }
    
}
