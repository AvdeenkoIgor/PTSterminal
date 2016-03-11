package com.example.root.ptsterminal;

import java.util.HashMap;
//
// не знаю для чего использовать
//
public class ContactItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String NAME = "name";
    
    public ContactItem(String name, String phone) {
        super();
        super.put(NAME, name);
    }   
    
    public String getName() {
        return super.get(NAME);
    }

    public void setName(String name) {
        super.put(NAME, name);
    }
    
}
