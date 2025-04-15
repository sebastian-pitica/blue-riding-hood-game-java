package BlueRidingHood.State.Player;

import BlueRidingHood.Animation.Animation;
import BlueRidingHood.Game.Enums.Action;
import BlueRidingHood.Game.Enums.Direction;

/*! \class PlayerState
    \brief Interfață pentru stările jucătorului.

    Oferă metode pentru:\n
        -preluarea animației/stării implicite.\n
        -preluarea animației în funcție de acțiune și direcție.

    \note Implementează design patternul **State**.
 */

public abstract class PlayerState {

    protected Animation leftStand; /*!< Animația idle stânga.*/
    protected Animation rightStand; /*!< Animația idle dreapta .*/
    protected Animation leftRun;/*!< Animația fugă stânga.*/
    protected Animation rightRun;/*!< Animația fugă dreapta.*/
    protected Animation leftDrawSword;/*!< Animația scoaterea sabiei stânga.*/
    protected Animation rightDrawSword;/*!< Animația scoaterea sabiei dreapta.*/
    protected Animation rightRetractSword;/*!< Animația retragerea sabiei dreapta.*/
    protected Animation leftRetractSword;/*!< Animația retragerea sabiei stânga.*/
    protected Animation leftAttack;/*!< Animația atac stânga.*/
    protected Animation rightAttack;/*!< Animația atac dreapta.*/
    protected Animation defaultAnimation;/*!< Animația implicită.*/

    /*! \fn static public PlayerState getDefaultState()
        \brief Returnează starea implicită a jucătorului.
    */
    static public PlayerState getDefaultState() {
        return ShieldOFF.getShieldOFF();
    }

    /*! \fn static public Animation getDefaultAnimation()
        \brief Returnează animația implicită a jucătorului.
    */
    public abstract Animation getDefaultAnimation();

    /*! \fn public Animation getAnimation(Direction direction, Action action)
        \brief Returnează animația jucătorului in funție de direcție și tipul acțiunii.
        \param direction direcția animației.
        \param action tipul de acțiune al animației.
    */
    public Animation getAnimation(Direction direction, Action action) {
        switch (action) {
            case RUN -> {
                return getRunAnimation(direction);
            }
            case IDLE -> {
                return getStandAnimation(direction);
            }
            case ATTACK -> {
                return getAttackAnimation(direction);
            }
            case DRAWSWORD -> {
                return getDrawSwordAnimation(direction);
            }
            case RETRACTSWORD -> {
                return getRetractSwordAnimation(direction);
            }
        }
        return defaultAnimation;
    }

    /*! \fn protected Animation getRunAnimation(Direction direction)
        \brief Returnează animația de fugă a jucătorului in funție de direcție.
        \param direction direcția animației.
    */
    protected Animation getRunAnimation(Direction direction) {
        switch (direction) {
            case RIGHT -> {
                return rightRun;
            }
            case LEFT -> {
                return leftRun;
            }
        }
        return defaultAnimation;
    }

    /*! \fn protected Animation getStandAnimation(Direction direction)
        \brief Returnează animația de idle a jucătorului in funție de direcție.
        \param direction direcția animației.
    */
    protected Animation getStandAnimation(Direction direction) {
        switch (direction) {
            case RIGHT -> {
                return rightStand;
            }
            case LEFT -> {
                return leftStand;
            }
        }
        return defaultAnimation;
    }

    /*! \fn protected Animation getAttackAnimation(Direction direction)
        \brief Returnează animația de atac a jucătorului in funție de direcție.
        \param direction direcția animației.
    */
    protected Animation getAttackAnimation(Direction direction) {
        switch (direction) {
            case RIGHT -> {
                return rightAttack;
            }
            case LEFT -> {
                return leftAttack;
            }
        }
        return defaultAnimation;
    }

    /*! \fn protected Animation getDrawSwordAnimation(Direction direction)
        \brief Returnează animația de scoaterea sabiei jucătorului in funție de direcție.
        \param direction direcția animației.
    */
    protected Animation getDrawSwordAnimation(Direction direction) {
        switch (direction) {
            case RIGHT -> {
                return rightDrawSword;
            }
            case LEFT -> {
                return leftDrawSword;
            }
        }
        return defaultAnimation;
    }

    /*! \fn protected Animation getDrawSwordAnimation(Direction direction)
        \brief Returnează animația de retragerea sabiei jucătorului in funție de direcție.
        \param direction direcția animației.
    */
    protected Animation getRetractSwordAnimation(Direction direction) {
        switch (direction) {
            case RIGHT -> {
                return rightRetractSword;
            }
            case LEFT -> {
                return leftRetractSword;
            }
        }
        return defaultAnimation;
    }

    /*! \fn protected abstract void initAnimation()
        \brief Inițializează animațiile specifice.
    */
    protected abstract void initAnimation();

}
