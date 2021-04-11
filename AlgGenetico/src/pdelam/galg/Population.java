package pdelam.galg;

/**
 * @author pdelam01
 */
public class Population {
	private final Chromosome[] population;
	private double populationApt;
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param n - tama�o poblaci�n
	 */
	public Population(int n) {
		this.population = new Chromosome[n];
	}
	
	/**
	 * Constructor de la clase.
	 * Creamos cada cromosoma o individuo y lo a�adimos a la poblaci�n.
	 * 
	 * @param n - tama�o poblaci�n
	 * @param tamChromosome - tama�o cromosoma (n� genes)
	 */
	public Population(int n, int tamChromosome) {
		this.population = new Chromosome[n];
		for (int i = 0; i < n; i++) {
			Chromosome indiv = new Chromosome(tamChromosome);
			this.population[i] = indiv;
		}
	}

	public Chromosome[] getIndivs() {
		return this.population;
	}

	public Chromosome setIndiv(int index, Chromosome indiv) {
		return this.population[index] = indiv;
	}

	public double getPopulationAptitude() {
		return this.populationApt;
	}

	public void setPopulationAptitude(double fitness) {
		this.populationApt = fitness;
	}

	public int getPopulationTam() {
		return this.population.length;
	}
	
	public Chromosome getChromosomes(int index) {
		return this.population[index];
	}
	

}
