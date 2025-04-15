package BlueRidingHood.Dijkstra;

import BlueRidingHood.Map.Map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/*! \class Node
    \brief Algoritmul lui Dijkstras.

    Funcționează împreună cu clasa DijkstrasAlgorithm.\n
    Oferă metode pentru:\n
        -preluarea căii de urmat între două puncte date aparținând matricii hărții curente.

    \note Sursa https://www.geeksforgeeks.org/printing-paths-dijkstras-shortest-path-algorithm/\n
            Modificat după nevoie.
     \note Nu se poate face un observer pentru hartă deoarece se lucrează cu static.
 */
public class Node {
    public static LinkedList<Integer> pathNodes; /*!< Calea prin noduri modificată de clasa DijkstrasAlgorithm.*/
    private static LinkedHashMap<Integer, Node> mapNodes; /*!< Dicționar ce mapează fiecarui nod un cod de ordine întreg.*/
    private static HashMap<Integer, Integer> nodeDictionary; /*!< Dicționar ce mapează fiecarui cod întreg, reprezentând un nod, un întreg ce reprezintă coordonatele lui, concatenate,
                                                                în matricea hărții.*/
    private static int[][] adiacenteMatrix; /*!< Matricea de adiacență.*/
    private static int mapIdForData = 0;  /*!< Variabilă prin care se recalculează datele corespunzătoare doar dacă harta s-a schimbat.*/
    private static Map currentMap;  /*!< Referință către harta curentă.*/
    private static int lastStart = -1, lastStop = -1; /*!< Se recalculează datele corespunzătoare doar dacă nodul de start sau de sfarșit s-a modificat.*/
    final int value; /*!< Valoarea dintr-un nod reprezentând coordonatele sale în matricea hărții, concatenate.*/
    final int indice; /*!< Codul de ordine al nodului.*/
    final int up; /*!< Valoara vecinului de sus.*/
    final int down;/*!< Valoara vecinului de jos.*/
    final int left; /*!< Valoara vecinului din stânga.*/
    final int right; /*!< Valoara vecinului din dreapta.*/

    /*! \fn  private Node(int indice, int value, int left, int right, int up, int down)
           \brief Constructorul clasei Node.
            \param indice codul de ordine al nodului.
            \param value coordonatele nodului în matricea hărții.
            \param left coordonatele vecinului din stânga.
            \param right coordonatele vecinului din dreapta.
            \param up coordonatele vecinului de sus.
            \param down coordonatele vecinului de jos.
    */
    private Node(int indice, int value, int left, int right, int up, int down) {
        this.down = down;
        this.up = up;
        this.left = left;
        this.right = right;
        this.indice = indice;
        this.value = value;
    }

    /*! \fn  private static void buildMapNodes()
           \brief Construiește nodurile hărții curente.
    */
    private static void buildMapNodes() {
        int[][] map = currentMap.getMatrix();

        for (int i = 0; i < map.length; ++i) {

            for (int j = 0; j < map[0].length; ++j) {
                int up = -1, down = -1, left = -1, right = -1;

                if (map[i][j] == 1) {
                    int value = i * 100 + j;

                    if (((i - 1) >= 0) && map[i - 1][j] == 1) {
                        up = (i - 1) * 100 + j;
                    }
                    if (((i + 1)) < map.length && map[i + 1][j] == 1) {
                        down = (i + 1) * 100 + j;
                    }
                    if ((j - 1) >= 0 && map[i][j - 1] == 1) {
                        left = i * 100 + j - 1;
                    }
                    if ((j + 1) < map[0].length && map[i][j + 1] == 1) {
                        right = i * 100 + j + 1;
                    }

                    mapNodes.put(mapNodes.size(), new Node(mapNodes.size(), value, left, right, up, down));
                    nodeDictionary.put(nodeDictionary.size(), value);
                }
            }
        }
    }

