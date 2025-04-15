package BlueRidingHood.State.Game;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Map.Map;

import java.awt.*;

/*! \class InMenuState
    \brief Starea de meniu în timpul jocului.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

    \note Implementează design patternul **State** și **Singleton**.
 */

public class InMenuState extends GameState {

    private static InMenuState inMenuState = null;/*!< Referință unică către starea meniu în timpul jocului.*/
    private final Image inMenu;/*!< Imaginea stării.*/

    /*! \fn protected InMenuState()
        \brief Constructorul clasei InMenuState.
    */
    protected InMenuState() {

        inMenu = Assets.stateImages[GameStates.INMENU.ordinal()];
    }

    /*! \fn  public static InMenuState getInMenuState()
         \brief Returnează referința unică la starea meniu în timpul jocului.
    */
    public static InMenuState getInMenuState() {
        if (inMenuState == null) {
            inMenuState = new InMenuState();
        }

        return inMenuState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
      \brief  Desenează imaginea specifică stării meniu în timpul jocului.
      \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(inMenu, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
    }


    /*! \fn public abstract void mouseHandler()
      \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click

      Poate actualiza starea jocului la starea de joc, acasă și starea de ieșire.
   */
    @Override
    public void mouseHandler() {
        if (this.mouseX >= 0.5 * 64 && this.mouseX <= 1.5 * 64) {
            if (this.mouseY >= gameWindow.GetWndHeight() - 2 * 64 && this.mouseY <= gameWindow.GetWndHeight() - 0.5 * 64) {
                GameState.setCurrentState(PlayState.getPlayState());
                notifyObservers();

                if(Map.getCurrentMap().getMapNr()==3)
                {
                    AudioHandler.playLastLevelAudio();
                }
            }
        } else {
            if (this.mouseX >= 6 * 64 && this.mouseX <= 7 * 64) {
                if (this.mouseY >= 5 * 64 && this.mouseY <= 6 * 64) {
                    GameState.setCurrentState(HomeState.getHomeMenuState());
                    notifyObservers();
                    PlayState.resetPlayState();
                }

                if (this.mouseY >= 6.5 * 64 && this.mouseY <= 7.5 * 64) {
                    GameState.setCurrentState(QuitState.getQuitState());
                    notifyObservers();
                    PlayState.resetPlayState();
                }
            }
        }
    }
}
