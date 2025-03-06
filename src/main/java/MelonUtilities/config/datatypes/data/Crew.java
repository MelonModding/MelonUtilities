package MelonUtilities.config.datatypes.data;

import java.util.ArrayList;
import java.util.List;

public class Crew {

	//public int crewVersion = MelonUtilities.crewConfigVersion;

	public String name;
	public String owner = null;
	public List<String> executives = new ArrayList<>();
	public List<String> members = new ArrayList<>();

	public Crew(String name, String owner, List<String> executives, List<String> members) {
		this.name = name;
		this.owner = owner;
		this.executives = executives;
		this.members = members;
	}

	public Crew(String name){
		this.name = name;
	}

	@Override
	public String toString(){
		return "WIP";
	}
}
