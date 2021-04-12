package pdelam.galg;
import java.text.DecimalFormat;

/**
 * @author pdelam01
 */
public class GeneticAlgorithm {
	private final int populationSize;
	private final double probMutation;
	private final double probCrossover;
	
	/**
	 * false: no se muestra información | true: sí
	 */
	final static boolean createInfo = false;
	final static boolean evaluateInfo = false;
	final static boolean selectionInfo = false;
	final static boolean crossoverInfo = false;
	final static boolean mutateInfo = false;
	final static boolean bestChromosomeInfo = true;
	
	DecimalFormat formato = new DecimalFormat("0.00");
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param populationSize - tamaño población
	 * @param probMutation - probabilidad de mutación (0-100)
	 * @param probCrossover - probabilidad de cruce (0-100)
	 */
	public GeneticAlgorithm(int populationSize, double probMutation, double probCrossover) {
		this.populationSize = populationSize;
		this.probMutation = probMutation;
		this.probCrossover = probCrossover;
	}
	
	/**
	 * Creamos la población inicial.
	 * 
	 * @param tamChromosome - la longitud del cromosoma (nº genes)
	 * @return population - la población inicial
	 */
	public Population createPopulation(int tamChromosome) {
		this.createInfoPrint("--- Se procede a la creación de la población inicial ---");
		Population population = new Population(this.populationSize, tamChromosome);

		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.createInfoPrint(population.getChromosomes(k).toString());
		}

