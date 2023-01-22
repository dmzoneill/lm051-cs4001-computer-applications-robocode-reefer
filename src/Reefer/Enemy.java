package Reefer;
import java.util.ArrayList;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Tracks information about enemy Robots<br>
 * @version 0.3
 */

public class Enemy 
{

	// my collection of enemies
	private static ArrayList robots = new ArrayList();
	// this instance robot name
	private String name;
	// this robots energy
	private double energy = 100;
	// robots previous energy / used for detecting power ouput of enemy target
	private double previousEnergy = 100;
	// used for calculating direction and angles
	private double oldHeading; 
	// used for calculating direction and angles
	private double oldHeadingRadians; 
	// this robots last heading
	private double heading;
	// this robots last heading in radians
	private double headingRadians;
	// this robots last bearing in radians
	private double bearingRadians;
	// this robots last bearing
	private double bearing;
	// distance from me
	private double distance;
	// distance from me
	private double velocity;
	// The Last target one of our bullets hit
	private static String lastHitTarget; 
	// current target is only used if stick target is true, otherwise the scanned robot event will not discrimnate
	private static String currentTarget = ""; 
	// used in conjuction with Current Target, on first run the scanned robot event will asign the inital target
	private static boolean target = false; 
	// holds a list of dead robots, if it contains the current target, it will trigger a new search for an enemy
	private static ArrayList whoDied = new ArrayList(); 
	// stick to the same target or not (melee)
	private static boolean stickTarget = false;
	// retaliate for previous assasination?
	private static boolean revenge = false;  
	// If reefer died, revenge will be set to true & current Will be set to revengeTarget for the next round
	private static String revengeTarget = ""; 
	// Enemy count
	private static byte opponentsCount; 
	// number of death events	
	private static byte countDeaths; 
	// number of my kills
	private static byte kills; 
	// Debug ArrayList storage
	private ArrayList degugInfo = new ArrayList();
	
	
	/**
	 * default constructor
	 */
	
	public Enemy() 
	{
		// TODO Auto-generated constructor stub
		// this the default enemy constructor
		// does not add anything to our robots array
		// this essentially does nothing, we dont want any ghosts in our robot array
	}
	
	
	/**
	 * constructor for our new ememy
	 * @param name  The name of enemy robot instance
	 */
	
	public Enemy(String Name) 
	{
		this.name = Name;
		// add this instance to the arraylist
		Enemy.robots.add(this);
	}	


	/**
	 * Returns the name of this enemy robot instance
	 * @return Name 
	 */
	
	public String getName()
	{
		return this.name;
	}	
	
	
	/**
	 * Set the Energy of this robot enemy
	 * @param energy double
	 */
	
	public void setEnergy(double energy)
	{
		this.energy = energy;
	}
	
	
	/**
	 * Sets the energy of the robot from the previous scan
	 * @param energy double
	 */
	
	public void setPreviousEnergy(double energy)
	{
		this.previousEnergy = energy;
	}
	
	
	/**
	 * returns the energy of this robot
	 * @return double enemy energy
	 */
	
	public double getEnergy()
	{
		return this.energy;
	}
	
	
	/**
	 * Gets the previous energy of this robot
	 * @return double previous energy
	 */
	
	public double getPreviousEnergy()
	{
		return this.previousEnergy;
	}
	
	
	/**
	 * Sets the heading of the previous scan of this robot Degrees
	 * @param oldEnemyHeading
	 */
	
	public void setOldHeading(double oldEnemyHeading)
	{
		this.oldHeading = oldEnemyHeading;
	}
	
	
	/**
	 * Gets the old heading of this robot degrees
	 * @return double
	 */
	
	public double getOldHeading()
	{
		return this.oldHeading;
	}
	
	
	/**
	 * Sets the heading of the previous scan of this robot
	 * @param oldEnemyHeading
	 */
	
	public void setOldHeadingRadians(double oldEnemyHeading)
	{
		this.oldHeadingRadians = oldEnemyHeading;
	}
	
	
	/**
	 * Gets the old heading of this robot
	 * @return double
	 */
	
	public double getOldHeadingRadians()
	{
		return this.oldHeadingRadians;
	}
	
	
	/**
	 * Sets the amount of death events in this battle
	 * @param int deaths
	 */
	
	public static void setDeathCount(int deaths)
	{
		Enemy.countDeaths = (byte) deaths;
	}
	
	
	/**
	 * Sets the amount of death events in this battle + 1
	 * @param int deaths
	 */
	
	public static void updateDeathCount()
	{
		Enemy.countDeaths += (byte) 1;
	}
	
	
	/**
	 * Gets the amount of death events in this battle
	 * @return byte deaths
	 */
	
	public static byte getDeathCount()
	{
		return Enemy.countDeaths;
	}

	
	/**
	 * Sets the amount of kills in this battle
	 * @param int deaths
	 */
	
	public static void setKillCount(int deaths)
	{
		Enemy.kills = (byte) deaths;
	}
	
	
	/**
	 * Updates the amount of kills in this battle + 1
	 *
	 */
	
