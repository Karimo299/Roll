package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.shape.Box;
import javafx.scene.paint.Color;  
import javafx.scene.paint.PhongMaterial;

public class Tile {
	public Box box;
	public static int size = 100;
	
	//Tile class, takes in the path of the texture, and the location to create a box
	Tile(String path, double XPosition, double YPosition, double ZPosition) {
		Image image; 
		try {
			image = new Image(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			image = null;
			e.printStackTrace();
		}
		
		
		box = new Box(size, size, size);
		box.setTranslateX(XPosition);
		box.setTranslateY(YPosition);
		box.setTranslateZ(ZPosition);	
	 
		PhongMaterial material = new PhongMaterial(Color.WHITE, image, null, null, null);
		box.setMaterial(material); 	
	}
}
