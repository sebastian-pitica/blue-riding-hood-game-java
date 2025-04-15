package BlueRidingHood.Entities;

import BlueRidingHood.Animation.Animation;
import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.Factories.EntitiesFactory;
import BlueRidingHood.Game.Enums.Sign;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Map.Map;
import BlueRidingHood.Observer.Observer;
import BlueRidingHood.State.Player.PlayerState;
import BlueRidingHood.State.Player.ShieldON;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import static BlueRidingHood.Dijkstra.Node.getPath;

/*! \class EnemieEntity
    \brief Interfață pentru entitățile inamice.

     Oferă metode pentru:\n
        -urmărirea jucătorului.\n
        -verificarea loviturilor către jucător.\n
        -verificarea pozițiilor enităților.\n
        -uciderea tuturor entităților curente.\n
        -lovirea entităților.\n
        -pornirea animațiilor.\n
        -desenarea animațiilor.

    \note Observer pentru PlayerState și poziția jucătorului.
    \note Folosește o coleție ce permite accesul concurent.
    \note Nu se poate implemneta un observer pentru hartă.\n
        Nu pot implementa life bar sau blinking dead provocă bug-uri la desenare.
 */

public abstract class EnemieEntity extends Entity implements Observer {
    protected static final int speed = 15; /*!< Viteza de redare a animațiilor.*/
    protected static final long followTimeLimit = 4000000; /*!< Limita de timp pentru urmărirea jucătorului.*/
    public static CopyOnWriteArrayList<EnemieEntity> actualEntities; /*!< Entitățile existente în momentul curent.*/
    protected static final int attackPower = 1; /*!< Puterea de atac.*/
    protected static long hitPlayerStartTime = 0; /*!< Ultima dată când a fost lovit jucătorul.*/
    protected static int playerMatrixY; /*!< Cordonata x în matricea hărții a jucătorului.*/
    protected static int playerMatrixX; /*!< Cordonata y în matricea hărții a jucătorului.*/
    protected static Player player = Player.getPlayer(); /*!< Referință către jucător.*/
    protected static PlayerState playerState = player.getCurrentState(); /*!< Referință către starea curentă a jucătorului.*/
    protected static final long attackTimeLimit = 1000000000; /*!< Limita de timp pentru atacarea jucătorului.*/
    protected static final AudioHandler audioHandler = AudioHandler.getAudioHandler(); /*!< Referință către handlerul audio.*/
    protected LinkedList<Integer> path; /*!< Calea prin matricea hărții către jucător.*/
    protected long followStartTime = 0; /*!< Ultima dată când a fost începută urmarirea jucătorului.*/
    protected int attackResistence; /*!< Rezistența la atac.*/
    protected int hitCounter = 0; /*!< Contorul de lovituri.*/
    protected Animation up; /*!< Animația sus.*/
    protected Animation down; /*!< Animația jos.*/
    protected Animation left; /*!< Animația stânga.*/
    protected Animation right; /*!< Animația dreapta.*/
    protected Animation currentAnimation; /*!< Animația curentă.*/

    /*! \fn public static void resetPlayerRefs()
           \brief Resetează referința la jucător și la starea lui curentă.
    */
    public static void resetPlayerRefs() {
        player = Player.getPlayer();
        playerState = player.getCurrentState();
    }

    /*! \fn public static void entitysFollowPlayer()
           \brief Pornește urmărirea jucătorului pentru toate entitățile curente.
    */
    public static void entitysFollowPlayer() {
        if (actualEntities != null)
            for (EnemieEntity entity : actualEntities) {
                entity.followPlayer();
            }
    }

    /*! \fn public static void checkHitPlayer()
           \brief Verifică dacă jucătorul a fost lovit.

           Dacă a fost lovit scade 50 de puncte din scor și redă un sunet de mușcătură.
    */
    public static void checkHitPlayer() {
        if (actualEntities != null)
            if (playerState != ShieldON.getShieldON() && System.nanoTime() - hitPlayerStartTime >= attackTimeLimit
                    && isAnotherEntityHere(playerMatrixX, playerMatrixY, null)) {
                hitPlayerStartTime = System.nanoTime();
                audioHandler.playRandomBite();
                player.hit(attackPower);
                player.addPointsToScore(-50);
            }
    }


