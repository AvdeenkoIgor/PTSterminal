package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_Dispenser extends HashMap<String, String> {
    public static final String index 			= "index";   		//--Номер заправочного места 
    public static final String status 			= "status";   		//--Текущий статус (ArrayList_PTSMaster -> enum statusPump)
    public static final String nozzle 			= "nozzle";   		//--Текущий пистолет 
    public static final String volume 			= "volume";   		//--Текущая доза
    public static final String order		 	= "order";			//--Состояние заказа (NoOrder = 0 ,	BasketOrder = 1 ,	CheckOrder(Поверка) = 2) 
    public static final String order_volume 	= "order_volume";   //--Доза заказа
    public static final String order_nozzle 	= "order_nozzle";   //--Пистолет из заказа
    public static final String basket 			= "basket";   		//--Номер корзины
	
    public ArrayList_Dispenser(
    		String INDEX,         
    		String STATUS,        
    		String NOZZLE,        
    		String VOLUME,        
    		String ORDER,         
    		String ORDER_VOLUME,  
    		String ORDER_NOZZLE,  
    		String BASKET) {   

        super();
        super.put(index,         INDEX       ); 		      
        super.put(status,        STATUS      ); 		      
        super.put(nozzle,        NOZZLE      ); 		      
        super.put(volume,        VOLUME      ); 		      
        super.put(order,         ORDER       ); 		      
        super.put(order_volume,  ORDER_VOLUME); 		      
        super.put(order_nozzle,  ORDER_NOZZLE); 		      
        super.put(basket,        BASKET      ); 		      
    }   
    
    public ArrayList_Dispenser() {
        super();
        super.put(index,         ""); 		      
        super.put(status,        "DISP_UNDEFINED"); 		      
        super.put(nozzle,        ""); 		      
        super.put(volume,        ""); 		      
        super.put(order,         ""); 		      
        super.put(order_volume,  ""); 		      
        super.put(order_nozzle,  ""); 		      
        super.put(basket,        ""); 		      
    }   
    
    // ------------------------------------------------------------

    public String getDispenserIndex() {
        return super.get(index);
    }

    public String getDispenserStatus() {
        return super.get(status);
    }

    public String getDispenserNozzle() {
        return super.get(nozzle);
    }

    public String getDispenserVolume() {
        return super.get(volume);
    }
    public String getDispenserOrder() {
        return super.get(order);
    }

    public String getDispenserOrder_nozzle() {
        return super.get(order_nozzle);
    }

    public String getDispenserOrder_volume() {
        return super.get(order_volume);
    }

    public String getDispenserBasket() {
        return super.get(basket);
    }


    // ------------------------------------------------------------

    // --- Set
    public void setDispenseIndex(String VALUE) {
        super.put(index, VALUE);
    }
    
    public void setDispenserStatus(String VALUE) {
        super.put(status, VALUE);
    }
    
    public void setDispenseNozzle(String VALUE) {
        super.put(nozzle, VALUE);
    }
    
    public void setDispenseVolume(String VALUE) {
        super.put(volume, VALUE);
    }
    
    public void setDispenseOrder(String VALUE) {
        super.put(order, VALUE);
    }
    
    public void setDispenseOrder_nozzle(String VALUE) {
        super.put(order_nozzle, VALUE);
    }
    
    public void setDispenseOrder_volume(String VALUE) {
        super.put(order_volume, VALUE);
    }
    
    public void setDispenseBasket(String VALUE) {
        super.put(basket, VALUE);
    }
    
    
}
