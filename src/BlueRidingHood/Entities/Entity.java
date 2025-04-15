package BlueRidingHood.Entities;

import BlueRidingHood.Game.Enums.Sign;
import BlueRidingHood.Graphics.Tile;

import static BlueRidingHood.Map.Map.getCurrentMap;

/*! \class Entity
    \brief Interfață pentru toate entitățile.

    Oferă metode pentru:\n
        -deplasarea pe verticală/orizontală.\n
        -actualizarea/asigurarea coerenței coordonatelor.\n
        -lovirea enităților.\n
        -obținerea coordonatelor matriceale/carteziene.\n
        -setarea coordonatelor carteziene.


    \note Nu se poate implemneta un observer pentru hartă.
 */


public abstract class Entity {

    protected static final int stepSize = 1; /*!< Dimensiunea în pixeli pentru deplasare.*/
    protected int matrixX; /*!< Coordonata matriceală x.*/
    protected int matrixY; /*!< Coordonata matriceală y.*/
    protected int xCoord; /*!< Coordonata x.*/
    protected int yCoord; /*!< Coordonata y.*/
    protected boolean alive = false; /*!< Marchează dacă entitatea traiește.*/

    /*! \fn  public void stepVertical(Sign sign)
      \brief Actualizează poziția entității atunci când se deplasează pe vertical.
      \param sign semnul pentru deplasare.
   */
    public void stepVertical(Sign sign)
    {
        updatePositionInMatrix();
        if (sign == Sign.PLUS) { //deplasare in jos
            if (getCurrentMap().canAdvance(matrixX, matrixY + 1) || yCoord < matrixY * Tile.TILE_HEIGHT) {
                yCoord += stepSize;
            }

        } else { //deplasare in sus
            if (getCurrentMap().canAdvance(matrixX, matrixY - 1) || yCoord > matrixY * Tile.TILE_HEIGHT) {
                yCoord -= stepSize;
            }
        }
        updatePositionInMatrix();
    }

    /*! \fn  public void stepHorizontal(Sign sign)
      \brief Actualizează poziția entității atunci când se deplasează pe orizontal.
      \param sign semnul pentru deplasare.
   */
    public void stepHorizontal(Sign sign)
    {
        updatePositionInMatrix();
        if (sign == Sign.PLUS) //deplasare la dreapta
        {
            if (getCurrentMap().canAdvance(matrixX + 1, matrixY) || xCoord < matrixX * Tile.TILE_HEIGHT) {
                xCoord += stepSize;
            }
        } else {//deplasare la stanga
            if (getCurrentMap().canAdvance(matrixX - 1, matrixY) || xCoord > matrixX * Tile.TILE_HEIGHT) {
                xCoord -= stepSize;
            }

        }
        updatePositionInMatrix();
    }

    /*! \fn  public void updatePositionInMatrix()
      \brief Asigură coerența între seturile de coordonate ale entității.
   */
    public void updatePositionInMatrix() {
        matrixX = (xCoord + Tile.TILE_HEIGHT / 2) / Tile.TILE_HEIGHT;
        matrixY = (yCoord + Tile.TILE_HEIGHT / 2) / Tile.TILE_HEIGHT;
    }

    /*! \fn  public void hit(int hitPower)
       \brief Incrementează contorul de lovituri cu valoarea primită.
    */
    public abstract void hit(int hitPower);

    /*! \fn   public int getyCoord()
       \brief Returnează coordonata y.
    */
    public int getyCoord() {
        return yCoord;
    }

    /*! \fn  public void setyCoord(int yCoord)
       \brief Setează coordonata y.
       \param yCoord noua valoare a coordonatei y.
    */
    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    /*! \fn   public int getxCoord()
       \brief Returnează coordonata x.
    */
    public int getxCoord() {
        return xCoord;
    }

    /*! \fn  public void setxCoord(int xCoord)
      \brief Setează coordonata x.
      \param xCoord noua valoare a coordonatei x.
   */
    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    /*! \fn   public int getMatrixX()
       \brief Returnează coordonata x matriceală.
    */
    public int getMatrixX() {

        return matrixX;

    }

    /*! \fn   public int getMatrixY()
       \brief Returnează coordonata y matriceală.
    */
    public int getMatrixY() {
        return matrixY;
    }
}


