import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Planisferio {
	 	static int ciudadesSize = MainGA.ciudades.size();
		static int[][] matrix = new int[ciudadesSize][ciudadesSize];
		ArrayList<Integer> ints = new ArrayList<>();
	 	//final int ALTO = 99999;
	 	
	    public Planisferio() {
	    	for (int i = 0; i < ciudadesSize; i++) {
				for (int j = i; j < ciudadesSize; j++) {
					if (i == j) {
						matrix[i][j]=0;
					}
					matrix[i][j] = (int) (Math.random() * 100 + 1);
					/*if((int) (Math.random() * 100 + 1) <= 75) {
						matrix[i][j] = (int) (Math.random() * 100 + 1);
					}else {
						matrix[i][j]=0;
					}*/
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
	    
	    public int longitudViaje(String[] camino) {
	    	int totalLong = 0;
	    	int ciudad1, ciudad2;
	    	for (int i =0;i<camino.length;i++) {
				if(i+1!=camino.length){
					ciudad1=MainGA.ciudades.indexOf(camino[i]);
					ciudad2=MainGA.ciudades.indexOf(camino[i+1]);
					totalLong += matrix[ciudad1][ciudad2];
					if(matrix[ciudad1][ciudad2]!=0){
						totalLong += matrix[ciudad1][ciudad2];
					}/*else{
						totalLong += ALTO;
					}*/
				}
	    	}
	    	totalLong=totalLong/2;
	    	ints.add(totalLong);
			System.out.println("holassss"+totalLong);
			System.out.println(ints.toString());
			//System.out.println(Collections.min(ints));
	    	return totalLong;
	    }

	    public int obtenerMinimoLong(){
			return Collections.min(ints);
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

	    public static void imprimirMatrizRecorridos(){
			for (int i = 0; i < ciudadesSize; i++) {
				System.out.print("|");
				for (int j = 0; j < ciudadesSize; j++) {
					System.out.print(matrix[i][j]);
					if (i!=matrix[j].length) System.out.print("\t");
				}
				System.out.println("|");
			}
			System.out.println();
		}
	    
	    
}
