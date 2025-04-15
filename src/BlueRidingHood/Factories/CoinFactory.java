package BlueRidingHood.Factories;

import BlueRidingHood.Map.Map;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/*! \class CoinFactory
    \brief Fabrică de monede.

    Oferă metode pentru:\n
        -creare de monede.

    \note Implementează design patternul **Singleton**.
    \note Nu poate implementa un observer pentru hartă, diferențele de timp ale apelurilor produc bug-uri în generarea pozițiilor
 */

public class CoinFactory {
    protected final Random randomGenerator; /*!< Generator de valori aleatoare pentru pozițiile "monedelor".*/
    protected static CoinFactory factory = null; /*!< Referință unică către fabrica de monede.*/

    /*! \fn protected CoinFactory()
          \brief Constructorul clasei CoinFactory.
   */
    protected CoinFactory() {
        randomGenerator = new Random();
    }

    /*! \fn public static CoinFactory provideCoinFactory()
           \brief Returnează referința unică către fabrica de monede.
    */
    public static CoinFactory provideCoinFactory() {
        if (factory == null) {
            factory = new CoinFactory();
        }
        return factory;
    }

    /*! \fn public Vector<Integer> createCoins(Map currentMap)
           \brief Creează și returnează "monedele".
    */
    public Vector<Integer> createCoins(Map currentMap) {

        LinkedList<Integer> availablePositions = currentMap.getAllAvailablePositions();

        int length = availablePositions.size();

        HashSet<Integer> actualPositionsToDraw = new HashSet<>();

        Vector<Integer> coinsPositions = new Vector<>(28);
        int i = 0;

        while (actualPositionsToDraw.size() != 28) {
            actualPositionsToDraw.add(availablePositions.get(randomGenerator.nextInt(length)));
        }

        for (int position : actualPositionsToDraw) {
            coinsPositions.insertElementAt(position, i++);
        }

        return coinsPositions;
    }

}
