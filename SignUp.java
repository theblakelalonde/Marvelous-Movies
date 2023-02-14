import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Homework2Retrieve
 */
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("\n======\nSignUp\n======");		
    	Boolean errorCheck = false;
    	String errorString = "";
    	
    	if(request.getParameter("errorInformation") != null){
    		if(request.getParameter("errorInformation") != ""){
	    		errorCheck = true;
	    		errorString =
	    				" <p id=\"errorMessage\">"
	    				+ request.getParameter("errorInformation").toUpperCase()
	    				+ "</p>";
	    		System.out.println(errorString);
    		}
    	}
			
		///////////////////////////////
		// JDBC driver name and database URL
		 String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		 //String DB_URL = "jdbc:mysql://52.26.86.130:3306/student";
		 String DB_URL = "jdbc:mysql://localhost:3306/marveldb";

		 String USER = "root";
		 String PASS = "";

		 Connection conn = null;
		 Statement stmt = null;


		////////////////////////////////////
		response.setContentType("text/html;charset=UTF-8");
		final PrintWriter out = response.getWriter();
		
//		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
		out.println("<head>"+
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8 width=device-width, initial-scale=1\">"+
				"    <link rel=\"stylesheet\" href=\"home.css\">"+
				"    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>"+
				"    <title>Sign-Up | Marvelous Movies</title>"+
				"</head>"+
				""+
				"<body>"+
				"	<div class=\"topNav\" id=\"myTopNav\">"+
				"		<form id=\"topNavLogo\" class=\"topNavForm\" action=\"Home\" method=\"post\">"+
				"       	<button id=\"imageButton\" name=\"userInformation\" value=\"null\"><img src=\"MARVELOUS MOVIES.jpg\" height=\"50\"></button>"+
				"       </form>"+
				"       <form id=\"topNavSearch\" class=\"topNavForm\" action=\"Search\" method=\"post\">"+
				"            <div id=\"searchDiv\">"+
				"                <input id=\"searchInput\" name=\"searchResult\" placeholder=\"The Avengers\">"+
				"				 <input type=\"hidden\" name=\"userInformation\" value=\"null\"</input>\n"+
				"                <button id=\"searchButton\" type=\"submit\">SEARCH</button>"+
				"            </div>"+
				"        </form>"+
				"	<form id=\"logIn\" class=\"topNavForm\" action=\"Login\" method=\"post\">"+
				 "		<button id=\"logInButton\" type=\"submit\">Log In</button>"
				 + "</form>"+
				"    </div>"+
				"	<div class=\"content\">"+
				"	<div id=\"signUpDiv\">"
				+ "		<h2>Sign Up!</h2>" +
				"    <form id=\"signupForm\" action=\"Register\" method=\"post\">"+
				"        <label id=\"nameLabel\">Name:</label><input name=\"name\" type=\"text\" id=\"nameBox\">"+
				"        <br>"+
				"        <label id=\"emailLabel\">Email:</label><input name=\"email\" type=\"email\" id=\"emailBox\">"+
				"        <br>"+
				"        <label id=\"passwordLabel\">Password:</label><input name=\"password\" type=\"password\" id=\"passwordBox\">"+
				"        <br>"+
				"        <br>"+
				"        <button id=\"signUpFormButton\" type=\"submit\">Sign Up</button>"+
				"    </form>"
				+ "	</div>"+ errorString
				+ "</div>" 
				+ "<footer>"
				+ "	<p id=\"copyright\" class=\"footerText\">&copy; 2022 by Blake Lalonde</p>"
				+ "</footer>"+
				"</body>"
		);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
