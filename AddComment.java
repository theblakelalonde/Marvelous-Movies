import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.sql.*;

public class AddComment extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
	
        String comment = request.getParameter("commentTextArea");
        String rating = request.getParameter("rating");
        String movieID = request.getParameter("movieID");
        String signedInID = request.getParameter("signedInID");
        String errorInformation = "";
        
        System.out.println("===\nAttempting to add comment in AddComment...\ncomment: " + comment + "\nrating: " + rating + "\nmovieID: " + movieID + "\nsignedInID: " + signedInID);
        
        int highestID = 0;
        Statement stmt = null;
        boolean validComment = true;
        
        try {
        
            // loading drivers for mysql
            Class.forName("com.mysql.jdbc.Driver");
            
            //creating connection with the database 
            Connection con = DriverManager.getConnection
                        ("jdbc:mysql://localhost:3306/marvelDB","root","");
             stmt = (Statement) con.createStatement();
             String sql;
			 sql = "SELECT * FROM marveldb.comments;";
			 ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			 			 
			 if ((comment == "" || comment == null) || (rating == "" || rating == null) || movieID == "" || signedInID == "") {
				 System.out.println("ERROR: Invalid comment");
				 errorInformation = "Please fill out both the comment section and the rating box before submitting.";
				 validComment = false;
	            }
			 while(rs.next()){
				 int id = Integer.parseInt(rs.getString("commentID"));
				 if(id > highestID){
					 highestID = id;
				 }
//				 if(email.equals(rs.getString("email"))){
//					 System.out.println("Found duplicate email at user with id: " + id +"\t(email submitted: " + email +" - email found: " + rs.getString("email") +")");
//					 System.out.println("Setting validAccount to false");
//					 validAccount = false;
//					 break;
//				 }
			 }
			 highestID++;
			 
			 if (validComment){
				 PreparedStatement ps = con.prepareStatement
	                        ("insert into marveldb.comments values(" + highestID + ",?,?,?,?)");

	            ps.setString(1, comment);
	            ps.setString(2, signedInID);
	            ps.setString(3, movieID);
	            ps.setString(4, rating);
	            int i = ps.executeUpdate();
			 }
	            
	            
//	            if(i > 0) {
//	            	System.out.println("Added comment for user " + signedInID + " in movie " + movieID);
//	            	request.setAttribute("signedInID", signedInID);
//	            	request.setAttribute("movieID", movieID);
//	            	RequestDispatcher rd = getServletContext().getRequestDispatcher("/Movie");
//	            	rd.forward(request,response);
	            	
	            	response.setContentType("text/html;charset=UTF-8");
	        		final PrintWriter out = response.getWriter();
	        		
//	        		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
	        		
	        			out.println(
	        				"<html>"+
	        				"<body onload=\"document.forms[0].submit()\">"
	        				+ "	<form id=\"submitForm\" action=\"Movie\" method=\"post\">"
	        				+ "		<input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"
	        				+ "		<input type=\"hidden\" name=\"movieID\" value=\"" + movieID + "\"></input>\n"
	        				+ "		<input type=\"hidden\" name=\"errorInformation\" value=\"" + errorInformation + "\"></input>\n"
	        				+ "	</form>"
	        				+ "</body>"
	        				+ "</html>");
	        		
        
			         
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