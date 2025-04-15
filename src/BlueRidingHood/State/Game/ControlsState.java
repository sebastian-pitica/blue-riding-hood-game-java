package BlueRidingHood.State.Game;

import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Graphics.Assets;

import java.awt.*;

/*! \class ControlsState
    \brief Starea de controale ale jocului.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

    \note Implementează design patternul **State** și **Singleton**.

 */

public class ControlsState extends GameState {
    private static ControlsState controlsState = null;  /*!< Referință unică către starea controale ale jocului.*/
    private final Image controls;/*!< Imaginea stării.*/

    /*! \fn protected ControlsState()
         \brief Constructorul clasei ControlsState.
    */
    protected ControlsState() {
        controls = Assets.stateImages[GameStates.CONTROLS.ordinal()];
    }

    /*! \fn  public static ControlsState getControlsSubState()
         \brief Returnează referința unică la starea controale.
    */
    public static ControlsState getControlsSubState() {

        if (controlsState == null) {
            controlsState = new ControlsState();
        }

        return controlsState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
       \brief Desenează imaginea specifică stării controale.
       \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(controls, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
    }

    /*! \fn public abstract void mouseHandler()
       \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click

       Poate actualiza starea jocului la acasă.
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


