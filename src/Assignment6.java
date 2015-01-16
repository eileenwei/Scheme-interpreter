
import frontend.*;
import backend.*;
import intermediate.*;
/**
 * CS152 Spr2014
 * Team: Red Coders
 * Assignment 6 Scheme Interpreter
 **/

public class Assignment6{
    public static void main(String[] args) {
       
	  //  Source source = new Source("input.lisp");
    	 Source source = new Source(args[0]);
        Scanner scanner = new Scanner(source);
		//System.out.println("This line new myParser3(scanner) =%%%%%%%%%%%");
        Parser parser   = new Parser(scanner);
		//System.out.println("This line before parser.parsing() =%%%%%%%%%%%");
		parser.parsing();                                     
        ParseInfoObject pInfoObject = parser.getParsedInfo();  
        // Generate parser results 
        Interpreter interpreter = new Interpreter(pInfoObject);
        interpreter.interprete();
    }
}
