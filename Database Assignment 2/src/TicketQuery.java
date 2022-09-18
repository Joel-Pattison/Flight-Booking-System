import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TicketQuery {
	//Attributes
	static ResultSet rs;
	
	public String createTicket(Connection con, String flightIdInput, String customerIdInput, String paymentIdInput, String numSeatsInput, String seatCostInput) {
		try {
			int ticketCost = Integer.parseInt(numSeatsInput) * Integer.parseInt(seatCostInput);
			String query = "INSERT INTO ticket (`flightID`, `customerID`, `paymentID`, `numSeats`, `ticketCost`) values ('" + flightIdInput + "', '" + customerIdInput + "', '" + paymentIdInput + "', '" + numSeatsInput + "', '" + ticketCost + "')";
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
	
	//Uses a select statement to get the ticket information based on an input ticket id, and returns an arraylist in the format customerID, flightID, paymentID, numSeats, ticketCost
	public ArrayList<String> getTicketInfo(Connection con, String ticketIdInput) {
		try {
			ArrayList<String> ticketInfo = new ArrayList<String>();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery ("SELECT * FROM ticket WHERE ticketID = " + ticketIdInput);
			
			while (rs.next ())
	        {
	            ticketInfo.add(rs.getString("customerID"));
	            ticketInfo.add(rs.getString("flightID"));
	            ticketInfo.add(rs.getString("paymentID"));
	            ticketInfo.add(rs.getString("numSeats"));
	            ticketInfo.add(rs.getString("ticketCost"));
	        }
			stmt.close();
			return ticketInfo;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	public void deleteTicket(Connection con, String customerIDInput) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM `payment information` WHERE customerID = " + customerIDInput);
			stmt.close();
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
}
