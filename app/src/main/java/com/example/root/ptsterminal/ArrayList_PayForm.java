package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_PayForm extends HashMap<String, String> {
//    private static final long serialVersionUID = 1L;
    public static final String type =	"type";		//--Pay form type
    public static final String name =	"name";		//--Pay form name
    public static final String id =		"id";		//--Pay form id
  
    public ArrayList_PayForm(String TYPE, String NAME, String ID) {
        super();
        super.put(type,	TYPE);
        super.put(name,	NAME);
        super.put(id,	ID);
    }   
    
    public ArrayList_PayForm() {
        super();
        super.put(type,	"");
        super.put(name,	"");
        super.put(id,	"");
    }   

    // ------------------------------------------------------------

    public String getType() {
        return super.get(type);
    }

    public String getName() {
        return super.get(name);
    }

    public String getId() {
        return super.get(id);
    }

    // ------------------------------------------------------------

    public void setType(String VALUE) {
        super.put(type, VALUE);
    }
    
    public void setName(String VALUE) {
        super.put(name, VALUE);
    }
    
    public void setId(String VALUE) {
        super.put(id, VALUE);
    }
    
}
