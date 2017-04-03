package sebastiengodbout.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //region DECLARATION
    Random rand = new Random();

    // CONST
    private final int GRID_SQR_SIZE = 10;
    private final int INITIAL_NB_MINE = 10;

    private final int TILED = 0;
    private final int EMPTY = 1;
    private final int MINE = 2;
    private final int MINE_SELECTED = 3;
    private final int FLAG = 2;
    private final int FLAG_ERROR = 3;

    // PRIVATE
    private int[][] mineArray = new int[GRID_SQR_SIZE][GRID_SQR_SIZE];
    private int[][] flagArray = new int[GRID_SQR_SIZE][GRID_SQR_SIZE];

    private int nbUnknownMineLocation = INITIAL_NB_MINE;
    private int nbLeftEmptyCell = (GRID_SQR_SIZE * GRID_SQR_SIZE) - INITIAL_NB_MINE;

    private boolean isGameRunning = false;
    //endregion

    // START
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Set_AllButtonListener();

        if(savedInstanceState == null)
        {
            InitGrid();
            UpdateMineCounter();

            isGameRunning = true;
        }
        else
        {
            ReloadSave(savedInstanceState);
        }
    }

    //region -SAVE/LOAD METHODE-
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Save the user's current game state
        savedInstanceState.putSerializable("MineArray", mineArray);
        savedInstanceState.putSerializable("FlagArray", flagArray);
        savedInstanceState.putBoolean("GameState", isGameRunning);
        savedInstanceState.putInt("NbUnknownMineLocation", nbUnknownMineLocation);
        savedInstanceState.putInt("NbLeftEmptyCell", nbLeftEmptyCell);

        super.onSaveInstanceState(savedInstanceState);
    }


    private void ReloadSave(Bundle savedInstanceState)
    {
        ReloadValue(savedInstanceState);
        ReloadButton();

        UpdateMineCounter();
    }

    private void ReloadValue(Bundle savedInstanceState)
    {
        mineArray = (int[][]) savedInstanceState.getSerializable("MineArray");
        flagArray = (int[][]) savedInstanceState.getSerializable("FlagArray");
        isGameRunning = savedInstanceState.getBoolean("GameState");
        nbUnknownMineLocation = savedInstanceState.getInt("NbUnknownMineLocation");
        nbLeftEmptyCell = savedInstanceState.getInt("NbLeftEmptyCell");
    }

    private void ReloadButton()
    {
        for(int i = 0; i < GRID_SQR_SIZE; i++)
        {
            for(int j = 0; j < GRID_SQR_SIZE; j++)
            {
                Button button = Get_ButtonByCoordinates(j,i);

                ReloadDrawable(button, i, j);
                ReloadEndGameDrawable(button, i, j);
            }
        }
    }

    private void ReloadDrawable(Button button, int i, int j)
    {
        if (flagArray[i][j] == FLAG)
        {
            button.setBackgroundResource(R.drawable.flag);
        }
        else if (mineArray[i][j] == TILED)
        {
            button.setBackgroundResource(R.drawable.filled);
        }
        else if (mineArray[i][j] == EMPTY)
        {
            int nbMineAround = 0;

            nbMineAround = VerifyAdjacentIndex(nbMineAround, i, j);

            nbMineAround = VerifyDiagonalIndex(nbMineAround, i, j);

            switch (nbMineAround)
            {
                case 0:  button.setBackgroundResource(R.drawable.empty_0);
                    break;
                case 1:  button.setBackgroundResource(R.drawable.empty_1);
                    break;
                case 2:  button.setBackgroundResource(R.drawable.empty_2);
                    break;
                case 3:  button.setBackgroundResource(R.drawable.empty_3);
                    break;
                case 4:  button.setBackgroundResource(R.drawable.empty_4);
                    break;
                case 5:  button.setBackgroundResource(R.drawable.empty_5);
                    break;
                case 6:  button.setBackgroundResource(R.drawable.empty_6);
                    break;
                case 7:  button.setBackgroundResource(R.drawable.empty_7);
                    break;
                case 8:  button.setBackgroundResource(R.drawable.empty_8);
                    break;
                default: break;
            }
        }
    }

    private void ReloadEndGameDrawable(Button button, int i, int j)
    {
        if(!isGameRunning)
        {
            if (mineArray[i][j] == MINE_SELECTED)
            {
                button.setBackgroundResource(R.drawable.mine_2);
            }
            else if (flagArray[i][j] == FLAG_ERROR)
            {
                button.setBackgroundResource(R.drawable.mine_3);
            }
            else if (mineArray[i][j] == MINE)
            {
                if(flagArray[i][j] != FLAG)
                {
                    button.setBackgroundResource(R.drawable.mine_1);
                }
            }
        }
    }
    //endregion

    //region -GETTER/SETTER METHODE-
    private Button Get_ButtonByCoordinates(int x, int y)
    {
        Button retVal = null;
        try
        {
            final Field fld = R.id.class.getField("cell_" + y + "_" + x);
            final int id = fld.getInt(R.id.class);
            retVal = (Button) findViewById(id);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return retVal;
    }

    private Button Get_ReplayButton()
    {
        Button retVal = null;
        try
        {
            final Field fld = R.id.class.getField("replay");
            final int id = fld.getInt(R.id.class);
            retVal = (Button) findViewById(id);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return retVal;
    }

    private TextView Get_MineCounter()
    {
        TextView retVal = null;
        try
        {
            final Field fld = R.id.class.getField("mineCounter");
            final int id = fld.getInt(R.id.class);
            retVal = (TextView) findViewById(id);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        return retVal;
    }

    private void Set_AllButtonListener()
    {
        // Grid Buttons
        for (int i = 0; i < GRID_SQR_SIZE; ++i)
        {
            for (int j = 0; j < GRID_SQR_SIZE; ++j)
            {
                final int x = j;
                final int y = i;

                Button button = Get_ButtonByCoordinates(x, y);

                button.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        OnCellClicked(x, y);
                    }
                });

                button.setOnLongClickListener(new View.OnLongClickListener()
                {
                    public boolean onLongClick(View v)
                    {
                        OnCellLongClicked(x, y);
                        return true;
                    }
                });
            }
        }

        // Replay Button
        Button replayBtn = Get_ReplayButton();
        replayBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                OnReplayClicked();
            }
        });
    }

    private void Set_FilledGridButton()
    {
        for(int i = 0; i < GRID_SQR_SIZE; i++)
        {
            for(int j = 0; j < GRID_SQR_SIZE; j++)
            {
                Button gridButton = Get_ButtonByCoordinates(j,i);
                gridButton.setBackgroundResource(R.drawable.filled);
            }
        }
    }
    //endregion

    //region -INITIALISATION METHODE-
    private void InitGrid()
    {
        List listOfAllCoord = new LinkedList();

        // Initialise the array and save all the grid coord
        InitEmptyAndSaveCoord(listOfAllCoord);;

        // Generate all the mines randomly in the Mine array
        InitMine(listOfAllCoord);
    }

    private void InitEmptyAndSaveCoord(List listOfAllCoord)
    {
        for(int i = 0; i < GRID_SQR_SIZE; i++)
        {
            for(int j = 0; j < GRID_SQR_SIZE; j++)
            {
                mineArray[i][j] = TILED;
                flagArray[i][j] = EMPTY;

                // Storing the Y and X position
                List listOfPosition = new LinkedList();
                listOfPosition.add(i);
                listOfPosition.add(j);
                listOfAllCoord.add(listOfPosition);
            }
        }
    }

    private void InitMine(List listOfAllCoord)
    {
        for(int i = 0; i < INITIAL_NB_MINE; i++)
        {
            // Getting a random available coord to spawn a mine...
            final int randomCoord = rand.nextInt(listOfAllCoord.size());
            List tempList = (List) listOfAllCoord.get(randomCoord);

            // Getting the value from the list...
            int valueX = (int) tempList.get(1);
            int valueY = (int) tempList.get(0);

            // Removing the coord from the available coord
            listOfAllCoord.remove(randomCoord);

            mineArray[valueY][valueX] = MINE;
        }
    }
    //endregion


    //region -BUTTON METHODE-
    public void OnCellClicked(int x, int y)
    {
        Log.d("CellClick", "OnCellClicked(" + y + ", " + x + ")");

        if(isGameRunning)
        {
            Button clickedButton = Get_ButtonByCoordinates(x,y);

            if(flagArray[y][x] == EMPTY)
            {
                if (mineArray[y][x] == TILED)
                {
                    Reveal(clickedButton, y, x);
                }
                else if (mineArray[y][x] == MINE)
                {
                    Defeat(clickedButton, y, x);
                }
            }
        }
    }

    private void OnCellLongClicked(int x, int y)
    {
        Log.d("LongCellClick", "LongToutch succesfull");

        if(isGameRunning)
        {
            if (mineArray[y][x] != EMPTY)
            {
                Button longClickedButton = Get_ButtonByCoordinates(x, y);

                if (flagArray[y][x] == EMPTY)
                {
                    flagArray[y][x] = FLAG;
                    longClickedButton.setBackgroundResource(R.drawable.flag);
                    nbUnknownMineLocation--;
                }
                else
                {
                    flagArray[y][x] = EMPTY;
                    longClickedButton.setBackgroundResource(R.drawable.filled);
                    nbUnknownMineLocation++;
                }

                UpdateMineCounter();
            }
        }
    }

    private void OnReplayClicked()
    {
        Log.d("Example2", "replay resquested");

        InitGrid();
        Set_FilledGridButton();

        nbLeftEmptyCell = (GRID_SQR_SIZE * GRID_SQR_SIZE) - INITIAL_NB_MINE;

        nbUnknownMineLocation = INITIAL_NB_MINE;
        UpdateMineCounter();

        isGameRunning = true;
    }
    //endregion

    //region -GAMEFLOW METHODE-
    private void Reveal(Button button, int y, int x)
    {
        if(flagArray[y][x] != FLAG)
        {
            int nbMineAround = 0;
            boolean chainReveal = false;

            nbMineAround = VerifyAdjacentIndex(nbMineAround, y, x);

            nbMineAround = VerifyDiagonalIndex(nbMineAround, y, x);

            switch (nbMineAround)
            {
                case 0:  button.setBackgroundResource(R.drawable.empty_0);
                    chainReveal = true;
                    break;
                case 1:  button.setBackgroundResource(R.drawable.empty_1);
                    break;
                case 2:  button.setBackgroundResource(R.drawable.empty_2);
                    break;
                case 3:  button.setBackgroundResource(R.drawable.empty_3);
                    break;
                case 4:  button.setBackgroundResource(R.drawable.empty_4);
                    break;
                case 5:  button.setBackgroundResource(R.drawable.empty_5);
                    break;
                case 6:  button.setBackgroundResource(R.drawable.empty_6);
                    break;
                case 7:  button.setBackgroundResource(R.drawable.empty_7);
                    break;
                case 8:  button.setBackgroundResource(R.drawable.empty_8);
                    break;
                default: break;
            }

            mineArray[y][x] = EMPTY;
            nbLeftEmptyCell--;

            if(nbLeftEmptyCell == 0)
            {
                Victory();
            }

            if(chainReveal)
            {
                RevealAdjacentIndex(y, x);;
                RevealDiagonalIndex(y, x);;
            }
        }
    }

    private int VerifyAdjacentIndex(int nbMineAround, int y, int x)
    {
        // Can verify Top
        if(y > 0)
        {
            // TOP
            if(mineArray[y - 1][x] == MINE)
            {
                nbMineAround++;
            }
        }
        // Can verify Bottom
        if(y < GRID_SQR_SIZE - 1)
        {
            // BOTTOM
            if(mineArray[y + 1][x] == MINE)
            {
                nbMineAround++;
            }
        }
        // Can verify Left
        if(x > 0)
        {
            // LEFT
            if(mineArray[y][x - 1] == MINE)
            {
                nbMineAround++;
            }
        }
        // Can verify Right
        if(x < GRID_SQR_SIZE - 1)
        {
            // RIGHT
            if(mineArray[y][x + 1] == MINE)
            {
                nbMineAround++;
            }
        }

        return nbMineAround;
    }

    private int VerifyDiagonalIndex(int nbMineAround, int y, int x)
    {
        // Can verify Top Left
        if(y > 0 && x > 0)
        {
            // TOP LEFT
            if(mineArray[y - 1][x - 1] == MINE)
            {
                nbMineAround++;
            }
        }
        // Can verify Top Right
        if(y > 0 && x < GRID_SQR_SIZE - 1)
        {
            // TOP RIGHT
            if(mineArray[y - 1][x + 1] == MINE)
            {
                nbMineAround++;
            }
        }
        // Can verify Bottom Left
        if(y < GRID_SQR_SIZE - 1 && x > 0)
        {
            // BOTTOM LEFT
            if(mineArray[y + 1][x - 1] == MINE)
            {
                nbMineAround++;
            }
        }
        // Can verify Bottom Right
        if(y < GRID_SQR_SIZE - 1 && x < GRID_SQR_SIZE - 1)
        {
            // BOTTOM RIGHT
            if(mineArray[y + 1][x + 1] == MINE)
            {
                nbMineAround++;
            }
        }

        return nbMineAround;
    }

    private void RevealAdjacentIndex(int y, int x)
    {
        // Can verify Top
        if(y > 0)
        {
            // TOP:
            if (mineArray[y - 1][x] == TILED)
            {
                Reveal(Get_ButtonByCoordinates(x, y - 1), y - 1, x);
            }
        }

        // Can verify Right
        if(x < GRID_SQR_SIZE - 1)
        {
            // RIGHT:
            if (mineArray[y][x + 1] == TILED)
            {
                Reveal(Get_ButtonByCoordinates(x + 1, y), y, x + 1);
            }
        }

        // Can verify Bottom
        if(y < GRID_SQR_SIZE - 1)
        {
            // BOTTOM:
            if (mineArray[y + 1][x] == TILED)
            {
                Reveal(Get_ButtonByCoordinates(x, y + 1), y + 1, x);
            }
        }

        // Can verify Left
        if(x > 0)
        {
            // LEFT:
            if (mineArray[y][x - 1] == TILED)
            {
                Reveal(Get_ButtonByCoordinates(x - 1, y), y, x - 1);
            }
        }
    }

    private void RevealDiagonalIndex(int y, int x)
    {
        // Can verify Top Left
        if(y > 0 && x > 0)
        {
            // TOP LEFT
            if(mineArray[y - 1][x - 1] == TILED)
            {
                Reveal(Get_ButtonByCoordinates(x - 1, y - 1), y - 1, x - 1);
            }
        }
        // Can verify Top Right
        if(y > 0 && x < GRID_SQR_SIZE - 1)
        {
            // TOP RIGHT
            if(mineArray[y - 1][x + 1] == TILED)
            {
                Reveal(Get_ButtonByCoordinates(x + 1, y - 1), y - 1, x + 1);
            }
        }
        // Can verify Bottom Left
        if(y < GRID_SQR_SIZE - 1 && x > 0)
        {
            // BOTTOM LEFT
            if(mineArray[y + 1][x - 1] == TILED)
            {
                Reveal(Get_ButtonByCoordinates(x - 1, y + 1), y + 1, x - 1);
            }
        }
        // Can verify Bottom Right
        if(y < GRID_SQR_SIZE - 1 && x < GRID_SQR_SIZE - 1)
        {
            // BOTTOM RIGHT
            if(mineArray[y + 1][x + 1] == TILED
                    )
            {
                Reveal(Get_ButtonByCoordinates(x + 1, y + 1), y + 1, x + 1);
            }
        }
    }

    private void UpdateMineCounter()
    {
        TextView mineCounter = null;

        mineCounter = Get_MineCounter();

        if(nbUnknownMineLocation < 10)
        {
            if(nbUnknownMineLocation < 0)
            {
                nbUnknownMineLocation = 0;
            }

            mineCounter.setText("Mines Restantes : 0" + nbUnknownMineLocation);
        }
        else
        {
            mineCounter.setText("Mines Restantes : " + nbUnknownMineLocation);
        }

    }
    //endregion

    //region -END GAME METHODE-
    private void Victory()
    {
        for(int i = 0; i < GRID_SQR_SIZE; i++)
        {
            for(int j = 0; j < GRID_SQR_SIZE; j++)
            {
                if(mineArray[i][j] == MINE)
                {
                    Button gridButton = Get_ButtonByCoordinates(j,i);
                    gridButton.setBackgroundResource(R.drawable.flag);
                    flagArray[i][j] = FLAG;
                }
            }
        }

        nbUnknownMineLocation = 0;
        UpdateMineCounter();

        isGameRunning = false;
    }

    private void Defeat(Button clickedButton, int y, int x)
    {
        // Revealing all the mine
        for(int i = 0; i < GRID_SQR_SIZE; i++)
        {
            for(int j = 0; j < GRID_SQR_SIZE; j++)
            {
                Button button = Get_ButtonByCoordinates(j,i);

                if(mineArray[i][j] == MINE)
                {
                    if(flagArray[i][j] == EMPTY)
                    {
                        button.setBackgroundResource(R.drawable.mine_1);
                    }
                }

                if(flagArray[i][j] == FLAG && mineArray[i][j] != MINE)
                {
                    button.setBackgroundResource(R.drawable.mine_3);
                    flagArray[i][j] = FLAG_ERROR;
                }
            }
        }

        // Reveal the exploded mine
        clickedButton.setBackgroundResource(R.drawable.mine_2);
        mineArray[y][x] = MINE_SELECTED;

        nbUnknownMineLocation = 0;
        UpdateMineCounter();

        isGameRunning = false;
    }
    //endregion
}