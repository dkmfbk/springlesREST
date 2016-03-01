$(function() {
	
	var input = $("<input class='form-control'>");
	var helper = $("<p class='help-block'></p>");
	
	$("#form1").submit(function(e) {
		var queryType = $( "#semanticInsertQuery option:selected" ).get(0).value;
		var oElements = {};
		$('#form1 [name]').each(function(){
		    oElements[this.name] = this.value;
		});
		var server=$( "#serverChoice option:selected" ).get(0).value;
		var url_=server+url;
		var dataSend = 
			'processID=1&queryType='+queryType+'&inputParameters='+JSON.stringify(oElements);
		
		e.preventDefault();
		$("#request").empty();
		$("#result").empty();
		$('#request').html("<span>GET " + url + "</span><br />" + library.json.prettyPrint(dataSend));
		dataSend.fieldValueJsonString = JSON.stringify(oElements);
  //  alert(url+dataSend.processID);
		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "json",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html("<span>OK " + jqXHR.status + " " + jqXHR.statusText + "</span><br />" + library.json.prettyPrint(print));
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});
	
	$("#semanticInsertQuery").change(function(){
		$("#inputPrint").find("input").remove();
		$("#inputPrint").find(".help-block").remove();
		var selected = $( "#semanticInsertQuery option:selected" ).get(0).value;
		var inputToPrint = arrayInsertQuery[selected];
		$.each(inputToPrint, function(index, value){
			var input2 = input.clone();
			var helper2 = helper.clone();
			$("#inputPrint").append(input2);
			$("#inputPrint").append(helper2);
			$(input2).attr("name", index);
			$(input2).attr("value", value);
			$(input2).attr("placeholder", "Insert " + index);
			$(helper2).text("Example " + index + ": " + value);
		});
	});
	
	

});

var arrayInsertQuery = new Array();
arrayInsertQuery["insertkbelementpoi"] = {
		"name" : "Centro Servizi Culturali S.Chiara",
		"externalPage" : "http://www.centrosantachiara.it/",
		"longitude" : "11.453953",
		"latitude" : "46.053198",
		"altitude" : " 392",
		"address" : " Via S. Croce, 67 ,38122  Trento",
		"phoneNumber" : " 0461 213811",
		"parentLocation" : ":trento",
		"poiCategory" : ":cat_teatro"
		
};
arrayInsertQuery["insertkbelementperson"] = {
    "name" : "Max Gazze",
		"wikipediaArticle" : "http://it.wikipedia.org/wiki/Max_Gazze",
    "externalPage" : "http://www.maxgazze.com/",
		"description" : "Max Gazze (Roma, 6 luglio 1967)"
};


