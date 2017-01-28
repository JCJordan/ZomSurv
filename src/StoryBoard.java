/**
 * Created by JCJordan on 28/01/2017.
 */
public class StoryBoard extends Event{
    private Event event;
    private String action;
    public StoryBoard(Event event, ArrayList<String> params){
        this.event = event;
    }
    public Event nextEvent(){
        this.action = event.getAction();
        private Event nextEvent;
        //do something to determine what to pass back
        nextEvent =
    }
    public Event getEvent(){
        return event;
    }
    public ArrayLisy<String> getListOfActions(){
        return event.getInteractable();
    }
}