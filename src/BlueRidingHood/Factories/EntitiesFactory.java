package BlueRidingHood.Factories;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.Entities.BasicEnemieEntities.*;
import BlueRidingHood.Entities.EnemieEntity;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Entities.ZaWalfo;
import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.Map.Map;
import BlueRidingHood.Observer.Observer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static BlueRidingHood.Entities.EnemieEntity.actualEntities;

/*! \class EntitiesFactory
    \brief Fabrică de entități inamice.

    Oferă metode pentru:\n
        -crearea entităților.\n
        -crearea entităților de tip gardă.

    \note Implementează design patternul **Singleton** și **Observer**.
    \note Observer pentru Map.
 */

public class EntitiesFactory implements Observer {
    private static final int maxEntityNumberPerMap = 4;/*!< Numărul maxim de entități per hartă.*/
    private static EntitiesFactory entitiesFactory = null; /*!< Referință unică către fabrica de entități.*/
    private static AudioHandler audioHandler; /*!< Referință către handlerul audio.*/
    private final Player player = Player.getPlayer(); /*!< Referință către jucător.*/
    private Map currentMap; /*!< Referință către harta curentă.*/
    private final Random randomGenerator; /*!< Generator de valori aleatoare pentru pozițiile entităților.*/


    /*! \fn protected EntitiesFactory()
        \brief Constructorul clasei EntitiesFactory.

         Inițializează valorile specifice.
    */
    protected EntitiesFactory() {
        currentMap = Map.getCurrentMap();
        currentMap.attach(this);
        audioHandler = AudioHandler.getAudioHandler();
        randomGenerator = new Random();
    }

    /*! \fn  public static EntitiesFactory getEntitiesFactory()
          \brief Returnează referința unică către fabrica de entități.
   */
    public static EntitiesFactory getEntitiesFactory() {
        if (entitiesFactory == null) {
            entitiesFactory = new EntitiesFactory();

        }

        return entitiesFactory;
    }

    /*! \fn public static void resetEntitiesFactory()
           \brief Resetează referința unică către fabrica de entități și colecția cu entități curente.
    */
    public static void resetEntitiesFactory() {
        entitiesFactory = null;
        actualEntities = null;
    }


    /*! \fn public void produceMapEntities()
           \brief Creează entitățile inamice specifice hărții curente.
    */
    public void produceMapEntities() {

        CopyOnWriteArrayList<EnemieEntity> result = new CopyOnWriteArrayList<>();
        LinkedList<Integer> availablePositions = currentMap.getAllAvailablePositions();
        int mapNr = currentMap.getMapNr();
        int length = availablePositions.size();
        LinkedList<Integer> actualPositions = new LinkedList<>();

        if (mapNr != 3) {
            while (actualPositions.size() != maxEntityNumberPerMap) {
                actualPositions.add(availablePositions.get(randomGenerator.nextInt(length)));
            }

            for (int i = 0; actualPositions.size() != 0; ++i) {
                Integer position = actualPositions.get(0);
                actualPositions.remove(0);
                int y = position / 100;
                int x = position % 100;

                if (x >= 0 && y >= 0) {
                    if (mapNr == 1) {
                        if (i < maxEntityNumberPerMap / 2) {
                            Fox1 fox1 = new Fox1(x, y);
                            result.add(fox1);
                            player.attach(fox1);
                        } else {
                            Fox2 fox2 = new Fox2(x, y);
                            result.add(fox2);
                            player.attach(fox2);
                        }
                    } else {

                        if (i < maxEntityNumberPerMap / 2) {
                            Bear1 bear1 = new Bear1(x, y);
                            result.add(bear1);
                            player.attach(bear1);
                        } else {
                            Bear2 bear2 = new Bear2(x, y);
                            result.add(bear2);
                            player.attach(bear2);
                        }
                    }
                }
            }
        } else {
            ZaWalfo zaWalfo = ZaWalfo.getZaWalfo();
            result.addAll(produceWolfs());
            result.add(zaWalfo);
            player.attach(zaWalfo);
        }

        actualEntities = result;
    }

    /*! \fn public CopyOnWriteArrayList<Wolf> produceWolfs()
          \brief Returnează gărzi.

          Redă sunetul de creare al gărzilor.
   */
    public CopyOnWriteArrayList<Wolf> produceWolfs() {
        CopyOnWriteArrayList<Wolf> result = new CopyOnWriteArrayList<>();

        LinkedList<Integer> availablePositions = currentMap.getAllAvailablePositions();

        int length = availablePositions.size();

        HashSet<Integer> actualPositions = new HashSet<>();

        while (actualPositions.size() != 3) {
            actualPositions.add(availablePositions.get(randomGenerator.nextInt(length)));
        }

        for (int position : actualPositions) {
            int y = position / 100;
            int x = position % 100;

            if (x >= 0 && y >= 0) {
                Wolf wolf = new Wolf(x, y);
                result.add(wolf);
                player.attach(wolf);
            }
        }

        audioHandler.playSoundEffect(Sounds.WOLFSHOWL.ordinal());
        return result;
    }

    /*! \fn  public void updateObserver()
      \brief Primește actualizări legate de modificarea hărții curente.

      Actualizează referința către harta curentă.
   */
    @Override
    public void updateObserver() {

        currentMap = Map.getCurrentMap();
    }
}
