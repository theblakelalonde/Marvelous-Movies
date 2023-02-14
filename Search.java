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
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("\n======\nSearch\n======");	
		boolean signedIn = false;
		boolean actorFound = false;
		boolean movieFound = false;
		String signedInID = "";
		String accountSection = "";
		String actorID = "", actorPicture = "", actorName = "", actorCharacter = "", numOfActorCols = "", 
				actorTable = "<div class=\"tableSection\"><table class=\"actorTable\" id=\"actorTable\">"
						+ "	<colgroup>"
						+ "		<col id=\"actorTableImageCol\">"
						+ "		<col id=\"actorTableNameCol\">"
						+ "	</colgroup>"
						+ "	<th id=\"actorTableHeaderImage\" colspan=\"2\">Actors</th>";
		String movieID = "", moviePicture = "", movieTitle = "", movieYear = "", numOfMovieCols = "", 
				movieTable = "<div class=\"tableSection\"><table class=\"movieTable\" id=\"movieTable\">"
						+ "	<colgroup>"
						+ "		<col id=\"movieTableImageCol\">"
						+ "		<col id=\"movieTableNameCol\">"
						+ "	</colgroup>"
						+ "	<th id=\"movieTableHeaderImage\" colspan=\"2\">Movies</th>"; 
		String searchResult = request.getParameter("searchResult");
		System.out.println("searchResult = " + searchResult);
		
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
				
				// Retrieve signedInID

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
					 ResultSet rs = (ResultSet) stmt.executeQuery(sql);
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
			 
			 
			 // Search through actors
			 
			 sql = "SELECT * FROM marveldb.actors WHERE actors.character LIKE \"%" + searchResult + "%\" UNION SELECT * FROM marveldb.actors WHERE actors.name LIKE \"%" + searchResult + "%\" ORDER BY name ASC;";
			 ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			 while(rs.next()){
				 actorFound = true;
				 actorID = rs.getString("actorID");
				 actorPicture = rs.getString("picture");
				 actorName = rs.getString("name");
				 actorCharacter = rs.getString("character");
				 actorTable = actorTable 
						 + "<tr id=\"actorRow\" onclick=\"document.getElementById('actorRowForm" + actorID + "').submit()\">"
						 + "	<form class=\"actorRowForm\" id=\"actorRowForm" + actorID + "\" action=\"Actor\" method=\"post\">"
						 + "		<td id=\"actorPictureCell\">"
			 			 + "			<input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"
			 			 + "			<input type=\"hidden\" name=\"actorID\" value=\"" + actorID + "\"</input>\n"
						 + "			<img id=\"actorPicture\" src=\"" + actorPicture + "\">"
						 + "		</td>"
						 + "		<td id=\"actorNameCell\">"
						 + "			<p id=\"actorName\">" + actorName + " (" + actorCharacter +  ")</p>"
						 + "		</td>"
						 + "	</form>"
						 + "</tr>";
			 }
			 actorTable = actorTable + "</table></div>";
			 
		 	
			 // Search through movies 
			 
			 sql = " SELECT * FROM marveldb.movies WHERE title LIKE \"%" + searchResult + "%\" ORDER BY title ASC;";
			 rs = (ResultSet) stmt.executeQuery(sql);
	 		 while(rs.next()){
	 			 movieFound = true;
	 			movieID = rs.getString("movieID");
	 			moviePicture = rs.getString("picture");
	 			movieTitle = rs.getString("title");
	 			movieYear = rs.getString("year");
	 			movieTable = movieTable 
	 					+ "<tr id=\"movieRow\" onclick=\"document.getElementById('movieRowForm" + movieID + "').submit()\">"
						 + "	<form class=\"movieRowForm\" id=\"movieRowForm" + movieID + "\" action=\"Movie\" method=\"post\">"
						 + "		<td id=\"moviePictureCell\">"
			 			 + "			<input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"
			 			 + "			<input type=\"hidden\" name=\"movieID\" value=\"" + movieID + "\"</input>\n"
						 + "			<img id=\"moviePicture\" src=\"" + moviePicture + "\">"
						 + "		</td>"
						 + "		<td id=\"movieTitleCell\">"
						 + "			<p id=\"movieTitle\">" + movieTitle + " (" + movieYear +  ")</p>"
						 + "		</td>"
						 + "	</form>"
						 + "</tr>";
			 }
	 		movieTable = movieTable + "</table></div>";
	 		
	 		if(!movieFound){
	 			movieTable = "";
	 		}
	 		if(!actorFound){
	 			actorTable = "";
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
				"    <link rel=\"stylesheet\" href=\"search.css\">"+
				"    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>"+
				"    <title>Search | Marvelous Movies</title>"+
				"</head>"+
				""+
				"<body>"+
				"	<div class=\"topNav\" id=\"myTopNav\">"+
				"		<form id=\"topNavLogo\" class=\"topNavForm\" action=\"Home\" method=\"post\">"+
				"       	<button id=\"imageButton\" name=\"userInformation\" value=\"" + signedInID +"\"><img src=\"MARVELOUS MOVIES.jpg\" height=\"50\"></button>"+
				"       </form>"+
				"       <form id=\"topNavSearch\" class=\"topNavForm\" action=\"Search\" method=\"post\">"+
				"            <div id=\"searchDiv\">"+
				"                <input id=\"searchInput\" name=\"searchResult\" placeholder=\"" + searchResult + "\">"+
				"				 <input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"+
				"                <button id=\"searchButton\" type=\"submit\">SEARCH</button>"+
				"            </div>"+
				"        </form>"+
				accountSection +
				"    </div>"+
				"	<div class=\"content\">"+ actorTable + movieTable
				+ "	</div>"
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
