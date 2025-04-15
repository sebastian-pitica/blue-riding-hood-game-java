package BlueRidingHood.State.Game;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.DataBaseHandler.DataBaseHandler;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Game.Game;
import BlueRidingHood.Graphics.Assets;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

/*! \class HomeState
    \brief Starea de acasă (implicită) a jocului.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

    \note Implementează design patternul **State** și **Singleton**.
 */

public class HomeState extends GameState {
    private static HomeState homeState = null; /*!< Referință unică către starea implicită.*/
    private final Image homeMenu; /*!< Imaginea stării.*/
    private String name = "defaultUser"; /*!< Numele implicit oferit jucătorului.*/

    /*! \fn protected HomeState()
         \brief Constructorul clasei HomeState.

         Inițializează referințele și variabilele locale, alături de aseturile secundare.
    */
    protected HomeState() {
        Game game = Game.provideGame();
        Assets.InitSecond();
        homeMenu = Assets.stateImages[GameStates.HOMEMENU.ordinal()];
        gameWindow = game.getGameWindow();
        audioHandler = AudioHandler.getAudioHandler();
        observers = new LinkedList<>();
        arial100 = new Font("Arial Bold", Font.BOLD, 100);
        arial40 = new Font("Arial Bold", Font.BOLD, 40);
        decimalFormat = new DecimalFormat("#0.00");
        checkedSave();
    }

    /*! \fn public static HomeState getHomeMenuState()
         \brief Returnează referința unică la starea acasă.
    */
    public static HomeState getHomeMenuState() {
        if (homeState == null) {
            homeState = new HomeState();
        }

        return homeState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
       \brief  Desenează imaginea specifică stării acasă.
       \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(homeMenu, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
    }


    /*! \fn public abstract void mouseHandler()
       \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click

       Poate actualiza starea jocului la starea de joc, încărcare fără/cu conținut, controale și starea de ieșire.
       Poate porni/opri opțiunile de redare a sunetului și salvarea progresului.
    */
    @Override
    public void mouseHandler() {
        //7,5-8,5 (x)
        if (this.mouseX >= 7.5 * 64 && this.mouseX <= 8.5 * 64) {
            //7-8 (y)
            if (this.mouseY >= 7 * 64 && this.mouseY <= 8 * 64) {
                if (playerWantsSaves) {
                    name = JOptionPane.showInputDialog(gameWindow.getWindowFrame(), "Username: ", null);
                }
                GameState.setCurrentState(PlayState.getPlayState());
                Player.getPlayer().setUsername(name);
                if(!error)
                    notifyObservers();
            } else {
                //8,5 - 9,5 (y)
                if (this.mouseY >= 8.5 * 64 && this.mouseY <= 9.5 * 64) {
                    if (DataBaseHandler.isSaved()) {
                        GameState.setCurrentState(LoadGameState.getLoadGameSubState());
                    } else {
                        GameState.setCurrentState(LoadNoGameState.getLoadNoGameSubState());
                    }
                    notifyObservers();

                } else {
                    //10 - 11 (y)
                    if (this.mouseY >= 10 * 64 && this.mouseY <= 11 * 64) {
                        GameState.setCurrentState(ControlsState.getControlsSubState());
                        notifyObservers();

                    } else {
                        //11.5 - 12.5  (y)
                        if (this.mouseY >= 11.5 * 64 && this.mouseY <= 12.5 * 64) {
                            GameState.setCurrentState(QuitState.getQuitState());
                            notifyObservers();

                        }
                    }
                }
            }
        } else {
            //17.5 - 18.5 (x)
            if (this.mouseX >= 17.5 * 64 && this.mouseX <= 18.5 * 64) {
                //7-8 (y)

                if (this.mouseY >= 7 * 64 && this.mouseY <= 8 * 64) {
                    playSound = !playSound;
                    if (!playSound) {
                        audioHandler.playSoundEffect(Sounds.OFF.ordinal());
                        audioHandler.stop(Sounds.AMBIENT.ordinal());
                    } else {
                        audioHandler.playSoundEffect(Sounds.ON.ordinal());
                        audioHandler.playMusic(Sounds.AMBIENT.ordinal());
                    }
                }
                //8.5-9.5 (y)

                if (this.mouseY >= 8.5 * 64 && this.mouseY <= 9.5 * 64) {
                    playerWantsSaves = !playerWantsSaves;
                    if (playerWantsSaves) {
                        audioHandler.playSoundEffect(Sounds.ON.ordinal());
                    } else {
                        audioHandler.playSoundEffect(Sounds.OFF.ordinal());
                    }
                }
            }
        }

    }
}
