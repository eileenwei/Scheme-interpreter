package backend;

import java.util.ArrayList;

import frontend.*;
import intermediate.*;

//contains only procedure tree and symbol table
public class Interpreter{
    private ArrayList<Node> tree;       
    private SymbolTable mainTable; 
    Stack runtimeStack;
    
    public Interpreter(ParseInfoObject pInfoObject){  //Initializing variables
        this.tree = pInfoObject.gettree();
        this.mainTable = pInfoObject.getmainTable();
        runtimeStack = new Stack();
        ActivationRecord maintable = new ActivationRecord(mainTable);
        runtimeStack.push(maintable);
	}
    
    public void interprete(){
        System.out.println("\nInterprete Result:\n");
        for (Node root : tree) {
        	//System.out.println("\n");
            Node car = root.car(); // first item in the top level list 
            Token token = car.getToken();             
            
            if (token.getType() == TokenType.KW_DEFINE) { // if the first item is define
            //	//System.out.println("#42  "+"here's define: "+ token.getText());
                Node cddr  = root.cdr().cdr(); // (define  
                Node cddar = cddr.car(); 
                if (cddr.car() != null) {
                	if (cddar.getType() != NodeType.ELEMENT) {// The value is not an element.  //(define x 2)
                		Node cddaar = cddar.car(); //cdda -> (lambda (a b (+ a b)))
                        if (cddaar != null) {
                            token = cddaar.getToken();
                            if (token.getType() != TokenType.KW_LAMBDA) { //if it is not lambda, just proceed with the list
                                Object value = intepreteDefine(root);
                                printResult(value);
                            }
                        }
                    }else { //if it is an element
                    	Object value = intepreteDefine(root);//return 2
                        printResult(value);
                    }
                }
            }else {// if the item is not define -> process the rest of the list 
                Object value = evaluation(root);
                ////System.out.println("final result: "+value);
                printResult(value);
            }
        }
    }
    
    
    public Object evaluation(Node root){//condition node point to (null? lst)
    	//System.out.println("#77  interpreteEval");
        NodeType nodeType = root.getType(); //cond ....
	        if (nodeType == NodeType.ELEMENT) {
	        	Object o = intepreteElement(root);
	        	if(o ==  null){
	        		return null;
	        	}
	            return o;
	        }else if (nodeType == NodeType.LISTELEMENT) {
	            return quoteList(root);
	        }else { //it is a list
	          //root point to the cond list-> (cond 
	            //System.out.println("#113  ---interpreteList---");
	    	    Node car = root.car(); //cond
	    	    Token token = car.getToken();
	    	    //System.out.println("#116  tokenType= "+token.getType());
	    	    
	    	    if(token.getType() == TokenType.KW_COND){
	    	    	//System.out.println("#116  Cond");
	    	    	return intepreteCond(root);
	    	    }else if(token.getType() == TokenType.KW_AND){
	    	    	//System.out.println("#179  AND");
	    	    	return intepreteAnd(root);
	    	    }else if ((token.getType() == TokenType.KW_LET_STAR) || (token.getType() == TokenType.KW_LET)){
	    		    	//System.out.println("#120  Let*/Let");
	    		     // Create an activation record for this scope
	    		        SymbolTable letSymtab = root.getSymtab();
	    		        ActivationRecord record = new ActivationRecord(letSymtab);
	    		        runtimeStack.push(record);

	    		        // Evaluate the variable
	    		        Node varList = root.cdr().car();
	    		        for (Node v = varList; v != null; v = v.cdr()) {
	    		            Node pair = v.car();
	    		            String name = pair.car().getToken().getText();
	    		            Object value = evaluation(pair.cdr().car());
	    		            record.setValue(name, value);
	    		        }
	    		        
	    		        
	    		        Node body = root.cdr().cdr().car();// Evaluate let 
	    		        Object value = evaluation(body);
	    		        runtimeStack.pop(); //remove activation record
	    		        return value;
	    		        
	    		        
	    		        
	    	    }else {// interprete a procedure call.//null? and equal etc procedure call
	    			//System.out.println("#122  It's a procedure call!***");
	    			SymbolTableElement procElement = car.getSymbolTableElement();
	    			//System.out.println("#127  procElement= "+procElement);
	    			SymbolTableElementType procType = procElement.getType();
	    			
	    	        Token procToken = car.getToken();
	    	        //System.out.println("#127  procToken= "+procToken.getType());
	    	        //System.out.println("   proc--> "+procToken.getText());
	    	        // Create an array list of actual argument values.
	    	        int nm=0;
	    	        ArrayList<Object> args = new ArrayList<>();
	    	        for (Node n = root.cdr(); n != null; n = n.cdr()) { //(x y)
	    	        	//System.out.println("next parameter");
	    	            car = n.car();
	    	            nm++;
	    	            if(car !=null ){
	    	            	//System.out.println("#"+nm+"    typeeeeeee=> "+car.getType());
	    	            }
	    	           // //System.out.println("type=> "+car.getToken().getType());
	    	           if(car.getType() == NodeType.ELEMENT){
	    	        	   if(car.getToken().getType() == TokenType.SS_QUOTE){
	    		        	   //System.out.println("quote list");
	    		        	   n = n.cdr();
	    		        	   car = n.car();
	    		        	   //System.out.println("#"+nm+" before    typeeeeeee=> "+car.getType());
	    		        	  // car.getToken().setType(TokenType.QUOTELIST);
	    		        	   car.setType(NodeType.LISTELEMENT);
	    		        	   //System.out.println("#"+nm+" after    typeeeeeee=> "+car.getType());
	    		        	   if(car.getToken()!=null){
	    			        	   //System.out.println("eeeeeeeeeeee   "+car.getToken().getType()+"\n\n");
	    			        	   args.add(evaluation(car));
	    		        	   }else{		   
	    		        		   //System.out.println("else");
	    		        		   //System.out.println(car.getType());
	    		        		//   //System.out.println("eeeeeeeeeeee   "+car.getToken().getType()+"\n\n");
	    			        	   args.add(evaluation(car));
	    		        	   }
	    		           }else{
	    		        	   Object o  = evaluation(car);
	    		        	   if(o != null){
	    		        		  
	    		        		   args.add(evaluation(car));
	    		        	   }else{
	    		        		   
	    		        	   }
	    		        	   
	    		           }
	    	        	  
	    	           }else{
	    	        	  
	    	        	   args.add(evaluation(car));
	    	           }  
	    	           
	    	        }
	    	        //System.out.println("end of reading PARAMETER.");
	    	       
	    			if (procType == SymbolTableElementType.SCHEMEPROC) { // Scheme procedure
	    				//System.out.println("#135  schemeProc");
	    	            return schemeProc(procToken, args);
	    	        }else { //defined procedure.
	    	        	//System.out.println("#138  interpreteCall   "+root);
	     
	    	    		SymbolTableElement sElement = root.car().getSymbolTableElement();
	    	            Node lambdaRoot = sElement.getNode();
	    	    		 
	    	            //System.out.println("#176  intepreteLambda "); 
	    	            SymbolTable lambdaSymtab = lambdaRoot.getSymtab();
	    	            ActivationRecord lambdaActRecord = new ActivationRecord(lambdaSymtab); // Create the activation record for the lambda's scope.
	    	            
	    	            Node callPara = root.cdr();          // root of the actual parms (x y)
	    	            Node definePara = lambdaRoot.cdr().car();  // root of the formal parms ( (a b) let ....)
	    	            Node  f2 = new Node();
	    	            //System.out.println("Evaluate the parameters");
	    	           for (Node call = callPara, define = definePara; call != null; call = call.cdr(), define = define.cdr()) {// Evaluate the parameters
	    	        	   f2 = define;
	    	            	  if(call.car().getType() == NodeType.ELEMENT && call.car().getToken().getType() == TokenType.SS_QUOTE){
	    	            		   String name = define.car().getToken().getText();
//	    	            		   //System.out.println("name= "+name);
//	    	            		   //System.out.println("%%%%%%%%%%%%%");
	    	            		   call = call.cdr();	   
//	    	            		   //System.out.println(call.car().getType());
	    	                       Object value = evaluation(call.car());
	    	                       lambdaActRecord.setValue(name, value);
	    	            	 }else{
	    	            		String name = define.car().getToken().getText();
	    	            		//System.out.println("name= "+name);
	    	                     Object value = evaluation(call.car());
	    	                     lambdaActRecord.setValue(name, value);
	    	            	 }
	    	            	   
	    	            }  //a-> x  b->y
	    	      /*     if(f2!=null){   //throw error
	    	        	   //System.out.println("# of parameter!");
	    	        	   try {
	    	                   throw new MyException("incorrect number of parameters!");
	    	                 } catch (MyException e) {
	    	                   e.printStackTrace(); 
	    	                   System.exit(0);
	    	                 }
	    	           }*/
	    	           
	    	            runtimeStack.push(lambdaActRecord); //push the activation record on to the stack
	    	            // Evaluate the lambda expression's body.  lambdaRoot.cdr() => ((item lst) (cond...) )
	    	            Node body = lambdaRoot.cdr().cdr().car(); //(cond ....
	    	            //System.out.println("...................Evaluate the lambda expression's body...............");
	    	            Object value = evaluation(body);
	    	            //System.out.println(value);
	    	            //System.out.println("...................end of lambda expression's body...............");
	    	            runtimeStack.pop(); //remove the activation record from the stack

	    	            return value;   
	    	        }
	    	    }
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            
	        }
	        
    }
    public Object quoteList(Node root){
   // 	//System.out.println("\n\n___________quote list->");
    	if(root.getToken()!=null && root.getToken().getType() == TokenType.IDENTIFIER){
    		//System.out.println("variable ===== "+root.getToken().getText());
    		//System.out.println("\n\n");
			return root.getToken();
    	}else{
    //		//System.out.println("parameter list ");
    		//	//System.out.println(root.car().getToken().getText()+" ...");//a
    		//	//System.out.println(root.cdr().car().car().getToken().getText()+" ");//b
    		//	//System.out.println(root.cdr().car().cdr().car().getToken().getText()+" "); //c
    		//	//System.out.println(root.cdr().cdr().car().getToken().getText()+" "); //d
    		//root = root.cdr();
        	/*for(Node n = root; n != null; n = n.cdr()){
        		//System.out.print(n.car().getToken().getValue()+" ");  	
        	}*/
  //      	//System.out.println("\n\n");
        	return root;
    	}

    	
    }
	public Object intepreteElement(Node element)
	{
		//System.out.println("method-------->> evalElement");
		if(element !=null){
		    Token token = element.getToken();
		    TokenType tokenType = token.getType();    
		    if (tokenType == TokenType.NUMBER) {
		        return token.getValue();
		    }else if (tokenType == TokenType.IDENTIFIER) {
				SymbolTableElement sElement = element.getSymbolTableElement();
				if(sElement == null){
					return null;
				}
				if(runtimeStack.getValue(sElement)==null){
					//System.out.println("runtimeStack.getValue(sElement) is null");
					return null;
				}
				return runtimeStack.getValue(sElement);
		    }else if(tokenType == TokenType.FALSE){
		    	//System.out.println("boolean "+tokenType);
		    	return token.getText();	
		    }else if(tokenType == TokenType.TRUE){
		    	//System.out.println("boolean "+tokenType);
			    return token.getText();	
		    }else if(tokenType == TokenType.KW_ELSE){
		    	//System.out.println("ELSE "+tokenType);
		        return null;
		    }else {
		    	//System.out.println("other? "+tokenType);
		        return null;
		    }
		}else{
			//System.out.println("Element is null");
			return null;
		}
	}

