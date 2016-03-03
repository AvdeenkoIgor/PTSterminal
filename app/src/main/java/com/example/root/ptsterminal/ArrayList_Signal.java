package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_Signal extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String msg_id =			"msg_id";			//--msg_id
    public static final String id =				"id";				//--id
   
    public ArrayList_Signal() {
        super();
        super.put(msg_id,		"");
        super.put(id,			"");
    }   

    public ArrayList_Signal(String MSG_ID, String ID) {
        super();
        super.put(msg_id,		MSG_ID);
        super.put(id,			ID);
    }   

    // ------------------------------------------------------------

    public String getMsgId() {
        return super.get(msg_id);
    }

    public String getId() {
        return super.get(id);
    }

    // ------------------------------------------------------------
    public void setMsgId(String VALUE) {
        super.put(msg_id, VALUE);
    }
    
    public void setId(String VALUE) {
        super.put(id, VALUE);
    }
}
