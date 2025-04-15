package BlueRidingHood.Entities;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.Game.Enums.PlayerStates;
import BlueRidingHood.Game.Enums.Sign;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Graphics.Tile;
import BlueRidingHood.Map.Map;
import BlueRidingHood.Observer.Observer;
import BlueRidingHood.Observer.Subject;
import BlueRidingHood.State.Game.GameState;
import BlueRidingHood.State.Player.PlayerState;

import java.util.LinkedList;

import static BlueRidingHood.State.Player.ShieldOFF.getShieldOFF;
import static BlueRidingHood.State.Player.ShieldON.getShieldON;

/*! \class Player
    \brief Implementează entitatea jucătorului.

     Oferă metode pentru:\n
        -preluarea vitezei/scorului/puterii de atac/contorului de lovituri.\n
        -adăugarea de puncte la scor.\n
        -lovirea jucătorului.\n
        -resetarea contorului de lovituri.\n
        -activarea/dezactivarea invincibilității/puterii mari de atac.\n
        -activarea/dezactivarea vitezei sporite pentru deplasare.\n
        -afișarea informațiilor funționale pentru entitatea jucătorului.\n
        -deplasarea pe verticală/orizontală.\n
        -actualizarea/asigurarea coerenței coordonatelor.\n
        -preluarea/setarea stării curente.\n
        -preluarea/setarea flagului de atac activ.\n
        -preluarea/setarea numelui de utilizator.\n
        -setarea scorului.


    \note Implementează design patternul **Singleton**, **State** și **Observer**.
    \note Observer pentru Map.
 */

public class Player extends Entity implements Subject, Observer {
    //map observer
    private static final boolean print = false; /*!< Controlează afișările specifice .*/
    private static final int speed = 6;/*!< Viteza animației.*/
    public static int score;/*!< Scorul jucătorului.*/
    private static Player player = null;/*!< Referință unică către jucător.*/
    private static int stepSize;/*!< Dimensiunea în pixeli pentru deplasare.*/
    private static int attackResistence;/*!< Rezistența la atac.*/
    private static int hitCounter;/*!< Contorul de lovituri.*/
    private static int attackPower;/*!< Puterea de atac.*/
    private final int defaultAttackPower;/*!< Puterea de atac implicită.*/
    private final int defaultAttackResistance;/*!< Resitența la atac implicită.*/
    protected int oldMatrixX;/*!< Vechea coordonată matriceală x.*/
    protected int oldMatrixY;/*!< Vechea coordonată matriceală y.*/
    protected final LinkedList<Observer> observers;/*!< Lista de observatori.*/
    protected PlayerState currentState; /*!< Referință către starea curentă.*/
    private Map currentMap;/*!< Referință la harta curentă.*/
    private boolean attackActive;/*!< Flag-ul ce determină starea atacului.*/
    private String username;/*!< Numele de utilizator al jucătorului.*/
    private AudioHandler audioHandler=null;/*!< Referință către handlerul audio.*/

    /*! \fn  protected Player()
          \brief Constructorul clasei Player.

          Inițializează valorile specifice.
   */
    protected Player() {
        score = 0;
        stepSize = 2;
        hitCounter = 0;
        attackPower = this.defaultAttackPower = 4;
        this.alive = true;
        attackResistence = defaultAttackResistance = 15;
        currentMap = Map.getCurrentMap();
        currentMap.attach(this);
        this.matrixX = oldMatrixX = 0;
        this.matrixY = oldMatrixY = currentMap.startY();
        this.xCoord = 0;
        this.yCoord = currentMap.startY() * Tile.TILE_HEIGHT;
        attackActive = false;
        observers = new LinkedList<>();

    }

    /*! \fn  public static Player getPlayer()
           \brief Returnează referința unică a jucătorului.
    */
    public static Player getPlayer() {
        if (player == null) {
            player = new Player();
        }

        return player;
    }

    /*! \fn  public static void resetPlayer()
           \brief Resetează referința unică a jucătorului.
    */
    public static void resetPlayer() {

        player = null;
    }

    /*! \fn  public static int getSpeed()
          \brief Returnează viteza jucătorului.
   */
    public static int getSpeed() {
        return speed;
    }

