package com.example.root.ptsterminal;

import java.util.HashMap;

public class ProductAssortmentItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String articleId = "articleId";		//--Product article
    public static final String name = "name";			//--article name
    public static final String unitName = "unitName";		//--article unit name
    public static final String price = "price";			//--article price
   
    public ProductAssortmentItem(String ARTICLEID, String NAME, String UNITNAME, String PRICE) {
        super();
        super.put(articleId, ARTICLEID);
        super.put(name, NAME);
        super.put(unitName, UNITNAME);
        super.put(price, PRICE);
    }   
    
    public ProductAssortmentItem() {
        super();
        super.put(articleId, "");
        super.put(name, "");
        super.put(unitName, "");
        super.put(price, "");
    }   
    
    public String getArticleId() {
        return super.get(articleId);
    }

    public String getName() {
        return super.get(name);
    }

    public String getUnitName() {
        return super.get(unitName);
    }

    public String getPrice() {
        return super.get(price);
    }

    public void setArticleId(String ARTICLEID) {
        super.put(articleId, ARTICLEID);
    }
    
    public void setName(String NAME) {
        super.put(name, NAME);
    }
    
    public void setUnitName(String UNITNAME) {
        super.put(unitName, UNITNAME);
    }
    
    public void setPrice(String PRICE) {
        super.put(price, PRICE);
    }
    
}
