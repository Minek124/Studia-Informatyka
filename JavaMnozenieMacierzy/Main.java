import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class Main {

	public static void main(String[] args) throws Exception {

		int n = Integer.parseInt(args[1]);
		double[][] matrix = new double[n][n];
		double[] vector = new double[n];
		Connection conn = DriverManager.getConnection(args[0]);
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("SELECT i,x FROM Xtable");
		int i = 0;
		while (rs.next()) {
			// ilosc elementow w tym wektorze nie moze byc wieksza niz n
			vector[i] = rs.getDouble("x");
			i++;
		}
		rs = statement.executeQuery("SELECT i,j,a FROM Atable");
		while (rs.next()) {
			/*
			 * zakladam ze i oraz j, oznaczajace wiersz i kolumne zaczynaja sie
			 * od 1 za koncza sie na (n)
			 */
			matrix[rs.getInt("i") - 1][rs.getInt("j") - 1] = rs.getDouble("a");
		}
		Multiply multiply = new Multiply(matrix, vector);
		double wynik = multiply.work();
		wynik = Math.round(wynik * 1000) / 1000.0d;
		System.out.println("Wynik : " + wynik);
	}
}
