$(function() {	
	$("#form10").submit(function(e) {
		//var queryType = "curatorsources";
		var oElements = {};
		$('#form10 [id]').each(function(){
		    oElements[this.id] = this.value;
		});
		var springleserverURL= serverURL;
		var springlesrepositoryID=$("#repoChoice").val();
		var url_=restURL+"querysparql";
	   var querySPARQL=oElements["querySPARQL"];
        var includeinferred = $("#form10 #includeinferred:checked").length;

		var dataSend ="repositoryID=" +springlesrepositoryID+"&serverURL="+serverURL+"&querySPARQL="+querySPARQL+"&includeinferred="+includeinferred ;
		
		
		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			$("#result").empty();
			$('#result').html("<table border='1'>"+data+"</table>");
            defineClickListerner();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});