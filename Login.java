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
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
    	
    	System.out.println("\n======\nLogIn\n======");		
    	Boolean errorCheck = false;
    	String errorString = "";
    	
    	if(request.getParameter("errorInformation") != null){
    		if(request.getParameter("errorInformation") != ""){
	    		errorCheck = true;
	    		errorString =
	    				" <p id=\"errorMessage\">"
	    				+ request.getParameter("errorInformation").toUpperCase()
	    				+ "</p>";
	    		System.out.println(request.getParameter("errorInformation"));
    		}
    	}
			
		///////////////////////////////
		// JDBC driver name and database URL
		 String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		 //String DB_URL = "jdbc:mysql://52.26.86.130:3306/student";
		 String DB_URL = "jdbc:mysql://localhost:3306/marveldb";

		 // Database credentials
		 String USER = "root";
		 String PASS = "";

		 Connection conn = null;
		 Statement stmt = null;
		 //STEP 2: Register JDBC driver
		 
		////////////////////////////////////
		response.setContentType("text/html;charset=UTF-8");
		final PrintWriter out = response.getWriter();
		
//		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
		out.println("<head>"+
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8 width=device-width, initial-scale=1\">"+
				"    <link rel=\"stylesheet\" href=\"home.css\">"+
				"    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>"+
				"    <title>Log In | Marvelous Movies</title>"+
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
				"	<form id=\"signUp\" class=\"topNavForm\" action=\"SignUp\" method=\"post\">"
				 + "	<button id=\"signUpButton\" type=\"submit\">Sign Up</button>"
				 + "</form>"+
				"    </div>" + 
				"	<div class=\"content\">"+
				"    <h2>Log In!</h2>"+
				""+
				""+
				"    <form id=\"signupForm\" action=\"Validate\" method=\"post\">"+
				"        <label id=\"emailLabel\">Email:</label><input name=\"email\" type=\"email\" id=\"emailBox\">"+
				"        <br>"+
				"        <label id=\"passwordLabel\">Password:</label><input name=\"password\" type=\"password\" id=\"passwordBox\">"+
				"        <br>"+
				"        <br>"+
				"        <button id=\"logInFormButton\"type=\"submit\">Log In</button>"+
				"    </form>" + errorString
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
