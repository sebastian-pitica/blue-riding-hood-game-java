package BlueRidingHood.Graphics;

import java.awt.image.BufferedImage;

/*! \class SpriteSheet
    \brief Referință către o imagine formată din dale (sprite sheet).

    Oferă metode pentru:\n
        -returnarea unei subimagini.
 */
public class SpriteSheet {
    private final BufferedImage spriteSheet;  /*!< Referință către obiectul BufferedImage ce conține sprite sheet-ul.*/

    /*! \fn public SpriteSheet(BufferedImage sheet)
        \brief Constructor, inițializează spriteSheet.
        \param img un obiect BufferedImage valid.
     */
    public SpriteSheet(BufferedImage buffImg) {
        spriteSheet = buffImg;
    }

    /*! \fn public BufferedImage crop(int x, int y, int tileWidth, int tileHeight)
        \brief Returnează un obiect BufferedImage ce conține o subimage (dală).

        Subimaginea este localizata avand ca referinta punctul din stanga sus.

        \param x numărul dalei din sprite sheet pe axa x.
        \param y numărul dalei din sprite sheet pe axa y.
        \param tileWidth lățimea dalei.
        \param tileHeight înălțimea dalei.
     */
    public BufferedImage crop(int x, int y, int tileWidth, int tileHeight) {
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }
}
