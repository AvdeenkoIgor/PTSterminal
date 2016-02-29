package com.example.root.ptsterminal;

import java.util.HashMap;

public class OperatorItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String CardCode = "cardCode";//--Code card operator
    public static final String CardPin = "cardPin";//--PIN code card operator
    
    public OperatorItem(String cardCode, String cardPin) {
        super();
        super.put(CardCode, cardCode);
        super.put(CardPin, cardPin);
    }   
    
    public String getCardCode() {
        return super.get(CardCode);
    }

    public String getCardPin() {
        return super.get(CardPin);
    }
    
    public void setCardCode(String cardCode) {
        super.put(CardCode, cardCode);
    }
    
    public void setCardPin(String cardPin) {
        super.put(CardPin, cardPin);
    }
}
