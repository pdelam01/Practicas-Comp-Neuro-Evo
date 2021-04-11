package pdelam.galg;

import java.util.ArrayList;

/**
 * @author pdelam01
 *
 */
public class MainGenAlg {

	/**
	 * false: 2 puntos cruce | true: 1 punto cruce
	 */
	final static boolean onePointCrossover = true;

	/**
	 * false: sin elitismo | true: con elitismo
	 */
	final static boolean elitism = true;

	/**
	 * false: mutación por gen | true: mutación por cromosoma
	 */
	final static boolean mutateChromosome = true;

	/**
	 * false: no ver gráfico | true: ver el gráfico
	 */
	final static boolean seeGraph = true;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		final int populationSize = 4;
		final int probMutation = 4;
		final int probCrossover = 90;
		final int numGenes = 6;
		final int maxNumGenerations = 500;
		int numGenerations = 0;

		GeneticAlgorithm genAlg = new GeneticAlgorithm(populationSize, probMutation, probCrossover);
		ArrayList<Double> apt = new ArrayList<>();

		//Nota: si se desea cambiar el num de genes, cambie max y min
		Population population = genAlg.createPopulation(numGenes);

		population=genAlg.evaluatePopulation(population);
		apt.add(population.getPopulationAptitude());

		do {
			if(elitism) {
				population=genAlg.selectRouletteWheelElitism(population);
			}else {
				population=genAlg.selectRouletteWheel(population);
			}

			if(onePointCrossover) {
				population=genAlg.crossoverOnePointPop(population);
			}else {
				population=genAlg.crossoverTwoPointPop(population);
			}

			if(mutateChromosome) {
				population=genAlg.mutatePopulationChromosome(population);
			}else {
				population=genAlg.mutatePopulationGen(population);
			}

			population=genAlg.evaluatePopulation(population);

			apt.add(population.getPopulationAptitude());
			numGenerations++;

		} while (numGenerations < maxNumGenerations);


		genAlg.printFitness(population);

		if(seeGraph) {
			Graph grafico = new Graph(apt, population);
		}

	}


}
