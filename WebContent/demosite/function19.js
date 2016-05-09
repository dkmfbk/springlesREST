$(function() {	
	$("#form19").submit(function(e) {
		//var queryType = "curatorsources"; 
		var oElements = {};
		$('#form19 [id]').each(function(){
		    oElements[this.id] = this.value;
		});

        
        
		var url_=restURL+"create_ruleset";
        var fileURI=oElements["fileURI"];
        var ruleset = oElements['file_ruleset'];
        
		var dataSend ="newfilename=" +fileURI+"&ruleset_path="+ruleset + "&inferencer="+$("#collapse19 #inferencer").val();
		
		e.preventDefault();
		$("#request").empty();
		$("#result").empty();
		$('#request').html("<span>GET " + url_ + "</span><br />" + library.json.prettyPrint(dataSend));

		$.ajax({
			 crossOrigin: true,
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(data);
            load_rulesets();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});