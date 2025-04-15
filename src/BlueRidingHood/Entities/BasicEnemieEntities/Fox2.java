package BlueRidingHood.Entities.BasicEnemieEntities;

import BlueRidingHood.Animation.Animation;
import BlueRidingHood.Entities.EnemieEntity;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Graphics.Tile;

import java.awt.*;

/*! \class Fox2
    \brief Implementează entitatea vulpe2.

       Oferă metode pentru:\n
        -lovirea entității.\n
        -pornirea animației.\n
        -desenarea animației.

 */
public class Fox2 extends EnemieEntity {

    /*! \fn  public Fox2(int entityMatrixX, int entityMatrixY)
          \brief Constructorul clasei Bear2.

          Inițializează valorile specifice.\n
          Apelează InitAnimation().
           \param entityMatrixX cordonata x în matricea hărții
           \param entityMatrixY cordonata y în matricea hărții

   */
    public Fox2(int entityMatrixX, int entityMatrixY) {
        this.path = null;
        this.alive = true;
        this.attackResistence = 7;
        this.matrixX = entityMatrixX;
        this.matrixY = entityMatrixY;
        this.xCoord = entityMatrixX * Tile.TILE_WIDTH;
        this.yCoord = entityMatrixY * Tile.TILE_HEIGHT;
        InitAnimation();
        this.path = null;
        currentAnimation = right;
    }

    /*! \fn  private void InitAnimation()
         \brief Inițializează animațiile specifice.
     */
    private void InitAnimation() {
        up = new Animation(speed, Assets.fox2Up);
        down = new Animation(speed, Assets.fox2Down);
        left = new Animation(speed, Assets.fox2Left);
        right = new Animation(speed, Assets.fox2Right);
    }

    /*! \fn  public void hit(int hitPower)
        \brief Incrementează contorul de lovituri cu valoarea primită.

        Dacă entitatea nu mai este în viață adaugă puncte la scorul jucătorului.
    */
    @Override
    public void hit(int hitPower) {
        hitCounter += hitPower;
        if (hitCounter > attackResistence) {
            alive = false;
            player.addPointsToScore(attackResistence * 10);
        }
    }

    /*! \fn  public void runAnimation()
     \brief Pornește animația curentă.
   */
    @Override
    public void runAnimation() {
        currentAnimation.runAnimation();

    }

    /*! \fn  public void draw(Graphics graphics)
      \brief Desenează animația curentă.
      \param graphics grafica pentru desenare.
   */
    @Override
    public void draw(Graphics graphics) {
        currentAnimation.drawAnimation(graphics, matrixX * Tile.TILE_WIDTH + Tile.TILE_HEIGHT / 4, matrixY * Tile.TILE_HEIGHT + Tile.TILE_HEIGHT / 4, Tile.TILE_WIDTH / 2, Tile.TILE_HEIGHT / 2);

    }

}
