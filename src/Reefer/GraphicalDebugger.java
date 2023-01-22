package Reefer;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Graphical Debugger<br>
 * @version 0.2
 */

public class GraphicalDebugger {

	private boolean enabled;
	private robocode.AdvancedRobot Owner;
	private Targetting target;
	private static boolean drawCurrentPosition = true;
	private static boolean drawEnemyPredictedPosition = true;
	private static boolean drawEnemyCurrentPosition = true;
	private static boolean drawBulletTimeDistances = true;
	private static boolean drawTargetttingAngle = true;
	private static boolean drawPredictedTargetttingAngle = true;
	private static boolean drawEnemyTracerLines = true;
	private static boolean drawDodgePoints = true;
	private static boolean drawAngularTrajectoryCurve = true;	
	private static boolean drawEnemyBulletLines = true;
	private static boolean drawDebugInformation = true;
	
	

	public GraphicalDebugger() {
		this.enabled = false;
	}
	
	
	/**
	 * Main Constructor 
	 * @param bool whether or not to draw 2d graphics
	 * @param paintForThisRobot the robot parent class
	 */
	
	public GraphicalDebugger(boolean bool, robocode.AdvancedRobot paintForThisRobot) {
		this.enabled = bool;
		this.Owner = paintForThisRobot;
		this.target = new Targetting();		
	}
	
	
	/**
	 * Paints a circle around my robot
	 */
	
	public void paintCurrentPosition()
	{
		if(this.enabled==false || GraphicalDebugger.drawCurrentPosition==false) {
			return;
		}		
		
		this.Owner.getGraphics().setColor(Color.green);
		this.Owner.getGraphics().drawOval((int)(this.Owner.getX()-50),(int)(this.Owner.getY()-50), 100,100);
	}
	
	
	/**
	 * Changes drawCurrentPosition to true or false
	 */

	public static void setDrawCurrentPosition()
	{
		GraphicalDebugger.drawCurrentPosition = GraphicalDebugger.drawCurrentPosition ? false : true;
	}
	
	
	/**
	 * Paints a circle around the predicted enemy position
	 * @param preX
	 * @param preY
	 */
	
	public void paintEnemyPredictedPosition(double preX, double preY)
	{
		if(this.enabled==false || GraphicalDebugger.drawEnemyPredictedPosition==false) {
			return;
		}	
				
		this.Owner.getGraphics().setColor(Color.pink);
		this.Owner.getGraphics().drawOval((int)(preX-50),(int)(preY-50), 100,100);		
	}
	
	
	/**
	 * Changes drawEnemyPredictedPosition to true or false
	 */
	
	public static void setDrawEnemyPredictedPosition()
	{
		GraphicalDebugger.drawEnemyPredictedPosition = GraphicalDebugger.drawEnemyPredictedPosition ? false : true;
	}
	
	
	/**
	 * Paints a circle around the current enemy target
	 */
	
	public void paintEnemyCurrentPosition()
	{
		if(this.enabled==false || GraphicalDebugger.drawEnemyCurrentPosition==false) {
			return;
		}	
	
		Point2D.Double myPosition = new Point2D.Double(this.Owner.getX(), this.Owner.getY());		
		Enemy eRobot = Enemy.getInstanceOfEnemy(Enemy.getCurrentTarget());			
		Point2D.Double enemyProjectedPosition = this.target.project(myPosition, this.Owner.getHeadingRadians() + eRobot.getBearingRadians(), eRobot.getDistance());
		this.Owner.getGraphics().setColor(Color.red);
		this.Owner.getGraphics().drawOval((int)(enemyProjectedPosition.x-50),(int)(enemyProjectedPosition.y-50), 100,100);
	}
	
	
	/**
	 * Changes drawEnemyCurrentPosition to true or false
	 */
	
	public static void setDrawEnemyCurrentPosition()
	{
		GraphicalDebugger.drawEnemyCurrentPosition = GraphicalDebugger.drawEnemyCurrentPosition ? false : true;
	}
	
	
	/**
	 * Draws circles around Reefer in respect to bullet time
	 * @param bulletPower used for calulateing the rings distance
	 */
	