	public static void updateKillCount()
	{
		Enemy.kills += (byte) 1;
	}
	
	
	/**
	 * Gets the amount of kills in this battle
	 * @return byte kills
	 */
	
	public static byte getKillCount()
	{
		return Enemy.kills;
	}
	
	
	/**
	 * Sets the amount of robots in the battle
	 * @param opponents int
	 */
	
	public static void setOthers(int opponents)
	{
		Enemy.opponentsCount = (byte) opponents;
	}
	
	
	/**
	 * Gets the amount of robots left in the battle
	 * @return byte opponents
	 */
	
	public static int getOthers()
	{
		return Enemy.opponentsCount;
	}
		
	
	/**
	 * Sets the heading of this robot in degrees
	 * @param heading double
	 */
	
	public void setHeading(double heading)
	{
		this.heading = heading;
	}
	
	
	/**
	 * Returns the heading of this robot in degrees
	 * @return heading double degrees
	 */
	
	public double getHeading()
	{
		return this.heading;
	}
	
	
	/**
	 * Sets the revenge target for the next round 
	 * @param String target
	 */
	
	public static void setRevengeTarget(String target)
	{
		Enemy.revengeTarget = target;
	}
	
	
	/**
	 * Returns the robot which killed Reefer in the last round
	 * @return String Target
	 */
	
	public static String getRevengeTarget()
	{
		return Enemy.revengeTarget;
	}	
	
	
	/**
	 * Sets the heading in radians for this robot
	 * @param heading
	 */
	
	public void setHeadingRadians(double heading)
	{
		this.headingRadians = heading;
	}
	
	
	/**
	 * Gets the heading in radians for this robot
	 * @return double heading Radians
	 */
	
	public double getHeadingRadians()
	{
		return this.headingRadians;
	}
	
	
	/**
	 * Sets the bearing for this robot 
	 * @param bearing double Radians
	 */
	
	public void setBearing(double bearing)
	{
		this.bearing = bearing;
	}
	
	
	/**
	 * Gets the bearing for this robot
	 * @return double bearing degrees
	 */
	
	public double getBearing()
	{
		return this.bearing;
	}
	
	
	/**
	 * Sets the bearing in Radians for this robot
	 * @param double bearing radians
	 */
	
	public void setBearingRadians(double bearing)
	{
		this.bearingRadians = bearing;
	}
	
	
	/**
	 * Gets the bearing in radians for this robot
	 * @return double bearing radians
	 */
	
	public double getBearingRadians()
	{
		return this.bearingRadians;
	}
	
	
	/**
	 * Sets the distance to the enemy robot
	 * @param distance
	 */
	
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	
	/**
	 * gets the distance to the enemy robot
	 * @return double distance
	 */
	
	public double getDistance()
	{
		return this.distance;
	}
		
	
	/**
	 * Sets the veloicty of this robot
	 * @param double velocity
	 */
	
	public void setVelocity(double velocity)
	{
		this.velocity = velocity;
	}
	
	
	/**
	 * Gets the velocity of this robot
	 * @return double velocity
	 */
	
	public double getVelocity()
	{
		return this.velocity;
	}
		
	
	/**
	 * Sets the lasthitTarget variable
	 * @param String the target which was hit
	 */
	
	public static void setlastHitTarget(String target)
	{
		Enemy.lastHitTarget = target;
	}
	
	
	/**
	 * Returns the lasthitTarget variable
	 * @return String lastHitTarget
	 */
	
	public static String getLastHitTarget()
	{
		return Enemy.lastHitTarget;
	}
	
	
	/**
	 * Sets the currentTarget variable
	 * @param String current Target
	 */
	
	public static void setCurrentTarget(String currentTarget)
	{
		Enemy.currentTarget = currentTarget;
	}
	
	
	/**
	 * Returns the currentTarget variable
	 * @return String currentTarget
	 */
	
	public static String getCurrentTarget()
	{
		return Enemy.currentTarget;
	}	
	

	/**
	 * Sets the revenge variable
	 * @param boolean revenge
	 */
	
	public static void setRevenge(boolean revenge)
	{
		Enemy.revenge = revenge;
	}
	
	
	/**
	 * Returns the revenge variable
	 * @return boolean revenge
	 */
	
	public static boolean getRevenge()
	{
		return Enemy.revenge;
	}
		

	/**
	 * Sets the stickTarget variable
	 * @param boolean whoDied
	 */
	
	public static void setStickTarget(boolean stickTarget)
	{
		Enemy.stickTarget = stickTarget;
	}
	
	
	/**
	 * Returns the stickTarget variable
	 * @return boolean stickTarget
	 */
	
	public static boolean getStickTarget()
	{
		return Enemy.stickTarget;
	}
	
	
	/**
	 * Adds A String to the whoDied Arraylist
	 * @param String whoDied
	 */
	
	public static void setWhoDied(String whoDied)
	{
		Enemy.whoDied.add(whoDied);
	}
	
	
	/**
	 * Returns the whoDied ArrayList
	 * @return String whoDied
	 */
	
