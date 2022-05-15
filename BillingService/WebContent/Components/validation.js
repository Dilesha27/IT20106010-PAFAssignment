$(document).ready(function() 
{  
		$("#alertSuccess").hide();  
	    $("#alertError").hide(); 
}); 
 
 
// SAVE ============================================ 
$(document).on("click", "#btnSave", function(event) 
{  
	// Clear alerts---------------------  
	$("#alertSuccess").text("");  
	$("#alertSuccess").hide();  
	$("#alertError").text("");  
	$("#alertError").hide(); 
 
	// Form validation-------------------  
	var status = validateBillingForm();  
	if (status != true)  
	{   
		$("#alertError").text(status);   
		$("#alertError").show();   
		return;  
	} 
 
	// If valid------------------------  
	var type = ($("#hidBillingIDSave").val() == "") ? "POST" : "PUT"; 

	$.ajax( 
	{  
			url : "BillingAPI",   
			type : type,  
			data : $("#formBilling").serialize(),  
			dataType : "text",  
			complete : function(response, status)  
			{   
				onBillingSaveComplete(response.responseText, status);  
			} 
	}); 
}); 


function onBillingSaveComplete(response, status) 
{  
	if (status == "success")  
	{   
		var resultSet = JSON.parse(response); 

		if (resultSet.status.trim() == "success")   
		{    
			$("#alertSuccess").text("Successfully saved.");    
			$("#alertSuccess").show(); 

			$("#divBillingGrid").html(resultSet.data);   
		} else if (resultSet.status.trim() == "error")   
		{    
			$("#alertError").text(resultSet.data);    
			$("#alertError").show();   
		} 

	} else if (status == "error")  
	{   
		$("#alertError").text("Error while saving.");   
		$("#alertError").show();  
	} else  
	{   
		$("#alertError").text("Unknown error while saving..");   
		$("#alertError").show();  
	} 

	$("#hidBillingIDSave").val("");  
	$("#formBilling")[0].reset(); 
} 

 
// UPDATE========================================== 
$(document).on("click", ".btnUpdate", function(event) 
{     
	$("#hidBillingIDSave").val($(this).closest("tr").find('#hidBillingIDUpdate').val());     
	$("#AccNo").val($(this).closest("tr").find('td:eq(0)').text());     
	$("#mUsage").val($(this).closest("tr").find('td:eq(1)').text());     
	$("#mAmount").val($(this).closest("tr").find('td:eq(2)').text());     
}); 




//REMOVE===========================================
$(document).on("click", ".btnRemove", function(event) 
{  
	$.ajax(  
	{   
		url : "BillingAPI",   
		type : "DELETE",   
		data : "BillID=" + $(this).data("billingid"),   
		dataType : "text",   
		complete : function(response, status)   
		{    
			onBillingDeleteComplete(response.responseText, status);   
		}  
	}); 
}); 

function onBillingDeleteComplete(response, status) 
{  
	if (status == "success")  
	{   
		var resultSet = JSON.parse(response); 

		if (resultSet.status.trim() == "success")   
		{    
			
			$("#alertSuccess").text("Successfully deleted.");    
			$("#alertSuccess").show(); 
		
			$("#divBillingGrid").html(resultSet.data); 
			
		} else if (resultSet.status.trim() == "error")   
		{    
			$("#alertError").text(resultSet.data);    
			$("#alertError").show();   
		}
		

	} else if (status == "error")  
	{   
		$("#alertError").text("Error while deleting.");   
		$("#alertError").show();  
	} else  
	{   
		$("#alertError").text("Unknown error while deleting..");   
		$("#alertError").show();  
	}
}
 
// CLIENT-MODEL========================================================================= 
function validateBillingForm() 
{  
	// ACCOUNT NO  
	 var tmpAccNo = $("#AccNo").val().trim();
	if (!$.isNumeric(tmpAccNo)) 
		{
		return "Insert Account No.";
		} 

	// USAGE------------------------  
	 var tmpUsage = $("#mUsage").val().trim();
	if (!$.isNumeric(tmpUsage)) 
		{
		return "Insert Usage.";
		}
	
	//AMOUNT-------------------------------
	 var tmpAmount = $("#mAmount").val().trim();
	if (!$.isNumeric(tmpAmount)) 
	 {
	 return "Insert Amount.";
	 }

	return true; 
}