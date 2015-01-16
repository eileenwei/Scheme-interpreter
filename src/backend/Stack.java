package backend;
import intermediate.*;

import java.util.ArrayList;

/**Runtime stack*/
public class Stack {
    private ArrayList<ActivationRecord> stack;
    private ArrayList<ActivationRecord> stackinfo; //tracks the level
    private int index = -1;
    
    public Stack (){
    	//initialize variables
        stack   = new ArrayList<>();
        stackinfo = new ArrayList<>();
        stackinfo.add(null);
    }
    //stack operations
    public void push(ActivationRecord record){ //push record onto the stack
        ++index;
        stack.add(record);
        if (stackinfo.size() <= record.getLevel()) {
            stackinfo.add(record);
        }else {
            record.setLink(stackinfo.get(record.getLevel()));
            stackinfo.set(record.getLevel(), record);
        }
    }
    
    public ActivationRecord pop(){ //pop the record from stack
        ActivationRecord record = stack.get(index);
        stackinfo.set(record.getLevel(), record.getLink());
        return stack.remove(index--);
    }
    
	public Object getValue(SymbolTableElement element){//get stack value
        ActivationRecord record = stackinfo.get(element.getSymtab().getLevel());
        return record.getValue(element.getName());
    }
    
	public Object setValue(SymbolTableElement element, Object value){//set stack value
        ActivationRecord record = stackinfo.get(element.getSymtab().getLevel());
        record.setValue(element.getName(), value);   
        return value;
    }
}
