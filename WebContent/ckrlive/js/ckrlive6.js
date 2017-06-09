    var springlesserverURL = "http://localhost:8080/openrdf-sesame";
	var restURL = "http://localhost:8080/SpringlesREST/rest/rest/";
//	var springlesserverURL = "http://dkmvm1.fbk.eu:8080/openrdf-sesame";
//	var restURL = "http://dkmvm1.fbk.eu:8080/SpringlesREST/rest/rest/";
		
//    var springlesserverURL = "http://madness:8080/openrdf-sesame";
//	var restURL = "http://madness:8080/SpringlesREST/rest/rest/";
	
	var springlesrepositoryID= "test-springles-6";
	var dataSend_ ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL  ;
	
	var url_playersfromtime=restURL+"playersfromtime";
	var url_getEvents=restURL+"eventlist";
	var url_getTeams=restURL+"teams";
	var url_getGoalList=restURL+"goallist";
	var url_getSubstitutionList=restURL+"substitutionlist";
	var url_getYCardList=restURL+"ycardlist";
	
	var eventtimearray = [];
	var event_ticks=[];
	var ticks_time=[];
$(document).ready(function(){
	
	var ticks_event=[0,11,46,58,68,90];
	var substitutionList=[];
	var dataSend =dataSend_ ;
	var goalList=[];
	var ycardList=[];
	var events=[];
	
  
	
	$.ajax({
		url : url_getTeams , 
		data : dataSend, 
        contentType: "application/json; charset=utf-8",
        dataType: "json",
		type : "GET"
	
	}).done(function(dataTeam, textStatus, jqXHR) {	
		
		
		if (dataTeam==null)
			 return false;
		var	teams_=dataTeam["team"];
			var hometeam=teams_[0].label;
			var hostteam=teams_[1].label;
			if (teams_[1].type=="home"){
			 hometeam=teams_[1].label;
			 hostteam=teams_[0].label;
			
			}
			
				
			$("#hometeam").html( hometeam);
	        $("#hostteam").html( hostteam);
			
			 
	});
	

	$.ajax({
		url : url_getGoalList , 
		data : dataSend, 
        contentType: "application/json; charset=utf-8",
        dataType: "json",
		type : "GET"
	
	}).done(function(dataGoal, textStatus, jqXHR) {	
		if (dataGoal==null)
			 return false;
			goal_data=dataGoal["scoredGoal"];
		
			 for(var index=0;index<goal_data.length ;index++){
				// var time_label = new Array(events[i].time,events[i].label);
				// var event_=new Array(events_[index].time,events_[index].label);
				
				 goalList[goal_data[index].id]=goal_data[index].time;
			 }
		
		
			
			 
	});
	
	
	$.ajax({
		url : url_getSubstitutionList , 
		data : dataSend, 
        contentType: "application/json; charset=utf-8",
        dataType: "json",
		type : "GET"
	
	}).done(function(dataSubs, textStatus, jqXHR) {	
		if (dataSubs==null)
			 return false;
		var	substitution_data=dataSubs["substitution"];
		
			 for(var index=0;index<substitution_data.length ;index++){
				// var time_label = new Array(events[i].time,events[i].label);
				// var event_=new Array(events_[index].time,events_[index].label);
				
				 substitutionList[substitution_data[index].id]=substitution_data[index].time;
			 }
		
		
			
			 
	});	
	
	
	$.ajax({
		url : url_getYCardList , 
		data : dataSend, 
        contentType: "application/json; charset=utf-8",
        dataType: "json",
		type : "GET"
	
	}).done(function(dataYCard, textStatus, jqXHR) {	
		if (dataYCard==null)
			 return false;
		var	ycard_data=dataYCard["yCard"];
		
			 for(var index=0;index<ycard_data.length ;index++){
				// var time_label = new Array(events[i].time,events[i].label);
				// var event_=new Array(events_[index].time,events_[index].label);
				
				ycardList[ycard_data[index].id]=ycard_data[index].time;
			 }
		
		
			
			 
	});	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// With JQuery
	$.ajax({
		url : url_getEvents , 
		data : dataSend, 
        contentType: "application/json; charset=utf-8",
        dataType: "json",
		type : "GET"
	
	}).done(function(dataEvents, textStatus, jqXHR) {	
		
	//	$("#result").empty();
	//	$('#result').append("<pre>"+library.json.prettyPrint(data)+"<pre>");
	//	$("#request").empty();
	//	$('#request').html("<span>GET " + url_getEvents + "</span><br />" + "<pre>"+library.json.prettyPrint(data)+"<pre>");
		
	var jsonHtmlTable = ConvertJsonToTable(dataEvents, 'JsonTable', null, 'Download');
		if (dataEvents==null)
			 return false;
			events_=dataEvents["event"];
		
			 for(var index=0;index<events_.length ;index++){
				// var time_label = new Array(events[i].time,events[i].label);
				// var event_=new Array(events_[index].time,events_[index].label);
				 eventtimearray.push(events_[index].time) ;
				 //events[events_[index].time]=events_[index].label;
				 events.push("<b>"+events_[index].time+"'</b> "+events_[index].label);
				 event_ticks.push(index*5);
				 ticks_time[index*5]=events_[index].time;
				// events.sort();
				// event_ticks.sort();
			 }
			 $("#ex14").bootstrapSlider({
				    value: 0,   
				    min:0,
				    max:events_.length*5,
				    enabled:true,
				    handle:'triangle',
				    step:5,
				   tooltip:'hide',
				 //   ticks: eventtimearray,
				   ticks:event_ticks,
				   ticks_labels:events,
				    orientation: 'vertical',
				    ticks_snap_bounds: 0,
					 // formatter: function(value_) { 
				    //var event
					//	return  value_ + " "+ (events[value_]!=null?events[value_]:"")	},
				});	 
			 
	});
	

	
	
			


			




$("#ex14").on("slideStop", function(slideEvt) {
	//var springlesserverURL = "http://localhost:8080/openrdf-sesame";
	//var restURL = "http://localhost:8080/SpringlesREST/rest/rest/";
	
	var url_=restURL+"playersfromtime";
	//var time=slideEvt.value;
	var time=	 ticks_time[slideEvt.value];
	var dataSend =dataSend_+"&time="+time ;
	
	
	$.ajax({
		url : url_playersfromtime , 
		data : dataSend, 
  contentType: "application/json; charset=utf-8",
  dataType: "json",
		type : "GET"
	
	}).done(function(data, textStatus, jqXHR) {			
		var risultato ="";
	//	var player = data['player'];
		$("#result").empty();
		$('#result').append("<pre>"+library.json.prettyPrint(data)+"<pre>");
		$("#request").empty();
		$('#request').html("<span>GET " + url_ + "</span><br />" + library.json.prettyPrint(dataSend));
		
		//	var risultato ="";  
	//	for (i = 0; i < player.length; i++) {
	//	    risultato = risultato + player[i].name +"<br />";
		    
	//	  }
		var stringa1a="";
		var stringa1b="";
		var stringa2a="";
		var stringa2b="";
		
		var ns="http://dkm.fbk.eu/ckr/live/brager-fifa14#";
		if (data==null)
		 return false;
		players=data["player"];
	//	$('#result').append("<table class='table table-striped'><thead><tr><th>Graphs</th><tr></thead><tbody>");
        for(var index=0;index<players.length ;index++){
            var  goalHostTeam=0;
            var goalHomeTeam=0;
            
            
            	for (index = 0; index < players.length; ++index) {
            		 if (players[index].teamtype=="HomeTeam"){
            			 goalHomeTeam=goalHomeTeam+parseInt(players[index].scoredGoal);
                        	
                         if (players[index].playing=="PlayingNow"){
                        	
                           stringa1a=stringa1a + "<li><b>"+players[index].number+"</b> "+players[index].name+ " <font color=\"grey\">("+players[index].position +")</font> "+showSubstitute(players[index].substitutionIn,substitutionList,"in")+showSubstitute(players[index].substitutionOut,substitutionList,"out") +showBall(parseInt(players[index].scoredGoal),players[index].scoredGoals,goalList)+showYellowCard(players[index].hasYCard,ycardList)+"</li>"
                           
                           
                         }else if(players[index].playing=="notPlayingNow"){
                        	 
                        	 stringa1b=stringa1b + "<li><b>"+players[index].number+"</b> "+players[index].name+" <font color=\"grey\">("+players[index].position +")</font> "+showSubstitute(players[index].substitutionIn,substitutionList,"in")+showSubstitute(players[index].substitutionOut,substitutionList,"out") +showBall(parseInt(players[index].scoredGoal),players[index].scoredGoals,goalList)+showYellowCard(players[index].hasYCard,ycardList)+"</li>"	 
                        	
                         }
            		 }else if (players[index].teamtype=="HostTeam"){
            			 goalHostTeam=goalHostTeam+parseInt(players[index].scoredGoal);
            			 if (players[index].playing=="PlayingNow"){
            		stringa2a=stringa2a + "<li><b>"+players[index].number+"</b> "+players[index].name+" <font color=\"grey\">("+players[index].position +")</font> " +showSubstitute(players[index].substitutionIn,substitutionList,"in")+showSubstitute(players[index].substitutionOut,substitutionList,"out")+showBall(parseInt(players[index].scoredGoal),players[index].scoredGoals,goalList)+showYellowCard(players[index].hasYCard,ycardList)+"</li>"	 
            		
            			 }else if(players[index].playing=="notPlayingNow"){
            				 stringa2b=stringa2b + "<li><b>"+players[index].number+"</b> "+players[index].name+" <font color=\"grey\">("+players[index].position +")</font> "+showSubstitute(players[index].substitutionIn,substitutionList,"in")+showSubstitute(players[index].substitutionOut,substitutionList,"out") +showBall(parseInt(players[index].scoredGoal),players[index].scoredGoals,goalList)+showYellowCard(players[index].hasYCard,ycardList)+"</li>"		 
            				 
            			 }          		 
            			 }
            }
        }
        document.getElementById("logol1a").innerHTML = stringa1a; 
        document.getElementById("logol1b").innerHTML = stringa1b; 
        document.getElementById("logol2a").innerHTML = stringa2a;
        document.getElementById("logol2b").innerHTML = stringa2b;
        $("#hometeamscore").html( goalHomeTeam);
        $("#hostteamscore").html( goalHostTeam);
          
        	//$('#result').append("<tr><td>"+data["player"][i].number+ "</td><td>"+data["player"][i].name+ "</td></tr>");
	
      //  $('#result').append("</tbody></table>");
		//$('#result').html("<span>OK " +risultato);
	
       
	}).fail(function(jqXHR, textStatus, errorThrown) { 
		$("#result").empty();
		$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
	});
});

$( "#selectteam1" ).change(function() {
    var str = "";
      str =  $( "#selectteam1 option:selected" ).text() ;   
	$("#team1H").html( str);
});

$( "#selectteam2" ).change(function() {
    var str = "";
      str =  $( "#selectteam2 option:selected" ).text() ;   
	$("#team2H").html( str);
});




   
});
 



