$(function() {	
	$("#form12").submit(function(e) {
		//var queryType = "curatorsources";
		var oElements = {};
		 var filetoupload="";
		$('#form12 [id]').each(function(){
			//if (this.id=="filetoupload"){
			//	filetoupload=document.getElementsByName(this.id).files[0];
			//}else{
		    oElements[this.id] = this.value;
		//	}
		});
		var springlesserverURL= oElements["springlesserverURL"];
		var springlesrepositoryID=oElements["springlesrepositoryID"];
		var url_=oElements["restURL"]+"upload";
	 
	 //   var filetoupload=oElements["filetoupload"];
	    var fileURI=oElements["fileURI"];
		//var dataSend ="repositoryID=" +springlesrepositoryID+"&serverURL="+springleserverURL+"&fileURI="+fileURI;
	    var documentData = new FormData();
	    documentData.append("springlesrepositoryID",springlesrepositoryID);
	    documentData.append("springlesserverURL",springlesserverURL);
	    documentData.append("baseURI",fileURI);
	   documentData.append("filetoupload",$('input#filetoupload')[0].files[0]);
	 
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html("<span>OK " + jqXHR.status + " " + jqXHR.statusText + "</span><br />" + library.json.prettyPrint(data));
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});