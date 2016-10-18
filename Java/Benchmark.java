abstract class Benchmark {
// w tej metodzie ma zostac umieszczone wywolywanie funkcji matematycznych
    abstract void benchmark( double x );

// count - liczba wywolan metody benchmark
// mult - wspolczynik uzywany przy ustaleniu wartosci argumentu metody benchmark
// jesli count = 10000 a chcemy aby metoda benchmark wywolywana byla od 0 do 1 to
// mult = 1 / count

    public final long repeat( long count, double mult ) {
        long start = System.nanoTime();
        for ( long i = 0; i < count; i++){
            benchmark( mult * i );
        }
        return (System.nanoTime() - start);
    }
}

class cos extends Benchmark{
    @Override
    void benchmark( double x ){
        Math.cos(x);
    }
}
class sin extends Benchmark{
    @Override
    void benchmark( double x ){
        Math.sin(x);
    }
}
class exp extends Benchmark{
    @Override
    void benchmark( double x ){
        Math.exp(x);
    }
}
class sqrt extends Benchmark{
    @Override
    void benchmark( double x ){
        Math.sqrt(x);
    }
}
class cosh extends Benchmark{
    @Override
    void benchmark( double x ){
        Math.cosh(x);
    }
}

class Start {
    public static void main(String[] args){
        sin sin = new sin();
        cos cos = new cos();
        exp exp = new exp();
        sqrt sqrt = new sqrt();
        cosh cosh = new cosh();
        
        final long count = 10000; // COUNT
        long[] wynik=new long[5];
        double[] wynikwzgledny=new double[5];
        double[] sumawynikow=new double[5];
                
        for (double mult=0.0001;mult<=0.5;mult=mult+0.0001){
        
        System.out.println("testy dla count ="+count+" i mult = "+mult);
        wynik[0]=sin.repeat(count, mult);
        wynik[1]=cos.repeat(count, mult);
        wynik[2]=cosh.repeat(count, mult);
        wynik[3]=sqrt.repeat(count, mult);
        wynik[4]=exp.repeat(count, mult);
        
        double min=wynik[0];
        for (int i=1;i<=4;i++){   // najkrotszy czas
            if(min > wynik[i]){   
                min=wynik[i];
            }
        }
        for (int i=0;i<=4;i++){  // liczenie przejrzystego czasu
            wynikwzgledny[i]=(double)(wynik[i]/min);
            sumawynikow[i]=sumawynikow[i]+ wynikwzgledny[i];
        }
        for (int i=0;i<=4;i++){
        String funkcja;
            switch(i){
                case 0:
                    funkcja="sin";
                    break;
                case 1:
                    funkcja="cos";
                    break;
                case 2:
                    funkcja="cosh";
                    break;
                case 3:
                    funkcja="sqrt";
                    break;
                case 4:
                    funkcja="exp";
                    break;
                default:
                    funkcja="nieznana";
                    break;
                    
            
        }
            System.out.println("czas dla funkcji "+funkcja+" = "+wynik[i]+" czas wzgledny = "+wynikwzgledny[i]);
        }
        }
       
        System.out.println("\n");
        for (int i=0;i<=4;i++){
        String funkcja;
            switch(i){
                case 0:
                    funkcja="sin";
                    break;
                case 1:
                    funkcja="cos";
                    break;
                case 2:
                    funkcja="cosh";
                    break;
                case 3:
                    funkcja="sqrt";
                    break;
                case 4:
                    funkcja="exp";
                    break;
                default:
                    funkcja="nieznana";
                    break;
                    
            
        }
            System.out.println("suma czasow "+funkcja+" = "+sumawynikow[i]);
        }
        System.out.println("widac nasze wyniki najszybsza ta z najmniejsza liczba dla mult od 0.0001 do 5 co 0.0001");
        System.out.println("zawsze najszybsza jest metoda sqrt");
        System.out.println("dla malych mult 2 miejsce funkcje cos i sin, 3 miejsce exp, 4 miejsce cosh");
        System.out.println("dla duzych mult 2 miejsce funkcja exp, 3 miejsce funkcja cosh, 4 miejsce funkcje cos i sin");
    }
}