package rs.direktnoizbaste.dizb.app;

/**
 * Created by 1 on 1/15/2016.
 */
public class AppConfig {
    public static final String URL_CONFIG_SENSOR = "http://192.168.4.1/wifisave?s=%1$s&p=%2$s";
    // Server login url
    public static final String URL_LOGIN_POST = "http://direktnoizbaste.rs/parametri.php";
    //"action=povuciPodatkeAndroidKorisnik&email=pera@gjsd.com&p=miki";
    public static final String URL_LOGIN_GET = "http://direktnoizbaste.rs/parametri.php?action=povuciPodatkeAndroidKorisnik&tag=%1$s&email=%2$s&p=%3$s";
    // Server register url
    public static final String URL_REGISTER_POST = "http://direktnoizbaste.rs/parametri.php";
    //action=registrujAndroid&email=pera@gjsd.com&sifra=miki&komitentime=miki&komitentprezime=miki";
    public static final String URL_REGISTER_GET = "http://direktnoizbaste.rs/parametri.php?action=registrujAndroid&tag=%1$s&email=%2$s&sifra=%3$s&komitentime=%4$s&komitentprezime=%5$s";
    //http://direktnoizbaste.rs/parametri.php?action=povuciSenzorUid&id=1
    public static final String URL_SENSOR_LIST_GET = "http://direktnoizbaste.rs/parametri.php?action=povuciSenzorUid&id=%1$s";
    //http://direktnoizbaste.rs/parametri.php?action=povuciPodatkeSenzorId&string=5CCF7F747A7&id=1
    public static final String URL_GRAPHS_DATA_GET = "http://direktnoizbaste.rs/parametri.php?action=povuciPodatkeSenzorId&id=%1$s&string=%2$s";
    //http://direktnoizbaste.rs/parametri.php?action=obrisiSenzorId&string=pera&id=1
    public static final String URL_DEL_SENSOR_GET = "http://direktnoizbaste.rs/parametri.php?action=obrisiSenzorId&id=%1$s&string=%2$s";
    //http://direktnoizbaste.rs/parametri.php?action=dodajSenzorId&string=pera&id=1&br=4
    public static final String URL_ADD_SENSOR_GET = "http://direktnoizbaste.rs/parametri.php?action=dodajSenzorId&id=%1$s&string=%2$s&br=%3$s";
    //{"kulture":[{"IdKulture":1,"ImeKulture":"Boranija","SlikaKulture":null},{"IdKulture":2,"ImeKulture":"Paprika","SlikaKulture":null},{"IdKulture":3,"ImeKulture":"Paradajz","SlikaKulture":null}]}
    public static final String URL_SENSOR_PLANTS_GET = "http://direktnoizbaste.rs/parametri.php?action=podaciKulture";
    //http://direktnoizbaste.rs/parametri.php?action=izmeniPodatkeSenzorId&id=57&string=5ECF7F0747A7&br=3
    public static final String URL_UPDATE_SENSOR_GET = "http://direktnoizbaste.rs/parametri.php?action=izmeniPodatkeSenzorId&id=%1$s&string=%2$s&br=%3$s";
}
