$(function() {	
	$("#summaryPanel").on("click",function() {
		//var queryType = "curatorsources";
		
		var springlesrepositoryID= $("#repoChoice").val();
		var url_=restURL+"summary";

	

		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL ;
		
	   
		$("#request").empty();
		$("#result").empty();
		
		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
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