package BlueRidingHood.State.Player;

import BlueRidingHood.Animation.Animation;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Graphics.Assets;

/*! \class ShieldOFF
    \brief Starea de scut oprit a jucătorului.

    Oferă metode pentru:\n
        -preluarea animației implicite.

    \note Implementează design patternul **State** și **Singleton**
 */

public class ShieldOFF extends PlayerState {
    private static ShieldOFF shieldOFF = null; /*!< Referința unică către starea de scut oprit.*/

    /*! \fn protected ShieldOFF()
        \brief Constructorul clasei ShieldOFF.

        Inițializează animațiile specifice.
    */
    protected ShieldOFF() {
        initAnimation();
    }

    /*! \fn  public static ShieldOFF getShieldOFF()
         \brief Returnează referința unică la starea de scut oprit.
    */
    public static ShieldOFF getShieldOFF() {
        if (shieldOFF == null) {
            shieldOFF = new ShieldOFF();
        }

        return shieldOFF;
    }

    /*! \fn static public Animation getDefaultAnimation()
        \brief Returnează animația implicită a jucătorului.
    */
    @Override
    public Animation getDefaultAnimation() {
        return defaultAnimation;
    }

    /*! \fn protected abstract void initAnimation()
       \brief Inițializează animațiile specifice.
    */
    @Override
    protected void initAnimation() {
        int speed = Player.getSpeed();
        leftStand = new Animation(speed, Assets.playerLeftStand);
        rightStand = new Animation(speed, Assets.playerRightStand);
        leftRun = new Animation(speed, Assets.playerLeftRun);
        rightRun = new Animation(speed, Assets.playerRightRun);
        leftDrawSword = new Animation(speed, Assets.playerLeftDrawSword);
        rightDrawSword = new Animation(speed, Assets.playerRightDrawSword);
        rightRetractSword = new Animation(speed, Assets.playerRightRetractSword);
        leftRetractSword = new Animation(speed, Assets.playerLeftRetractSword);
        leftAttack = new Animation(speed, Assets.playerLeftAttack);
        rightAttack = new Animation(speed, Assets.playerRightAttack);
        defaultAnimation = rightStand;
    }

}
