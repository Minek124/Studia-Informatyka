
class TablicaNWymiarowa {
   
   public int size=10;
   public int n;
   public int tab[][]=new int[size+1][];
   public int ostatnia=1;
   public int ostatniapozycja;
   public boolean nieladne_rozwiazanie_problemu;
   public TablicaNWymiarowa( int N, int size1 ) {
       n=N;
       
   }
   
   public void rozszerz_tablice(){
       size=size*2;
       int tab_pomocnicza[][];
       tab_pomocnicza=tab;
       tab=new int[size][n+2];
       for(int i=1;i<=ostatnia-1;i++){
           for(int j=1;j<=n+1;j++){
               tab[i][j]=tab_pomocnicza[i][j];
           }
       }
   }
 
   public int get( int[] poz ) {
       boolean k=true;
       int wart=0;
       for(int i=1;((i<ostatnia) && k);i++){
           boolean h=true;
           for(int j=1;(j<=n) && h;j++){
               if(tab[i][j]==poz[j-1]) {
                   if(j==n) {
                       wart=tab[i][j+1];
                       ostatniapozycja=i;
                       k=false;
                       nieladne_rozwiazanie_problemu=true;
                   }
               }
               else{
                   h=false;
               }
                   
           }
       }
       return wart;
   }

  
   public void set( int[] poz, int wartosc ) {
       if(poz.length<n) {
           System.out.println("niepoprawne wspolrzedne, ma byc N wymiarow");
       }
       else{
            nieladne_rozwiazanie_problemu=false;
            if(get(poz)!=0 || nieladne_rozwiazanie_problemu) {
               tab[ostatniapozycja][n+1]=wartosc;
            }
            
       
               if(ostatnia>=size-1){
                   rozszerz_tablice();
                }
                 tab[ostatnia]=new int[n+2];
                 for(int i=1;i<=n;i++){
                     tab[ostatnia][i]=poz[i-1];
                  }
                  tab[ostatnia][n+1]=wartosc;
                  ostatnia++;
            }
       
   }
   
}

class Start {
    
    public static void main(String[] args){
        TablicaNWymiarowa tablet=new TablicaNWymiarowa(4,9999999);

        int p[]={1,888,234,666};
        int q[]={0,4,5,7};
        int w[]={0,4,5,4};
        int e[]={3,3,33,3};
        int p1[]={1,8838,234,666};
        int q1[]={30,4,5,7};
        int w1[]={0,34,5,43};
        int e1[]={3,3,33,3};
        int p2[]={1,888,234,6636};
        int q2[]={0,43,5,73};
        int w2[]={0,4,53,34};
        int e2[]={3,33,3,3};
        tablet.set(p, -32);
        tablet.set(q, 0);
        tablet.set(q, 33);
        tablet.set(p, 9);
        tablet.set(p1, -132);
        tablet.set(q1, 177);
        tablet.set(w1, 313);
        tablet.set(e1, 919);
        tablet.set(p2, -322);
        tablet.set(q2, 727);
        tablet.set(w2, 323);
        tablet.set(e2, 929);
        System.out.println(tablet.get(p));
        System.out.println(tablet.get(q));
        System.out.println(tablet.get(w));
        System.out.println(tablet.get(e));
        System.out.println(tablet.get(p1));
        System.out.println(tablet.get(q1));
        System.out.println(tablet.get(w1));
        System.out.println(tablet.get(e1));
        System.out.println(tablet.get(p2));
        System.out.println(tablet.get(q2));
        System.out.println(tablet.get(w2));
        System.out.println(tablet.get(e2));
    }
}
