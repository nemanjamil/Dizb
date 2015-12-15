package rs.direktnoizbaste.dizb.app;

/**
 * Created by milan on 12/12/2015.
 */
public class AppConfig {
    // Server login url
    public static String URL_LOGIN_POST = "http://direktnoizbaste.rs/parametri.php";
            //"action=povuciPodatkeAndroidKorisnik&email=pera@gjsd.com&p=miki";

    public static String URL_LOGIN_GET = "http://direktnoizbaste.rs/parametri.php?action=povuciPodatkeAndroidKorisnik&tag=%1$s&email=%2$s&password=%3$s";
    // Server register url
    public static String URL_REGISTER_POST = "http://direktnoizbaste.rs/parametri.php";
            //action=registrujAndroid&email=pera@gjsd.com&sifra=miki&komitentime=miki&komitentprezime=miki";
    public static String URL_REGISTER_GET = "http://direktnoizbaste.rs/parametri.php?action=registrujAndroid&tag=%1$s&email=%2$s&password=%3$s&komitentime=%4$s&komitentprezime=%5$s";

}
