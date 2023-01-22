package Reefer;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Miscellaneous Methods<br>
 * @version 0.2
 */

public class MiscellaneousMethods 
{
	private robocode.AdvancedRobot Owner;
	private Targetting Aim;
	private boolean quadDamage = false;

	public MiscellaneousMethods() 
	{
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Custom Constructor, needs access to the parent robot and the current instance of that robots targetting object
	 * @param myRobot robocode.AdvancedRobot
	 * @param aimer Targetting
	 */
	
	public MiscellaneousMethods(robocode.AdvancedRobot myRobot, Targetting aimer) 
	{
		this.Owner = myRobot;
		this.Aim = aimer;
	}	
	
	
	/**
	 * Handles The key Event code and calls the appropriate on off method in the graphical debugger class
	 * @param e KeyEvent
	 */
	
	public void graphicalDebuggerKeyOptionsHandler(KeyEvent e)
	{
		switch (e.getKeyCode()) 
		{		
			case 112: // F1
				GraphicalDebugger.setDrawParagraph();				
			break;

			case 113: // F2				
				GraphicalDebugger.setDrawEnemyCurrentPosition();
				GraphicalDebugger.setDrawTargetttingAngle();
			break;

			case 114: // F3
				GraphicalDebugger.setDrawEnemyPredictedPosition();
				GraphicalDebugger.setDrawPredictedTargetttingAngle();
				GraphicalDebugger.setDrawAngularTrajectoryCurve();
			break;

			case 115: // F4
				GraphicalDebugger.setDrawCurrentPosition();				
			break;
			
			case 116: // F5
				GraphicalDebugger.setDrawDodgePoints();
			break;
			
			case 117: // F6
				GraphicalDebugger.setDrawEnemyTracerLines();
			break;
			
			case 118: // F7
				GraphicalDebugger.setDrawBulletTimeDistances();
			break;		
			
			case 119: // F7
				GraphicalDebugger.setDrawEnemyBulletLines();
			break;	
		}
	}
	
	
	/**
	 * Sets Reefer's Colors
	 */
	
	public void reeferColors()
	{
		
		int currentColor;
		int hits = this.Aim.getBulletHits();

		if(hits<=0) 
		{ 
			hits = 0; 
			this.Aim.setBulletHits(0);
		}
		if(hits>=5) 
		{ 
			hits = 5; 
			this.Aim.setBulletHits(5);
		}	
		
		// 10 * 0 * 0 = 0 cold
		// 10 * 1 * 1 = 1
		// 10 * 2 * 2 = 40
		// 10 * 3 * 3 = 90
		// 10 * 4 * 4 = 160
		// 10 * 5 * 5 = 250 hot
		
		currentColor = 10 * hits * hits;

		// create the color
		Color ReeferColor = new Color(currentColor,0,0);
		
		// bullet color
		Color BulletColor = new Color(255,255-currentColor,255-currentColor);
		
		Color bodyColor = ReeferColor;
		Color gunColor = ReeferColor;
		Color radarColor = ReeferColor;
		Color bulletColor = BulletColor;
		Color scanArcColor = this.colorGenerator();				
		
		this.Owner.setColors(bodyColor, gunColor, radarColor, bulletColor, scanArcColor);		
	}
	

	/**
	 * returns a random new color 
	 * @return Color
	 */
	
	public Color colorGenerator()
	{	
		Random rand = new Random();
		int randnum1 = rand.nextInt(255);
		int randnum2 = rand.nextInt(255);
		int randnum3 = rand.nextInt(255);
		Color x = new Color(randnum1,randnum2,randnum3);
		return x; 			
	}
	
	
	/**
	 * Sets the quaddamage boolean
	 * @param bool boolean
	 */
	
	public void setQuadDamage(boolean bool)
	{
		this.quadDamage = bool;
	}
	
	
	/**
	 * Returns the boolean quadDamage
	 * @return boolean quaddamage
	 */
	
	public boolean getQuadDamage()
	{
		return this.quadDamage;
	}
}
