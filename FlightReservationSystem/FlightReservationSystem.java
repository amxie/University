import java.util.ArrayList;
import java.util.Scanner;

// Flight System for one single day at YYZ (Print this in title) Departing flights!!

public class FlightReservationSystem
{
	public static void main(String[] args) throws InvalidSeatExeption, PassengerException, FlightFullException
	{
		FlightManager manager = new FlightManager();

		ArrayList<Reservation> myReservations = new ArrayList<Reservation>();	// my flight reservations


		Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		while (scanner.hasNextLine())
		{
			String inputLine = scanner.nextLine();
			if (inputLine == null || inputLine.equals("")) 
			{
				System.out.print("\n>");
				continue;
			}

			Scanner commandLine = new Scanner(inputLine);
			String action = commandLine.next();

			if (action == null || action.equals("")) 
			{
				System.out.print("\n>");
				continue;
			}

			else if (action.equals("Q") || action.equals("QUIT"))
				return;

			else if (action.equalsIgnoreCase("LIST"))
			{
				manager.printAllFlights(); 
			}
			else if (action.equalsIgnoreCase("RES"))
			{
				String flightNum = null;
				String passengerName = "";
				String passport = "";
				String seat = "";

				if (commandLine.hasNext())
				{
					flightNum = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					passengerName = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					passport = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					seat = commandLine.next();

					// attempts to reserve the seat with the manager
					Reservation res = manager.reserveSeatOnFlight(flightNum, passengerName, passport, seat);
					if (res != null)
					{
						// adds the reservation
						myReservations.add(res);
						res.print();
					}
					else
					{
						System.out.println(manager.getErrorMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("CANCEL"))
			{
				String flightNum = null;
				String passengerName = "";
				String passport = "";

				if (commandLine.hasNext())
				{
					flightNum = commandLine.next().toUpperCase();
				}
				if (commandLine.hasNext())
				{
					passengerName = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					passport = commandLine.next();
	
					// checks if the reservation is listed
					int index = myReservations.indexOf(new Reservation(flightNum, passengerName, passport));
					if (index >= 0)
					{
						// cancels the reservation if there is one found
						manager.cancelReservation(myReservations.get(index));
						myReservations.remove(index);
						System.out.println("Successfully canceled");
					}else{
						System.out.println("Reservation on Flight " + flightNum + " Not Found");
					}

				}
			}
			else if (action.equalsIgnoreCase("SEATS"))
			{
				String flightNum = "";
				
				if (commandLine.hasNext())
				{
					flightNum = commandLine.next();
				}
				manager.displaySeats(flightNum);
			}
			else if (action.equalsIgnoreCase("MYRES"))
			{
				for (Reservation myres : myReservations)
					myres.print();
			}
			// prints out the list of passengers in a flight
			else if (action.equalsIgnoreCase("PASMAN"))
			{
				String flightNum = "";
				if(commandLine.hasNext())
				{ 
					flightNum = commandLine.next();
				}
				manager.printPass(flightNum);
			}
			System.out.print("\n>");
		}
	}


}

