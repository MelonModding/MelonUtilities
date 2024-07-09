package BTAServerSolutions.BTAServerUtilities.utility;

public class CommandSyntaxLine {

	public String name;
	public String owner;
	public String message;

	public CommandSyntaxLine(String name, String owner, String message){
		this.name = name;
		this.owner = owner;
		this.message = message;
	}

	//TODO change "none" when a CommandSyntaxLine has no owner to null. DO NOT DO THIS UNLESS ALL STATEMENTS HAVE BEEN PROPERLY ADJUSTED

	public CommandSyntaxLine(String name, String message){
		this.name = name;
		this.owner = "none";
		this.message = message;
	}

}
