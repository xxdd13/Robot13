package exceptions;

/**
 * An exception thrown when the robot tries to deliver more items than its tube capacity without refilling.
 */
public class InvalidConfigException extends Throwable {
	public InvalidConfigException(){
		super("2 weak bots");
	}
}
