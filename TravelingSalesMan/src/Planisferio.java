import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

public class Planisferio {
	
	 	private List<String> ciudades;
	 	private List<String> relaciones;
	 	int[][] matrix;
	 	private HashMap<String, Integer> hasm;
	 	
	    public Planisferio() {
	    	this.ciudades = Arrays.asList("Albacete", "Bercianos del Paramo", "Calatayud", "Don Benito", "Escobar de Campos");
	    	
	    	for (int i = 0; i < ciudades.size(); i++) {
				for (int j = i; j < ciudades.size(); j++) {
					if (i == j) {
						matrix[i][j]=0;
					}
					matrix[i][j] = (int) (Math.random() * 100 + 1);
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
	    
	    public int longitudViaje() {
	    	return 0;
	    }
	    
	    public int maxValue(){
	    	return 0;
	    }
	    
	    
}
