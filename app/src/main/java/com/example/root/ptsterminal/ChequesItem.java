package com.example.root.ptsterminal;

import java.util.HashMap;

public class ChequesItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String chequeId = "chequeId";		//--Cheque number
    public static final String chequeStatus = "chequeStatus";	//--Cheque status
    public static final String purseId = "purseId";		//--The authorized number of purse cards
    public static final String chequeCost = "chequeCost";		//--Cheque cost(all items in cheque)
    public static final String dtClose = "dtClose";		//--Datetime of close cheque
    
    public ChequesItem(String CHEQUEID, String CHEQUESTATUS, String PURSEID, String CHEQUECOST, String DTCLOSE) {
        super();
        super.put(chequeId, CHEQUEID);
        super.put(chequeStatus, CHEQUESTATUS);
        super.put(purseId, PURSEID);
        super.put(chequeCost, CHEQUECOST);
        super.put(dtClose, DTCLOSE);
    }   
    
    public ChequesItem() {
        super();
        super.put(chequeId, "");
        super.put(chequeStatus, "");
        super.put(purseId, "");
        super.put(chequeCost, "");
        super.put(dtClose, "");
    }   
    
    public String getChequeId() {
        return super.get(chequeId);
    }

    public String getStatus() {
        return super.get(chequeStatus);
    }
    
    public String getPurseId() {
        return super.get(purseId);
    }

    public String getChequeCost() {
        return super.get(chequeCost);
    }
    
    public String getDate() {
        return super.get(dtClose);
    }

    public void setChequeId(String CHEQUEID) {
        super.put(chequeId, CHEQUEID);
    }
    
    public void setStatus(String CHEQUESTATUS) {
        super.put(chequeStatus, CHEQUESTATUS);
    }

    public void setPurseId(String PURSEID) {
        super.put(purseId, PURSEID);
    }
    
    public void setChequeCost(String CHEQUECOST) {
        super.put(chequeCost, CHEQUECOST);
    }
    
    public void setDate(String DTCLOSE) {
        super.put(dtClose, DTCLOSE);
    }
    
}
