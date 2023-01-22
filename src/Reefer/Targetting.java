package Reefer;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import robocode.util.Utils;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Targetting mechanisms<br>
 * @version 0.2
 */

public class Targetting 
{
	
	private double calculatedAngleGunHeadingRadians; // holds new circuliar gun heading in radians
	private double calculatedAngleRadarHeadingRadians;	// holds new circuliar radar heading in radians
	private double myY;
	private double myX;
	private double battleFieldWidth;
	private double battleFieldHeight;
	private double myHeadingRadians;
	private double bulletPower; // bullet power used for dynamic targetting calulations and power management
	private int bulletHits;
	private double myRadarHeadingRadians;
	private double myGunHeadingRadians;
	private Enemy target;
	private Point2D.Double enemyProjectedXY;
	private Point2D.Double enemyPredictedXY;
	private double turnrate;
	// Debug ArrayList storage
	private ArrayList degugInfo = new ArrayList();
	

	public Targetting() 
	{
		/*
		 * TODO implement linear targetting
		 * Possibly predictive targetting 
		 * And or other targetting methods
		 */
		
	}
			

	/**
	 * Set Method for increase bullet hits
	 */
	
	public void increaseBulletHits()
	{
		this.bulletHits += 1;
	}	
	
	
	/**
	 * Set Method for decrease bullet hits
	 */
	
	public void decreaseBulletHits()
	{
		this.bulletHits -= 1;
	}	
	
	
	/**
	 * Set Method for bullet hits
	 */
	
	public void setBulletHits(int hits)
	{
		this.bulletHits = hits;
	}
	
	
	/**
	 * Get Method for bullet hits
	 */
	
	public int getBulletHits()
	{
		return this.bulletHits;
	}
	
	
	/**
	 * Set Method for bullet power
	 */
	
	public void setBulletPower(double power)
	{
		this.bulletPower = power;
	}	
	
	
	/**
	 * Set Method for increase bullet power
	 */
	
	public void increaseBulletPower(double power)
	{
		this.bulletPower += power;
		if(this.bulletPower >= 3.0)
		{	
			// bullet power at max
			// max bullet power is 3.0 lets keep it at that :)
			this.bulletPower = 3.0;
		}	
	}	
	
	
	/**
	 * Set Method for decrease bullet power
	 */
	
	public void decreaseBulletPower(double power)
	{
		this.bulletPower -= power;
		if(this.bulletPower <= 0.5)
		{	
			// at my defined minimum
			this.bulletPower = 0.5;
		}	
	}	
	

	/**
	 * Get Method for bullet power
	 */
	
	public double getBulletPower()
	{
		return this.bulletPower;
	}	
	
	
	/**
	 * Set Method for My Current Target
	 * @param Enemy instance
	 */
	
	public void setTarget(Enemy newTarget)
	{
		this.target = newTarget;
	}
	
	
	/**
	 * Set Method for My Current RadarHeading in radians
	 * @param angle double Radian
	 */
	
	public void setGunHeadingRadians(double angle)
	{
		this.myGunHeadingRadians = angle;
	}	
	
	
	/**
	 * Set Method for My Current RadarHeading in radians
	 * @param angle double Radian
	 */
	
	public void setRadarHeadingRadians(double angle)
	{
		this.myRadarHeadingRadians = angle;
	}	
	
	
	/**
	 * Get Method for new calculated gun heading in radians
	 * @return double radian
	 */
	
	public double getCalculatedGunHeadingRadians()
	{
		return this.calculatedAngleGunHeadingRadians;
	}
		
	
	/**
	 * Get Method for new calculated radar heading in radians
	 * @return double radian
	 */
	
	public double getCalculatedRadarHeadingRadians()
	{
		return this.calculatedAngleRadarHeadingRadians;
	}
	
	
	/**
	 * Set Method for myX myY coordinates
	 * @param X double position on the X plane
	 * @param Y double position on the Y plane
	 */
	
	public void setPosition(double X, double Y)
	{
		this.myX = X;
		this.myY = Y;
	}	
	
	
	/**
	 * Set method for battlefield dimensions
	 * @param width double width of the battlefield
	 * @param height double height of the battlefield
	 */
	
	public void setBattlefieldDimensions(double width, double height)
	{
		this.battleFieldHeight = height;
		this.battleFieldWidth = width;
	}
	
	
	/**
	 * Set method for my Heading in radians
	 */
	
	public void setHeadingRadians(double angle)
	{
		this.myHeadingRadians = angle;
	}
		
	
	/**
	 * Get Method for future X Y position
	 * @return double radian
	 */
	
