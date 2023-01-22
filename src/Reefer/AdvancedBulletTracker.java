package Reefer;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import robocode.util.Utils;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Bullet Avoider<br><br>
 * 
 * Forward<br>
 * On Enemy bullet fired<br>
 * (A) Get his Point2D position <br>
 * (B) Get my future Point2D position<br>
 * (C) Get the equation for the line, for that vector -> (A, B)<br>
 * (D) Move Parralel to this line equation with a deviation of 18+ to avoid being hit<br><br>
 * 
 * Notes *<br>
 * (*) Points of convergence (velocity * time = point -> bullet velocity * time = point)<br>
 * (*) minimal check (based on my heading, is there a point that satisfys the line equation) (avoid that heading)<br>
 * (*) optimal check ( is there a point of convergence based on time taken for the 2 vectors (bullet vector) (my heading vector) with a deviation of +-18 ((robotsize))<br><br>
 *
 * @version 0.1 alpha 1<br>
 */

public class AdvancedBulletTracker {
		
	/*
	 * 
	 * 	Time and distance measurements in Robocode
	 *	Time (t) 1 tick = 1 turn = 1 frame.
	 *	Distance Measurement pixels
     *	Acceleration (a) 	Robots accelerate at the rate of 1 pixel/tick. Robots decelerate at the rate of 2 pixels/tick. 
	 *	Velocity Equation(v) 	v = at. 
	 *	Distance Equation (d) 	d = vt.
	 *	Max rate of rotation of robot 	(10 - .75 * abs(velocity)) degrees / tick. The faster you're moving, the slower you turn.
	 *	Max rate of rotation of gun 	20 degrees / tick. This is added to the current rate of rotation of the robot.
	 *	Max rate of rotation of radar 	45 degrees / tick. This is added to the current rate of rotation of the gun.
	 *	Bullets
	 *	Damage 	4 * firepower. If firepower > 1, it does an additional 2 * (power - 1).
	 *	Velocity 	20 - 3 * firepower;
	 *	GunHeat generated 	1 + firepower / 5. You cannot fire if gunHeat > 0. All guns are hot at the start of each round.
	 *	Power returned on hit 	3 * firepower.
	 *	Collisions
	 *	With Another Robot 	Each robot takes .6 damage. If a robot is moving away from the collision, it will not be stopped.
	 *	With a Wall 	AdvancedRobots take Math.abs(velocity) * .5 - 1; (Never < 0)
	 *
	 */
	
	/*
	 * Equation for a line 
	 * y = mx + c
	 * 
	 * intersection of lines	 * 
	 *
     *  1. Get the two equations for the lines into slope-intercept form. That is, have them in this form: y = mx + c.
     *  2. Set the two equations for y equal to each other.
     *  3. Solve for x. This will be the x-coordinate for the point of intersection.
     *  4. Use this x-coordinate and plug it into either of the original equations for the lines and solve for y. This will be the y-coordinate of the point of intersection.
     *  5. As a check for your work plug the x-coordinate into the other equation and you should get the same y-coordinate.
     *  6. You now have the x-coordinate and y-coordinate for the point of intersection.
	 *
	 * 
	 */

	private static ArrayList bullets = new ArrayList();
	private static robocode.AdvancedRobot Owner;
	private static double enemyBulletEnergy = 1.5;
	private static Targetting tracker = new Targetting();
	private static double oldHeadingRadians;
	private static Point2D.Double myProjectedPosition;
	private static Point2D.Double myPredictedXY;
	
	
	public AdvancedBulletTracker()
	{
		
	}
	
	
	/**
	 * Main contructor
	 * @param myRobot
	 */
	
	public AdvancedBulletTracker(robocode.AdvancedRobot myRobot)
	{
		AdvancedBulletTracker.Owner = myRobot;
	}
	
	
	/**
	 * Updates the old heading with the current heading
	 * @param heading
	 */
	
	public static void setOldHeading(double heading)
	{
		AdvancedBulletTracker.oldHeadingRadians = heading;
	}
		
	
	
	/**
	 * Adds a new bullet to the static arraylist of bullets
	 * @param originX Bullet x origin point
	 * @param originY Bullet y origin point
	 * @param heading Heading of the bullet
	 * @param bulletPower energy drop of the enemy robot
	 */
	
	public static void addNewBullet(double originX, double originY, double heading, double bulletPower, long spawnTime)
	{
		double bullet[] = new double[5];
		bullet[0] = originX;
		bullet[1] = originY;
		bullet[2] = heading;
		bullet[3] = bulletPower; // speed of bullet
		bullet[4] = spawnTime;
		
		AdvancedBulletTracker.bullets.add(bullet);		
		AdvancedBulletTracker.removeOldBullets();
	}
	
	
	/**
	 * Reads the Static ArrayList, Each Item is an array containing bullet trajectories and flight times
	 * Calculate whether the bullet has travelled outside the bounds of the battlefield
	 * Based on time and speed of the bullet 
	 */
	
	public static void removeOldBullets()
	{		
		ArrayList bullets = AdvancedBulletTracker.getBullets();
		
		double xOrigin, yOrigin, bulletAngle, speed, spawnTime;
		int X, Y;
		double currentTime = AdvancedBulletTracker.Owner.getTime();
		double changeInTime;
		double distanceTravelled;
				
		for(int h=0; h<bullets.size();h++)
		{
			double bullet[] = (double[]) bullets.get(h);
			xOrigin = bullet[0];
			yOrigin = bullet[1];
			bulletAngle = bullet[2];
			speed = bullet[3];
			spawnTime = bullet[4];
			
			changeInTime = currentTime - spawnTime;
			distanceTravelled = changeInTime * speed;			
			
			X = (int) (xOrigin+distanceTravelled*Math.sin(bulletAngle*Math.PI/180));
			Y = (int) (yOrigin+distanceTravelled*Math.cos(bulletAngle*Math.PI/180));	
			
			//System.out.println("Bullet Speed " + speed);
			//System.out.println("Current Time " + currentTime);
			//System.out.println("Bullet Fired Time " + spawnTime);
			//System.out.println("Change in time " + changeInTime);
			//System.out.println("Distance Travelled " + distanceTravelled);
			
			if((X > (int) AdvancedBulletTracker.Owner.getBattleFieldWidth() || X <= 0 ) || (Y > (int) AdvancedBulletTracker.Owner.getBattleFieldHeight() || Y <= 0))
			{
				AdvancedBulletTracker.bullets.remove(h);
			}			
		}			
	}
	
	
	/**
	 * TODO Need to update to find point of intersection between the bullet time and my time to the new point on the battlefield
	 * NOT IMPLEMNETED
	 * @param velocity
	 * @param heading
	 * @param origin
	 * @param timeTaken
	 * @return
	 */
	
	public static Point2D.Double calculatePredictedCollisionPoint(double velocity, double heading, Point2D.Double origin, double timeTaken)
	{
		int Radius = (int) (20.0 - 3.0 * AdvancedBulletTracker.enemyBulletEnergy);
		
		double X = origin.x+Radius*Math.sin(heading*Math.PI/180);
		double Y = origin.y+Radius*Math.cos(heading*Math.PI/180);
		
		return new Point2D.Double(X,Y);
		
	}	
	
	
	/**
	 * Call to circuliarTargetting() calculates and updates angles for use in targetting 
	 */
	
	public static void circuliarTargetting(double enemyX, double enemyY, double enemyHeadingRadians, double enemyBearing, double distance)
	{
		// Try to track the bullet
		/*
		 	double bulletHeading = this.Owner.getHeading() - eRobot.getBearing();
			double bulletVelocity = 20.0 - 3.0 * changeInEnergy;
			AdvancedBulletTracker.setBulletEnergy(changeInEnergy);
			AdvancedBulletTracker.circuliarTargetting(this.Aim.getCurrentXYPosition().x, this.Aim.getCurrentXYPosition().y, eRobot.getHeadingRadians(), eRobot.getBearingRadians(), eRobot.getDistance());
		
			AdvancedBulletTracker.addNewBullet(this.Aim.getCurrentXYPosition().x, this.Aim.getCurrentXYPosition().y, bulletHeading, bulletVelocity, this.Owner.getTime());
		 */
		
		// Targetting
		// Based on many circuliar targetting examples
		// this one updated to work with my Enemy Class
		// for tracking enemy postions etc
		
		// current heading of the enemy in radians
		double newEnemyHeading = Utils.normalAbsoluteAngle(AdvancedBulletTracker.Owner.getHeadingRadians());
		// change in angle heading between old enemy heading and new enemy heading
		double deltaEnemyHeading = Utils.normalRelativeAngle(newEnemyHeading - AdvancedBulletTracker.oldHeadingRadians);
		// set the old heading to the current heading for the next ScannedRobot Event
		AdvancedBulletTracker.setOldHeading(newEnemyHeading);		
		// get my point on the battlefield
		Point2D.Double enemyPosition = new Point2D.Double(enemyX, enemyY);
		// get enemy position on map from me using trig 
		Point2D.Double myNewProjectedPosition = AdvancedBulletTracker.tracker.project(enemyPosition, enemyHeadingRadians + (enemyBearing * -1), distance);
		
		AdvancedBulletTracker.myProjectedPosition = myNewProjectedPosition;
		
		// delta = tick	
		double deltaTime = 1;
		
		
		// create a new x,y postion for the enemy cloning the current posotion
		Point2D.Double predictedMyPosition = (Point2D.Double) myNewProjectedPosition.clone();
		// set the predicted heading as the current heading
		double predictedHeading = newEnemyHeading;
		// loop to create a new x, y postion based on the time taken for bullet to travel to that postion
		while(deltaTime * (20.0 - 3.0 * AdvancedBulletTracker.enemyBulletEnergy) < enemyPosition.distance(predictedMyPosition))	
		{	
			// time is to small for this bullet distance there by increase the time taken and therefore increase the angle
			deltaTime++;
			// keep adding the predicted heading to the current heading
			predictedHeading += deltaEnemyHeading;		
			// project the point for targetting based on his velocity and predicted heading
			Point2D.Double newPredictedPosition = AdvancedBulletTracker.tracker.project(predictedMyPosition, predictedHeading, AdvancedBulletTracker.Owner.getVelocity());
			// make sure the enemy target x does not go outside the bounds of the battlefield
			newPredictedPosition.x = Math.min(Math.max(18.0, newPredictedPosition.x), AdvancedBulletTracker.Owner.getBattleFieldWidth() - 18.0);	
			// make sure the enemy target y does not go outside the bounds of the battlefield
			newPredictedPosition.y = Math.min(Math.max(18.0, newPredictedPosition.y), AdvancedBulletTracker.Owner.getBattleFieldHeight() - 18.0);
			// where the enemy should be assuming the loop ends
			predictedMyPosition = newPredictedPosition;
		}
		
		AdvancedBulletTracker.myPredictedXY = predictedMyPosition;
		
	}	
		
	
	/**
	 * Get method for the static arraylist holding existing bullets
	 * @return ArrayList 
	 */
	
	public static ArrayList getBullets()
	{
		return AdvancedBulletTracker.bullets;
	}
	
	
	/**
	 * Sets teh static variable bullet energy to the output in enrgy fo the enemy robot
	 * @param bulletEnergy double
	 */
	
	public static void setBulletEnergy(double bulletEnergy)
	{
		AdvancedBulletTracker.enemyBulletEnergy = bulletEnergy;
	}
	
	
}
