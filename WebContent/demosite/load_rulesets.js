function load_rulesets(){
    var dataSend ="inferencer="+$("#collapse19 #inferencer").val()+"&serverURL="+serverURL+"&repositoryID="+$("#repoChoice").val();
    $.ajax({
			 crossOrigin: true,
			url : restURL+"list_of_ruleset" , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            $("#list_of_rulesets").empty();
            $("#list_of_rulesets").append(data);
            getRuleset($(".ruleset").get(0).id);
            $(".ruleset").click(function(){
                var index=$(".ruleset").index(this);
                getRuleset($(".ruleset").get(index).id);
            });
            $(".delete").click(function(){
                var index=$(".delete").index(this);
                var ruleset = $(".ruleset").get(index).id;
                deleteRuleset(ruleset);
                
            });
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}


function getRuleset(name){
    $.ajax({
            
			 crossOrigin: true,
			url : restURL+"content_of_ruleset" , 
			data : "filename="+name, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            $("#result").append(data);
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}

function deleteRuleset(name){
    $.ajax({
            
			 crossOrigin: true,
			url : restURL+"delete_ruleset" , 
			data : "filename="+name+"&inferencer="+$("#collapse19 #inferencer").val(), 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            $("#result").append(data);
            load_rulesets();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}

function load_rulesets_closure(){
    var dataSend ="inferencer="+$("#collapse14 #inferencer").val()+"&serverURL="+serverURL+"&repositoryID="+$("#repoChoice").val();
    $.ajax({
			 crossOrigin: true,
			url : restURL+"list_of_ruleset" , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            $(".ruleset").click(function(){
                var index=$(".ruleset").index(this);
                getRuleset($(".ruleset").get(index).id);
            });
            $(".delete").click(function(){
                var index=$(".delete").index(this);
                var ruleset = $(".ruleset").get(index).id;
                deleteRuleset(ruleset);
                
            });
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}