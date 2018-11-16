// Created nov 04 sun 2018
// https://docs.oracle.com/javase/tutorial/java/data/numberformat.html

// https://archive.org/details/Episode1-IntroductionToThePodcast
// https://archive.org/download/Episode1-IntroductionToThePodcast/MBp1.mp3
// https://archive.org/download/PlanetoftheApes/Retroist-154-The-Planet-of-the-Apes.mp3

// https://images-na.ssl-images-amazon.com/images/I/610CpdfoH5L.png

package mypack;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Downloader extends Thread {

  public static String ADDRESS_URL;
  public static String FILE_NAME;
  public static JLabel dataPropertiesLabel, dataDownloadLabel, imageWaitBarSetAlfaSpinner;
  private static boolean key;
  public static ImageIcon alfaSpinner;

  public Downloader ( ImageIcon alfaSpinner, JLabel imageWaitBarSetAlfaSpinner, JLabel dataPropertiesLabel, JLabel dataDownloadLabel, String ADDRESS_URL, String FILE_NAME ) {
    this.alfaSpinner = alfaSpinner;
    this.imageWaitBarSetAlfaSpinner = imageWaitBarSetAlfaSpinner;
    this.dataPropertiesLabel = dataPropertiesLabel;
    this.dataDownloadLabel = dataDownloadLabel;
    this.ADDRESS_URL = ADDRESS_URL;
    this.FILE_NAME = FILE_NAME;
  }

  public void setKey ( boolean key ) { this.key = key; }

  public void run () {
    double amount = 0;
    String infoAmount = "";
    String outputProperties = "";
    String outputDownload = "";
    DecimalFormat formatterMBDownload = new DecimalFormat ( "loaded    .## MB" );
    DecimalFormat formatterKBDownload = new DecimalFormat ( "loaded   ###.## KB" );
    DecimalFormat formatterMBProperties = new DecimalFormat ( "file size    .## MB" );
    DecimalFormat formatterKBProperties = new DecimalFormat ( "file size   ###.## KB" );

    if ( key ) { // to get file - push button "Download"
      try (
            BufferedInputStream in = new BufferedInputStream ( new URL ( ADDRESS_URL ).openStream () );
            FileOutputStream fileOutputStream = new FileOutputStream ( FILE_NAME );
      ) {
          // get info of file
          amount = ( ( new URL ( ADDRESS_URL ).openConnection ().getContentLength () ) / 1000.0 );
          // get file
          byte [] dataBuffer = new byte [ 1024 ];
          int bytesRead;
          double i = 0;
          while ( ( bytesRead = in.read ( dataBuffer, 0, 1024 ) ) != -1 ) {
            fileOutputStream.write ( dataBuffer, 0, bytesRead );
            i++; // received bytes
            // formating amount to KB
            if ( i < 1000 ) {
              if ( amount < 1000 ) {
                outputProperties = formatterKBProperties.format ( amount );
                dataPropertiesLabel.setText ( outputProperties );
              }
                else {
                  outputProperties = formatterMBProperties.format ( amount / 1000.0 );
                  dataPropertiesLabel.setText ( outputProperties );
                }
              outputDownload = formatterKBDownload.format ( i );
              dataDownloadLabel.setText ( outputDownload );
              System.out.printf ( "%.2f KB\n", i );
            }
              else { // formating amount to MB
                outputProperties = formatterMBProperties.format ( amount / 1000.0 );
                dataPropertiesLabel.setText ( outputProperties );
                outputDownload = formatterMBDownload.format ( i / 1000.0 );
                dataDownloadLabel.setText ( outputDownload );
                System.out.printf ( "%.2f MB\n", ( i / 1000.0 ) );
              }
            //
          }
        } catch ( IOException e ) { System.out.println ( e ); }
    }
      else if ( !key ) { // to get info of file - push button "properties"
        try (
              BufferedInputStream in = new BufferedInputStream ( new URL ( ADDRESS_URL ).openStream () );
        ) {
          amount = ( ( new URL ( ADDRESS_URL ).openConnection ().getContentLength () ) / 1000.0 );
          System.out.printf ( "%.2f " + " MB\n", amount / 1000.0 );

          if ( amount < 1000 ) {
            outputProperties = formatterKBProperties.format ( amount );
            dataPropertiesLabel.setText ( outputProperties );
          }
            else {
              outputProperties = formatterMBProperties.format ( amount / 1000.0 );
              dataPropertiesLabel.setText ( outputProperties );
            }
          } catch ( IOException e ) { System.out.println ( e ); }
      } else {} // end code of get file and get properties
        // after properties and download
        if ( key ) { // get size after loaded file
          if ( amount < 1000 ) {
            System.out.println ( amount + " KB" );
            outputDownload = formatterKBDownload.format ( amount );
            dataDownloadLabel.setText ( outputDownload );
          }
            else {
              System.out.println ( ( amount / 1000.0 ) + " MB" );
              outputDownload = formatterMBDownload.format ( amount / 1000.0 );
              dataDownloadLabel.setText ( outputDownload );
            }
        }

        imageWaitBarSetAlfaSpinner.setIcon ( alfaSpinner );
        //System.out.println ( amount ); // the line checks value while debugging
        System.out.println ( FILE_NAME );
        System.out.println ( Thread.currentThread () );
        JOptionPane.showMessageDialog ( null, "That's All!" );
        System.out.println ( "DONE!" );
      }
}
