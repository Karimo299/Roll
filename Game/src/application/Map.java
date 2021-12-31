package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.*;
import javafx.scene.paint.Color;

public class Map {
	private String path;
	public Group group = new Group();
	public ArrayList<int[]> mapArray;
	
	Map(String filePath) {
		path = filePath;
		mapArray();
		for(int i = mapArray.size()-1; i >= 0; i--) {
			for (int j = 0; j < mapArray.get(i).length; j++) {
				if(mapArray.get(i)[j] == 1) {
					Tile block;
					if (i == mapArray.size()-1) {
						block = new Tile("Assets/finish.png", Tile.size * (j+2), 300, Tile.size*(i-2));
					} else {
						block = new Tile("Assets/block.png", Tile.size * (j+2), 300, Tile.size*(i-2));															
					}
					group.getChildren().addAll(block.box);
				}
			}
		}
		AmbientLight light = new AmbientLight(Color.web("444555", 1));
		group.getChildren().add(light);
	}
	
	private void mapArray() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			Scanner s = new Scanner(reader);
			ArrayList<int[]> tempMapArray = new ArrayList<int[]>();
			
			while(s.hasNextLine()) {
				String[] line = s.nextLine().trim().split(" ");
				int[] tempArray = new int[line.length];
	            for (int j=0; j<line.length; j++) {
	            	tempArray[j] = Integer.parseInt(line[j]);
	            }
	            tempMapArray.add(tempArray);
	     	}
			s.close();
			mapArray = tempMapArray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
