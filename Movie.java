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
public class Movie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Movie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
    	
		System.out.println("\n======\nMovie\n======");	
    	
    	String movieID = "";
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
    	
    	
    	// Retrieve movieID
    	if((request.getParameter("movieID") == null || request.getParameter("movieID").equals("null") || request.getParameter("movieID").equals("")) &&  request.getAttribute("movieID") == null){
			System.out.println("error getting movieID or userID");
		} else {
			if(request.getAttribute("movieID") == null){
				movieID = request.getParameter("movieID");
			} else {
				movieID = request.getAttribute("movieID").toString();
			}
			System.out.println("Set movieID to: " + movieID);
		}
    	
		
		System.out.println("movieID ID: " + movieID);
		String actorList = "<ul class=\"actorList\">\n";
		String commentTable = "";
		String commentRows = "";
		String colGroupString ="";
		String addCommentSection = "";
		String accountSection = "";
		boolean checkComment = false;
		String title="", id = "", picture = "", cast = "", year="", video="";
		
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
//			 System.out.println("Connecting to database...");
			 conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
			 //STEP 4: Execute a query
//			 System.out.println("Creating statement...");
			 stmt = (Statement) conn.createStatement();
			 String sql;
			 sql = "SELECT * FROM marveldb.movies WHERE movieID = "+ movieID +";";
//			 System.out.println(sql);
			 ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			 
		 	//Get movie
			 while(rs.next()){
				 title = rs.getString("title");
				 picture = rs.getString("picture");
				 cast = rs.getString("cast");
				 String rating = rs.getString("rating");
				 year = rs.getString("year");
				 video = rs.getString("video");
				 video = video.replace('"', '\"');
			 }
			 String actorsNew = cast.replace("\"", "");
			 String[] actorsArray = actorsNew.split(",");
			 		 
			 sql = "SELECT * FROM marveldb.comments, marveldb.users WHERE movieID = " + movieID +" AND marveldb.users.userid = marveldb.comments.userID;";
//			 System.out.println(sql);
			 rs = (ResultSet) stmt.executeQuery(sql);
			 while(rs.next()){
				 checkComment = true;
				 String commentName = rs.getString("name");
				 String commentText = rs.getString("text");
				 String commentRating = rs.getString("rating");
				 commentRows = commentRows + 
						 "<tr>\n" +
						 "<td id=\"commentName\">" + commentName + "</td>\n" +
						 "<td id=\"commentText\">" + commentText + "</td>\n" +
						 "<td id=\"commentRating\">" + commentRating + " / 10</td>\n" +
						 "</tr>\n";
			 }
			 
			 if(checkComment == true){
				 commentTable = "    <table id=\"commentsTable\">"
							+ "<colgroup>"
							+ "<col class=\"userColumn\">"
							+ "<col class=\"textColumn\">"
							+ "<col class=\"ratingColumn\">"
							+ "</colgroup>"
							+ "<tbody>"
							+ "<tr class=\"commentsHeader\">"+
							"        <th id=\"userHeader\">User</th>"
							+ "<th id=\"commentHeader\">Comment</th>"
							+ "<th id=\"ratingHeader\">Rating</th>"
							+ "</tr>";
			 }
			 
			 for(String value:actorsArray){
				 colGroupString = colGroupString + "<col>\n";
				 sql = "SELECT * FROM marveldb.actors WHERE actorID = " + value +";";
				 System.out.println(sql);
				 rs = (ResultSet) stmt.executeQuery(sql);
				 while(rs.next()){
					 String name = rs.getString("name");
					 String actorID = rs.getString("actorID");
					 String actorPicture = rs.getString("picture");
					 String actorCard = "<form id=\"form" + actorID + "\"action=\"Actor\" method=\"post\"> "
					 				+ "<td id=\"actorCard\" onclick=\"document.getElementById('form" + actorID + "').submit()\">\n"
				 				    + "<input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"
					 				+ "<input type=\"hidden\" name=\"actorID\" value=\"" + actorID + "\"></input>\n"
					 				+ "<img id=\"actorImage\" src=\"" + actorPicture + "\" width=\"175\" height=\"250\"  style=\"border-radius: 15px; object-fit: cover;\">\n<p id=\"actorName\">"+name+"</p>\n"
					 				+ "</td>\n"
					 				+ "</a>\n"
					 				+ "</form>";
					 actorList = actorList + actorCard;
				 }
			 }
			 
			 // Comment Section
			 if(signedInID.equals("")){
				 addCommentSection = "";
			 } else {
				 System.out.println("User signed in, displaying comment box...");
				 addCommentSection = 
				  " <form id=\"commentForm\" action=\"AddComment\" method=\"post\">"+
				  "		<div id=\"addCommentSection\">"
				+ "			<textarea name=\"commentTextArea\" id=\"commentTextArea\" maxlength=\"1000\" placeholder=\"If you've seen this movie, feel free to leave a comment for others to view!\" rows=\"4\" cols=\"73\" style=\"resize: none;\"></textarea>"
				+ " 		<div id=\"ratingDiv\">"
				+ "				<label id=\"ratingLabel\">RATING</label>"
				+ "				<input id=\"ratingInputBox\" placeholder=\"1-10\" type=\"number\" min=\"1\" max=\"10\" name=\"rating\"/>"
				+ "			</div>"
				+ "			<button id=\"commentSubmitButton\" type=\"submit\">SUBMIT</button>"
				+ "			<input type=\"hidden\" name=\"movieID\" value=\"" + movieID + "\"></input>\n"
				+ "			<input type=\"hidden\" name=\"signedInID\" value=\"" + signedInID + "\"></input>\n"
				+ "		</div>"
				+ "	</form>";
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
//				 System.out.println(sql);
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
		out.println("<html>"+
				""+"<head>"+
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
				"    <link rel=\"stylesheet\" href=\"movies.css\">"+
				"    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>"+
				"    <title>Movie Page | Marvelous Movies</title>"+
				"</head>"+
				""+
				"<body onload=\"build()\">"+
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
				""+
				""+
				"	<div class=\"content\">"+
				"    <div class=\"movieGrid\">"+
				"        <div class=\"moviePicture\">"+
				"            <img id=\"movieImage\" src=\"" + picture + "\">"+
				"        </div>"
				+ "		<div class=\"movieVideo\">"
				+ "			<iframe id=\"embeddedTrailer\" src=\""+ video +
				"			\"></iframe> "
				+ "			</div>"+
				"        <div class=\"movieTitle\">"+
				"            <h1 id=\"movieTitle\">" + title + " (" + year + ")"+"</h1>"+
				"        </div>"+
				"        <div class=\"movieActors\">"+
				"            <h2 id=\"castHeader\">Cast</h2>"
				+ "		 <div id=\"movieActorsTable\">"
				+ "<table id=\"castCardsTable\">"
				+ "<colgroup>" + colGroupString + "</colgroup>"
						+ "<tbody>"
						+ "<tr class=\"classCardsRow\">"+ actorList + "</tr> </tbody> </table>"
								+ "</div>" +
				"        </div>"+
				"    </div>"+ 
				addCommentSection + errorString 
				+ commentTable + commentRows +
				"    </tbody></table>"
				+ "</div>"
				+ "<footer>"
				+ "	<p id=\"copyright\" class=\"footerText\">&copy; 2022 by Blake Lalonde</p>"
				+ "</footer>"+
				"<body>"
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
