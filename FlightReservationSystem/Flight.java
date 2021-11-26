import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class Flight
{
	public enum Status {DELAYED, ONTIME, ARRIVED, INFLIGHT};
	public static enum Type {SHORTHAUL, MEDIUMHAUL, LONGHAUL};
	public static enum SeatType {ECONOMY, FIRSTCLASS, BUSINESS};

	private String flightNum;
	private String airline;
	private String origin, dest;
	private String departureTime;
	private Status status;
	private int flightDuration;
	protected Aircraft aircraft;
	protected int numPassengers;
	protected Type type;
	protected ArrayList<Passenger> manifest;
	protected TreeMap<String, Passenger> seatMap;
	
	protected Random random = new Random();
	
	private String errorMessage = "";
	  
	public String getErrorMessage()
	{
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public Flight()
	{
		this.flightNum = "";
		this.airline = "";
		this.dest = "";
		this.origin = "Toronto";
		this.departureTime = "";
		this.flightDuration = 0;
		this.aircraft = null;
		numPassengers = 0;
		status = Status.ONTIME;
		type = Type.MEDIUMHAUL;
		manifest = new ArrayList<Passenger>();
		seatMap = new TreeMap<String, Passenger>();
	}
	
	public Flight(String flightNum)
	{
		this.flightNum = flightNum;
	}
	
	public Flight(String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft)
	{
		this.flightNum = flightNum;
		this.airline = airline;
		this.dest = dest;
		this.origin = "Toronto";
		this.departureTime = departure;
		this.flightDuration = flightDuration;
		this.aircraft = aircraft;
		numPassengers = 0;
		status = Status.ONTIME;
		type = Type.MEDIUMHAUL;
		manifest = new ArrayList<Passenger>();
		seatMap = new TreeMap<String, Passenger>();
	}
	
	public int getColumns()
	{
		return this.aircraft.getNumColumns();
	}
	public Type getFlightType()
	{
		return type;
	}
	
	public String getFlightNum()
	{
		return flightNum;
	}
	public void setFlightNum(String flightNum)
	{
		this.flightNum = flightNum;
	}
	public String getAirline()
	{
		return airline;
	}
	public void setAirline(String airline)
	{
		this.airline = airline;
	}
	public String getOrigin()
	{
		return origin;
	}
	public void setOrigin(String origin)
	{
		this.origin = origin;
	}
	public String getDest()
	{
		return dest;
	}
	public void setDest(String dest)
	{
		this.dest = dest;
	}
	public String getDepartureTime()
	{
		return departureTime;
	}
	public void setDepartureTime(String departureTime)
	{
		this.departureTime = departureTime;
	}
	
	public Status getStatus()
	{
		return status;
	}
	public void setStatus(Status status)
	{
		this.status = status;
	}
	public int getFlightDuration()
	{
		return flightDuration;
	}
	public void setFlightDuration(int dur)
	{
		this.flightDuration = dur;
	}
	public int getNumPassengers()
	{
		return numPassengers;
	}
	public void setNumPassengers(int numPassengers)
	{
		this.numPassengers = numPassengers;
	}
	
	 public void assignSeat(Passenger p, String seat)
	 {
	 	p.setSeat(seat);
	 }
	
	public boolean seatsAvailable(String seat)
	{
		if (seat.contains("+")) return false;
		return numPassengers < aircraft.numEconomySeats;
	}
	
	public void cancelSeat(String name, String passport, String seat) throws InvalidSeatExeption, PassengerException
	{
		// checks if the seat is valid or not
		if (!aircraft.getSeats().contains(seat)) 
		{
			throw new InvalidSeatExeption("Seat " + seat + " is not valid");
		}
		//checks if the passenger is reserved on the flight
		Passenger p = new Passenger(name, passport);
		if (manifest.indexOf(p) == -1) 
		{
			throw new PassengerException("Passenger not found");													
		}
		manifest.remove(manifest.indexOf(p));
		seatMap.remove(seat);
		if (numPassengers > 0) numPassengers--;
	}
	
	public void reserveSeat(String name, String passport, String seat) throws FlightFullException, InvalidSeatExeption, PassengerException
	{
		if (numPassengers >= aircraft.getNumSeats())
		{
			throw new FlightFullException("There are no more available seats left");
		}
		// checks for if they added the first class sign or invalid letter
		if(!aircraft.getSeats().contains(seat))
		{
			throw new InvalidSeatExeption(seat + " is not valid");
		}	
		// checks if the seat has already been reserved
		if (seatMap != null)
		{
			if(seatMap.containsKey(seat))
			{
				throw new InvalidSeatExeption(seat + " is already reserved");	
			}
		}
		// Check for duplicate passenger
		Passenger p = new Passenger(name, passport, "", seat);
		if (manifest.indexOf(p) >=  0)
		{
			throw new PassengerException("Duplicate passenger on flight");
		}
		assignSeat(p, seat);
		manifest.add(p);
		seatMap.put(seat, p);
		numPassengers++;
	}
	
	public boolean equals(Object other)
	{
		Flight otherFlight = (Flight) other;
		return this.flightNum.equals(otherFlight.flightNum);
	}
	
	public String toString()
	{
		 return airline + "\t Flight:  " + flightNum + "\t Dest: " + dest + "\t Departing: " + departureTime + "\t Duration: " + flightDuration + "\t Status: " + status;
	}
	
	public void printPassengerManifest()
	{
		for(Passenger p: manifest) System.out.println(p);
	}
	
	public void printSeats()
	{
		ArrayList<String> seatNums = aircraft.getSeats();
		for(int j = 0; j < seatNums.size(); j++)
		{
			String seat = seatNums.get(j);
			if(seatMap.containsKey(seat))
			{
				for(int i = 0; i < seat.length(); i++) System.out.print("X");
				System.out.print(" ");
			} else {
				System.out.print(seat + " ");
			}
			// prints the new row if the letter changes
			if(seat.charAt(seat.length()-1) != 'D')
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

class InvalidSeatExeption extends Exception
{
	InvalidSeatExeption(String m)
	{
		super(m);
	}
}

class PassengerException extends Exception
{
	PassengerException(String m)
	{
		super(m);
	}
}

class FlightFullException extends Exception
{
	FlightFullException(String m)
	{
		super(m);
	}
}

