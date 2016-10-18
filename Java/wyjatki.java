class MojWyjatek extends Exception{
    
    public MojWyjatek(String message) {
        super(message);
    }

}
class TablicaNWymiarowa {

    public int size = 10;
    public int deklarowana_wielkosc;
    public static int n;
    public int tab[][] = new int[size + 1][];
    public int ostatnia = 1;
    public int ostatniapozycja;
    public boolean nieladne_rozwiazanie_problemu;
    
    public TablicaNWymiarowa(int N, int size1) {
        n = N;
        deklarowana_wielkosc=size1;
    }

    public void rozszerz_tablice() {
        size = size * 2;
        int tab_pomocnicza[][];
        tab_pomocnicza = tab;
        tab = new int[size][n + 2];
        for (int i = 1; i <= ostatnia - 1; i++) {
            for (int j = 1; j <= n + 1; j++) {
                tab[i][j] = tab_pomocnicza[i][j];
            }
        }
    }

    public int get(int[] poz) throws MojWyjatek {
        int wart = 0;
        if (poz.length != n ) {
            if (poz.length > n) {
                throw new MojWyjatek("funkcja get(),niepoprawny rozmiar, za duza tablica, nie zwroce tej wartosci");
            } else {
                throw new MojWyjatek("funkcja get(),niepoprawny rozmiar, za mala tablica, nie zwroce tej wartosci");
            }
        } else {
            boolean k = true;
            for (int i = 1; ((i < ostatnia) && k); i++) {
                boolean h = true;
                for (int j = 1; (j <= n) && h; j++) {
                    if (tab[i][j] == poz[j - 1]) {
                        if (j == n) {
                            wart = tab[i][j + 1];
                            ostatniapozycja = i;
                            k = false;
                            nieladne_rozwiazanie_problemu = true;
                        }
                    } else {
                        h = false;
                    }

                }
            }
        }
        return wart;

    }

    public void set(int[] poz, int wartosc) throws MojWyjatek {
        for(int i=0;i<poz.length;i++){
            if(poz[i]>deklarowana_wielkosc){
                throw new MojWyjatek("Podana pozycja nie jest zgodna z rozmiarem tablicy");
            }
        }
        if (poz.length != n) {
            if (poz.length > n) {
                throw new MojWyjatek("funkcja set(),niepoprawny rozmiar, za duza tablica, nie ustawie tej wartosci,");
            } else {
                throw new MojWyjatek("funkcja set(),niepoprawny rozmiar, za mala tablica, nie ustawie tej wartosci");
            }
        } else {
            nieladne_rozwiazanie_problemu = false;
            if (get(poz) != 0 || nieladne_rozwiazanie_problemu) {
                tab[ostatniapozycja][n + 1] = wartosc;
            }


            if (ostatnia >= size - 1) {
                rozszerz_tablice();
            }
            tab[ostatnia] = new int[n + 2];
            for (int i = 1; i <= n; i++) {
                tab[ostatnia][i] = poz[i - 1];
            }
            tab[ostatnia][n + 1] = wartosc;
            ostatnia++;
        }

    }
}

class Start {

    public static void main(String[] args) {
        TablicaNWymiarowa tablet = new TablicaNWymiarowa(4, 9999999);
        int p[] = {1, 888, 234, 100000000};  // za duza wspolrzedna
        int q[] = {0, 4, 5, 6}; // ok
        int w[] = {0, 4, 5, 4, 5};  // za duza
        int e[] = {3, 3, 33}; // za mala
        try {
            tablet.set(p, -32); // wyjatek
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
        try {
            tablet.set(q, 66); 
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
        try {
            tablet.set(w, 33);   // wyjatek
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
        try {
            tablet.set(e, 9);   // wyjatek
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
        try {
            System.out.println(tablet.get(p));
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
        try {
            System.out.println(tablet.get(q));
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
        try {
            System.out.println(tablet.get(w)); // wyjatek
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
        try {
            System.out.println(tablet.get(e)); // wyjatek
        } catch (MojWyjatek ex) {
            System.out.println(ex);
        }
    }
}