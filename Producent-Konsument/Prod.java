import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

class Producer extends Thread {

	Scanner input;
	double av = 0.1d;
	double sigma = 0.01d;
	int size;
	Random rand = new Random();
	Queue<Integer> queue;
	boolean[] prodHasFinished;

	int getTime() {
		double time = (rand.nextGaussian() * sigma) + av;
		int timeInMs = (int) (time * 1000);
		//System.out.println("Producent czeka: " + timeInMs + "ms");
		if (timeInMs < 0) {
			return 0;
		}
		return timeInMs;
	}

	Producer(String input_file, int size, Queue<Integer> queue,
			boolean[] prodHasFinished) throws FileNotFoundException {
		input = new Scanner(new File(input_file));
		this.size = size;
		this.queue = queue;
		this.prodHasFinished = prodHasFinished;
	}

	@Override
	public void run() {
		while (input.hasNextInt()) {
			/*
			 * gdy kolejka jest pelna to czeka okreslony czas
			 */
			while (queue.size() == size) {
				try {
					Thread.sleep(getTime());
				} catch (InterruptedException e) {
				}
			}
			int element = input.nextInt();
			//System.out.println("Producent dodaje do kolejki:" + element);
			synchronized (queue) {
				queue.add(element);
			}
			/*
			 * po kazdym dodaniu producent odczekuje pewnien czas
			 */
			try {
				Thread.sleep(getTime());
			} catch (InterruptedException e1) {
			}
		}
		//System.out.println("Producent zakonczyl prace");
		prodHasFinished[0] = true;
		input.close();
	}
}

class Consumer extends Thread {

	int conId;
	double sigma;
	double av;
	Random rand = new Random();;
	int sum = 0;
	int n = 0;
	Queue<Integer> queue;
	boolean[] prodHasFinished;

	Consumer(int conId, double av, double sigma, Queue<Integer> queue,
			boolean[] prodHasFinished) {
		this.queue = queue;
		this.prodHasFinished = prodHasFinished;
		this.conId = conId;
		this.av = av;
		this.sigma = sigma;
	}

	double getAverage() {
		double ret = ((double) (sum) / n);
		return ret;
	}

	int getTime() {
		double time = (rand.nextGaussian() * sigma) + av;
		int timeInMs = (int) (time * 1000);
		//System.out.println("Konsument" + conId + " czeka: " + timeInMs + "ms");
		if (timeInMs < 0) {
			return 0;
		}
		return timeInMs;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (queue) {
				if (!queue.isEmpty()) {
					if (conId == 1) {
						if (queue.peek() <= 10) {
							//System.out.println("Konsument1 zdejmuje "
							//		+ queue.peek());
							sum += queue.peek();
							++n;
							queue.remove();
						}
					} else {
						if (queue.peek() >= 10) {
							//System.out.println("Konsument2 zdejmuje "
							//		+ queue.peek());
							sum += queue.peek();
							++n;
							queue.remove();
						}
					}

				} else {
					/*
					 * prawie zawsze konsumneci zastaja pusta kolejke kiedy
					 * producent jeszcze nie zakonczyl pracy dla tych danych
					 * czasowych jest to konieczne
					 */
					if (prodHasFinished[0]) {
						//System.out.println("Konsument zakonczyl prace");
						break;
					} else {
						//System.out.println("Konsument"+conId+" czeka na producenta");
					}
				}
			}
			/*
			 * po pobraniu czeka okreslony czas
			 */
			try {
				Thread.sleep(getTime());
			} catch (InterruptedException e) {
			}
		}
	}
}

class Prod {

	Queue<Integer> queue;
	boolean[] prodHasFinished;
	Producer prod;
	Consumer cons1;
	Consumer cons2;
	/*
	 * konstruktor przyjmujcy lokalizacje pliku 
	 * oraz wielkosc kolejki
	 */
	Prod(String input_file, int size) throws FileNotFoundException {
		queue = new ArrayDeque<Integer>();
		prodHasFinished = new boolean[1];
		prodHasFinished[0] = false;
		prod = new Producer(input_file, size, queue, prodHasFinished);
		cons1 = new Consumer(1, 0.15, 0.2, queue, prodHasFinished);
		cons2 = new Consumer(2, 0.12, 0.3, queue, prodHasFinished);

	}
	/*
	 * glowna metoda tej klasy
	 */
	double work() {
		prod.start();
		cons1.start();
		cons2.start();
		try {
			prod.join();
			cons1.join();
			cons2.join();
		} catch (InterruptedException e) {
		}
		double ret = cons2.getAverage() - cons1.getAverage();
		return ret;
	}

}
