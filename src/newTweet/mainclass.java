package newTweet;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import javax.servlet.http.HttpSession;

//import com.google.appengine.repackaged.com.google.api.client.json.Json;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.JSONObject;
import twitter4j.JSONArray;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;
import newTweet.tweetData;
//import com.google.appengine.labs.repackaged.org.json.JSONArray;





public class mainclass extends HttpServlet{
	HashMap<String,int[]> hm=new HashMap<String,int[]>();
	public static DatastoreService datastore=DatastoreServiceFactory.getDatastoreService();
	HashSet<String> negate=new HashSet<String>();
	HashSet<String> stopwords=new HashSet<String>();
	Hashtable<String,PriorityQueue<tweetData>> ht=new Hashtable<String,PriorityQueue<tweetData>>();
	Hashtable<String,PriorityQueue<tweetData>> htpos=new Hashtable<String,PriorityQueue<tweetData>>();
	Hashtable<String,PriorityQueue<tweetData>> htneg=new Hashtable<String,PriorityQueue<tweetData>>();
	Hashtable<String,PriorityQueue<tweetData>> htneu=new Hashtable<String,PriorityQueue<tweetData>>();
	HashSet<Integer> hs=new HashSet<Integer>();
	Pattern p=Pattern.compile("#\\s*(\\w+)");
	
	
	public void init()throws ServletException
	{
		makeMap();
	}
	
