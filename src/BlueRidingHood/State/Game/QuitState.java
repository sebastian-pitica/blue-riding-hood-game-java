package BlueRidingHood.State.Game;

import BlueRidingHood.Game.Game;

import java.awt.*;

/*! \class QuitState
    \brief Starea de ieșire.
    \note Implementează design patternul **State** și **Singleton**.
 */


public class QuitState extends GameState {
    private static QuitState quitState = null;  /*!< Referința unică către starea de ieșire.*/

    /*! \fn protected QuitState()
       \brief Constructorul clasei QuitState.
   */
    protected QuitState() {
    }

    /*! \fn  public static QuitState getQuitState()
        \brief Returnează referința unică la starea de ieșire.
   */
    public static QuitState getQuitState() {
        if (quitState == null) {
            quitState = new QuitState();
        }

        return quitState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
      \brief UNUSED.
      \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
    }

    /*! \fn  public void Update()
      \brief Apelează funția de părăsire a jocului.
   */
    @Override
    public void Update() {
        Game.provideGame().quit();
    }

    /*! \fn public abstract void mouseHandler()
        \brief UNUSED
     */
    @Override
    public void mouseHandler() {
    }
}
