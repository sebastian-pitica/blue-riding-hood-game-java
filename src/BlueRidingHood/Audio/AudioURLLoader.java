package BlueRidingHood.Audio;

import BlueRidingHood.Game.Enums.Sounds;

import java.net.URL;

/*! \class AudioURLLoader
    \brief Loader al adreselor fizice pentru sunet.

    Oferă metode pentru:\n
        -inițializarea adreselor fizice ale sunetelor.\n
        -preluarea adreselor.
 */
public class AudioURLLoader {
    static private final URL[] soundsURL = new URL[Sounds.values().length]; /*!< Adrese fizice pentru sunete*/

    /*! \fn public static void Init()
            \brief Inițializează vectorul cu adresele fizice pentru sunet.
     */
    public static void Init() {
        soundsURL[Sounds.AMBIENT.ordinal()] = AudioURLLoader.class.getResource("/sounds/ambient.wav");
        soundsURL[Sounds.PRUN.ordinal()] = AudioURLLoader.class.getResource("/sounds/running player.wav");
        soundsURL[Sounds.ERUN.ordinal()] = AudioURLLoader.class.getResource("/sounds/running entity.wav");
        soundsURL[Sounds.WOLFSHOWL.ordinal()] = AudioURLLoader.class.getResource("/sounds/wolfs.wav");
        soundsURL[Sounds.SWORDKILL.ordinal()] = AudioURLLoader.class.getResource("/sounds/sword kill.wav");
        soundsURL[Sounds.SWORDHIT.ordinal()] = AudioURLLoader.class.getResource("/sounds/sword hit.wav");
        soundsURL[Sounds.SWORDDRAW.ordinal()] = AudioURLLoader.class.getResource("/sounds/sword draw .wav");
        soundsURL[Sounds.CLICK.ordinal()] = AudioURLLoader.class.getResource("/sounds/click.wav");
        soundsURL[Sounds.FINALROUND.ordinal()] = AudioURLLoader.class.getResource("/sounds/final_round.wav");
        soundsURL[Sounds.GAMEOVER.ordinal()] = AudioURLLoader.class.getResource("/sounds/game_over.wav");
        soundsURL[Sounds.LOSTSOUND.ordinal()] = AudioURLLoader.class.getResource("/sounds/lostsound.wav");
        soundsURL[Sounds.WINSOUND.ordinal()] = AudioURLLoader.class.getResource("/sounds/winsound.wav");
        soundsURL[Sounds.YOUWIN.ordinal()] = AudioURLLoader.class.getResource("/sounds/you_win.wav");
        soundsURL[Sounds.LLSONG.ordinal()] = AudioURLLoader.class.getResource("/sounds/llsong.wav");
        soundsURL[Sounds.BITE1.ordinal()] = AudioURLLoader.class.getResource("/sounds/bite (1).wav");
        soundsURL[Sounds.BITE2.ordinal()] = AudioURLLoader.class.getResource("/sounds/bite (2).wav");
        soundsURL[Sounds.BITE3.ordinal()] = AudioURLLoader.class.getResource("/sounds/bite (3).wav");
        soundsURL[Sounds.BITE4.ordinal()] = AudioURLLoader.class.getResource("/sounds/bite (4).wav");
        soundsURL[Sounds.COIN.ordinal()] = AudioURLLoader.class.getResource("/sounds/coin.wav");
        soundsURL[Sounds.SWORDNOHIT.ordinal()] = AudioURLLoader.class.getResource("/sounds/sword sound.wav");
        soundsURL[Sounds.SHIELD.ordinal()] = AudioURLLoader.class.getResource("/sounds/shield.wav");
        soundsURL[Sounds.ON.ordinal()] = AudioURLLoader.class.getResource("/sounds/on.wav");
        soundsURL[Sounds.OFF.ordinal()] = AudioURLLoader.class.getResource("/sounds/off.wav");

    }

    /*! \fn public static URL getSoundsURL(int i)
            \brief Returnează adresa fizică a sunetului de index dat.
            \param i indexul sunetului.
     */
    public static URL getSoundsURL(int i) {
        return soundsURL[i];
    }
}
