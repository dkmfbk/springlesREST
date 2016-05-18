var closureStatus  = "STALE";

function showPanel(index){
    $(index).show();
}

function defineClickListerner(){
    
        $(".graph").click(function(){
                   var context = $(this).text();
                    var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o FROM <"+ context + "> WHERE { ?s ?p ?o }";
                    var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",serverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
		
                    //dataSend2 = dataSend2.replace("#","%23");
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2, 
                     contentType : false,
                  processData : false,
                  cache: false,
                      dataType: "html",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                            $("#result").empty();
			                 $('#result').html("<table class='table table-striped'><thead><tr><th colspan='3'>"+context+"</th></tr></thead>"+data + "</table>");
                        defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
    
        $(".s").click(function(){
                   var subject = $(this).text();
                     var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o WHERE { <"+subject+"> ?p ?o}";
                    var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",serverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2,contentType : false,
                      processData : false,
                      cache: false,
                      dataType: "html",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                            $("#result").empty();
			                 $('#result').html("<table class='table table-striped'><thead><tr><th colspan='3'>"+subject+"</th></tr></thead>"+data + "</table>");
                        defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
        $(".o").click(function(){
                   var obj = $(this).text();
                    var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o WHERE { ?s ?p <"+obj+"> }";
                   var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",serverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2, 
                      contentType : false,
                  processData : false,
                  cache: false,
                      dataType: "html",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                            $("#result").empty();
			                $('#result').html("<table class='table table-striped'><thead><tr><th colspan='3'>"+obj+"</th></tr></thead>"+data + "</table>");
                        defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
    $(".p").click(function(){
                   var pred = $(this).text();
                    var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o WHERE { ?s <"+pred+"> ?o }";
                   var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",serverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2, contentType : false,
      processData : false,
      cache: false,
                      dataType: "html",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                        
                            $("#result").empty();
			                 $('#result').html("<table class='table table-striped'><thead><tr><th colspan='3'>"+pred+"</th></tr></thead>"+data + "</table>");
                        defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });

}

function getClosureStatus(){
        var dataSend = "serverURL="+serverURL+"&repositoryID="+$("#repoChoice").val();
            $.ajax({
                    url : restURL + "getClosureStatus" , 
                    data : dataSend, 
                      contentType: "application/json; charset=utf-8",
                      dataType: "html",
                    type : "GET"

                    }).done(function(data, textStatus, jqXHR) {	
                        closureStatus = data;
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
}

$("#collapse19 #inferencer").on("change",function(){
    load_rulesets();
});