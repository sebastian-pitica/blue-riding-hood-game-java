package BlueRidingHood.Observer;

/*! \class Observer
    \brief Interfață pentru un observator.
    \note Implementează design patternul **Observer** alături de Subject.
 */

public interface Observer {
    /*! \fn  public void updateObserver()
      \brief Primește actualizări legate de modificarea subiectului urmărit.
   */
    void updateObserver();
}
