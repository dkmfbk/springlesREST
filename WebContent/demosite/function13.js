$(function() {	
	$("#form13").submit(function(e) {
		//var queryType = "curatorsources";
		var oElements = {};
		 var filetoupload="";
		$('#form13 [id]').each(function(){
			//if (this.id=="filetoupload"){
			//	filetoupload=document.getElementsByName(this.id).files[0];
			//}else{
		    oElements[this.id] = this.value;
		//	}
		});
		var springlesserverURL= oElements["springlesserverURL"];
		var springlesrepositoryID=oElements["springlesrepositoryID"];
		var contextURI=escape(oElements["contextURI"]);
		var exportformat=oElements["exportformat"];
		
		var url_=oElements["restURL"]+"fullexport";
	 
	 //   var filetoupload=oElements["filetoupload"];
	    //var fileURI=oElements["fileURI"];
	
	    var documentData = new FormData();
	    documentData.append("springlesrepositoryID",springlesrepositoryID);
	    documentData.append("springlesserverURL",springlesserverURL);
	    documentData.append("contextURI",contextURI);
	    documentData.append("exportformat",exportformat);
		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL+"&contextURI="+contextURI+
		"&exportformat="+exportformat;
		
	    window.location.href = url_+"?"+dataSend;
	 //  documentData.append("filetoupload",$('input#filetoupload')[0].files[0]);
	 //   alert(url_);
	//	e.preventDefault();
	//	$("#request").empty();
	//	$("#result").empty();
	//	$('#request').html("<span>POST " + url_ + "</span><br />" + library.json.prettyPrint(documentData));
	//	$("#request").html("<span>POST " + url_ + "</span><br />" );
		//dataSend.fieldValueJsonString = JSON.stringify(oElements);
	 
	/*	$.ajax({
			
			 crossOrigin: true,
				url : url_ , 
				data : documentData, 
				 contentType : false,
			      processData : false,
			      cache: false,
				type : "POST"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			
			
			$("#result").empty();
			$('#result').html("<span>OK " + jqXHR.status + " " + jqXHR.statusText + "</span><br />" + library.json.prettyPrint(data));
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});*/
		return false;
	});

});