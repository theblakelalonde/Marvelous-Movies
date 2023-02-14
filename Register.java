import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.sql.*;

public class Register extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
	
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String errorMessage = "";
        boolean validAccount = true;
        int highestID = 0;
        Statement stmt = null;
        
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
			 
			 if (email == "" || name == "" || password == "") {
	                validAccount = false;
	            }
			 while(rs.next()){
				 int id = Integer.parseInt(rs.getString("userID"));
				 if(id > highestID){
					 highestID = id;
				 }
				 if(email.equals(rs.getString("email"))){
					 System.out.println("Found duplicate email at user with id: " + id +"\t(email submitted: " + email +" - email found: " + rs.getString("email") +")");
					 System.out.println("Setting validAccount to false");
					 errorMessage = "Email already in use!";
					 validAccount = false;
					 break;
				 }
			 }
			 highestID++;
			 
			 if (validAccount){
				 PreparedStatement ps = con.prepareStatement
	                        ("insert into marveldb.users values(?,?,?," + highestID +")");

	            ps.setString(1, name);
	            ps.setString(2, email);
	            ps.setString(3, password);
	            int i = ps.executeUpdate();
	            
	            if(i > 0) {
	            	System.out.println("Added account for " + name);
	            	response.setContentType("text/html;charset=UTF-8");
	        		final PrintWriter out = response.getWriter();
	        		out.println("<!DOCTYPE html>"+
	        				"<html>"+
	        				"<body onload=\"document.forms[0].submit()\">"
	        				+ "	<form id=\"submitForm\" action=\"Home\" method=\"post\">"
	        				+ "		<input type=\"hidden\" name=\"userInformation\" value=\"" + highestID + "\"</input>\n"
	        				+ "	</form>"
	        				+ "</body>"
	        				+ "</html>");
	            }} else {
	            	System.out.println("Error adding account");
	            	response.setContentType("text/html;charset=UTF-8");
	        		final PrintWriter out = response.getWriter();
	        		out.println("<!DOCTYPE html>"+
	        				"<html>"+
	        				"<body onload=\"document.forms[0].submit()\">"
	        				+ "	<form id=\"submitForm\" action=\"SignUp\" method=\"post\">"
	        				+ "		<input type=\"hidden\" name=\"errorInformation\" value=\""+ errorMessage+"\"</input>\n"
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