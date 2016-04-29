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
			                 $('#result').html("<table border='1'><tr><th colspan='3'>"+context+"</th></tr>"+data + "</table>");
                        defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
    
        $(".subj").click(function(){
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
			                 $('#result').html("<table border='1'><tr><th colspan='3'>"+subject+"</th></tr>"+data + "</table>");
                        defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
        $(".obj").click(function(){
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
			                $('#result').html("<table border='1'><tr><th colspan='3'>"+obj+"</th></tr>"+data + "</table>");
                        defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
    $(".pred").click(function(){
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
			                 $('#result').html("<table  border='1'><tr><th colspan='3'>"+pred+"</th></tr>"+data + "</table>");
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