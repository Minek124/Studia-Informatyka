class Main {

	public static void main(String[] args) throws Exception {

		String input_file = args[0];
		int size = Integer.parseInt(args[1]);
		Prod prod = new Prod(input_file, size);
		double wynik = prod.work();
		wynik = Math.round(wynik * 1000) / 1000.0d;
		System.out.println("Wynik : " + wynik);
	}
}
