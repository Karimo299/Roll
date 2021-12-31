package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Leaderboard {
	
	//Scene for the name entry
	public StackPane entryRoot = new StackPane();
	public Scene entryScene = new Scene(entryRoot, 600, 500, true, SceneAntialiasing.BALANCED);
	
	//Scene for the leaderboard
	public StackPane leaderboardRoot = new StackPane();
	public Scene leaderboardScene = new Scene(leaderboardRoot, 600, 500, true, SceneAntialiasing.BALANCED);
	
	private String path = "Assets/leaderboard.txt";
	
	private ArrayList<EntryProperties> leaderboardList = new ArrayList<EntryProperties>();
	public  ArrayList<EntryProperties> sortedList = new ArrayList<EntryProperties>();
	
	
	Leaderboard(int score) {		
		//entryScene elements
		Text scoreMessage = new Text("Your score is: " + score);
		scoreMessage.setStyle("-fx-font: 25 arial;");
		scoreMessage.setTranslateY(-100);
		Text enterMessage = new Text("Enter your name to save your score!");
		Text enterMessage1 = new Text("Then press enter!");
		enterMessage.setStyle("-fx-font: 25 arial;");
		enterMessage1.setStyle("-fx-font: 25 arial;");
		enterMessage.setTranslateY(-50);
		enterMessage1.setTranslateY(-25);
		entryScene.setFill(Color.LIGHTSKYBLUE);
		entryRoot.getChildren().addAll(scoreMessage, enterMessage, enterMessage1);
		
		//LeaderboardScene elements
		Rectangle leaderboardSquare = new Rectangle(550, 300);
		Text leaderboardText = new Text("Leaderboard");
		leaderboardText.setStyle("-fx-font: 50 arial;");
		StackPane.setAlignment(leaderboardText, Pos.TOP_CENTER);
		leaderboardSquare.setTranslateY(leaderboardSquare.getTranslateY() + 20);
		leaderboardSquare.setFill(Color.WHITE);
		leaderboardSquare.setStroke(Color.BLACK);
		leaderboardScene.setFill(Color.LIGHTSKYBLUE);
		leaderboardRoot.getChildren().addAll(leaderboardText, leaderboardSquare);
	}
	
	//Adds the new score to the leaderboard file
	public void addEntry(String name, int score) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
		    writer.write(name + ", " + score + "\n");
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Sorts the array from highest score to lowest
	private void sortList(ArrayList<EntryProperties> list) {
		if (list.size() > 0) {
			int highestNumber = 0;
			int index = 0;
			for (int i = 0; i < list.size(); i++) {
				int score = (int) list.get(i).score;
				
				if(score > highestNumber) {
					highestNumber = score;
					index = i;
				}	
			}
			
			sortedList.add(new EntryProperties(list.get(index).name, list.get(index).score));
			list.remove(index);
			sortList(list);			
		}
	}
	
	//Reads the file and creates an array
	public void readArray() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			Scanner s = new Scanner(reader);
			
			while(s.hasNextLine()) {
				String[] line = s.nextLine().trim().split(", ");
				leaderboardList.add(new EntryProperties(line[0], Integer.parseInt(line[1])));
	     	}
			
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sortList(leaderboardList);
		createCells();
	}
	
	//Creates the cells that rank player's usernames
	private void createCells() {
		for (int i = 0; i < 8; i++) {
			Rectangle cell = new Rectangle(550, 50);
			cell.setTranslateY(-155 + (50 * i));
			cell.setFill(Color.WHITE);
			cell.setStroke(Color.BLACK);
			leaderboardRoot.getChildren().add(cell);
			
			if (i < sortedList.size()) {
				Text text = new Text((i+1) + ") " + sortedList.get(i).name + ": " + sortedList.get(i).score);
				text.setStyle("-fx-font: 25 arial;");
				text.setTranslateX(250 - text.getLayoutBounds().getWidth()/2);
				text.setTranslateY(-155 + (50 * i));
				StackPane.setAlignment(text, Pos.CENTER_LEFT);
				leaderboardRoot.getChildren().add(text);
			}	
		}
	}
}
