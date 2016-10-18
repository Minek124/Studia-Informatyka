
class Main {
	
	public static void main(String[] args) throws Exception{
		
		double a = 0;
		for(int i = 0 ; i<args.length ; i++){// wczytywanie parametru zewnetrznego <a>
			try{
				a = Double.parseDouble(args[i]);
				break;  // pobieram pierwsza lepsze liczba ze strumienia, smieci odrzucam
			}
			catch(Exception e){

			}
		}
		Chain chain=new Chain(a);
		double wynik=chain.work();
		wynik = Math.round (wynik*100000) / 100000.0d;
		System.out.println("Wartosc calki : "+ wynik);
	}
}