	public void paintBulletTimeDistances(double bulletPower)
	{
		if(this.enabled==false || GraphicalDebugger.drawBulletTimeDistances==false) {
			return;
		}	
		
		double bulletDistanceInterval;
		double bulletDistanceIntervalCenterPoint;
		
		Enemy eRobot = Enemy.getInstanceOfEnemy(Enemy.getCurrentTarget());
		
		this.Owner.getGraphics().setColor(Color.white);		
		this.Owner.getGraphics().drawRect(2, 2, 220, 18);
		this.Owner.getGraphics().drawString("BT = Bullet Time (20.0 - 3.0 * bulletPower) " , 6, 6);		
		
		
		int u = 20;
		int i = 1;
		
		this.Owner.getGraphics().setColor(Color.white);
		
		while(u<120)
		{
			bulletDistanceInterval = u * (20.0 - 3.0 * bulletPower);
			bulletDistanceIntervalCenterPoint = bulletDistanceInterval / 2;
			this.Owner.getGraphics().drawOval((int)(this.Owner.getX()-bulletDistanceIntervalCenterPoint), (int)(this.Owner.getY()-bulletDistanceIntervalCenterPoint), (int)bulletDistanceInterval, (int)bulletDistanceInterval);
			// y strings
			this.Owner.getGraphics().drawString(i + " bt" , (int) this.Owner.getX(), (int) (this.Owner.getY() + bulletDistanceIntervalCenterPoint + 15));
			this.Owner.getGraphics().drawString(i + " bt" , (int) this.Owner.getX(), (int) (this.Owner.getY() - bulletDistanceIntervalCenterPoint - 20));
			// x string
			this.Owner.getGraphics().drawString(i + " bt" , (int) (this.Owner.getX() + bulletDistanceIntervalCenterPoint + 15), (int) this.Owner.getY());
			this.Owner.getGraphics().drawString(i + " bt" , (int) (this.Owner.getX() - bulletDistanceIntervalCenterPoint - 30), (int) this.Owner.getY());
			u += 20;
			i++;
		}
					
		int distance = (int) eRobot.getDistance() * 2;
		int distanceCenterPoint = (int) eRobot.getDistance();
		
		this.Owner.getGraphics().setColor(Color.red);
		this.Owner.getGraphics().drawOval((int)(this.Owner.getX()-distanceCenterPoint),(int)(this.Owner.getY()-distanceCenterPoint), distance,distance);		
	}	
	
	
	/**
	 * Changes drawBulletTimeDistances to true or false
	 */
	
	public static void setDrawBulletTimeDistances()
	{
		GraphicalDebugger.drawBulletTimeDistances = GraphicalDebugger.drawBulletTimeDistances ? false : true;
	}
	
	
	/**
	 * Draws a line to the current target position
	 */
	
	public void paintTargetttingAngle()
	{
		if(this.enabled==false || GraphicalDebugger.drawTargetttingAngle==false) {
			return;
		}
		
		Point2D.Double myPosition = new Point2D.Double(this.Owner.getX(), this.Owner.getY());
		Enemy eRobot = Enemy.getInstanceOfEnemy(Enemy.getCurrentTarget());	
		Point2D.Double enemyProjectedPosition = this.target.project(myPosition, this.Owner.getHeadingRadians() + eRobot.getBearingRadians(), eRobot.getDistance());
		this.Owner.getGraphics().setColor(Color.red);
		this.Owner.getGraphics().drawLine((int)myPosition.x, (int)myPosition.y, (int)enemyProjectedPosition.x, (int)enemyProjectedPosition.y);		
	}
	
	
	/**
	 * Changes drawTargetttingAngle to true or false
	 */
	
	public static void setDrawTargetttingAngle()
	{
		GraphicalDebugger.drawTargetttingAngle = GraphicalDebugger.drawTargetttingAngle ? false : true;
	}
	
	
	/**
	 * Draws a line to the predicted targetting position
	 * @param preX Predicted X position
	 * @param preY Predicted Y position
	 */
	
	public void paintPredictedTargetttingAngle(double preX, double preY)
	{
		if(this.enabled==false || GraphicalDebugger.drawPredictedTargetttingAngle==false) {
			return;
		}	
		
		Point2D.Double myPosition = new Point2D.Double(this.Owner.getX(), this.Owner.getY());
		this.Owner.getGraphics().setColor(Color.pink);
		this.Owner.getGraphics().drawLine((int)myPosition.x, (int)myPosition.y, (int)preX, (int)preY);		
	}
	
	
	/**
	 * Changes drawPredictedTargetttingAngle to true or false
	 */
	
