package pdelam.galgSalesman;

/* *
 * @author pdelam01
 */
import java.util.ArrayList;
import java.util.Collections;

public class Planisferio {
	static int ciudadesSize = MainGA.ciudades.size();
	static int[][] matrix = new int[ciudadesSize][ciudadesSize];
	static ArrayList<Integer> ints = new ArrayList<>();
	final int ALTO = 9999;

	/**
	 * Constructor de la clase.
	 * Creamos en mapa aleatorio (matriz) de caminos.
	 */
	public Planisferio() {
	    	for (int i = 0; i < ciudadesSize; i++) {
				for (int j = i; j < ciudadesSize; j++) {
					if (i == j) {
						matrix[i][j]=0;
					}
					if((int) (Math.random() * 100 + 1) <= 90) {
						matrix[i][j] = (int) (Math.random() * 100 + 1);
					}else {
						matrix[i][j]=0;
					}
				}
			}

	    	for (int j = 0; j < ciudadesSize; j++) {
				for (int i = j; i < ciudadesSize; i++) {
					if (i == j) {
						matrix[i][j]=0;
					}
					matrix[i][j] = matrix[j][i];
				}
			}
	    }

	/**
	 * Método calcula la longitud del camino (cromosoma) pasado.
	 *
	 * @param camino - camino (cromosoma) a calcular longitud total
	 * @return totalLong - longitud total del camino
	 */
	public int longitudViaje(String[] camino) {
		int totalLong = 0;
		int ciudad1, ciudad2;

		for (int i = 0; i < camino.length; i++) {
			if (i + 1 != camino.length) {
				ciudad1 = MainGA.ciudades.indexOf(camino[i]);
				ciudad2 = MainGA.ciudades.indexOf(camino[i + 1]);
				if (matrix[ciudad1][ciudad2] != 0) {
					totalLong += matrix[ciudad1][ciudad2];
				} else {
					totalLong += ALTO;
				}
			}
		}
		ints.add(totalLong);
		return totalLong;
	}

	public int longitudMaxMapa(){
		int maxValores=0;
		for (int i = 0; i < ciudadesSize; i++) {
			for (int j = 0; j < ciudadesSize; j++) {
				maxValores+=matrix[i][j];
			}
		}
		return maxValores/2;
	}

	/**
	 * Obtenemos la longitud menor de todas las calculadas.
	 *
	 * @return longitud miníma
	 */
	public int obtenerMinimoLong() {
		return Collections.min(ints);
	}

	/**
	 * Método imprime la matriz de distancias elaborada.
	 */
	public static void imprimirMatrizRecorridos() {
		System.out.print("\t");
		for (char k = 0; k < ciudadesSize; k++) {
			System.out.print("|" + MainGA.ciudades.get(k).charAt(0) + "|\t");

		}
		System.out.println();

		for (int i = 0; i < ciudadesSize; i++) {
			System.out.print("|");
			System.out.print(MainGA.ciudades.get(i).charAt(0) + "|\t");
			for (int j = 0; j < ciudadesSize; j++) {
				if (matrix[i][j] == 0) {
					System.out.print("-");
				} else {
					System.out.print(matrix[i][j]);
				}
				if (i != matrix[j].length) System.out.print("\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	    
	    
}
