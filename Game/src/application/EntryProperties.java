package application;

//Class to store the data from the leaderboard text file
public class EntryProperties {
	public String name;
	public int score;
	
	EntryProperties(String player, int gameScore) {
		name = player;
		score = gameScore;
	}
}
