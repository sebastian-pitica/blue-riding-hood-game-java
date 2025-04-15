package BlueRidingHood.Audio;


import BlueRidingHood.Game.Enums.Sounds;
import BlueRidingHood.State.Game.GameState;

import java.util.Random;

/*! \class AudioHandler
    \brief Handler pentru sunet.

    Oferă metode pentru:\n
        -redarea, în buclă dacă este necesar, a unor sunete de fundal/efecte sonor.\n
        -redarea sunetelor specifice unui anume moment din joc.\n
        -oprirea redării a unui sunet sau mai multe.\n
        -evitarea supra-redării unor sunete.

    \note Pentru a nu încărca codul cu preluare de referințe, metode, verificări, etc.\n
    Am ales ca fiecare clasă să preia și să apeleze metodele clasei AudioHandler, când sau dacă are nevoie.
    \note Implementează design patternul **Singleton**.
    \note Nu poate implementa observer pentru gamestate, bug în launching.

 */
public class AudioHandler {
    private static final long playPRUNSecondsLimit = 6;   /*!< Limita de timp pentru redarea a doua sunete consecutive de alergat.*/
    private static AudioHandler audioHandler = null;  /*!< Referință unică către handlerul audio.*/
    private static Sound[] sounds;  /*!< Toate sunetele disponibile.*/
    private static boolean swordNoHit = true;  /*!< Jucătorul a lovit o entitate sau nu.*/
    private final Random randomGenerator;  /*!< Generator de valori aleatoare pentru a reda unul din cele 4 sunete de mușcătură pentru entități.*/

    /*! \fn protected AudioHandler()
           \brief Constructorul clasei AudioHandler.

           Inițializează referințele locale.\n
           Inițializează vectorul de sunete.
    */
    protected AudioHandler() {
        AudioURLLoader.Init();
        sounds = new Sound[Sounds.values().length];
        randomGenerator = new Random();
        for (Sounds soundType : Sounds.values()) {
            sounds[soundType.ordinal()] = new Sound(soundType.ordinal());
        }
    }

    /*! \fn public static AudioHandler getAudioHandler()
            \brief Returnează referința unică a handlerului audio.
     */
    public static AudioHandler getAudioHandler() {

        if (audioHandler == null) {
            audioHandler = new AudioHandler();
        }

        return audioHandler;
    }

    /*! \fn public void playMusic(int index)
           \brief Pornește redarea în buclă a unui sunet de fundal.
            
           Dacă opțiunea de sunet a fost activată.\n
           Limitează încercările de redare multiplă a unui sunet, sau două sunete de fundal distincte, simultan.\n
           \param index indexul sunetului de redat.
    */
    public void playMusic(int index) {
        if (GameState.getPlaySound()) {
            stopOverPlay(index);
            excludeDoublePlay(index);
            sounds[index].play();
            sounds[index].loop();
        }
    }

    /*! \fn private void stopOverPlay(int indexSoundToPlay)
          \brief Limitează redarea simultană a două sunete de fundal distincte.
          \param indexSoundToPlay indexul sunetului de redat.
    */
    private void stopOverPlay(int indexSoundToPlay) {
        if (indexSoundToPlay == Sounds.AMBIENT.ordinal()) {
            sounds[Sounds.LLSONG.ordinal()].stop();
        } else {
            if (indexSoundToPlay == Sounds.LLSONG.ordinal())
                sounds[Sounds.AMBIENT.ordinal()].stop();
        }
    }

    /*! \fn  private void excludeDoublePlay(int indexSoundToPlay)
          \brief Limitează redarea multiplă simultană a aceluiași sunet de fundal.
          \param indexSoundToPlay indexul sunetului de redat.
    */
    private void excludeDoublePlay(int indexSoundToPlay) {
        sounds[indexSoundToPlay].stop();
    }

    public void stop(int index) {
        try {
            sounds[index].stop();
            sounds[index].setLastTimePlayed(0);
        } catch (Exception ignored) {
        }
    }

