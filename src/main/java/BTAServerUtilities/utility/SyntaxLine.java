package BTAServerUtilities.utility;

public class SyntaxLine {

	public String name;
	public String owner;
	public String message;
	public boolean op;

	public SyntaxLine(String name, String owner, String message, boolean op){
		this.name = name;
		this.owner = owner;
		this.message = message;
		this.op = op;
	}

	public SyntaxLine(String name, String owner, String message){
		this.name = name;
		this.owner = owner;
		this.message = message;
		this.op = false;

	}

	//TODO change "none" when a CommandSyntaxLine has no owner to null. DO NOT DO THIS UNLESS ALL STATEMENTS HAVE BEEN PROPERLY ADJUSTED

	public SyntaxLine(String name, String message, boolean op){
		this.name = name;
		this.owner = "none";
		this.message = message;
		this.op = op;
	}

	public SyntaxLine(String name, String message){
		this.name = name;
		this.owner = "none";
		this.message = message;
		this.op = false;
	}

}
