package BlueRidingHood.Animation;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.Entities.Coin;
import BlueRidingHood.Entities.EnemieEntity;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Factories.CoinFactory;
import BlueRidingHood.Game.Enums.Action;
import BlueRidingHood.Game.Enums.Direction;
import BlueRidingHood.Game.Enums.PlayerStates;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Graphics.Tile;
import BlueRidingHood.InputManager.KeyboardInputManager;
import BlueRidingHood.Map.Map;
import BlueRidingHood.Observer.Observer;
import BlueRidingHood.State.Player.PlayerState;

import java.awt.*;
import java.util.Vector;

import static BlueRidingHood.Entities.EnemieEntity.actualEntities;
import static BlueRidingHood.State.Player.ShieldON.getShieldON;

/*! \class AnimationHandler
    \brief Handler pentru toate animațiile.

    Oferă metode pentru:\n
        -desenarea tuturor animațiilor conform inputului/așteptărilor.\n
        -redarea tuturor animațiilor.

    \note Observer pentru Map.
    \note Implementează design patternul **Singleton**, **Observer** și **Flyweight** pentru monedă (Un singur obiect desenat în mai multe locuri).

 */
public class AnimationHandler implements Observer {
    private static AudioHandler audioHandler; /*!< Referință către handlerul audio.*/
    private static AnimationHandler animationHandler = null; /*!< Referință unică către handlerul de animație.*/
    private final KeyboardInputManager keyboardInputManager; /*!< Referință către managerul de input de la tastatură.*/
    private final Player player; /*!< Referință către jucător.*/
    private final Coin coin = Coin.getCoin(); /*!< Referință către monedă.*/
    final private int corectiePlayerCoord = 12; /*!< Corectează poziționarea animației jucatorului.*/
    final private int animationSecondsLimit = 5; /*!< Limitează posibilitatea activării scutului la un anumit număr de secunde.*/
    private Map currentMap; /*!< Referință către harta curentă.*/
    private Vector<Integer> coinsPositions; /*!< Pozițiile în care se va desena moneda.*/
    private long shieldStartTime; /*!< Memorează timpul la care a început animația scutului.*/
    private long shieldStopTime; /*!< Memorează timpul la care s-a oprit animația scutului.*/
    private Animation currentPlayAnimation; /*!< Referință către animația curentă a jucătorului.*/
    private PlayerState currentState; /*!< Referință către starea curentă a jucătorului .*/

    /*! \fn protected AnimationHandler()
            \brief Constructorul clasei AnimationHandler.

           Inițializează referințele locale.\n
           Setează starea curentă a jucătorului la cea implicită.\n
     */
    protected AnimationHandler() {
        keyboardInputManager = KeyboardInputManager.provideKeyboardInputManager();
        player = Player.getPlayer();
        player.setCurrentState(PlayerState.getDefaultState());
        currentState = player.getCurrentState();
        currentPlayAnimation = currentState.getDefaultAnimation();
        currentMap = Map.getCurrentMap();
        currentMap.attach(this);
        audioHandler = AudioHandler.getAudioHandler();
        coinInit();
    }

    /*! \fn public static AnimationHandler getAnimationHandler()
            \brief Returnează referința unică a handlerului de animație.
     */
    public static AnimationHandler getAnimationHandler() {
        if (animationHandler == null) {
            animationHandler = new AnimationHandler();
        }

        return animationHandler;
    }

    /*! \fn public static void resetAnimationHandler()
            \brief Resetează referința unică a handlerului de animație.
     */
    public static void resetAnimationHandler() {
        animationHandler = null;
    }

    /*! \fn private void coinInit()
            \brief Preia pozițiile de desenare a monedei.
     */
    private void coinInit() {
        CoinFactory coinFactory = CoinFactory.provideCoinFactory();
        coinsPositions = coinFactory.createCoins(currentMap);
    }

