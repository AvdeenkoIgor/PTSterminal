package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_ProductAssortment extends HashMap<String, String> {
//    private static final long serialVersionUID = 1L;
    public static final String articleId =	"articleId";		//--Product article
    public static final String name =		"name";				//--Product name
    public static final String shortName =	"shortName";		//--Product short name
    public static final String unitName =	"unitName";			//--Product unit name
    public static final String price =		"price";			//--Product price
    public static final String type =		"type";				//--Product type Goods = 0 , Service = 1 , Dispense = 2 , Ingredient = 3 , Compound = 4 , Pack = 5
    public static final String rest =		"rest";				//--Остаток
  
    public ArrayList_ProductAssortment(String ARTICLEID, String NAME, String SHORTNAME, String UNITNAME, String PRICE, String TYPE, String REST) {
        super();
        super.put(articleId,	ARTICLEID);
        super.put(name,			NAME);
        super.put(shortName,	SHORTNAME);
        super.put(unitName,		UNITNAME);
        super.put(price,		PRICE);
        super.put(type,			TYPE);
        super.put(rest,			REST);
    }   
    
    public ArrayList_ProductAssortment() {
        super();
        super.put(articleId,	"");
        super.put(name,			"");
        super.put(shortName,	"");
        super.put(unitName,		"");
        super.put(price,		"");
        super.put(type,			"");
        super.put(rest,			"");
    }   

    // ------------------------------------------------------------

    public String getArticleId() {
        return super.get(articleId);
    }

    public String getName() {
        return super.get(name);
    }

    public String getShortName() {
        return super.get(shortName);
    }

    public String getUnitName() {
        return super.get(unitName);
    }

    public String getPrice() {
        return super.get(price);
    }
    
    public String getRest() {
        return super.get(rest);
    }
    
    public String getType() {
        return super.get(type);
    }
    
    // ------------------------------------------------------------

    public void setArticleId(String ARTICLEID) {
        super.put(articleId, ARTICLEID);
    }
    
    public void setName(String NAME) {
        super.put(name, NAME);
    }
    
    public void setShortName(String NAME) {
        super.put(shortName, NAME);
    }
    
    public void setUnitName(String UNITNAME) {
        super.put(unitName, UNITNAME);
    }
    
    public void setPrice(String PRICE) {
        super.put(price, PRICE);
    }
    
    public void setRest(String REST) {
        super.put(rest, REST);
    }

    public void setType(String TYPE) {
        super.put(type, TYPE);
    }
}
