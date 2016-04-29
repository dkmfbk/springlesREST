$(function() {	
	$("#update_cl").click(function() {
		
		var springlesrepositoryID=$("#repoChoice").val();
		var url_=restURL+"computeclosure";

	
	    var documentData = new FormData();
	    documentData.append("springlesrepositoryID",springlesrepositoryID);
	    documentData.append("springlesserverURL",serverURL);
		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+serverURL;
		
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