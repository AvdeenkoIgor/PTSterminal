package com.example.root.ptsterminal;

import java.util.HashMap;

public class BasketItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String articleId = "articleId";		//--Product article
    public static final String actionType = "actionType";	//--The type of action (add, returning)
    public static final String unitName = "unitName";		//--Name of unit of product (points or litres)
    public static final String unit = "unit";				//--Unit of product (points or litres)
    public static final String amount = "amount";			//--Amount
    public static final String name = "name";				//--article name
    public static final String price = "price";				//--article price
    public static final String cost = "cost";				//--article cost
    public static final String purseId = "purseId";			//--Purse id
    public static final String balance = "balance";			//--Balance of the day
   
    public BasketItem(String ARTICLEID, String ACTIONTYPE, String UNITNAME, String UNIT, String AMOUNT, String NAME, String PRICE, String COST, String PURSEID, String BALANCE) {
        super();
        super.put(articleId, ARTICLEID);
        super.put(actionType, ACTIONTYPE);
        super.put(unitName, UNITNAME);
        super.put(unit, UNIT);
        super.put(amount, AMOUNT);
        super.put(name, NAME);
        super.put(price, PRICE);
        super.put(cost, COST);
        super.put(purseId, PURSEID);
        super.put(balance, BALANCE);
    }   
    
    public BasketItem() {
        super();
        super.put(articleId, "");
        super.put(actionType, "");
        super.put(unitName, "");
        super.put(unit, "");
        super.put(amount, "");
        super.put(name, "");
        super.put(price, "");
        super.put(cost, "");
        super.put(purseId, "");
        super.put(balance, "");
    }   
    
    public String getArticleId() {
        return super.get(articleId);
    }

    public String getType() {
        return super.get(actionType);
    }

    public String getUnitName() {
        return super.get(unitName);
    }

    public String getUnit() {
        return super.get(unit);
    }

    public String getAmount() {
        return super.get(amount);
    }

    public String getName() {
        return super.get(name);
    }

    public String getPrice() {
        return super.get(price);
    }

    public String getCost() {
        return super.get(cost);
    }

    public String getPurseId() {
        return super.get(purseId);
    }

    public String getBalance() {
        return super.get(balance);
    }

    public void setPurseId(String PURSEID) {
        super.put(purseId, PURSEID);
    }

    public void setArticleId(String ARTICLEID) {
        super.put(articleId, ARTICLEID);
    }
    
    public void setActionType(String ACTIONTYPE) {
        super.put(actionType, ACTIONTYPE);
    }
    
    public void setUnitName(String UNITNAME) {
        super.put(unitName, UNITNAME);
    }
    
    public void setUnit(String UNIT) {
        super.put(unit, UNIT);
    }
    
    public void setAmount(String AMOUNT) {
        super.put(amount, AMOUNT);
    }
    
    public void setName(String NAME) {
        super.put(name, NAME);
    }
    
    public void setPrice(String PRICE) {
        super.put(price, PRICE);
    }
    
    public void setCost(String COST) {
        super.put(cost, COST);
    }
    
    public void setBalance(String BALANCE) {
        super.put(balance, BALANCE);
    }
    
}
