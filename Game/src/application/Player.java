package application;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;	
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;


public class Player {
	public int score = 0;
	public static int PLAY = 1; 
	public static int ENTRY = 2; 
	public static int LEADERBOARD = 3;
	public static int PREP = 4;
		
	Sphere sphere;
	Map world;

	double fallSpeed = 0;
	int gameState = Player.PLAY;
	TranslateTransition translate = new TranslateTransition(); 
	
	Player(Map map, double XPosition, double YPosition, double ZPosition) {
		world = map;
		sphere = new Sphere(50);
		sphere.setTranslateX(XPosition);
		sphere.setTranslateY(YPosition);
		sphere.setTranslateZ(ZPosition);
		PhongMaterial material = new PhongMaterial(Color.RED, null, null, null, null);
		sphere.setMaterial(material);
	}

	
	//method for the fall animation and ending the game
	void fall() {
		fallSpeed = 10;
		sphere.setTranslateY(sphere.getTranslateY() + fallSpeed);    		
		TranslateTransition translate = new TranslateTransition(); 
		translate.setToX((int) Math.round(sphere.getTranslateX()/ 100) * Tile.size); 
		translate.setNode(sphere);
		translate.setDuration(Duration.seconds(0.03));
		translate.play();
		if (gameState == Player.PLAY) changeState(Player.ENTRY);
	}
	
	//checks if there is a ground underneath 
	void groundCheck() {
		try {
			int row = (int) Math.round(sphere.getTranslateZ()/ 100 + 2);
			int column = (int) Math.round(sphere.getTranslateX()/ 100 - 2);
			score = (row - 3)*2;
			
			if (row == world.mapArray.size() - 1) {
				if (gameState == Player.PLAY) changeState(Player.ENTRY);
			}
			
			if (world.mapArray.get(row)[column] == 0) {
				fall();
			} else {
				fallSpeed = 0;
			}
			
			sphere.setTranslateY(sphere.getTranslateY() + fallSpeed);
		} catch(Exception e) {
			fall();
		}
	}
	
	void move(boolean[] pressedKeys) {
		sphere.setTranslateZ(sphere.getTranslateZ() + 20);
		if(pressedKeys[0]) sphere.setTranslateX(sphere.getTranslateX() - 8);
		if(pressedKeys[1]) sphere.setTranslateX(sphere.getTranslateX() + 8);	
	}
	
	void changeState(int state) {
		gameState = state;
	}
}