$( window ).load(function() {
	var url_playersfromtime=restURL+"playersfromtime";
	//var time=slideEvt.value;
	var time=	 0;
	var dataSend =dataSend_+"&time="+time ;


	$.ajax({
		url : url_playersfromtime , 
		data : dataSend, 
	contentType: "application/json; charset=utf-8",
	dataType: "json",
		type : "GET"

	}).done(function(data, textStatus, jqXHR) {			
		var risultato ="";
//		var player = data['player'];
		$("#result").empty();
		$('#result').append("<pre>"+library.json.prettyPrint(data)+"<pre>");
		$("#request").empty();
		$('#request').html("<span>GET " + url_playersfromtime + "</span><br />" + library.json.prettyPrint(dataSend));
		
		//	var risultato ="";  
//		for (i = 0; i < player.length; i++) {
//		    risultato = risultato + player[i].name +"<br />";
		    
//		  }
		var stringa1a="";
		var stringa1b="";
		var stringa2a="";
		var stringa2b="";
		
		var ns="http://dkm.fbk.eu/ckr/live/brager-fifa14#";
		if (data==null)
		 return false;
		players=data["player"];
//		$('#result').append("<table class='table table-striped'><thead><tr><th>Graphs</th><tr></thead><tbody>");
	    for(var index=0;index<players.length ;index++){
	        var  goalHostTeam=0;
	        var goalHomeTeam=0;
	        
	        
	        	for (index = 0; index < players.length; ++index) {
	        		 if (players[index].teamtype=="HomeTeam"){
	        			 goalHomeTeam=goalHomeTeam+parseInt(players[index].scoredGoal);
	                    	
	                     if (players[index].playing=="PlayingNow"){
	                    	
	                       stringa1a=stringa1a + "<li><b>"+players[index].number+"</b> "+players[index].name+ " <font color=\"grey\">("+players[index].position +")</font></li>";
	                       
	                       
	                     }else if(players[index].playing=="notPlayingNow"){
	                    	 
	                    	 stringa1b=stringa1b + "<li><b>"+players[index].number+"</b> "+players[index].name+" <font color=\"grey\">("+players[index].position +")</font> </li>"	 
	                    	
	                     }
	        		 }else if (players[index].teamtype=="HostTeam"){
	        			 goalHostTeam=goalHostTeam+parseInt(players[index].scoredGoal);
	        			 if (players[index].playing=="PlayingNow"){
	        		stringa2a=stringa2a + "<li><b>"+players[index].number+"</b> "+players[index].name+" <font color=\"grey\">("+players[index].position +")</font> </li>"	 
	        		
	        			 }else if(players[index].playing=="notPlayingNow"){
	        				 stringa2b=stringa2b + "<li><b>"+players[index].number+"</b> "+players[index].name+" <font color=\"grey\">("+players[index].position +")</font> </li>"		 
	        				 
	        			 }          		 
	        			 }
	        }
	    }
	    document.getElementById("logol1a").innerHTML = stringa1a; 
	    document.getElementById("logol1b").innerHTML = stringa1b; 
	    document.getElementById("logol2a").innerHTML = stringa2a;
	    document.getElementById("logol2b").innerHTML = stringa2b;
	    $("#hometeamscore").html( goalHomeTeam);
	    $("#hostteamscore").html( goalHostTeam);
	      
	    	//$('#result').append("<tr><td>"+data["player"][i].number+ "</td><td>"+data["player"][i].name+ "</td></tr>");

	  //  $('#result').append("</tbody></table>");
		//$('#result').html("<span>OK " +risultato);

	   
	}).fail(function(jqXHR, textStatus, errorThrown) { 
		$("#result").empty();
		$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
	});
});
















function showSubstitute( time ,substitutionList, inout){
    if(time==null)
    	return "";
    
  //  return inout + substitutionList[time] + "'";
    
    return '<img src="./img/'+inout+'.jpg" alt="'+inout+'" height="25" width="20">'+ substitutionList[time] + "'";
    
    }

   function showBall( goal,goalStrings, goalTime){
	  
    if(goal==0){
    	return "";
   
    	
    }else{
    if (goal==1)	
    	 var result='<img src="./img/ball.png" alt="ball" height="25" width="25">'+goalTime[goalStrings.id]+"'";
    else{
    	var result='<img src="./img/ball.png" alt="ball" height="25" width="25">'+goalTime[goalStrings[0].id]+"'";
    
    for (i=1; i< goal;i++){
    	result=result+'<img src="./img/ball.png" alt="ball" height="25" width="25">'+goalTime[goalStrings[i].id]+"'";
    }
    }
    }
    return result;
   }
    
    
     function showYellowCard( time,ycardList){
    
    if(time==null) return "";
  
    
       return '<img src="./img/cartellino-giallo.jpg" alt="yellow" height="15" width="15">'+ ycardList[time]+"'";
  
    }
    
   // var events={75:"goal germania",46:"ammonizione"};