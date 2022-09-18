import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	// Creating a Scanner object
	public static Scanner myObj = new Scanner(System.in);
	
	//Creating the database object to initialise the database
	static CustomerQuery customerTable = new CustomerQuery();
	static AirportQuery airportTable = new AirportQuery();
	static FlightQuery flightTable = new FlightQuery();
	static PaymentInformationQuery paymentInformationTable = new PaymentInformationQuery();
	static TicketQuery ticketTable = new TicketQuery();
	
	//Attributes
	private static Connection con;

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/r00206715_flightbooking?user=root&password=root" );
			
		}
		catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
		
		
		//Asking the user to choose to either create a ticket or view/edit a ticket
		String option;
		do {
			System.out.println("Please select an option: 1. Purchase a ticket 2. View/edit ticket details 3. Manage airports / flights: ");
			option = myObj.nextLine();
		} while(Integer.parseInt(option) != 1 && Integer.parseInt(option) != 2 && Integer.parseInt(option) != 3);
		
		if (Integer.parseInt(option) == 1) {
			purchaseTicket();
		}
		else if (Integer.parseInt(option) == 2){
			manageTicket();
		}
		else {
			manageAirports();
		}
		
	}
	
	public static void purchaseTicket() {
		//Attributes
		String firstName, lastName, email, dateOfBirth, eircode, chosenAirportID, chosenFlightID, seats, currentUserID, currentUserpaymentInformationID, chosenTicketID;
		
		//Prompting the user to enter their personal information
		do {
			System.out.println("Please enter your first name: ");
			firstName = myObj.nextLine();
		} while(!(firstName.length() > 0 && firstName.length() < 20));
		
		do {
			System.out.println("Please enter your last name: ");
			lastName = myObj.nextLine();
		} while(!(lastName.length() > 0 && lastName.length() <= 20));
		
		do {
			System.out.println("Please enter your email: ");
			email = myObj.nextLine();
		} while(!(lastName.length() > 0 && lastName.length() < 20) && email.contains("@"));
		
		do {
			System.out.println("Please enter your date of birth (YYYY-MM-DD): ");
			dateOfBirth = myObj.nextLine();
		} while(!(lastName.length() > 0 && lastName.length() <= 10));
		
		do {
			System.out.println("Please enter your eircode: ");
			eircode = myObj.nextLine();
		} while(!(lastName.length() > 0 && lastName.length() <= 7));
		
		//Storing the users information in a new entry in the customer table and receiving the created users ID
		currentUserID = customerTable.createUser(con, firstName, lastName, email, dateOfBirth, eircode);
		
		//Retrieving the list of airports from the database
		ArrayList<String> airportList = airportTable.getAirports(con);
		
		//Printing out a list of available airports and prompting the user to select one
		System.out.print("Please select an airport you would like to depart from:");
		for (int i = 0; i < airportList.size(); i++) {
			int currentID = i + 1;
			System.out.print(" " + currentID + ". " + airportList.get(i));
		}
		System.out.println();
		chosenAirportID = myObj.nextLine();
		
		//Retrieving the list of flights from the database based on the chosen airport, the data in the arraylist returned is formatted as flightID > flightDestination > flightCost
		ArrayList<String> unparsedFlightList = flightTable.getFlightsByAirportID(con, chosenAirportID);
		
		//Organising flight list into IDs destinations and costs for easier management and readability
		ArrayList<String> flightListIds = new ArrayList<String>();
		ArrayList<String> flightListdestinations = new ArrayList<String>();
		ArrayList<String> flightListCosts = new ArrayList<String>();
		
		for (int i = 0; i < unparsedFlightList.size(); i += 3) {
			flightListIds.add(unparsedFlightList.get(i));
			flightListdestinations.add(unparsedFlightList.get(i + 1));
			flightListCosts.add(unparsedFlightList.get(i + 2));
		}
		
		
		//Printing out a list of available flights departing and their prices from the chosen airport and prompting the user to select one
		System.out.print("Please select a flight:");
		int currentID = 0;
		for (int i = 0; i < flightListIds.size(); i++) {
			currentID = i + 1;
			System.out.print(" " + currentID + ". " + flightListdestinations.get(i) + " (€" + flightListCosts.get(i) + ")");
		}
		System.out.println();
		String chosenFlight = myObj.nextLine();
		chosenFlightID = flightListIds.get(Integer.parseInt(chosenFlight) - 1);
		
		//Prompting user for the number of seats they would like to book and their credit card information
		System.out.println("How many seats would you like to book?:");
		seats = myObj.nextLine();
		System.out.println("Please enter a credit card number:");
		String cardNumber = myObj.nextLine();
		System.out.println("Please enter a credit card CVC:");
		String cardCVC = myObj.nextLine();
		System.out.println("Please enter the card holders name:");
		String cardName = myObj.nextLine();
		System.out.println("Please enter the cards expiry date:");
		String cardExpiry = myObj.nextLine();
		
		//Storing the users card information in a new entry in the payment information table and receiving the created payment information ID
		currentUserpaymentInformationID = paymentInformationTable.addCustomerPaymentInformation(con, currentUserID, cardNumber, cardName, cardExpiry, cardCVC);
		
		//Creating a ticket based on all of the information collected
		chosenTicketID = ticketTable.createTicket(con, chosenFlightID, currentUserID, currentUserpaymentInformationID, seats, flightListCosts.get(Integer.parseInt(chosenFlight) - 1));
		
		System.out.println("Thank you for your purchase, your ticket ID is " + chosenTicketID + ". If you would like to view or edit your ticket at any point, you will need to input this ID");
	}
	
	public static void manageTicket() {
		//Attributes
		String ticketID, ticketCustomerID, ticketFlightID, ticketPaymentID, ticketNumSeats, ticketCost, ticketAirportID, ticketDestinationCountry, ticketSeatCost, 
		ticketAirportCounty, ticketFirstName, ticketLastName, ticketEmail, ticketDOB, ticketEircode, ticketCardNumber, ticketCardHolder, ticketExpiryDate, ticketCvc;
		
		//Prompting the user to input their ticket ID
		System.out.println("Please enter your ticket ID");
		ticketID = myObj.nextLine();
		
		//Getting the ticket info based on the provided ticket id and receiving an arraylist in the format customerID, flightID, paymentID, numSeats, ticketCost
		ArrayList<String> ticketInfo = ticketTable.getTicketInfo(con, ticketID);
		
		//Parsing through and sorting the received arraylist
		ticketCustomerID = ticketInfo.get(0);
		ticketFlightID = ticketInfo.get(1);
		ticketPaymentID = ticketInfo.get(2);
		ticketNumSeats = ticketInfo.get(3);
		ticketCost = ticketInfo.get(4);
		
		//Getting the flight info based on the provided flight id and receiving an arraylist in the format airportID, destinationCountry, seatCost
		ArrayList<String> flightInfo = flightTable.getFlightInfo(con, ticketFlightID);
		
		//Parsing through and sorting the received arraylist
		ticketAirportID = flightInfo.get(0);
		ticketDestinationCountry = flightInfo.get(1);
		ticketSeatCost = flightInfo.get(2);
		
		//Getting the airport county
		ticketAirportCounty = airportTable.getAirportCounty(con, ticketAirportID);
		
		//Getting the customers info based on the provided customer id and receiving an arraylist in the format firstName, lastName, email, dateOfBirth, eircode
		ArrayList<String> customerInfo =  customerTable.getCustomerInfo(con, ticketCustomerID);
		
		//Parsing through and sorting the received arraylist
		ticketFirstName = customerInfo.get(0);
		ticketLastName = customerInfo.get(1);
		ticketEmail = customerInfo.get(2);
		ticketDOB = customerInfo.get(3);
		ticketEircode = customerInfo.get(4);
		
		//Getting the customers payment info based on the payment id and receiving an arraylist in the format cardNumber, cardHolder, ExpiryDate, cvc
		ArrayList<String> paymentInfo =  paymentInformationTable.getPaymentInfo(con, ticketPaymentID);
		
		//Parsing through and sorting the received arraylist
		ticketCardNumber = paymentInfo.get(0);
		ticketCardHolder = paymentInfo.get(1);
		ticketExpiryDate = paymentInfo.get(2);
		ticketCvc = paymentInfo.get(3);
		
		//Printing ticket
		System.out.println("--------------------Ticket ID: " + ticketID + "--------------------");
		System.out.println("Ticket Holder Information");
		System.out.println("Name: " + ticketFirstName + " " + ticketLastName);
		System.out.println("Email: " + ticketEmail);
		System.out.println("DOB: " + ticketDOB);
		System.out.println("Eircode: " + ticketEircode);
		System.out.println("");
		System.out.println("Payment Information");
		System.out.println("Card Number: " + ticketCardNumber);
		System.out.println("Card Holder: " + ticketCardHolder);
		System.out.println("Card Expiry Date: " + ticketExpiryDate);
		System.out.println("Card CVC: " + ticketCvc);
		System.out.println("");
		System.out.println("Flight Information");
		System.out.println("Departing from: " + ticketAirportCounty);
		System.out.println("Arriving at: " + ticketDestinationCountry);
		System.out.println("Number of seats: " + ticketNumSeats);
		System.out.println("Seat Cost: " + ticketSeatCost);
		System.out.println("Total Cost: " + ticketCost);
		
		//Asking the user to choose to one of the options.
		String option;
		do {
			System.out.println("Please select an option: 1. Update Email 2. Update payment card number 3. Cancel ticket 4. return to main menu: ");
			option = myObj.nextLine();
		} while(Integer.parseInt(option) != 1 && Integer.parseInt(option) != 2 && Integer.parseInt(option) != 3 && Integer.parseInt(option) != 4);
				
		if (Integer.parseInt(option) == 1) {
			updateEmail(ticketCustomerID);
		}
		else if (Integer.parseInt(option) == 2){
			updateCard(ticketPaymentID);
		}
		else if (Integer.parseInt(option) == 3){
			cancelTicket(ticketCustomerID, ticketPaymentID);
		}
		else if (Integer.parseInt(option) == 4){
			main(null);
		}
	}
	
	public static void manageAirports() {
		String option;
		do {
			//System.out.print("Please select an option: 1. Add airport 2. edit airport 3. delete airport 3. Add flight 4. edit flight 5. delete flight 6. return to main menu");
			System.out.println("Please select an option: 1. Add airport 2. Add flight  3. Update airport 4. Update flight: ");
			option = myObj.nextLine();
		} while(Integer.parseInt(option) != 1 && Integer.parseInt(option) != 2 && Integer.parseInt(option) != 3 && Integer.parseInt(option) != 4);
		
		if (Integer.parseInt(option) == 1) {
			addAirport();;
		}
		else if (Integer.parseInt(option) == 2){
			addFlight();
		}
	}
	
	public static void updateEmail(String customerIDInput) {
		System.out.println("Please enter a new email: ");
		String email = myObj.nextLine();
		customerTable.updateEmail(con, customerIDInput, email);
		main(null);
	}
	
	public static void cancelTicket(String customerIDInput, String paymentIDInput) {
		ticketTable.deleteTicket(con, customerIDInput);
		customerTable.deleteCustomer(con, customerIDInput);
		paymentInformationTable.deletePaymentInfo(con, paymentIDInput );
		main(null);
	}
	
	public static void updateAirport() {
		System.out.println("Please enter the ID of the airport you would like to update: ");
		String airportID = myObj.nextLine();
		System.out.println("Please enter the new county of the airport: ");
		String county = myObj.nextLine();
		airportTable.updateAirport(con, airportID, county);
		main(null);
	}
	
	public static void updateFlight() {
		System.out.println("Please enter the ID of the flight you would like to update: ");
		String flightID = myObj.nextLine();
		System.out.println("Please enter a new destination county for the flight: ");
		String county = myObj.nextLine();
		System.out.println("Please enter a new seat cost of the flight: ");
		String cost = myObj.nextLine();
		flightTable.updateFlight(con, flightID, county, cost);;
		main(null);
	}
	
	public static void updateCard(String paymentIDInput) {
		System.out.println("Please enter a new card number: ");
		String card = myObj.nextLine();
		paymentInformationTable.updatecardNumber(con, card, paymentIDInput);
		main(null);
	}
	
	public static void addAirport() {
		System.out.println("Please enter the county of the new airport: ");
		String county = myObj.nextLine();
		airportTable.createAirport(con, county);
		main(null);
	}
	
	public static void addFlight() {
		System.out.println("Please input the airport ID that the flight will be departing from: ");
		String airportID = myObj.nextLine();
		System.out.println("Please enter the destination county of the flight: ");
		String destination = myObj.nextLine();
		System.out.println("Please input the flight seat cost: ");
		String seatCost = myObj.nextLine();
		flightTable.createFlight(con, airportID, destination, seatCost);
		main(null);
	}
}