import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

class Knapsack {
	
	private final boolean DEBUG = false;  // stala do testowania kodu: true - wydruki kontrolne
	private int maxXY;  // bok kwadratu
	private ArrayList<Point> rect;   // prostokaty ze wejscia  x,y - dlugosci boku 
	private ArrayList<Point> rectSorted;  //prostokaty posortowane powierzchnia
	
	/*
	 * konstruktor z dwoma parametrami
	 * Knapsack( rozmiar tafli, lista elementow)
	 */
	Knapsack(int maxXY_o,ArrayList<Point> rect_o){
		//inicjalizacja 
		maxXY=maxXY_o;  
		rect=rect_o;    
		rectSorted=new ArrayList<Point>();
		Iterator<Point> i=rect.iterator();
		while(i.hasNext()){ // sortowanie prostokatow od najwiekszej powierzchni
			Point tmpRect=i.next();
			Iterator<Point> it=rectSorted.iterator();
			int j=0;
			boolean wpisano=false;
			while(it.hasNext()){
				Point tmp=it.next();
				if((tmp.x*tmp.y)<tmpRect.x*tmpRect.y){
					rectSorted.add(j, tmpRect);
					wpisano=true;
					break;
				}
				j++;
			}
			if(!wpisano){
				rectSorted.add(j, tmpRect);
			}
		}
		Iterator<Point> it=rectSorted.iterator();
		if(DEBUG){
			while(it.hasNext()){  //wydruk kontrolny
				System.out.println(it.next());
			}
		}
	}
	/*
	 * rekurencja wstawia prostokat w obszar ograniczony dwoma punktami (x1,y1) oraz (x2,y2)
	 * powstaja dwie wolne przestrzenie (obszar nad wstawionym prostokatem oraz obok niego)
	 * wykonuje funkcje rekurencja dla tych dwoch obszarow
	 */
	int rekurencja(int x1,int y1,int x2,int y2){
		Iterator<Point> it=rectSorted.iterator();
		while(it.hasNext()){
			Point tmp=it.next();	
			int px=tmp.x;  // dlugosc boku prostokata x
			int py=tmp.y;  // dlugosc boku prostokata y
			int px_rev=tmp.y;  // dlugosc boku prostokata odwroconego o 90 stopni
			int py_rev=tmp.x;  // dlugosc boku prostokata odwroconego o 90 stopni
			if(px<=(x2-x1) && py<=(y2-y1)){  // sprawdzanie czy prostokat sie miesci
				//wycinanie prostokatu o wymiarach px x py
				if(DEBUG){  // wydruk kontrolny
					System.out.println("wycinam prostokat: "+px+" x "+py+" okreslony przez punkty: ("+x1+","+y1+") , ("+ (x1+px)+","+(y1+py)+")" );
				}
				it.remove();  // usuwanie wycietego prostokatu z listy				
				int ret2=rekurencja(x1+px,y1,x2,y1+py); // odpadek z drugiego obszaru
				int ret1=rekurencja(x1,y1+py,x2,y2);  // odpadek z pierwszego obszaru
				return (ret1+ret2);
			}else
				if(px_rev<=(x2-x1) && py_rev<=(y2-y1)){ // sprawdzanie czy odwrocony prostokat sie miesci
					//wycinanie prostokatu o wymiarach px x py w pozycji odwroconej o 90 stopni
					if(DEBUG){  //wydruk kontrolny
						System.out.println("wycinam prostokat: "+px_rev+" x "+py_rev+" okreslony przez punkty: ("+x1+","+y1+") , ("+ (x1+px_rev)+","+(y1+py_rev)+")");
					}
					it.remove(); // usuwanie wycietego prostokatu z listy					
					int ret2=rekurencja(x1+px_rev,y1,x2,y1+py_rev);// odpadek z drugiego obszaru
					int ret1=rekurencja(x1,y1+py_rev,x2,y2);  // odpadek z pierwszego obszaru
					return (ret1+ret2);
				}
			
		}
		return ((x2-x1)*(y2-y1));  //odpadek, w ktory juz nic nie zmieszcze
	}
	/*
	 * metoda wycinajaca emelementy i zwracajaca ilosc odpadkow
	 */
	int pack(){
		return rekurencja(0,0,maxXY,maxXY);
	}
	
}
