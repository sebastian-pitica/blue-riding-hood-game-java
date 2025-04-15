package BlueRidingHood.State.Game;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.DataBaseHandler.DataBaseHandler;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.GameWindow.GameWindow;
import BlueRidingHood.InputManager.KeyboardInputManager;
import BlueRidingHood.Memento.Memento;
import BlueRidingHood.Observer.Observer;
import BlueRidingHood.Observer.Subject;
import BlueRidingHood.UI.UI;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

/*! \class GameState
    \brief Interfață pentru stările jocului.

    Oferă metode pentru:\n
        -setarea stării curente a jocului.\n
        -preluarea valorilor pentru opțiunile de sunet/salvarea progresului.\n
        -desenarea elementelor caracteristice.\n
        -actualizarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.\n\
        -încărcarea specifică pentru starea de câștig/înfrângere/salvarea progresului.

    \note Implementează design patternul **State** și **Observer**.
    \note Observer pentru Map.
    \note Nu se poate subclasa HomeState, se vor produce bug-uri în funcția draw()
 */

public abstract class GameState implements Observer, Subject {
    protected static boolean playSound = false; /*!< Indică alegerea jucătorului pentru redarea sunetului.*/
    protected static boolean playerWantsSaves; /*!< Indică alegerea jucătorului pentru salvarea progresului.*/
    protected static GameWindow gameWindow; /*!< Referință la fereastra în care se desenează.*/
    protected static AudioHandler audioHandler; /*!< Referință la handlerul audio.*/
    protected static LinkedList<Observer> observers; /*!< Lista de observatori.*/
    protected static GameState currentState; /*!< Starea curentă a jocului.*/
    protected static Font arial100; /*!< Fontul cu care se vor scrie diverse mesaje.*/
    protected static Font arial40; /*!< Fontul cu care se vor scrie diverse mesaje.*/
    protected static DecimalFormat decimalFormat; /*!< Formatul în care se va face afișarea timpului.*/
    protected KeyboardInputManager keyboardInputManager; /*!< Referință la handlerul inputului de la tastatură.*/
    protected int mouseX; /*!< Coordonata x a mouse-ului.*/
    protected int mouseY; /*!< Coordonata y a mouse-ului.*/
    protected static Memento memento; /*!< Un memento pentru salvarea/încărcarea progresului.*/
    protected static boolean error = false; /*!< Indică apariția unei erori ce nu poate fi ignorată.*/

    /*! \fn public static GameState getDefaultState()
        \brief Setează starea curentă a jocului la cea implicită și o returnează.
    */
    public static GameState getDefaultState() {
        currentState = HomeState.getHomeMenuState();
        return currentState;
    }

    /*! \fn public static void checkedSave()
        \brief Verifică existența unei progres salvat și presupune
        că jucătorul dorește salvarea progresului în funție de acea valoare.
    */
    protected static void checkedSave() {
        playerWantsSaves = DataBaseHandler.getDataBaseHandler().checkSaveDB();
    }

    /*! \fn public static GameState getCurrentState()
        \brief Returnează starea curentă a jocului.
    */
    public static GameState getCurrentState() {
        if (currentState != null)
            return currentState;
        else
            return getDefaultState();
    }

    /*! \fn public static void setCurrentState(GameState currentState)
        \brief Setează starea curentă a jocului cu cea furnizată.
        \param currentState noua stare.
    */
    public static void setCurrentState(GameState currentState) {

        GameState.currentState = currentState;
    }

    /*! \fn public static boolean getPlaySound()
        \brief Returnează opțiunea jucătorului referitor la redarea de sunet.
    */
    public static boolean getPlaySound() {
        return playSound;
    }

    /*! \fn public static boolean getPlayerWantsSaves()
       \brief Returnează opțiunea jucătorului referitor la salvarea progresului.
    */
    public static boolean getPlayerWantsSaves() {
        return playerWantsSaves;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
       \brief  Deseneaza elementele grafice în fereastră, coresponzătoar starilor actualizate ale elementelor.
       \param graphics grafica pentru desenare.
    */
    public abstract void Draw(Graphics graphics);

    /*! \fn  public void Update()
       \brief Actualizează starea elementelor.
    */
    public void Update() {
    }

    /*! \fn public void clicked(int x, int y)
       \brief Preia coordonatele la care s-a făcut click și apelează handler-ul local.
       \param x coordonata x la care s-a făcut click
       \param y coordomata y la care s-a făcut click
    */
    public void clicked(int x, int y) {
        audioHandler.playSoundEffect(Sounds.CLICK.ordinal());
        this.mouseX = x;
        this.mouseY = y;
        mouseHandler();
    }

    /*! \fn public abstract void mouseHandler()
       \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click
    */
    protected abstract void mouseHandler();

    /*! \fn   public void attach(Observer observer)
     \brief Atașează observatorul dat.
     \param observer observatorul de atașat.
    */
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /*! \fn public void notifyObservers()
   \brief Notifică lista de observatori.
    */
    @Override
    public void notifyObservers() {
        if (observers != null)
            for (Observer observer : observers) {
                observer.updateObserver();
            }
    }

    /*! \fn  public void updateObserver()
      \brief Implementarea implicită pentru clasele ce nu sunt observatori ai hărții.
   */
    @Override
    public void updateObserver()
    //implementare default pentru stateurile care nu au nevoie sa fie map observeri
    {
    }

    /*! \fn public static void loadWin()
     \brief Încarcă starea de câștig.

        Execută toate acțiunile caracteristice.
    */
    public static void loadWin() {
        WonState won = WonState.getWonState();
        won.setScore(Player.getScore());
        won.setTotalPlayTime(UI.getTotalPlaytime());
        GameState.setCurrentState(won);
        won.notifyObservers();

        AudioHandler.playWinSounds();
        resetPlayState();
    }

    /*! \fn public static void loadLost()
     \brief Încarcă starea de înfrângereși.

          Execută toate acțiunile caracteristice.
    */
    public static void loadLost() {
        LostState lost = LostState.getLostState();
        lost.setScore(Player.getScore());
        lost.setTotalPlayTime(UI.getTotalPlaytime());
        GameState.setCurrentState(lost);
        lost.notifyObservers();

        AudioHandler.playLostSounds();
        resetPlayState();
    }

    /*! \fn public static void loadSaveGameState()
     \brief Încarcă starea de încărcare a salvării.

            Execută toate acțiunile caracteristice.
    */
    public static void loadSaveGameState() {
        audioHandler.stopAll();
        audioHandler.playMusic(Sounds.AMBIENT.ordinal());
        setCurrentState(SaveGameState.getSaveGameState());
        currentState.notifyObservers();
    }

    /*! \fn private static void resetPlayState()
     \brief Resetează starea de joc.

          Execută toate acțiunile caracteristice.
    */
    private static void resetPlayState() {
        PlayState.resetPlayState();
        GameState.getCurrentState().notifyObservers();
        audioHandler.playMusic(Sounds.AMBIENT.ordinal());
    }

    /*! \fn  public static boolean getError()
     \brief Returnează valoarea flag-ului error.
    */
    public static boolean getError()
    {
        return error;
    }


}
