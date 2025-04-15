package BlueRidingHood.Entities;

import BlueRidingHood.Animation.Animation;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Graphics.Tile;
import BlueRidingHood.Map.Map;
import BlueRidingHood.State.Game.GameState;

import java.awt.*;

/*! \class ZaWalfo
    \brief Implementează entitatea ZaWalfo.

    Oferă metode pentru:\n
        -lovirea entității.\n
        -pornirea animației.\n
        -desenarea animației.

    \note Implementează design patternul **Singleton**.
 */

public class ZaWalfo extends EnemieEntity {

    static final int corectieY = 12; /*!< Corectarea poziționării animației.*/
    static private ZaWalfo zaWalfo = null; /*!< Referință unică către jucător.*/

    /*! \fn  protected ZaWalfo()
          \brief Constructorul clasei ZaWalfo.

          Inițializează valorile specifice.
   */
    protected ZaWalfo() {
        this.path = null;
        this.matrixX = 28;
        this.matrixY = 14;
        this.alive = true;
        this.attackResistence = 25;
        this.xCoord = matrixX * Tile.TILE_WIDTH;
        this.yCoord = matrixY * Tile.TILE_HEIGHT;
        InitAnimation();
        this.path = null;
        currentAnimation = right;
    }

    /*! \fn  public static ZaWalfo getZaWalfo()
           \brief Returnează referința unică a entității.
    */
    static public ZaWalfo getZaWalfo() {
        if (zaWalfo == null && Map.getCurrentMap().getMapNr() == 3) {
            zaWalfo = new ZaWalfo();
        }

        return zaWalfo;
    }

    /*! \fn  private void InitAnimation()
          \brief Inițializează animațiile specifice.
   */
    private void InitAnimation() {

        down = new Animation(speed, Assets.zaWalfoUp);
        up = new Animation(speed, Assets.zaWalfoDown);
        right = new Animation(speed, Assets.zaWalfoLeft);
        left = new Animation(speed, Assets.zaWalfoRight);
    }

    /*! \fn  public void hit(int hitPower)
        \brief Incrementează contorul de lovituri cu valoarea primită.

        Dacă entitatea nu mai este în viață adaugă puncte la scorul jucătorului și schimbă starea jocului în win.
        \see WonState
     */
    @Override
    public void hit(int hitPower) {
        hitCounter += hitPower;
        if (hitCounter > attackResistence) {
            alive = false;
            player.addPointsToScore(attackResistence * 100);
            GameState.loadWin();
        }
    }

    /*! \fn  public void runAnimation()
      \brief Pornește animația curentă.
    */
    public void runAnimation() {
        currentAnimation.runAnimation();

    }

    /*! \fn  public void draw(Graphics graphics)
      \brief Desenează animația curentă.
      \param graphics grafica pentru desenare.
   */
    public void draw(Graphics graphics) {
        currentAnimation.drawAnimation(graphics, matrixX * Tile.TILE_WIDTH, matrixY * Tile.TILE_HEIGHT - corectieY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

    }

    /*! \fn  public static void reset()
           \brief Resetează referința unică a entității.
    */
    public static void reset() {
        zaWalfo = null;
    }

}
