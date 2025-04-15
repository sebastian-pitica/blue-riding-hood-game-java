package BlueRidingHood.State.Game;

import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Graphics.Assets;

import java.awt.*;

/*! \class LostState
    \brief Starea de înfrângere.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.\n
        -setarea scorului și a timpului total de joc ce vor fi afișate.

    \note Implementează design patternul **State** și **Singleton**.

 */

public class LostState extends GameState {

    private static LostState lostState = null;/*!< Referința unică către starea de înfrângere.*/
    private final Image lost;/*!< Imaginea stării.*/
    private int score; /*!< Scorul final al jucătorului.*/
    private float totalPlayTime; /*!< Tipul de joc total al jucătorului.*/

    /*! \fn protected LostState()
        \brief Constructorul clasei LostState.
    */
    protected LostState() {

        lost = Assets.stateImages[GameStates.LOST.ordinal()];
    }

    /*! \fn  public static LostState getLostState()
         \brief Returnează referința unică la starea de înfrângere.
    */
    public static LostState getLostState() {
        if (lostState == null) {
            lostState = new LostState();
        }

        return lostState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
     \brief  Desenează imaginea specifică stării de înfrângere și detaliile finale.
     \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(lost, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
        graphics.setFont(arial100);
        graphics.setColor(Color.black);
        graphics.drawString(decimalFormat.format(totalPlayTime) + "s", 13 * 64, (int) (7.3 * 64));
        graphics.drawString(String.valueOf(score), (int) (15.5 * 64), (int) (10.3 * 64));

    }

    /*! \fn public abstract void mouseHandler()
      \brief Decide acțiunea ce trebuie efectuată în funcție de coordonatele la care s-a făcut click

      Poate actualiza starea jocului la starea de acasă.
   */
    @Override
    public void mouseHandler() {
        if (this.mouseX >= 10.5 * 64 && this.mouseX <= 11.5 * 64) {
            if (this.mouseY >= gameWindow.GetWndHeight() - 2.5 * 64 && this.mouseY <= gameWindow.GetWndHeight() - 1.5 * 64) {
                GameState.setCurrentState(HomeState.getHomeMenuState());
                notifyObservers();
                audioHandler.playMusic(Sounds.AMBIENT.ordinal());
            }
        }
    }

    /*! \fn public void setScore(int score)
       \brief Setează scorul la cel indicat.
       \param score noua valoare a scorului.
    */
    public void setScore(int score) {
        this.score = score;
    }

    /*! \fn public void setTotalPlayTime(float totalPlayTime)
       \brief Setează timpul de joc la cel indicat.
       \param totalPlayTime noua valoare a timpului de joc.
    */
    public void setTotalPlayTime(float totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

}
