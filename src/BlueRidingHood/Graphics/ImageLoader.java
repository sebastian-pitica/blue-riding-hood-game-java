package BlueRidingHood.Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/*! \class ImageLoader
    \brief Încarcă o imagine din memorie.

    Oferă metode pentru:\n
        -încărcarea unei imagini din memorie.
 */
public class ImageLoader {

    /*! \fn   public static BufferedImage LoadImage(String path)
        \brief Returnează o referință către o imagine pe care o încarcă.

        \param path Calea relativă pentru localizarea fișierul imagine.
     */
    public static BufferedImage LoadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

