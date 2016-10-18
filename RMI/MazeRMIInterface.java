import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejs RMI dla serwisu obslugujacego ruch pojazdow po plaszy-labiryncie.
 * @author oramus
 *
 */
public interface MazeRMIInterface extends Remote {

    /**
     * Ustawia plansze zawierajaca labirynt. Dostarczona plansza zawsze musi byc
     * kwadratowa. Pola zawierajace false traktowane sa jako
     * zablokowane, pola zawierajace true jako przejezdne.
     * Metoda musi byc wykonana przed pierwszym ruchem gracza.
     * Ustalona wartosc nie ulega zmianie.
     * @param board - referencja do dwuwymiarowej, kwadratowej tablicy
     * @throws RemoteException
     */
    void setBoard( boolean[][] board ) throws RemoteException;

    /**
     * Ustala dodatkowy czas spania (sleep) w milisekundach ustawiony dla
     * metod {@linkplain getFuel}, {@linkplain getPosition} oraz
     * {@linkplain go}. Wymionione powyzej metody maja zablokowac
     * watek, ktÃ³ry je wywoÅ‚aÅ‚ na ustalony czas.
     * Metoda musi byc wykonana przed pierwszym ruchem gracza.
     * Ustalona wartosc nie ulega zmianie.
     *
     * @param msec - liczba milisekund czasu spania
     * @throws RemoteException
     */
    void setSleepTime( long msec ) throws RemoteException;

    /**
     * Liczba pojazdow, ktorymi moze poslugiwac sie gracz. Ruch kazdego
     * z pojazdow moze byc zlecany i wykonywany niezaleznie. Metoda
     * musi zostac wykonana przed pierwszym ruchem gracza.
     * Metoda musi byc wykonana przed pierwszym ruchem gracza.
     * Ustalona wartosc nie ulega zmianie.
     *
     * @param vehicles - liczba pojazdow
     * @throws RemoteException
     */
    void setNumberOfVehicles( int vehicles ) throws RemoteException;

    /**
     * Ustala poczatkowa ilosc paliwa w baku kazdego z pojazdow.
     * Metoda musi byc wykonana przed pierwszym ruchem gracza.
     * Ustalona wartosc nie ulega zmianie.
     *
     * @param level - poczatkowy poziom paliwa
     * @throws RemoteException
     */
    void setFuel( int level ) throws RemoteException;

    /**
     * Zwraca ilosc paliwa dla wskazanego pojazdu. Operacja blokuje
     * watek, ktory ja wywolal. Operacje dla roznych pojazdow moga
     * byc wykonywane rownoczesnie. Operacje odczytu dla
     * blednych numerow pojazdu zwracaja Integer.MIN_VALUE.
     *
     * @param vehicleID - identyfikator pojazdu, dla ktorego
     * poziom paliwa ma zostac od 0 do liczbaPojazdow - 1
     * @return - poziom paliwa wskazanego pojazdu
     * @throws RemoteException
     */
    int getFuel( int vehicleID ) throws RemoteException;

    /**
     * Zwraca polozenie wskazanego pojazdu. Operacja odczytu
     * blokuje na zadany czas watek, ktory ja wywolal. Operacje
     * dla roznych pojazdow zlecane przez rozne watki
     * moga byc realizowane rownoczesnie.
     * W przypadku wskazania blednego numeru pojazdu zwraca null.
     * @param vehicleID - identyfikator pojazdu, dla
     * ktorego odczytywana jest pozycja. Poprawne wartosci od 0
     * do liczbaPojazdow - 1
     * @return - pozycja pojazdu o wskazanym identyfikatorze lub
     * null w przypadku uzycia blednego identyfikatora pojazdu.
     * @throws RemoteException
     */
    VehiclesInterface.Position getPosition( int vehicleID ) throws RemoteException;

    /**
     * O ile jest to mozliwe dokonuje zmiany polozenia pojazdu o wskazanym
     * identyfikatorze we wskazanym kierunku. Zmiana pojazdu jest
     * mozliwa, chyba, ze
     * <br> * identyfikator pojazdu jest bledny
     * <br> * w pojezdzie brak juz paliwa
     * <br> * ruch prowadzi do opuszczenia planszy
     * <br> * pozycja docelowa jest sciana
     * Metoda blokuje na zadany czas watek, ktory ja wywolal. Operacje
     * dla roznych pojazdÃ³w moga byc realizowane rownoczesnie, o ile
     * zlecane sa za pomoca roznych watkow.
     *
     * @param vehicleID - identyfikator pojazdu, ktory ma zostac przesuniety
     * do nowej pozycji
     * @param d - kierunek ruchu
     * @return - jesli wywolanie metody zmienilo pozycje pojazdu, to
     * zwracana jest prawda, w przeciwnym wypadku - falsz.
     * @throws RemoteException
     */
    boolean go( int vehicleID, VehiclesInterface.Direction d ) throws RemoteException;
}