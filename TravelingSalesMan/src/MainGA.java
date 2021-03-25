
import io.jenetics.EnumGene;
import io.jenetics.Genotype;
import io.jenetics.Optimize;
import io.jenetics.PartiallyMatchedCrossover;
import io.jenetics.PermutationChromosome;
import io.jenetics.Phenotype;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.SwapMutator;

import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;

import io.jenetics.stat.DoubleMomentStatistics;

import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

import java.util.Arrays;
import java.util.List;




public class MainGA {
	final static boolean numGenerationsLimit = false;
	final static boolean evaluateInfo = true;
	
	 public static int evaluate(Genotype<EnumGene<String>> gt) {
		 return 0;
	}
	
	public static void main(String[] args) {
		final int populationSize = 4;
		final double probMutation = 0.1;
		final int probCrossover = 85;
		final int maxNumGenerations = 200;
		final int maxStableGenerations = 3;
		List<String> ciudades = Arrays.asList("Albacete", "Bercianos del Paramo", "Calatayud", "Don Benito", "Escobar de Campos");
		
		
		/* Creamos los genes, que son una secuencia de alelos (datos dentro de los genes) */
		/* ISeq: secuencia inmutable, ordenada y de tama�o fijo. */
		ISeq<String> genes = ISeq.of(ciudades);
		
		/* Creamos (Factory) un  individuo (Genotype) formado por un conjunto de genes (EnumGene) */
		Factory<Genotype<EnumGene<String>>> indiv = Genotype.of(PermutationChromosome.of(genes));
		
		/* 
		 * Engine, la clase principal, corre el algoritmo al completo
		 * 
		 * PartiallyMatchedCrossover: Garantiza que todos los genes se encuentran exactamente una vez en cada cromosoma.
		 * 							  En este tipo de cruce no se duplica ning�n gen (permutaciones)
		 * 
		 * SwapMutator: Permite mutaci�n del cromosoma cambiando el orden de los genes en este.
		 */
		Engine<EnumGene<String>, Integer> engine = Engine
	        	.builder(MainGA::evaluate,indiv)
	        	.optimize(Optimize.MINIMUM)
	        	.populationSize(populationSize)
	        	.selector(new RouletteWheelSelector<>())
	        	.alterers(new PartiallyMatchedCrossover<>(probCrossover), 
	        			  new SwapMutator<>(probMutation))
	        	.build();
		
		/* EvolutionStatics: recopila informaci�n estad�stica adicional */
		EvolutionStatistics<Integer, DoubleMomentStatistics> stats = EvolutionStatistics.ofNumber();
		
		/* Fenotipo */
		if(numGenerationsLimit) {
			Phenotype<EnumGene<String>, Integer> result = engine.stream()
					/* La evoluci�n parar� cuando llegue a "maxNumGenerations" generaciones */
					.limit(maxNumGenerations)
					/* Actualizamos las estad�sticas por cada generaci�n */
					.peek(stats)
					/* Seleccionamos el mejor fenotipo de todos */
					.collect(toBestPhenotype());
			
			System.out.println(result);
		}else {
			Phenotype<EnumGene<String>, Integer> result = engine.stream()
					/* La evoluci�n parar� cuando llegue a "maxStableGenerations" generaciones estables (iguales) */
					.limit(bySteadyFitness(maxStableGenerations))
					.peek(stats)
					.collect(toBestPhenotype());
			
			System.out.println(result);
		}
		
		
		MainGA.evaluateInfoPrint("Estad�sticas de la poblaci�n: \n"+stats);
	}
	
	private static void evaluateInfoPrint(String info) {
		if(MainGA.evaluateInfo){
			System.out.println(info);
		}
	}

}
