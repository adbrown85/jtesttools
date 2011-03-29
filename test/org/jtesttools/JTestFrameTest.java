package org.jtesttools;

import java.awt.Graphics2D;
import javax.swing.JFrame;
import org.jtesttools.JGraphicsPanel;
import org.jtesttools.JTestFrame;
import org.junit.Test;


public class JTestFrameTest {
   
   @Test
   public void testRun() throws Exception {
      
      JFrame frame = new JTestFrame();
      
      frame.add(new JGraphicsPanel() {
         @Override
         public void doPaint(Graphics2D g2d) {
            
         }
      });
      JTestFrame.start(frame);
   }
}
