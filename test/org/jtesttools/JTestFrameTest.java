package org.jtesttools;

import java.awt.Canvas;
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
        JTestFrame.run(frame, WAIT_TIME);
    }
    
    @Test
    public void testRunCanvas() throws Exception {
        
        Canvas canvas = new Canvas();
        
        canvas.setPreferredSize(new Dimension(600, 400));
        JTestFrame.run(canvas, WAIT_TIME);
    }
    
    @Test
    public void testRunPanel() throws Exception {
        
        JPanel panel = new JPanel();
        
        panel.setPreferredSize(new Dimension(600, 400));
        JTestFrame.run(panel, WAIT_TIME);
    }
}
