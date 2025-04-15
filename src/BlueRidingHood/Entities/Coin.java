package BlueRidingHood.Entities;

import BlueRidingHood.Animation.Animation;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Graphics.Tile;

import java.awt.*;

/*! \class Coin
    \brief Implementează entitatea monedei.

     Oferă metode pentru:\n
        -pornirea animației.\n
        -desenarea animației.
    \note Implementează design patternul **Singleton**.

 */

public class Coin {

    final static private Animation coinAnimation = new Animation(6, Assets.coin); /*!< Animația monedei.*/
    static private Coin coin;/*!< Referință unică către monedă.*/

    /*! \fn protected Coin()
           \brief Constructorul clasei Coin.
    */
    protected Coin() {
    }

    /*! \fn public static Coin getCoin()
           \brief Returnează referința unică a monedei.
    */
    public static Coin getCoin() {
        if (coin == null) {
            coin = new Coin();
        }
        return coin;
    }

    /*! \fn public void drawCoin(Graphics g, int xCoord, int yCoord)
           \brief Desenează moneda la coordonatele date.
           \param g grafica pentru desenare.
           \param xCoord coordonata x în matricea hărții.
           \param yCoord coordonata y în matricea hărții.
    */
    public void drawCoin(Graphics g, int xCoord, int yCoord) {
        coinAnimation.drawAnimation(g, xCoord * Tile.TILE_WIDTH + Tile.TILE_WIDTH / 2 - 8
                , yCoord * Tile.TILE_HEIGHT + Tile.TILE_HEIGHT / 2 - 8, Tile.TILE_WIDTH / 4, Tile.TILE_HEIGHT / 4);
    }

    /*! \fn  public void runAnimation()
      \brief Pornește animația monedei.
    */
    public void runAnimation() {
        coinAnimation.runAnimation();
    }

}
