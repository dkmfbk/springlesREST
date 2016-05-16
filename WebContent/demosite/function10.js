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
	   var querySPARQL= yasqe.getValue();
        var includeinferred = $("#form10 #includeinferred:checked").length;
        var dataSend = new FormData();
		dataSend.append("repositoryID",springlesrepositoryID);
        dataSend.append("serverURL",serverURL);
        dataSend.append("querySPARQL",querySPARQL);
        dataSend.append("includeinferred",includeinferred );
		
		
		$.ajax({
			url : url_ , 
			data : dataSend, 
            contentType : false,
      processData : false,
      cache: false,
			type : "POST"
		
		}).done(function(data, textStatus, jqXHR) {			
			$("#result").empty();
			$('#result').html("<table class='table table-striped'>"+data+"</table>");
            defineClickListerner();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});