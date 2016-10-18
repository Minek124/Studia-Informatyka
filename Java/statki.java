import java.util.Random;


class Statek{
	public int nr;
	public Statek(int x){
		nr=x;
	};
	public String toString(){
		String s=""+nr;
		return s;
	}
}

class Start {
	final static int SEA_SIZE=50;
	final static int SHIPS=10;
	public static Statek[] tab=new Statek[SEA_SIZE];
	public static void wypisz(Statek t[]){
		for(int j=0;j<SEA_SIZE;j++) 
			if((t[j])!=null) System.out.println(" Na pozycji "+j+" jest statek "+t[j].nr);
				
	}
	public static void main(String[] args){
		Random losuj=new Random();
		int i=SHIPS;
		while(i>0){
				int pole=losuj.nextInt(SEA_SIZE);
				if(tab[pole]==null){
					tab[pole]=new Statek(i);
					i--;
				}
		};
		
		wypisz(tab);
		
	}
}