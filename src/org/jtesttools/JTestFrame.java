package org.jtesttools;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Window that works well for testing with JUnit.
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
public class JTestFrame extends JFrame {
    
    /**
     * Creates a test frame.
     */
    public JTestFrame() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addListeners();
    }
    
    /**
     * Shows a frame until it's closed, then disposes of it.
     * 
     * @param frame JFrame to show, may be generic
     */
    public static void run(final JFrame frame) {
        run(frame, -1);
    }
    
    /**
     * Shows a frame for a certain amount of time, then disposes of it.
     * 
     * @param frame JFrame to show, may be generic
     * @param time Number of milliseconds to wait for frame to close
     * @throws RuntimeException if interrupted while waiting
     */
    public static void run(final JFrame frame, long time) {
        
        // Show the frame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
        
        // Wait for it to close
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
        
        // Dispose of frame
        frame.setVisible(false);
        frame.dispose();
    }
    
    //------------------------------------------------------------
    // Helpers
    //
    
    /**
     * Adds required listeners.
     */
    private void addListeners() {
        addKeyListener(new KeyObserver());
        addWindowListener(new WindowObserver());
    }
    
    /**
     * Wakes up all threads waiting on the frame.
     */
    private synchronized void wake() {
        notifyAll();
    }
    
    //------------------------------------------------------------
    // Nested classes
    //
    
    /**
     * Observer of key events.
     */
    class KeyObserver extends KeyAdapter {
        
        /**
         * Wakes up waiting threads when the Escape key is pressed.
         */
        @Override
        public void keyReleased(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                wake();
            }
        }
    }
    
    /**
     * Observer of window events.
     */
    class WindowObserver extends WindowAdapter {
        
        /**
         * Wakes up waiting threads when the window is closed.
         */
        @Override
        public void windowClosing(WindowEvent event) {
            wake();
        }
    }
}
