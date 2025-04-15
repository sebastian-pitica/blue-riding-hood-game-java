package BlueRidingHood.State.Game;

import BlueRidingHood.Game.Game;

import java.awt.*;

/*! \class ErrorState
    \brief Starea de eroare.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

     \note Implementează design patternul **State**.
 */

public class ErrorState extends GameState{
    private final String errorText;  /*!< Mesajul care descrie eroarea apărută.*/

    /*! \fn public ErrorState(String errorText)
       \brief Constructorul clasei ErrorState.
       \param errorText mesajul descriptiv al erorii.
   */
    public ErrorState(String errorText){
        this.errorText=errorText;
        error=true;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
      \brief Desenează un text sugestiv și mesajul erorii.
      \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
        graphics.setFont(arial100);
        graphics.setColor(Color.black);
        graphics.drawString("Error: ",2*64,5*64);
        graphics.setFont(arial40);
        graphics.drawString(errorText,4*64,7*64);
        graphics.drawString("Click on screen to exit!", 64,15*64);
        graphics.setFont(arial100);
    }

    /*! \fn public abstract void mouseHandler()
        \brief Închide jocul.
     */
    @Override
    public void mouseHandler() {
        Game.provideGame().quit();
    }
}
