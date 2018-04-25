package strategies;
import automail.Clock;
import automail.MailItem;
import automail.PriorityMailItem;
import automail.StorageTube;

public class LowerRobotBehaviour implements IRobotBehaviour {
	
	private int newPriority; // Used if we are notified that a priority item has arrived. 
		
	public LowerRobotBehaviour() {
		newPriority = 0;
	}
	
	public void startDelivery() {
		newPriority = 0;
	}
	
	@Override
    public void priorityArrival(int priority, int weight) {
    	if (priority > newPriority) newPriority = priority;  // Only the strong robot will deliver priority items so weight of no interest
    }
 
	private int tubePriority(StorageTube tube) {  // Assumes at least one item in tube
		MailItem item = tube.peek();
		return (item instanceof PriorityMailItem) ? ((PriorityMailItem) item).getPriorityLevel() : 0;
	}
	
	@Override
	public boolean returnToMailRoom(StorageTube tube) {
		//since it's a weak robot
		return false;
	}
	
}
