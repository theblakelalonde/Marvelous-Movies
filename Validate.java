import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.sql.*;

public class Validate extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
	
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean validLogin = false;
        String errorMessage = "";
        Statement stmt = null;
        int id = -1;
        
        try {
        
            // loading drivers for mysql
            Class.forName("com.mysql.jdbc.Driver");
            
            //creating connection with the database 
            Connection con = DriverManager.getConnection
                        ("jdbc:mysql://localhost:3306/marvelDB","root","");
             stmt = (Statement) con.createStatement();
             String sql;
			 sql = "SELECT * FROM marveldb.users;";
			 System.out.println(sql);
			 ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			 
			 if (email == "" || password == "") {
	                validLogin = false;
	            }
			 while(rs.next()){
				 id = Integer.parseInt(rs.getString("userID"));
				 if(email.equals(rs.getString("email"))){
					 System.out.println("emails match");
					 if(password.equals(rs.getString("password"))){
						 System.out.println("passwords match\n sending user " + id + " back to home page");
						 validLogin = true;
						 break;
					 }
					 System.out.println("passwords do not");
				 }
			 }
			 
			 if (validLogin){     
				 	System.out.println("VALID LOGIN");     	
	            	response.setContentType("text/html;charset=UTF-8");
	        		final PrintWriter out = response.getWriter();
	        		
	        		out.println("<!DOCTYPE html>"+
	        				"<html>"+
	        				"<body onload=\"document.forms[0].submit()\">"
	        				+ "	<form id=\"submitForm\" action=\"Home\" method=\"post\">"
	        				+ "		<input type=\"hidden\" name=\"userInformation\" value=\"" + id + "\"</input>\n"
	        				+ "	</form>"
	        				+ "</body>"
	        				+ "</html>");
			 } else {
				 System.out.println("Error logging in");
	            	response.setContentType("text/html;charset=UTF-8");
	        		final PrintWriter out = response.getWriter();
	        		errorMessage = "Invalid login. Please try again.";
	        		out.println("<!DOCTYPE html>"+
	        				"<html>"+
	        				"<body onload=\"document.forms[0].submit()\">"
	        				+ "	<form id=\"submitForm\" action=\"Login\" method=\"post\">"
	        				+ "		<input type=\"hidden\" name=\"errorInformation\" value=\""+ errorMessage+ "\"</input>\n"
	        				+ "	</form>"
	        				+ "</body>"
	        				+ "</html>");
			 }
        }
        catch(Exception se) {
            se.printStackTrace();
        }
	
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}