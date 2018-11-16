// Created nov 16 fri 2018

// reset && javac -d bin -cp lib/tritonus_share.jar:lib/commons-logging-1.2.jar:lib/mp3spi1.9.4.jar:lib/jl1.0.jar:lib/basicplayer3.0.jar:lib/jlayer-1.0.1.jar *.java && java -cp lib/tritonus_share.jar:lib/commons-logging-1.2.jar:lib/mp3spi1.9.4.jar:lib/jl1.0.jar:lib/basicplayer3.0.jar:lib/jlayer-1.0.1.jar:bin mypack.GUI

// https://stackoverflow.com/questions/1912758/how-to-add-a-popup-menu-to-a-jtextfield
// http://www.java2s.com/Code/Java/Swing-JFC/Apopupmenuissometimescalledacontextmenu.htm
// https://stackoverflow.com/questions/30682416/java-right-click-copy-cut-paste-on-textfield
// http://www.java2s.com/Code/Java/Swing-JFC/DemonstrationofFiledialogboxes.htm

package mypack;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.text.DefaultEditorKit.*;

public class GUI extends Thread implements MouseListener, MouseWheelListener {

  private static Ellipse2D circleOpen = new Ellipse2D.Double ( 2, 2, 36, 36 );
	private static Ellipse2D circlePlay = new Ellipse2D.Double ( 12, 12, 15, 15 );
	private static Ellipse2D circleVolume = new Ellipse2D.Double ( 0, 0, 40, 40 );
	private static PlayTool playTool = new PlayTool ();
  private static int vol = 135;
	private static float volume = 0.5f;
	private static boolean mousePressed = false;
	private static boolean isPlaying = false;
	private static boolean isResume = false;
  private static String audioFile = ""; // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!11

  private static BorderLayout border;
  private static JFrame frame = new JFrame ();
  private static JPanel windowContent, paneProgress, paneButton;
  public static JLabel labelInfoDownload, labelPropertiesFile, waitBar, labelPlayer;
  private static JButton buttonGetProperties, buttonDownload, buttonSave;
  private static ImageIcon alfaSpinner, picSpinner, picProperties, picDownload, picSave;
  public static JTextField fieldAddressURL, fieldFileName;
  private static JPopupMenu popUpMenuContext;

  public static String addressURL;
  public static String fileName;
  private Downloader downProperties, downDownload;

  public GUI () {
    setLayout ();
    setPanel ();
    setPictureIcon ();
    setLabel ();
    setButton ();
    setContextMenuFieldURLFile ();
    addContentToPanel ();
    listeners ();
    setFrame ();
    frame.addMouseListener ( this );
    frame.addMouseWheelListener ( this );
  }

  // Methods

  private void setLayout () { border = new BorderLayout (); }

  private void setPanel () {
    windowContent = new JPanel ();
    paneButton = new JPanel ();
    paneProgress = new JPanel ();
  }

  private void setLabel () {
    waitBar = new JLabel ( alfaSpinner );
    paneProgress.setBorder ( new TitledBorder ( "progress" ) );
    windowContent.setLayout ( new BorderLayout () );

    labelInfoDownload = new JLabel ( "  . . ." );
    labelInfoDownload.setFont ( new Font ( "Liberation Sans", Font.BOLD, 16 ) );
    labelInfoDownload.setForeground ( Color.WHITE );

    labelPropertiesFile = new JLabel ( "  . . ." );
    labelPropertiesFile.setFont ( new Font ( "Liberation Sans", Font.BOLD, 16 ) );
    labelPropertiesFile.setForeground ( Color.WHITE );

    labelPlayer = new JLabel ( "Player" ) {
      public void paint ( Graphics g ) {
    		g.setColor ( Color.RED );
    		g.fillOval ( 0, 0, 40, 40 ); // NO
    		g.setColor ( Color.DARK_GRAY );
        g.fillArc ( 0, 0, 40, 40, -45, -90 ); // NO
        g.setColor ( Color.YELLOW );
        g.fillArc ( 0, 0, 40, 40, -45, vol ); // NO

        if ( mousePressed ) { g.setColor ( Color.LIGHT_GRAY ); }
        else { g.setColor ( Color.GRAY ); }

        g.fillOval ( 2, 2, 36, 36 );
        /*g.setColor ( Color.BLACK );
        Graphics2D g2 = ( Graphics2D ) g; // to display boundary contours
        g2.draw ( circlePlay );
        g2.draw ( circleOpen ); // */

    		if ( mousePressed ) {
    			g.setColor ( Color.GRAY );
    			g.fillOval ( 12, 12, 15, 15 );
    		}
    		else {
    			g.setColor ( Color.LIGHT_GRAY );
    			g.fillOval ( 12, 12, 15, 15 );
    		}
        repaint ();
      }
    };
    labelPlayer.setFont ( new Font ( "Liberation Sans", Font.BOLD, 16 ) );
    labelPlayer.setForeground ( Color.WHITE );
  }

