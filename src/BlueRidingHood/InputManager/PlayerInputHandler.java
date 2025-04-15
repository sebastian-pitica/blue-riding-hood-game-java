package BlueRidingHood.InputManager;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.Entities.EnemieEntity;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Factories.EntitiesFactory;
import BlueRidingHood.Game.Enums.Sign;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Game.Game;
import BlueRidingHood.Graphics.Tile;
import BlueRidingHood.Map.Map;
import BlueRidingHood.Observer.Observer;
import BlueRidingHood.State.Game.GameState;
import BlueRidingHood.State.Game.InMenuState;

import static BlueRidingHood.Entities.EnemieEntity.hitEntityAtCoords;
import static BlueRidingHood.Entities.EnemieEntity.isAnotherEntityHere;

/*! \class PlayerInputHandler
    \brief Implementeză trecerea din input de la tastatură în acțiuni concrete.

    Oferă metode pentru:\n
        -prelucrarea inputului de la tastatură.

    \note Implementează design patternul **Singleton** și **Observer**.
    \note Observer pentru Map.
 */


public class PlayerInputHandler implements Observer {
    private static PlayerInputHandler playerInputHandler = null; /*!< Referință unică către managerul de input.*/
    private final KeyboardInputManager keyboardInputManager; /*!< Referința către managerul de input de la tastatură.*/
    private final Player player; /*!< Referința către jucător.*/
    private final long hitEntityTimeLimit = 1000000000; /*!< Limita de timp între două lovituri consecutive ale jucătorului.*/
    private final EntitiesFactory entitiesFactory; /*!< Referința către fabrica de entități.*/
    private Map currentMap; /*!< Referința către harta curentă.*/
    private long hitEntityStartTime = 0; /*!< Timpul la care a fost lovită entitatea anterior.*/
    private AudioHandler audioHandler=null; /*!< Referința către handlerul audio.*/

    /*! \fn protected PlayerInputHandler()
         \brief Constructorul clasei PlayerInputHandler.

         Inițializează referințele locale.
    */
    protected PlayerInputHandler() {
        keyboardInputManager = KeyboardInputManager.provideKeyboardInputManager();
        entitiesFactory = EntitiesFactory.getEntitiesFactory();
        currentMap = Map.getCurrentMap();
        currentMap.attach(this);
        player = Player.getPlayer();
    }

    /*! \fn  public static PlayerInputHandler getPlayerInputHandler()
         \brief Returnează referința unică a handlerului de input.
    */
    public static PlayerInputHandler getPlayerInputHandler() {
        if (playerInputHandler == null) {
            playerInputHandler = new PlayerInputHandler();
        }

        return playerInputHandler;
    }

    /*! \fn   public static void resetPlayerInputHandler()
           \brief Resetează referința unică a handlerului de input.
    */
    public static void resetPlayerInputHandler() {
        playerInputHandler = null;
    }

    /*! \fn   public void handler()
           \brief Funcția principală care se ocupă de prelucrări.
    */
    public void handler()
    {

        if (!player.getAttackActive()) {
            //restrictionez miscarile si atacul simultan
            if (keyboardInputManager.left || keyboardInputManager.right)
            //restrictionez miscarile simultane pe cele doua axe
            {
                if (keyboardInputManager.left) {
                    player.stepHorizontal(Sign.MINUS);
                    player.displayPlayerDetails();
                }
                if (keyboardInputManager.right) {
                    if (!currentMap.end(player.getMatrixX(), player.getMatrixY())) {
                        player.stepHorizontal(Sign.PLUS);
                        player.displayPlayerDetails();

                    } else {
                        win();
                    }
                }
                player.displayPlayerDetails();
            } else {
                if (keyboardInputManager.up) {
                    player.stepVertical(Sign.MINUS);
                    player.displayPlayerDetails();

                }
                if (keyboardInputManager.down) {
                    player.stepVertical(Sign.PLUS);
                    player.displayPlayerDetails();

                }
            }

        }
        //reset pozitie jucator
        else {
            if (System.nanoTime() - hitEntityStartTime >= hitEntityTimeLimit && isAnotherEntityHere(player.getMatrixX(), player.getMatrixY(), null)) {
                hitEntityStartTime = System.nanoTime();
                hitEntityAtCoords(player.getMatrixX(), player.getMatrixY(), Player.getAttackPower());
            }

        }
        if (keyboardInputManager.reset) {
            player.setyCoord(currentMap.startY() * Tile.TILE_HEIGHT);
            player.setxCoord(0);
        }


        if (keyboardInputManager.quit) {
            Game.provideGame().quit();
        }

        if (keyboardInputManager.escape) {
            GameState.setCurrentState(InMenuState.getInMenuState());
            GameState.getCurrentState().notifyObservers();

            if(audioHandler==null)
                audioHandler = AudioHandler.getAudioHandler();

            audioHandler.stopAll();
            audioHandler.playMusic(Sounds.AMBIENT.ordinal());

        }

        if (keyboardInputManager.GODModeOn) {
            player.GODmodeON();
        } else {
            player.GODmodeOFF();
        }

        if (keyboardInputManager.faster) {
            player.fasterON();
        } else {
            player.fasterOFF();
        }

        if (keyboardInputManager.killAllEnemies) {
            EnemieEntity.killThemAll();
        }

        if (keyboardInputManager.resetHitCounter) {
            player.resetHitCounter();
        }

    }

    /*! \fn  private void win()
          \brief Funcție care determină ce se întâmplă când se îndeplinește câștigul la nivelul curent.
          Nu se ocupă de câștigul la ultimul nivel.
   */
    private void win()
    {
        System.out.println("You passed map number: " + currentMap.getMapNr() + "!");
        Map.setMap(currentMap.getMapNr() + 1);
        currentMap.notifyObservers();
        player.addPointsToScore(350);
        player.setyCoord(currentMap.startY() * Tile.TILE_HEIGHT);
        player.setxCoord(0);
        player.resetHitCounter();//ai scris ca la noul nivel se reseteaza hit counterul
        entitiesFactory.produceMapEntities();

        if (currentMap.getMapNr() == 3) {

            AudioHandler.playLastLevelAudio();
        }

        if (GameState.getPlayerWantsSaves()) {
            if(audioHandler==null)
                audioHandler = AudioHandler.getAudioHandler();

            GameState.loadSaveGameState();
        }


    }

    /*! \fn  public void updateObserver()
      \brief Primește actualizări legate de modificarea hărții curente.
   */
    @Override
    public void updateObserver() {
        currentMap = Map.getCurrentMap();
    }
}
