package com.example.root.ptsterminal;

import java.util.HashMap;

public class HolderRequestItem extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    public static final String dataId = "dataId";         //--Number of data in the configuration
    public static final String dataHeader = "dataHeader";		//--Header data
    public static final String dataType = "dataType";		//--Data type
    public static final String data = "data";			//--Data
    
    public HolderRequestItem(String DATAID, String DATAHEADER, String DATATYPE, String DATA) {
        super();
        super.put(dataId, DATAID);
        super.put(dataHeader, DATAHEADER);
        super.put(dataType, DATATYPE);
        super.put(data, DATA);
    }   
    
    public String getId() {
        return super.get(dataId);
    }

    public String getHeader() {
        return super.get(dataHeader);
    }

    public String getType() {
        return super.get(dataType);
    }

    public String getData() {
        return super.get(data);
    }

    public void setId(String DATAID) {
        super.put(dataId, DATAID);
    }
    
    public void setHeader(String DATAHEADER) {
        super.put(dataHeader, DATAHEADER);
    }
    
    public void setType(String DATATYPE) {
        super.put(dataType, DATATYPE);
    }
    
    public void setData(String DATA) {
        super.put(data, DATA);
    }
    
}