    /*! \fn private void currentPlayerAnimation()
            \brief Setează animația curentă a jucătorului.

            În funcție de inputul de la tastatură și starea curentă a jucătorului.\n
            Redă sau oprește sunetul corespunzător jucătorului.
     */
    private void currentPlayerAnimation() {
        if (keyboardInputManager.up || keyboardInputManager.down) {
            currentPlayAnimation = currentState.getAnimation(keyboardInputManager.lastHorizontalDirection, Action.RUN);
            audioHandler.playSoundEffect(Sounds.PRUN.ordinal());
        }

        if (keyboardInputManager.left) {
            currentPlayAnimation = currentState.getAnimation(Direction.LEFT, Action.RUN);
            audioHandler.playSoundEffect(Sounds.PRUN.ordinal());
        }
        if (keyboardInputManager.right) {
            currentPlayAnimation = currentState.getAnimation(Direction.RIGHT, Action.RUN);
            audioHandler.playSoundEffect(Sounds.PRUN.ordinal());
        }

        if (keyboardInputManager.reset || !keyboardInputManager.anyMovementKeyPressed())
        //daca s-a facut reset sau nu s-a apasat nici o tasta de miscare
        {
            currentPlayAnimation = currentState.getAnimation(keyboardInputManager.lastHorizontalDirection, Action.IDLE);
            audioHandler.stop(Sounds.PRUN.ordinal());
        }

        if (keyboardInputManager.attack) {
            //daca este apasata tasta de atac
            if (player.getAttackActive()) {
                //daca atacul este in desfasurare
                currentPlayAnimation = currentState.getAnimation(keyboardInputManager.lastHorizontalDirection, Action.ATTACK);
                audioHandler.playSoundEffect(Sounds.SWORDNOHIT.ordinal());
            } else {
                //daca atacul nu este in desfasurare
                player.setAttackActive(true);
                //atacul este acum in desfasurare
                currentPlayAnimation = currentState.getAnimation(keyboardInputManager.lastHorizontalDirection, Action.DRAWSWORD);
                audioHandler.playSoundEffect(Sounds.SWORDDRAW.ordinal());
            }
        } else {
            //daca nu a fost apasata tasta de atac, dar atacul este in desfasurare
            if (player.getAttackActive()) {
                player.setAttackActive(false);
                //atacul este incheiat
                currentPlayAnimation = currentState.getAnimation(keyboardInputManager.lastHorizontalDirection, Action.RETRACTSWORD);
            }
        }

    }

    /*! \fn public void animationStartTimeHandler()
            \brief Seteză, dacă este cazul, starea jucătorului în cea de scut pornit.
     */
    public void animationStartTimeHandler() {
        if (keyboardInputManager.shieldActivated && timeToStartAnimation(shieldStopTime)) //daca scutul a fost activat
        {
            player.setCurrentState(PlayerStates.SHIELDON);
            currentState = player.getCurrentState();
            shieldStartTime = System.nanoTime();
        }

    }

    /*! \fn public void aniomationStopTimeHandler()
           \brief Seteză, dacă este cazul, starea jucătorului în cea de scut oprit.
    */
    public void aniomationStopTimeHandler() {
        if (currentState == getShieldON() && timeToStopAnimation(shieldStartTime)) //verific daca este nevoie sa opresc animatia scutului
        {
            player.setCurrentState(PlayerStates.SHIELDOFF);
            currentState = player.getCurrentState();
            shieldStopTime = System.nanoTime();
        }

    }

    /*! \fn private boolean timeToStartAnimation(long stopTime)
           \brief Determină dacă limita de timp pentru pornirea scutului este respectată.
           \param stopTime timpul la care a fost oprit scutul anterior.
    */
    private boolean timeToStartAnimation(long stopTime) {
        long nowTime = System.nanoTime();

        return (nowTime - stopTime) / animationSecondsLimit >= 1000000000;

    }

    /*! \fn private boolean timeToStopAnimation(long startTime)
           \brief Determină dacă limita de timp pentru oprirea scutului este atinsă.
           \param startTime timpul la care a fost pornit scutul anterior.
    */
    private boolean timeToStopAnimation(long startTime) {
        long nowTime = System.nanoTime();

        return (nowTime - startTime) / animationSecondsLimit >= 1000000000;

    }