	public Point2D.Double getFutureXYPosition()
	{
		return this.enemyPredictedXY;
	}
	
	
	/**
	 * Get Method for current X Y position
	 * @return double radian
	 */
	
	public Point2D.Double getCurrentXYPosition()
	{
		return this.enemyProjectedXY;
	}
	
	
	/**
	 * Get Method for change in turnrate
	 * @return double radian
	 */
	
	public double getTurnRate()
	{
		return this.turnrate;
	}
	
	
	/**
	 * Call to circuliarTargetting() calculates and updates angles for use in targetting 
	 */
	
	public void circuliarTargetting()
	{
		
		// Targetting
		// Based on many circuliar targetting examples
		// this one updated to work with my Enemy Class
		// for tracking enemy postions etc
		
		// absolute current heading of the enemy in radians 
		double newEnemyHeading = Utils.normalAbsoluteAngle(this.target.getHeadingRadians());
		// change in angle heading between old enemy heading and new enemy heading
		double deltaEnemyHeading = Utils.normalRelativeAngle(newEnemyHeading - this.target.getOldHeadingRadians());
		// set the old heading to the current heading for the next ScannedRobot Event
		this.target.setOldHeadingRadians(newEnemyHeading);		
		// get my point on the battlefield
		Point2D.Double reeferPosition = new Point2D.Double(this.myX, this.myY);
		// get enemy position on map from me using trig 
		Point2D.Double enemyProjectedPosition = this.project(reeferPosition, this.myHeadingRadians + this.target.getBearingRadians(), this.target.getDistance());
		
		this.enemyProjectedXY = enemyProjectedPosition;
		
		// delta = tick	
		double deltaTime = 1;
		
		
		// create a new x,y postion for the enemy cloning the current posotion
		Point2D.Double predictedEnemyPosition = (Point2D.Double) enemyProjectedPosition.clone();
		// set the predicted heading as the current heading
		double predictedHeading = newEnemyHeading;
		// loop to create a new x, y postion based on the time taken for bullet to travel to that postion
		while(deltaTime * (20.0 - 3.0 * this.bulletPower) < reeferPosition.distance(predictedEnemyPosition))	
		{	
			// time is to small for this bullet distance there by increase the time taken and therefore increase the angle
			deltaTime++;
			// keep adding the predicted heading to the current heading
			predictedHeading += deltaEnemyHeading;		
			// project the point for targetting based on his velocity and predicted heading
			Point2D.Double newPredictedPosition = this.project(predictedEnemyPosition, predictedHeading, this.target.getVelocity());
			// make sure the enemy target x does not go outside the bounds of the battlefield
			newPredictedPosition.x = Math.min(Math.max(18.0, newPredictedPosition.x), this.battleFieldWidth - 18.0);	
			// make sure the enemy target y does not go outside the bounds of the battlefield
			newPredictedPosition.y = Math.min(Math.max(18.0, newPredictedPosition.y), this.battleFieldHeight - 18.0);
			// where the enemy should be assuming the loop ends
			predictedEnemyPosition = newPredictedPosition;
		}
		
		this.enemyPredictedXY = predictedEnemyPosition;
		
		this.calculatedAngleRadarHeadingRadians = Utils.normalRelativeAngle(this.angle(reeferPosition, enemyProjectedPosition) - this.myRadarHeadingRadians);
		this.calculatedAngleGunHeadingRadians = Utils.normalRelativeAngle(this.angle(reeferPosition, predictedEnemyPosition) - this.myGunHeadingRadians);
		
	}	
	
	
	
	public void linearTargetting()
	{
		/*
		
		http://robowiki.net/cgi-bin/robowiki?LinearTargeting
		
		Sample implementation:

			//Add import robocode.util.* for Utils and import java.awt.geom.* for Point2D
			//This code goes in your onScannedRobot() event handler
			
			double bulletPower = Math.min(3.0,getEnergy());
			double myX = getX();
			double myY = getY();
			double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
			double enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
			double enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
			double enemyHeading = e.getHeadingRadians();
			double enemyVelocity = e.getVelocity();
			
			
			double deltaTime = 0;
			double battleFieldHeight = getBattleFieldHeight(), battleFieldWidth = getBattleFieldWidth();
			double predictedX = enemyX, predictedY = enemyY;
			while((++deltaTime) * (20.0 - 3.0 * bulletPower) < Point2D.Double.distance(myX, myY, predictedX, predictedY)){		
				predictedX += Math.sin(enemyHeading) * enemyVelocity;	
				predictedY += Math.cos(enemyHeading) * enemyVelocity;
				if(	predictedX < 18.0 
					|| predictedY < 18.0
					|| predictedX > battleFieldWidth - 18.0
					|| predictedY > battleFieldHeight - 18.0){
					predictedX = Math.min(Math.max(18.0, predictedX), battleFieldWidth - 18.0);	
					predictedY = Math.min(Math.max(18.0, predictedY), battleFieldHeight - 18.0);
					break;
				}
			}
			double theta = Utils.normalAbsoluteAngle(Math.atan2(predictedX - getX(), predictedY - getY()));
			
			setTurnRadarRightRadians(Utils.normalRelativeAngle(absoluteBearing - getRadarHeadingRadians()));
			setTurnGunRightRadians(Utils.normalRelativeAngle(theta - getGunHeadingRadians()));
			fire(bulletPower);
		*/
	}
	
		
	/**
	 * Calculate the future enemy position
	 * @param origin	 		reefer x,y position
	 * @param angle				angle in relation to reefer
	 * @param distance 			distance from reefer to enemy robot
	 * @return Point2D.Double	x,y position of enemy
	 */
	
