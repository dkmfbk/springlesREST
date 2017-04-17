    var springlesserverURL = "http://localhost:8080/openrdf-sesame";
	var restURL = "http://localhost:8080/SpringlesREST/rest/rest/";
	
//    var springlesserverURL = "http://madness:8080/openrdf-sesame";
//	var restURL = "http://madness:8080/SpringlesREST/rest/rest/";
	
	var springlesrepositoryID= "test-springles-5";
	var dataSend_ ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL  ;
	
	var url_playersfromtime=restURL+"playersfromtime";
	var url_getEvents=restURL+"eventlist";
$(document).ready(function(){
	
	var ticks_event=[0,11,46,58,68,90];
	
	var dataSend =dataSend_ ;
	var eventtimearray = new Array();
	var events=[];
// With JQuery
	$.ajax({
		url : url_getEvents , 
		data : dataSend, 
        contentType: "application/json; charset=utf-8",
        dataType: "json",
		type : "GET"
	
	}).done(function(data, textStatus, jqXHR) {	
		
		$("#result").empty();
		$('#result').append("<pre>"+library.json.prettyPrint(data)+"<pre>");
		$("#request").empty();
		$('#request').html("<span>GET " + url_getEvents + "</span><br />" + "<pre>"+library.json.prettyPrint(data)+"<pre>");
		
	var jsonHtmlTable = ConvertJsonToTable(data, 'JsonTable', null, 'Download');
		if (data==null)
			 return false;
			events_=data["event"];
			events_.sort();
			 for(var index=0;index<events_.length ;index++){
				// var time_label = new Array(events[i].time,events[i].label);
				// var event_=new Array(events_[index].time,events_[index].label);
				 eventtimearray.push(events_[index].time) ;
				 events[events_[index].time]=events_[index].label;
			 }
			 $("#ex14").bootstrapSlider({
				    value: 0,   
				    min:0,
				    max:90,
				    step:1,
				    ticks: eventtimearray,
				    ticks_snap_bounds: 200,
					  formatter: function(value) { 
				    var event
						return  value + " "+ (events[value]!=null?events[value]:"")	},
				});
	});
	

	
	
			


			




$("#ex14").on("slide", function(slideEvt) {
	var springlesserverURL = "http://localhost:8080/openrdf-sesame";
	var restURL = "http://localhost:8080/SpringlesREST/rest/rest/";
	
	var url_=restURL+"playersfromtime";
	var time=slideEvt.value;
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
            var goalHostTeam=0;
            var goalHomeTeam=0;
            
            
            	for (index = 0; index < players.length; ++index) {
            		 if (players[index].ttype=="HomeTeam"){
                        if (players[index].scoredGoal!=null)
                        	goalHomeTeam++;
                         if (players[index].playing=="PlayingNow"){
                           stringa1a=stringa1a + "<li>"+players[index].number+" "+players[index].name+" ("+players[index].position +") "+showSubstitute(players[index].substitutionOut) +" "+showSubstitute(players[index].substitutionIn) +" "+showBall(players[index].scoredGoal)+" "+showYellowCard(players[index].hasYCard)+"</li>"
                         }else{
                        	 
                        	 stringa1b=stringa1b + "<li>"+players[index].number+" "+players[index].name+" ("+players[index].position +") "+showSubstitute(players[index].substitutionOut) +" "+showSubstitute(players[index].substitutionIn) +" "+showBall(players[index].scoredGoal)+" "+showYellowCard(players[index].hasYCard)+"</li>"	 
                         }
            		 }else{
            			 if (players[index].scoredGoal!=null)
                         	goalHostTeam++;
            			 if (players[index].playing=="PlayingNow"){
            		stringa2a=stringa2a + "<li>"+players[index].number+" "+players[index].name+" ("+players[index].position +") "+showSubstitute(players[index].substitutionOut) +" "+showSubstitute(players[index].substitutionIn) +" "+showBall(players[index].scoredGoal)+" "+showYellowCard(players[index].hasYCard)+"</li>"	 
            		 
            			 }  else{
            				 stringa2b=stringa2b + "<li>"+players[index].number+" "+players[index].name+" ("+players[index].position +") "+showSubstitute(players[index].substitutionOut) +" "+showSubstitute(players[index].substitutionIn) +" "+showBall(players[index].scoredGoal)+" "+showYellowCard(players[index].hasYCard)+"</li>"		 
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

$("#ex14").on("slideoLd", function(slideEvt) {
         $("#ex14SliderVal").text(slideEvt.value);
  var players1 = [ ["12", "JULIO CESAR", "0","0","0"],
                 ["4", "DAVID LUIZ", "0","0","0"],
                 ["5", "FERNANDINHO", "46","0","0"],
               ];
               
var players2 = [["1", "NEUER", "0","0","0"],
                 ["4", "HOWEDES", "0","0","0"],
                 ["5", "HUMMELS", "46","0","0"],
                 ["6", "KHEDIRA", "76","0","46"],
                ["7", "SCHWEINSTEIGER", "0","75","0"],
               ];

var index;
var a = ["a", "b", "c"];  
    var stringa1="";
    var stringa2;
for (index = 0; index < players.length; ++index) {
  
         stringa1=stringa1 + "<li>"+players[index][0]+" "+players1[index][1]+" "+players1[index][2]+" "+showBall(players1[index][3])+" "+players1[index][4]+"</li>"
 }
 

 
  document.getElementById("logol1").innerHTML = "<li>Values just changed. min: " + slideEvt.value +"</li>"+stringa1;  
  document.getElementById("logol2").innerHTML = "<li>Values just changed. min: " + slideEvt.value + "</li>"+stringa2;
  


});
   



   
});
 
function showSubstitute( time ){
    if(time==null) return "";
    
    return "substitute " + time + "'";
    
	
    }

   function showBall( time){
    if(time==null) return "";
    
    return '<img src="./img/ball.png" alt="ball" height="22" width="22">'
    
	
    }
    
     function showYellowCard( time){
    
    if(time==null) return "";
  
    
       return '<img src="./img/cartellino-giallo.jpg" alt="yellow" height="12" width="12"> '+time+"'"
  
    }
    
   // var events={75:"goal germania",46:"ammonizione"};