package org.jtesttools;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import org.jtesttools.JGraphicsPanel;
import org.jtesttools.JTestFrame;
import org.junit.Before;
import org.junit.Test;



public class JGraphicsPanelTest {
   
   Shape shape;
   
   @Before
   public void setUp() {
      shape = new Ellipse2D.Float(20, 20, 50, 50);
   }
   
   @Test
   public void testPaint() {
      
      JFrame frame = new JTestFrame();
      
      frame.add(new JGraphicsPanel() {
         @Override
         public void doPaint(Graphics2D g2d) {
            g2d.draw(shape);
         }
      });
      JTestFrame.start(frame);
   }
}
