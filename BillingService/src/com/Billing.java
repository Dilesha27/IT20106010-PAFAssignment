package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class Billing {
	
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/billservice?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertBilling(String AccNo, String mUsage, String amount)  
	{   
		String output = ""; 	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for inserting."; } 
	 
			// create a prepared statement 
			String query = " insert into bill(`BillID`,`AccNo`,`mUsage`,`mAmount`)" + " values (?, ?, ?, ?)"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			 preparedStmt.setInt(1, 0);
			 preparedStmt.setString(2, AccNo);
			 preparedStmt.setString(3, mUsage);
			 preparedStmt.setString(4, amount);
			
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	   
			String newBilling = readBilling(); 
			output =  "{\"status\":\"success\", \"data\": \"" + newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while inserting the billing.\"}";  
			System.err.println(e.getMessage());   
		} 		
	  return output;  
	} 	
	
	public String readBilling()  
	{   
		String output = ""; 
		try   
		{    
			Connection con = connect(); 
		
			if (con == null)    
			{
				return "Error while connecting to the database for reading."; 
			} 
	 
			// Prepare the html table to be displayed    
			output = "<table border=\'1\'><tr><th>Account No</th><th>Monthly Usage</th><th>Monthly Amount</th><th>Update</th><th>Remove</th></tr>";
	 
			String query = "select * from bill";    
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
	 
			// iterate through the rows in the result set    
			while (rs.next())    
			{     
				 String BillID = Integer.toString(rs.getInt("BillID"));
				 String AccNo = rs.getString("AccNo");
				 String mUsage = rs.getString("mUsage");
				 String amount = rs.getString("mAmount");
				 
				// Add into the html table 
				output += "<tr><td><input id=\'hidBillingIDUpdate\' name=\'hidBillingIDUpdate\' type=\'hidden\' value=\'" + BillID + "'>" 
							+ AccNo + "</td>"; 
				output += "<td>" + mUsage + "</td>";
				output += "<td>" + amount + "</td>";
	 
				// buttons     
				output +="<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"       
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-billingid='" + BillID + "'>" + "</td></tr>"; 
			
			}
			con.close(); 
	   
			output += "</table>";   
		}   
		catch (Exception e)   
		{    
			output = "Error while reading the billing.";    
			System.err.println(e.getMessage());   
		} 	 
		return output;  
	}
	
	public String updateBilling(String BillID, String AccNo, String mUsage, String amount)  
	{   
		String output = "";  
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for updating."; } 
	 
			// create a prepared statement    
			String query = "UPDATE bill SET AccNo=?,mUsage=?,mAmount=?"  + "WHERE BillID=?";  	 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setString(1, AccNo);
			 preparedStmt.setString(2, mUsage);
			 preparedStmt.setString(3, amount);
			 preparedStmt.setInt(4, Integer.parseInt(BillID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close();  
			String newBilling = readBilling();    
			output = "{\"status\":\"success\", \"data\": \"" + newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while updating the billing.\"}";   
			System.err.println(e.getMessage());   
		} 	 
	  return output;  
	} 
	
	public String deleteBilling(String BillID)   
	{   
		String output = ""; 
	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{
				return "Error while connecting to the database for deleting."; 			
			} 
	 
			// create a prepared statement    
			String query = "delete from bill where BillID=?"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setInt(1, Integer.parseInt(BillID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	 
			String newBilling = readBilling();    
			output = "{\"status\":\"success\", \"data\": \"" +  newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output = "Error while deleting the billing.";    
			System.err.println(e.getMessage());   
		} 
	 
		return output;  
	}
	
}
