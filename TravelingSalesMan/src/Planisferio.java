import java.util.List;
import java.util.Arrays;

public class Planisferio {
		//Si desea añadir nuevas ciudades, siga el orden alfabetico
	 	private List<String> ciudades = Arrays.asList("Albacete", "Bercianos del Paramo", "Calatayud", "Don Benito", "Escobar de Campos");
	 	int[][] matrix = new int[ciudades.size()][ciudades.size()];
	 	
	    public Planisferio() {
	    	for (int i = 0; i < ciudades.size(); i++) {
				for (int j = i; j < ciudades.size(); j++) {
					if (i == j) {
						matrix[i][j]=0;
					}
					if((int) (Math.random() * 100 + 1) <= 75) {
						matrix[i][j] = (int) (Math.random() * 100 + 1);
					}else {
						matrix[i][j]=0;
					}
				}
			}
	    	
	    	for (int j = 0; j < ciudades.size(); j++) {
				for (int i = j; i < ciudades.size(); i++) {
					if (i == j) {
						matrix[i][j]=0;
					}
					matrix[i][j] = matrix[j][i];
				}
			}
	    }
	    
	    public int longitudViaje(String[] longitud) {
	    	int lenght = 0;
	    	//trip llega desordenado: B A C D
	    	for (int i =0;i<longitud.length;i++) {

	    	}
	    	
	    	return 0;
	    }
	    
	    public int maxValue(){
	    	int maxValores=0;
	    	for (int i = 0; i < ciudades.size(); i++) {
				for (int j = 0; j < ciudades.size(); j++) {
					maxValores+=matrix[i][j];
				}
	    	}
	    	return maxValores/2;
	    }
	    
	    
}
