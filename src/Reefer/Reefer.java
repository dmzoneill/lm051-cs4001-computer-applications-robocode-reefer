package Reefer;
import java.awt.event.KeyEvent;
import java.io.File;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br>
 * @version 0.5
 */

public class Reefer extends AdvancedRobot 
{	 
	// my robot directory	
	private File robotDirectory;
	// create an instance of our Targetting class
	private Targetting Aim;
	// Set up our Enemy class
	private Enemy EnemyRobot;
	// Miscellaneous Methods
	private MiscellaneousMethods Misc;
	// Graphical Debugger
	private GraphicalDebugger Paint;
	// Motion Tracker
	private MotionTracker Track;
	// Movement Handler
	private Movement Move;

	
  	/**
  	 * @see robocode.Robot#run()
  	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html#run()'>http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html#run()</a>
  	 */	
	
	public void run() 
	{	
		// initilize Graphical Debugger
		this.Paint = new GraphicalDebugger(true, this);	
		
		// Set Up our Enemy Class static variables
		Enemy.clearWhoDied();	
		Enemy.setOthers((byte) this.getOthers());
		Enemy.setDeathCount(0);
		Enemy.setKillCount(0);
		
		// Initilize our targetting class
		this.Aim = new Targetting();
		this.Aim.setBattlefieldDimensions(this.getBattleFieldWidth(), this.getBattleFieldHeight());
		this.Aim.setBulletPower(1.5);
		this.Aim.setBulletHits(0);	
		
		// Initlize Movement Handler
		this.Move = new Movement(this,this.Paint);
		
		// initilize MiscellaneousMethods class
		this.Misc = new MiscellaneousMethods(this,this.Aim);
		this.Misc.reeferColors();
		
		// Motion Tracking Class
		// For Now we wont moinitor between rounds for our graphical debugger
		MotionTracker.clearDodgePoints();		
		MotionTracker.clearAllTracerPoints();
		
		// reefers data directory for use in our custom sounds
		this.robotDirectory = this.getDataDirectory();	
				
		// radar and gun move freely from body
		this.setAdjustRadarForGunTurn(true);	
		
		// enable Custom sounds
		CustomSound.setSoundsBoolean(true);
		
		// taunt the enemy
		new CustomSound(-1,this.robotDirectory).start();	
		
		// initial scan
		while(true)
		{
			// rotate radar until it fires the ScannedRobotEvent
			this.turnRadarRightRadians(Double.POSITIVE_INFINITY); 
		}
	}
			
		
	/**
	 * @see robocode.Robot#onScannedRobot(robocode.ScannedRobotEvent)
	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/ScannedRobotEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/ScannedRobotEvent.html</a>
	 */