  private void setButton () {
    buttonSave = new JButton ( "save" );
    //buttonSave = new JButton ( "save", picSave );
    buttonSave.setBackground ( Color.GRAY );
    buttonSave.setForeground ( Color.WHITE );
    buttonGetProperties = new JButton ( "properties", picProperties );
    buttonGetProperties.setBackground ( Color.GRAY );
    buttonGetProperties.setForeground ( Color.WHITE );
    buttonDownload = new JButton ( "Download", picDownload );
    buttonDownload.setBackground ( Color.GRAY );
    buttonDownload.setForeground ( Color.WHITE );
  }

  private void setContextMenuFieldURLFile () {
    fieldAddressURL = new JTextField ();
    fieldFileName = new JTextField ( "file.*" );
    popUpMenuContext = new JPopupMenu ();

    Action copy = new DefaultEditorKit.CopyAction ();
    copy.putValue ( Action.NAME, "Copy" );
    copy.putValue ( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke ( "control C" ) );
    popUpMenuContext.add ( copy );

    Action paste = new DefaultEditorKit.PasteAction ();
    paste.putValue ( Action.NAME, "Paste" );
    paste.putValue ( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke ( "control V" ) );
    popUpMenuContext.add ( paste );

    Action cut = new DefaultEditorKit.CutAction ();
    cut.putValue ( Action.NAME, "Cut" );
    cut.putValue ( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke ( "control X" ) );
    popUpMenuContext.add ( cut );

    Action selectAll = new SelectAll ();
    popUpMenuContext.add ( selectAll );

    frame.addMouseListener ( new MouseAdapter () {
      public void mouseReleased ( MouseEvent e ) {
        if ( e.getButton () == e.BUTTON3 ) {
          popUpMenuContext.show ( e.getComponent (), e.getX (), e.getY () );
        }
      }
    } );
    fieldAddressURL.setComponentPopupMenu ( popUpMenuContext );
    fieldFileName.setComponentPopupMenu ( popUpMenuContext );
  }

  private static class SelectAll extends TextAction {
        public SelectAll () {
            super ( "Select All" );
            putValue ( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke ( "control S" ) );
        }

        public void actionPerformed ( ActionEvent e ) {
            JTextComponent component = getFocusedComponent ();
            component.selectAll ();
            component.requestFocusInWindow ();
        }
    }

  private void setPictureIcon () {
    alfaSpinner = new ImageIcon ( getClass ().getResource ( "pic/alfa_193x24.png" ) );
    picSpinner = new ImageIcon ( getClass ().getResource ( "pic/spinner_warning_by_193x24.gif" ) );
    picProperties = new ImageIcon ( getClass ().getResource ( "pic/shelf.png" ) );
    picDownload = new ImageIcon ( getClass ().getResource ( "pic/download_00.png" ) );
    //picSave = new ImageIcon ( getClass ().getResource ( "pic/save.png" ) );
  }

  public void setSpinner () {
    waitBar.setIcon ( picSpinner );
  }

  private void addContentToPanel () {
    paneProgress.setLayout ( new GridLayout ( 2, 0, 0, 0 ) );
    paneProgress.setBackground ( Color.GRAY );
    paneProgress.add ( labelInfoDownload );
    paneProgress.add ( waitBar );
    paneProgress.add ( labelPropertiesFile );
    paneProgress.add ( labelPlayer );

    paneButton.setLayout ( new GridLayout ( 3, 0, 0, 0 ) );
    paneButton.add ( buttonGetProperties );
    paneButton.add ( buttonDownload );
    paneButton.add ( buttonSave );

    windowContent.add ( fieldAddressURL, BorderLayout.NORTH );
    windowContent.add ( fieldFileName, BorderLayout.SOUTH );
    windowContent.add ( paneButton, BorderLayout.EAST );
    windowContent.add ( paneProgress, BorderLayout.CENTER );
  }

