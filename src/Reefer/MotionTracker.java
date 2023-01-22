package Reefer;
import java.awt.geom.Point2D;
import java.util.ArrayList;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Keeps a History of enemy 2D points<br>
 * @version 0.1
 */

public class MotionTracker 
{
	private static ArrayList trackedRobots = new ArrayList();
	private static ArrayList dodgePoints = new ArrayList();
	private String name;
	private ArrayList coordinates;

	
	public MotionTracker() 
	{
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * constructor for our new ememy motion tracker
	 * @param name  The name of enemy robot instance
	 */
	
	public MotionTracker(String Name) 
	{
		this.name = Name;
		// add this instance to the arraylist
		MotionTracker.trackedRobots.add(this);
		this.coordinates = new ArrayList();
	}	
	
	
	/**
	 * Returns the name of this enemy motion tracker instance
	 * @return Name 
	 */
	
	public String getName()
	{
		return this.name;
	}	
	
	
	/**
	 * Adds 2D points to the enemy points tracking ArrayList
	 * @return Name 
	 */
	
	public void addCoordinate(Point2D.Double posCoor)
	{
		this.coordinates.add(posCoor);
	}	
	
	
	/**
	 * Trims the arrylist to the (size or amount) of items in the arraylist
	 * note this is mainly for clean up of memory, and should only be excuted
	 * If you really want to clear up the memory
	 */
	
	public void trimTracerPoints()
	{
		this.coordinates.trimToSize();	
	}
	
	
	/**
	 * Clears the dodge points Arraylist
	 */
	
	public static void clearDodgePoints()
	{
		MotionTracker.dodgePoints.clear();			
	}
	
	
	/**
	 * Clears the tracing points from the Instance coordinates ArrayList
	 */
	
	public void clearTracerPoints()
	{
		this.coordinates.clear();			
	}
	
	
	/**
	 * Clears the enemy robots tracing points for each instance 
	 */
	
	public static void clearAllTracerPoints()
	{
		ArrayList temp = MotionTracker.trackedRobots;		
		MotionTracker trackedEnemy;
		
		for(int t=0; t<temp.size(); t++)
		{
			trackedEnemy = (MotionTracker) temp.get(t);
			trackedEnemy.clearTracerPoints();
			trackedEnemy.trimTracerPoints();
		}
	}
	
	
	/**
	 * Adds points to the dodgepoints ArrayList
	 */
	
	public static void addDodgePoint(Point2D.Double point)
	{
		MotionTracker.dodgePoints.add(point);
	}
	
	
	/**
	 * Returns the dodgepoints arraylist
	 * @return ArrayList 
	 */
	
	public static ArrayList getDodgePoints()
	{
		return MotionTracker.dodgePoints;
	}
	
	
	/**
	 * Returns the coordinates arraylist
	 * @return ArrayList
	 */
	
	public ArrayList getCoordinatesMap()
	{
		return this.coordinates;
	}
	
	
	/**
	 * Gets the size of the coordinates arraylist for this instance
	 * @return int
	 */
	
	public int tracerPointsSize()
	{
		return this.coordinates.size();
	}
	
	
	/**
	 * Returns the correct instace based on the name of the robot passed, or creates a new instance of this class
	 * @param robotName
	 * @return MotionTracker
	 */
	
	public static MotionTracker getInstanceOf(String robotName)
	{
		// loop through our arraylist of tracked enemy instances
		for (int i = 0; i < MotionTracker.trackedRobots.size(); i++)
        {
			// cast the object in robots(i) to temp, an instance of enemy
			MotionTracker temp = (MotionTracker) MotionTracker.trackedRobots.get(i); 
			// get the instance name
            String robotInstanceName = temp.getName();
            // if the instance name is the same as the robot were are working with
            if(robotInstanceName==robotName)
            {
            	// return the MotionTracker enemy instance
            	// dermot would not be proud
            	// breaking the loop
            	// this needs to be updated
            	return temp;            	 
            }
        } 
		// if the MotionTracker enemy instance was not found create a new instance for this MotionTracker robot
		return new MotionTracker(robotName);
	}
	
	
	/**
	 * Ouputs Any Debugging information relveant to this class
	 */
	
	public static void debug()
	{
		//System.out.println("------ Tracking " + this.getName() + " -------");
		//for(int t=0;t<this.coordinates.size();t++)
		//{			
			//Point2D.Double temp = (Point2D.Double) this.coordinates.get(t); 
			//System.out.println(" X : " + temp.x + "Y : " + temp.y);			
		//}	
		
		// This degub is mainly a testing ground
		// printing out all tracking points at this stage would be alot of information
		
		// But we will print the size of the coordinates arraylist for each instance at the start of the round
		// too see if we are clearly up the memory properly
		
		ArrayList temp = MotionTracker.trackedRobots;
		
		MotionTracker trackedEnemy;
		
		for(int t=0; t<temp.size(); t++)
		{
			trackedEnemy = (MotionTracker) temp.get(t);
			System.out.println(trackedEnemy.getName() + " coordinates size " + " " + trackedEnemy.tracerPointsSize());
			
		}
	}

}