	public void onScannedRobot(ScannedRobotEvent e) 
	{				
		// Call getInstanceOfEnemy and return the correct enemy instance object to EnemyRobot
		this.EnemyRobot = Enemy.getInstanceOfEnemy(e.getName());		
		this.EnemyRobot.setEnergy(e.getEnergy());
		this.EnemyRobot.setBearing(e.getBearing());
		this.EnemyRobot.setDistance(e.getDistance());
		this.EnemyRobot.setHeading(e.getHeading());
		this.EnemyRobot.setHeadingRadians(e.getHeadingRadians());
		this.EnemyRobot.setBearingRadians(e.getBearingRadians());
		this.EnemyRobot.setVelocity(e.getVelocity());
		this.EnemyRobot.debug(); 
		
		this.Track = MotionTracker.getInstanceOf(this.EnemyRobot.getName());		
		//this.Track.debug();
		
		if(Enemy.getOthers()>=6)
		{   // too busy to be taking revenge
			Enemy.setRevenge(false);
			Enemy.setStickTarget(false);
		}
			
		if(Enemy.getRevenge() == false)
		{
			if(Enemy.getOthers()<=4) {
				Enemy.setStickTarget(true); // if there are 1 - 4 enemies, lets stick to a target
			} else {
				Enemy.setStickTarget(false); // fight like a mad man (more than 4 robots)
			}
		}
		
		// stick to the same target ?
		if(Enemy.getStickTarget() == true)
		{
			// if target = 0 then this is the first scanned robot in this round
			if(Enemy.getTarget() == false)
			{ 
				// our first target	
				Enemy.setCurrentTarget(this.EnemyRobot.getName());
				// set target = true so this does not change the current target again when using stick target
				Enemy.setTarget(true);
			} 	
			// keep to the same target, otherwise skip the new event for this enemy.
			if(this.EnemyRobot.getName() != Enemy.getCurrentTarget()) 
			{ 
				// bug fix for when Whodied Variable changed before the ScannedRobotEvent picked up the 
				// previous death event leaving reefer looking for the previously dead target 
				// updated whodied to an arrayList, and now check if this list of dead robots contains our current enemy
				if(Enemy.getWhoDied().contains(Enemy.getCurrentTarget()))
				{
					// if the whodied variable holds the name of the robot were attacking
					// update the current target with the latest scanned robot
					Enemy.setCurrentTarget(this.EnemyRobot.getName());
					// If revenge was taken set revenge as false
					Enemy.setRevenge(false);
					this.Track.clearTracerPoints();
				}
				else 
				{			
					// were still attacking our current target robot
					// ignore this scannedRobotEvent
					return; 
				}
			} 
		}	
		
		if(Enemy.getWhoDied().contains(Enemy.getCurrentTarget()))
		{
			// Enemy Died clear his tracer points
			this.Track.clearTracerPoints();
		}
		
		this.Aim.setPosition(this.getX(), this.getY());
		this.Aim.setHeadingRadians(this.getHeadingRadians());
		this.Aim.setTarget(this.EnemyRobot);
		this.Aim.setRadarHeadingRadians(this.getRadarHeadingRadians());
		this.Aim.setGunHeadingRadians(this.getGunHeadingRadians());
		this.Aim.circuliarTargetting();
		this.Aim.debug();
		
		this.Track.addCoordinate(this.Aim.getCurrentXYPosition());
		
		this.Paint.paintEnemyCurrentPosition();
		this.Paint.paintEnemyTracerLines(this.Track);		
		this.Paint.paintBulletTimeDistances(this.Aim.getBulletPower());
		this.Paint.paintEnemyPredictedPosition(this.Aim.getFutureXYPosition().x, this.Aim.getFutureXYPosition().y);
		this.Paint.paintTargetttingAngle();
		this.Paint.paintPredictedTargetttingAngle(this.Aim.getFutureXYPosition().x, this.Aim.getFutureXYPosition().y);
		this.Paint.paintAngularTrajectoryCurve(this.Aim.getCurrentXYPosition(), this.Aim.getFutureXYPosition(), this.EnemyRobot.getHeading() - this.EnemyRobot.getOldHeading());
		this.Paint.paintDodgePoints();
		this.Paint.paintStringParagraph(this.EnemyRobot.getDebug(),0);
		this.Paint.paintStringParagraph(this.Aim.getDebug(),300);
		this.Paint.paintEnemyBulletLines();

		
		this.setTurnRadarRightRadians(this.Aim.getCalculatedRadarHeadingRadians());
		this.setTurnGunRightRadians(this.Aim.getCalculatedGunHeadingRadians());	
		
		
		if(this.EnemyRobot.getEnergy() == 0)
		{ 	
			// enemy disabled, ram him
			this.Move.ram(this.EnemyRobot); 			 
		} 
		else 
		{	
			// fire only when the gun is cool and the turret is not rotating (< 5 - close enough)
			if (this.getGunHeat() == 0 && Math.abs(this.getGunTurnRemaining()) < 5)
			{
				this.setFire(this.Aim.getBulletPower());
			}				
		}		
	
		if(Enemy.getOthers()>=2)
		{	
			// mayhem go for the kill, get in tight and attack
			this.Move.move(2,this.EnemyRobot);				
		}
		else if(Enemy.getOthers() == 1) 
		{	
			if(this.EnemyRobot.getDistance() > 300)
			{
				// long distance dodging
				// slowly move in closer
				this.Move.move(1,this.EnemyRobot);	
			}
			else
			{
				// execute circular movement around target if were less than or equal to 300 pixels away
				this.Move.move(2,this.EnemyRobot);	
			}
		}
		else 
		{ 	
			// do nothing
				
		}		
		this.EnemyRobot.setOldHeading(this.EnemyRobot.getHeading());
	}	

	
	/**
	 * (non-Javadoc)
	 * @see robocode.Robot#onKeyPressed(java.awt.event.KeyEvent)
	 */
	
	public void onKeyPressed(KeyEvent e) 
	{
		// Pass the keyevent to one of our misc methods
		this.Misc.graphicalDebuggerKeyOptionsHandler(e);
	}
	
	
	/**
	 * @see robocode.Robot#onHitWall(robocode.HitWallEvent)
	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/HitWallEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/HitWallEvent.html</a>
	 */
	
	public void onHitWall(HitWallEvent e) 
	{ 
		this.Move.hitWall();		
	}

	
	/** 
	 * @see robocode.Robot#onHitRobot(robocode.HitRobotEvent)
	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/HitRobotEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/HitRobotEvent.html</a>
	 */
	
