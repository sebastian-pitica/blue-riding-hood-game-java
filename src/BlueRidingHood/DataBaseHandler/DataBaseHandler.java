package BlueRidingHood.DataBaseHandler;

import BlueRidingHood.Memento.Memento;
import BlueRidingHood.State.Game.ErrorState;
import BlueRidingHood.State.Game.GameState;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*! \class DataBaseHandler
    \brief Handler pentru baza de date.

    Oferă metode pentru:\n
        -închiderea conexiunilor cu bazele de date (deschiderea se face automat).\n
        -crearea/preluarea/actualizarea/verificarea existenței informațiilor în tabele.\n

    \note Implementează design patternul **Singleton**.
    \note Clasa este predispusă la excepții dar este asigurată astfel încât acestea să
     nu oprească buna funcționare a programului, sau dacă gravitatea este mai mare să informeze utilizatorul despre originea lor
     și să nu permită funcționarea defectuoasă.\n
     ex1. Nu s-a putut realiza conexiunea cu bazele de date.\n
     ex2. Nu s-au putut găsit datele necesare.
 */
public class DataBaseHandler {
    private Statement statement1 = null; /*!< Statement pentru baza de date ce conține hărțile.*/
    private Statement statement2 = null; /*!< Statement pentru baza de date ce conține game save-ul.*/
    private static DataBaseHandler dataBaseHandler = null;  /*!< Referință unică către handlerul pentru baza de date.*/
    private static boolean existSave;  /*!< Există sau nu un joc salvat în baza de date.*/
    Connection connection1, connection2;

    /*! \fn protected DataBaseHandler()
          \brief Constructorul clasei DataBaseHandler.

          Realizează conexiunea cu bazele de date corespunzătoare.
   */
    protected DataBaseHandler() {
        existSave = false;
        try {
            Class.forName("org.sqlite.JDBC");

            connection1 = DriverManager.getConnection("jdbc:sqlite:maps.db");
            statement1 = connection1.createStatement();

            connection2 = DriverManager.getConnection("jdbc:sqlite:savegame.db");
            statement2 = connection2.createStatement();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
            GameState.setCurrentState(new ErrorState("Nu s-a putut realiza conexiunea cu baza de date. Detalii: "+
                    e.getClass().getName() + ": " + e.getLocalizedMessage()));
            GameState.getCurrentState().notifyObservers();
        }
    }

    /*! \fn public static DataBaseHandler getDataBaseHandler()
         \brief Returnează referința unică a handlerului pentru baza de date.
     */
    public static DataBaseHandler getDataBaseHandler() {
        if (dataBaseHandler == null) {
            dataBaseHandler = new DataBaseHandler();
        }

        return dataBaseHandler;
    }

    /*! \fn public void close()
         \brief Închide statement-urile deschise.
     */
    public void close() {
        try {
            connection2.close();
            connection1.close();
            statement2.close();
            statement1.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
        }
    }

    /*! \fn public boolean checkSaveDB()
         \brief Verifică dacă există un joc salvat.
     */
    public boolean checkSaveDB() {
        try {
            ResultSet resultSet = statement2.executeQuery("SELECT 1 FROM SAVE limit 1");
            existSave = resultSet.next();
        } catch (Exception e) {
            existSave = false;
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
        }

        return existSave;
    }

    /*! \fn public void saveGame(Memento memento)
         \brief Salvează jocul în baza de date.
         \param memento memento-ul ce conține datele ce trebuiesc salvate.
     */
    public void saveGame(Memento memento) {

        String sql = "DELETE from SAVE where indice=1;";
        try {
            statement2.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
        }

        sql = "INSERT INTO SAVE VALUES (1,'" + memento.username + "'," + memento.score + ",'" + memento.playTime + "'," + memento.mapNr + ");";

        try {
            statement2.executeUpdate(sql);
            existSave = true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
        }
    }

    /*! \fn public void loadGame(Memento memento)
         \brief Încarcă jocul cu valorile din baza de date.
         \param memento memento-ul ce trebuie încărcat cu datele jocului.
     */
    public void loadGame(Memento memento) {
        try {
            ResultSet resultSet = statement2.executeQuery("SELECT * FROM SAVE;");
            resultSet.next();
            memento.username = resultSet.getString("username");
            memento.score = resultSet.getInt("score");
            memento.playTime = resultSet.getFloat("playtime");
            memento.mapNr = resultSet.getInt("mapNr");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
        }

    }

