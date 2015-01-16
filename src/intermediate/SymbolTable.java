package intermediate;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import frontend.ParseInfoObject;
import frontend.Token;
import frontend.TokenType;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

public class SymbolTable 
{
    private int level;                      
	private TreeMap<String, SymbolTableElement> nodes;
    private Iterator<Map.Entry<String, SymbolTableElement>> iter;
	private Map.Entry<String, SymbolTableElement> element;
	private Set<Map.Entry<String, SymbolTableElement>> elements;
	
    public SymbolTable(int levelParam)
    {
        level = levelParam;
		nodes = new TreeMap<>();
    }
	public void showSymbolTableInfo()
	{
		System.out.println("SymbolTable level:  " + level +"\n");
		if(nodes == null)
		{
			System.out.println("SymbolTable nodes is null\n");
		}
		else
		{
			System.out.println("SymbolTable nodes NOT null\n");
		}
		
	}
    public void setLevel(int levelParam)
	{
		level = levelParam;
	}
    public int getLevel() 
	{ 
		return level; 
	}
    public SymbolTableElement insert(String sym, SymbolTableElementType type)
    {

        SymbolTableElement element = nodes.get(sym);
        if (element == null) 
		{
            element = new SymbolTableElement(sym, type, this);
			nodes.put(sym, element);
            return element;
        }
        else 
		{
            return element;
        }
    }

	public void showSymbolTable()
    {
        System.out.println("\n");
		System.out.println("  Symbol Table===========");
		System.out.println("  Level " + level + " ------ \n" );
		iter = iterator();
        while (iter.hasNext()) 
		{
			element = iter.next();
			System.out.println("  " + element.getKey() + " | " + element.getValue().getType() + "");
        }
		System.out.println("==========================\n");
    }
	public SymbolTableElement searchEntry(String sym)
	{
		SymbolTableElement en;
		en = nodes.get(sym);
		return en;
	}
	public static SymbolTable schemeExistingProcedure()
    {
        SymbolTable schemeExistingProc = new SymbolTable(0);
		schemeExistingProc.insert("cons", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("null?", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("equal?", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("not", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("car", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("cdr", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("list", SymbolTableElementType.SCHEMEPROC);
        schemeExistingProc.insert("+", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("-", SymbolTableElementType.SCHEMEPROC);
        schemeExistingProc.insert("*", SymbolTableElementType.SCHEMEPROC); 
		schemeExistingProc.insert("/", SymbolTableElementType.SCHEMEPROC);		
		schemeExistingProc.insert("pairs? ", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("symbol?", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("integer?", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("real?", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("char?", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("boolean?", SymbolTableElementType.SCHEMEPROC);
		schemeExistingProc.insert("string?", SymbolTableElementType.SCHEMEPROC);
		
        return schemeExistingProc;
    }
    public Iterator<Map.Entry<String, SymbolTableElement>> iterator()
    {
		elements = nodes.entrySet();
		iter = elements.iterator();		
		return iter;
    }
}