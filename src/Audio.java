import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class is used to record audio for playback.
 * 
 * @author Ben Kirtley
 * @version 1.0
 * @date 3/7/14
 * 
 */
public class Audio
{
    private String filePath = null;
    private Clip clip = null;
    private AudioInputStream inputStream = null;

    /*
     * The constructor to play a audio file.
     */
    Audio(String filePath)
    {
		//Changes made to the inputstream without compiler 
        try
        {
            this.filePath = filePath;
            inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new File(this.filePath)));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            System.out.println("Clip successfully loaded");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * To play a audio file.
     */
    public void play()
    {
        System.out.println("Clicked Play");
        
        if (isavailable())
            clip.start();       
    }

    /*
     * To stop a audio file.
     */
    public void stop()
    {
       System.out.println("Clicked Stop");
      
       if (isavailable())
        clip.stop();  
    }

    /*
     * To loop a audio file continuously.
     */
    public void loop()
    {
        if (isavailable())
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /*
     * Closes all input streams and sets each null.
     */
    public void close()
    {
        stop();
        clip.close();
        clip = null;
        inputStream = null;
    }

    /*
     * Getter for clip field.
     */
    public Clip getClip()
    {
        return this.clip;
    }
    
    /*
     * To determine if a sound file is loaded.
     */
    public boolean isavailable()
    {
        if (clip != null)
            return true;
        else
            return false;
    }
}