	public static void setDrawPredictedTargetttingAngle()
	{
		GraphicalDebugger.drawPredictedTargetttingAngle = GraphicalDebugger.drawPredictedTargetttingAngle ? false : true;
	}
	
	
	/**
	 * Paints Lines indicating the history of movement for target robot
	 * @param Track
	 */
	
	public void paintEnemyTracerLines(MotionTracker Track)
	{
		if(this.enabled==false || GraphicalDebugger.drawEnemyTracerLines==false) {
			return;
		}		
		
		ArrayList temp = Track.getCoordinatesMap();

		this.Owner.getGraphics().setColor(new Color(255,0,0));		
		
		for(int t=(temp.size()-1); t>0; t--)
		{		
			// THIS IS ABSOLUTE FILTH
			// NEXT VERSION TODO CHANGE
			// POSSIBLY UPGRADE TO AN IMAGE PATH FOR EXTRAPOLATION BETWEEN MULTIPLE POINTS
			if(t >= temp.size() - 20 ) {
				this.Owner.getGraphics().setColor(new Color(255, 0, 0));
			}
			if(t >= temp.size() - 40 && t <= temp.size() - 20) {
				this.Owner.getGraphics().setColor(new Color(255, 25, 25));
			}
			if(t >= temp.size() - 60 && t <= temp.size() - 40) {
				this.Owner.getGraphics().setColor(new Color(255, 50, 50));
			}
			if(t >= temp.size() - 80 && t <= temp.size() - 60) {
				this.Owner.getGraphics().setColor(new Color(255, 75, 75));
			}
			if(t >= temp.size() - 100 && t <= temp.size() - 80) {
				this.Owner.getGraphics().setColor(new Color(255, 100, 100));
			}
			if(t >= temp.size() - 120 && t <= temp.size() - 100) {
				this.Owner.getGraphics().setColor(new Color(255, 125, 125));
			}
			if(t >= temp.size() - 140 && t <= temp.size() - 120) {
				this.Owner.getGraphics().setColor(new Color(255, 150, 150));
			}
			if(t >= temp.size() - 160 && t <= temp.size() - 140) {
				this.Owner.getGraphics().setColor(new Color(255, 175, 175));
			}
			if(t >= temp.size() - 180 && t <= temp.size() - 160) {
				this.Owner.getGraphics().setColor(new Color(255, 200, 200));
			}
			if(t >= temp.size() - 200 && t <= temp.size() - 180) {
				this.Owner.getGraphics().setColor(new Color(255, 225, 255));
			}
			if(t >= temp.size() - 220 && t <= temp.size() - 200) {
				this.Owner.getGraphics().setColor(new Color(70, 77, 105));
			}

			Point2D.Double coorStart = (Point2D.Double) temp.get(t -1);
			Point2D.Double coorEnd = (Point2D.Double) temp.get(t); 
			this.Owner.getGraphics().drawLine((int) coorStart.x, (int) coorStart.y, (int) coorEnd.x, (int) coorEnd.y);
		}		
	}
	
	
	/**
	 * Changes drawEnemyTracerLines to true or false
	 */
	
	public static void setDrawEnemyTracerLines()
	{
		GraphicalDebugger.drawEnemyTracerLines = GraphicalDebugger.drawEnemyTracerLines ? false : true;
	}
	
	
	/**
	 * Paint the dodge points for my robot
	 */
	
	public void paintDodgePoints()
	{		
		if(this.enabled==false || GraphicalDebugger.drawDodgePoints==false) {
			return;
		}
		
		ArrayList temp = MotionTracker.getDodgePoints();
		
		for(int t=1;t<temp.size();t++)
		{			
			Point2D.Double point = (Point2D.Double) temp.get(t);
			this.Owner.getGraphics().setColor(Color.white);
			this.Owner.getGraphics().fillRect((int)point.x, (int)point.y, 5, 5);					
		}					
	}
	
	
	/**
	 * Changes drawDodgePoints to true or false
	 */
	
