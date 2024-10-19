package MelonUtilities.utility;

import net.minecraft.core.net.command.CommandSender;

import java.util.ArrayList;

public class SyntaxBuilder {

	ArrayList<SyntaxLine> syntaxLines = new ArrayList<>();

	public void append(SyntaxLine syntaxLine){
		if(syntaxLine.owner.equals("none")){
			syntaxLines.add(syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
					} else {
						for (int j = i + 1; j < syntaxLines.size(); j++) {
							if (syntaxLines.get(j).name.equals(syntaxLines.get(i).owner)) {
								if (j == syntaxLines.size() - 1) {
									syntaxLines.add(syntaxLine);
									break;
								}
								continue;
							}
							syntaxLines.add(syntaxLine);
							break;
						}
					}
					break;
				}
			}
		}
	}

	public void append(String name, String message){
		SyntaxLine syntaxLine = new SyntaxLine(name, message);
		if(syntaxLine.owner.equals("none")){
			syntaxLines.add(syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
					} else {
						for (int j = i + 1; j < syntaxLines.size(); j++) {
							if (syntaxLines.get(j).name.equals(syntaxLines.get(i).owner)) {
								if (j == syntaxLines.size() - 1) {
									syntaxLines.add(syntaxLine);
									break;
								}
								continue;
							}
							syntaxLines.add(syntaxLine);
							break;
						}
					}
					break;
				}
			}
		}
	}

	public void append(String name, boolean op, String message){
		SyntaxLine syntaxLine = new SyntaxLine(name, message, op);
		if(syntaxLine.owner.equals("none")){
			syntaxLines.add(syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
					} else {
						for (int j = i + 1; j < syntaxLines.size(); j++) {
							if (syntaxLines.get(j).name.equals(syntaxLines.get(i).owner)) {
								if (j == syntaxLines.size() - 1) {
									syntaxLines.add(syntaxLine);
									break;
								}
								continue;
							}
							syntaxLines.add(syntaxLine);
							break;
						}
					}
					break;
				}
			}
		}
	}

	public void append(String name, String owner, String message){
		SyntaxLine syntaxLine = new SyntaxLine(name, owner, message);
		if(syntaxLine.owner.equals("none")){
			syntaxLines.add(syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
					} else {
						for (int j = i + 1; j < syntaxLines.size(); j++) {
							if (syntaxLines.get(j).name.equals(syntaxLines.get(i).owner)) {
								if (j == syntaxLines.size() - 1) {
									syntaxLines.add(syntaxLine);
									break;
								}
								continue;
							}
							syntaxLines.add(syntaxLine);
							break;
						}
					}
					break;
				}
			}
		}
	}

	public void append(String name, String owner, boolean op, String message){
		SyntaxLine syntaxLine = new SyntaxLine(name, owner, message, op);
		if(syntaxLine.owner.equals("none")){
			syntaxLines.add(syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
					} else {
						for (int j = i + 1; j < syntaxLines.size(); j++) {
							if (syntaxLines.get(j).name.equals(syntaxLines.get(i).owner)) {
								if (j == syntaxLines.size() - 1) {
									syntaxLines.add(syntaxLine);
									break;
								}
								continue;
							}
							syntaxLines.add(syntaxLine);
							break;
						}
					}
					break;
				}
			}
		}
	}

	public void prepend(SyntaxLine syntaxLine){
		if(syntaxLines.isEmpty()){
			syntaxLines.add(syntaxLine);
		} else if (syntaxLine.owner.equals("none")){
			syntaxLines.add(0, syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
                    } else {
						syntaxLines.add(i + 1, syntaxLine);
                    }
                    break;
                }
			}
		}
	}

	public void prepend(String name, String message){
		SyntaxLine syntaxLine = new SyntaxLine(name, message);
		if(syntaxLines.isEmpty()){
			syntaxLines.add(syntaxLine);
		} else if (syntaxLine.owner.equals("none")){
			syntaxLines.add(0, syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
                    } else {
						syntaxLines.add(i + 1, syntaxLine);
                    }
                    break;
                }
			}
		}
	}

	public void prepend(String name, String owner, String message){
		SyntaxLine syntaxLine = new SyntaxLine(name, owner, message);
		if(syntaxLines.isEmpty()){
			syntaxLines.add(syntaxLine);
		} else if (syntaxLine.owner.equals("none")){
			syntaxLines.add(0, syntaxLine);
		} else {
			for (int i = 0; i < syntaxLines.size(); i++) {
				if (syntaxLines.get(i).name.equals(syntaxLine.owner)) {
					if (syntaxLines.size() - 1 == i) {
						syntaxLines.add(syntaxLine);
                    } else {
						syntaxLines.add(i + 1, syntaxLine);
                    }
                    break;
                }
			}
		}
	}

	public void printAllLines(CommandSender sender){
		for(SyntaxLine syntaxLine : syntaxLines){
			if(sender.isAdmin() && syntaxLine.op) {
				sender.sendMessage(syntaxLine.message);
			} else if(!syntaxLine.op){
				sender.sendMessage(syntaxLine.message);
			}
		}
	}

	String thisLayerOwner = null;
	public void printLayer(String name, CommandSender sender){
		for(int i = 0; i < syntaxLines.size(); i++){
			if(syntaxLines.get(i).name.equals(name)){
				printLayerOwners(syntaxLines.get(i), sender);
				sender.sendMessage(syntaxLines.get(i).message);
				for(int j = i+1; j < syntaxLines.size(); j++){
					if(syntaxLines.get(j).owner.equals(name)){
						if(sender.isAdmin() && syntaxLines.get(j).op) {
							sender.sendMessage(syntaxLines.get(j).message);
							thisLayerOwner = syntaxLines.get(j).name;
						} else if(!syntaxLines.get(j).op){
							sender.sendMessage(syntaxLines.get(j).message);
							thisLayerOwner = syntaxLines.get(j).name;
						}
					}
				}
			}
		}
		thisLayerOwner = null;
	}

	boolean printedLayerOwners = false;
	public void printLayerAndSubLayers(String name, CommandSender sender){

		for(int i = 0; i < syntaxLines.size(); i++){
			if(syntaxLines.get(i).name.equals(name)){
				if(!printedLayerOwners) {
					printLayerOwners(syntaxLines.get(i), sender);
					sender.sendMessage(syntaxLines.get(i).message);
					printedLayerOwners = true;
				}
				for(int j = i+1; j < syntaxLines.size(); j++){
					if(syntaxLines.get(j).owner.equals(name)){
						sender.sendMessage(syntaxLines.get(j).message);
						thisLayerOwner = syntaxLines.get(j).name;
					} else if (syntaxLines.get(j).owner.equals(thisLayerOwner)) {
						printedLayerOwners = true;
						printLayerAndSubLayers(thisLayerOwner, sender);
					}
				}
			}
		}
		thisLayerOwner = null;
		printedLayerOwners = false;
	}

	ArrayList<String> layerOwnerMessages = new ArrayList<>();
	String insideLayersOwner;
	SyntaxLine insideLayer;
	private void printLayerOwners(SyntaxLine syntaxLine, CommandSender sender){
		insideLayer = syntaxLine;
		for(int i = syntaxLines.size() - 1; i >= 0; i--){
			if(insideLayer.owner.equals("none")) {
				break;
			}
			if(syntaxLines.get(i).name.equals(syntaxLine.name)){
				insideLayer = syntaxLine;
				for(int j = i-1; j >= 0; j--){
					assert insideLayer != null;
					if(syntaxLines.get(j).name.equals(insideLayer.owner)){
						layerOwnerMessages.add(0, syntaxLines.get(j).message);
						insideLayer = syntaxLines.get(j);
						printLayerOwners(insideLayer, sender);
					}
				}
			}
		}
		for(String message : layerOwnerMessages) {
			sender.sendMessage(message);
		}
		layerOwnerMessages.clear();
		insideLayersOwner = null;
	}

	public void clear(){
		syntaxLines.clear();
	}
}
