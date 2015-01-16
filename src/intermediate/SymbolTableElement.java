package intermediate;
import intermediate.Node;                 
import intermediate.NodeType;
import intermediate.SymbolTable;       
import intermediate.SymbolTableElementType;
//new for a6
public class SymbolTableElement
{
    private String elementName;          
    private SymbolTableElementType elementType;   
	private SymbolTable symtab;    
    private Node node;             
    public SymbolTableElement(String n, SymbolTableElementType t, SymbolTable sym)
    {
        node = null;    
		symtab = sym;  
		elementName = n;
        elementType = t;           
                          
    }  
	public void showSymbolTableElementInfo()
	{
		System.out.println("\n\nThis is SymbolTableElementInfo =================");
		System.out.println("SymbolTableElement elementName:      " + elementName + "");
		System.out.println("SymbolTableElement elementType:      " + elementType + "\n");
		System.out.println();
	}
    public String getName()          
	{ 
		return elementName; 
	}
    public SymbolTableElementType getType() 
	{ 
		return elementType; 
	} 
	public SymbolTable getSymtab()   
	{ 
		return symtab; 
	}          
    public Node getNode()            
	{ 
		return node; 
	}          
    public void setSymtab(SymbolTable st) 
	{ 
		symtab = st; 
	}         
    public void setNode(Node n)            
	{ 
		node = n; 
	} 
	
}