	public static void setDrawDodgePoints()
	{
		GraphicalDebugger.drawDodgePoints = GraphicalDebugger.drawDodgePoints ? false : true;
	}	
	
	
	/**
	 * Draws An Arc Or line between the current position and the predicted position
	 * @param Origin Current X Y enemy position
	 * @param Predicted Predicted x y Enemy position
	 * @param turnrate change angle per turn 
	 */
	
	public void paintAngularTrajectoryCurve(Point2D.Double Origin, Point2D.Double Predicted, double turnrate)
	{
		if(this.enabled==false || GraphicalDebugger.drawAngularTrajectoryCurve==false) {
			return;
		}		
		
		int ePosX = (int) Origin.x;
		int ePosY = (int) Origin.y;
		
		int ePosPreX = (int) Predicted.x;
		int ePosPreY = (int) Predicted.y;
		
		int Xdiff = (int) Math.pow((ePosX - ePosPreX), 2);
		int Ydiff = (int) Math.pow((ePosY - ePosPreY), 2);
		
		int widthOfCurve = (int) Math.sqrt(Xdiff + Ydiff);

		int angle = (int) turnrate;
		if(angle==0) {
			angle = 1;
		}
		int heightOfCurve = widthOfCurve / angle;
		
		this.Owner.getGraphics().setColor(Color.YELLOW);
		// Circuliar
		this.Owner.getGraphics().drawArc(ePosX, ePosY, widthOfCurve, Math.abs(heightOfCurve), Math.abs(angle), Math.abs(angle));
		// linear
		this.Owner.getGraphics().drawLine(ePosX, ePosY, ePosPreX, ePosPreY);		
	}
	
	
	/**
	 * Changes drawAngularTrajectoryCurve to true or false
	 */
	
	public static void setDrawAngularTrajectoryCurve()
	{
		GraphicalDebugger.drawAngularTrajectoryCurve = GraphicalDebugger.drawAngularTrajectoryCurve ? false : true;
	}	
	
	
	/**
	 * Draw Enemy Bullet lines or vectors Tracked by AdvancedBulletTracker
	 */
	
	public void paintEnemyBulletLines()
	{
		if(this.enabled==false || GraphicalDebugger.drawEnemyBulletLines==false) {
			return;
		}	
		
		ArrayList bullets = AdvancedBulletTracker.getBullets();
		
		if(bullets.size()<=0)
		{
			return;
		}
		
		double xOrigin, yOrigin, bulletAngle;
		int X, Y;
		
		this.Owner.getGraphics().setColor(Color.green);	
		
		for(int h=0; h<bullets.size();h++)
		{
			double bullet[] = (double[]) bullets.get(h);
			xOrigin = bullet[0];
			yOrigin = bullet[1];
			bulletAngle = bullet[2];
			
			X = (int) (xOrigin+2000*Math.sin(bulletAngle*Math.PI/180));
			Y = (int) (yOrigin+2000*Math.cos(bulletAngle*Math.PI/180));	
			
			this.Owner.getGraphics().drawLine((int)xOrigin, (int)yOrigin, X, Y);			
		}		
	}
	
	
	/**
	 * Changes drawDebugInformation to true or false
	 */
	
	public static void setDrawEnemyBulletLines()
	{
		GraphicalDebugger.drawEnemyBulletLines = GraphicalDebugger.drawEnemyBulletLines ? false : true;
	}		
	
	
	/**
	 * Prints Out Debugging info on the top left corner
	 * @param ArrayList String[]
	 */
	
	public void paintStringParagraph(ArrayList paragraph,int startPos)
	{
		if(this.enabled==false || GraphicalDebugger.drawDebugInformation==false) {
			return;
		}	
		
		int y = 15;
		int ypos;
		
		this.Owner.getGraphics().setColor(Color.YELLOW);
		
		for(int i=0;i<paragraph.size();i++)
		{
			ypos = (i + 1) * y;
			this.Owner.getGraphics().drawString(paragraph.get(i).toString(), 5, (int) this.Owner.getBattleFieldHeight() - startPos - ypos);
		}
	}
	
	
	/**
	 * Changes drawDebugInformation to true or false
	 */
	
	public static void setDrawParagraph()
	{
		GraphicalDebugger.drawDebugInformation = GraphicalDebugger.drawDebugInformation ? false : true;
	}		
	
}
