package intermediate;

import frontend.Token;

public class Node 
{
    private SymbolTable symtab = null;
	private SymbolTableElement sElement = null;
	private NodeType type = NodeType.LIST;  
    private Node car = null;                 
    private Node cdr = null;
	private Token token = null;             
   
	
	public Node(){}
	public void showNodeInfo(int level)
	{
		System.out.println("\n====================================");
		System.out.println("Begin show NODE INFO");
		System.out.println("Node in level:     " + level + "");
		if(symtab != null) symtab.showSymbolTableInfo();
		else System.out.println("symtab is         null");
		if(sElement != null) sElement.showSymbolTableElementInfo();
		else System.out.println("sElement is       null");
		if(token != null) token.showTokenInfo();
		else System.out.println("token is          null");
		System.out.println("Node type:        " + type + "");
		if(car == null)
		{
			System.out.println("Node.car is       null");
		}
		else 
		{
			System.out.println("Node.car          NOT null");
		}
		
		if( cdr == null)
		{
			System.out.println("Node.cdr is       null");
		}
		else 
		{
			System.out.println("Node.cdr          NOT null");
		}
		System.out.println("====================================\n\n\n");
	}
	public SymbolTableElement getSymbolTableElement() 
	{ 
		return sElement; 
	}   
    public Token getToken()       
	{ 
		return token; 
	}  
	public Node getCar()            
	{ 
		return car;  
	}
    public Node getCdr()            
	{ 
		return cdr;  
	}

    public void setType(NodeType type){
    	this.type = type;
    }
    public NodeType getType()        
	{ 
		return type; 
	}          
    public SymbolTable getSymtab()      
	{ 
		return symtab; 
	}           
	
	public void setSymtab(SymbolTable st)     
	{ 
		symtab = st; 
	}           
	public Node car()            
	{ 
		return car;  
	}
    public Node cdr()            
	{ 
		return cdr;  
	}
    public void setSymbolTableElement(SymbolTableElement element) 
	{ 
		sElement = element; 
	}
	public void setCar(Node left)                 
	{ 
		car = left;  
	}
    public void setCdr(Node right)                
	{ 
		cdr = right; 
	}
    public Node(Token token)          
    {
        this.token = token;   
        this.car  = null;
        this.cdr = null;
        this.type  = NodeType.ELEMENT;           
        this.symtab = null;          
		this.sElement = null;
    }
}
