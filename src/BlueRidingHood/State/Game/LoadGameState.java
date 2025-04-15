package BlueRidingHood.State.Game;

import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Memento.Memento;

import java.awt.*;

/*! \class LoadGameState
    \brief Starea de încărcare a progresului.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

    \note Implementează design patternul **State** și **Singleton**.
 */

public class LoadGameState extends GameState {
    private static LoadGameState loadGameState = null;/*!< Referință unică de încărcare a progresului.*/
    private final Image loadGame;/*!< Imaginea stării.*/

    /*! \fn protected LoadGameState()
       \brief Constructorul clasei LoadGameState.
   */
    protected LoadGameState() {
        loadGame = Assets.stateImages[GameStates.LOADGAME.ordinal()];
    }

    /*! \fn  public static LoadGameState getLoadGameSubState()
         \brief Returnează referința unică la starea de încărcare a progresului.
    */
    public static LoadGameState getLoadGameSubState() {
        if (loadGameState == null) {
            loadGameState = new LoadGameState();
        }

        return loadGameState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
     \brief  Desenează imaginea specifică stării de încărcare a progresului.
     \param graphics grafica pentru desenare.
   */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(loadGame, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
    }


    /*! \fn public abstract void mouseHandler()
      \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click

      Poate actualiza starea jocului la starea de acasă și starea de joc.
   */
    @Override
    public void mouseHandler() {
        if (this.mouseX >= 0.5 * 64 && this.mouseX <= 1.5 * 64) {
            if (this.mouseY >= gameWindow.GetWndHeight() - 2 * 64 && this.mouseY <= gameWindow.GetWndHeight() - 0.5 * 64) {
                GameState.setCurrentState(HomeState.getHomeMenuState());
                notifyObservers();
            }
        }

        if (this.mouseX >= 6 * 64 && this.mouseX <= 7 * 64) {
            if (this.mouseY >= 5 * 64 && this.mouseY <= 7 * 64) {
                loadMemento();
                GameState.setCurrentState(PlayState.getPlayState());
                notifyObservers();
            }
        }
    }

    /*! \fn private void loadMemento()
      \brief Încarcă progresul salvat în baza de date într-un mememnto.

      Setează progresul actual la cel din memento.
   */
    private void loadMemento()
    {
        PlayState.resetPlayState();
        memento = new Memento();
        memento.loadGame();
        memento.setState();
    }

}