  private void listeners () {
    buttonGetProperties.addActionListener ( ( ActionEvent event ) -> {
      System.out.println ( "button properties" );
      setSpinner ();
      downProperties = new Downloader ( alfaSpinner, waitBar, labelPropertiesFile, labelInfoDownload, fieldAddressURL.getText (), fieldFileName.getText () );
      downProperties.setKey ( false );
      downProperties.start ();

    } ); // end of adapter

    buttonDownload.addActionListener ( ( ActionEvent event ) -> {
      System.out.println ( "button download" );
      setSpinner ();
      downDownload = new Downloader ( alfaSpinner, waitBar, labelPropertiesFile, labelInfoDownload, fieldAddressURL.getText (), fieldFileName.getText () );
      downDownload.setKey( true );
      downDownload.start ();

    } ); // end of adapter

    buttonSave.addActionListener ( new SaveFile () );
  }

  class SaveFile implements ActionListener {
    public void actionPerformed ( ActionEvent e ) {
      JFileChooser choose = new JFileChooser ();
      // Demonstrate "Save" dialog:
      int returnValue = choose.showSaveDialog ( windowContent );
      if ( returnValue == JFileChooser.APPROVE_OPTION ) {
        fieldFileName.setText ( choose.getCurrentDirectory ().toString () + "/" + choose.getSelectedFile ().getName () );
      }
      if ( returnValue == JFileChooser.CANCEL_OPTION ) {
        fieldFileName.setText("You pressed cancel");
      }
    }
  }

  private void setFrame () {
    frame.setTitle ( "File downloader v.00" );
    frame.setContentPane ( windowContent );
    frame.setSize ( 500, 180 );
    frame.setResizable ( true );
    frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
    frame.setVisible ( true );
  }

  public void mouseReleased ( MouseEvent e ) {}
	public void mouseEntered ( MouseEvent e ) {}
	public void mouseExited ( MouseEvent e ) {}
	public void mousePressed ( MouseEvent e ) {}
  // ROUND BUTTON
	public void mouseClicked ( MouseEvent e ) {
    if ( !( !circlePlay.contains ( e.getPoint () ) && circleOpen.contains ( e.getPoint () ) ) ) {
			if ( !isPlaying ) {
				if ( !isResume ) {
					isResume = true;
					openFile ( fieldFileName.getText () );
					playAudio ();
				}
				else if ( isResume ) { // isResume
					isResume = true;
					playResume ();
				}
				System.out.println ( "Click Play" );
				isPlaying = true;
				mousePressed = true;
			}
			else if ( isPlaying ) { // isPlaying
				pauseAudio ();
				System.out.println ( "Click Pause" );
				isPlaying = false;
				mousePressed = false;
			}
    }
    if ( !circlePlay.contains ( e.getPoint () ) && circleOpen.contains ( e.getPoint () ) ) {
			if ( isPlaying ) {
				mousePressed = false;
				stopAudio ();
				isPlaying = false;
				System.out.println ( "Click Stop" );
			}
			else if ( isPlaying ) { // !isPlaying
				openFile ( fieldFileName.getText () );
				isResume = false;
				isPlaying = true;
				System.out.println ( "Click Open" );
			}
	  }
  }

  void eventOutputVolume ( String eventDescription ) {
        System.out.println ( eventDescription );
  }

  // VOLUME
	public void mouseWheelMoved ( MouseWheelEvent e ) {
    String message = "";
    if ( !circleOpen.contains ( e.getPoint () ) ) {
			int notches = e.getWheelRotation ();
			if ( notches < 0 && vol > 0 ) {
				vol -= 5;
				volume += 0.018f;
				setVolume ( volume );
				System.out.println ( volume );
				message = vol + " volume +\n";
			} else if ( vol < 270 ) {
				vol += 5;
				volume -= 0.018f;
				setVolume ( volume );
				System.out.println ( volume );
				message = vol + " volume -\n";
			}
			//repaint ();
			eventOutputVolume ( message );
		}
  }

  private void openFile ( String file ) { playTool.openFile ( file ); }
	private void playAudio () { playTool.playFile ();	}
	private void pauseAudio () { playTool.pauseFile ();	}
	private void playResume () { playTool.resumeFile (); }
	private void stopAudio () { playTool.stopFile (); }
	private void setPlayerVolume () {};
	private void setVolume ( float volume ) { playTool.volumeAudio ( volume ); };

  public static void main ( String [] args ) {
    new GUI ();
  }

}
