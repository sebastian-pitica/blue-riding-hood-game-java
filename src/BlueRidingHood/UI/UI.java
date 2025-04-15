package BlueRidingHood.UI;

import BlueRidingHood.Entities.Player;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Graphics.Tile;

import java.awt.*;
import java.text.DecimalFormat;

/*! \class UI
    \brief Implementează interfața utilizator.

    Oferă metode pentru:\n
        -preluarea/setarea timpului total de joc.\n
        -desenarea elementelor caracteristice.

    \note Implementează design patternul **Singleton**.

    \note Sursă idee: https://www.youtube.com/watch?v=0yD5iT8ObCs&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&index=12
 */

public class UI {

    private static float playtime; /*!< Timpul de joc curent.*/
    private static UI ui = null; /*!< Referință unică către interfața utilzator.*/
    private static int score; /*!< Scorul curent.*/
    private final Font arial40; /*!< Fontul cu care se vor scrie diverse mesaje.*/
    private final DecimalFormat decimalFormat = new DecimalFormat("#0.00"); /*!< Formatul în care se va face afișarea timpului.*/
    private final int resistance = 15; /*!< Rezistența la atac a jucătorului.*/
    private int lostLives; /*!< Numărul de vieți pierdute.*/

    /*! \fn protected UI()
        \brief Constructorul clasei UI.
    */

    protected UI() {
        arial40 = new Font("Arial", Font.BOLD, 40);
    }

    /*! \fn  public static UI getUI()
         \brief Returnează referința unică la interfața utilizator.
    */
    public static UI getUI() {
        if (ui == null) {
            ui = new UI();
        }

        return ui;
    }

    /*! \fn  public static void resetUI()
           \brief Resetează referința unică pentru interfața utilizator.
    */
    public static void resetUI() {
        ui = null;
        playtime = 0;
        score = 0;
    }

    /*! \fn  public static float getTotalPlaytime()
           \brief Returnează timpul total de joc.
    */
    public static float getTotalPlaytime() {
        return playtime;
    }

    /*! \fn  public static void setPlaytime(float value)
          \brief Setează timpul total de joc la valoarea dată.
          \param value noua valoare pentru timpul total de joc
   */
    public static void setPlaytime(float value) {
        playtime = value;
    }

    /*! \fn public void draw(Graphics graphics)
     \brief Desenează interfața utilizator: numărul actual de vieți, scorul și timpul de joc.
     \param graphics grafica pentru desenare.
    */
    public void draw(Graphics graphics) {
        graphics.setFont(arial40);
        graphics.setColor(Color.white);
        score = Player.getScore();
        graphics.drawString("SCORE: " + score, 420, 35);
        graphics.drawString("Lives left: ", 20, 35);
        lostLives = 5 - (resistance - Player.getHitCounter()) / 3;
        if(lostLives<5) {
            graphics.drawImage(Assets.lifeBar[lostLives], 200, 0, 200, Tile.TILE_HEIGHT - 20, null);
        }
        playtime += (float) 1 / 60;
        graphics.drawString("Playtime: " + decimalFormat.format(playtime), 1600, 35);
    }

}
