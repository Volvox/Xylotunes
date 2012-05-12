import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Sequencer {
    
    JPanel mainPanel;
    ArrayList<JCheckBox> checkboxList; //cell storage
    SongData melody;
    JFrame theFrame;
    String[] notes = {    "c", "B", "A", "G", "F", "E", "D", "C"    };

    //aka xylophone keys
    int[] solenoids = {
            melody.getc(),
            melody.getB(),
            melody.getA(),
            melody.getG(),
            melody.getF(),
            melody.getE(),
            melody.getD(),
            melody.getC(),
    };



    public static void main(String args[]){
        new Sequencer().buildGUI();
    }


    //================================================================================ boring GUI stuff
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
        buttonBox.add(start);
        
        Box labels = new Box(BoxLayout.X_AXIS);
        for (int i = 0; i < 8; i++){
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
        
        for (int i = 0; i < 128; i++){
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);            
        }

        tuneUp();
        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true); 
        
        
    }

    //================================================================================ xyloTuner
    public void xyloTuner() {

        /*  8-element array [1 whole note] to hold values for one note, across all 8 beats.
            if the note is supposed to play on that beat, the val @ that element will
            be the key. If that note is NOT supposed to play on that beat, put a ZERO  */
        int[] noteList = null; 
        int r, c;
        melody.clear();

        //hit each note in the column
        for (c = 0; c < 8; c++) {
            noteList = new int[8];

            //set the key for the correct note to trigger a solenoid
            int key = solenoids[c];

            /* hit each beat in the given column of the note by scanning the rows :
     
                        D  (beat at 1/8)
                        D  (half note beat)
                        0
                        0
                        0
                        0
                        D  (beat at 2/8)
                        0
     
                .....   D    C
            */
                for (r = 0; r < 8; r++) {
                    JCheckBox jc = (JCheckBox) checkboxList.get(r + (16*c));

                    //checkbox @ this beat selected
                    if (jc.isSelected()){
                    noteList[r] = key; //put key value in this slot to trigger solenoid
                    }
                    else{
                        noteList[r] = melody._REST; //nothing should be triggered on this beat
                    }
                }   //close inner loop



            /*for all 8 beats of this note, send to method will compile and play the entire array
            needs to loop through list  and set key = index */

            SongData noteBeats = new SongData(key, noteList);
            //TODO: melody.Play() method
            //TODO: song looping?

        }   //close outer loop
    }


    //================================================================================ ActionListeners
    public class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
            xyloTuner();
        }
    }
    public class StopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            //TODO melody.Stop() method
        }
    }
    
}

