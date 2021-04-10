
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
    /* false: condicion de parada X generaciones estables | true: X generaciones totales */
	final static boolean numGenerationsLimit = true;

	/* false: no se muestra info adicional | true: sí */
	final static boolean evaluateInfo = true;
	final static boolean matrizRecorridoInfo = true;

	public static List<String> ciudades = Arrays.asList("Albacete", "Bercianos del Paramo", "Calatayud", "Don Benito", "Escobar de Campos");
	private static final Planisferio mapa = new Planisferio();
	private static Phenotype<EnumGene<String>, Integer> result;

	 public static int evaluate(final Genotype<EnumGene<String>> gt) {
		 String [] viaje = new String[ciudades.size()];

		 for(int i=0; i<gt.chromosome().length(); i++){
		 	viaje [i] = gt.get(0).get(i).toString();
		 }

		 return mapa.longitudViaje(viaje);
	}
	
	public static void main(String[] args) {
		final int populationSize = 5;
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
	        	.populationSize(populationSize)
				.optimize(Optimize.MINIMUM)
	        	.selector(new RouletteWheelSelector<>())
	        	.alterers(new PartiallyMatchedCrossover<>(probCrossover), 
	        			  new SwapMutator<>(probMutation))
	        	.build();
		
		/* EvolutionStatics: recopila información estadística adicional */
		EvolutionStatistics<Integer, DoubleMomentStatistics> stats = EvolutionStatistics.ofNumber();

		/* Mostramos la matriz de distancias */
		if(matrizRecorridoInfo){
			Planisferio.imprimirMatrizRecorridos();
		}
		
		/* Fenotipo */
		if(numGenerationsLimit) {
			result = engine.stream()
					/* La evolución parará cuando llegue a "maxNumGenerations" generaciones */
					.limit(maxNumGenerations)
					/* Actualizamos las estadísticas por cada generación */
					.peek(stats)
					/* Seleccionamos el mejor fenotipo de todos */
					.collect(toBestPhenotype());

		}else {
			result = engine.stream()
					/* La evolución parará cuando llegue a "maxStableGenerations" generaciones estables (iguales) */
					.limit(bySteadyFitness(maxStableGenerations))
					.peek(stats)
					.collect(toBestPhenotype());

		}

		System.out.println(MainGA.toStringResults());
		MainGA.evaluateInfoPrint("\nEstadísticas de la población: \n"+stats);
	}

    /**
     * Permite mostrar información sobre el método evaluar.
     *
     * @param info - String con la info a mostrar
     */
	private static void evaluateInfoPrint(String info) {
		if(MainGA.evaluateInfo){
			System.out.println(info);
		}
	}

    /**
     * Muestra información sobre aptitud del mejor cromosoma
     *
     * @return aux - String con la info a mostrar
     */
	public static String toStringResults(){
		StringBuilder aux = new StringBuilder();
		aux.append("Camino mínimo y aptitud mínima: ");
		aux.append(result).append(" puntos.\n");
		aux.append("Longitud del viaje de: ").append(mapa.obtenerMinimoLong()).append("Km.");

		return aux.toString();
	}

}
