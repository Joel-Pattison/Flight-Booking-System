import java.util.ArrayList;
import java.util.Scanner;

public class Main2 {

	public static void main(String[] args) {
		//Attributes
		String firstName, lastName, email, dateOfBirth, eircode, chosenAirportID, chosenFlightID, seats, currentUserID, currentUserpaymentInformationID, chosenTicketID;
		
		
		// Creating a Scanner object
		Scanner myObj = new Scanner(System.in);
		
		//Creating the database object to initialise the database
		Database bookingSystem = new Database();
		
		//Prompting the user to enter their personal information
		System.out.println("Please enter your first name: ");
		firstName = myObj.nextLine();
		System.out.println("Please enter your last name: ");
		lastName = myObj.nextLine();
		System.out.println("Please enter your email: ");
		email = myObj.nextLine();
		System.out.println("Please enter your date of birth: ");
		dateOfBirth = myObj.nextLine();
		System.out.println("Please enter your eircode: ");
		eircode = myObj.nextLine();
		
		//Storing the users information in a new entry in the customer table and receiving the created users ID
		currentUserID = bookingSystem.createUser(firstName, lastName, email, dateOfBirth, eircode);
		System.out.println(currentUserID);
		
		//Retrieving the list of airports from the database
		ArrayList<String> airportList = bookingSystem.getAirports();
		
		//Printing out a list of available airports and prompting the user to select one
		System.out.print("Please select an airport you would like to depart from:");
		for (int i = 0; i < airportList.size(); i++) {
			int currentID = i + 1;
			System.out.print(" " + currentID + ". " + airportList.get(i));
		}
		System.out.println();
		chosenAirportID = myObj.nextLine();
		
		//Retrieving the list of flights from the database based on the chosen airport, the data in the arraylist returned is formatted as flightID > flightDestination > flightCost
		ArrayList<String> unparsedFlightList = bookingSystem.getFlightsByAirportID(chosenAirportID);
		
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
		currentUserpaymentInformationID = bookingSystem.addCustomerPaymentInformation(currentUserID, cardNumber, cardName, cardExpiry, cardCVC);
		
		//Creating a ticket based on all of the information collected
		chosenTicketID = bookingSystem.createTicket(chosenFlightID, currentUserID, currentUserpaymentInformationID, seats, flightListCosts.get(Integer.parseInt(chosenFlight) - 1));
		
		System.out.println("Thank you for your purchase.");
	}
}