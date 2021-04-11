package pdelam.galg;
import java.text.DecimalFormat;

/**
 * @author pdelam01
 */
public class Chromosome {
	private double[] chromosome;
	private double aptitude;
	private double max[] = { 10.0, 10.0, 10.0, 10.0, 10.0, 10.0};
	private double min[] = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
	DecimalFormat formato1 = new DecimalFormat("0.000");
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param chromosome - cromosoma
	 */
	public Chromosome(double[] chromosome) {
		this.chromosome = chromosome;
	}
	
	/**
	 * Constructor de la clase.
	 * 
	 * Damos valores aleatorios a los genes de los cromosomas, dentro de 
	 * unos limites max-min establecidos.
	 * 
	 * @param chromosomeTam -tamaño cromosoma
	 */
	public Chromosome(int chromosomeTam) {
		this.chromosome = new double[chromosomeTam];
		for (int i = 0; i < chromosomeTam; i++) {
			for (int j = 0; j < getGenSize(); j++) {
				this.setGen(j, (Math.random() * (max[j] - min[j]) + min[j]));
				//aux=(int)(Math.random() * (max-min+1)+min); valores 1-10 incluidos
				//pero con double tomaria decimales por encima de este
			}
		}
	}
	

	public double[] getChromosome() {
		return this.chromosome;
	}

	public int getChromosomeTam() {
		return this.chromosome.length;
	}

	public void setGen(int index, double gen) {
		this.chromosome[index] = gen;
	}

	public double getGen(int index) {
		return this.chromosome[index];
	}

	public int getGenSize() {
		return this.chromosome.length;
	}

	public double getMax(int index) {
		return max[index];
	}

	public double getMin(int index) {
		return min[index];
	}
	
	public double getMaxLength() {
		return max.length;
	}

	public void setAptitude(double aptitude) {
		this.aptitude = aptitude;
	}

	public double getAptitude() {
		return this.aptitude;
	}

	public String toString() {
		StringBuilder aux = new StringBuilder();
		aux.append(" ");
		for (int i = 0; i < getChromosomeTam(); i++) {
			aux.append(" | " + formato1.format(this.chromosome[i]) + " | ");
		}
		return aux.toString();
	}
}