    /*! \fn public void createDBGame()
         \brief Crează baza de date pentru save-game.
     */
    public void createDBGame() {
        String sql = "CREATE TABLE SAVE (indice INT PRIMARY KEY, username TEXT," +
                "score INT, playtime TEXT, mapNr INT);";
        try {
            statement2.execute(sql);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
        }
    }


    /*! \fn public static boolean isSaved()
         \brief Returnează valoarea flag-ului existSave.
     */
    public static boolean isSaved() {
        return existSave;
    }

    /*! \fn  public int[][] getMap(int index)
         \brief Preia din baza de date matricea hărții date prin index.
         \param index indexul hărții.
         \returns Matricea hărții.
     */
    public int[][] getMap(int index) {
        int[][] map = new int[16][30];

        try {
            ResultSet resultSet = statement1.executeQuery("SELECT * FROM map"+index+";");
            for (int i = 0; i < 16; ++i) {
                resultSet.next();
                for (int j = 0; j < 30; ++j) {
                    map[i][j] = resultSet.getInt("Coloana" + j);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
            GameState.setCurrentState(new ErrorState("Nu s-a putut prelua harta din baza de date! Detalii: "+
                    e.getClass().getName() + ": " + e.getLocalizedMessage()));
            GameState.getCurrentState().notifyObservers();
        }

        return map;
    }

    /*! \fn public void createDBMap()
        \brief Crează baza de date pentru hărți.
    */
    public void createDBMap() {

        for(int i=0;i<3;++i) {

            StringBuilder sql = new StringBuilder("CREATE TABLE map" + (i + 1) +
                    "(Rand TEXT PRIMARY KEY NOT NULL,");

            for (int j = 0; j < maps[i][0].length - 1; ++j) {
                sql.append("Coloana").append(j).append(" INT NOT NULL,");
            }

            sql.append("Coloana").append(maps[i][0].length - 1).append(" INT NOT NULL);");

            try {
                statement1.execute(sql.toString());

                for (int j = 0; j < maps[i].length; ++j) {
                    sql = new StringBuilder("INSERT INTO map" + (i + 1) + " VALUES ('Rand" + j + "'");

                    //
                    for (int k = 0; k < maps[i][0].length - 1; ++k) {
                        sql.append(",").append(maps[i][j][k]);
                    }
                    sql.append(",").append(maps[i][j][maps[i][0].length - 1]).append(")");

                    try {
                        statement1.executeUpdate(sql.toString());
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    //

                }
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
            }
        }
    }
    private static final int[][][] maps = new int[][][]{{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 3, 3, 3, 1, 1, 1, 1, 1, 1, 0, 3, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 3, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 3, 0},
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 1, 1, 1, 1, 0, 3, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0},
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0},
            {0, 3, 0, 1, 0, 1, 1, 1, 0, 3, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 3, 0, 1, 1, 3, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 3, 0},
            {0, 3, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0},
            {0, 1, 1, 1, 0, 3, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0},
            {0, 3, 3, 3, 0, 1, 1, 1, 0, 3, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 3, 0, 0, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}},
            {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 3, 0, 1, 1, 1, 1, 3, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 3, 3, 3, 0},
            {0, 3, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 3, 1, 1, 0, 1, 0, 3, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 0, 3, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 3, 0},
            {0, 3, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 3, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 3, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 3, 0},
            {0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 3, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 3, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 1, 0, 1, 0, 3, 0},
            {0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1},
            {0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0},
            {0, 3, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 3, 0, 3, 3, 3, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 3, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}},
            {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 3, 3, 3, 1, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 0, 3, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0},
            {1, 1, 0, 3, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0},
            {0, 1, 0, 0, 0, 1, 0, 3, 0, 1, 3, 0, 1, 0, 3, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 3, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 3, 0},
            {0, 3, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 3, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 3, 1, 1, 1, 1, 1, 1, 3, 0, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}}};

}  /*!< Matricea ce conține matricele hărților.*/


