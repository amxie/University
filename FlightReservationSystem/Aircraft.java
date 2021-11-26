import java.util.ArrayList;

public class Aircraft implements Comparable<Aircraft>
{
	int numEconomySeats;
	int numFirstClassSeats;
	ArrayList<String> seatLayout = new ArrayList<>();
	String model;
	
	public Aircraft(int seats, String model)
	{
		this.numEconomySeats = seats;
		this.numFirstClassSeats = 0;
		this.model = model;
		setSeatNums();
	}

	public Aircraft(int economy, int firstClass, String model)
	{
		this.numEconomySeats = economy;
		this.numFirstClassSeats = firstClass;
		this.model = model;
		setSeatNums();
	}
	
	public int getNumSeats()
	{
		return numEconomySeats;
	}
	
	public int getTotalSeats()
	{
		return numEconomySeats + numFirstClassSeats;
	}
	
	public int getNumFirstClassSeats()
	{
		return numFirstClassSeats;
	}

	public String getModel()
	{
		return model;
	}

	public int getNumColumns()
	{
		return getTotalSeats()/4;
	}

	public int getNumFirstClassColumns()
	{
		return getNumFirstClassSeats()/4;
	}

	public ArrayList<String> getSeats()
	{
		return seatLayout;
	}

	public void setModel(String model)
	{
		this.model = model;
	}
	
	private void setSeatNums()
	{
		for(char row = 'A'; row <= 'D'; row++)
		{
			// prints through each column
			for(int column = 0; column < getNumColumns(); column++)
			{
				String seat = "";
				if(getNumFirstClassSeats() > 0){
					// builds the seat string together
					// adds a "+" at the end if it is a first class seat
					if(column < getNumFirstClassColumns()) seat = Integer.toString(column + 1) + Character.toString(row) + "+";
					// just has the number and letter if economy
					else seat = Integer.toString(column + 1) + Character.toString(row);
				} else {
					// gets the seat string
					seat = Integer.toString(column + 1) + Character.toString(row);
				}
				seatLayout.add(seat);
			}
		}
	}
	public void print()
	{
		System.out.println("Model: " + model + "\t Economy Seats: " + numEconomySeats + "\t First Class Seats: " + numFirstClassSeats);
	}

  public int compareTo(Aircraft other)
  {
  	if (this.numEconomySeats == other.numEconomySeats)
  		return this.numFirstClassSeats - other.numFirstClassSeats;
  	
  	return this.numEconomySeats - other.numEconomySeats; 
  }
}
