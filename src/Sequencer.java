import gnu.io.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;




public class Sequencer extends Thread implements Runnable{
    SerialPort serialPort;
    static
    volatile boolean stopFlag = true;
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbmodemfd121", // Mac OS X

    };
    /** Buffered input stream from the port */
    private InputStream input;
    /** The output stream to the port */
    private OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;

    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;

    public boolean getON(){
       return this.ON;
    }
    public void setON(boolean onOrOff){
        this.ON = onOrOff;
    }
    JPanel mainPanel;
    ArrayList<JCheckBox> checkboxList; //cell storage
    private Object lock = new Object();
      private boolean ON = true;
     JFrame theFrame;
    String[] notes = {    "c", "B", "A", "G", "F", "E", "D", "C"    };

    int count = 0;



    int[][] noteArray = new int[8][8];




    @Override
    public void run() {
        while (!stopFlag){
             count++;
            /*  8-element array to hold values for one note, across all 8 beats.
if the note is supposed to play on that beat, the element value
will be a 1. If that note is NOT supposed to play on that beat, a zero.*/
            int[] noteROW;
            int r, c;

            int i = 0;
            //        boolean ON = true;
            int[] noteON = {0,1,2,3,4,5,6,7};

            //fill matrix with binary data from checkbox grid
            for (r = 0; r < noteON.length; r++)
            {
                for (c = 0; c < noteON.length; c++)
                {
                    JCheckBox jc = checkboxList.get(c + (noteON.length*r));

                    //checkbox @ this beat selected
                    if (jc.isSelected()){
                        noteArray[r][c] = 1; //put binary value in this slot to trigger solenoid

                    }
                    else noteArray[r][c] = 0;
                    System.out.print(noteArray[r][c]);//debug
                }
                System.out.println();
            }

            int j;
            try{
                while (!stopFlag){

                    //hit each note in the column
                    for (j = 0; j < noteArray.length; j++) {
                        noteROW = noteArray[j];
                        for (i = 0; i < noteArray.length; i++)
                        {
                            if (noteROW[i]==1)
                            {
                                System.out.println(noteON[i]);
                            }
                        }
                        //delay for a bit [tempo]
                        i=(i+1)%8;  //if i reaches 8 then goes back to 0, goes on forevearrrara
                        if(ON){
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }catch (Exception e){

            }
            }

    }



    //================================================================================ ActionListeners
    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
            run();
        }
    }
    class StopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            stopFlag = false;

        }
    }
    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
            //clear();
        }
    }

    private class XylophoneTrigger extends Thread {
        // The code represents the note name
        private char code;

        public XylophoneTrigger(char code) {
            this.code = code;
        }



//        @Override
//        public void run() {
//            //synchronized (lock)
//        }

    }






    public static void main(String args[]){
        SwingUtilities.invokeLater(new Runnable() {  //Note 1
            public void run() {
                Sequencer seq = new Sequencer();
                seq.buildGUI();

            }
        });
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
        background.add(BorderLayout.SOUTH, labels);

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

//
//    @Override
//    public void serialEvent(SerialPortEvent serialPortEvent) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }



}
