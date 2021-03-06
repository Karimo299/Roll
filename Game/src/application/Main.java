package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;


public class Main extends Application {
	
	//load up the map and the player
	private Map world = new Map("Maps/map1.txt");
	private Player ball = new Player(world, 300, 200, 100);
	
	private Stage stage;
	
	//setup for the game scene
	private Pane gameRoot = new Pane();
	private Scene gameScene = new Scene(gameRoot, 600, 500, true, SceneAntialiasing.BALANCED);
	
	//Set up the camera and lighting for better visuals.
	private Camera camera = new PerspectiveCamera();
	private PointLight light = new PointLight();

	private boolean[] pressedKeys = new boolean[2];
	
	private Text scoreText = new Text(450, 40, "Score: " + ball.score);
	
	AnimationTimer timer = new Timer();

	//Need this to make the app refresh to allow for the movement as per examples online
	class Timer extends AnimationTimer {
		private int gameState;
		private int counter  = 0;
		private String name = new String();
		private Text nameText = new Text("Name: " + name);
		private Leaderboard leaderboard = null;
		private boolean shiftPressed = false;
		private int framesPassed = 0;
		
	    @Override
	    public void handle(long now) {
	    	framesPassed = framesPassed+1;
	    	framesPassed = framesPassed % (100/60);
	    	if (framesPassed == 0) {
//	    		System.out.println(framesPassed);
	    		//updates the score on the top right
	    		scoreText.setText("Score: " + ball.score);
	    		
	    		//GameState controls which scene to show, either the game, input name, or the leaderboard
	    		gameState = ball.gameState;
	    		if(gameState == Player.PLAY) { // If the state is to play
	    			ball.move(pressedKeys);
	    			stage.setScene(gameScene);
	    			
	    		} else if (gameState == Player.ENTRY) { //If the state is to input the name
	    			//Counter just sets a delay so the switch doesn't seem abrupt
	    			if(counter < 50) {	 
	    				counter ++;
	    			} else {
	    				if (leaderboard == null) {
	    					leaderboard = new Leaderboard(ball.score);
	    					nameText.setStyle("-fx-font: 25 arial;");
	    					nameText.setTranslateY(25);
	    					leaderboard.entryRoot.getChildren().add(nameText);
	    				}
	    				
	    				stage.setScene(leaderboard.entryScene);	
	    				
	    				/* 
	    				 * So when I used textFeilds and Button classes, it would cause
	    				 * an issue with the 3D scenes, so I had come up with this instead
	    				 * */
	    				leaderboard.entryScene.setOnKeyReleased(e -> { 
	    					if(e.getCode() == KeyCode.SHIFT) shiftPressed = false;
	    				});
	    				
	    				leaderboard.entryScene.setOnKeyPressed(e -> {
	    					if(e.getCode() == KeyCode.SHIFT) shiftPressed = true;
	    					//If letters, or digits were being typed, It adds them to the name String
	    					if (e.getCode() == KeyCode.BACK_SPACE) { //Deletes the last character if the user presses backspace
	    						if (!name.equals("")) {
	    							name = name.substring(0, name.length() - 1);
	    							nameText.setText("Name: " + name);    							
	    						}
	    					} else if (!e.getCode().isWhitespaceKey()) {
	    						if(shiftPressed) {
	    							name  = name + e.getText().toUpperCase();
	    							
	    						} else {
	    							name  = name + e.getText();	    			    		
	    						}
	    						nameText.setText("Name: " + name);
	    					} else if (e.getCode() == KeyCode.ENTER && !name.equals("")) { //Checks if the name isn't empty, and adds that to the leaderboard
	    						ball.changeState(Player.LEADERBOARD);
	    						leaderboard = new Leaderboard(ball.score);
	    						leaderboard.addEntry(name, ball.score);
	    						leaderboard = null;
	    					}
	    				});
	    				
	    				leaderboard = null;
	    			}
	    		} else if (gameState == Player.LEADERBOARD) { //If the state needs to show the leaderboard
	    			if (leaderboard == null) {
	    				leaderboard = new Leaderboard(ball.score);
	    				leaderboard.readArray();
	    			}
	    			stage.setScene(leaderboard.leaderboardScene);
	    			leaderboard.leaderboardScene.setOnKeyPressed(e -> {
	    				if (e.getCode() == KeyCode.ENTER) {
	    					ball.changeState(Player.PREP);
	    				}	
	    			});
	    		} else if (gameState == Player.PREP){ //Prep state is just to reset everything so the player can play again
	    			leaderboard = null;
	    			ball.translate = null;
	    			pressedKeys[0] = false;
	    			pressedKeys[1] = false;
	    			counter = 0;
	    			gameRoot.getChildren().remove(ball.sphere);
	    			ball = new Player(world, 300, 200, 100);
	    			gameRoot.getChildren().add(ball.sphere);
	    		}
	    		ball.groundCheck();
	    		followBall();
	    	}
	    }
	}
	
	//Makes the camera and the lighting follow the ball as it moves forward
	void followBall() {
		camera.setTranslateZ(ball.sphere.getTranslateZ());
    	light.setTranslateX(camera.getTranslateX());
		light.setTranslateY(camera.getTranslateY());
		light.setTranslateZ(camera.getTranslateZ());
		scoreText.setTranslateZ(camera.getTranslateZ());
	}

	//Required by JavaFX 
	@Override
	public void start(Stage primaryStage) {
		scoreText.setStyle("-fx-font: 25 arial;");
		stage = primaryStage;
		try {
			//Applies rotation to give that top-down view
			Rotate rotateX = new Rotate(-30, Rotate.X_AXIS);	
			camera.getTransforms().add(rotateX);
			scoreText.getTransforms().add(rotateX);
			
			
			//Create group that has everything and sets it up
			gameRoot.getChildren().addAll(world.group, ball.sphere, light, scoreText);
			gameScene.setCamera(camera);
			gameScene.setFill(Color.LIGHTSKYBLUE);
			primaryStage.setScene(gameScene);
			primaryStage.setTitle("ROLL");
			primaryStage.setResizable(false);
						
			
			//Starts the timer needed to keep refreshing the screen
	        timer.start();
			
	        //Adds the keyHandler needed
	        gameScene.setOnKeyPressed(e -> {
		    	if (e.getCode() == KeyCode.A) pressedKeys[0] = true;
				if (e.getCode() == KeyCode.D) pressedKeys[1] = true;
			});
			
	        gameScene.setOnKeyReleased(e -> {
				if (e.getCode() == KeyCode.A) pressedKeys[0] = false;
				if (e.getCode() == KeyCode.D) pressedKeys[1] = false;
			});	
	
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace(); 
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

