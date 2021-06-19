package ties.UI;

import javax.imageio.ImageIO;
import javax.sound.sampled.SourceDataLine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


import com.kitfox.svg.Group;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

public class MainScreen {

    JFrame f;  

    JTextField regexField;
    ImagePanel imgNDFA;
    ImagePanel imgDFA;
    ImagePanel minImgNDFA;
    JTextArea language;

    public MainScreen() {  
        f = new JFrame();//creating instance of JFrame  
                
        regexField = new JTextField(); 
        regexField.addActionListener(new OnKeyEnterAction());
        imgNDFA = new ImagePanel("formele_methode/example/diagram.png");
        imgDFA = new ImagePanel("formele_methode/example/diagram2.png");   
        minImgNDFA = new ImagePanel("formele_methode/example/diagram.png");
        language = new JTextArea();
        
        // Define the panel to hold the buttons 
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(regexField, 1200, 1200, 1200)
            .addGroup(layout.createSequentialGroup()
                .addComponent(imgNDFA.getPicLabel(), 400, 400, 400)
                .addComponent(imgDFA.getPicLabel(), 400, 400, 400)
                .addComponent(minImgNDFA.getPicLabel(), 400, 400, 400)
            )
            .addComponent(language, 1200, 1200, 1200)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(regexField, 25, 25, 25)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(imgNDFA.getPicLabel(), 400, 400, 400)
                .addComponent(imgDFA.getPicLabel(), 400, 400, 400)
                .addComponent(minImgNDFA.getPicLabel(), 400, 400, 400)
            )
            .addComponent(language, 100, 100, 100)
        );

        
   
        f.setSize(1250,600);
        f.add(panel);
        //f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private final class OnKeyEnterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println(regexField.getText());
            //TODO: ADD logic
            //TODO: get new generated image
            imgNDFA.reDraw("formele_methode/example/diagram2.png");
        }
    }

}
