package Reefer;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


/**
 * API <a target='_blank' href='http://robocode.sourceforge.net/docs/robocode/'>http://robocode.sourceforge.net/docs/robocode/</a><br>
 * Homepage <a target='_blank' href='http://www.feeditout.com'>http://www.feeditout.com</a><br>
 * @author David O Neill 0813001<br><br>
 * Handles Custom Sounds 
 * @version 0.2
 */

public class CustomSound extends Thread 
{

	private String soundFile;
	private File soundDir;
	private static boolean soundEnabled;
	private String[] sounds; // Sound list array
	
	public CustomSound()
	{
		// default constructor do nothing
	}
	
	
	/**
	 * Main contrusctor for sound thread
	 * @param fileNum The int correspoding to the file to play
	 * @param dataDir robot data directory
	 */
	
	public CustomSound(int fileNum,File dataDir) 
	{
		// sound list
		this.sounds = new String[10];
		this.sounds[0] = "firstblood.wav";
		this.sounds[1] = "killingspree.wav";
		this.sounds[2] = "rampage.wav";
		this.sounds[3] = "monsterkill.wav";
		this.sounds[4] = "ultrakill.wav";
		this.sounds[5] = "holyshit.wav";
		this.sounds[6] = "wickedsick.wav";
		this.sounds[7] = "ludicrouskill.wav";
		this.sounds[8] = "unstoppable.wav";
		this.sounds[9] = "holyshit.wav";
		
		String soundFileNum = "";
		
		if(fileNum<0)
		{
			if(fileNum==-1)
			{
				soundFileNum = "prepare.wav";
			}
			if(fileNum==-2)
			{
				soundFileNum = "godlike.wav";
			}
			if(fileNum==-3)
			{
				soundFileNum = "humiliation.wav";
			}
			if(fileNum==-4)
			{
				soundFileNum = "quaddamage.wav";
			}
		}
		else
		{
			soundFileNum = this.sounds[fileNum];
		}
		
		// Filename Directory And boolean whether sounds are enabled or not
		this.soundFile = soundFileNum;
		this.soundDir = dataDir;
		
	}
	
	
	/**
	 * Returns that status of cusomt sounds boolean
	 * @return boolean
	 */
	
	public static boolean getSoundsBoolean()
	{
		return CustomSound.soundEnabled;
	}
	
	
	/**
	 * Sets the value of the custom sounds boolean
	 * @param val boolean
	 */
	
	public static void setSoundsBoolean(boolean val)
	{
		CustomSound.soundEnabled = val;
	}

	
	/**
	 * The run method for this class which extends thread, so CustomSound.start() executes this
	 */
	
	public void run()	
	{
		if(CustomSound.getSoundsBoolean()==true)
		{
			try
			{
				// convert from type directory to string
				String SoundDirectory = this.soundDir.toString();
				// create file resource path + filename
				File theSoundFile = new File(SoundDirectory + "/" + this.soundFile);
				// pass file resource to audio input stream reader
				AudioInputStream in = AudioSystem.getAudioInputStream(theSoundFile);
				// create a new datasound line stream using audio line Class, audio format, buffer size
				DataLine.Info info = new DataLine.Info(Clip.class, in.getFormat(), ((int)in.getFrameLength()*in.getFormat().getFrameSize()));
				// load the info and cast it to type clip
				Clip clip = (Clip) AudioSystem.getLine(info);
				// open a new sound line
				clip.open(in);
				// start from beginning of the sound stream
				clip.setFramePosition(0);
				// do not loop
				clip.loop(0);
				// Read the file in till the end
				clip.drain();
				// Flush the buffer to the sound system
				clip.flush();
				// Flush the buffer to the sound system
				Thread.sleep(2500);   
				clip.stop();
				clip.close();
				clip = null;
			}
			catch (Exception e)
			{
				System.out.println("");
				System.out.println("------ Custom Sounds Class -------");
				System.out.println("Exception playing sound:");
				e.printStackTrace();
			}
		}
	}
}