	public Object intepreteAnd(Node andRoot){
		//System.out.println("____________________________interprete And");
		 Node car = andRoot.car(); //and 
		 Token procToken = car.getToken();
	     ArrayList<Object> args = new ArrayList<>(); //(and (x) (y))
	        for (Node n = andRoot.cdr(); n != null; n = n.cdr()) { //(x y)
	        	//System.out.println("next AND parameter");
	            car = n.car();
	            Object result = evaluation(car);
	            //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~And Result  = "+result.toString());
	            args.add(result);
	        }
	        //System.out.println("end of reading And PARAMETER!!!");
		return schemeProc(procToken, args);
	}


	public Object intepreteCond(Node condRoot){ //(cond ((null? lst) '() (and...)  (else...))))
		//System.out.println("\n#141  interpreteCond: ");
		int statementNumber = 0;
		Node car,condition,returnVal;
        for (Node n = condRoot.cdr(); n != null; n = n.cdr()) { 
        	//System.out.println("\n\nCond statement #"+ (++statementNumber));
            car = n.car(); //get the condition list
            condition = car.car(); //condition expression of the statement
            //System.out.println("Evuating the condition expression of statement #"+ (statementNumber)+"...");
            Object value = evaluation(condition);  //condition node point to (null? lst)
            returnVal = car.cdr().car();//return value
            //System.out.println("condition result: "+value);
            if(value!=null && value.equals(true)){
            //	//System.out.println("return->" + returnVal.getToken().getText());
            	return evaluation(returnVal);
            }else if(condition.getToken()!= null && condition.getToken().getType() == TokenType.KW_ELSE){
            	//System.out.println("Cond statement #"+statementNumber);
            	//System.out.println("ELSE!");
             // Evaluate the else expression's body.
            	return evaluation(returnVal);
            }
        }
		
		return null;
	}
	public Object intepreteDefine(Node defineRoot){
		//System.out.println("#141  interpreteDefine: ");
	    Node cdar = defineRoot.cdr().car(); //x
	    SymbolTableElement sElement = cdar.getSymbolTableElement();
		
	    Node cddar = defineRoot.cdr().cdr().car(); //2
	    Token token = cddar.getToken();
	    Number value = token.getValue();
	    //System.out.println("#150  value= "+value.intValue());
		return runtimeStack.setValue(sElement, value);
	}

   
    
