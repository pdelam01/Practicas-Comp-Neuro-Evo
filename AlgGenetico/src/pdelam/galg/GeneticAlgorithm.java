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
	 * false: no se muestra informaci�n | true: s�
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
	 * @param populationSize - tama�o poblaci�n
	 * @param probMutation - probabilidad de mutaci�n (0-100)
	 * @param probCrossover - probabilidad de cruce (0-100)
	 */
	public GeneticAlgorithm(int populationSize, double probMutation, double probCrossover) {
		this.populationSize = populationSize;
		this.probMutation = probMutation;
		this.probCrossover = probCrossover;
	}
	
	/**
	 * Creamos la poblaci�n inicial.
	 * 
	 * @param tamChromosome - la longitud del cromosoma (n� genes)
	 * @return population - la poblaci�n inicial
	 */
	public Population createPopulation(int tamChromosome) {
		this.createInfoPrint("--- Se procede a la creaci�n de la poblaci�n inicial ---");
		Population population = new Population(this.populationSize, tamChromosome);

		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.createInfoPrint(population.getChromosomes(k).toString());
		}

		return population;
	}
	
	/**
	 * Calculamos la aptitud  de cada cromosoma.
	 * 
	 * Esta aptitud total ser� la suma de los valores de sus genes.
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
	 * Evaluamos la aptitud de la poblaci�n entera.
	 * 
	 * Recorremos los individuos de la poblaci�n, calculamos la
	 * aptitud para cada uno y finalmente la total de la poblaci�n.
	 * 
	 * @param population - la poblaci�n a evaluar
	 * @return population - la poblaci�n ya evaluada
	 */
	public Population evaluatePopulation(Population population) {
		this.evaluateInfoPrint("\n--- Se procede a la evaluaci�n de la poblaci�n ---");
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
	 * Seleccionamos a la poblaci�n mediante el algoritmo de la ruleta de pesos.
	 * 
	 * Escogemos una posici�n aleatoria. Luego recorremos cada cromosoma, sumando su
	 * aptitud a medida que avanzamos, hasta llegar a la posici�n aleatoria escogida
	 * al principio.
	 * 
	 * Existe elitismo: el mejor cromosoma pasa a la siguiente fase, si entrar en la
	 * seleccion
	 * 
	 * @param population - la poblaci�n evaluada
	 * @return newPopulation - la nueva poblaci�n tras la selecci�n
	 */
	public Population selectRouletteWheelElitism(Population population) {
		this.selectInfoPrint("\n--- Se procede a la selecci�n de la poblaci�n (Con elitismo)---");
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
			this.selectInfoPrint("Posici�n al azar: " + formato.format(rouletteWheelPosition)
					+ " | Aptitud total poblaci�n: " + formato.format(totalFitness) + " | Divisi�n: "
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

		this.selectInfoPrint("\nPoblaci�n resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.selectInfoPrint(newPop.getChromosomes(k).toString());
		}

		return newPop;
	}
	
	/**
	 * Seleccionamos a la poblaci�n mediante el algoritmo de la rultea de pesos
	 * 
	 * Operamos de igual manera que anteriormente, solo que sin elitismo
	 * 
	 * @param population - la poblaci�n evaluada
	 * @return newPopulation - la nueva poblaci�n tras la selecci�n
	 */
	public Population selectRouletteWheel(Population population) {
		this.selectInfoPrint("\n--- Se procede a la selecci�n de la poblaci�n (Sin elitismo)---");
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
			this.selectInfoPrint("Posici�n al azar: " + formato.format(rouletteWheelPosition) + " | Aptitud total poblaci�n: "
					+ formato.format(totalFitness) + " | Divisi�n: "
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
		
		this.selectInfoPrint("\nPoblaci�n resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.selectInfoPrint(newPop.getChromosomes(k).toString());
		}
		
		return newPop;
	}
	
	
	/**
	 * Cruce o apareamiento, toma a la poblaci�n tras la selecci�n y 
	 * combina a los individuos para crear 2 nuevos hijos por cada 
	 * par padre-madre que se cruzan.
	 * 
	 * @param population - la poblaci�n tras la selecci�n
	 * @return population - la poblaci�n tras el cruce
	 */
	public Population crossoverOnePointPop(Population population) {
		this.crossoverInfoPrint("\\n--- Se procede al cruce de la poblaci�n (1 punto)---");
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
				this.crossoverInfoPrint("1� hijo: " + child2.toString());
				this.crossoverInfoPrint("2� hijo: " + child.toString());

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

		this.crossoverInfoPrint("\nPoblaci�n resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.crossoverInfoPrint(population.getChromosomes(k).toString());
		}
		return population;
	}
	
	/**
	 * Operamos de igual manera que anteriormente, solo que esta vez existen 2
	 * puntos de corte de combinaci�n de los hijos.
	 * 
	 * @param population - poblaci�n tras la selecci�n
	 * @return population - poblaci�n tras el cruce
	 */
	public Population crossoverTwoPointPop(Population population) {
		this.crossoverInfoPrint("\n--- Se procede al cruce de la poblaci�n (2 puntos) ---");
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
				this.crossoverInfoPrint("1� hijo: "+child2.toString());
				this.crossoverInfoPrint("2� hijo: "+child.toString());
				
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
		
		this.crossoverInfoPrint("\nPoblaci�n resultante obtenida: ");
		for (int k = 0; k < population.getPopulationTam(); k++) {
			this.crossoverInfoPrint(population.getChromosomes(k).toString());
		}
		
		return population;
	}
	
	/**
	 * Aplicamos mutaci�n por cromosma a la poblaci�n.
	 * 
	 * Recorremos cada individuo o cromosoma de la poblaci�n. Si el n�mero aleatorio
	 * resultante es mayor o menor que el valor de la probabilidad de mutaci�n,
	 * dicho cromosoma mutar� un gen al azar o no, respectivamente. 
	 * 
	 * @param population - poblaci�n tras el cruce
	 * @return population - poblaci�n con mutaciones o no
	 */
	public Population mutatePopulationChromosome(Population population) {
		this.mutateInfoPrint("\n--- Se procede a la mutaci�n de la poblaci�n ---");
		double maxGen = 0.0;
		double minGen = 0.0;
		double valorGenRandom = 0.0;
		for (int i = 0; i < populationSize; i++) {
			Chromosome indiv = population.getChromosomes(i);
			if (probMutation >= (int) (Math.random() * 100 + 1)) {
				this.mutateInfoPrint("Mutaci�n!");
				int genRandom = (int) (Math.random() * indiv.getGenSize());

				maxGen = indiv.getMax(genRandom);
				minGen = indiv.getMin(genRandom);
				valorGenRandom = (Math.random() * ((maxGen - minGen + 0.00001) + minGen));
				this.mutateInfoPrint("Cromosoma a mutar: " + indiv.toString());
				this.mutateInfoPrint(
						"Nuevo valor: " + formato.format(valorGenRandom) + " muta el gen n�mero: " + (genRandom + 1));

				indiv.setGen(genRandom, valorGenRandom);
				this.mutateInfoPrint("Cromosoma tras mutaci�n: " + indiv.toString() + "\n");
			}
			population.setIndiv(i, indiv);
		}
		return population;
	}
	
	/**
	 * Aplicamos mutaci�n por gen a la poblaci�n.
	 * 
	 * Recorremos cada individuo o cromosoma de la poblaci�n y aplicamos a cada uno de sus
	 * genes la probabilidad de mutar o no, haciendo que por cada cromosoma, su probabilidad
	 * de mutaci�n aumente, siendo mucho mayor que la mutaci�n por cromosoma. 
	 * 
	 * @param population - poblaci�n tras el cruce
	 * @return population - poblaci�n con mutaciones o no
	 */
	public Population mutatePopulationGen(Population population) {
		this.mutateInfoPrint("\n--- Se procede a la mutaci�n de la poblaci�n ---");	
		double maxGen = 0.0;
		double minGen = 0.0;
		double valorGenRandom = 0.0;
		for (int i = 0; i < populationSize; i++) {
			Chromosome indiv = population.getChromosomes(i);
			for (int j = 0; j < indiv.getGenSize(); j++) {
				if (probMutation >= (int) (Math.random() * 100 + 1)) {
					this.mutateInfoPrint("Mutaci�n!");
					
					maxGen = indiv.getMax(j);
					minGen = indiv.getMin(j);
					valorGenRandom = (Math.random() * ((maxGen - minGen+0.00001) + minGen));
					this.mutateInfoPrint("Cromosoma a mutar: "+indiv.toString());
					this.mutateInfoPrint("Nuevo valor: " + formato.format(valorGenRandom)+" muta el gen n�mero: "+ (j+1));
	
					indiv.setGen(j, valorGenRandom);
					this.mutateInfoPrint("Cromosoma tras mutaci�n: "+indiv.toString()+"\n");
				}
			}
			population.setIndiv(i, indiv);
		}
		return population;
	}
	
	/**
	 * Obtenemos el mejor cromosoma, siendo este el mejor ya que tiene la mayor
	 * aptitud de su poblaci�n.
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
	 * Mostramos el mejor cromosoma de la poblaci�n y su aptitud total.
	 * 
	 * @param population
	 */
	public void printFitness(Population population) {
		this.bestChromosomeInfo("\nEl mejor cromosoma es: " + this.bestChromosome(population));
		this.bestChromosomeInfo("Con una aptitud total de " + formato.format(this.bestChromosome(population).getAptitude())
				+ " puntos sobre " + this.maximoValor(population));
	}
	
	/**
	 * Obtenemos el valor m�ximo que podr�a obtener el cromosoma, a partir de la suma
	 * de los valores m�ximos que pueden tener cada genes de cada cromosoma.
	 * 
	 * (En este caso sabemos cual ser�a la mejor de las soluciones. Por norma general
	 * en algoritmos gen�ticos no la conocemos. De eso se trata, claro)
	 * 
	 * @param population - poblaci�n a evaluar
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
	 * Permite mostrar informaci�n sobre el m�todo crear.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void createInfoPrint(String info) {
		if(GeneticAlgorithm.createInfo) {
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar informaci�n sobre el m�todo evaluar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void evaluateInfoPrint(String info) {
		if(GeneticAlgorithm.evaluateInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar informaci�n sobre el m�todo seleccionar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void selectInfoPrint(String info) {
		if(GeneticAlgorithm.selectionInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar informaci�n sobre el m�todo cruzar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void crossoverInfoPrint(String info) {
		if(GeneticAlgorithm.crossoverInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar informaci�n sobre el m�todo mutar.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void mutateInfoPrint(String info) {
		if(GeneticAlgorithm.mutateInfo){
			System.out.println(info);
		}
	}
	
	/**
	 * Permite mostrar informaci�n sobre el m�todo de mostar mejor cromosoma.
	 * 
	 * @param info - String con la info a mostrar
	 */
	private void bestChromosomeInfo(String info) {
		if(GeneticAlgorithm.bestChromosomeInfo){
			System.out.println(info);
		}
	}
	
}
