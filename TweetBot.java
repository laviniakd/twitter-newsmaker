//Lavinia Dunagan, 2018
//posts tweets to Twitter account @newsmaker made with NewsMaker

import java.util.*;
import twitter4j.*;

public class TweetBot {
	
	public static void main(String... args) {
		try {
			sendTweet("Hello fellow journalists!");
			getHomeTimeLine();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private static Status sendTweet(String text) throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();

		Status status = null;

		status = twitter.updateStatus(text);
		System.out.println("Successfully updated the status to ["
				+ status.getText() + "].");

		return status;
	}

	private static void getHomeTimeLine() throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();
		List<Status> statuses = null;
		statuses = twitter.getHomeTimeline();

		System.out.println("Showing home timeline.");
		if (statuses != null) {
			for (Status status : statuses) {
				System.out.println(status.getUser().getName() + ":"
						+ status.getText());
			}
		}
	}


}