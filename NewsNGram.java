//Lavinia Dunagan, 2018
//n-gram data structure built from input text, can generate new text from it

import java.util.*;

public class NewsNGram {
	
	private List<String> words; //tweet text in array form
	private Map<String ,List<String>> nGrams; //n-grams in map format
    public static final int n = 4; //currently doing n = 3 analysis
	
    //constructs n-gram
    //String text = set of text to make n-gram from
    public NewsNGram(String text) {
		
    	words = new ArrayList<String>();
		String[] wordArr = text.split(" ");
		for (int i = 0; i < wordArr.length; i++) {
			String word = wordArr[i];
			if (word.length() < 5 || 
				!word.substring(0, 5).equalsIgnoreCase("https")) {
				words.add(word);
			}
		}
        nGrams = new HashMap<String ,List<String>>();
	}
	
	//constructs a new n-gram from tweet and other n-gram
	//String s = tweet
	//NewsNGram o = other n-gram
	public NewsNGram(String s, NewsNGram o) {
		
		this(s);
		makeNGrams();
		Map<String, List<String>> oNGrams = o.getNGrams();
		
		for (String key : nGrams.keySet()) {
			if (oNGrams.containsKey(key)) {
				oNGrams.get(key).addAll(nGrams.get(key));
			} else {
				oNGrams.put(key, nGrams.get(key));
			}
		}
		nGrams = oNGrams;
		
		words.addAll(o.getWords());
	}
	
	//returns the words String[] of the n-gram
	public List<String> getWords() {
		return words;
	}
	
	//returns the n-gram
	public Map<String, List<String>> getNGrams() {
		if (nGrams.isEmpty()) {
			makeNGrams();
		}
		return nGrams;
	}
	
	//makes the n-gram in map format
	public void makeNGrams() {

        for(int i = 0; i < words.size() - n; i++){
        	
        	String key = "";
        	
        	key += words.get(i).trim();
        	
        	for (int j = 1; j < n - 1; j++) {
        		key += " " + words.get(i + j);
        	}

            if (!nGrams.containsKey(key)) {
            	ArrayList<String> list = new ArrayList<>();
                nGrams.put(key, list);
            }
            
            nGrams.get(key).add(words.get(i + n - 1));
        }
    }
		
	//returns a tweet String after random generation using the n-gram
	public String getATweet(List<String> keys, int charLim) {
		
		Random r = new Random();
		int index = r.nextInt(keys.size());
		String start = "";
		
		String w = keys.get(index);
		String[] madeNews = w.split(" ");
		start += w + " ";
		
		boolean underLimit = true;
		String news = start;
		String key = start.trim();
		
        while (nGrams.containsKey(key) && underLimit) {
        	
        	List<String> options = nGrams.get(key);
            if (options.size() > 1) {  	
            	index = r.nextInt(options.size());
            } else {
            	index = 0;
            }
            String next = options.get(index);
            
            key = "";
            if ((news + next).length() < charLim) {
            	
            	//---look for way to make less awkward
            	for (int i = 0; i < madeNews.length - 1; i++) {
            		madeNews[i] = madeNews[i + 1];
            		key += madeNews[i] + " ";
            	}
            	madeNews[madeNews.length - 1] = next;
            	key += next;
            	news += next + " ";
            	
            } else {
            	underLimit = !underLimit;
            } 
        }
        
        return news.trim();
	}
}