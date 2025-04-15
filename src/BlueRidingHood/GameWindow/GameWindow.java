package BlueRidingHood.GameWindow;

import BlueRidingHood.Graphics.Tile;
import BlueRidingHood.InputManager.KeyboardInputManager;
import BlueRidingHood.InputManager.MouseInputManager;

import javax.swing.*;
import java.awt.*;

/*! \class GameWindow
    \brief Implementează fereastra jocului.

    Oferă metode pentru:\n
        -construierea ferestrei de joc.\n
        -preluarea lâțimii/înălțimii ferestrei.\n
        -preluarea unei referințe către pânza pe care se desenează.\n
        -preluarea unei referințe către fereastra principală.
 */
public class GameWindow {
    private final String windowTitle;       /*!< Titlul ferestrei*/
    private final int windowWidth;       /*!< Lățimea ferestrei în pixeli*/
    private final int windowHeight;      /*!< Înaltimea ferestrei în pixeli*/
    private JFrame windowFrame;       /*!< Fereastra principală a jocului*///
    private Canvas canvas;         /*!< "Pânza/tablou" în care se poate desena*/

    /*! \fn GameWindow(String title, int width, int height)
            \brief Constructorul cu parametri al clasei GameWindow.

            Inițializează variabilele locale.

            \param title Titlul ferestrei.
            \param width Lățimea ferestrei în pixeli.
            \param height Înălțimea ferestrei în pixeli.
         */
    public GameWindow() {
        windowTitle = "BlueRidingHood";
        windowWidth = 30 * Tile.TILE_WIDTH;
        windowHeight = 16 * Tile.TILE_HEIGHT;
        windowFrame = null;
    }

    /*! \fn private void BuildGameWindow()
        \brief Construiește/crează fereastra și setează toate proprietățile
        necesare.
     */
    public void BuildGameWindow() {

        if (windowFrame != null) {
            return;
        }
        windowFrame = new JFrame(windowTitle);
        windowFrame.setSize(windowWidth, windowHeight);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setResizable(false);
        windowFrame.setLocationRelativeTo(null);
        windowFrame.addKeyListener(KeyboardInputManager.provideKeyboardInputManager());
        windowFrame.setVisible(true);
        windowFrame.setFocusable(true);
        windowFrame.requestFocusInWindow();
        canvas = new Canvas();
        canvas.addMouseListener(MouseInputManager.provideMouseInputManager());
        canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));
        canvas.setMaximumSize(new Dimension(windowWidth, windowHeight));
        canvas.setMinimumSize(new Dimension(windowWidth, windowHeight));
        windowFrame.add(canvas);
        windowFrame.pack();
    }

    /*! \fn public int GetWndWidth()
        \brief Returneaza latimea ferestrei.
     */
    public int GetWndWidth() {
        return windowWidth;
    }

    /*! \fn public int GetWndHeight()
        \brief Returneaza inaltimea ferestrei.
     */
    public int GetWndHeight() {
        return windowHeight;
    }

    /*! \fn public int GetCanvas()
        \brief Returneaza referinta catre canvas-ul din fereastra pe care se poate desena.
     */
    public Canvas GetCanvas() {
        return canvas;
    }

    /*! \fn public JFrame getWindowFrame()
       \brief Returneaza referinta catre windowFrame pentru a putea adăuga un JOptionPane.
       \see void BlueRidingHood.State.Game.HomeState.mouseHandler()
    */
    public JFrame getWindowFrame() {
        return windowFrame;
    }

}
