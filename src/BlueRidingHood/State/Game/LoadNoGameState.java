package BlueRidingHood.State.Game;

import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Graphics.Assets;

import java.awt.*;

/*! \class LoadNoGameState
    \brief Starea de încărcare a progresului fără conținut.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

    \note Implementează design patternul **State** și **Singleton**.
 */

public class LoadNoGameState extends GameState {
    private static LoadNoGameState loadNoGameState = null;/*!< Referință unică de încărcare a progresului fără conținut.*/
    private final Image loadNoGame;/*!< Imaginea stării.*/

    /*! \fn protected LoadNoGameState()
   \brief Constructorul clasei LoadNoGameState.
*/
    protected LoadNoGameState() {
        loadNoGame = Assets.stateImages[GameStates.LOADNOGAME.ordinal()];
    }

    /*! \fn  public static LoadGameState getLoadGameSubState()
         \brief Returnează referința unică la starea de încărcare a progresului fără conținut.
    */
    public static LoadNoGameState getLoadNoGameSubState() {
        if (loadNoGameState == null) {
            loadNoGameState = new LoadNoGameState();
        }

        return loadNoGameState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
     \brief  Desenează imaginea specifică stării de încărcare a progresului.
     \param graphics grafica pentru desenare.
   */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(loadNoGame, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
    }

    /*! \fn public abstract void mouseHandler()
      \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click

      Poate actualiza starea jocului la starea de acasă.
   */
    @Override
    public void mouseHandler() {
        if (this.mouseX >= 0.5 * 64 && this.mouseX <= 1.5 * 64) {
            if (this.mouseY >= gameWindow.GetWndHeight() - 2 * 64 && this.mouseY <= gameWindow.GetWndHeight() - 0.5 * 64) {
                GameState.setCurrentState(HomeState.getHomeMenuState());
                notifyObservers();
            }
        }
    }
}