	public void makeMap(){
		System.out.println("in init....***********");
		negate.add("not");
		negate.add("no");
		negate.add("never");
		negate.add("neither");
		negate.add("anti");
	
		Scanner scanner,scanner2;
		try{
			 File file = new File("Sentiment.csv");
			 File file2 = new File("dicts.txt");
			
			 scanner= new Scanner(file);
			 scanner2= new Scanner(file2);
			 
			 while(scanner2.hasNextLine())
			{
					String line = scanner2.nextLine();
					//System.out.println(line);
					String []split=line.split("\\s+");
					if (split.length>1)
					{
					
						if(!hm.containsKey(split[0].trim()))
						{	//System.out.println(split[0]);
							int a[]=new int[2];
							
							if (split.length<2){
								//System.out.print(split[0]);
							}
							int i=Integer.parseInt(split[1].trim());
							if (i<0)
							{
									a[0]=0;
									a[1]=(-1)*i;
							}
							else
							{
								a[0]=i;
								a[1]=0;
							}
							
							hm.put(split[0].trim(),a);
	
						}
					}
					
					else{
						//System.out.println(line);
					}
				}
			 
			 //System.out.println(hm.size());
			 
			 
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				String []split=line.split(",");
				
				if(!hm.containsKey(split[0]))
				{	//System.out.println(split[0]);
					String p=split[1].replaceAll("positive:","");
					String n=split[2].replaceAll("negative:","");
					//System.out.println(p+"-"+n);
					int []a={Integer.parseInt(p),Integer.parseInt(n)};
					hm.put(split[0].trim(),a);

				}
			}
			//System.out.println(hm.size());	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}
	
	
	//Get tweets
	
	public Query buildQuery(String cat,String reg)
	{
		System.out.println("in gettweet 2");
        
        
        double lat;
        double lon;
        
        String inputLine;
        StringBuilder builder = new StringBuilder();
       
        Query query=null;
        
        try {
             query= new Query(cat);
             String adr[]=reg.split("\\s");
             String a="";
             for(int i=0;i<adr.length;i++){
            	 a=a+adr[i];
             }
             
             URL geoapi = new URL("http://maps.googleapis.com/maps/api/geocode/json?"+"address="+a+"&sensor=false");
             URLConnection yc = geoapi.openConnection();
             BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
             while ((inputLine = in.readLine()) != null) 
             {              
            	 builder.append(inputLine); 
             }
             in.close();
             
             JSONObject json = new JSONObject(builder.toString());
             lat =(double) ((JSONArray)json.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location").get("lat");
                
             lon = (double)((JSONArray)json.get("results")).getJSONObject(0)
                     .getJSONObject("geometry").getJSONObject("location").getInt("lng");
                
             String ln =  (String) ((JSONArray)json.get("results")).getJSONObject(0)
                     .get("formatted_address");
             System.out.print(ln);
             System.out.print(lat+" "+lon);
             
        
            GeoLocation geo = new GeoLocation(lat,lon);
     		query.setGeoCode(geo, 250.0, Query.KILOMETERS);
     		query.setCount(100);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        
        return query;
	}
	
	public boolean gettweet(Query query) throws TwitterException
	{	
		ArrayList<Status> tweetdata=new ArrayList<Status>();
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("BrrP3VBsabHudki1tJQQ", "PJJnS6eCiwWf7cJ1Zt67orwTI8TyUM0We1cBw80jWw");
        twitter.setOAuthAccessToken(new AccessToken("1355786214-vXljuYoRNBlJbuD1AN8Jw5OfeUWaH7MYHAnif5S", "nixPPxzPStp3upmgxV6qV7XB5XcarhAv9RPb7hGc"));
        
		
        List<Status> tweets=null;
         QueryResult result=null;
         
        try {
             	tweetdata.clear();
     		
            int cnt=0;
           do {
                result = twitter.search(query);
                tweets=result.getTweets();
                //System.out.println("size: "+tweets.size());
                cnt++;
                for(Status s:tweets)
                {
                	categorize(s);
                }
                
                
                //tweetdata.addAll(tweets);
                query=result.nextQuery();
                
           } while(cnt!=10 &&(query=result.nextQuery())!=null);
            
        }
        catch(Exception e)
        {
        e.printStackTrace();
        e.getCause();
        }
        System.out.println("--"+tweetdata.size());
       return true;
        
	} 
	
		
	
	public int getScore(String s)
	{	
		
		int sum=0;
		int[] val=new int[2];
		boolean fl=false;
		s=s.replaceAll("[^A-Za-z0-9]", " ");
	    String[] spl=s.split("\\s");	
		
	    for(int i=0;i<spl.length;i++){
			if(negate.contains(spl[i].toLowerCase()))
			{
				fl=true;
				continue;
			}
			//System.out.println("Score: "+sum+"wrd: "+spl[i]);
			if(hm.containsKey(spl[i].toLowerCase())){	
		        val=hm.get(spl[i].toLowerCase());
		        if(fl){
		        	sum+=(-1)*val[0]-(-1)*val[1];
		        	fl=false;
		        }
		        else{
		        sum+=val[0]-val[1];
				}
			}
				
			}
		return sum;
	}
	
	
	
	

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		  PrintWriter out = resp.getWriter();
		  try
		  {  
			  //makeMap();
	          hs.clear();
	          ht.clear();
	          htpos.clear();
	          htneg.clear();
	          htneu.clear();
			  System.out.println("1");
	          String cat= req.getParameter("category");
	          String reg = req.getParameter("region");
	          System.out.println("2");
	          
	          System.out.println(cat);
	          System.out.println(reg);
	          
	        boolean val;	
	        Query q=buildQuery(cat, reg);
	  		val=gettweet(q);
	  		//Hashtable<String,PriorityQueue<tweetData>> ht=categorize(ar);
	  		printResults(ht);
	  		if(val)
	  		{
	  			
	  			HttpSession ss=req.getSession();
	  			ss.setAttribute("tags",ht);
	  			System.out.println("Done...");
	  			System.out.println("POS: "+htpos.size());
	  			System.out.println("neg: "+htneg.size());
	  			System.out.println("neu: "+htneu.size());
	  			resp.sendRedirect("./result.jsp");
	  			
	  		}
	  		
	  		
	  		
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
	}
	
	public void categorize(Status s)
	{
		
		String text=s.getText();
		URLEntity[] ue=s.getURLEntities();
		if(ue.length>0)
		{	for(int i=0;i<ue.length;i++)
			{
				
				text=text.replaceAll(ue[i].getText(), " ");
			}
		}
		
		if ( !(hs.contains(text.hashCode())) && s.isRetweet()==false)
		{
			int score=getScore(text);
			
			HashtagEntity[] he=s.getHashtagEntities();
			
			tweetData td=new tweetData(s,score);
			for(int i=0;i<he.length;i++)
			{
				PriorityQueue<tweetData> pqd;	
				String str=he[i].getText().toString();
				if(ht.containsKey((str.toUpperCase())))
				{	
					pqd=ht.get(str.toUpperCase());		
				}
				else
				{
					mysort ms=new mysort();
					pqd=new PriorityQueue<tweetData>(10,ms);
				}
				pqd.add(td);
				ht.put(str.toUpperCase(), pqd);
				hs.add(text.hashCode());
		//System.out.println(text+": \n\tScore: "+score.get(score.size()-1)+" \t"+s.getCreatedAt()+"\n\t Hash: "+score.get(0));
		
			}
	
	
		}
		
	}
	
	
	public void printResults(Hashtable<String,PriorityQueue<tweetData>> ht){
		for(String s:ht.keySet()){
			
			PriorityQueue<tweetData> pq=ht.get(s);
			int sum=0;
			
			tweetData[] t=(tweetData[]) pq.toArray();
			
			for(tweetData td:t)
			{
				sum+=td.score;
			}
			
			if(sum<0)
			{
				htneg.put(s,pq);
			}
			else if(sum>0)
			{
				htpos.put(s,pq);
			}
			else
			{
				htneu.put(s, pq);
			}
			
		}
		
		}
		

}