import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;



public class FlightManager
{
	Map<String, Flight> flights = new TreeMap<>();
	
	String[] cities 	= 	{"Dallas", "New York", "London", "Paris", "Tokyo"};
	final int DALLAS = 0;  final int NEWYORK = 1;  final int LONDON = 2;  final int PARIS = 3; final int TOKYO = 4;
	
	int[] flightTimes = { 3, // Dallas
							1, // New York
							7, // London
							8, // Paris
							16,// Tokyo
							};
	
	ArrayList<Aircraft> airplanes = new ArrayList<Aircraft>();
	
	String errMsg = null;
	Random random = new Random();
 
  
	public FlightManager()
	{
		// Create some aircraft types with max seat capacities
		airplanes.add(new Aircraft(4, "Bombardier 5000"));
		airplanes.add(new Aircraft(36, "Dash-8 100"));
		airplanes.add(new Aircraft(88, "Boeing 737"));
		airplanes.add(new Aircraft(180,"Airbus 320"));
		airplanes.add(new Aircraft(120, 12, "Boeing 747"));
			
		try
		{
			Scanner scan = new Scanner(new File("flights.txt"));
			while(scan.hasNextLine())
			{
				String line = scan.nextLine();
				String[] info = line.split(" ");
				// replaces the underscore in the airline name
				info[0] = info[0].replace("_", " ");
				info[1] = info[1].replace("_", " ");
				// generates a random flight number
				String flightNum = generateFlightNumber(info[0]);
				Flight flight = null;
				Boolean found = false;
				// checks individually which city the flight is going to and assigns flight the flight information
				if(info[1].equalsIgnoreCase("Dallas")){
					for(int i = 0; i < airplanes.size(); i++){
						if(Integer.parseInt(info[3]) < airplanes.get(i).getNumSeats() && !found){
							flight = new Flight(flightNum, info[0], "Dallas", info[2], flightTimes[DALLAS], airplanes.get(i));
							found = true;
						}
					}
				} else if(info[1].equalsIgnoreCase("New York")) {
					for(int i = 0; i < airplanes.size(); i++){
						if(Integer.parseInt(info[3]) < airplanes.get(i).getNumSeats() && !found){
							flight = new Flight(flightNum, info[0], "New York", info[2], flightTimes[NEWYORK], airplanes.get(i));
							found = true;
						}
					}
				} else if(info[1].equalsIgnoreCase("London")) {
					for(int i = 0; i < airplanes.size(); i++){
						if(Integer.parseInt(info[3]) < airplanes.get(i).getNumSeats() && !found){
							flight = new Flight(flightNum, info[0], "London", info[2], flightTimes[LONDON], airplanes.get(i));
							found = true;
						}
					}
				} else if(info[1].equalsIgnoreCase("Paris")) {
					for(int i = 0; i < airplanes.size(); i++){
						if(Integer.parseInt(info[3]) < airplanes.get(i).getNumSeats() && !found){
							flight = new Flight(flightNum, info[0], "Paris", info[2], flightTimes[PARIS], airplanes.get(i));
							found = true;
						}
					}
				} else if(info[1].equalsIgnoreCase("Tokyo")) {
					flight = new LongHaulFlight(flightNum, info[0], "Tokyo", info[2], flightTimes[TOKYO], airplanes.get(4));
					found = true;
				}
			flights.put(flightNum, flight);
			}
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
		
		private String generateFlightNumber(String airline)
		{
			String word1, word2;
			Scanner scanner = new Scanner(airline);
			word1 = scanner.next();
			word2 = scanner.next();
			String letter1 = word1.substring(0, 1);
			String letter2 = word2.substring(0, 1);
			letter1.toUpperCase(); letter2.toUpperCase();
			
			// Generate random number between 101 and 300
			int flight = random.nextInt(200) + 101;
			String flightNum = letter1 + letter2 + flight;
			scanner.close();
			return flightNum;
		}

		public String getErrorMessage()
		{
			return errMsg;
		}
		
		public void printAllFlights()
		{
			for (String f : flights.keySet())System.out.println(flights.get(f));
		}
		
		public boolean seatsAvailable(String flightNum, String seat)
		{
			return flights.get(flightNum).seatsAvailable(seat);
		}
		
		public Reservation reserveSeatOnFlight(String flightNum, String name, String passport, String seat) throws FlightFullException, InvalidSeatExeption, PassengerException
		{
			flightNum = flightNum.toUpperCase();
			// checks if a flight number is valid or not
			if (!flights.containsKey(flightNum))
			{
				errMsg = "Flight " + flightNum + " Not Found";
				return null;
				
			}
			// checks if seats are available
			if (!seatsAvailable(flightNum, seat))
			{
				errMsg = "Seats not available";
				return null;
			}
			Flight flight = flights.get(flightNum);
			
			flight.reserveSeat(name, passport, seat);
			// returns the new reservation
			return new Reservation(flightNum, flight.toString(), name, passport, seat);
		}
		
	public boolean cancelReservation(Reservation res) throws InvalidSeatExeption, PassengerException
	{
			String flightNum = res.getFlightNum().toUpperCase();
			if (flights.containsKey(flightNum))
			{
				errMsg = "Flight " + res.getFlightNum() + " Not Found";
				return false;
			}
			Flight flight = flights.get(flightNum);
			flight.cancelSeat(res.name, res.passport, res.seatType);
			return true;
	}

	public void displaySeats(String flightNum)
	{
		flightNum = flightNum.toUpperCase();
		if(flights.containsKey(flightNum))
		{
			if(flights.get(flightNum) instanceof LongHaulFlight) ((LongHaulFlight)flights.get(flightNum)).printSeats();
			else flights.get(flightNum).printSeats();
		}
	}

	public void printPass(String flightNum)
	{
		flightNum = flightNum.toUpperCase();
		if(flights.containsKey(flightNum))
		{
			flights.get(flightNum).printPassengerManifest();
		}
	}
}
