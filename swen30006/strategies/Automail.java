package strategies;

import automail.IMailDelivery;
import automail.Robot;
import exceptions.InvalidConfigException;

public class Automail {
	      
    public Robot robot1, robot2;
    public IMailPool mailPool;
    
    public Automail(IMailDelivery delivery, String robot_type_1, String robot_type_2) throws InvalidConfigException {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	//// Swap the next line for the one below
    	mailPool = new WeakStrongMailPool();
    	
    	boolean regular = false, weak= false;
    	boolean strong =true, big =true;
   
    	
    	/** Initialize robot */
    	if(robot_type_1.equals("weak")) {
    		if(robot_type_2.equals("weak")) {//2 weak bot
    			//NOT VALID
    			throw new InvalidConfigException();
    		}
    		robot1 = new Robot(new LowerRobotBehaviour(), delivery, mailPool, weak, regular );
    		if(robot_type_2.equals("strong")) {
    			robot2 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, regular );;
	    	}
	    	else {
	    		robot2 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, big);
	    }
    		
	}
    	else if(robot_type_1.equals("big")) {
    		if(robot_type_2.equals("weak")) {//2 weak bot
    			robot1 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, big);
    			robot2 = new Robot(new LowerRobotBehaviour(), delivery, mailPool, weak, regular );
    		}
    		if(robot_type_2.equals("strong")) {
    			robot1 = new Robot(new LowerRobotBehaviour(), delivery, mailPool, strong, big);
    			robot2 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, regular );
	    	}
	    	else { //2 big
	    		robot1 = new Robot(new LowerRobotBehaviour(), delivery, mailPool, strong, big);
	    		robot2 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, big);
	    }	
	}
    	else if(robot_type_1.equals("strong")) {
    		if(robot_type_2.equals("weak")) {//2 weak bot
    			robot1 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, regular);
    			robot2 = new Robot(new LowerRobotBehaviour(), delivery, mailPool, weak, regular );
    		}
    		if(robot_type_2.equals("big")) {
    			robot1 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, regular);
    			robot2 = new Robot(new LowerRobotBehaviour(), delivery, mailPool, strong, big);
	    	}
	    	else { //2 strong
	    		robot1 = new Robot(new UpperRobotBehaviour(), delivery, mailPool, strong, big);
	    		robot2 = new Robot(new LowerRobotBehaviour(), delivery, mailPool, strong, big);
	    }	
	}
	
    	
    	    	
    
    	
    
    }
    
    
}
