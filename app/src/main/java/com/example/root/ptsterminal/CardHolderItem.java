package com.example.root.ptsterminal;

import java.util.HashMap;

public class CardHolderItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String CardCode = "cardCode";			//--Code card holder
    public static final String CardPin = "cardPin";				//--PIN code card holder
    public static final String CardSerial = "cardSerial";		//--Serial number card
    public static final String HolderName = "holderName";		//--Holder name
    public static final String VehicleNumber = "vehicleNumber";	//--Vehicle number
    
    public CardHolderItem(String cardCode, String cardPin, String cardSerial, String holderName, String vehicleNumber) {
        super();
        super.put(CardCode, cardCode);
        super.put(CardPin, cardPin);
        super.put(CardSerial, cardSerial);
        super.put(HolderName, holderName);
        super.put(VehicleNumber, vehicleNumber);
    }   
    
    public String getCardCode() {
        return super.get(CardCode);
    }

    public String getCardPin() {
        return super.get(CardPin);
    }
    
    public String getCardSerial() {
        return super.get(CardSerial);
    }

    public String getHolderName() {
        return super.get(HolderName);
    }

    public String getVehicleNumber() {
        return super.get(VehicleNumber);
    }

    public void setCardCode(String cardCode) {
        super.put(CardCode, cardCode);
    }
    
    public void setCardPin(String cardPin) {
        super.put(CardPin, cardPin);
    }
    
    public void setCardSerial(String cardSerial) {
        super.put(CardSerial, cardSerial);
    }
    
    public void setHolderName(String holderName) {
        super.put(HolderName, holderName);
    }
    
    public void setVehicleNumber(String vehicleNumber) {
        super.put(VehicleNumber, vehicleNumber);
    }
}
