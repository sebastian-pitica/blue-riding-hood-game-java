package BlueRidingHood.Memento;

import BlueRidingHood.Audio.AudioHandler;
import BlueRidingHood.DataBaseHandler.DataBaseHandler;
import BlueRidingHood.Entities.EnemieEntity;
import BlueRidingHood.Entities.Player;
import BlueRidingHood.Factories.EntitiesFactory;
import BlueRidingHood.Map.Map;
import BlueRidingHood.State.Game.GameState;
import BlueRidingHood.UI.UI;

/*! \class Memento
    \brief Implementeză salvarea/încărcarea progresului.

    Oferă metode pentru:\n
        -salvarea/încărcarea progresului.\n
        -salvarea/încărcarea progresului în/din baza de date.


    \note Implementează design patternul **Memento**.
 */

public class Memento {
    public int score; /*!< Scorul jucătorului.*/
    public float playTime; /*!< Timpul de joc.*/
    public int mapNr; /*!< Identificatorul hărții.*/
    public String username; /*!< Numele de utilizator al jucătorului.*/
    public final DataBaseHandler dataBaseHandler; /*!< Referință către mangerul de baze de date.*/

    /*! \fn  public Memento()
          \brief Constructorul clasei Memento.
    */
    public Memento() {
        dataBaseHandler = DataBaseHandler.getDataBaseHandler();
    }

    /*! \fn  public void getState()
          \brief Preia datele necesare pentru a putea salva un memento.
    */
    public void getState() {
        username = Player.getPlayer().getUsername();
        mapNr = Map.getCurrentMap().getMapNr();
        score = Player.getScore();
        playTime = UI.getTotalPlaytime();
    }

    /*! \fn  public void setState()
          \brief Încarcă datele salvate în memento.
    */
    public void setState() {
        Map.setMap(mapNr);
        Map.getCurrentMap().notifyObservers();
        EntitiesFactory.resetEntitiesFactory();
        Player.resetPlayer();
        EnemieEntity.resetPlayerRefs();
        Player.getPlayer().setUsername(username);
        Player.setScore(score);
        UI.setPlaytime(playTime);

        if (mapNr == 3 && GameState.getPlaySound()) {
            AudioHandler.playLastLevelAudio();
        }
    }

    /*! \fn  public void saveGame()
          \brief Salvează memento-ul în baza de date.
    */
    public void saveGame() {
        dataBaseHandler.saveGame(this);
    }

    /*! \fn  public void loadGame()
          \brief Preia memento-ul din baza de date.
    */
    public void loadGame() {
        dataBaseHandler.loadGame(this);
    }
}