    /*! \fn public static boolean isAnotherEntityHere(int matrixX, int matrixY, EnemieEntity callerEntity)
          \brief Returnează dacă la coordonatele date se află o altă entitate față de cea apelantă.

          \param matrixX coordonata x în matricea hărții.
          \param matrixY coordonata y în matricea hărții.
          \param callerEntity entitatea care a apelat metoda.
   */
    public static boolean isAnotherEntityHere(int matrixX, int matrixY, EnemieEntity callerEntity) {
        if (actualEntities != null)
            for (EnemieEntity entity : actualEntities) {
                if (matrixX == entity.matrixX && matrixY == entity.matrixY && entity != callerEntity) {
                    return true;
                }
            }
        return false;
    }

    /*! \fn  public static void hitEntityAtCoords(int matrixX, int matrixY, int hitPower)
           \brief Lovește orice enitate de la coordonatele date cu puterea dată.

           Dacă o entitate este lovită redă sunetul corespunzător
           și o elimină dacă e cazul.\n

          \param matrixX coordonata x în matricea hărții.
          \param matrixY coordonata y în matricea hărții.
          \param hitPower puterea loviturii.
    */
    public static void hitEntityAtCoords(int matrixX, int matrixY, int hitPower) {
        for (EnemieEntity entity : actualEntities) {
            if (matrixX == entity.matrixX && matrixY == entity.matrixY) {
                audioHandler.setSwordNoHit(false);
                entity.hit(hitPower);
                audioHandler.playSoundEffect(Sounds.SWORDHIT.ordinal());
                if (actualEntities != null && !entity.alive) {
                    actualEntities.remove(entity);
                    AudioHandler.playSwordKill();
                    audioHandler.setSwordNoHit(true);
                    checkConstantGuard();
                }
            }
        }
    }

    /*! \fn private static void checkConstantGuard()
           \brief Asigură existența constantă a cel puțin unei gărzi.
    */
    private static void checkConstantGuard() {
        if (actualEntities.size() == 1 && Map.getCurrentMap().getMapNr() == 3) {
            EntitiesFactory.getEntitiesFactory().produceMapEntities();
        }
    }

    /*! \fn  public static void killThemAll()
           \brief Ucide toate entitățile curente.
    */
    public static void killThemAll() {
        //daca e folosita pe harta 3 jocul nu poate fi castigat
        if (actualEntities != null)
            actualEntities = null;
    }

    /*! \fn  protected void followPlayer()
           \brief Urmărește jucătorul pe calea cea mai scurtă.
    */
    protected void followPlayer() {
        try {
            if (System.nanoTime() - followStartTime >= followTimeLimit) {
                followStartTime = System.nanoTime();
                int next = path.get(0);

                int nextPosY = next / 100;
                int nextPosX = next % 100;

                if (nextPosX == matrixX && nextPosY == matrixY) {
                    path.remove(0);
                    if (path.size() == 0) {
                        audioHandler.stop(Sounds.ERUN.ordinal());
                    }
                } else if (!isAnotherEntityHere(nextPosX, nextPosY, this)) {
                    if (nextPosX > matrixX) {
                        stepHorizontal(Sign.PLUS);
                        currentAnimation = left;
                        audioHandler.playSoundEffect(Sounds.ERUN.ordinal());

                    } else if (nextPosX < matrixX) {
                        stepHorizontal(Sign.MINUS);
                        currentAnimation = right;
                        audioHandler.playSoundEffect(Sounds.ERUN.ordinal());
                    }
                    if (nextPosY > matrixY) {
                        stepVertical(Sign.PLUS);
                        currentAnimation = up;
                        audioHandler.playSoundEffect(Sounds.ERUN.ordinal());
                    } else if (nextPosY < matrixY) {
                        stepVertical(Sign.MINUS);
                        currentAnimation = down;
                        audioHandler.playSoundEffect(Sounds.ERUN.ordinal());
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    /*! \fn   protected void establishNewPath()
          \brief Preia noua cale către jucător când acesta își modifică poziția.
   */
    protected void establishNewPath() {
        playerMatrixX = player.getMatrixX();
        playerMatrixY = player.getMatrixY();
        this.path = getPath(matrixY * 100 + matrixX, playerMatrixY * 100 + playerMatrixX);
    }

    /*! \fn  public void runAnimation()
      \brief Pornește animația curentă.
    */
    public abstract void runAnimation();

    /*! \fn  public void draw(Graphics graphics)
      \brief Desenează animația curentă.
      \param graphics grafica pentru desenare.
   */
    public abstract void draw(Graphics graphics);

    /*! \fn  public void updateObserver()
      \brief Primește actualizări legate de modificarea stării sau a coordonatelor jucătorului.
   */
    public void updateObserver() {
        if (playerState != player.getCurrentState()) {
            playerState = player.getCurrentState();
        } else {
            establishNewPath();
        }
    }
}
