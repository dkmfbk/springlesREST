var repoID;
var restURL = "http://localhost:8080/SpringlesREST/rest/rest/";
var serverURL = "";
$(function() {	
	$("#loadRepo").on("click",function() {
		//var queryType = "curatorsources";
		var oElements = {};
		 var filetoupload="";
		
		var springlesserverURL= $("#form18 #springlesserverURL").val();
		var url_=restURL+"getRepositories";

	
	    var documentData = new FormData();
	    documentData.append("springlesserverURL",springlesserverURL);
		var dataSend ="springlesserverURL="+springlesserverURL ;
		serverURL = $("#form18 #springlesserverURL").val();

		$("#result").empty();

		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#repoChoice').html(data);
            $(".springlesrepositoryID").each(function(){
                $(this).html(data);
            });
            $(".hide-repo").each(function(){
                    $(this).show();
                });
            $("#summaryPanel").click();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});