		return population;
	}
	
	/**
	 * Calculamos la aptitud  de cada cromosoma.
	 * 
	 * Esta aptitud total será la suma de los valores de sus genes.
	 * 
	 * @param indiv - cromosoma a evaluar
	 * @return totalScore - aptitud total del cromosoma
	 */
	public double calculateFitnessIndiv(Chromosome indiv) {
		double totalScore = 0.0;
		for (int i = 0; i < indiv.getChromosomeTam(); i++) {
			totalScore += indiv.getGen(i);
		}
		this.evaluateInfoPrint("   con una aptitud total de: " + formato.format(totalScore) + " puntos \n");
		indiv.setAptitude(totalScore);

		return totalScore;
	}
	
	/**
	 * Evaluamos la aptitud de la población entera.
	 * 
	 * Recorremos los individuos de la población, calculamos la
	 * aptitud para cada uno y finalmente la total de la población.
	 * 
	 * @param population - la población a evaluar
	 * @return population - la población ya evaluada
	 */
	public Population evaluatePopulation(Population population) {
		this.evaluateInfoPrint("\n--- Se procede a la evaluación de la población ---");
		double populationFitness = 0.0;
		for (int i = 0; i < populationSize; i++) {
			Chromosome indiv = population.getChromosomes(i);
			this.evaluateInfoPrint("Cromosoma "+(i+1)+":"+indiv.toString());
			populationFitness += calculateFitnessIndiv(indiv);
		}
		population.setPopulationAptitude(populationFitness);
		return population;
	}
	
	/**
	 * Seleccionamos a la población mediante el algoritmo de la ruleta de pesos.
	 * 
	 * Escogemos una posición aleatoria. Luego recorremos cada cromosoma, sumando su
	 * aptitud a medida que avanzamos, hasta llegar a la posición aleatoria escogida
	 * al principio.
	 * 
	 * Existe elitismo: el mejor cromosoma pasa a la siguiente fase, si entrar en la
	 * seleccion
	 * 
	 * @param population - la población evaluada
	 * @return newPopulation - la nueva población tras la selección
	 */
	public Population selectRouletteWheelElitism(Population population) {
		this.selectInfoPrint("\n--- Se procede a la selección de la población (Con elitismo)---");
		Population newPop = new Population(populationSize);
		double totalFitness = population.getPopulationAptitude();
		double roulettePortion = 0;

		for (int j = 0; j < populationSize; j++) {
			roulettePortion = population.getChromosomes(j).getAptitude() / population.getPopulationAptitude() * 100;
			this.selectInfoPrint("El cromosoma " + population.getChromosomes(j) + " tiene un total de "
					+ formato.format(roulettePortion) + " espacios");
		}

		newPop.setIndiv(0, this.bestChromosome(population));
		this.selectInfoPrint("\nEl mejor cromosoma es: " + this.bestChromosome(population) + "\n");
		for (int i = 1; i < populationSize; i++) {
			double rouletteWheelPosition = Math.random() * population.getPopulationAptitude();
			double totalAptitudes = 0.0;
			int aux = 0;
			this.selectInfoPrint("Posición al azar: " + formato.format(rouletteWheelPosition)
					+ " | Aptitud total población: " + formato.format(totalFitness) + " | División: "
					+ formato.format(rouletteWheelPosition / population.getPopulationAptitude() * 100));
			do {
				totalAptitudes += population.getChromosomes(aux).getAptitude();
				if (totalAptitudes >= rouletteWheelPosition) {
					newPop.setIndiv(i, population.getChromosomes(aux));
					break;
				}
				aux++;
			} while (aux <= (population.getPopulationTam() - 1));
		}

		this.selectInfoPrint("\nPoblación resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.selectInfoPrint(newPop.getChromosomes(k).toString());
		}

		return newPop;
	}
	
	/**
	 * Seleccionamos a la población mediante el algoritmo de la rultea de pesos
	 * 
	 * Operamos de igual manera que anteriormente, solo que sin elitismo
	 * 
	 * @param population - la población evaluada
	 * @return newPopulation - la nueva población tras la selección
	 */
	public Population selectRouletteWheel(Population population) {
		this.selectInfoPrint("\n--- Se procede a la selección de la población (Sin elitismo)---");
		Population newPop = new Population(populationSize);
		double totalFitness = population.getPopulationAptitude();
		double roulettePortion=0.0;
		
		for (int j = 0; j < populationSize; j++) {
			roulettePortion = population.getChromosomes(j).getAptitude() / population.getPopulationAptitude() * 100;
			this.selectInfoPrint("El cromosoma " + population.getChromosomes(j) + " tiene un total de "
					+ formato.format(roulettePortion) + " espacios");
		}
		this.selectInfoPrint(" ");
		
		for (int i = 0; i < populationSize; i++) {
			double rouletteWheelPosition = Math.random() * population.getPopulationAptitude();
			double totalAptitudes = 0.0;
			int aux = 0;
			this.selectInfoPrint("Posición al azar: " + formato.format(rouletteWheelPosition) + " | Aptitud total población: "
					+ formato.format(totalFitness) + " | División: "
					+ formato.format(rouletteWheelPosition / population.getPopulationAptitude() * 100));
			do {
				totalAptitudes +=  population.getChromosomes(aux).getAptitude();
				if (totalAptitudes >= rouletteWheelPosition) {
					newPop.setIndiv(i, population.getChromosomes(aux));
					break;
				}
				aux++;
			}while (aux <= (population.getPopulationTam()-1));
		}
		
		this.selectInfoPrint("\nPoblación resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.selectInfoPrint(newPop.getChromosomes(k).toString());
		}
		
		return newPop;
	}
	
	
	/**
	 * Cruce o apareamiento, toma a la población tras la selección y 
	 * combina a los individuos para crear 2 nuevos hijos por cada 
	 * par padre-madre que se cruzan.
	 * 
	 * @param population - la población tras la selección
	 * @return population - la población tras el cruce
	 */
	public Population crossoverOnePointPop(Population population) {
		this.crossoverInfoPrint("\\n--- Se procede al cruce de la población (1 punto)---");
		for (int i = 0; i < populationSize; i++) {
			Chromosome parent1 = population.getChromosomes(i);
			Chromosome parent2 = population.getChromosomes(++i);
			Chromosome child = new Chromosome(parent1.getChromosomeTam());
			Chromosome child2 = new Chromosome(parent1.getChromosomeTam());

			if (this.probCrossover >= (int) (Math.random() * 100 + 1)) {
				this.crossoverInfoPrint("\n   It's a match!");
				int breakpoint = (int) (Math.random() * (parent1.getGenSize() - 1) + 1);

				this.crossoverInfoPrint("Padre: " + parent1);
				this.crossoverInfoPrint("Madre: " + parent2);

				for (int j = 0; j < parent1.getChromosomeTam(); j++) {
					if (j >= breakpoint) {
						child.setGen(j, parent2.getGen(j));
						child2.setGen(j, parent1.getGen(j));
					} else {
						child.setGen(j, parent1.getGen(j));
						child2.setGen(j, parent2.getGen(j));
					}
				}
				this.crossoverInfoPrint("Punto de cruce: " + breakpoint);
				this.crossoverInfoPrint("1º hijo: " + child2.toString());
				this.crossoverInfoPrint("2º hijo: " + child.toString());

				population.setIndiv(i, child);
				population.setIndiv(--i, child2);
			} else {
				this.crossoverInfoPrint("\n   Not a match :(");
				population.setIndiv(i, parent2);
				population.setIndiv(--i, parent1);
			}

			if ((i + 1) == populationSize) {
				break;
			}

			i++;
		}

		this.crossoverInfoPrint("\nPoblación resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.crossoverInfoPrint(population.getChromosomes(k).toString());
		}
		return population;
	}
	
	/**
	 * Operamos de igual manera que anteriormente, solo que esta vez existen 2
	 * puntos de corte de combinación de los hijos.
	 * 
	 * @param population - población tras la selección
	 * @return population - población tras el cruce
	 */
	public Population crossoverTwoPointPop(Population population) {
		this.crossoverInfoPrint("\n--- Se procede al cruce de la población (2 puntos) ---");
		for (int i = 0; i < populationSize; i++) {
			
			Chromosome parent1 = population.getChromosomes(i);
			Chromosome parent2 = population.getChromosomes(++i);
			Chromosome child = new Chromosome(parent1.getChromosomeTam());
			Chromosome child2 = new Chromosome(parent1.getChromosomeTam());
			
			if(this.probCrossover >= (int) (Math.random() * 100 + 1)) {
				this.crossoverInfoPrint("\n   It's a match!");
				int breakpoint1 = (int) (Math.random() * (parent1.getGenSize()-1)+1);
				int breakpoint2 = (int) (Math.random() * (parent1.getGenSize()-1)+1);
				
				this.crossoverInfoPrint("Padre: "+parent1);
				this.crossoverInfoPrint("Madre: "+parent2);
				
				if(breakpoint1 == breakpoint2) {
					if(breakpoint1==1 && breakpoint2==1) {
						breakpoint1=1;
						//aleatorio entre 2 y genSize - 1
						breakpoint2=(int) (Math.random() * (parent1.getGenSize()-2)+2);
					}else {
						if(breakpoint1==(parent1.getGenSize()-1) && breakpoint2==(parent1.getGenSize()-1)) {
							//aleatorio entre 1 y genSize - 2 (sin tomar valor genSize-1)
							breakpoint1=(int) (Math.random() * (parent1.getGenSize()-2)+1);
							breakpoint2=parent1.getGenSize()-1;
						}else {
							breakpoint1--;
							breakpoint2++;
						}
					}
				}
				
				//si el punto 1 mayor que el punto 2, intercambiamos:
				if(breakpoint2 < breakpoint1) {
					int aux = breakpoint1;
					breakpoint1 = breakpoint2;
					breakpoint2 = aux;
				}
				
				for (int j = 0; j < parent1.getChromosomeTam(); j++) {
					if (j < breakpoint1 || j >= breakpoint2) {
						child.setGen(j, parent2.getGen(j));
						child2.setGen(j, parent1.getGen(j));
					} else {
						child.setGen(j, parent1.getGen(j));
						child2.setGen(j, parent2.getGen(j));
					}
				}
				
				this.crossoverInfoPrint("Punto de cruce 1: "+breakpoint1);
				this.crossoverInfoPrint("Punto de cruce 2: "+breakpoint2);
				this.crossoverInfoPrint("1º hijo: "+child2.toString());
				this.crossoverInfoPrint("2º hijo: "+child.toString());
				
				population.setIndiv(i, child);
				population.setIndiv(--i, child2);
			}else {
				this.crossoverInfoPrint("\n   Not a match :(");
				population.setIndiv(i, parent2);
				population.setIndiv(--i, parent1);
			}
			
			
			if ((i + 1) == populationSize) {
                break;
			}
			
			i++;
		}
		
		this.crossoverInfoPrint("\nPoblación resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.crossoverInfoPrint(population.getChromosomes(k).toString());
		}
		
		return population;
	}
	
	/**
	 * Aplicamos mutación por cromosma a la población.
	 * 
	 * Recorremos cada individuo o cromosoma de la población. Si el número aleatorio
	 * resultante es mayor o menor que el valor de la probabilidad de mutación,
	 * dicho cromosoma mutará un gen al azar o no, respectivamente. 
	 * 
	 * @param population - población tras el cruce
	 * @return population - población con mutaciones o no
	 */
	public Population mutatePopulationChromosome(Population population) {
		this.mutateInfoPrint("\n--- Se procede a la mutación de la población ---");
		double maxGen = 0.0;
		double minGen = 0.0;
		double valorGenRandom = 0.0;
		for (int i = 0; i < populationSize; i++) {
			Chromosome indiv = population.getChromosomes(i);
			if (probMutation >= (int) (Math.random() * 100 + 1)) {
				this.mutateInfoPrint("Mutación!");
				int genRandom = (int) (Math.random() * indiv.getGenSize());

				maxGen = indiv.getMax(genRandom);
				minGen = indiv.getMin(genRandom);
				valorGenRandom = (Math.random() * ((maxGen - minGen + 0.00001) + minGen));
				this.mutateInfoPrint("Cromosoma a mutar: " + indiv.toString());
				this.mutateInfoPrint(
						"Nuevo valor: " + formato.format(valorGenRandom) + " muta el gen número: " + (genRandom + 1));

				indiv.setGen(genRandom, valorGenRandom);
				this.mutateInfoPrint("Cromosoma tras mutación: " + indiv.toString() + "\n");
			}
			population.setIndiv(i, indiv);
		}
		return population;
	}
	
	/**
	 * Aplicamos mutación por gen a la población.
	 * 
	 * Recorremos cada individuo o cromosoma de la población y aplicamos a cada uno de sus
	 * genes la probabilidad de mutar o no, haciendo que por cada cromosoma, su probabilidad
	 * de mutación aumente, siendo mucho mayor que la mutación por cromosoma. 
	 * 
	 * @param population - población tras el cruce
	 * @return population - población con mutaciones o no
	 */
	public Population mutatePopulationGen(Population population) {
		this.mutateInfoPrint("\n--- Se procede a la mutación de la población ---");	
		double maxGen = 0.0;
		double minGen = 0.0;
		double valorGenRandom = 0.0;
		for (int i = 0; i < populationSize; i++) {
			Chromosome indiv = population.getChromosomes(i);
			for (int j = 0; j < indiv.getGenSize(); j++) {
				if (probMutation >= (int) (Math.random() * 100 + 1)) {
					this.mutateInfoPrint("Mutación!");
					
					maxGen = indiv.getMax(j);
					minGen = indiv.getMin(j);
					valorGenRandom = (Math.random() * ((maxGen - minGen+0.00001) + minGen));
					this.mutateInfoPrint("Cromosoma a mutar: "+indiv.toString());
					this.mutateInfoPrint("Nuevo valor: " + formato.format(valorGenRandom)+" muta el gen número: "+ (j+1));
	
					indiv.setGen(j, valorGenRandom);
					this.mutateInfoPrint("Cromosoma tras mutación: "+indiv.toString()+"\n");
				}
			}
			population.setIndiv(i, indiv);
		}
		return population;
	}
	
	/**
	 * Obtenemos el mejor cromosoma, siendo este el mejor ya que tiene la mayor
	 * aptitud de su población.
	 * 
	 * @param population
	 * @return
	 */
	public Chromosome bestChromosome(Population population) {
		Chromosome max = population.getChromosomes(0);
		for (int i = 0; i < populationSize; i++) {
			Chromosome indiv = population.getChromosomes(i);
			if (indiv.getAptitude() >= max.getAptitude()) {
				max = indiv;
			}
		}
		return max;
	}
	
	/**
	 * Mostramos el mejor cromosoma de la población y su aptitud total.
	 * 
	 * @param population
	 */
	public void printFitness(Population population) {
		this.bestChromosomeInfo("\nEl mejor cromosoma es: " + this.bestChromosome(population));
		this.bestChromosomeInfo("Con una aptitud total de " + formato.format(this.bestChromosome(population).getAptitude())
				+ " puntos sobre " + this.maximoValor(population));
	}
	
	/**
	 * Obtenemos el valor máximo que podría obtener el cromosoma, a partir de la suma
	 * de los valores máximos que pueden tener cada genes de cada cromosoma.
	 * 
	 * (En este caso sabemos cual sería la mejor de las soluciones. Por norma general
	 * en algoritmos genéticos no la conocemos. De eso se trata, claro)
	 * 
	 * @param population - población a evaluar
	 * @return valor - valor maximo a obtener el cromosoma
	 */
	public double maximoValor(Population population) {
		double valor = 0;
		for (int i = 0; i < population.getChromosomes(0).getMaxLength(); i++) {
			valor += population.getChromosomes(0).getMax(i);
		}
		return valor;
	}
	
	/**
	 * Permite mostrar información sobre el método crear.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void createInfoPrint(String info) {
		if(GeneticAlgorithm.createInfo) {
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar información sobre el método evaluar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void evaluateInfoPrint(String info) {
		if(GeneticAlgorithm.evaluateInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar información sobre el método seleccionar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void selectInfoPrint(String info) {
		if(GeneticAlgorithm.selectionInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar información sobre el método cruzar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void crossoverInfoPrint(String info) {
		if(GeneticAlgorithm.crossoverInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar información sobre el método mutar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void mutateInfoPrint(String info) {
		if(GeneticAlgorithm.mutateInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar información sobre el método de mostar mejor cromosoma.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void bestChromosomeInfo(String info) {
		if(GeneticAlgorithm.bestChromosomeInfo){
			System.out.println(info);
		}
	}
	
}
