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
import javax.swing.JPanel;


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
public abstract class JGraphicsPanel extends JPanel
                                     implements MouseListener,
                                                MouseMotionListener,
                                                MouseWheelListener {
    
    private static final int DEFAULT_WIDTH = 512;
    private static final int DEFAULT_HEIGHT = 512;
    
    private boolean moveOrigin;
    private int button, zoom;
    private Point pan, location;
    
    /** Initializes the graphics panel. */
    public JGraphicsPanel() {
        this(false);
    }
    
    /** Initializes the graphics panel. */
    public JGraphicsPanel(boolean moveOrigin) {
        
        this.moveOrigin = moveOrigin;
        
        zoom = 1;
        location = new Point();
        pan = new Point();
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }
    
    /** Performs some default actions, then calls {@link #doPaint}. */
    public final void paint(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.clearRect(0, 0, getWidth(), getHeight());
        if (moveOrigin) {
            g2d.translate(0, getHeight());
        }
        g2d.translate(pan.x, pan.y);
        g2d.scale(zoom, zoom);
        doPaint(g2d);
    }
    
    //-----------------------------------------------------------------
    // Hooks
    //
    
    /** Hook for subclasses to paint into the panel using Java2D. */
    public abstract void doPaint(Graphics2D g2d);
    
    //-----------------------------------------------------------------
    // Event handling
    //
    
    @Override
    public void mouseClicked(MouseEvent arg0) {
    }
    
    @Override
    public void mouseEntered(MouseEvent arg0) {
    }
    
    @Override
    public void mouseExited(MouseEvent arg0) {
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
    
    /** Pan the panel when the middle mouse is dragged. */
    @Override
    public void mouseDragged(MouseEvent e) {
        
        if (button != 2) {
            return;
        }
        
        pan.x += e.getX() - location.x;
        pan.y += e.getY() - location.y;
        
        location.setLocation(e.getX(), e.getY());
        
        repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    /** Change the zoom factor when the mouse wheel is rotated. */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom -= e.getWheelRotation();
        repaint();
    }
}
