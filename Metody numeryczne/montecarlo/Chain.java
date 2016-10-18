
class Chain {
	
	int divNumber = 1000; // ilosc podzialow na prostokaty
	double maxIt = 100000; // ilosc losowan
	double a;  // parametr zewnetrzny
	
	Chain(double granica){
		a = Math.abs(granica);  // inicjalizacja
	}
	/*
	 * metoda liczaca calke a * cosh(x/a) 
	 * z przedzialu [start, end]
	 */
	double monteCarlo(double start, double end){
		int count = 0;
		double min = a * Math.cosh((start / a));  // minimalna wartosc funkcji w danym przedziale
		double max = a * Math.cosh((end / a));	// maxymalna wartosc funkcji w danym przedziale
		double width = end - start;	// szerokosc przedzialu
		double height = max - min;	// wysokosc przedzialu
		for (int i=0 ; i< maxIt ; i++){
			double x = Math.random()* width + start;
			double y = Math.random()* height + min;
			if(y < (a * Math.cosh((x/a)))){ // czy znajduje sie pod wykresem
				count ++;
			}
		}
		double ratio =((double)(count) / maxIt);   //stosunek pola pod wykresem do pola przedzialu
		double add = (min * width); // wartosc pod przedzialem
		return ((ratio * width * height) + add);
	}

	double work(){
		double sum = 0 ;
		double width = a / divNumber; // szerokosc przedzialu
		for (int i = 0; i < divNumber ; i++){
			sum += monteCarlo(i * width, (i+1) * width);
		}
		return sum;
	}
	
}