	public static ArrayList getWhoDied()
	{
		return Enemy.whoDied;
	}
		
	
	/**
	 * Clear the whoDied Arraylist
	 * @param String whoDied
	 */
	
	public static void clearWhoDied()
	{
		Enemy.whoDied.clear();
	}
	
	
	/**
	 * Sets the target variable ( do we have a target ?)
	 * @param byte target
	 */
	
	public static void setTarget(boolean target)
	{
		Enemy.target = target;
	}
	
	
	/**
	 * Returns the target variable
	 * @return byte target
	 */
	
	public static boolean getTarget()
	{
		return Enemy.target;
	}
	
		
	/**
	 * Creates a new robot enemy instance or returns an instance of a robot already made
	 * @param robotName string name for this robot instance
	 * @return instance of Enemy
	 */
	
	public static Enemy getInstanceOfEnemy(String robotName)
	{
		// loop through our arraylist of enemy instances
		for (int i = 0; i < Enemy.robots.size(); i++)
        {
			// cast the object in robots(i) to temp, an instance of enemy
			Enemy temp = (Enemy) Enemy.robots.get(i); 
			// get the instance name
            String robotInstanceName = temp.getName();
            // if the instance name is the same as the robot were are working with
            if(robotInstanceName==robotName)
            {
            	// return the enemy instance
            	// dermot would not be proud
            	// breaking the loop
            	// this needs to be updated
            	return temp;            	 
            }
        } 
		// if the enemy instance was not found create a new instance for this robot
		return new Enemy(robotName);
	}		
	
	
	/**
	 * Used for outputting robot enemy information
	 */
	
	public void debug()
	{
		System.out.println("\nEnemy Robots\n------------------------------------------------");
		// enemy instances in arraylist
		for (int i = 0; i < Enemy.robots.size(); i++)
	    {
			// cast the object in robots(i) to temp, an instance of enemy
			Enemy temp = (Enemy) Enemy.robots.get(i); 
			// get the instance name
	        String robotInstanceName = temp.getName();
	        System.out.println(robotInstanceName);
	    } 
		System.out.println("");		
		
		System.out.println("------------ Tracking " + this.name + " ----------------");
		System.out.println("Current Energy                   : " + this.energy);
		System.out.println("Previous Energy                  : " + this.previousEnergy);
		System.out.println("Previous Heading Radians    : " + this.oldHeadingRadians);
		System.out.println("Current Heading                 : " + this.heading);
		System.out.println("Current Heading Radians     : " + this.headingRadians);
		System.out.println("Bearing                             : " + this.bearing);
		System.out.println("Bearing Radians                 : " + this.bearingRadians);
		System.out.println("Distance                            : " + this.distance);
		System.out.println("Velocity                              : " + this.velocity);	
		System.out.println("Last Hit Target                   : " + Enemy.getLastHitTarget());
		System.out.println("Current Target                    : " + Enemy.getCurrentTarget());
		System.out.println("Target                               : " + Enemy.getTarget());
		System.out.println("Revenge                             : " + Enemy.getRevenge());
		System.out.println("Revenge Target                  : " + Enemy.getRevengeTarget());
		System.out.println("Stick Target                        : " + Enemy.getStickTarget());
		System.out.println("Who Died                            : " + Enemy.getWhoDied());
		System.out.println("Opponents                          : " + Enemy.getOthers());
		System.out.println("Kills                                    : " + Enemy.getKillCount());
		
	}
	
	
	/**
	 * Used for getting debug robot enemy information
	 * @return ArrayList String[]
	 */
	
	public ArrayList getDebug()
	{
		
		this.degugInfo.clear();
		
		this.degugInfo.add("------------ Tracking " + this.name + " ----------------");
		this.degugInfo.add("Current Energy                   : " + this.energy);
		this.degugInfo.add("Previous Energy                  : " + this.previousEnergy);
		this.degugInfo.add("Previous Heading Radians    : " + this.oldHeadingRadians);
		this.degugInfo.add("Current Heading                 : " + this.heading);
		this.degugInfo.add("Current Heading Radians     : " + this.headingRadians);
		this.degugInfo.add("Bearing                             : " + this.bearing);
		this.degugInfo.add("Bearing Radians                 : " + this.bearingRadians);
		this.degugInfo.add("Distance                            : " + this.distance);
		this.degugInfo.add("Velocity                              : " + this.velocity);	
		this.degugInfo.add("Last Hit Target                   : " + Enemy.getLastHitTarget());
		this.degugInfo.add("Current Target                    : " + Enemy.getCurrentTarget());
		this.degugInfo.add("Target                               : " + Enemy.getTarget());
		this.degugInfo.add("Revenge                             : " + Enemy.getRevenge());
		this.degugInfo.add("Revenge Target                  : " + Enemy.getRevengeTarget());
		this.degugInfo.add("Stick Target                        : " + Enemy.getStickTarget());
		this.degugInfo.add("Who Died                            : " + Enemy.getWhoDied());
		this.degugInfo.add("Opponents                          : " + Enemy.getOthers());
		this.degugInfo.add("Kills                                    : " + Enemy.getKillCount());
		
		return this.degugInfo;
		
	}
	
}
