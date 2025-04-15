package BlueRidingHood.InputManager;

import BlueRidingHood.Observer.Observer;
import BlueRidingHood.State.Game.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/*! \class MouseInputManager
    \brief Implementeză managementul inputului de la mouse.

    \note Implementează design patternul **Singleton** și **Observer**.
    \note Observer pentru GameState.
 */

public class MouseInputManager implements MouseListener, Observer {

    static private MouseInputManager mouseInputManager = null; /*!< Referință unică către managerul de input.*/
    private static GameState currentState; /*!< Referință starea curentă a jocului.*/

    /*! \fn  protected MouseInputManager()
          \brief Constructorul clasei MouseInputManager.
   */
    protected MouseInputManager() {
        currentState = GameState.getCurrentState();
        currentState.attach(this);
    }

    /*! \fn  public static MouseInputManager provideMouseInputManager()
         \brief Returnează referința unică a managerului de input.
    */
    public static MouseInputManager provideMouseInputManager() {
        if (mouseInputManager == null) {
            mouseInputManager = new MouseInputManager();
        }
        return mouseInputManager;
    }

    /*! \fn   public void mouseClicked(MouseEvent e)
         \brief Preia coordonatele care jucătorul a făcut click.

         Apelează handlerul pentru acest tip de eveniment al starii curente de joc.
   */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        currentState.clicked(x, y);
    }

    /*! \fn public void mouseEntered(MouseEvent e)
          \brief UNUSED.
    */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /*! \fn public void mouseExited(MouseEvent e)
         \brief UNUSED.
   */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /*! \fn public void mousePressed(MouseEvent e)
         \brief UNUSED.
   */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /*! \fn public void mouseReleased(MouseEvent e)
         \brief UNUSED.
   */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /*! \fn  public void updateObserver()
     \brief Primește actualizări legate de modificarea stării curente de a jocului.

     Actualizează referința către starea curentă.
  */
    @Override
    public void updateObserver() {
        currentState = GameState.getCurrentState();
    }
}
