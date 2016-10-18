class Multiply {

	/*
	 * mnozy x-ty wiersz macierzy przez wektor
	 */
	class Worker implements Runnable {
		int x;

		Worker(int x) {
			this.x = x;
		}

		@Override
		public void run() {
			double sum = 0;
			for (int i = 0; i < size; i++) {
				sum += matrix[x][i] * vector[i];
			}
			output[x] = sum;
		}
	}

	double[][] matrix; // macierz z inputu
	double[] vector; // wektor z inputu
	double[] output; // wektor wynikowy po wymnozeniu
	int size; // dlugosc wektora

	/*
	 * konstruktor przyjmujacy macierz , oraz wektor
	 */
	Multiply(double[][] matrix, double[] vector) {
		// inicjalizacja
		this.size = vector.length;
		this.matrix = matrix;
		this.vector = vector;
		this.output = new double[size];
	}

	/*
	 * glowna metoda tej klasy, macierz i wektor, a wynik zapisuje do tablicy
	 * output zwraca maksymalna wartosc wektora output
	 */
	double work() {
		/*
		 * przechodzi przez kazdy wiersz macierzy i liczy skalar tego wiersza z
		 * wektorem
		 */
		for (int i = 0; i < (size - 1); i++) {
			new Thread(new Worker(i)).start();
		}

		/*
		 * ten watek zakonczy sie jako ostatni wiec kiedy zakonczy dzialanie,
		 * bede wiedzial ze wektor wynikowy jest jest kompletny
		 */
		Thread lastOne = new Thread(new Worker(size - 1));
		lastOne.start();
		try {
			lastOne.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// szukanie najmniejszego elementu w wektorze wynikowym
		double max = output[0];
		for (int i = 1; i < size; i++) {
			if (output[i] > max)
				max = output[i];
		}
		return max;
	}

}
