package frontend;
import intermediate.Node;
import java.util.ArrayList;
import intermediate.SymbolTable;
import intermediate.Node;                 
import intermediate.NodeType;
import intermediate.SymbolTable;  
import intermediate.SymbolTableElement;      
import intermediate.SymbolTableElementType;        


public class ParseInfoObject
{
    private ArrayList<Node> tree;
	private SymbolTable mainTable;
    
    public ParseInfoObject(SymbolTable mTable, ArrayList<Node> t)
    {
        mainTable = mTable;
        tree = t;
		//System.out.println("Been here ParseInfoObject ...");
    }
    public void setMainTable(SymbolTable mTable)
	{
		mainTable = mTable;
	}
	public SymbolTable getmainTable() 
	{ 
		return mainTable; 
	}
	public void setTree(ArrayList<Node> t)
	{
		tree = t;
	}
    public ArrayList<Node> gettree()      
	{ 
		return tree; 
	}
}
