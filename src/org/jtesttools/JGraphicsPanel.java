package org.jtesttools;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;


/** Abstract JPanel that is well-suited for drawing with Java2D.
 * 
 * <p>Normally when painting into a window, you use a {@link JPanel}
 * and override its paint method.  However, you normally need to
 * cast the Graphics object to a Graphics2D one, and then you often
 * need to clear the whole area as well.  This can get tedious when
 * you do it over and over...
 * 
 * <p><i>JGraphicsPanel</i> helps by overriding the paint method
 * and performing these actions for you, then calling the {@link 
 * doPaint} method as a hook.  This way you still get the same 
 * functionality, but you have to do a bit less.
 */
public abstract class JGraphicsPanel extends JPanel {
    
    private static final int DEFAULT_WIDTH = 512;
    private static final int DEFAULT_HEIGHT = 512;
    
    private final Point2D.Float pan = new Point2D.Float();
    private final Point location = new Point();
    private int button = 0;
    private float zoom = 1;
    
    /** Initializes the graphics panel. */
    public JGraphicsPanel() {
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        new MouseObserver();
    }
    
    /** Performs some default actions, then calls {@link #doPaint}. */
    public final void paint(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.translate(cx, cy);
        g2d.scale(zoom, zoom);
        g2d.translate(-cx + pan.x, -cy + pan.y);
        doPaint(g2d);
    }
    
    //-----------------------------------------------------------------
    // Hooks
    //
    
    /** Hook for subclasses to paint into the panel using Java2D. */
    public abstract void doPaint(Graphics2D g2d);
    
    //------------------------------------------------------------
    // Nested classes
    //
    
    /**
     * Observer of mouse events.
     */
    class MouseObserver extends MouseInputAdapter {
        
        MouseObserver() {
            JGraphicsPanel.this.addMouseListener(this);
            JGraphicsPanel.this.addMouseMotionListener(this);
            JGraphicsPanel.this.addMouseWheelListener(this);
        }
        
        /** Pan the panel when the middle mouse is dragged. */
        @Override
        public void mouseDragged(MouseEvent e) {
            
            if (button != 2) {
                return;
            }
            
            pan.x += (e.getX() - location.x) / zoom;
            pan.y += (e.getY() - location.y) / zoom;
            
            location.setLocation(e.getX(), e.getY());
            
            repaint();
        }
        
        /** Store mouse state for later. */
        @Override
        public void mousePressed(MouseEvent e) {
            button = e.getButton();
            location.setLocation(e.getX(), e.getY());
        }
        
        /** Store mouse state for later. */
        @Override
        public void mouseReleased(MouseEvent e) {
            button = e.getButton();
            location.setLocation(e.getX(), e.getY());
        }
        
        /** Change the zoom factor when the mouse wheel is rotated. */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            
            int amount = e.getWheelRotation();
            
            if (amount < 0) {
                zoom *= 2;
            } else {
                zoom *= 0.5;
            }
            repaint();
        }
    }
}
