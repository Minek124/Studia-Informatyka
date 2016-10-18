import java.util.Random;

class BitwaMorska extends BitwaMorskaAbstr {

    Random randomizer = new Random();
    int[][][] tab;
    int liczbaokretow;
    int[] rozmiar_po_id;
    int[] ilosc_po_id;
    boolean czy_moga_sie_stykac;
    int size1;
    KlasaOkretu[] mojaprywatnaflota;

    public boolean wyszukajTrase(int x, int y, int dlugosc, boolean mozliwosc_stykania, int kierunek) {
        if (kierunek == 1) {  //pion
            if (mozliwosc_stykania) {
                for (int k = 1; k <= dlugosc; k++) {
                    if (tab[x][y + k - 1][0] == 1) {
                        return false;
                    }

                }
            } else {
                for (int k = 0; k <= dlugosc + 1; k++) {
                    if (((y + k - 1) >= 0) && ((y + k - 1) < size1)) {
                        if (tab[x][y + k - 1][0] == 1) {
                            return false;
                        }
                        if (x != (size1 - 1)) {
                            if (tab[x + 1][y + k - 1][0] == 1) {
                                return false;
                            }
                        }
                        if (x != 0) {
                            if (tab[x - 1][y + k - 1][0] == 1) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else {
            if (mozliwosc_stykania) {
                for (int k = 1; k <= dlugosc; k++) {
                    if (tab[x + k - 1][y][0] == 1) {
                        return false;
                    }

                }
            } else {
                for (int k = 0; k <= dlugosc + 1; k++) {
                    if ((x + k - 1 >= 0) && ((x + k - 1) < size1)) {
                        if (tab[x + k - 1][y][0] == 1) {
                            return false;
                        }
                        if (y != (size1 - 1)) {
                            if (tab[x + k - 1][y + 1][0] == 1) {
                                return false;
                            }
                        }
                        if (y != 0) {
                            if (tab[x + k - 1][y - 1][0] == 1) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    public void setFlota(FlotaI f) {
        liczbaokretow = f.getLiczbaKlasOkretow();
        rozmiar_po_id = new int[liczbaokretow];
        ilosc_po_id = new int[liczbaokretow];
        mojaprywatnaflota = new KlasaOkretu[liczbaokretow];
        czy_moga_sie_stykac = f.czyMogaSieStykac();

        for (int i = 0; i <= liczbaokretow - 1; i++) {
            KlasaOkretu okret = f.getKlasaOkretu(i);
            rozmiar_po_id[i] = okret.rozmiar;
            mojaprywatnaflota[i] = okret;
        }

        for (int i = 0; i <= liczbaokretow - 1; i++) {
            ilosc_po_id[i] = f.liczbaJednostekDanejKlasy(i);
        }

    }


    public void ustawPlansze(int size) {
        size1 = size;
        int x = 0;
        int y = 0;
        tab = new int[size][size][4];
        for (int i = 0; i < size; i++) {      // zerowanie tablicy
            for (int j = 0; j < size; j++) {
                tab[i][j][0] = 0;
                tab[i][j][1] = 0;
            }
        }
        for (int i = 0; i < liczbaokretow; i++) {// po kazdej jednostce

            for (int j = 1; j <= ilosc_po_id[i]; j++) {  // losujemy tyle ile jest jednostek danej klasy
                if (randomizer.nextBoolean()) {   // stawiamy pionowo
                    boolean znaleziona = false;
                    while (!znaleziona) {
                        x = randomizer.nextInt(size);
                        y = randomizer.nextInt(size - rozmiar_po_id[i] + 1);
                        znaleziona = wyszukajTrase(x, y, rozmiar_po_id[i], czy_moga_sie_stykac, 1); //zwraca false jezeli nie znalazl miejsc dla danego x y
                    }
                    int licznik = 1;
                    for (int k = 1; k <= rozmiar_po_id[i]; k++) {
                        tab[x][y + k - 1][0] = 1;
                        tab[x][y + k - 1][1] = i;
                        tab[x][y + k - 1][2] = 1;
                        tab[x][y + k - 1][3] = licznik;
                        licznik++;

                    }
                } else {                                 // stawiamy poziomo
                    boolean znaleziona = false;
                    while (!znaleziona) {
                        x = randomizer.nextInt(size - rozmiar_po_id[i] + 1);
                        y = randomizer.nextInt(size);
                        znaleziona = wyszukajTrase(x, y, rozmiar_po_id[i], czy_moga_sie_stykac, 2); //zwraca false jezeli nie znalazl miejsc dla danego x y
                    }
                    int licznik = 1;
                    for (int k = 1; k <= rozmiar_po_id[i]; k++) {
                        tab[x + k - 1][y][0] = 1;
                        tab[x + k - 1][y][1] = i;
                        tab[x + k - 1][y][2] = 2;
                        tab[x + k - 1][y][3] = licznik;
                        licznik++;
                    }
                }
            }

        }
    }


    public int strzal(int x, int y) {
        if ((x >= size1) || (y >= size1) || x < 0 || y < 0) {
            return -1;
        }
        if (tab[x][y][0] == 1) {
            tab[x][y][0] = 2;
            if (tab[x][y][2] == 1) {   //poziom
                for (int i = 1; i <= rozmiar_po_id[tab[x][y][1]]; i++) {
                    int przesun = tab[x][y][3];
                    if (tab[x][y - przesun + i][0] == 1) {
                        return 1;
                    }
                }
                for (int i = 1; i <= rozmiar_po_id[tab[x][y][1]]; i++) {
                    int przesun = tab[x][y][3];
                    tab[x][y - przesun + i][0] =3;
                }
                
                return 2;
            }
             else { //pion
                for (int i = 1; i <= rozmiar_po_id[tab[x][y][1]]; i++) {
                    int przesun = tab[x][y][3];
                    if (tab[x - przesun + i][y][0] == 1) {
                        return 1;
                    }
                }
                for (int i = 1; i <= rozmiar_po_id[tab[x][y][1]]; i++) {
                    int przesun = tab[x][y][3];
                    tab[x - przesun + i][y][0] =3;
                }
                return 2;
            }
        }
         else {
            return 0;
        }
    }


    public KlasaOkretu coZatopiono(int x, int y) {
        if (tab[x][y][0] == 3) {
            return mojaprywatnaflota[tab[x][y][1]];
        }
        return null;
    }

    public void wypisz(boolean okr) {
        if (okr) {
            for (int i = 0; i < liczbaokretow; i++) {
                System.out.println(mojaprywatnaflota[i]);
            }
            System.out.println();
        }
        for (int j = 0; j < size1; j++) {

            for (int i = 0; i < size1; i++) {
                System.out.print(tab[i][j][0] + " ");
            }
            System.out.print("   ");
            for (int i = 0; i < size1; i++) {
                int x = tab[i][j][1];

                System.out.print(x + " ");
            }
            System.out.print("   ");
            for (int i = 0; i < size1; i++) {
                System.out.print(tab[i][j][2] + " ");
            }
            System.out.print("   ");
            for (int i = 0; i < size1; i++) {
                System.out.print(tab[i][j][3] + " ");
            }
            System.out.println();
        }


    }
}