
package newTweet;
import java.io.Serializable;
import java.util.Comparator;

import twitter4j.Status;

public class tweetData implements Serializable{
	public Status s;
	public int score;
	
	tweetData(Status s,int score){
		this.s=s;
		this.score=score;
	}
	
}

class mysort implements Comparator<tweetData>,Serializable{

	
	@Override
	public int compare(tweetData o1, tweetData o2) {
		// TODO Auto-generated method stub
		return o2.s.getRetweetCount()-o1.s.getRetweetCount();
	}
}