    /*! \fn  private static void buildAdiMatrix()
           \brief Construiește matricea de adiacență pentru harta curentă.
    */
    private static void buildAdiMatrix() {
        adiacenteMatrix = new int[mapNodes.size()][mapNodes.size()];

        for (int i = 0; i < mapNodes.size(); ++i) {
            Node currentNode = mapNodes.get(i);
            if (currentNode.left != -1) {
                for (Node element : mapNodes.values()) {
                    if (element.value == currentNode.left) {
                        adiacenteMatrix[i][element.indice] = 1;
                        break;
                    }
                }
            }
            if (currentNode.right != -1) {
                for (Node element : mapNodes.values()) {
                    if (element.value == currentNode.right) {
                        adiacenteMatrix[i][element.indice] = 1;
                        break;
                    }
                }
            }
            if (currentNode.up != -1) {
                for (Node element : mapNodes.values()) {
                    if (element.value == currentNode.up) {
                        adiacenteMatrix[i][element.indice] = 1;
                        break;
                    }
                }
            }
            if (currentNode.down != -1) {
                for (Node element : mapNodes.values()) {
                    if (element.value == currentNode.down) {
                        adiacenteMatrix[i][element.indice] = 1;
                        break;
                    }
                }
            }


        }

    }

    /*! \fn  private static void clean()
          \brief Curăță datele specifice acumulate.
   */
    private static void clean() {
        mapNodes = new LinkedHashMap<>();
        nodeDictionary = new HashMap<>();
        pathNodes = new LinkedList<>();
        adiacenteMatrix = null;
        mapIdForData = 0;
    }

    /*! \fn  private static void build()
          \brief Preia referința către harta curentă.
   */
    private static void build() {
        currentMap = Map.getCurrentMap();
        mapIdForData = currentMap.getMapNr();
    }

    /*! \fn  private static int getNodeNumber(int value)
          \brief Returnează codul de ordine pentru nodul dat prin valoarea value.
          \param value valoarea nodului pentru care se dorește codul de ordine.
   */
    private static int getNodeNumber(int value) {
        for (Integer key : nodeDictionary.keySet()) {
            if (nodeDictionary.get(key) == value) {
                return key;
            }
            if (nodeDictionary.get(key) > value) {
                break;
            }
        }

        return -1;
    }

    /*! \fn private static void translatePath()
          \brief Traduce nodurile din pathNodes.

          Transformă calea de noduri, formată din coduri de ordine, în cale de noduri formate din valorile nodurilor.\n
   */
    private static void translatePath() {
        LinkedList<Integer> coordsPath = new LinkedList<>();
        for (int element : pathNodes) {
            int translatedCoord = 0;
            for (int key : nodeDictionary.keySet()) {
                if (element == key) {
                    translatedCoord = nodeDictionary.get(key);
                    break;
                }
            }
            coordsPath.add(translatedCoord);
        }
        pathNodes = coordsPath;
    }

    /*! \fn public static LinkedList<Integer> getPath(int start, int stop)
          \brief Returnează calea de noduri formată din valorile acestora.
          \param start nodul de start dat prin valoarea sa.
          \param end nodul de sfârșit dat prin valoarea sa.
   */
    public static LinkedList<Integer> getPath(int start, int stop) {
        if (mapIdForData != Map.getCurrentMap().getMapNr())
        //daca difera noua cerere de cea veche
        {
            clean();
            build();
            buildMapNodes();
            buildAdiMatrix();
        }

        if (start != lastStart || stop != lastStop)
        //daca difera noua cerere de cea veche
        {
            lastStart = start;
            lastStop = stop;
            if (getNodeNumber(start) != -1 && getNodeNumber(stop) != -1) {
                DijkstrasAlgorithm.dijkstra(adiacenteMatrix, getNodeNumber(start), getNodeNumber(stop));
                translatePath();
                return pathNodes;
            } else {
                return null;
            }
        } else {
            return pathNodes;
        }
    }

    @Override
    public String toString() {
        return "value: " + value + ", indice: " + indice + ", up: " + up + ", down: " + down + ", left: " + left + ", right: " + right + "\n";
    }

}

