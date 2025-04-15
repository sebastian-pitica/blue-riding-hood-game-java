package BlueRidingHood.State.Game;

import BlueRidingHood.Game.Enums.GameStates;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Graphics.Assets;

import java.awt.*;

/*! \class WonState
    \brief Starea de câștig.

     Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.\n
        -setarea scorului și a timpului total de joc ce vor fi afșate.

    \note Implementează design patternul **State** și **Singleton**.
 */

public class WonState extends GameState {

    private static WonState wonState = null;/*!< Referința unică către starea de câștig.*/
    private final Image won;/*!< Imaginea stării.*/
    private int score; /*!< Scorul final al jucătorului.*/
    private float totalPlayTime; /*!< Tipul de joc total al jucătorului.*/

    /*! \fn protected WonState()
        \brief Constructorul clasei WonState.
    */
    protected WonState() {
        won = Assets.stateImages[GameStates.WON.ordinal()];
    }

    /*! \fn  public static WonState getWonState()
         \brief Returnează referința unică la starea de câștig.
    */
    public static WonState getWonState() {
        if (wonState == null) {
            wonState = new WonState();
        }

        return wonState;
    }

    /*! \fn public abstract void Draw(Graphics graphics)
      \brief  Desenează imaginea specifică stării de câștig și detaliile finale.
      \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {
        graphics.drawImage(won, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
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
