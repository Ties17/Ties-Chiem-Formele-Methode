package ties.UI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ties.Machine;
import ties.RegExpression.RegExParser;
import ties.RegExpression.RegExpresion;

public class MainScreen {

    JFrame f;

    JTextField regexField;
    ImagePanel imgNDFA;
    ImagePanel imgDFA;
    ImagePanel imgDFAmin;
    JTextArea language;

    public MainScreen() {
        f = new JFrame();// creating instance of JFrame

        regexField = new JTextField();
        regexField.addActionListener(new OnKeyEnterAction());
        imgNDFA = new ImagePanel("example/diagram.png");
        imgDFA = new ImagePanel("example/diagram2.png");
        imgDFAmin = new ImagePanel("example/diagram.png");
        language = new JTextArea();
        language.setLineWrap(true);

        // Define the panel to hold the buttons
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(regexField, 1200, 1200, 1200)
                        .addGroup(layout.createSequentialGroup().addComponent(imgNDFA.getPicLabel(), 400, 400, 400)
                                .addComponent(imgDFA.getPicLabel(), 400, 400, 400)
                                .addComponent(imgDFAmin.getPicLabel(), 400, 400, 400))
                        .addComponent(language, 1200, 1200, 1200));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(regexField, 25, 25, 25))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(imgNDFA.getPicLabel(), 400, 400, 400)
                        .addComponent(imgDFA.getPicLabel(), 400, 400, 400)
                        .addComponent(imgDFAmin.getPicLabel(), 400, 400, 400))
                .addComponent(language, 100, 100, 100));

        f.setSize(1250, 600);
        f.add(panel);
        // f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private final class OnKeyEnterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(regexField.getText());
            // TODO: ADD logic
            // TODO: get new generated image

            RegExParser parser = new RegExParser();

            System.out.println("Parsing String");
            RegExpresion regex = parser.parseString(regexField.getText());

            System.out.println("Doing Thompson conversion");
            Machine<Integer> NDFA = regex.thompsonConvert();
            NDFA.drawName("NDFA");
            imgNDFA.reDraw("example/NDFA.png");

            System.out.println("Converting NDFA to DFA");
            Machine<String> DFA = NDFA.toDFA();
            DFA.drawName("DFA");
            imgDFA.reDraw("example/DFA.png");

            System.out.println("Minimizing DFA");
            Machine<Character> DFAmin = DFA.minimize();
            DFAmin.drawName("DFAmin");
            imgDFAmin.reDraw("example/DFA.png");
            
            
            

            language.setText(DFAmin.getLanguageForLength(10).toString());
        }
    }

}
