import java.util.Random;

class Robot {


    public int go(int liczbaKrokow, double p) {
        return 0;
    }
}

class RobotZdecydowany extends Robot {
    public Random losuj = new Random();
    public int lokalizacja = 0;

    public boolean WylosujKierunek() {
        if (losuj.nextDouble() < 0.5) {
            return false; // czyli lewo
        } else {
            return true; // czyli prawo
        }
    }

    @Override
    public int go(int liczbaKrokow, double p) {
        lokalizacja = 0;
        if (WylosujKierunek()) {
            lokalizacja =lokalizacja + liczbaKrokow;
            return lokalizacja;
        } else {
            lokalizacja =lokalizacja - liczbaKrokow;
            return lokalizacja;
        }


    }
}

class RobotNiezdecydowany extends Robot {
    public Random losuj = new Random();
    public int lokalizacja = 0;

    public boolean WylosujKierunek() {
        if (losuj.nextDouble() < 0.5) {
            return false; // czyli lewo
        } else {
            return true; // czyli prawo
        }
    }

    boolean prawo; // true = kierunek prawo , false= kierunek lewo

    @Override
    public int go(int liczbaKrokow, double p) {
        lokalizacja = 0;
        if (WylosujKierunek()) {
            lokalizacja++;
            prawo = true;
        } else {
            lokalizacja--;
            prawo = false;
        }
        for (int i = 2; i <= liczbaKrokow; i++) {
            if (losuj.nextDouble() <= p) {
                if (WylosujKierunek()) {
                    lokalizacja++;
                    prawo = true;
                } else {
                    lokalizacja--;
                    prawo = false;
                }
            } else {
                if (prawo) {
                    lokalizacja++;
                } else {
                    lokalizacja--;
                }
            }
        }
        return lokalizacja;
    }
}

class RobotZPamieciaKierunku extends Robot {
    public Random losuj = new Random();
    public int lokalizacja = 0;

    public boolean WylosujKierunek() {
        if (losuj.nextDouble() < 0.5) {
            return false; // czyli lewo
        } else {
            return true; // czyli prawo
        }
    }

    boolean prawo; // true = kierunek prawo , false= kierunek lewo

    @Override
    public int go(int liczbaKrokow, double p) {
        lokalizacja = 0;
        if (WylosujKierunek()) {
            lokalizacja++;
            prawo = true;
        } else {
            lokalizacja--;
            prawo = false;
        }
        for (int i = 2; i <= liczbaKrokow; i++) {
            if (losuj.nextDouble() <= p) {
                if (WylosujKierunek()) {
                    if (prawo) {
                        lokalizacja++;
                        prawo = true; // nie ma znaczenia i tak jest true
                    } 
                    else {
                        if (losuj.nextDouble() <= p) {
                            lokalizacja++;
                            prawo = true;
                        }
                        else {
                            lokalizacja--;
                            prawo=false;  // nie ma znaczenia bo jest false
                        }
                    }
                } else {
                    if (prawo) {
                        if (losuj.nextDouble() <= p) {
                            lokalizacja--;
                            prawo = false;
                        }
                        else{
                            lokalizacja++;
                            prawo=true; // nie ma znaczenia bo jest true
                        }
                    } 
                    else {
                        lokalizacja--;
                        prawo = false; // nie ma znaczenia i tak jest false
                    }
                }
            } else {
                if (prawo) {
                    lokalizacja++;
                } else {
                    lokalizacja--;
                }
            }
        }
        return lokalizacja;
    }
}

class RobotZwariowany extends Robot {
    public Random losuj = new Random();
    public int lokalizacja = 0;

    public boolean WylosujKierunek() {
        if (losuj.nextDouble() < 0.5) {
            return false; // czyli lewo
        } else {
            return true; // czyli prawo
        }
    }

    boolean prawo;
    double prawdopodobienstwo;

    @Override
    public int go(int liczbaKrokow, double p) {
        prawdopodobienstwo=p;
        lokalizacja = 0;
        if (WylosujKierunek()) {
            lokalizacja++;
            prawo = true;
        } else {
            lokalizacja--;
            prawo = false;
        }
        for (int i = 2; i <= liczbaKrokow; i++) {
            if (losuj.nextDouble() <= prawdopodobienstwo) {
                if (WylosujKierunek()) {
                    lokalizacja++;
                    prawo = true;
                } else {
                    lokalizacja--;
                    prawo = false;
                }
            }
             else {
                if (prawo) {
                    lokalizacja++;
                } else {
                    lokalizacja--;
                }
            }
        prawdopodobienstwo=prawdopodobienstwo*p;
        }
        return lokalizacja;
    }
}

class Start {

    static public final double pp=0.3;
    static public final int kroki=1000;
    
    public static void main(String[] args) {
        
        Robot RobotZdecydowany=new RobotZdecydowany();
        Robot RobotNiezdecydowany=new RobotNiezdecydowany();
        Robot RobotZPamieciaKierunku=new RobotZPamieciaKierunku();
        Robot RobotZwariowany=new RobotZwariowany();
        int s1=0;
        int s2=0;
        int s3=0;
        int s4=0;
        for(int i=1;i<=1000;i++){
            s1=s1+Math.abs(RobotZdecydowany.go(kroki,pp));
            s2=s2+Math.abs(RobotNiezdecydowany.go(kroki,pp));
            s3=s3+Math.abs(RobotZPamieciaKierunku.go(kroki,pp));
            s4=s4+Math.abs(RobotZwariowany.go(kroki,pp));
        }
        s1=s1/1000;
        s2=s2/1000;
        s3=s3/1000;
        s4=s4/1000;
        System.out.println("liczba krokow: "+kroki+" prawdopobobienstwo: "+pp);
        System.out.println("robot zdecydowany pozycja koncowa: "+ RobotZdecydowany.go(kroki,pp) + ", srednie oddalenie od pkt 0: " +s1);
        System.out.println("robot niezdecydowany pozycja koncowa: "+ RobotNiezdecydowany.go(kroki,pp) + ", srednie oddalenie od pkt 0: " +s2);
        System.out.println("robot z pamiecia pozycja koncowa: "+ RobotZPamieciaKierunku.go(kroki,pp) + ", srednie oddalenie od pkt 0: " +s3);
        System.out.println("robot zwariowwany pozycja koncowa: "+ RobotZwariowany.go(kroki,pp) + ", srednie oddalenie od pkt 0: " +s4);
    }
}
