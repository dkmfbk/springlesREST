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
		var includeinferred = $("#form13 #includeinferred:checked").length;
		var url_=oElements["restURL"]+"fullexport";
	 
	 //   var filetoupload=oElements["filetoupload"];
	    //var fileURI=oElements["fileURI"];
	
	    var documentData = new FormData();
	    documentData.append("springlesrepositoryID",springlesrepositoryID);
	    documentData.append("springlesserverURL",springlesserverURL);
	    documentData.append("contextURI",contextURI);
	    documentData.append("exportformat",exportformat);
        documentData.append("inludeinferred",includeinferred);
		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL+"&contextURI="+contextURI+
		"&exportformat="+exportformat + "&includeinferred="+includeinferred;
		
	    window.location.href = url_+"?"+dataSend;
	
		return false;
	});

});