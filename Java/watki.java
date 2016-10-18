
import java.util.Random;

class Gra {

    private boolean[] morze;
    private int ilosc;
    public int rozmiar;
    private Random losuj = new Random();

    Gra(int wielkosc, int ile) {
        ilosc = ile;
        rozmiar = wielkosc;
        morze = new boolean[wielkosc];

        int i = ilosc;
        while (i > 0) {
            int pole = losuj.nextInt(wielkosc);
            if (!morze[pole]) {
                morze[pole] = true;
                i--;
            }
        }
    }

    boolean czyKoniec() {
        int suma = 0;
        for (int i = 0; i < morze.length; i++) {
            if (morze[i]) {
                suma++;
            }
        }
        if (suma == 0) {
            return true;
        }
        return false;
    }

    synchronized boolean strzal(int x) {
        if (morze[x]) {
            morze[x] = false;
            return true;
        }
        return false;
    }
}

class Gracz implements Runnable {

    private Gra plansza;
    private int suma = 0;
    private String name;
    private int rozmiar;
    private Random losuj;

    Gracz(Gra g, String imie) {
        this.plansza = g;
        this.name = imie;
        rozmiar = plansza.rozmiar;
        losuj = new Random(); 
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            int x = losuj.nextInt(rozmiar);
            if (plansza.strzal(x)) {
                suma++;
            }
        }
    }

    @Override
    public String toString() {

        String s = "Jestem " + name + " i zeszczelilem " + suma + " statkow!";
        return s;
    }

    public int wynik() {
        return suma;
    }
}

class Start {

    static final int SIZE = 500000;
    static final int SHIPS = 100;

    public static void main(String[] argv) {
        Gra gra = new Gra(SIZE, SHIPS);

        Gracz gracz1 = new Gracz(gra, "Gracz1");
        Gracz gracz2 = new Gracz(gra, "Gracz2");
        Gracz gracz3 = new Gracz(gra, "Gracz3");
        Gracz gracz4 = new Gracz(gra, "Gracz4");

        Thread watekGracza1 = new Thread(gracz1);
        Thread watekGracza2 = new Thread(gracz2);
        Thread watekGracza3 = new Thread(gracz3);
        Thread watekGracza4 = new Thread(gracz4);

        watekGracza1.start();
        watekGracza2.start();
        watekGracza3.start();
        watekGracza4.start();

        System.out.println("Gra Rozpoczela sie!");

        while (!gra.czyKoniec()) {
        }

        watekGracza1.interrupt();
        watekGracza2.interrupt();
        watekGracza3.interrupt();
        watekGracza4.interrupt();

        System.out.println(gracz1);
        System.out.println(gracz2);
        System.out.println(gracz3);
        System.out.println(gracz4);

        int sprawdzenie = gracz1.wynik() + gracz2.wynik() + gracz3.wynik() + gracz4.wynik();
        System.out.println("razem zeszczelilismy az " + sprawdzenie + " statkow!");
    }
}