	public void onHitRobot(HitRobotEvent e) 
	{ 
		this.Move.hitRobot();
	}

	
	/**
	 * @see robocode.Robot#onHitByBullet(robocode.HitByBulletEvent)
	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/HitByBulletEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/HitByBulletEvent.html</a>
	 */
	
	public void onHitByBullet(HitByBulletEvent e) 
	{
		// update the revenge target (see onDeath event)
		Enemy.setRevengeTarget(e.getName());
	}
		
	
	/**
	 * @see robocode.Robot#onBulletHit(robocode.BulletHitEvent)
	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/BulletHitEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/BulletHitEvent.html</a>
	 */

	public void onBulletHit(BulletHitEvent e) 
	{			
		// set the last target for death monitoring
		Enemy.setlastHitTarget(e.getName());
		// were hitting, increase bullet power
		this.Aim.increaseBulletHits();
		this.Aim.increaseBulletPower(0.75);
		this.Misc.reeferColors();
		
		
		// QuadDamage stuff
		// quad dmage bool is false
		// Play quad damage sound clips if bullet hits is >= 4 and quadDamge Boolean = false
		// Only play it once but setting quaddamage bool to true, wait till bullet hits goes back to 0, then set the boolean to flase again
		// to avoid playing over and over again
		
		if(this.Aim.getBulletHits()>=4 && this.Misc.getQuadDamage() == false)
		{
			new CustomSound(-4, this.robotDirectory).start();
			this.Misc.setQuadDamage(true);
		}		
   	}
	
	
	/**
	 * @see robocode.Robot#onBulletMissed(robocode.BulletMissedEvent)
	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/BulletMissedEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/BulletMissedEvent.html</a>
	 */
 
	public void onBulletMissed(BulletMissedEvent e)
	{	
		// were missing start decreasing the bullet power
		this.Aim.decreaseBulletHits();	
		this.Aim.decreaseBulletPower(0.25);
		this.Misc.reeferColors();
		
		// if bullet hits is gone back down to 0
		// set quad dmage as false
		// so we can build back up again
		if(this.Aim.getBulletHits()==0)
		{
			this.Misc.setQuadDamage(false);
		}
   	}
	
	
	/**
	 *  @see robocode.Robot#onWin(robocode.WinEvent)
	 * 	<a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/RobotWinEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/RobotWinEvent.html</a>
	 */
	
	public void onWin(WinEvent e)
	{
		// taunt the enemy
		new CustomSound(-2, this.robotDirectory).start();
		this.turnRadarRightRadians(1200); 		
	}
	
	
	/**
	 * @see robocode.Robot#onWin(robocode.DeathEvent)
	 * <a target='_blank' href='/http://robocode.sourceforge.net/docs/robocode/robocode/DeathEvent.html'>/http://robocode.sourceforge.net/docs/robocode/robocode/DeathEvent.html</a>
	 */
	
	public void onDeath(DeathEvent e)
	{		
		// Reefer has been humiliated
		new CustomSound(-3, this.robotDirectory).start();
		// if Reefer was killed, the CurrentTarget For the next round will be set to the revengeTarget set by the onHitbyBulletEvent
		Enemy.setCurrentTarget(Enemy.getRevengeTarget());
		Enemy.setStickTarget(true);
		Enemy.setRevenge(true);			
	}

	
	/**
	 * @see robocode.Robot#onRobotDeath(robocode.RobotDeathEvent)
	 * <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/robocode/RobotDeathEvent.html'>http://robocode.sourceforge.net/docs/robocode/robocode/RobotDeathEvent.html</a>
	 */
	
	public void onRobotDeath(RobotDeathEvent e)	
	{ 
		// update opponentsCount
		Enemy.setOthers(Enemy.getOthers() -1);
		
		// update the whodied variable with the latest death robot name
		Enemy.setWhoDied(e.getName()); 
		
		if(Enemy.getDeathCount()==0 && Enemy.getOthers()>0)
		{
			// first guy to die
			new CustomSound(0,this.robotDirectory).start();	
		}
		
		if(Enemy.getWhoDied().contains(Enemy.getLastHitTarget()))
		{		
			// margin of error here, this may not always be true that Reefer killed
			Enemy.updateKillCount();
			if(Enemy.getKillCount()>=2 && Enemy.getKillCount()<=10)
			{
				new CustomSound(Enemy.getKillCount() - 1,this.robotDirectory).start();			
			}
		}		
		Enemy.updateDeathCount();
	}		
}
