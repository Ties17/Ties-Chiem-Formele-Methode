package ties.UI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import guru.nidi.graphviz.attribute.Image;

public class ImagePanel {

    private BufferedImage image;
    private JLabel picLabel;

    public ImagePanel(String path) {
        try {                
            image = ImageIO.read(new File(path));
            picLabel = new JLabel(new ImageIcon(image.getScaledInstance(400, 400, 2))); 
            
         } catch (IOException ex) {
              System.out.println(ex.getMessage());
         }
    }

    public void reDraw(String path) {
        try {                
            image = ImageIO.read(new File(path));
            picLabel.setIcon(new ImageIcon(image.getScaledInstance(400, 400, 2)));
            
         } catch (IOException ex) {
              System.out.println(ex.getMessage());
         }
    }

    public BufferedImage getImg() {
        return this.image;
    }

    public JLabel getPicLabel() {
        return this.picLabel;
    }
}
