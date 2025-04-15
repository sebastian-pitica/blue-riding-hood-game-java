package BlueRidingHood.Dijkstra;

import java.util.LinkedList;

import static BlueRidingHood.Dijkstra.Node.pathNodes;

/*! \class DijkstrasAlgorithm
    \brief Algoritmul lui Dijkstras.

    Funcționează împreună cu clasa Node.
    \note Sursa https://www.geeksforgeeks.org/printing-paths-dijkstras-shortest-path-algorithm/\n
           Modificat după nevoie.
 */

public class DijkstrasAlgorithm {

    private static final boolean print = false; /*!< Controlează afișările specifice clasei.*/
    private static final int NO_PARENT = -1; /*!< Valoarea pentru nici un părinte al nodului.*/

     /*! \fn public static void dijkstra(int[][] adjacencyMatrix, int startVertex, int endVertex)
           \brief Implementează algoritmul lui Dijkstras.
           \param adjacencyMatrix matricea de adiacență a grafului.
           \param startVertex nodul de început.
           \param endVertex nodul de sfârșit.
    */
    public static void dijkstra(int[][] adjacencyMatrix, int startVertex, int endVertex) {
        int nVertices = adjacencyMatrix[0].length;
        pathNodes = new LinkedList<>();

        // shortestDistances[i] will hold the
        // shortest distance from src to i
        int[] shortestDistances = new int[nVertices];

        // added[i] will true if vertex i is
        // included / in shortest path tree
        // or shortest distance from src to
        // i is finalized
        boolean[] added = new boolean[nVertices];

        // Initialize all distances as
        // INFINITE and added[] as false
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        // Distance of source vertex from
        // itself is always 0
        shortestDistances[startVertex] = 0;

        // Parent array to store shortest
        // path tree
        int[] parents = new int[nVertices];

        // The starting vertex does not
        // have a parent
        parents[startVertex] = NO_PARENT;

        // Find shortest path for all
        // vertices
        for (int i = 1; i < nVertices; i++) {

            // Pick the minimum distance vertex
            // from the set of vertices not yet
            // processed. nearestVertex is
            // always equal to startNode in
            // first iteration.
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            // Mark the picked vertex as
            // processed
            added[nearestVertex] = true;

            // Update dist value of the
            // adjacent vertices of the
            // picked vertex.
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
                int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance +
                            edgeDistance;
                }
            }
        }

        formSolution(startVertex, shortestDistances, parents, endVertex);
    }

    /*! \fn private static void formSolution(int startVertex, int[] distances, int[] parents, int vertexIndex)
           \brief Formează calea prin noduri.

            Începe afișarea ei dacă este cazul.

    */
    private static void formSolution(int startVertex, int[] distances, int[] parents, int vertexIndex) {

        if (print)
            print1(startVertex, distances, vertexIndex);

        formPath(vertexIndex, parents);

    }

    /*! \fn  private static void formPath(int currentVertex, int[] parents)
          \brief Formează calea prin noduri.

          Continuă afișarea ei dacă este cazul.
   */
    private static void formPath(int currentVertex, int[] parents) {
        if (currentVertex == NO_PARENT) {
            return;
        }
        formPath(parents[currentVertex], parents);
        pathNodes.add(currentVertex);

        if (print)
            print2(currentVertex);
    }

    /*! \fn  private static void print1(int startVertex, int[] distances, int vertexIndex)
          \brief Funcție de print a nodurilor.
     */
    private static void print1(int startVertex, int[] distances, int vertexIndex) {
        System.out.print("\n" + startVertex + " -> ");
        System.out.print(vertexIndex + " \t\t ");
        System.out.print(distances[vertexIndex] + "\t\t");
    }

    /*! \fn  private static void print2(int currentVertex)
          \brief Funcție de print a nodurilor.
     */
    private static void print2(int currentVertex) {
        System.out.print(currentVertex + " ");
    }

}
