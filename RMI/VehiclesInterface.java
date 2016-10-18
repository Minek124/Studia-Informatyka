import java.io.Serializable;


public interface VehiclesInterface {

    /**
     * Typ wyliczeniowy reprezentujacy dozwolone kierunki ruchu
     * @author oramus
     *
     */
    public enum Direction implements Serializable {
        EAST, WEST, SOUTH, NORTH,;
    }

    /**
     * Klasa reprezentujaca polozenie na dwu-wymiarowej planszy do gry.
     * @author oramus
     *
     */
    public class Position implements Serializable {
        private static final long serialVersionUID = -595306863880643313L;
        private int x,y;

        public Position( int x, int y ) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String toString(){
            return "(" + x + "," + y + ")";
        }
    }

    /**
     * Zleca ruch wskazanego pojazdu w zadanym kierunku.
     * @param vehicleID - numer pojazdu
     * @param d - kierunek ruchu
     * @return true - gdy ruch zostal wykonany,
     * false - gdy we wzkazanym kierunku pojazd nie moze sie poruszyc, lub gdy brak paliwa
     */
    boolean go( int vehicleID, Direction d );

    /**
     * Zwraca ilosc paliwa w pojezdzie
     * @param vehicleID - numer pojazdu
     * @return ilosc jednostek paliwa
     */
    int getFuelLevel( int vehicleID );

    /**
     * Zwraca polozenie wskazanego pojazdu
     * @param vehicleID - numer pojazdu
     * @return polozenie wskazaniego pojazdy
     */
    Position getPosition( int vehicleID );
}