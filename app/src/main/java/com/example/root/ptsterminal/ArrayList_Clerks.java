package com.example.root.ptsterminal;

import java.util.HashMap;

public class ArrayList_Clerks extends HashMap<String, String> {
//    private static final long serialVersionUID = 1L;
    public static final String index =			"index";			//--Clerk index
    public static final String name =			"name";				//--Clerk name
    public static final String permissions =	"permissions";		//--Clerk permissions
    public static final String password =		"password";			//--Clerk password
   
    public ArrayList_Clerks(
    		String INDEX, 
    		String NAME,
    		String PERMISSIONS,
    		String PASSWORD 
  		) {
        super();
        super.put(index,		INDEX);
        super.put(name,			NAME);
        super.put(permissions,	PERMISSIONS);
        super.put(password,		PASSWORD);
    }   
    
    public ArrayList_Clerks() {
        super();
        super.put(index,		"");
        super.put(name,			"");
        super.put(permissions,	"");
        super.put(password,		"");
    }   

    // ------------------------------------------------------------

    public String getIndex() {
        return super.get(index);
    }

    public String getName() {
        return super.get(name);
    }

    public String getPermissions() {
        return super.get(permissions);
    }

    public String getPassword() {
        return super.get(password);
    }
    
    
    // ------------------------------------------------------------

    public void setIndex(String INDEX) {
        super.put(index, INDEX);
    }
    
    public void setName(String NAME) {
        super.put(name, NAME);
    }
    
    public void setPermissions(String PERMISSIONS) {
        super.put(permissions, PERMISSIONS);
    }
    
    public void setPassword(String PASSWORD) {
        super.put(password, PASSWORD);
    }
    
    
}
