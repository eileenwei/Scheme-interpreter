package frontend;
import intermediate.Node;                 
import intermediate.NodeType;
import intermediate.SymbolTable;  
import intermediate.SymbolTableElement;      
import intermediate.SymbolTableElementType;
import backend.TreePrinter;            
import java.util.ArrayList;


public class Parser
{
    private Scanner scanner;
    private ArrayList<Node> tree;
    private SymbolTable mainTable;  
	private ArrayList<SymbolTable> stack = new ArrayList<>();
	private int counter;
	private int DELAY = 0;
	private int DELAY_PRINT = 0;
	
    public Parser(Scanner scanner)
    {
        this.scanner = scanner;
        this.tree = new ArrayList<Node>();
		this.counter = -1;
    }
  
	public void parsing()
    {      
		Token token;
		SymbolTable existingPro = SymbolTable.schemeExistingProcedure();
		addToStack(existingPro);
	    mainTable = new SymbolTable(1);
	    addToStack(mainTable);
		
		while ((token = scanner.nextToken()).getType() != TokenType.EOF) 
		{  
            TokenType tokenType = token.getType();
            if (tokenType == TokenType.SS_LPAREN) 
			{
                Node node = parseList(1);				
                tree.add(node);							
				traverseTree(node, 1);				
            }
        }		
		existingPro.showSymbolTable();
		mainTable.showSymbolTable();		
        removeFromStack();	
		printTree(tree);
    }
	public void mySleep(int delay)
	{
		System.out.println("Go to sleep ===========\n");
		try
		{
                Thread.sleep(delay);
        } 
        catch (InterruptedException exception) 
		{
           ;
        }
	}
	public ParseInfoObject getParsedInfo()  
	{
		ParseInfoObject pObject = new ParseInfoObject(this.mainTable, tree);
		return pObject;	
	}

	public void traverseTree(Node tree, int level)  
    {
        while (tree != null) 
		{ 
            
			NodeType nt = tree.getType();
			
			if (nt != NodeType.ELEMENT) 
			{
                Node car = tree.car();
                if (car != null) 
				{
				nt = car.getType();	
				if (nt == NodeType.ELEMENT) 
				{
					Token tk = car.getToken();
					TokenType type = tk.getType();
                    if (type == TokenType.KW_DEFINE) 
					{
						int t_level;
						Node cdar = tree.cdr().car();
						Node cddar = tree.cdr().cdr().car();
						Node cddarcdar = null;
						Node cddarcddr = null;
						Token token = cdar.getToken();
						SymbolTable st = null;
						
						if(cddar.getType() == NodeType.ELEMENT) 
						{
							SymbolTableElementType elementType = SymbolTableElementType.VARIABLE;
							SymbolTableElement sElement = mainTable.insert(token.getText(), elementType);
							cdar.setSymbolTableElement(sElement);
							sElement.setNode(cddar);
							if (elementType == SymbolTableElementType.PROCEDURE)
							{
								t_level = level + 1;
								st = new SymbolTable(t_level);
								addToStack(st);
								cddar.setSymtab(st);
								cddarcdar = cddar.cdr().car();
								insertNode2SymTable(cddarcdar, st);
								cddarcddr = cddar.cdr().cdr();
								if (cddarcddr != null)
								{
									traverseTree(cddarcddr, t_level);
								} 
								removeFromStack();
							}
							
							st.showSymbolTable(); 
						}
						else 
						{
							SymbolTableElementType elementType = SymbolTableElementType.PROCEDURE;
							SymbolTableElement sElement = mainTable.insert(token.getText(), elementType);
							cdar.setSymbolTableElement(sElement);
							sElement.setNode(cddar);
							if (elementType == SymbolTableElementType.PROCEDURE)
							{
								t_level = level + 1;
								st = new SymbolTable(t_level);
								addToStack(st);
								cddar.setSymtab(st);
								cddarcdar = cddar.cdr().car();
								insertNode2SymTable(cddarcdar, st);
								cddarcddr = cddar.cdr().cdr();
								if (cddarcddr != null)
								{
									traverseTree(cddarcddr, t_level);
								} 
								removeFromStack();
							}
							
							st.showSymbolTable(); 
						}
                        return;
                    }
					else if(type != TokenType.NUMBER)
					{
						searchSymbolNode(car);
					} 
                }
                else 
				{
					traverseTree(car, level);
                }
                }
            }
			else if (nt == NodeType.ELEMENT) 
			{
				searchSymbolNode(tree);
            }
            
            tree = tree.cdr();  //cdr of the tree
        }//end while loop
    }
		
	private SymbolTableElement searchSymbolNode(Node symNode)  
    {
		Token tk = symNode.getToken();
		String t = tk.getText();
		SymbolTableElement sElement;
		sElement = null;		
		for (int i = counter; i >= 0; i--) 
		{
			sElement = stack.get(i).searchEntry(t);
			if(sElement != null)
			{
				symNode.setSymbolTableElement(sElement);
				i=-1;
			}
        } 		
		return sElement;
    }
    public void insertNode2SymTable(Node tree, SymbolTable st)
	{
		for (Node node = tree; node != null; node = node.cdr()) 
		{
            String name = node.car().getToken().getText();
			st.insert(name, SymbolTableElementType.VARIABLE);
        }
	}
    private Node parseList(int level)      
    {
        Node root = new Node();
        Node currentNode = null;
        Token token = scanner.nextToken(); 
        TokenType tokenType = token.getType();
        
        while (tokenType != TokenType.SS_RPAREN) 
		{  
            if (currentNode == null) 
			{
                currentNode = root;
            }
            else 
			{
                Node newNode = new Node();
                currentNode.setCdr(newNode);
                currentNode = newNode;
            }          
            if (tokenType == TokenType.SS_LPAREN) 
			{
                currentNode.setCar(parseList(level+1));         
            }
            else 
			{
				Node nodeTemp = new Node(token);
				currentNode.setCar(nodeTemp);
            }           
            token = scanner.nextToken();   
            tokenType = token.getType();
        }
        return root;  
    }
	
    public void addToStack(SymbolTable st) 
    {
        counter++;
        stack.add(st);
    }	
	public SymbolTable removeFromStack() 
    {
        SymbolTable temp;
		temp = stack.remove(counter--);
		return temp;
    }

	public void printTree(ArrayList<Node> tree)
	{
		for (Node node : tree) 
		{
			TreePrinter.print(node);            
		}
	}
}