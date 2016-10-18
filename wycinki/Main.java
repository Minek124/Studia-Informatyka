import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


class Main {
	
	private static final boolean DEBUG = false;  // stala do testowania kodu true - wydruki kontrolne
	
	public static void main(String[] args) throws Exception{
		
		if (args.length !=2) {
            System.out.println("niepoprawne parametry");
            return;
        }
		
		ArrayList<Point> rect=new ArrayList<Point>();  // tablica prostokatow
		int maxXY=Integer.parseInt(args[1]);  // wymiar tafli
		
		if(DEBUG){  //wydruk kontrolny
			System.out.println("wymiary tafli: "+maxXY+" x "+maxXY);
		}
		
		Scanner input = new Scanner(new File(args[0]));
		int j=0;
		while (input.hasNextInt()) { // czytanie wymiarow prostokatow
			int x=input.nextInt();
			if(!input.hasNextInt())
				break;
			int y=input.nextInt();
			rect.add(new Point(x, y));
				if(DEBUG){  //wydruk kontrolny
					System.out.println("prostokat o wymiarach "+rect.get(j).x+" x "+rect.get(j).y);
				}
				j++;
			
		}
		input.close();
		
		Knapsack knapsack=new Knapsack(maxXY,rect);
		
		int odpadki=knapsack.pack();
		System.out.println("Powierzchnia odpadu : "+odpadki);
	}
}
