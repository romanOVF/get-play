// Created nov 16 fri 2018

package mypack;

import java.io.*;
import java.util.Map;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;
import javazoom.jl.player.advanced.*;
import javazoom.jlgui.basicplayer.*;

public class PlayTool implements BasicPlayerListener {
	private BasicPlayer playMP3;
	private BasicController control;
	private float volume = 0.5f;
	private String audioFile = "";
	private boolean statusResume = false;

	// Constructor
	public PlayTool () {}

	public void playFile () {
		try {
			playMP3 = new BasicPlayer ();
			control = ( BasicController ) playMP3;
			playMP3.addBasicPlayerListener ( this ); // for info file
			control.open ( new File ( audioFile ) );
			control.play ();
		}
		catch ( Exception e ) { System.out.println ( "1. " + e ); }
	}

	public void opened ( Object stream, Map properties ) {
    // Pay attention to properties. It's useful to get duration,
    // bitrate, channels, even tag such as ID3v2.
    display ( "opened : " + properties.toString () );
  }

  public void progress ( int bytesread, long microseconds, byte [] pcmdata, Map properties ) {
    // Pay attention to properties. It depends on underlying JavaSound SPI
    // MP3SPI provides mp3.equalizer.
    display ( "progress : " + properties.toString () );
  }

  public void stateUpdated ( BasicPlayerEvent event ) {
    // Notification of BasicPlayer states (opened, playing, end of media, ...)
    display ( "stateUpdated : " + event.toString () );
  }

  public void setController ( BasicController controller ) {
    display ( "setController : " + controller );
  }

  public void display ( String msg ) {
    if ( System.out != null ) System.out.println ( msg );
  }

	public void openFile ( String audioFile ) {
		this.audioFile = audioFile;
	}

	public void resumeFile () {
		try { control.resume (); }
		catch ( Exception e ) { System.out.println ( "resume.e " +e ); }
	}

	public void pauseFile () {
		try { control.pause (); }
		catch ( Exception e ) { System.out.println ( "pause.e " +e ); }
	}

	public void stopFile () {
		try { control.stop (); }
		catch ( Exception e ) { System.out.println ( "stop.e " + e ); }
	}

	public void volumeAudio ( float volume ) {
		System.out.println ( "Hallo " + volume );
		this.volume = volume;
		try { control.setGain ( volume ); } // Set Volume (0 to 1.0).
		catch ( Exception e ) { System.out.println ( "volume.e " +e ); }
	}

	public void setAudioBalance () {
		try { control.setPan ( 0.0 ); } // Set Pan (-1.0 to 1.0).
		catch ( Exception e ) { System.out.println ( "pan.e " + e ); }
	}
}