    /*! \fn  public void playSoundEffect(int index)
          \brief Pornește redarea unică a unui efect audio.

           Dacă opțiunea de sunet a fost activată.
          \note Efectele sonore de pornit/oprit nu sunt limitate de valoarea opțiunii de sunet.\n
          Rolul lor fiind tocmai de a face evidentă activarea/dezactivarea unor opțiuni.
          \param indexSoundToPlay indexul efectului de redat.
    */
    public void playSoundEffect(int index) {

        if (GameState.getPlaySound() || index == Sounds.ON.ordinal() || index == Sounds.OFF.ordinal()) {
            if (index != Sounds.PRUN.ordinal() && index != Sounds.ERUN.ordinal() && index != Sounds.SWORDNOHIT.ordinal() && index != Sounds.SHIELD.ordinal()) {
                sounds[index].play();
                sounds[index].setLastTimePlayed(System.nanoTime());
            }

            if (index == Sounds.PRUN.ordinal())
                if ((System.nanoTime() - sounds[index].getLastTimePlayed()) / playPRUNSecondsLimit >= 1000000000) {
                    sounds[index].play();
                    sounds[index].setLastTimePlayed(System.nanoTime());
                }

            if (index == Sounds.ERUN.ordinal()) {
                if ((System.nanoTime() - sounds[index].getLastTimePlayed()) >= 1000000000) {
                    sounds[index].play();
                    sounds[index].setLastTimePlayed(System.nanoTime());
                }
            }

            if (index == Sounds.SWORDNOHIT.ordinal() && swordNoHit) {
                if ((System.nanoTime() - sounds[index].getLastTimePlayed()) >= 500000000) {
                    sounds[index].play();
                    sounds[index].setLastTimePlayed(System.nanoTime());
                }
            }

            if (index == Sounds.SHIELD.ordinal()) {
                if ((System.nanoTime() - sounds[index].getLastTimePlayed()) >= 900000000) {
                    sounds[index].play();
                    sounds[index].setLastTimePlayed(System.nanoTime());
                }
            }
        }
    }

    /*! \fn  public void stopAll()
          \brief Oprește toate sunetele.
    */
    public void stopAll() {
        for (Sounds soundType : Sounds.values()) {
            sounds[soundType.ordinal()].stop();
        }
    }

    /*! \fn  public void setSwordNoHit(boolean swordNoHit)
          \brief Stează valoarea flag-ului swordNoHit.
          \param swordNoHit noua valoare.

   */
    public void setSwordNoHit(boolean swordNoHit) {
        AudioHandler.swordNoHit = swordNoHit;
    }

    /*! \fn  public void playRandomBite()
          \brief Redă aleatoriu un sunet de mușcătură.

   */
    public void playRandomBite() {
        int option = randomGenerator.nextInt(1, 4);
        switch (option) {
            case 1 -> audioHandler.playSoundEffect(Sounds.BITE1.ordinal());
            case 2 -> audioHandler.playSoundEffect(Sounds.BITE2.ordinal());
            case 3 -> audioHandler.playSoundEffect(Sounds.BITE3.ordinal());
            case 4 -> audioHandler.playSoundEffect(Sounds.BITE4.ordinal());
        }
    }


    /*! \fn  public static void playLostSounds()
          \brief Redă doar sunetele specifice înfrângerii.

    */

    public static void playLostSounds()
    {
        audioHandler.stopAll();
        audioHandler.playSoundEffect(Sounds.GAMEOVER.ordinal());
        audioHandler.playSoundEffect(Sounds.LOSTSOUND.ordinal());

    }

    /*! \fn  public static void playWinSounds()
          \brief Redă doar sunetele specifice victoriei.
   */
    public static void playWinSounds()
    {
        audioHandler.stopAll();
        audioHandler.playSoundEffect(Sounds.WINSOUND.ordinal());
        audioHandler.playSoundEffect(Sounds.YOUWIN.ordinal());
    }

    /*! \fn  public static void playLastLevelAudio()
          \brief Redă doar sunetele specifice ultimului nivel.

    */
    public static void playLastLevelAudio()
    {
        audioHandler.stopAll();
        audioHandler.playSoundEffect(Sounds.FINALROUND.ordinal());
        audioHandler.playMusic(Sounds.LLSONG.ordinal());
    }

    /*! \fn  public static void playSwordKill()
          \brief Redă sunetul de ucidere cu sabia al unei entități.
    */
    public static void playSwordKill()
    {
        audioHandler.stop(Sounds.SWORDHIT.ordinal());
        audioHandler.playSoundEffect(Sounds.SWORDKILL.ordinal());
    }


}
