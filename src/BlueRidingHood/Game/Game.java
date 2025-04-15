package BlueRidingHood.Game;

import BlueRidingHood.DataBaseHandler.DataBaseHandler;
import BlueRidingHood.GameWindow.GameWindow;
import BlueRidingHood.Observer.Observer;
import BlueRidingHood.State.Game.ErrorState;
import BlueRidingHood.State.Game.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;


/*! \class Game
    \brief Clasa principală implementează jocul.

    Oferă metode pentru:\n
        -pornirea/oprirea jocului.\n
        -preluarea unei referințe către fereastra jocului.

    \note Implementează design patternul **Singleton**, **State** și **Observer**.
    \note Observer pentru GameState.

    Implementeaza Game - Loop.
 */
public class Game implements Runnable, Observer {
    private static Game game = null; /*!< Referință unică către joc.*/
    private GameWindow gameWindow;    /*!< Fereastra in care se va desena tabla jocului*/
    private boolean runState;   /*!< Flag ce starea firului de executie.*/
    private Thread gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private JFrame gameWindowFrame;  /*!< Referință către fereastra jocului.*/
    private GameState currentState; /*!< Referință către starea curentă.*/

    /*! \fn public Game(String title, int width, int height)
        \brief Constructor de inițializare al clasei Game.

        Inițializează variabilele locale.

        \param title Titlul ferestrei.
        \param width Lățimea ferestrei în pixeli.
        \param height Înălțimea ferestrei în pixeli.
     */
    protected Game() {
        gameWindow = new GameWindow();
        runState = false;
    }

    /*! \fn   public static Game provideGame()
           \brief Returnează referința unică a jocului.
    */
    public static Game provideGame() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    /*! \fn private void InitGame()
        \brief Metoda construiește fereastra jocului.

         Setează starea curentă a jocului la cea implicită. Verifică o posibilă eroare de început.

     */
    private void InitGame() {
        gameWindow = new GameWindow();
        gameWindow.BuildGameWindow();
        gameWindowFrame = gameWindow.getWindowFrame();
        gameWindowFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                StopGame();
                System.exit(0);
            }
        });
        currentState = GameState.getDefaultState();
        currentState.attach(this);

        if(GameState.getError())
        {
            GameState.setCurrentState(new ErrorState("Nu s-a putut încărca imaginea meniului!"));
            currentState=GameState.getCurrentState();
            currentState.notifyObservers();
        }
    }

    /*! \fn public void run()
        \brief Metoda ce va rula în thread-ul creat.
     */
    public void run()
    {
        InitGame();
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

        final int framesPerSecond = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

        while (runState) {
            curentTime = System.nanoTime();
            if ((curentTime - oldTime) > timeFrame) {
                Update();
                Draw();
                oldTime = curentTime;
            }
        }

    }

    /*! \fn public synchronized void start()
        \brief Creaza și pornește firul separat de execuție.

        Metoda trebuie să fie declarată synchronized pentru ca apelul acesteia să fie semaforizat.
     */
    public synchronized void StartGame()
    {
        System.out.println("Game started!");
        if (!runState) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        } else {
            return;
        }
    }

    /*! \fn public synchronized void stop()
        \brief Opreste executie thread-ului.

        Metoda trebuie să fie declarata synchronized pentru ca apelul acesteia să fie semaforizat.
     */
    private synchronized void StopGame()
    {
        System.out.println("Game stoped!");
        if (runState) {
            runState = false;
            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            return;
        }

    }

    /*! \fn private void Update()
        \brief Actualizează starea elementelor, prin starea curentă a jocului.

     */
    private void Update() {
        currentState.Update();
    }

    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice în fereastră, coresponzătoar starilor actualizate ale elementelor, prin starea curentă.

     */
    private void Draw() {
        BufferStrategy bufferStrategy = gameWindow.GetCanvas().getBufferStrategy();
        if (bufferStrategy == null) {
            try {
                gameWindow.GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assert bufferStrategy != null;
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.clearRect(0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight());
        gameWindowFrame.requestFocusInWindow();
        currentState.Draw(graphics);
        bufferStrategy.show();
        graphics.dispose();
    }

    /*! \fn public GameWindow getGameWindow()
        \brief Returnează o referință către fereastra jocului.
     */
    public GameWindow getGameWindow() {
        return gameWindow;
    }

    /*! \fn  public void updateObserver()
      \brief Primește actualizări legate de modificarea stării curente a jocului.

   */
    @Override
    public void updateObserver() {
        currentState = GameState.getCurrentState();
    }

    /*! \fn   public void quit()
      \brief Oprește execuția programului.
   */
    public void quit() {
        DataBaseHandler.getDataBaseHandler().close();
        gameWindowFrame.dispose();
        System.exit(0);
        StopGame();
    }
}

