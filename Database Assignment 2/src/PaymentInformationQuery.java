import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PaymentInformationQuery {
	//Attributes
	static ResultSet rs;
	
	//Inserting a new payment information record for the input user ID
	public String addCustomerPaymentInformation(Connection con, String customerIdInput, String cardNumberInput, String cardHolderInput, String expiryDateInput, String cardCvcInput) {
		try {
			String query = "INSERT INTO `payment information` (`customerID`, `cardNumber`, `cardHolder`, `expiryDate`, `cvc`) values ('" + customerIdInput + "', '" + cardNumberInput + "', '" + cardHolderInput + "', '" + expiryDateInput + "', '" + cardCvcInput + "')";
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
	
	//Uses a select statement to get the customers payment information based on an input payment id, and returns an arraylist in the format cardNumber, cardHolder, ExpiryDate, cvc
	public ArrayList<String> getPaymentInfo(Connection con, String paymentIDInput) {
		try {
			ArrayList<String> paymentInfo = new ArrayList<String>();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery ("SELECT * FROM `payment information` WHERE paymentID = " + paymentIDInput);
			
			while (rs.next ())
	        {
				paymentInfo.add(rs.getString("cardNumber"));
				paymentInfo.add(rs.getString("cardHolder"));
				paymentInfo.add(rs.getString("expiryDate"));
				paymentInfo.add(rs.getString("cvc"));
	        }

			stmt.close();
			return paymentInfo;
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return null;
        }
	}
	
	public void deletePaymentInfo(Connection con, String paymentIDInput) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM `payment information` WHERE paymentID = " + paymentIDInput);
			stmt.close();
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
	
	public void updatecardNumber(Connection con, String cardInput, String paymentIDInput) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE `payment information` SET cardNumber = \"" + cardInput + "\" WHERE paymentID = " + paymentIDInput);
			stmt.close();
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
}
