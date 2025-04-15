package BlueRidingHood.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

/*! \class Animation
    \brief Animațiile pentru jucător și entități.

    Oferă metode pentru:\n
        -desenarea unei animații.\n
        -redarea unei animații la o viteză dată.

    \note Clasă preluată din proiectul SunnyLand, relizat de Paval Mihaela Irina (2019-2020), cu acord de utilizare.
    \attention Documentația este proprie.

 */

public class Animation {

    private final int speed; /*!< Viteza de redare a animației.*/
    private final int frames; /*!< Numărul de cadre al animației.*/
    private final BufferedImage[] images; /*!< Imaginile ce formează animația.*/
    private int index = 0;
    private int count = 0;
    private BufferedImage currentImg; /*!< Imaginea curentă din animație.*/

    /*! \fn public Animation(int speed, BufferedImage[] arg)
            \brief Constructorul cu parametri al clasei Animation.
            \param speed viteza de redare a animației.
            \param arg vectorul ce conține imaginile din care este formată animația.
         */
    public Animation(int speed, BufferedImage[] arg) {
        this.speed = speed;

        images = new BufferedImage[arg.length];

        System.arraycopy(arg, 0, images, 0, arg.length);
        frames = arg.length;
    }

    /*! \fn  public void runAnimation()
           \brief Pornește animația la viteza speed.
        */
    public void runAnimation() {
        index++;
        if (index > speed) {
            index = 0;
            nextFrame();
        }
    }

    /*! \fn  private void nextFrame()
           \brief Selectează următorul frame din animație.
        */
    private void nextFrame() {
        for (int i = 0; i < frames; i++) {
            if (count == i)
                currentImg = images[i];
        }
        count++;

        if (count > frames)
            count = 0;
    }

    /*! \fn public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY)
          \brief Desenează frame-ul curent.
          \param g grafica pentru desenare.
          \param x coordonata x.
          \param y coordonata Y.
          \param scaleX scala pe x.
          \param scaleY scala pe y.
       */
    public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY) {
        g.drawImage(currentImg, x, y, scaleX, scaleY, null);
    }

}
