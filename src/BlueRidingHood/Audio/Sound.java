package BlueRidingHood.Audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*! \class Sound
    \brief Implementează un sunet.

    Oferă metode pentru:\n
        -redarea unui sunet o singură dată sau în buclă.\n
        -oprirea redării unui sunet.\n
        -setarea/preluarea timpului la care s-a redat sunetul ultima dată.

    \note Sursă de inspirație https://www.youtube.com/watch?v=nUHh_J2Acy8&t=378s
    \note Clasa este predispusă la excepții dar este asigurată astfel încât acestea să
     nu oprească buna funcționare a programului.\n
     ex1. Sunetul nu există fizic (lipsește, este numit altfel, este corupt sau nu a putut fi încărcat din orice alt motiv).\n
     ex2. Oprirea/buclarea unui sunet care nu a fost pornit.
 */

public class Sound {
    private Clip clip; /*!< Clipul audio al sunetului.*/
    private final int index;  /*!< Indexul sunetului.*/
    private long lastTimePlayed = 0; /*!< Timpul când a fost redat ultima dată.*/

    /*! \fn  public Sound(int index)
           \brief Constructorul clasei Sound.
            \param index valoarea indexului pentru sunet.
    */
    public Sound(int index) {
        this.index = index;
    }

    /*! \fn  public void play()
          \brief Redă sunetul.
    */
    public void play() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(AudioURLLoader.getSoundsURL(index));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        } catch (Exception ignored) {

        }
    }

    /*! \fn  public void loop()
         \brief Redă sunetul în buclă.
    */
    public void loop() {
        try {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ignored) {

        }
    }

    /*! \fn  public void stop()
         \brief Oprește sunetul.
    */
    public void stop() {
        try {
            clip.stop();
        } catch (Exception ignored) {

        }
    }

    /*! \fn  public long getLastTimePlayed()
         \brief Returnează ultima dată când a fost redat sunetul.
    */
    public long getLastTimePlayed() {
        return lastTimePlayed;
    }

    /*! \fn  public void setLastTimePlayed(long lastTimePlayed)
         \brief Setează ultima dată când a fost redat sunetul.
         \param lastTimePlayed noua valoare.
    */
    public void setLastTimePlayed(long lastTimePlayed) {
        this.lastTimePlayed = lastTimePlayed;
    }
}
