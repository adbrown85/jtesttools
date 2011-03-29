package org.jtesttools;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import org.jtesttools.JGraphicsPanel;
import org.jtesttools.JTestFrame;
import org.junit.Test;


public class JGraphicsPanelTest {
   
   private static final long WAIT_TIME = 1000;
   
   @Test
   public void testPaint() {
      
      final JFrame frame = new JTestFrame();
      final Shape shape = new Ellipse2D.Float(20, 20, 50, 50);
      
      frame.add(new JGraphicsPanel() {
         @Override
         public void doPaint(Graphics2D g2d) {
            g2d.draw(shape);
         }
      });
      JTestFrame.run(frame, WAIT_TIME);
   }
}
