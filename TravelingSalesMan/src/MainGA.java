
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
	private static final List<String> ciudades = Arrays.asList("Albacete", "Bercianos del Paramo", "Calatayud", "Don Benito", "Escobar de Campos");
	private static final Planisferio mapa = new Planisferio();

	 public static int evaluate(final Genotype<EnumGene<String>> gt) {

		 String [] trip = new String[ciudades.size()];
	        
	        for(int i=0; i<gt.chromosome().length(); i++){
	            trip [i] = gt.chromosome().gene().toString();
	        }
	              
	        return mapa.maxValue() - mapa.longitudViaje(trip);
	}
	
	public static void main(String[] args) {
		final int populationSize = ciudades.size();
		final double probMutation = 0.1;
		final double probCrossover = 0.85;
		final int maxNumGenerations = 200;
		final int maxStableGenerations = 3;

		
		
		/* Creamos los genes, que son una secuencia de alelos (datos dentro de los genes) */
		/* ISeq: secuencia inmutable, ordenada y de tamaño fijo. */
		ISeq<String> genes = ISeq.of(ciudades);
		
		/* Creamos (Factory) un  individuo (Genotype) formado por un conjunto de genes (EnumGene) */
		Factory<Genotype<EnumGene<String>>> indiv = Genotype.of(PermutationChromosome.of(genes));
		
		/* 
		 * Engine, la clase principal, corre el algoritmo al completo
		 * 
		 * PartiallyMatchedCrossover: Garantiza que todos los genes se encuentran exactamente una vez en cada cromosoma.
		 * 							  En este tipo de cruce no se duplica ningún gen (permutaciones)
		 * 
		 * SwapMutator: Permite mutación del cromosoma cambiando el orden de los genes en este.
		 */
		Engine<EnumGene<String>, Integer> engine = Engine
	        	.builder(MainGA::evaluate,indiv)
	        	.optimize(Optimize.MINIMUM)
	        	.populationSize(populationSize)
	        	.selector(new RouletteWheelSelector<>())
	        	.alterers(new PartiallyMatchedCrossover<>(probCrossover), 
	        			  new SwapMutator<>(probMutation))
	        	.build();
		
		/* EvolutionStatics: recopila información estadística adicional */
		EvolutionStatistics<Integer, DoubleMomentStatistics> stats = EvolutionStatistics.ofNumber();
		
		/* Fenotipo */
		if(numGenerationsLimit) {
			Phenotype<EnumGene<String>, Integer> result = engine.stream()
					/* La evolución parará cuando llegue a "maxNumGenerations" generaciones */
					.limit(maxNumGenerations)
					/* Actualizamos las estadísticas por cada generación */
					.peek(stats)
					/* Seleccionamos el mejor fenotipo de todos */
					.collect(toBestPhenotype());

			System.out.println(result);
		}else {
			Phenotype<EnumGene<String>, Integer> result = engine.stream()
					/* La evolución parará cuando llegue a "maxStableGenerations" generaciones estables (iguales) */
					.limit(bySteadyFitness(maxStableGenerations))
					.peek(stats)
					.collect(toBestPhenotype());

			System.out.println(result);
		}
		MainGA.evaluateInfoPrint("Estadísticas de la población: \n"+stats);
	}
	
	private static void evaluateInfoPrint(String info) {
		if(MainGA.evaluateInfo){
			System.out.println(info);
		}
	}

}
