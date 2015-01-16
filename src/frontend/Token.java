package frontend;

public class Token 
{
    private TokenType type;
    private String text;
    private Number value;
    
    public Token(TokenType type) 
    { 
        this.type = type; 
        this.value = null;
    }
	
	public void showTokenInfo()
	{
		System.out.println("Token type:      " + this.type + "");
		System.out.println("Token text:      " + this.text + "");
		if(this.value != null) System.out.println("Token value:      " + this.value + "");
	}
    public TokenType getType()  
	{ 
		return type; 
	}
    public void setType(TokenType type){
    	this.type = type;
    }
    public String getText()  
	{ 
		return text; 
	}
    public Number getValue() 
	{ 
		return value; 
	}
    
    public void setText(String text)   
	{ 
		this.text = text; 
	}
    public void setValue(Number value) 
	{ 
		this.value = value; 
	}
}
