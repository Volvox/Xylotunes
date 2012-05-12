import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Sequencer {
    
    JPanel mainPanel;
    ArrayList<JCheckBox> checkboxList; //cell storage

    JFrame theFrame;
    String[] notes = {    "c", "B", "A", "G", "F", "E", "D", "C"    };

    //aka xylophone keys
    int[][] noteArray = {
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }
    };



    public static void main(String args[]){
        new Sequencer().buildGUI();
    }

    //================================================================================ GUI
    public void buildGUI(){
        theFrame = new JFrame("Cordialatron");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        checkboxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        
        JButton start = new JButton("Start");
        start.addActionListener(new StartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new StopListener());
        buttonBox.add(stop);

        JButton reset = new JButton("Reset");
        stop.addActionListener(new ResetListener());
        buttonBox.add(reset);
        
        Box labels = new Box(BoxLayout.X_AXIS);
        for (int i = 0; i < 8; i++)
        {
            labels.add(new Label(notes[i]));
        }
      
        
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, labels);
        
        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(8,8);
        grid.setVgap(1);
        grid.setHgap(2);

        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);
        
        for (int i = 0; i < 64; i++){
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);            
        }


        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);
        
    }

    //================================================================================ xyloTuner
    public void xyloTuner() {
   

        /*  8-element array [1 whole note] to hold values for one note, across all 8 beats.
            if the note is supposed to play on that beat, the val @ that element will
            be the key. If that note is NOT supposed to play on that beat, put a ZERO  */
        int[] noteROW;
        int r, c;

        int i = 0;
//        boolean ON = true;
        int[] noteON = {0,1,2,3,4,5,6,7};

        //fill matrix with binary data from user input
        for (r = 0; r < 8; r++)
        {
            for (c = 0; c < 8; c++)
            {
                JCheckBox jc = checkboxList.get(r + (8*c));

                //checkbox @ this beat selected
                if (jc.isSelected()){
                    noteArray[r][c] = 1; //put key value in this slot to trigger solenoid
                  }
            }
        }
        int j=0;
        while (j!=8){

            noteROW = noteArray[i];
            //hit each note in the column
            for (j = 0; j < noteROW.length; j++) {

                if (noteROW[j]==1)
                {
                System.out.print(noteON[j]);
                }
            //delay for a bit [tempo]
//             i=(i+1)%8;  //if i reaches 8 then goes back to 0, goes on forevearrrara

            }

        }


        //TODO: melody.Play() method    — need to figure out how to set .start() actionListener

    }

    public void clear() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                noteArray[r][c] = 0;
            }
        }
    }
    //================================================================================ ActionListeners
    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
            xyloTuner();
        }
    }
    class StopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            //TODO melody.Stop() method
        }
    }
    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
           clear();
        }
    }
}
