package com.example.root.ptsterminal;

public class defines {
  
  public static final int RESULT_TIMEOUT = 10;
  public static final int RESULT_ERROR = 11;
  public static final int RESULT_CUSTOM1 = 12;
  public static final int RESULT_PROTOCOL_ERROR = 13;
  

  public static final int UDT_HEADER_SIZE = 21;
  public static final int PTSMaster_HEADER_SIZE = 22;


  // --- DISPENSER_STATUS
  public static final int DS_UNDEFINED = 0;
  public static final int DS_OFFLINE = 1;
  public static final int DS_IDLE = 2;
  public static final int DS_CALL_DEPRECATED = 3;
  public static final int DS_WORK = 4;
  public static final int DS_AUTH = 5;
  public static final int DS_BUSY = 6;
  public static final int DS_LOCK = 7;

  // --- ORDER_STATUS
  public static final int OS_IDLE = 0;
  public static final int OS_INPROGRESS = 1;
  
  // --- DISPENSER_ORDER_MODE
  public static final int OM_AUTO = 0;
  public static final int OM_VOLUME = 1;
  public static final int OM_AMOUNT = 2;
  public static final int OM_FULL_TANK = 3;
  
//  public static final int MSG_ADDR_SIZE = 24;
//  public static final int MSG_FN_SIZE = 24;
//  public static final int MSG_PORT = 29999;
//
//  // --- Types of data bus messages (protocol PTSMaster)
//  public static final int REGISTER_MSG = 1;
//  public static final int ERROR_MSG = 2;
//  public static final int REQUEST_MSG = 3;
//  public static final int RESPONSE_MSG = 4;
//  public static final int SIGNAL_MSG = 5;
  
}
