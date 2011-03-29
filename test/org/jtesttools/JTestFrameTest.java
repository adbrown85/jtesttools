package org.jtesttools;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jtesttools.JTestFrame;
import org.junit.Test;


public class JTestFrameTest {
   
   private static final long WAIT_TIME = 1000;
   
   @Test
   public void testRun() throws Exception {
      
      JFrame frame = new JTestFrame();
      JPanel panel = new JPanel();
      
      panel.setPreferredSize(new Dimension(600, 400));
      frame.add(panel);
      JTestFrame.start(frame, WAIT_TIME);
   }
}
