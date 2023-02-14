import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

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
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
    	boolean signedIn = false;
		String signedInID = "";
		
		
		System.out.println("\n======\nHome\n======");		
		if(request.getParameter("userInformation") == null || request.getParameter("userInformation").equals("null") || request.getParameter("userInformation").equals("")){
			System.out.println("No user logged in");
		} else {
			signedInID = request.getParameter("userInformation").toString();
			signedIn = true;
			System.out.println("User "+ signedInID + " logged in");
		}
		String actorTable = "";
		String movieTable = "";
		String top10 = "";
		String accountSection = "";
		
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
			 conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
			 //STEP 4: Execute a query
			 stmt = (Statement) conn.createStatement();
			 
			 
			 String sql;			 
			 // Build Top 10
			 sql = "SELECT * FROM marveldb.movies ORDER BY rating DESC LIMIT 10;";
			 System.out.println(sql);
			 ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			 while(rs.next()){
				 String title = rs.getString("title");
				 String id = rs.getString("movieID");
				 String picture = rs.getString("picture");
				 String rating = rs.getString("rating");
				 String movieCard =  "<td id=\"movieCard\" onclick=\"document.getElementById('form" + id + "').submit()\">\n"
						 		+ "<form id=\"form" + id + "\"action=\"Movie\" method=\"post\"> "
				 				+ "<input type=\"hidden\" name=\"userInformation\" value=\"" + signedInID + "\"</input>\n"
				 				+ "<input type=\"hidden\" name=\"movieID\" value=\"" + id + "\"</input>\n"
				 				+ "<img src=\"" + picture + "\" width=\"175\" height=\"250\" style=\"border-radius: 15px;\">\n<p>"+title+"</p>\n<p>"+rating+"%</p>"
				 				+ "</td>\n"
				 				+ "</form>";
				 top10 = top10 + movieCard;
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
		out.println(
				"<html>"+
						""+
						"<head>"+
						"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
						"	 <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
						+ "<meta property=\"og:site_name\" content=\"Marvelous Movies\" />" + 
						"	<meta property=\"og:title\" content=\"Marvelous Movies | Home\" />"+
						"	<meta property=\"og:type\" content=\"website\" />"+
						"	<meta property=\"og:url\" content=\"http://52.53.170.204/FirstServlet/Home\" />"
						+ "	<meta property=\"og:image\" content=\"https://ibb.co/L8ZkmPL\">"
						+ "<meta name=\"image\" property=\"og:image\" content=\"https://ibb.co/L8ZkmPL\">"+
						"	<meta property=\"og:description\" content=\"A catalog of all of the Infinity Saga MCU films and their actors!\" />"+
						"	<meta name=\"theme-color\" content=\"#f01018\">"+
						""+
//						"<!-- Include this to make the og:image larger -->"+
//						"<meta name=\"twitter:card\" content=\"summary_large_image\">" +
						"    <link rel=\"stylesheet\" href=\"home.css\">"+
						"    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>"+
						"    <title>Home | Marvelous Movies</title>"+
						"</head>"+
						""+
						"<body data-new-gr-c-s-check-loaded=\"14.1078.0\" data-gr-ext-installed=\"\" >"+
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
						"	<div class=\"content\">"
						+ "	<div id=\"introSection\">"
						+ "		<img id=\"bigMarvelousLogo\" src=\"MARVELOUS MOVIES.jpg\">"
						+ "		<h3 id=\"introText\">Welcome to Marvelous Movies, a catalog of all of the Infinity Saga MCU films and their actors!</h3>"
						+ "	</div>"
						+ "	<div id=\"rottenTomatoesHeader\">"
						+ "		<img id=\"rottenTomatoesLogo\" src=\"rotten tomatoes.png\">"+
						"		<h2 id=\"top10Header\"> Rotten Tomatoes Top-10 Rated Movies in the MCU:</h2>"
						+ "	</div>"+
						"   <div id=\"top10Div\">"+   
						"        <table id=\"top10\">"+
						"            <colgroup>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"                <col>"+
						"            </colgroup>"+
						"            <tbody>"+
						"                <tr class=\"top10header\">"+
						"                    <th>1</th>"+
						"                    <th>2</th>"+
						"                    <th>3</th>"+
						"                    <th>4</th>"+
						"                    <th>5</th>"+
						"                    <th>6</th>"+
						"                    <th>7</th>"+
						"                    <th>8</th>"+
						"                    <th>9</th>"+
						"                    <th>10</th>"+
						"                </tr>"+
						" 				 <tr>"+ top10 +
						"                </tr>"+
						"            </tbody>"+
						"        </table>"+
						"    </div>"
						+ "</div>"
						+ "<footer>"
						+ "	<p id=\"copyright\" class=\"footerText\">&copy; 2022 by Blake Lalonde</p>"
						+ "</footer>"+
						"<body>"
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
