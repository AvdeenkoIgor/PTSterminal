package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_DispenserCfg extends HashMap<String, String> {
//    private static final long serialVersionUID = 1L;
    public static final String index =			"index";			//--Dispenser index
    public static final String name =			"name";				//--Dispenser name
    public static final String nozzle =			"nozzle";			//--Dispenser permissions
    public static final String store =			"store";			//--Dispenser store
    public static final String count =			"count";			//--Dispenser nozzle count
   
    public ArrayList_DispenserCfg(
    		String INDEX, 
    		String NAME,
    		String NOZZLE,
    		String STORE, 
    		String COUNT 
  		) {
        super();
        super.put(index,		INDEX);
        super.put(name,			NAME);
        super.put(nozzle,		NOZZLE);
        super.put(store,		STORE);
        super.put(count,		COUNT);
    }   
    
    public ArrayList_DispenserCfg() {
        super();
        super.put(index,		"");
        super.put(name,			"");
        super.put(nozzle,		"");
        super.put(store,		"");
        super.put(count,		"");
    }   

    // ------------------------------------------------------------

    public String getIndex() {
        return super.get(index);
    }

    public String getName() {
        return super.get(name);
    }

    public String getNozzle() {
        return super.get(nozzle);
    }

    public String getStore() {
        return super.get(store);
    }
    
    public String getCcount() {
        return super.get(count);
    }
    
    
    // ------------------------------------------------------------

    public void setIndex(String VALUE) {
        super.put(index, VALUE);
    }
    
    public void setName(String VALUE) {
        super.put(name, VALUE);
    }
    
    public void setNozzle(String VALUE) {
        super.put(nozzle, VALUE);
    }
    
    public void setStore(String VALUE) {
        super.put(store, VALUE);
    }
    
    public void setCount(String VALUE) {
        super.put(count, VALUE);
    }
    
    
}
