package Reefer;
import java.awt.geom.Point2D;
import java.util.Random;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Handles Robot Movement<br>
 * @version 0.1
 */

public class Movement 
{
	// inverse will change direction of movement
	private byte moveDirection;
	private final double maxVelocity = 8.0;
	private final double minTurnAngle = 10.0;
	private final double maxTurnAngle = 45.0;
	private robocode.AdvancedRobot Owner;
	private GraphicalDebugger Paint;
	private boolean nearWall = false;

	
	public Movement() 
	{
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Main Constructor, sets up reference variables to parent classes etc
	 * @param myRobot robocode.AdvancedRobot
	 * @param painterObject GraphicalDebugger
	 * @param aimInst Targetting
	 */
	
	public Movement(robocode.AdvancedRobot myRobot, GraphicalDebugger painterObject) 
	{
		this.Owner = myRobot;
		this.Paint = painterObject;
		this.moveDirection = (byte)1;
	}
	
	
	/**
	 * Randomizes the move direction, backwards or forwards
	 */
		
	private void changeMovementTrajectory()
	{
		Random b = new Random();
		boolean direction = b.nextBoolean();
		if(direction==true) {
			this.moveDirection = 1;
		} else {
			this.moveDirection = -1;
		}
	}
	
	
	/**
	 * Main move method calls the correct movement style dpending on the number of enemies
	 * @param mov int based on my movement types specified in melee and one on one movement
	 * @param eRobot Enemy instance
	 */
	
	public void move(int mov, Enemy eRobot)
	{
		if(this.Owner.getOthers()==1)
		{
			// one on one movement
			this.doOneOne(mov, eRobot);
		}
		else
		{
			// melee movement
			this.doMelee(eRobot);
		}
	}
	
	
	/**
	 * Ram method for this robot
	 * @param eRobot Enemy instance
	 */
	
	public void ram(Enemy eRobot)
	{		
		//	enemy is disabled with (no power) ram
		this.Owner.setMaxVelocity(8.0);
		this.Owner.setTurnRight(eRobot.getBearing());
		this.Owner.ahead(eRobot.getDistance() + 10);	
		this.Owner.execute();
		// lets do a scan while were trying to ram someone
		this.Owner.setTurnRadarRight(Double.POSITIVE_INFINITY);
	}
		
		
	/** 
	 * Handles robot Melee Movement
	 * @param mov 				which movement sequenece to use
	 * @param eRobot 			the Enemy instance
	 */	
	
	private void doMelee(Enemy eRobot) 
	{
		this.Paint.paintCurrentPosition();
		
		this.isNearWall();
		if(this.nearWall == true)
		{
			// see one on one
			return;
		}
		
		double changeInEnergy = eRobot.getPreviousEnergy() - eRobot.getEnergy();

        if (eRobot.getVelocity() == 0)  
        {
        	if (this.Owner.getTime() % 20 == 0) 
        	{       		
        		this.moveDirection *= -1;
        	} 
        	this.Owner.setTurnRight((eRobot.getBearing() + 90) - (15 * this.moveDirection));
        	this.Owner.setAhead(20 * this.moveDirection);
        }       

        else if (changeInEnergy > 0 && changeInEnergy <= 3)
        {
        	// Dodge bullet fire by changing direction
        	this.moveDirection *= -1;
        	this.Owner.setAhead((eRobot.getDistance() / 4 + 25 ) * this.moveDirection);
        }
        else
        {
        	this.Owner.setTurnRight((eRobot.getBearing() + 90) - (15 * this.moveDirection));
        	this.Owner.setAhead(20 * this.moveDirection);
        }
		eRobot.setPreviousEnergy(eRobot.getEnergy());
	}
	
	
	/** 
	 * Handles robot One on One Movement
	 * @param mov 				which movement sequenece to use
	 * @param eRobot 			the Enemy instance
	 */	
	
	private void doOneOne(int mov, Enemy eRobot) 
	{
		this.Paint.paintCurrentPosition();
		
		// this code can be prolemtic if there are lot of enmies on the battlefield
		// straight away find out if were too close to a wall
		this.isNearWall();
		if(this.nearWall == true)
		{
			// still trying to get away from the wall
			// note that dodging is not in effect if we return here
			// but if were stuck in a corner its best to get the hell out of the corner than risk 
			// beeing hit multiple times consecutivly
			return;
		}
		
		// used for random turn and set ahead directions
		Random r = new Random();	
		
		// random velocity			
		double speed = Math.min(r.nextDouble() * 30, this.maxVelocity);
		this.Owner.setMaxVelocity(speed);
		
		// Random turn angle	
		double turnAngle = Math.min(this.maxTurnAngle * r.nextDouble(), this.minTurnAngle * r.nextInt(4));
		
		// Angle to turn
		double angleDirection = (eRobot.getBearing() + 90) - (turnAngle * this.moveDirection);
		
		// distance
		double distance = (eRobot.getDistance() / r.nextInt(5) + 25) * this.moveDirection;


		double changeInEnergy = eRobot.getPreviousEnergy() - eRobot.getEnergy();
		Point2D.Double point = new Point2D.Double(this.Owner.getX(),this.Owner.getY());
		
		if(mov == 1)	
		{				
			// strafing zig zag			
			this.Owner.setTurnRight(angleDirection);
         	// If the bot has small energy drop,
    		// assume it fired		    		
    		if (changeInEnergy>0 && changeInEnergy<=3) 
    		{
         		// Track my dodgepoints
    			MotionTracker.addDodgePoint(point);
    			
    			// Change MoveDirection
    			this.changeMovementTrajectory();
    			
    			// Start Moving
    			this.Owner.setAhead(distance);
    			this.Owner.execute();
     		}    		
		}
		else if(mov == 2) 
		{ 
			// Start Circling
			if (eRobot.getVelocity() == 0)	{
				// if hes stopped he most likely trying to target Reefer. change my direction to make it difficult to lock down	
				if (this.Owner.getTime() % 20 == 0) 
				{
					// lets not get into a wobble :P
					this.Owner.setTurnRight(angleDirection);
					this.changeMovementTrajectory();
				}				
				this.Owner.setAhead(20 * this.moveDirection);
				this.Owner.execute();
			}	
			else if (changeInEnergy > 0 && changeInEnergy <= 3) 
	    	{
				// Track my dodgepoints
				MotionTracker.addDodgePoint(point);				
			
    			// Change MoveDirection
				this.changeMovementTrajectory();
				
				// Start Moving
				this.Owner.setAhead(distance);
				this.Owner.execute();
	     	}
			else 
			{
				if (this.Owner.getTime() % 50 == 0) 
				{
					// lets not get into a wobble :P
					this.Owner.setTurnRight(angleDirection);
				}	
				this.Owner.setAhead(20 * this.moveDirection);
				this.Owner.execute();
			}
		}
		else if(mov == 3)	
		{ 
			// enemy is disabled with (no power) ram
			this.Owner.setMaxVelocity(8.0);
			this.Owner.setTurnRight(eRobot.getBearing());
			this.Owner.ahead(eRobot.getDistance() + 10);	
			this.Owner.execute();
		}	
		else 
		{ 
			// do nothing

		}
		// Track the energy level of enemy
		eRobot.setPreviousEnergy(eRobot.getEnergy());
	}
	
	
	/**
	 * Stops, and reverses the direction
	 */
	
	public void hitWall()
	{
		this.Owner.stop();
		this.Owner.ahead(100 * this.moveDirection * -1); 
		this.Owner.execute(); 
	}
	
	
	/**
	 * Stops, and reverses the direction
	 */
	
	public void hitRobot()
	{
		this.Owner.stop();
		this.Owner.setAhead(100 * this.moveDirection * -1); 
		this.Owner.execute(); 
	}
	
	
	/**
	 * <a href='http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo'>http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo</a><br>
	 * Takes a source point and a destination point, calculates the heading in degrees to get to the source point
	 * @param source Point2D.Double x, y
	 * @param target Point2D.Double x, y
	 * @return double degrees to desintation
	 */
	
	private double absoluteBearing(Point2D source, Point2D target) 
	{
        return Math.toDegrees(Math.atan2(target.getX() - source.getX(), target.getY() - source.getY()));
	}

	
	/**
	 * <a href='http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo'>http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo</a><br>
	 * Returns negative or positive angle (quickest path)
	 * @param angle double in degrees
	 * @return double angle in degrees
	 */
	
	private double normalRelativeAngle(double angle) 
	{
        double relativeAngle = angle % 360;
        if (relativeAngle <= -180) {
			return 180 + (relativeAngle % 180);
		} else if (relativeAngle > 180) {
			return -180 + (relativeAngle % 180);
		} else {
			return relativeAngle;
		}
    }

    
    /**
     * <a href='http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo'>http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo</a><br>
     * Gets my orobots Point2D.Double location
     * @return Point2D.double
     */
    
    private Point2D.Double getRobotLocation() {
        return new Point2D.Double(this.Owner.getX(), this.Owner.getY());
    }

    
    /**
     * <a href='http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo'>http://robowiki.net/cgi-bin/robowiki?Movement/CodeSnippetGoTo</a><br>
     * Sends my robot to this point
     * @param point Point2D.Double
     */
    
    private void goTo(Point2D.Double point) {
        
        double distance = this.getRobotLocation().distance(point);
        double angle = this.normalRelativeAngle(this.absoluteBearing(this.getRobotLocation(), point) - this.Owner.getHeading());
        if (Math.abs(angle) > 90.0) {
            distance *= -1.0;
            if (angle > 0.0) {
                angle -= 180.0;
            }
            else {
                angle += 180.0;
            }
        }
        this.Owner.setTurnRight(angle);
        this.Owner.setAhead(distance);     
    }

    
    /**
     * This check to see if were near a wall
     * If we are it creates a new point away from the wall and adjusts our direction to that point
     */
    
    private void isNearWall()
    {
    	double battleFieldWidth = this.Owner.getBattleFieldWidth();
    	double battleFieldHeight = this.Owner.getBattleFieldHeight();
    	double myX = this.Owner.getX();
    	double myY = this.Owner.getY();
    	double newX = 0;
    	double newY = 0;
    	boolean state = false;
    	
    	// Well use a random offset to avoid pattern matching when we get close to the wall
    	Random r = new Random();
    	
    	int minMax = (int) ((r.nextInt(200)) *  r.nextDouble());
    	int offset = Math.min(100, Math.max(50, minMax));
    	
    	// far right
    	if(myX > battleFieldWidth - offset)
    	{
    		newX = myX - offset;
    		newY = myY;
    		state = true;
    		
    	}
    	// far left
    	if(myX < offset)
    	{
    		newX = myX + offset;
    		newY = myY;
    		state = true;
    	}
    	
    	// top
    	if(myY > battleFieldHeight - offset)
    	{
    		newY = myY - offset;
    		newX = myX;
    		state = true;
    	}
    	// bottom
    	if(myY < offset)
    	{
    		newY = myY + offset;
    		newX = myX;
    		state = true;
    	}    	
    	
    	if(state==true)
    	{
    		// Setting the near wall boll to true stops normal movement, see movement method
    		this.nearWall = true;
    		this.goTo(new Point2D.Double(newX,newY));
    	}
    	else 
    	{
    		// were not near a wall, so set the nearWall bool to flase, allowing our normal movement to execute
    		this.nearWall = false;
    	}
    }
}
