package BlueRidingHood.State.Player;

import BlueRidingHood.Animation.Animation;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Graphics.Assets;

/*! \class ShieldON
    \brief Starea de scut pornit a jucătorului.

    Oferă metode pentru:\n
        -preluarea animației implicite.

    \note Implementează design patternul **State** și **Singleton**
 */

public class ShieldON extends PlayerState {

    private static ShieldON shieldON = null;/*!< Referința unică către starea de scut pornit.*/

    /*! \fn protected ShieldON()
        \brief Constructorul clasei ShieldON.

        Inițializează animațiile specifice.
    */
    protected ShieldON() {
        initAnimation();
    }

    /*! \fn  public static ShieldON getShieldON()
         \brief Returnează referința unică la starea de scut pornit.
    */
    public static ShieldON getShieldON() {
        if (shieldON == null) {
            shieldON = new ShieldON();
        }

        return shieldON;
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
        leftStand = new Animation(speed, Assets.playerLeftShieldStand);
        rightStand = new Animation(speed, Assets.playerRightShieldStand);
        leftRun = new Animation(speed, Assets.playerLeftShieldRun);
        rightRun = new Animation(speed, Assets.playerRightShieldRun);
        leftDrawSword = new Animation(speed, Assets.playerLeftShieldDrawSword);
        rightDrawSword = new Animation(speed, Assets.playerRightShieldDrawSword);
        rightRetractSword = new Animation(speed, Assets.playerRightShieldRetractSword);
        leftRetractSword = new Animation(speed, Assets.playerLeftShieldRetractSword);
        leftAttack = new Animation(speed, Assets.playerLeftShieldAttack);
        rightAttack = new Animation(speed, Assets.playerRightShieldAttack);
        defaultAnimation = rightStand;
    }

}
