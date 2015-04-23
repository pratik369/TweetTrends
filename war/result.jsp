
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="newTweet.tweetData" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>


<!-- saved from url=(0039)http://getbootstrap.com/examples/theme/ -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="http://getbootstrap.com/assets/ico/favicon.ico">

    <title>TweetTrends</title>

    <!-- Bootstrap core CSS -->
    <link href="http://getbootstrap.com/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="http://getbootstrap.com/dist/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="http://getbootstrap.com/examples/theme/theme.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  <style type="text/css"></style><style id="clearly_highlighting_css" type="text/css">/* selection */ html.clearly_highlighting_enabled ::-moz-selection { background: rgba(246, 238, 150, 0.99); } html.clearly_highlighting_enabled ::selection { background: rgba(246, 238, 150, 0.99); } /* cursor */ html.clearly_highlighting_enabled {    /* cursor and hot-spot position -- requires a default cursor, after the URL one */    cursor: url("chrome-extension://pioclpoplcdbaefihamjohnefbikjilc/clearly/images/highlight--cursor.png") 14 16, text; } /* highlight tag */ em.clearly_highlight_element {    font-style: inherit !important; font-weight: inherit !important;    background-image: url("chrome-extension://pioclpoplcdbaefihamjohnefbikjilc/clearly/images/highlight--yellow.png");    background-repeat: repeat-x; background-position: top left; background-size: 100% 100%; } /* the delete-buttons are positioned relative to this */ em.clearly_highlight_element.clearly_highlight_first { position: relative; } /* delete buttons */ em.clearly_highlight_element a.clearly_highlight_delete_element {    display: none; cursor: pointer;    padding: 0; margin: 0; line-height: 0;    position: absolute; width: 34px; height: 34px; left: -17px; top: -17px;    background-image: url("chrome-extension://pioclpoplcdbaefihamjohnefbikjilc/clearly/images/highlight--delete-sprite.png"); background-repeat: no-repeat; background-position: 0px 0px; } em.clearly_highlight_element a.clearly_highlight_delete_element:hover { background-position: -34px 0px; } /* retina */ @media (min--moz-device-pixel-ratio: 2), (-webkit-min-device-pixel-ratio: 2), (min-device-pixel-ratio: 2) {    em.clearly_highlight_element { background-image: url("chrome-extension://pioclpoplcdbaefihamjohnefbikjilc/clearly/images/highlight--yellow@2x.png"); }    em.clearly_highlight_element a.clearly_highlight_delete_element { background-image: url("chrome-extension://pioclpoplcdbaefihamjohnefbikjilc/clearly/images/highlight--delete-sprite@2x.png"); background-size: 68px 34px; } } </style><style id="holderjs-style" type="text/css"></style><style>[touch-action="none"]{ -ms-touch-action: none; touch-action: none; }[touch-action="pan-x"]{ -ms-touch-action: pan-x; touch-action: pan-x; }[touch-action="pan-y"]{ -ms-touch-action: pan-y; touch-action: pan-y; }[touch-action="scroll"],[touch-action="pan-x pan-y"],[touch-action="pan-y pan-x"]{ -ms-touch-action: pan-x pan-y; touch-action: pan-x pan-y; }</style></head>

  <body role="document" style="zoom: 1;">

    <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="http://getbootstrap.com/examples/theme/#">TweetTrends</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="http://getbootstrap.com/examples/theme/#">Home</a></li>
            <li><a href="http://getbootstrap.com/examples/theme/#about">About</a></li>
            
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
    
    
    
    
    
    

    <div class="container theme-showcase" role="main">

        
      
	
      <div class="page-header">
        <h1>Trending</h1>
      </div>
      <div class="row">
        
        <div class="col-sm-4">
          <div class="panel panel-success">
            <div class="panel-heading">
              <h3 class="panel-title">Positive</h3>
            </div>
            <div class="panel-body">

            <%
			
			Hashtable<String,PriorityQueue<tweetData>> ht=(Hashtable<String,PriorityQueue<tweetData>>)session.getAttribute("tags");
			
			for(String s:ht.keySet()){
			 %>
			<a href="tweets.jsp/?tag=<%= s%>"><%= s%></a></br>
			<%
			
		}
			%>
			
	      </div>
          </div>
          <div class="panel panel-info">
            <div class="panel-heading">
              <h3 class="panel-title">Neutral</h3>
            </div>
            <div class="panel-body">
            
            </div>
          </div> 
        <div class="panel panel-danger">
            <div class="panel-heading">
              <h3 class="panel-title">Negative</h3>
            </div>
            <div class="panel-body">

            </div>
          </div>
        </div><!-- /.col-sm-4 -->
        <div class="col-sm-4">
    
        </div><!-- /.col-sm-4 -->
      </div>




     

      <div class="page-header">
        <h1>Alerts</h1>
      </div>
      
        <%
        String s1="";
        for(String s:ht.keySet()){
        	s1=s;
        	break;
        }
        PriorityQueue<tweetData> pq=ht.get(s1);
        int sum=0,i=1;
        if(pq.isEmpty())
        {
        	%>Hiiiii<%
        }
        while(!pq.isEmpty()&& i<2){
        %>
        <div class="alert alert-success">
		<%
			tweetData t=pq.peek();
			sum+=(int)t.score;
			i++;
			%>
			Tweet: <%= t.s.getText()%>
			<%
			System.out.println("Tweet: "+t.s.getText()+"\n\t Score: "+t.score+"\t RC: "+t.s.getRetweetCount());
		 %>   
		      </div>	
		<%
		}
					
        %>

      <div class="alert alert-info">
        <strong>Heads up!</strong> This alert needs your attention, but it's not super important.
      </div>
      <div class="alert alert-warning">
        <strong>Warning!</strong> Best check yo self, you're not looking too good.
      </div>
      <div class="alert alert-danger">
        <strong>Oh snap!</strong> Change a few things up and try submitting again.
      </div>



     


      



</div>
     

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./Theme Template for Bootstrap_files/jquery.min.js"></script>
    <script src="./Theme Template for Bootstrap_files/bootstrap.min.js"></script>
    <script src="./Theme Template for Bootstrap_files/docs.min.js"></script>
  

</body></html>