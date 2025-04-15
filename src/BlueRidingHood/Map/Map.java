package BlueRidingHood.Map;

import BlueRidingHood.DataBaseHandler.DataBaseHandler;
import BlueRidingHood.Observer.Observer;
import BlueRidingHood.Observer.Subject;

import java.util.LinkedList;

/*! \class Map
    \brief Implementeză harta și operațiile cu harta.

    Oferă metode pentru:\n
        -setarea/preluarea hărții curente.\n
        -preluarea identificatorului hărții curente.\n
        -preluarea matricii hărții curente.\n
        -preluarea pozițiilor disponibile pentru deplasare/creare de entități.\n
        -verificarea posibilității de a avansa pe o anumită poziție.\n
        -verificarea existenței unui element cu ucidere instantă pe o anumită poziție.\n
        -preluarea cordonatei y de început/sfărșit (cordonata x este mereu aceeași).

    \note Implementează design patternul **Observer**.
 */

public class Map implements Subject {

    private static final Map map1 = new Map(1); /*!< Referință unică către prima hartă.*/
    private static final Map map2 = new Map(2);/*!< Referință unică către a doua hartă.*/
    private static final Map map3 = new Map(3);/*!< Referință unică către a treia hartă.*/
    private static Map currentMap; /*!< Referință către harta curentă.*/
    private static LinkedList<Observer> observers; /*!< Lista cu observatori.*/
    private final int mapNr; /*!< Indetificatorul hărții.*/
    private int[][] matrix; /*!< Matricea hărții.*/
    private final DataBaseHandler dataBaseHandler; /*!< Referință către managerul bazei de date.*/
    private LinkedList<Integer> allAvailablePositions = null; /*!< Lista cu pozițiile disponibile pentru creare entităților.*/

    /*! \fn  private Map(int mapNumber)
        \brief Constructorul clasei Map.
        \param mapNumber indicele hărții curente.
        Inițializează referințele și variablilele locale.\n
   */
    private Map(int mapNumber) {
        this.dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        dataBaseHandler.createDBMap();
        this.mapNr = mapNumber;
        switch (mapNumber) {
            case 1 -> {
                observers = new LinkedList<>();
                this.matrix = dataBaseHandler.getMap(1);
            }

            case 2 -> this.matrix = dataBaseHandler.getMap(2);


            case 3 -> this.matrix = dataBaseHandler.getMap(3);
        }

    }

    /*! \fn public static void resetMap()
        \brief Resetează harta curentă și lista de observatori.
    */
    public static void resetMap() {
        observers = new LinkedList<>();
        currentMap = map1;
    }

    /*! \fn public static void setMap(int mapNr)
        \brief Setează harta curentă la cea specificată prin indice.
        \param mapNr indicele hărții
    */
    public static void setMap(int mapNr) {
        switch (mapNr) {
            case 2 -> currentMap = map2;
            case 3 -> currentMap = map3;
        }
    }

    /*! \fn public static Map getCurrentMap()
        \brief Returnează referința la harta curentă.
    */
    public static Map getCurrentMap() {
        if (currentMap == null)
            currentMap = map1;
        return currentMap;
    }

    /*! \fn public int getMapNr()
       \brief Returnează identificatorul hărții.
    */
    public int getMapNr() {
        return currentMap.mapNr;
    }

    /*! \fn public int[][] getMatrix()
       \brief Returnează matricea hărții.
    */
    public int[][] getMatrix() {
        return matrix;
    }

    /*! \fn public LinkedList<Integer> getAllAvailablePositions()
       \brief Obține și returnează pozițiile disponibile pentru crearea entităților.

       Dacă pozițiile au fost deja calculate returnează referința la lista respectivă.
    */
    public LinkedList<Integer> getAllAvailablePositions() {
        if (allAvailablePositions == null) {
            allAvailablePositions = new LinkedList<>();

            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 30; ++j) {
                    if (matrix[i][j] == 1) {
                        allAvailablePositions.add(i * 100 + j);
                    }
                }
            }
        }

        return allAvailablePositions;
    }

    /*! \fn  public boolean canAdvance(int x, int y)
       \brief Returnează dacă pe poziția matriceală x,y se poate înainta.

    */
    public boolean canAdvance(int x, int y)
    //y de pe ecran in matrice este indicele pentru rand
    //x de pe ecran in matrice este indicele pentru coloana
    {
        if (y >= 0 && x >= 0) {
            return matrix[y][x] == 1 || matrix[y][x] == 3;
        } else
            return false;

    }

    /*! \fn   public boolean canKill(int x, int y)
       \brief Returnează dacă pe poziția matriceală x,y se află un element ce va ucide jucătorul.
       \param x coordonata matriceală x.
       \param y coordonata matriceală y.
    */
    public boolean canKill(int x, int y)
    //y de pe ecran in matrice este indicele pentru rand
    //x de pe ecran in matrice este indicele pentru coloana
    {
        return matrix[y][x] == 3;
    }

    /*! \fn   public boolean end(int x, int y)
       \brief Returnează dacă pe poziția matriceală x,y se află finalul hărții.
        \param x coordonata matriceală x.
       \param y coordonata matriceală y.
    */
    public boolean end(int x, int y)
    //y de pe ecran in matrice este indicele pentru rand
    //x de pe ecran in matrice este indicele pentru coloana
    {
        if (y >= 0 && x >= 0) {
            return y == endY() && x == 29;
        } else
            return false;

    }

    /*! \fn public int startY()
       \brief Furnizează poziția, de start, matriceală a lui y.
    */
    public int startY()
    {
        if (mapNr == 1) {
            return 10;
        } else {
            if (mapNr == 2) {
                return 8;
            } else {
                return 6;
            }
        }
    }


    /*! \fn   public int endY()
       \brief Furnizează poziția, de final, matriceală a lui y.

    */
    public int endY()
    {
        if (mapNr == 1) {
            return 14;
        } else {
            if (mapNr == 2) {
                return 12;
            } else {
                return -1;
            }
        }

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
}
