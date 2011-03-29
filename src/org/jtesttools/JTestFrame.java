package org.jtesttools;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/** Window that works well for testing with JUnit.
 *  
 * <p><i>JTestFrame</i> is meant to be used in a JUnit test.  Its
 * main benefit over a regular {@link JFrame} is that by using the 
 * {@link #start} method you can make the JUnit thread block
 * until the window is closed.  This is needed because unlike a 
 * regular <i>main</i> method the JUnit thread will not normally do
 * so.
 * 
 * <p>Since a thread is now waiting on the frame, special care must
 * be taken to notify the thread when the window is finally closed.
 * JTestFrame does so by responding to window closing events and
 * then calling the {@link notifyAll} method.  The user can close the
 * window using the mouse or by pressing the ESC key.
 * 
 * <p>Lastly, the {@link #start} method is overloaded to 
 * accept a timeout.  This way the JUnit test can close the window
 * after a certain amount of time for more automated tests.
 */
public class JTestFrame extends JFrame implements KeyListener,
                                                  WindowListener {
   
   /** Create a new test frame. */
	public JTestFrame() {
		
	   setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	   
	   addKeyListener(this);
	   addWindowListener(this);
	}
	
	//-----------------------------------------------------------------
   // Utilities
   //
	
	/** Show frame until it's closed, then dispose of it. */
	public static void start(final JFrame frame) {
	   start(frame, -1);
	}
	
	/** Show frame for time milliseconds, then dispose of it. */
	public static void start(final JFrame frame, long time) {
	   
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            frame.pack();
            frame.setVisible(true);
         }
      });
      
      synchronized (frame) {
   	   try {
   	      if (time < 0) {
   	         frame.wait();
   	      } else {
   	         frame.wait(time);
   	      }
   	   } catch (InterruptedException e) {
   	      throw new RuntimeException("Interrupted while waiting!");
   	   }
      }
      
      frame.setVisible(false);
      frame.dispose();
	}
	
	//-----------------------------------------------------------------
   // Event handling
   //
	
	@Override
	public void keyPressed(KeyEvent e) {
	}
	
	/** Notify when Escape key is pressed. */
	@Override
	public void keyReleased(KeyEvent e) {
		
	   switch (e.getKeyCode()) {
	   case KeyEvent.VK_ESCAPE:
	      synchronized (this) {
	         notifyAll();
	      }
	      break;
	   }
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
   @Override
   public void windowActivated(WindowEvent arg0) {
   }
   
   @Override
   public void windowClosed(WindowEvent arg0) {
   }
   
   /** Notify when it's closing. */
   @Override
   public void windowClosing(WindowEvent arg0) {
      synchronized (this) {
         notifyAll();
      }
   }
   
   @Override
   public void windowDeactivated(WindowEvent arg0) {
   }
   
   @Override
   public void windowDeiconified(WindowEvent arg0) {
   }
   
   @Override
   public void windowIconified(WindowEvent arg0) {
   }
   
   @Override
   public void windowOpened(WindowEvent arg0) {
   }
}
