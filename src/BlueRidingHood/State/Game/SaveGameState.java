package BlueRidingHood.State.Game;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Map.Map;
import BlueRidingHood.Memento.Memento;

import java.awt.*;

/*! \class SaveGameState
    \brief Starea de salvare a progresului.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

    \note Implementează design patternul **State** și **Singleton**.

 */

public class SaveGameState extends GameState {

    private static SaveGameState saveGameState = null;/*!< Referința unică către starea de salvare a progresului.*/
    private final Image saveGame;/*!< Imaginea stării.*/

    /*! \fn protected SaveGameState()
         \brief Constructorul clasei ControlsState.
    */
    protected SaveGameState() {
        saveGame = Assets.stateImages[GameStates.SAVEGAME.ordinal()];
    }

    /*! \fn  public static SaveGameState getSaveGameState()
        \brief Returnează referința unică la starea salvare a progresului.
   */
    public static SaveGameState getSaveGameState() {
        if (saveGameState == null) {
            saveGameState = new SaveGameState();
        }

        return saveGameState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
       \brief Desenează imaginea specifică stării salvare a progresului.
       \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(saveGame, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
    }

    /*! \fn public abstract void mouseHandler()
       \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click

       Poate actualiza starea jocului la starea de joc.
       Poate salva progresul actual.
    */
    @Override
    public void mouseHandler() {
        if (this.mouseX >= 7.5 * 64 && this.mouseX <= 9 * 64) {
            if (this.mouseY >= 6.5 * 64 && this.mouseY <= 8 * 64) {
                saveMemento();
                GameState.setCurrentState(PlayState.getPlayState());
                notifyObservers();
                if (Map.getCurrentMap().getMapNr() == 3) {
                    AudioHandler.playLastLevelAudio();
                }

            }
            if (this.mouseY >= 9 * 64 && this.mouseY <= 10.5 * 64) {
                GameState.setCurrentState(PlayState.getPlayState());
                notifyObservers();
                if (Map.getCurrentMap().getMapNr() == 3) {
                    AudioHandler.playLastLevelAudio();
                }
            }
        }
    }

    /*! \fn private void saveMemento()
       \brief Salvează progresul actual într-un memento și în baza de date.
    */
    private void saveMemento()
    {
        memento = new Memento();
        memento.getState();
        memento.saveGame();
    }
}
