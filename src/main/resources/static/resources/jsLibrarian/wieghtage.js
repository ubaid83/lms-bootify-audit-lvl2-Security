$(document).ready(function() {
  var assignmentScore = new Array();
  var weightage = new Array();
  var testScore=new Array();
  var wieghtageTest=new Array();
  var finalValue;
  var finalValueTest;
   $("#courseId").on('change',function() {
					var selectedValue = $(this).val();
						window.location = '${pageContext.request.contextPath}/getAllAssignments?courseId='
								+ encodeURIComponent(selectedValue);
						return false;
					});
   
  
  
   finalValue = generateWeightedAverage('#inboxTable td',assignmentScore,weightage,'assignmentScore','weightage');
   
   finalValueTest = generateWeightedAverage('#outboxTable td',testScore,wieghtageTest,'testScore','wieghtageTest');
   
   
   
   $("#resultForAssignment").text(finalValue);
   
   $("#resultForTest").text(finalValueTest);
	
    $("#register-block").hide();
    
	$("#link-two").on("click" , function(){
		
        $("#register-block").show();
    });
    
    
    
    
    
   
});

function generateWeightedAverage(tableId,array1,array2,idText1,idText2){
	$(tableId).each(function(){
	  var idAttribute = $(this).attr('id');
	  
	  if(idAttribute!=undefined){
		 
		   if(idAttribute.includes(idText1)){
			  
			testScore.push($(this).text());
		   
		   }else if(idAttribute.includes(idText2)){
			   
			   
			wieghtageTest.push($(this).text());
		   }
			 
	  }

  });
  
  finalValue = calculateWegihtage(array1,array2);
  return finalValue;
	
}

function calculateWegihtage(x,y){
	var sum = 0;
for(var a =0;a<x.length;a++){

	var output = 0;
	output = performCalc(x[a],y[a]);
	sum = sum + output;
}
return sum;
}

function performCalc(a,b){
var calc = a*(b/100);
return calc;
}
