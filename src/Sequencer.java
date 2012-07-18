import processing.core.PApplet;
import processing.serial.Serial;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Sequencer extends PApplet {
    JPanel mainPanel;
    Serial port;
    int[][] noteArray = new int[8][8];
    String[] notes = {    "c", "B", "A", "G", "F", "E", "D", "C"    };
    Trigger xylophone = new Trigger();

    static volatile boolean startFlag = false;
    ArrayList<JCheckBox> checkboxList; //cell storage
    private static final long serialVersionUID = 1L;

    public void setup()
    {
        for (int i = 0; i < Serial.list().length; i++)
        {
            System.out.println("Device No."+ i + " : " +Serial.list()[i]);
        }
        int device = 0;
        port = new Serial(this, Serial.list()[device], 19200);

        GUI();
    }


    public void GUI(){
        JFrame theFrame = new JFrame("Cordialatron");
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

        for (int i = 0; i < 64; i++)
        {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        }

        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);

    }
    public void draw() {}

    //================================================================================ ActionListeners
    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
//            Sequencer2 main = new Sequencer2();
            xylophone.run();
        }
    }
    class StopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            startFlag = false;
        }
    }
    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
            //clear();
        }
    }

    private class Trigger extends Thread{

        int[] noteROW;
        int r, c;
        int i = 0;
        int[] noteON = {0,1,2,3,4,5,6,7};

        public void run() {

            //fill matrix with binary data from checkbox grid
            for (r = 0; r < noteON.length; r++)
            {
                for (c = 0; c < noteON.length; c++)
                {
                    JCheckBox jc = checkboxList.get(c + (noteON.length*r));

                    //checkbox @ this beat selected
                    if (jc.isSelected())
                    {
                        noteArray[r][c] = 1; //put binary value in this slot to trigger solenoid
                    }
                    else
                        noteArray[r][c] = 0;
                    System.out.print(noteArray[r][c]);//debug
                }
                System.out.println();
            }

            int j=0;
            byte[] serialSend = new byte[8];

            //hit each note in the column
            for (j = 0; j < noteArray.length; j++)
            {
                noteROW = noteArray[j];
                for (i = 0; i < noteROW.length; i++)
                {
                    serialSend[i]=(byte)noteON[i]; //append column values

                    if (noteROW[i]==1)
                    {
                        System.out.println(noteON[i]);
                    }
                }
            }

            port.write(serialSend);

            try
            {
                Thread.sleep(1500);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}kkjjjj