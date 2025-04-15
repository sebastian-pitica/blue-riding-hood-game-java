package BlueRidingHood.State.Game;

import BlueRidingHood.Animation.AnimationHandler;
import BlueRidingHood.Entities.EnemieEntity;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Entities.ZaWalfo;
import BlueRidingHood.Factories.EntitiesFactory;
import BlueRidingHood.Game.Game;
import BlueRidingHood.Graphics.Assets;
import BlueRidingHood.Graphics.Tile;
import BlueRidingHood.InputManager.KeyboardInputManager;
import BlueRidingHood.InputManager.PlayerInputHandler;
import BlueRidingHood.Map.Map;
import BlueRidingHood.UI.UI;

import java.awt.*;

/*! \class PlayState
    \brief Starea de joc.

    Oferă metode pentru:\n
        -desenarea elementelor caracteristice.\n
        -actualizarea elementelor caracteristice.\n
        -prelucrarea evenimentelor mouse.

    \note Implementează design patternul **Singleton** și **State**.
 */

public class PlayState extends GameState {

    private static PlayState playState = null; /*!< Referință unică către starea de joc.*/
    private final PlayerInputHandler inputHandler; /*!< Referință la handlerul de input.*/
    private final UI gameui; /*!< Referință la interfața utilizator.*/
    private final Player player; /*!< Referință la jucător.*/
    private AnimationHandler animationHandler; /*!< Referință la handlerul de animații.*/
    private Image currentMapImage; /*!< Imaginea hărții curente.*/
    private Map currentMap; /*!< Referință la harta curentă.*/
    private boolean displayRect; /*!< Determină afișarea unui pătrat de încadrare pentru animația jucătorului.*/
    private boolean grid; /*!< Determină afișarea grătarului (grid-ului).*/

    /*! \fn protected PlayState()
        \brief Constructorul clasei PlayState.

        Inițializează referințele și variabilele locale.\n
        Pornește inițializarea aseturilor principale.
    */
    protected PlayState() {

        Game game = Game.provideGame();
        displayRect = false;
        grid = false;
        Assets.InitFirst();
        gameui = UI.getUI();
        player = Player.getPlayer();
        EntitiesFactory entitiesFactory = EntitiesFactory.getEntitiesFactory();
        currentMap = Map.getCurrentMap();
        currentMapImage = Assets.maps[currentMap.getMapNr() - 1];
        currentMap.attach(this);

        animationHandler = AnimationHandler.getAnimationHandler();
        inputHandler = PlayerInputHandler.getPlayerInputHandler();
        entitiesFactory.produceMapEntities();

        gameWindow = game.getGameWindow();
        keyboardInputManager = KeyboardInputManager.provideKeyboardInputManager();
        animationHandler = AnimationHandler.getAnimationHandler();

    }

    /*! \fn  public static PlayState getPlayState()
         \brief Returnează referința unică la starea de joc.
    */
    public static PlayState getPlayState() {
        if (playState == null) {
            playState = new PlayState();
        }
        return playState;
    }

    /*! \fn  public static void resetPlayState()
           \brief Resetează referința unică a stării de joc.
    */
    public static void resetPlayState() {
        playState = null;
        Map.resetMap();
        Player.resetPlayer();
        PlayerInputHandler.resetPlayerInputHandler();
        ZaWalfo.reset();
        EntitiesFactory.resetEntitiesFactory();
        AnimationHandler.resetAnimationHandler();
        EnemieEntity.resetPlayerRefs();
        UI.resetUI();
    }

    /*! \fn public abstract void Draw(Graphics graphics)
       \brief  Deseneaza elementele grafice în fereastră, coresponzătoar starilor actualizate ale elementelor.
       \param graphics grafica pentru desenare.
    */
    @Override
    public void Draw(Graphics graphics) {

        animationHandler.aniomationStopTimeHandler(); //verific daca trebuie oprit o posibila animatie in curs
        graphics.drawImage(currentMapImage, 0, 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
        animationHandler.drawAnimations(graphics);
        animationHandler.animationStartTimeHandler(); //verific daca trebuie pornita vreo animatie
        EnemieEntity.entitysFollowPlayer();
        EnemieEntity.checkHitPlayer();
        gameui.draw(graphics);

        graphics.setColor(Color.black);
        if (grid) {
            drawGrid(graphics);
        }

        if (displayRect) {
            drawRect(graphics);
        }

    }

    /*! \fn  public void Update()
       \brief Actualizează starea elementelor.
    */
    @Override
    public void Update() {
        if (gameWindow != null) {
            displayRect = keyboardInputManager.rectangular;
            grid = keyboardInputManager.grid;

            inputHandler.handler();
            animationHandler.runAnimations();

        }
    }

    /*! \fn  private void drawGrid(Graphics graphics)
       \brief Desenează gratarul (grid-ul)
      \param graphics grafica pentru desenare.

    */
    private void drawGrid(Graphics graphics) {
        for (int x = 0; x < gameWindow.GetWndWidth(); x += Tile.TILE_HEIGHT)
            for (int y = 0; y < gameWindow.GetWndHeight(); y += Tile.TILE_HEIGHT) {
                graphics.drawRect(x, y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            }
    }

    /*! \fn    private void drawRect(Graphics graphics)
      \brief Desenează pătratul de încadrare.
     \param graphics grafica pentru desenare.

   */
    private void drawRect(Graphics graphics) {
        graphics.drawRect(player.getxCoord(), player.getyCoord(), Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
        graphics.drawRect(player.getxCoord() + Tile.TILE_HEIGHT / 2 - 5, player.getyCoord() + Tile.TILE_HEIGHT / 2 - 5, 10, 10);
    }

    /*! \fn  public void updateObserver()
      \brief Primește actualizări legate de modificarea hărții curente.

      Actualizează referința către harta curentă.
    */
    @Override
    public void updateObserver() {
        currentMap = Map.getCurrentMap();
        currentMapImage = Assets.maps[currentMap.getMapNr() - 1];
    }

    /*! \fn public abstract void mouseHandler()
       \brief Modifică poziția jucătorului la aceea unde a fost făcut click dacă este posibil (nu e zid).
    */
    @Override
    public void mouseHandler() {
        if (currentMap.canAdvance((mouseX) / Tile.TILE_HEIGHT, (mouseY) / Tile.TILE_HEIGHT)) {
            player.setxCoord(mouseX - Tile.TILE_HEIGHT / 2);
            player.setyCoord(mouseY - Tile.TILE_WIDTH / 2);
            player.updatePositionInMatrix();
        }
    }
}
