/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		DBproject esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new DBproject (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Ship");
				System.out.println("2. Add Captain");
				System.out.println("3. Add Cruise");
				System.out.println("4. Book Cruise");
				System.out.println("5. List number of available seats for a given Cruise.");
				System.out.println("6. List total number of repairs per Ship in descending order");
				System.out.println("7. Find total number of passengers with a given status");
				System.out.println("8. < EXIT");
				
				switch (readChoice()){
					case 1: AddShip(esql); break;
					case 2: AddCaptain(esql); break;
					case 3: AddCruise(esql); break;
					case 4: BookCruise(esql); break;
					case 5: ListNumberOfAvailableSeats(esql); break;
					case 6: ListsTotalNumberOfRepairsPerShip(esql); break;
					case 7: FindPassengersCountWithStatus(esql); break;
					case 8: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddShip(DBproject esql) {//1
	int ID;
	String make;
	String model;
	int age;
	int seats;
	String query;
	do{
		System.out.print("Enter in Ship ID: ");
		try{
			ID = Integer.parseInt(in.readLine());
			break;
		}catch (Exception e) {
			System.out.println("Your input is invalid! : " + e.getMessage());
			continue;
		}
	} while(true);
	
	do{
                System.out.print("Enter in Ship Make: ");
                try{
                        make = in.readLine();
			if(make.length() > 32 || make.length() <= 0)
			{
				throw new RuntimeException("Ship Make input is invalid");
			}
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);
	
	 do{
                System.out.print("Enter in Ship Model: ");
                try{
                        model = in.readLine();
                        if(model.length() > 64 || model.length() <= 0)
                        {
                                throw new RuntimeException("Ship Model input is invalid");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);
	 
	do{
                System.out.print("Enter in Ship Age: ");
                try{
                        age = Integer.parseInt(in.readLine());
                        if(age <= 0)
                        {
                                throw new RuntimeException("Ship Age input is invalid. Needs to be 0 or more");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);
	 do{
                System.out.print("Enter in number of seats on the Ship: ");
                try{
                        seats = Integer.parseInt(in.readLine());
                        if(seats <= 0 || seats > 500)
                        {
                                throw new RuntimeException("Ship Age input is invalid. Needs to be 0 or more or less than 500");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);

	//where we run the query
	try{
		query = "INSERT INTO Ship VALUES (" + ID + ", \'" + make + "\', \'" + model + "\', " + age + ", " + seats + ");";
		esql.executeUpdate(query); 
		
	}catch (Exception e)
	{
		 System.out.println("Your input is invalid! : " + e.getMessage());
                 
	}
}

	public static void AddCaptain(DBproject esql) {//2
	}

	public static void AddCruise(DBproject esql) {//3
	int cnum;
	int cost;
	int num_sold;
	int num_stops;
	String actual_departure_date;
	String actual_arrival_date;
	String arrival_port;
	String departure_port;	
	String query;
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	 do{
                System.out.print("Enter in Cruise Number: ");
                try{
                        cnum = Integer.parseInt(in.readLine());
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);

        do{
                System.out.print("Enter in Cruise Cost: ");
                try{
                        cost  = Integer.parseInt(in.readLine());
                        if(cost <= 0)
                        {
                                throw new RuntimeException("Ship Cost input is invalid. Must be larger than 0");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);
	
	do{
                System.out.print("Enter in number of tickets sold: ");
                try{
                        num_sold = Integer.parseInt(in.readLine());
                        if(num_sold < 0)
                        {
                                throw new RuntimeException("Ship Tickets sold input is invalid. Must be larger than -1");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);
	
	 do{
                System.out.print("Enter in number of stops: ");
                try{
                        num_stops = Integer.parseInt(in.readLine());
                        if(num_stops < 0)
                        {
                                throw new RuntimeException("Ship stops input is invalid. Must be larger than -1");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);

	 do{
                System.out.print("Enter in actual departure date in the format of yyyy-MM-dd HH:mm: ");
                try{
                        actual_departure_date = in.readLine();
			LocalDate localD = LocalDate.parse(actual_departure_date, dateFormat);
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);

	 do{
                System.out.print("Enter in actual arrival date in the format of yyyy-MM-dd HH:mm: ");
                try{
                        actual_arrival_date = in.readLine();
                        LocalDate localA = LocalDate.parse(actual_arrival_date, dateFormat);
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);
	
	 do{
                System.out.print("Enter in arrival port code: ");
                try{
                        arrival_port = in.readLine();
                        if(arrival_port.length() > 5 || arrival_port.length() <= 0)
                        {
                                throw new RuntimeException("Arrival port input is invalid");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);
	
	 do{
                System.out.print("Enter in departure port code: ");
                try{
                        departure_port = in.readLine();
                        if(departure_port.length() > 5 || departure_port.length() <= 0)
                        {
                                throw new RuntimeException("Arrival port input is invalid");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        } while(true);

	try{
		query = "INSERT INTO Cruise VALUES (" + cnum + ", " + cost + ", " + num_sold + ", " + num_stops + ", \'" + actual_departure_date + "\', \'" + actual_arrival_date + "\', \'" + arrival_port + "\', \'" + departure_port +"\');";
		esql.executeUpdate(query);
	}
	catch(Exception e)
	{
		System.out.println("Your Query failed! : " + e.getMessage());


	}
//update
	}

	public static void BookCruise(DBproject esql) {//4
		// Given a customer and a Cruise that he/she wants to book, add a reservation to the DB
	}

	public static void ListNumberOfAvailableSeats(DBproject esql) {//5
		// For Cruise number and date, find the number of availalbe seats (i.e. total Ship capacity minus booked seats )
		String query;
		String check1Query;
		int cnum;
		String date;
		int result = 1;
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		 do{
                System.out.print("Enter in Cruise Number you want: ");
                try{
                        cnum = Integer.parseInt(in.readLine());
                        if(cnum < 0)
                        {
                                throw new RuntimeException("cnum input is invalid");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
                } while(true);
	
		 do{
                System.out.print("Enter in the date in the format of yyyy-MM-dd HH:mm: ");
                try{
                        date = in.readLine();
                        LocalDate localA = LocalDate.parse(date, dateFormat);
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        	} while(true);

		try{
                    check1Query = "SELECT * FROM Cruise C WHERE C.cnum = " + cnum + " AND C.actual_departure_date =\' "+ date + "\';";
                        result = esql.executeQuery(check1Query);
                }
                catch(Exception e)
                {
                         System.out.println("Your Query failed! : " + e.getMessage());

                }
                if(result == 0)
                {
                        System.out.println("There is no cruise number " + cnum + " with that specific date in our database");
                        return;
                }
		

		try{
                        query = "SELECT COUNT(R.ccid) FROM Reservation R WHERE R.cid = " + cnum + " AND R.status = \'" + pass_stat + "\';";
                        esql.executeQueryAndPrintResult(query);

                }catch(Exception e)
                {
                         System.out.println("Your Query failed! : " + e.getMessage());
                }

		

		

	}

	public static void ListsTotalNumberOfRepairsPerShip(DBproject esql) {//6
		// Count number of repairs per Ships and list them in descending order
		String query;
		try{
			query = "SELECT S.id AS ShipID, count(R.rid) FROM Ship S, Repairs R WHERE S.id = R.ship_id GROUP BY S.id ORDER BY count DESC;";
			esql.executeQueryAndPrintResult(query);  	
		}catch(Exception e)
		{
			System.out.println("Your Query failed! : " + e.getMessage());
		}
	}

	
	public static void FindPassengersCountWithStatus(DBproject esql) {//7
		// Find how many passengers there are with a status (i.e. W,C,R) and list that number.
		int cnum;
		String check1Query;
		int result = 1;
		String pass_stat;
		String query;
		
		do{
                System.out.print("Enter in Cruise Number you want: ");
                try{
                        cnum = Integer.parseInt(in.readLine());
                        if(cnum < 0)
                        {
                                throw new RuntimeException("cnum input is invalid");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }		
        	} while(true);
		
		try{
		    check1Query = "SELECT * FROM Cruise C WHERE C.cnum = " + cnum + ";";
			result = esql.executeQuery(check1Query); 	
		}
		catch(Exception e)
		{
			 System.out.println("Your Query failed! : " + e.getMessage());
		 
		}
		if(result == 0)
		{
			System.out.println("There is no cruise number " + cnum + " in our database");
			return;
		}
		

		do{
                System.out.print("Enter in Passenger status: ");
                try{
                        pass_stat = in.readLine();
			//System.out.println( pass_stat.equals("W"));
                        if(!(pass_stat.equals("W") || pass_stat.equals("C") || pass_stat.equals("R"))) 
                        {
                                throw new RuntimeException("Passenger status input is invalid");
                        }
                        break;
                }catch (Exception e) {
                        System.out.println("Your input is invalid! : " + e.getMessage());
                        continue;
                }
        	} while(true);

		try{
			query = "SELECT COUNT(R.ccid) FROM Reservation R WHERE R.cid = " + cnum + " AND R.status = \'" + pass_stat + "\';";
			esql.executeQueryAndPrintResult(query);  			

		}catch(Exception e)
		{
			 System.out.println("Your Query failed! : " + e.getMessage());
		}
		
	
	}
}
