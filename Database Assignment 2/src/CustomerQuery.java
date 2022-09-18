import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerQuery {
	//Attributes
	static ResultSet rs;
	
	//Inserting a customer record based on the input received and returning 
	public String createUser(Connection con, String firstNameInput, String lastNameInput, String emailInput, String dateOfBirthInput, String eircodeInput) {
		try {
			String query = "Insert into customer (`firstName`, `lastname`, `email`, `dateOfBirth`, `eircode`) values ('" + firstNameInput + "', '" + lastNameInput + "', '" + emailInput + "', '" + dateOfBirthInput + "', '" + eircodeInput + "')";
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
	
	//Uses a select statement to get the customers information based on an input customer id, and returns an arraylist in the format firstName, lastName, email, dateOfBirth, eircode
	public ArrayList<String> getCustomerInfo(Connection con, String customerIDInput) {
		try {
			ArrayList<String> customerInfo = new ArrayList<String>();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery ("SELECT * FROM customer WHERE customerID = " + customerIDInput);
			
			while (rs.next ())
	        {
				customerInfo.add(rs.getString("firstName"));
				customerInfo.add(rs.getString("lastName"));
				customerInfo.add(rs.getString("email"));
				customerInfo.add(rs.getString("dateOfBirth"));
				customerInfo.add(rs.getString("eircode"));
	        }
			
			stmt.close();
			return customerInfo;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	public void updateEmail(Connection con, String customerIDInput, String emailInput) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE customer SET email = \"" + emailInput + "\" WHERE customerID = " + customerIDInput);
			stmt.close();
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
	
	public void deleteCustomer(Connection con, String customerIDInput) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM customer WHERE customerID = " + customerIDInput);
			stmt.close();
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
}
