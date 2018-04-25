package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.InvalidConfigException;
import exceptions.ItemTooHeavyException;
import exceptions.MailAlreadyDeliveredException;
import strategies.Automail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {

    /** Constant for the mail generator */


    private static ArrayList<MailItem> MAIL_DELIVERED;
    private static double total_score = 0;

    public static void main(String[] args) throws InvalidConfigException { //throws IOException {
    	// Should probably be using properties here
    	Properties automailProperties = new Properties();
		// Defaults
		//automailProperties.setProperty("Name_of_Property", "20");  
    		// Property value may need to be converted from a string to the appropriate type

		FileReader inStream = null;
		
		try {
			try {
				inStream = new FileReader("automail.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				automailProperties.load(inStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			 if (inStream != null) {
	                try {
						inStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
		}
		
		int Number_of_Floors = Integer.parseInt(automailProperties.getProperty("Number_of_Floors"));
		double Delivery_Penalty = Double.parseDouble(automailProperties.getProperty("Delivery_Penalty"));
		int Last_Delivery_Time = Integer.parseInt(automailProperties.getProperty("Last_Delivery_Time"));
		int Mail_to_Create = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
		String Robot_Type_1 = automailProperties.getProperty("Robot_Type_1");
		String Robot_Type_2 = automailProperties.getProperty("Robot_Type_2");
		

		Building.FLOORS = Number_of_Floors;
		
        MAIL_DELIVERED = new ArrayList<MailItem>();
                
        /** Used to see whether a seed is initialized or not */
        HashMap<Boolean, Integer> seedMap = new HashMap<>();
        
        /** Read the first argument and save it as a seed if it exists */
        if(args.length != 0){
        	int seed = Integer.parseInt(args[0]);
        	seedMap.put(true, seed);
        } else{
        	seedMap.put(false, 0);
        }
        Automail automail = new Automail(new ReportDelivery(),Robot_Type_1, Robot_Type_2);
        MailGenerator generator = new MailGenerator(Mail_to_Create,automail.mailPool, seedMap);
        
        /** Initiate all the mail */
        generator.generateAllMail();
        PriorityMailItem priority;
        while(MAIL_DELIVERED.size() != generator.MAIL_TO_CREATE) {
        	//System.out.println("-- Step: "+Clock.Time());
            priority = generator.step();
            if (priority != null) {
            	automail.robot1.getBehaviour().priorityArrival(priority.getPriorityLevel(), priority.getWeight());
            	automail.robot2.getBehaviour().priorityArrival(priority.getPriorityLevel(), priority.getWeight());
            }
            try {
				automail.robot1.step();
				automail.robot2.step();
			} catch (ExcessiveDeliveryException|ItemTooHeavyException e) {
				e.printStackTrace();
				System.out.println("Simulation unable to complete.");
				System.exit(0);
			}
            Clock.Tick();
        }
        printResults();
    }
    
    static class ReportDelivery implements IMailDelivery {
    	
    	/** Confirm the delivery and calculate the total score */
    	public void deliver(MailItem deliveryItem){
    		if(!MAIL_DELIVERED.contains(deliveryItem)){
                System.out.printf("T: %3d > Delivered     [%s]%n", Clock.Time(), deliveryItem.toString());
    			MAIL_DELIVERED.add(deliveryItem);
    			// Calculate delivery score
    			total_score += calculateDeliveryScore(deliveryItem);
    		}
    		else{
    			try {
    				throw new MailAlreadyDeliveredException();
    			} catch (MailAlreadyDeliveredException e) {
    				e.printStackTrace();
    			}
    		}
    	}

    }
    
    private static double calculateDeliveryScore(MailItem deliveryItem) {
    	// Penalty for longer delivery times
    	final double penalty = 1.1;
    	double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
    	if(deliveryItem instanceof PriorityMailItem){
    		priority_weight = ((PriorityMailItem) deliveryItem).getPriorityLevel();
    	}
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

    public static void printResults(){
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Final Score: %.2f%n", total_score);
    }
}
