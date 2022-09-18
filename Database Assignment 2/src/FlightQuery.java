import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class FlightQuery {
	//Attributes
	static ResultSet rs;
	
	//returning an arraylist which contains all of the available flights based on the input airport ID
	public ArrayList<String> getFlightsByAirportID(Connection con, String airportIDInput) {
		try {
			ArrayList<String> flightList = new ArrayList<String>();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery ("SELECT * FROM flight WHERE airportID = " + airportIDInput +"");
			while (rs.next ())
	        {
	            String flightID = rs.getString ("FlightID");
	            String flightDestination = rs.getString ("destinationCounty");
	            String flightCost = rs.getString ("seatCost");
	            //System.out.println("County is : "+airport);
	            flightList.add(flightID);
	            flightList.add(flightDestination);
	            flightList.add(flightCost);
	        }
			stmt.close();
			return flightList;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	//Uses a select statement to get the flight information based on an input flight id, and returns an arraylist in the format airportID, destinationCountry, seatCost
	public ArrayList<String> getFlightInfo(Connection con, String flightIdInput) {
		try {
			ArrayList<String> flightInfo = new ArrayList<String>();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery ("SELECT * FROM flight WHERE flightID = " + flightIdInput);
			while (rs.next ())
	        {
				flightInfo.add(rs.getString("airportID"));
				flightInfo.add(rs.getString("destinationCounty"));
				flightInfo.add(rs.getString("seatCost"));
	        }
			stmt.close();
			return flightInfo;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	public String createFlight(Connection con, String airportIDinput, String destinationCountyInput, String seatCostInput) {
		try {
			String query = "INSERT INTO flight (`airportID`, `destinationCounty`, `seatCost`) values ('" + airportIDinput + "', '" + destinationCountyInput + "', '" + seatCostInput + "')";
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
	
	public void updateFlight(Connection con, String flightIDInput, String countyInput, String seatCostInput) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE flight SET county = \"" + countyInput + "\", seatCost = \"" + seatCostInput + "\" WHERE airportID = " + flightIDInput);
			stmt.close();
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
}
