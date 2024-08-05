package BTAServerUtilities.config.custom.classes;

import java.util.List;

public class Crew {
	public String name;
	public String owner;
	public List<String> executives;
	public List<String> members;

	public Crew(String name, String owner, List<String> executives, List<String> members) {
		this.name = name;
		this.owner = owner;
		this.executives = executives;
		this.members = members;
	}

	@Override
	public String toString(){
		return 	"WIP";
	}
}
