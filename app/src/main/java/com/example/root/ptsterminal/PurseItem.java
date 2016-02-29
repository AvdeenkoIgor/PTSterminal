package com.example.root.ptsterminal;

import java.util.HashMap;

public class PurseItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String purseId = "purseId";			//--Purse id
    public static final String purseType = "purseType";			//--Purse type
    public static final String purseStatus = "purseStatus";		//--Purse status(blocked, active, etc)
    public static final String balance = "balance";			//--Balance of the day
   
    public PurseItem(String PURSEID, String PURSETYPE, String PURSESTATUS, String BALANCE) {
        super();
        super.put(purseId, PURSEID);
        super.put(purseType, PURSETYPE);
        super.put(purseStatus, PURSESTATUS);
        super.put(balance, BALANCE);
    }   
    
    public String getId() {
        return super.get(purseId);
    }

    public String getType() {
        return super.get(purseType);
    }

    public String getStatus() {
        return super.get(purseStatus);
    }

    public String getBalance() {
        return super.get(balance);
    }

    public void setId(String PURSEID) {
        super.put(purseId, PURSEID);
    }
    
    public void setType(String PURSETYPE) {
        super.put(purseType, PURSETYPE);
    }
    
    public void setStatus(String PURSESTATUS) {
        super.put(purseStatus, PURSESTATUS);
    }
    
    public void setBalance(String BALANCE) {
        super.put(balance, BALANCE);
    }
    
}
