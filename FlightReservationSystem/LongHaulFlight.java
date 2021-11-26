import java.util.ArrayList;

/*
 * A Long Haul Flight is a flight that travels a long distance and has two types of seats (First Class and Economy)
 */

public class LongHaulFlight extends Flight
{
	int firstClassPassengers;
		
	public LongHaulFlight(String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft)
	{
		super(flightNum, airline, dest, departure, flightDuration, aircraft);
		type = Flight.Type.LONGHAUL;
	}

	public LongHaulFlight()
	{
		firstClassPassengers = 0;
		type = Flight.Type.LONGHAUL;
	}
	
	public void assignSeat(Passenger p, String seat)
	{
		p.setSeat(seat);
	}
	
	public void reserveSeat(String name, String passport, String seat) throws FlightFullException, PassengerException, InvalidSeatExeption
	{
		// checks if it is a first class seat
		if (seat.contains("+"))
		{
			// check if all the first class passengers seat are occupied
			if (firstClassPassengers >= aircraft.getNumFirstClassSeats())
			{
				throw new FlightFullException("First Class seats are all reserved");
			}
			Passenger p = new Passenger(name, passport, "", seat);
			// checks for duplicate passenger
			if (manifest.indexOf(p) >=  0)
			{
				throw new PassengerException("Duplicate passenger on flight");
			}
			
			assignSeat(p, seat);
			manifest.add(p);
			firstClassPassengers++;
		}
		else // economy passenger
			super.reserveSeat(name, passport, seat);
	}
	
	public void cancelSeat(String name, String passport, String seat) throws PassengerException, InvalidSeatExeption
	{
		if (seat.contains("+"))
		{
			Passenger p = new Passenger(name, passport);
			// checks if the passenger has a reservation with the flight
			if (manifest.indexOf(p) == -1) 
			{
				throw new PassengerException("Passenger not found");
			}
			// removes the passenger from the flight
			manifest.remove(manifest.indexOf(p));
			seatMap.remove(seat);
			if (firstClassPassengers > 0) firstClassPassengers--;
		}
		else
			super.cancelSeat(name, passport, seat);
	}
	
	public int getPassengerCount()
	{
		return getNumPassengers() +  firstClassPassengers;
	}
	
	
	public boolean seatsAvailable(String seat)
	{
		if (seat.contains("+"))
			return firstClassPassengers < aircraft.getNumFirstClassSeats();
		else
			return super.seatsAvailable(seat);
	}
	
	public String toString()
	{
		 return super.toString() + "\t LongHaul";
	}

	// prints out the seat map of the flight
	public void printSeats()
	{
		ArrayList<String> seatNums = aircraft.getSeats();
		for(int j = 0; j < seatNums.size(); j++)
		{
			String seat = seatNums.get(j);
			// checks if the seat is reserved already
			if(seatMap.containsKey(seat))
			{
				// prints n amount of X's depending on how long the string is for the seat
				for(int i = 0; i < seat.length(); i++) System.out.print("X");
				System.out.print(" ");
			} else {
				System.out.print(seat + " ");
			}
			// prints the new row if the letter changes
			if(seat.charAt(seat.length()-1) != 'D' && seat.charAt(seat.length()-1) != '+')
			{
				if(seat.charAt(seat.length()-1) != seatNums.get(j+1).charAt(seatNums.get(j+1).length()-1))
				{
					System.out.println();
				}
			}	
			// prints extra space for the aisle
			if(seat.charAt(seat.length()-1) == 'B' && seatNums.get(j+1).charAt(seatNums.get(j+1).length()-1) != 'B') System.out.println();
		}
	}
}