    /*! \fn  public static int getScore()
         \brief Returnează scorul jucătorului.
    */
    public static int getScore() {
        return score;
    }

    /*! \fn  public static int getHitCounter()
         \brief Returnează contorul de lovituri al jucătorului.
    */
    public static int getHitCounter() {
        return hitCounter;
    }

    /*! \fn  public static int getAttackPower()
        \brief Returnează puterea de atac jucătorului.
    */
    public static int getAttackPower() {
        return attackPower;
    }

    /*! \fn   public void addPointsToScore(int amount)
        \brief Adaugă puncte la scorul jucătorului.
        \param amount numărul punctelor de adăugat
     */
    public void addPointsToScore(int amount) {
        score += amount;
    }

    /*! \fn  public void hit(int hitPower)
        \brief Incrementează contorul de lovituri cu valoarea primită.

        Dacă jucătorul nu mai este în viață adaugă se apelează isKilled().
  */
    @Override
    public void hit(int hitPower) {
        hitCounter += hitPower;
        if (hitCounter > attackResistence) {
            isKilled();
        }
    }

    /*! \fn   public void isKilled()
        \brief Ucide jucătorul și schimbă starea jocului în lost.
        \see LostState
     */
    private void isKilled() {
        alive = false;
        GameState.loadLost();

    }

    /*! \fn  public void resetHitCounter()
        \brief Resetează contorul de lovituri.
    */
    public void resetHitCounter() {
        hitCounter = 0;
    }

    /*! \fn  public void GODmodeON()
        \brief Modifică puterea de atac și rezistența la atac a jucătorului la valori foarte mari.
    */
    public void GODmodeON() {
        attackResistence = 9999999;
        attackPower = 9999999;
    }

    /*! \fn  public void GODmodeOFF()
        \brief Modifică puterea de atac și rezistența la atac a jucătorului la cele implicite.
    */
    public void GODmodeOFF() {
        attackResistence = defaultAttackResistance;
        attackPower = defaultAttackPower;
    }

    /*! \fn  public void fasterON()
        \brief Mărește viteza de deplasare a jucătorului.
    */
    public void fasterON() {
        stepSize = 8;
    }

    /*! \fn  public void fasterOFF()
        \brief Setează viteza de deplasare a jucătorului la valoarea implicită.
    */
    public void fasterOFF() {
        stepSize = 2;
    }

    /*! \fn  public void displayPlayerDetails()
        \brief Afișează detalii funționale legate de jucător.
    */
    public void displayPlayerDetails()
    {
        if (print) {
            System.out.print("\n\nx: " + xCoord + ", y: " + yCoord + "\nmatrixXCoord: " + matrixX + ", matrixYCoord: " + matrixY + "\n");
            System.out.print("Resistence: " + attackResistence + ", HitCounter: " + hitCounter + "\n" + "CloseAttackPower: " + attackPower + ", Speed: " + stepSize + "\n" + "Score: " + score + "\n");
        }

    }

    /*! \fn  public void stepVertical(Sign sign)
      \brief Actualizează poziția jucatorului atunci când se deplasează pe vertical.

      \param sign semnul pentru deplasare.
   */
    public void stepVertical(Sign sign)
    {
        updatePositionInMatrix();
        if (sign == Sign.PLUS) { //deplasare in jos
            if (currentMap.canAdvance(matrixX, matrixY + 1) || yCoord < matrixY * Tile.TILE_HEIGHT) {
                yCoord += stepSize;
            }

        } else { //deplasare in sus
            if (currentMap.canAdvance(matrixX, matrixY - 1) || yCoord > matrixY * Tile.TILE_HEIGHT) {
                yCoord -= stepSize;
            }
        }
        updatePositionInMatrix();
    }

    /*! \fn  public void stepHorizontal(Sign sign)
      \brief Actualizează poziția jucatorului atunci când se deplasează pe orizontal.

      \param sign semnul pentru deplasare.
   */
    public void stepHorizontal(Sign sign)
    {
        updatePositionInMatrix();
        if (sign == Sign.PLUS) //deplasare la dreapta
        {
            if (currentMap.canAdvance(matrixX + 1, matrixY) || xCoord < matrixX * Tile.TILE_HEIGHT) {
                xCoord += stepSize;
            }
        } else {//deplasare la stanga
            if (currentMap.canAdvance(matrixX - 1, matrixY) || xCoord > matrixX * Tile.TILE_HEIGHT) {
                xCoord -= stepSize;
            }

        }
        updatePositionInMatrix();
    }

