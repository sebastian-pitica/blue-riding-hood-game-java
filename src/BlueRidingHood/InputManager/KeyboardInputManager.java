package BlueRidingHood.InputManager;

import BlueRidingHood.Game.Enums.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*! \class KeyboardInputManager
    \brief Implementeză managementul inputului de la tastatură.

    Oferă metode pentru:\n
        -Verificarea evenimentelor de la tastatură.\n
    Accesul la flag-urile interne, ce marchează diferitele evenimente tastatură, este public.

    \note Implementează design patternul **Singleton**
    Se folosește de valori booleene pentru a marca diversele evenimente.
 */

public class KeyboardInputManager implements KeyListener {
    protected static KeyboardInputManager keyboardInputManager = null; /*!< Referință unică către managerul de input.*/
    public Boolean up; /*!< Flag tasta w.*/
    public Boolean down; /*!< Flag tasta s.*/
    public Boolean left; /*!< Flag tasta a.*/
    public Boolean right; /*!< Flag tasta d.*/
    public Boolean attack; /*!< Flag tasta space.*/
    public Boolean shieldActivated; /*!< Flag tasta q.*/
    public Boolean escape; /*!< Flag tasta escape.*/
    public Boolean GODModeOn; /*!< Flag tasta f10.*/
    public Boolean resetHitCounter; /*!< Flag tasta f11.*/
    public Boolean killAllEnemies; /*!< Flag tasta f12.*/
    public Boolean reset; /*!< Flag tasta r.*/
    public Boolean rectangular; /*!< Flag tasta t.*/
    public Boolean grid; /*!< Flag tasta g.*/
    public Boolean quit; /*!< Flag tasta p.*/
    public Boolean faster; /*!< Flag tasta f9.*/
    public Direction lastHorizontalDirection = Direction.RIGHT; /*!< Ultima direcție de deplasare orizontală.*/
    public Direction lastMovementDirection = Direction.RIGHT; /*!< Ultima direție de deplasre.*/

    /*! \fn  protected KeyboardInputManager()
          \brief Constructorul clasei KeyboardInputManager.

          Inițializează valorile flag-urilor.
   */
    protected KeyboardInputManager() {
        up = down = left = right = attack =
                shieldActivated = escape = GODModeOn = resetHitCounter =
                        killAllEnemies = faster = reset = rectangular = quit = grid = false;
    }

    /*! \fn  public static KeyboardInputManager provideKeyboardInputManager()
          \brief Returnează referința unică a managerului de input.
   */
    public static KeyboardInputManager provideKeyboardInputManager() {
        if (keyboardInputManager == null) {
            keyboardInputManager = new KeyboardInputManager();
        }
        return keyboardInputManager;
    }

    /*! \fn   public void keyPressed(KeyEvent e)
          \brief Activează flag-urile corespunzătoare fiecărei taste.

          Pentru tastele on/off deactivează flag-urile corespunzătoare.
    */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyPressedCode = e.getKeyCode();

        if (keyPressedCode == KeyEvent.VK_W) {
            up = true;
            lastMovementDirection = Direction.UP;
        }
        if (keyPressedCode == KeyEvent.VK_S) {
            down = true;
            lastMovementDirection = Direction.DOWN;
        }
        if (keyPressedCode == KeyEvent.VK_A) {
            left = true;
            lastHorizontalDirection = Direction.LEFT;
            lastMovementDirection = Direction.LEFT;
        }
        if (keyPressedCode == KeyEvent.VK_D) {
            right = true;
            lastHorizontalDirection = Direction.RIGHT;
            lastMovementDirection = Direction.RIGHT;
        }
        if (keyPressedCode == KeyEvent.VK_R) {
            reset = true;
        }
        if (keyPressedCode == KeyEvent.VK_T) {
            rectangular = !rectangular;
        }
        if (keyPressedCode == KeyEvent.VK_G) {
            grid = !grid;
        }


        if (keyPressedCode == KeyEvent.VK_SPACE) {
            attack = true;
        }
        if (keyPressedCode == KeyEvent.VK_Q) {
            shieldActivated = true;
        }
        if (keyPressedCode == KeyEvent.VK_P) {
            quit = true;
        }

        if (keyPressedCode == KeyEvent.VK_F10) {
            GODModeOn = !GODModeOn;
        }
        if (keyPressedCode == KeyEvent.VK_F9) {
            faster = !faster;
        }
        if (keyPressedCode == KeyEvent.VK_F11) {
            resetHitCounter = true;
        }
        if (keyPressedCode == KeyEvent.VK_F12) {
            killAllEnemies = true;
        }

        if (keyPressedCode == KeyEvent.VK_ESCAPE) {
            escape = true;
        }


    }

    /*! \fn    public void keyReleased(KeyEvent e)
          \brief Dezactivează flag-urile corespunzătoare fiecărei taste.
    */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyReleasedCode = e.getKeyCode();

        if (keyReleasedCode == KeyEvent.VK_W) {
            up = false;
        }
        if (keyReleasedCode == KeyEvent.VK_S) {
            down = false;
        }
        if (keyReleasedCode == KeyEvent.VK_A) {
            left = false;
        }
        if (keyReleasedCode == KeyEvent.VK_D) {
            right = false;
        }
        if (keyReleasedCode == KeyEvent.VK_R) {
            reset = false;
        }


        if (keyReleasedCode == KeyEvent.VK_SPACE) {
            attack = false;
        }
        if (keyReleasedCode == KeyEvent.VK_Q) {
            shieldActivated = false;
        }

        if (keyReleasedCode == KeyEvent.VK_F11) {
            resetHitCounter = false;
        }
        if (keyReleasedCode == KeyEvent.VK_F12) {
            killAllEnemies = false;
        }

        if (keyReleasedCode == KeyEvent.VK_ESCAPE) {
            escape = false;
        }
    }

    /*! \fn public boolean anyMovementKeyPressed()
          \brief Returnează dacă a fost apăsată vreo tastă de mișcare.
    */
    public boolean anyMovementKeyPressed()
    //verifica daca a fost apasat vreo tasta de miscare
    {
        return up || down || left || right;
    }

    /*! \fn public void keyTyped(KeyEvent e)
          \brief UNUSED.
    */
    @Override
    public void keyTyped(KeyEvent e) {
    }
}