	public Point2D.Double project(Point2D.Double origin, double angle, double distance)
	{	
		// using x origin + distance (velocity pixels) * sin of bearing projects a new point2d 
		return new Point2D.Double(origin.x + distance * Math.sin(angle), origin.y + distance * Math.cos(angle));
	}
	
	
	/**
	 * Calculate the theta or change in angle for gun aiming (Normalizes + an angle to an absolute angle)
	 * @param origin 			x,y position of enmey
	 * @param destination	 	projected x,y position of enemy
	 * @return double 			projected angle for gun aiming
	 */
	
	public double angle(Point2D.Double origin, Point2D.Double destination)
	{	
		return Utils.normalAbsoluteAngle(Math.atan2(destination.x - origin.x, destination.y - origin.y));
	}

	
	/**
	 * Normalizing the bearing returns the most efficient gun bearing movement
	 * to normalize the input angle to an angle between -180 and 180 degrees.
	 * @param angle 			angle to the enemy bot
	 * @return double angle 	angle positive negative angle to enemy bot
	 */
	
	public double normalizeBearing(double angle) 
	{ 
		// rather than using rectangular coordinates, we use polar coordinates to track him on a curve
        return Math.atan2(Math.sin(angle), Math.cos(angle));
    }	
	
	
	/**
	 * Used for outputting targetting information
	 */
	
	public void debug()
	{
		System.out.println("");	
		
		System.out.println("------------ Targetting " + this.target.getName() + " ----------------");
		System.out.println("My X Position                            : " + this.myX);
		System.out.println("My Y Position                             : " + this.myY);
		System.out.println("Battlefield Width                        : " + this.battleFieldWidth);
		System.out.println("Battlefield Height                       : " + this.battleFieldHeight);
		System.out.println("Current Heading Radians            : " + this.myHeadingRadians);
		System.out.println("Bullet Power                              : " + this.bulletPower);
		System.out.println("Bullet Hits                                 : " + this.bulletHits);
		System.out.println("My Radar Heading Radians          : " + this.myRadarHeadingRadians);
		System.out.println("My Gun Heading Radians            : " + this.myGunHeadingRadians);	
		System.out.println("Calculated Shift in Radar Angle   : " + this.calculatedAngleRadarHeadingRadians);
		System.out.println("Calculated Shift in Gun Angle     : " + this.calculatedAngleGunHeadingRadians);
	
	}
	
	
	/**
	 * Used for getting debug targetting information
	 * @return ArrayList string[]
	 */
	
	public ArrayList getDebug()
	{
		this.degugInfo.clear();
				
		this.degugInfo.add("------------ Targetting " + this.target.getName() + " ----------------");
		this.degugInfo.add("My X Position                            : " + this.myX);
		this.degugInfo.add("My Y Position                             : " + this.myY);
		this.degugInfo.add("Battlefield Width                        : " + this.battleFieldWidth);
		this.degugInfo.add("Battlefield Height                       : " + this.battleFieldHeight);
		this.degugInfo.add("Current Heading Radians            : " + this.myHeadingRadians);
		this.degugInfo.add("Bullet Power                              : " + this.bulletPower);
		this.degugInfo.add("Bullet Hits                                 : " + this.bulletHits);
		this.degugInfo.add("My Radar Heading Radians          : " + this.myRadarHeadingRadians);
		this.degugInfo.add("My Gun Heading Radians            : " + this.myGunHeadingRadians);	
		this.degugInfo.add("Calculated Shift in Radar Angle   : " + this.calculatedAngleRadarHeadingRadians);
		this.degugInfo.add("Calculated Shift in Gun Angle     : " + this.calculatedAngleGunHeadingRadians);
	
		return this.degugInfo;
	}
	
}