    /*! \fn  public void updatePositionInMatrix()
     \brief Asigură coerența între seturile de coordonate ale jucătorului.

     Dacă jucătorul pașește într-o groapă sau într-un iaz îl ucide.
  */
    @Override
    public void updatePositionInMatrix() {
        matrixX = (xCoord + Tile.TILE_HEIGHT / 2) / Tile.TILE_HEIGHT;
        matrixY = (yCoord + Tile.TILE_HEIGHT / 2) / Tile.TILE_HEIGHT;
        if (player != null && currentMap.canKill(player.matrixX, player.matrixY))
        //daca jucatorul ajunge pe un camp insta kill este teleportat pe pozitia initiala
        {
            player.isKilled();
        }
        if (player != null && player.wasPositionChanged()) {
            notifyObservers();
        }

    }

    /*! \fn  private boolean wasPositionChanged()
     \brief Returnează dacă jucătorul și-a modificat poziția matriceală.
    */
    private boolean wasPositionChanged() {
        boolean result = false;

        if (matrixY != oldMatrixY) {
            oldMatrixY = matrixY;
            result = true;
        }
        if (matrixX != oldMatrixX) {
            oldMatrixX = matrixX;
            result = true;
        }

        return result;
    }

    /*! \fn   public void attach(Observer observer)
     \brief Atașează observatorul dat.
     \param observer observatorul de atașat.
    */
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /*! \fn    public void notifyObservers()
     \brief Notifică lista de observatori.
    */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.updateObserver();
        }
    }

    /*! \fn    public PlayerState getCurrentState()
     \brief Returnează starea curentă a jucătorului.
    */
    public PlayerState getCurrentState() {
        return currentState;
    }

    /*! \fn    public void setCurrentState(PlayerStates state)
     \brief Setează starea curentă a jucătorului la cea dată prin indetificator.

     \param state indetificatorul stării.
    */
    public void setCurrentState(PlayerStates state) {
        if(audioHandler==null)
        {
            audioHandler=AudioHandler.getAudioHandler();
        }
        switch (state) {
            case SHIELDON -> {
                currentState = getShieldON();
                audioHandler.playSoundEffect(Sounds.SHIELD.ordinal());
            }
            case SHIELDOFF -> {
               audioHandler.playSoundEffect(Sounds.SHIELD.ordinal());
                currentState = getShieldOFF();
            }

        }
        notifyObservers();

    }

    /*! \fn    public void setCurrentState(PlayerState state)
     \brief Setează starea curentă a jucătorului la cea dată prin valoare.

     \param state valoarea stării.
    */
    public void setCurrentState(PlayerState state) {
        currentState = state;
        notifyObservers();
    }

    /*! \fn  public boolean getAttackActive()
      \brief Returnează valoarea flag-ului attackActive.
    */
    public boolean getAttackActive() {
        return attackActive;
    }

    /*! \fn  public void setAttackActive(Boolean value)
     \brief Setează valoarea flag-ului attackActive la cea dată.
      \param attackActive noua valoare.
   */
    public void setAttackActive(Boolean value) {
        attackActive = value;
    }

    /*! \fn  public void updateObserver()
      \brief Primește actualizări legate de modificarea hărții curente.

      Actualizează referința către harta curentă.
   */
    @Override
    public void updateObserver() {
        currentMap = Map.getCurrentMap();
    }

    /*! \fn   public static void setScore(int value)
     \brief Setează valoarea scorului la cea dată.
      \param value noua valoare a scorului.
   */
    public static void setScore(int value) {
        score = value;
    }

    /*! \fn   public String getUsername()
     \brief Returnează valoarea numelui de utilizator.
   */
    public String getUsername() {
        return username;
    }

    /*! \fn    public void setUsername(String value)
     \brief Setează valoarea numelui de utilizator la cea dată.
     \param value noua valoare a numelui de utlizator.
   */
    public void setUsername(String value) {
        username = value;
    }

}
