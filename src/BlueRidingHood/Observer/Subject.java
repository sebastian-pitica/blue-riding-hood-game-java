package BlueRidingHood.Observer;

/*! \class Subject
    \brief Interfață pentru un subiect de observat.
    \note Implementează design patternul **Observer** alături de Observer.
 */

public interface Subject {
    /*! \fn   public void attach(Observer observer)
     \brief Atașează observatorul dat.
     \param observer observatorul de atașat.
    */
    void attach(Observer observer);

    /*! \fn public void notifyObservers()
    \brief Notifică lista de observatori.
   */
    void notifyObservers();
}
