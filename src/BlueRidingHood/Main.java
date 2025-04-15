package BlueRidingHood;

import BlueRidingHood.DataBaseHandler.DataBaseHandler;
import BlueRidingHood.Game.Game;

/*! \class Main
    \brief Implementează metoda main().
 */
public class Main {
    /*! \fn public static void main(String[] args)
        \brief Metoda main

        Pornește jocul.
    */
    public static void main(String[] args) {
        Game BlueRidingHood = Game.provideGame();
        DataBaseHandler.getDataBaseHandler().createDBGame();
        BlueRidingHood.StartGame();
    }
}

