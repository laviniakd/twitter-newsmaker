import java.util.*;
import twitter4j.*;
import java.io.*;

//Lavinia Dunagan, 2018
// uses n-gram analysis where n = 3 to create "hypothetical" news tweets using
// the raw JSON from the cited source

//Littman, Justin; Wrubel, Laura; Kerchner, Daniel; Bromberg Gaber, Yonah, 2017,
//"News Outlet Tweet Ids", https://doi.org/10.7910/DVN/2FIFLH, Harvard Dataverse,
//V3, UNF:6:I38WJ5vqwDky1fkEOeexvQ==; news_outlets.txt, NewsMaker.java

public class NewsMaker {
	
	static final int CHAR_LIMIT = 280; //max number of characters in a tweet
	static final int TWEET_NUM = 100; //number of tweets to generate
	
	public static void main(String[] args) throws FileNotFoundException, TwitterException {
		
		Scanner news = new Scanner(new File("hydratedtweets.txt")); 
			//raw JSON file containing all hydrated tweets
		File tweets = new File("tweets1.txt"); //generated tweets

		System.out.println(System.currentTimeMillis());
		List<String> jsons = makeJsons(news);
		System.out.println(System.currentTimeMillis());
		
		//makes list of tweet text using raw JSON
		List<String> tweetTexts = new ArrayList<String>();
		for (String j : jsons) {
			Status tweetText = TwitterObjectFactory.createStatus(j);
			String tweet = tweetText.getText();
			int twSize = tweet.split(" ").length;
			if (tweetText.getLang().equals("en") && twSize > 2) {
				tweetTexts.add(tweet);
			}
		}
		
		PrintStream p = new PrintStream(tweets);
		NewsNGram gram = makeNGram(tweetTexts);
		List<String> madeTweets = makeTweets(gram);
		for (String t : madeTweets) {
			p.println(t);
		}
		
		p.close();
		news.close();
	}
	
	//makes list of tweets in raw JSON using file
	public static List<String> makeJsons(Scanner news) {
		List<String> jsons = new ArrayList<String>();
		String json = "";
		while (news.hasNextLine()) {
			String line = news.nextLine();
			json += line;
			int length = line.length();
			
			String maybeTrue = line.substring(length - 6, length);
			String maybeFalse = line.substring(length - 7, length);
			if (maybeTrue.equals("true}}") || maybeFalse.equals("false}}")) {
				jsons.add(json);
				json = "";
			}
		}
		return jsons;
	}
	
	//creates n-gram representation of tweets
	//List<String> tweetTexts = tweets to make n-gram from
	public static NewsNGram makeNGram(List<String> tweetTexts) {

		NewsNGram gram = new NewsNGram(tweetTexts.get(0));
		
		for (int s = 1; s < tweetTexts.size(); s++) {
			NewsNGram more = new NewsNGram(tweetTexts.get(s), gram);
			gram = more;
		}
		return gram;
	}
	
	//creates the tweets using an n-gram object
	//NewsNGram gram = model of tweets being used
	public static List<String> makeTweets(NewsNGram gram) {
		
		List<String> madeTweets = new ArrayList<String>();
		
		Set<String> keysSet = gram.getNGrams().keySet();
		List<String> keys = new ArrayList<String>();
		for (String s : keysSet) {
			keys.add(s.trim());
		}
		
		for (int i = 0; i < TWEET_NUM; i++) {
 			madeTweets.add(gram.getATweet(keys, CHAR_LIMIT));
		}
		
		return madeTweets;
	}
}
