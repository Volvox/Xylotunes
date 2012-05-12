public class SongData {
    //================================================================================ constants
    public  static final int _REST   = 0;  // The cell is empty.
    boolean play = false;

    //================================================================================ fields
    private int       _maxRows;           // Number of rows. Set in constructor.
    private final int _maxCols = 8;      // Number of columns. Only 8 notes on xylophone
    
    //notes
    private int c;
    private int D;
    private int E;
    private int F;
    private int G;
    private int A;
    private int B;
    private int C;
    private int tempo; //bpm
    private int EIGHTH;
    private int HALF;
    int WHOLE;
    
    private int[][] _grid;      // The grid values.

    //================================================================================ constructor

    public SongData(int[] notesList){
        this(notesList, 200);
    }
    public SongData(int[] noteList, int tempo)  {
        //_maxRows = rows;  //8 rows per whole note
        this.tempo = 200; 
        this.tempo = tempo;
        _grid = new int[_maxRows][_maxCols];
        clear();

        c = 4;
        D = 10;
        F = 8;
        E = 9;
        G = 7;
        A = 6;
        B = 5;
        C = 11;
        HALF = this.tempo *2;
        WHOLE = HALF*2;
        EIGHTH = this.tempo /8;
    }

    //================================================================================ reset
    /** Clears board to initial state. **/
    public void clear() {
        for (int r = 0; r < _maxRows; r++) {
            for (int c = 0; c < _maxCols; c++) {
                _grid[r][c] = _REST;
            }
        }

    }


    //================================================================================ noteOn
    //gets called each time a btn is clicked on the grid
    public int noteOn(int x, int y, int[][] currentGrid) {
        // Y = note length
        // X = letter
        int cell = currentGrid[x][y];
        int note = x; //index of x position corresponds to a letter note
        if (cell==_REST){
            switch(note) {
                    case 0: cell = c;
                        break;
                    case 1: cell = D;
                        break;
                    case 2: cell = E;
                        break;      
                    case 3: cell = F;
                        break;
                    case 4: cell = c;
                        break;
                    case 5: cell = D;
                        break;
                    case 6: cell = E;
                        break;
                    case 7: cell = C;
                        break;
            }
        }
        else {
            cell = _REST; //
        }
       return cell;
    }



    //================================================================================ getters&setters
    public int getF() {
        return F;
    }

    public void setF(int f) {
        F = f;
    }
    public int getc() {
        return c;
    }

    public void setc(int c) {
        this.c = c;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
}

