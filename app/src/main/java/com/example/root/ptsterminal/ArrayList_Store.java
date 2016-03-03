package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_Store extends HashMap<String, String> {
//    private static final long serialVersionUID = 1L;
    public static final String articleId =	"articleId";		//--Product article
    public static final String index =		"index";			//--Store index
    public static final String size =		"size";				//--Store size
    public static final String height =		"height";			//--Store height
    public static final String unit =		"unit";				//--Product unit
    public static final String shortName =	"shortName";		//--Product shortName
    public static final String name =		"name";				//--Product name
  
    public ArrayList_Store(String ARTICLEID, String INDEX, String SIZE, String HEIGHT, String UNIT, String SHORTNAME, String NAME) {
        super();
        super.put(articleId,	ARTICLEID);
        super.put(index,		INDEX);
        super.put(size,			SIZE);
        super.put(height,		HEIGHT);
        super.put(unit,			UNIT);
        super.put(shortName,	SHORTNAME);
        super.put(name,			NAME);
    }   
    
    public ArrayList_Store() {
        super();
        super.put(articleId,	"");
        super.put(index,		"");
        super.put(size,			"");
        super.put(height,		"");
        super.put(unit,			"");
        super.put(shortName,	"");
        super.put(name,			"");
    }   

    // ------------------------------------------------------------

    public String getArticleId() {
        return super.get(articleId);
    }

    public String getIndex() {
        return super.get(index);
    }

    public String getSize() {
        return super.get(size);
    }

    public String getHeight() {
        return super.get(height);
    }

    public String getShortName() {
        return super.get(shortName);
    }

    public String getName() {
        return super.get(name);
    }

    public String getUnit() {
        return super.get(unit);
    }

    // ------------------------------------------------------------

    public void setArticleId(String VALUE) {
        super.put(articleId, VALUE);
    }
    
    public void setIndex(String VALUE) {
        super.put(index, VALUE);
    }
    
    public void setSize(String VALUE) {
        super.put(size, VALUE);
    }
    
    public void setHeight(String VALUE) {
        super.put(height, VALUE);
    }
    
    public void setUnit(String VALUE) {
        super.put(unit, VALUE);
    }
    
    public void setShortName(String VALUE) {
        super.put(shortName, VALUE);
    }
    
    public void setName(String VALUE) {
        super.put(name, VALUE);
    }
    
}
