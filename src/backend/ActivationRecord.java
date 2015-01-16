package backend;

import intermediate.*;
import java.util.Map;
import java.util.TreeMap;

/**This is the runtime activation record class.*/
public class ActivationRecord 
{
                          		
    private TreeMap<String, Object> localMem = null;  // local memory of the procedure call
    private ActivationRecord prev = null;          		    
    private int level;
    
    public ActivationRecord(SymbolTable table){
    	//initialize variables
        this.level = table.getLevel();
        this.prev  = null;
        this.localMem = new TreeMap<>();

        Map<String, SymbolTableElement> map = new TreeMap<String, SymbolTableElement>();
        for(Map.Entry<String, SymbolTableElement> entry : map.entrySet()) {
        	  String key = entry.getKey();
        	// SymbolTableElement value = entry.getValue();
        	  // create entry for variables
  			if (entry.getValue().getType() == SymbolTableElementType.VARIABLE) 
  				 localMem.put(key, null);
        //	  System.out.println(key + " => " + value);
        	}   
        
    }
    
    public int getLevel() { 
		return this.level; 
	}
    public ActivationRecord getLink()  { 
		return this.prev; 
	}

    public Object getValue(String val){
        return localMem.get(val);
    }
    public void setLink(ActivationRecord record) { 
		this.prev = record; 
	}

    public void setValue(String key, Object value){
        localMem.put(key, value);
    }
}
