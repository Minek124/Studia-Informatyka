enum Szachy implements ZasadyGry {

    GONIEC(1),
    KROL(2),
    SKOCZEK(3),
    HETMAN(4);
    int nr;

    Szachy(int numer) {
        this.nr = numer;
    }

    @Override
    public boolean ruchPoprawny(int xp, int yp, int xk, int yk) {

        if (xp < 0 || yp < 0 || xk < 0 || yk < 0 || xp > 7 || yp > 7 || xk > 7 || yk > 7) {
            return false;
        }
        if (xp==xk && yp==yk) {
            return false;
        }
        int k1, k2;
        switch (nr) {
            case 1: //goniec
                k1 = Math.abs(xk - xp);
                k2 = Math.abs(yk - yp);
                if (k1 == k2) {
                    return true;
                }
                return false;
            case 2://krol
                k1 = Math.abs(xk - xp);
                k2 = Math.abs(yk - yp);
                if (k1 <= 1 && k2 <= 1) {
                    return true;
                }
                return false;
            case 3: //SKOCZEK
                k1 = Math.abs(xk - xp);
                k2 = Math.abs(yk - yp);
                if (k1 > 2 || k2 > 2) {
                    return false;
                }
                if (k1 == k2) {
                    return false;
                }
                if (k1 == 0 || k2 == 0) {
                    return false;
                }
                return true;
            case 4: //HETMAN
                k1 = Math.abs(xk - xp);
                k2 = Math.abs(yk - yp);
                if (k1 == k2) {
                    return true;
                }
                if (k1 == 0 || k2 == 0) {
                    return true;
                }
                return false;
            default:
                return false;
        }

    }
}