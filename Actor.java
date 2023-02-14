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
public class Actor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Actor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("\n======\nActor\n======");	
    	
		String actorID = request.getParameter("actorID");
		System.out.println("\nACTOR ID: " + actorID);
		String movieList = "<ul class=\"movieList\">\n";
		String colGroupString ="";
		String name="", character = "", id = "", picture = "", movies = "";
		String accountSection = "";
		
		// Retrieve signedInID
		boolean signedIn = false;
		String signedInID = "";
		if((request.getParameter("userInformation") == null || request.getParameter("userInformation").equals("null") || request.getParameter("userInformation").equals("")) && request.getAttribute("loggedInUserID") == null){
			System.out.println("No user logged in");
		} else {
			if(request.getAttribute("loggedInUserID") == null){
				signedInID = request.getParameter("userInformation");
			} else {
				signedInID = request.getAttribute("loggedInUserID").toString();
			}
			signedIn = true;
			System.out.println("User "+ signedInID + " logged in");
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
		 try {
			 Class.forName("com.mysql.jdbc.Driver");
			 //STEP 3: Open a connection
			 System.out.println("Connecting to database...");
			 conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
			 //STEP 4: Execute a query
			 System.out.println("Creating statement...");
			 stmt = (Statement) conn.createStatement();
			 String sql;
			 sql = "SELECT * FROM marveldb.actors WHERE actorID = "+ actorID +";";
//			 System.out.println(sql);
			 ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			 
		 	//Get actors
			 
			 while(rs.next()){
				 name = rs.getString("name");
				 character = rs.getString("character");
				 id = rs.getString("actorID");
				 picture = rs.getString("picture");
				 movies = rs.getString("movies");
				 
			 }
			 String moviesNew = movies.replace("\"", "");
			 String[] moviesArray = moviesNew.split(",");
			 
			 for(String value:moviesArray){
				 colGroupString = colGroupString + "<col>\n";
				 sql = "SELECT * FROM marveldb.movies WHERE movieID = " + value +";";
//				 System.out.println(sql);
				 rs = (ResultSet) stmt.executeQuery(sql);
				 while(rs.next()){
					 String movieTitle = rs.getString("title");
					 String movieID = rs.getString("movieID");
					 String year = rs.getString("year");
					 String moviePicture = rs.getString("picture");
					 String movieCard = "<form id=\"form" + movieID + "\"action=\"Movie\" method=\"post\"> "
				 				+ "<td id=\"movieCard\" onclick=\"document.getElementById('form" + movieID + "').submit()\">\n"
				 				+ "<input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"
				 				+ "<input type=\"hidden\" name=\"movieID\" value=\"" + movieID + "\"></input>\n"
				 				+ "<img id=\"moviePicture\" src=\"" + moviePicture + "\" width=\"175\" height=\"250\"  style=\"border-radius: 15px; object-fit: cover;\">\n<p id=\"movieTitle\">"+movieTitle+" ("+year+")</p>\n"
				 				+ "</td>\n"
				 				+ "</a>\n"
				 				+ "</form>";
					 movieList = movieList + movieCard;
				 }
			 }
			 
			// Account Section
			 if(signedIn == false){
				 accountSection = 
						 "	<form id=\"signUp\" class=\"topNavForm\" action=\"SignUp\" method=\"post\">"
						 + "	<button id=\"signUpButton\" type=\"submit\">Sign Up</button>"
						 + "</form>"+
						 "	<form id=\"logIn\" class=\"topNavForm\" action=\"Login\" method=\"post\">"
						 + "	<button id=\"logInButton\" type=\"submit\">Log In</button>"
						 + "</form>";
				 		
				 
			 } else {
				 sql = "SELECT * FROM marveldb.users WHERE userID = " + signedInID + ";";
				 System.out.println(sql);
				 rs = (ResultSet) stmt.executeQuery(sql);
				 while(rs.next()){
					 String userName = rs.getString("name");					 
					 accountSection =
							 "	<form id=\"logOut\" class=\"topNavForm\" action=\"Home\" method=\"post\">"
							 + "	<input type=\"hidden\" name=\"userInformation\" value=\"null\"</input>\n"
							 + "	<button id=\"logOutButton\" type=\"submit\">Log Out</button>"
							 + "</form>"
							 + "<div id=\"welcomeBack\">"
							 + "	<p>Welcome back, " + userName + "</p>"
							 + "</div>";
				 }
			 }
			 
		} catch (ClassNotFoundException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

		////////////////////////////////////
		response.setContentType("text/html;charset=UTF-8");
		final PrintWriter out = response.getWriter();
		
//		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
		out.println("<html>"
				+ "<head>"+
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8 width=device-width, initial-scale=1\">"+
				"    <link rel=\"stylesheet\" href=\"actor.css\">"+
				"    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>"+
				"    <title>Actor Page | Marvelous Movies</title>"+
				"</head>"+
				""+
				"<body>"+
				"	<div class=\"topNav\" id=\"myTopNav\">"+
				"		<form id=\"topNavLogo\" class=\"topNavForm\" action=\"Home\" method=\"post\">"+
				"       	<button id=\"imageButton\" name=\"userInformation\" value=\"" + signedInID +"\"><img src=\"MARVELOUS MOVIES.jpg\" height=\"50\"></button>"+
				"       </form>"+
				"       <form id=\"topNavSearch\" class=\"topNavForm\" action=\"Search\" method=\"post\">"+
				"            <div id=\"searchDiv\">"+
				"                <input id=\"searchInput\" name=\"searchResult\" placeholder=\"The Avengers\">"+
				"				 <input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"+
				"                <button id=\"searchButton\" type=\"submit\">SEARCH</button>"+
				"            </div>"+
				"        </form>"+
				accountSection +
				"    </div>"+
				"	<div class=\"content\">"+
				"    <div class=\"actorGrid\">"+
				"        <div class=\"actorPicture\">"+
				"            <img id=\"actorImage\" src=\"" + picture + "\" style=\"width: 350px; height: 400px; object-fit: cover; border-radius: 10px;\">"+
				"        </div>"+
				"        <div class=\"actorName\">"+
				"            <h1 id=\"actorName\">" + name + " - " + character + "</h1>"+
				"        </div>"+
				"        <div id=\"actorMovies\">"+
				"            <h2 style=\"text-decoration: underline\">Movies</h2>"
				+ "<div id=\"actorMoviesTable\">"
				+ "<table id=\"movieCardsTable\">"
				+ "<colgroup>"+ colGroupString+ "</colgroup>" + "<tbody>"
				+ "<tr class=\"classCardsRow\">"+ movieList + "</tr> </tbody> </table>"
				+ "</div>" +
				"        </div>"+
				"    </div>"
				+ "</div>"
				+ "<footer>"
				+ "	<p id=\"copyright\" class=\"footerText\">&copy; 2022 by Blake Lalonde</p>"
				+ "</footer>"+
				"</body>"
				+ "</html>"
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
