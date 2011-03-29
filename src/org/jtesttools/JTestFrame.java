package org.jtesttools;

import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * JFrame well-suited for use in JUnit tests.
 * 
 * <p><i>JTestFrame</i> makes using windows in unit tests easy by
 * performing several tasks for the developer.
 * 
 * <p>Most importantly, it allows the developer to open a window and
 * have JUnit wait for it to close.  Normally this is not a concern
 * with a normal <tt>main</tt> method because the JVM waits for the
 * Swing thread to finish before exiting.  However, JUnit exits as
 * soon as the main thread gets to the end of the test cases.
 * Therefore windows in JUnit tests are closed immediately, or never
 * show up in the first place.
 * 
 * <p><i>JTestFrame</i> approaches this problem from two directions.
 * First, it provides static <i>run</i> methods that show a frame and
 * then call {@link #wait()} on it.  When a test launches the window
 * in this manner, the JUnit thread pauses.  Second, when an instance
 * of <i>JTestFrame</i> is closed, instead of exiting, it hides itself
 * and calls {@link notifyAll()}.  The paused JUnit thread therefore
 * restarts, disposes of the frame, and continues onto the next test.
 * 
 * <p>Besides this important functionality, <i>JTestFrame</i> also
 * <ul>
 *    <li>allows the user to close the window by pressing the Escape key,
 *    <li>can stop waiting for the window after a certain amount of time, and
 *    <li>packs the frame if an explicit size has not been set.
 * </ul>
 */
public class JTestFrame extends JFrame {
    
    private static final long DEFAULT_WAIT_TIME = 2000;
    private static final String DEFAULT_TITLE = "Test";
    private static final int DEFAULT_CLOSE_KEY = KeyEvent.VK_ESCAPE;
    private static final int DEFAULT_LOCATION_X = 40;
    private static final int DEFAULT_LOCATION_Y = 40;
    
    /**
     * Creates a test frame.
     */
    public JTestFrame() {
        this(DEFAULT_TITLE);
    }
    
    /**
     * Creates a test frame with a title.
     * 
     * @param title Text to show on window
     */
    public JTestFrame(String title) {
        super(title);
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocation(DEFAULT_LOCATION_X, DEFAULT_LOCATION_Y);
        
        addKeyListener(new KeyObserver());
        addWindowListener(new WindowObserver());
    }
    
    /**
     * Shows a frame for a short amount of time, then disposes of it.
     * 
     * @param frame JFrame to show, may be generic
     */
    public static void run(final JFrame frame) {
        run(frame, DEFAULT_WAIT_TIME);
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
                if (!hasSize(frame)) {
                    frame.pack();
                }
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
    
    /**
     * Shows a canvas in a temporary frame for a short amount of time.
     * 
     * @param canvas Custom canvas object
     */
    public static void run(final Canvas canvas) {
        run(canvas, DEFAULT_WAIT_TIME);
    }
    
    /**
     * Shows a canvas in a temporary frame for a certain amount of time.
     * 
     * @param canvas Custom canvas object
     * @param time Number of milliseconds to wait for frame to close
     * @throws RuntimeException if interrupted while waiting
     */
    public static void run(final Canvas canvas, long time) {
        
        final JFrame frame = new JTestFrame();
        
        frame.add(canvas);
        run(frame, time);
    }
    
    //------------------------------------------------------------
    // Helpers
    //
    
    /**
     * Wakes up all threads waiting on the frame.
     */
    private synchronized void wake() {
        notifyAll();
    }
    
    /**
     * Checks if a size has been specified for a frame.
     * 
     * @param frame JFrame to check, may be generic
     * @return <tt>true</tt> if frame has a width or height
     */
    private static boolean hasSize(JFrame frame) {
       
       int width = frame.getWidth();
       int height = frame.getHeight();
       
       return (width > 0) || (height > 0);
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
            if (event.getKeyCode() == DEFAULT_CLOSE_KEY) {
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
