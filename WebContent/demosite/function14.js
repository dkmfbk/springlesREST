$(function() {	
	$("#update_cl").click(function() {
		
		var springlesrepositoryID=$("#repoChoice").val();
		var url_=restURL+"computeclosure";

	
	    var documentData = new FormData();
	    documentData.append("springlesrepositoryID",springlesrepositoryID);
	    documentData.append("springlesserverURL",serverURL);
		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+serverURL +"&inferencer="+$("#closure_inferencer").val()+"&ruleset="+$("#closure_ruleset").val()+"&bindings="+$("#bindings").val()+"&inferredcontext="+$("#inferred_context_prefix").val();
		
	   if(closureStatus != "CURRENT")
            $.ajax({
                url : url_ , 
                data : dataSend, 
          contentType: "application/json; charset=utf-8",
          dataType: "html",
                type : "GET"

            }).done(function(data, textStatus, jqXHR) {			
                //var print = eval("(" + data + ')'); 
                $("#result").empty();
                $('#result').html(data);
                getClosureStatus();
            }).fail(function(jqXHR, textStatus, errorThrown) { 
                $("#result").empty();
                $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
            });
          else
                $('#result').html("Closure status is just CURRENT");
            
            return false;
        });
        
    $("#clear_cl").click(function() {
		
		var springlesrepositoryID=$("#repoChoice").val();
            var url_=restURL+"clear";


            var documentData = new FormData();
            documentData.append("springlesrepositoryID",springlesrepositoryID);
            documentData.append("springlesserverURL",serverURL);
            var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+serverURL + "&includenotinferred=0" ;


                $.ajax({
                    url : url_ , 
                    data : dataSend, 
              contentType: "application/json; charset=utf-8",
              dataType: "html",
                    type : "GET"

                }).done(function(data, textStatus, jqXHR) {			
                    //var print = eval("(" + data + ')'); 
                    $("#result").empty();
                    $('#result').html(data);
                    getClosureStatus();
                }).fail(function(jqXHR, textStatus, errorThrown) { 
                    $("#result").empty();
                    $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                });
             

                return false;
            });
  
});


function load_rulesets_closure(){
    var dataSend ="inferencer="+$("#closure_inferencer").val()+"&serverURL="+serverURL+"&repositoryID="+$("#repoChoice").val();
    $.ajax({
			 crossOrigin: true,
			url : restURL+"list_of_closure_ruleset" , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            
           $("#closure_ruleset").empty();
           for(var i=0;i< data.split('\n').length-1;i++){
                $("#closure_ruleset").append("<option val='"+data.split('\n')[i]+"' >"+data.split('\n')[i]+"</option>");
            }
    
                                 
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}

$("#closure_inferencer").on("change",function(){
    load_rulesets_closure();
});