	//print out the interpret result
	public void printResult(Object value){
        if (value instanceof Node) {  // if it is a node, use treeprinter to print the result
            printNode((Node)value,0);
        	System.out.println();
        }else{   //if it is not a node, just print out the result
        	System.out.println(value);
        }
    }
    
	//modified Tree Printer from Assignment 5
	private void printNode(Node root, int nestingLevel){
		System.out.print("(");
		Node currentNode = root;		// Start with the tree root.
		// Loop and print until we fall off the bottom of the tree.
		while (currentNode != null) {
			Node car = currentNode.car();
			if (car != null) {
                NodeType type = car.getType();
                if (type == NodeType.LIST) {
                   
                    printNode(car, nestingLevel+1);
                } else {
                    Token token = car.getToken();
                    if (token != null) System.out.print(token.getText());
                }
            }
			// Use the cdr link to move down the tree.
			currentNode = currentNode.cdr();
			if (currentNode != null) System.out.print(" ");
		}
		System.out.print(")");
	}
    
    
    //this is a method that handles a sets of scheme procedures
    public Object schemeProc(Token procedure, ArrayList<Object> parameters){
        String proc = procedure.getText();
        if(proc.equals("cons")){
        	//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>  cons      <<<<<<<<<<<<<<<<<<<<<<<<<<<");
        	//(cons (car lst) (remove-last item (cdr lst)))
        	//System.out.println("args.size()= "+parameters.size());
        	//System.out.println("object class = "+(parameters.get(0).getClass()));
        //	((Node)args.get(0)).setCdr(((Node)args.get(1)));
        //	for (Object arg : args) 
		//	{
        		Node newList = new Node();
        		newList.setType(NodeType.LIST);	
        		newList.setCar(((Node)parameters.get(0)));
        		newList.setCdr(((Node)parameters.get(1)));
        	//	//System.out.println("object class = "+((Node)args.get(0)).getToken().getText());
        	//	//System.out.println("object class = "+((Node)args.get(1)).car().getToken().getText());
		//	}
        	return newList;
        }else if(proc.equals("null?")){
        	//System.out.println("null?");
        	boolean ifNull = false;
        	if(parameters.size() == 0){
        		return true;
        	}
        	for (Object par : parameters) 
			{
        		//System.out.println("args.size()= "+parameters.size());
        		if(par instanceof Node){
        			if (((Node)par).car()==null)
        				ifNull = true;
        		}
            }
        	//System.out.println("list is null? = "+ifNull);
        	return ifNull;
        	
        }else if(proc.equals("and")){
        	//System.out.println("and");
        	boolean result = true;
        	for (Object par : parameters) {
        		result= (result && (Boolean)par);
        		
        	}
        	return result;
        }else if(proc.equals("equal?")){
        	//System.out.println("equal?");
        	boolean ifEqual = false;
         	//System.out.println("object class = "+(parameters.get(0).getClass()));
         	Object o1 = (Object) parameters.get(0);
         	if(o1 instanceof Number){
         		//System.out.println("object class is an instance of number");
	         	if(((Node)parameters.get(1)).getToken()!=null){
	         		Object o2 = (Object) ((Node)parameters.get(1)).getToken().getValue();
		         	if(o1.getClass().equals(o2.getClass())){         		
			        	//System.out.println("# -> "+parameters.size()+"    "+((Float)parameters.get(0))+", "+((Node)parameters.get(1)).getToken().getValue());  
			        	Float f1 = (Float) parameters.get(0);
			        	Float f2 = (Float) ((Node)parameters.get(1)).getToken().getValue();
			        	if(f1.equals(f2)){
			        		ifEqual = true;
			        	}
	         		}else{
	         			//System.out.println("return equal? = false");
	         			return false;
	         		}
	         	}else{
	         		//System.out.println("return equal? = false");
	         		return false;
	         	}
         	}else if(o1 instanceof Node){
         		//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa   object class is an instance of Node");
         		//System.out.println("# -> "+parameters.size());
         		//System.out.println(((Node)parameters.get(0)).getType());
         		//System.out.println(((Node)parameters.get(1)).getType());
         		Node n1 = ((Node)parameters.get(0));
         		Node n2= ((Node)parameters.get(1));
         		NodeType t1 = ((Node)parameters.get(0)).getType();
         		NodeType t2= ((Node)parameters.get(1)).getType();
         		
         		if((t1.equals(NodeType.LISTELEMENT) && t2.equals(NodeType.LIST))){
         			//System.out.println("******************************************************************"); 
         			////System.out.println(n1.getToken().getValue());
         			Node fn2 = new Node();
          	       	for (Node a = n1, f = n2; a != null; a = a.cdr(), f = f.cdr()) {
          	       		fn2 = f;
	          	    	  //System.out.println("a.car().getToken()= "+a.car().getToken().getType());
	          	    	  //System.out.println("a.car().getToken()= "+f.car().getToken().getType());
	          	    	  if((a.car().getToken().getType()).equals(f.car().getToken().getType())){
	          	    		  if(a.car().getToken().getType() == TokenType.IDENTIFIER){
	          	    			  if(a.car().getToken().getText().equals(f.car().getToken().getText())){
	          	    				//System.out.println("a.car().getToken().getText()= "+a.car().getToken().getText());
	          	    				//System.out.println("f.car().getToken().getText()= "+f.car().getToken().getText());
	          	    				  ifEqual = true;
	          	    			  }else{
	          	    				return false;
	          	    			  }
	          	    		  }else if(a.car().getToken().getType() == TokenType.NUMBER){
	          	    			  
	          	    		  } 
	          	    	  }else{
	          	    		  return false;
	          	    	  }
          	    	 
          	      }
          	      if(fn2.cdr()==null && ifEqual == true){
          	    	  //System.out.println("f2 is null.");
        	       		return true;
        	      }
          	       	
         		}
         		return false;
         	}else {
         		//System.out.println("object class is an instance of Symbol");
         		//System.out.println("# -> "+parameters.size());
         		
         		//System.out.println(((Token)parameters.get(0)).getType());
         		String s1 = ((Token)parameters.get(0)).getText();
         		
         		if(((Node)parameters.get(1)).getToken()==null){
         			//System.out.println("return equal? = null");
         			return null;
         		}else{
         			String s2 = (String) ((Node)parameters.get(1)).getToken().getText();
         			//System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+s1+", "+s2);
	         		 if(s1.equals(s2)){
	         			//System.out.println("return equal? = true");
	         			 return true;
	         		 }
	        	}
         		//System.out.println("return equal? = false");
         		return false;
         	}
        	//System.out.println("return = "+ifEqual);
        	
        	return ifEqual;
        }else if(proc.equals("car")){
        	//System.out.println("car");
        	for (Object arg : parameters) {
        		//System.out.println("object class = "+(arg.getClass()));
        		if(arg instanceof Node){
        			//System.out.println("object class is Node");
        		//	//System.out.println("return Car = "+((Node)arg).car().getToken().getValue());
        			return ((Node)arg).car();
        		}
        		
			}
        }else if(proc.equals("cdr")){
        	//System.out.println("cdr:");
        	for (Object arg : parameters) {
        		//System.out.println("object class = "+(arg));
        		if(arg instanceof Node){
        		//	//System.out.println("return Cdr:" +((Node)arg).car().getToken().getValue());
        			//System.out.println("return Cdr:" +((Node)arg).cdr());
        			if(((Node)arg).cdr()  == null)
        				return null;
        			return ((Node)arg).cdr();
        		}
        		
			}
        	
        }else if(proc.equals("not")){
        	//System.out.println("not");
        	if(parameters.size()>0){
        		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   args.size()>0= "+parameters.size());
        		for (Object arg : parameters) {
        			//System.out.println("arg is class -> "+arg.getClass());
        			//System.out.println("before not---------------------> "+(arg));
        			if(arg.equals("#t")){
        				return false;
        				
        			}else{
        				return true;
        			}
        			
        		}
        	}
        	return false; 
        }else if(proc.equals("symbol?")){
        	//System.out.println("symbol?");
        	if(((Node)parameters.get(0)).getToken().getType() == TokenType.SYMBOL)
        		return true;
        	return false;
        }else if(proc.equals("integer?")){
        	//System.out.println("integer?");
        }else if(proc.equals("real?")){
        	//System.out.println("real?");
        }else if(proc.equals("boolean?")){
        	//System.out.println("boolean?");
        	if(((Node)parameters.get(0)).getToken().getType() == TokenType.FALSE ||((Node)parameters.get(0)).getToken().getType() == TokenType.TRUE )
        		return true;
        	return false;
        }else if(proc.equals("char?")){
        	//System.out.println("char?");
        }else if(proc.equals("string?")){
        	//System.out.println("string?");
        	if(((Node)parameters.get(0)).getToken().getText() != null)
        		return true;
        	return false;
        }else if(proc.equals("pair?")){
        	//System.out.println("pair?");
        	//if there's at least a parameter in the list '()
        	if(parameters.size()>0){
        		if(parameters.get(0) instanceof Node)
        			if(((Node)parameters.get(0)).getType() == NodeType.LISTELEMENT && ((Node)parameters.get(0)).car()!=null)
        				return true;
        	}
        	return false;
        }else if (proc.equals("+")) {
            int result = 0;
            for (Object arg : parameters) {
            	result += (Integer) arg;
            }
            return result;
        } else if (proc.equals("*")) {
            int result = 1;
            for (Object arg : parameters){
            	result *= (Integer) arg;
            }
            return result;
        }
        return null;
    }
    
    
   
}