    /*! \fn public void runAnimations()
           \brief Pornește toate animațiile.
    */
    public void runAnimations() {
        runEntitiesAnimation();
        runCoinAnimations();
        runPlayerAnimation();
    }

    /*! \fn public void drawAnimations(Graphics graphics)
           \brief Desenează toate animațiile.
           \param graphics grafica pentru desenare.
    */
    public void drawAnimations(Graphics graphics) {
        drawPlayerAnimation(graphics);
        drawCoinAnimations(graphics);
        drawEntitiesAnimation(graphics);
    }

    /*! \fn private void runEntitiesAnimation()
           \brief Pornește animațiile entităților inamice curente.
    */
    private void runEntitiesAnimation() {
        if (actualEntities != null)
            for (EnemieEntity element : actualEntities) {
                element.runAnimation();
            }
    }

    /*! \fn private void drawEntitiesAnimation(Graphics g)
          \brief Desenează animațiile entităților inamice curente.
           \param g grafica pentru desenare.
   */
    private void drawEntitiesAnimation(Graphics g) {
        if (actualEntities != null)
            for (EnemieEntity element : actualEntities) {
                element.draw(g);
            }
    }

    /*! \fn private void runCoinAnimations()
          \brief  Pornește animația monedei.

           Verifică starea curentă a "monedelor".
    */
    private void runCoinAnimations() {

        checkCoinVector();

        coin.runAnimation();

    }

    /*! \fn private void checkCoinVector()
         \brief Verifică starea curentă a "monedelor".

         Elimină monedele colectate.\n
         Adaugă 100 de puncte la scorul jucătorului pentru fiecare monedă colectată.\n
         Redă sunetul monedei.\n
         Dacă toate monedele au fost colectate crează altele noi.
  */
    private void checkCoinVector() {
        if (isCoinAtThisPosition(player.getMatrixX() + player.getMatrixY() * 100)) {
            player.addPointsToScore(100);
            audioHandler.playSoundEffect(Sounds.COIN.ordinal());
            eliminateCoinAtCoords(player.getMatrixX() + player.getMatrixY() * 100);
        }

        if (coinsPositions.size() == 0) {
            coinInit();
        }
    }

    /*! \fn private void drawCoinAnimations(Graphics g)
          \brief Desenează monedele existente.
          \param g grafica pentru desenare.

    */
    private void drawCoinAnimations(Graphics g) {
        for (Integer coord : coinsPositions) {
            coin.drawCoin(g, coord % 100, coord / 100);
        }
    }

    /*! \fn private boolean isCoinAtThisPosition(int coords)
          \brief Verifică dacă la poziția dată se află o monedă.
          \param coords coodonatele x și y concatenate împreună.
     */
    private boolean isCoinAtThisPosition(int coords) {
        for (int element : coinsPositions) {
            if (element == coords) {
                return true;
            }
        }
        return false;
    }

    /*! \fn private void eliminateCoinAtCoords(int coords)
         \brief Elimină coordonata primită din vectorul de poziții al monedelor.
         \param coords coodonatele x și y concatenate împreună.
     */
    private void eliminateCoinAtCoords(int coords) {
        coinsPositions.removeIf(element -> element == coords);
    }

    /*! \fn private void drawPlayerAnimation(Graphics graphics)
         \brief Desenează animația curentă a jucătorului.
         \param graphics grafica pentru desenare.
     */
    private void drawPlayerAnimation(Graphics graphics) {
        currentPlayerAnimation();
        currentPlayAnimation.drawAnimation(graphics, player.getxCoord(), player.getyCoord() - corectiePlayerCoord, Tile.TILE_HEIGHT, Tile.TILE_WIDTH);
    }

    /*! \fn  private void runPlayerAnimation()
         \brief Pornește animația curentă a jucătorului.
     */
    private void runPlayerAnimation() {
        currentPlayAnimation.runAnimation();
    }

    /*! \fn  public void updateObserver()
         \brief Actualizează referința la harta curentă.
     */
    @Override
    public void updateObserver() {
        currentMap = Map.getCurrentMap();
        coinInit();
    }


}
