import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AirportQuery {
	//Attributes
	static ResultSet rs;
	
	//Returning an arraylist of all of the airports
	public ArrayList<String> getAirports(Connection con) {
		try {
    		ArrayList<String> airportList = new ArrayList<String>();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery ("SELECT * FROM airport");
			while (rs.next ())
	        {
	            String airport = rs.getString ("county");
	            //System.out.println("County is : "+airport);
	            airportList.add(airport);
	        }
			stmt.close();
			return airportList;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	//Returning an airports county based on the input airport ID
	public String getAirportCounty(Connection con, String airportIDinput) {
		try {
			String airportCounty = "";
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery ("SELECT * FROM airport WHERE airportID = " + airportIDinput);
			
			if (rs.next()){
			    airportCounty = rs.getString("county");
			}

			stmt.close();
			return airportCounty;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	//Inserting a new airport into the airport table
	public String createAirport(Connection con, String county) {
		try {
			String query = "INSERT INTO airport (`county`) values ('" + county + "')";
			Statement stmt = con.createStatement();
			String ID = "";
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs.next()){
			    ID = rs.getString(1);
			}
			stmt.close();
			return ID;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	public void updateAirport(Connection con, String airportIDInput, String countyInput) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE airport SET county = \"" + countyInput + "\" WHERE airportID = " + airportIDInput);
			stmt.close();
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
}
