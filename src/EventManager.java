import java.util.ArrayList;
import java.io.*;
import java.util.*;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class EventManager{

    private ArrayList<Event> events  = new ArrayList<Event>();
	ArrayList<Event> availableEvents = new ArrayList<Event>();
    public EventManager() {
		readEventList();
    }

    public Event getEvent(Player player){
		availableEvents.clear(); //clear current list of available events
		String location = player.getLocation().getType(); //find where user is
		for(Event event : events){
			if(event.getLocation().equalsIgnoreCase(location) || event.getLocation().equalsIgnoreCase("Null")){
				availableEvents.add(event);
			}
		}
		if (availableEvents.size() == 0){
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(availableEvents.size());
		return availableEvents.get(index);
    }

    public void readEventList(){
        //events.add(new Event("data from file"))
        //TODO: Read events from file into events array
		String wholeFile = new String();
		ArrayList<String> arrayOfEvents = new ArrayList<String>();
		try{
			BufferedReader file = new BufferedReader(new FileReader(new File("src/events.txt")));
			String event = "";
			for(String x=file.readLine(); x!=null; x=file.readLine()){
				
				if (x.equals("~~~")){
					arrayOfEvents.add(event);
					event = "";
				}
				else{
					event = event.concat("\n");
					event = event.concat(x);
					
				}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		for(int x =0; x<arrayOfEvents.size(); x++){
			String event = arrayOfEvents.get(x);
			ArrayList<String> arrayOfItems = new ArrayList<String>();
			ArrayList<String> actions = new ArrayList<String>(Arrays.asList(event.split("\n")[3]));
			Random rand = new Random();
			float chance = rand.nextFloat();
			//event is a string, we split this event string into the type, description and action, 1 is type, 2 is description and 3 is action
			switch(event.split("\n")[1]){
				case "hospital": 
					if(chance<0.2){
						arrayOfItems.add("medicine");
					}
				break;
				case "road": 
					if(chance<0.002){
						arrayOfItems.add("food"); //everything has 1% of being acquired
					}
					else if(chance<0.004){
						arrayOfItems.add("medicine");
					}
					else if(chance<0.006){
						arrayOfItems.add("weapons");
					}
				break;
				case "null": 
					if(chance<0.002){
						arrayOfItems.add("food"); //everything has 1% of being acquired
					}
					else if(chance<0.004){
						arrayOfItems.add("medicine");
					}
					else if(chance<0.006){
						arrayOfItems.add("weapons");
					}
				break;
				case "hospitalInner": 
					if(chance<0.5){
						arrayOfItems.add("medicine");
					}//higher chance but chance of getting injured
				break;
				case "church": if(chance<0.1){
					arrayOfItems.add("medicine"); 
				}	//lower chance
				break;
				case "woods": if(chance<0.2){
					arrayOfItems.add("weapons"); 
				}
				break;
				case "woodsInner": 
					if(chance<0.5){
					arrayOfItems.add("weapons"); 
					}
				break;
				//higher chance but chance of getting injured
			}	
			Event actualEvent = new Event(event.split("\n")[1], event.split("\n")[2], arrayOfItems ,actions);
			events.add(actualEvent);
		}
		
		//System.out.println(wholeFile);
		//String[] events = wholeFile.split("\n");
		//System.out.println((arrayOfEvents.get(1).split("\n")[1]));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] arg){
		EventManager em = new EventManager();
		
		
	}

}
