package pdelam.galgSalesman;

/**
 * @author pdelam01
 */
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
import static io.jenetics.engine.Limits.byFixedGeneration;
import static io.jenetics.engine.Limits.bySteadyFitness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGA {
    /* false: condicion de parada X generaciones estables | true: X generaciones totales */
	final static boolean numGenerationsLimit = false;

	/* false: no se muestra info sobre cromosomas a evaluar | true: sí */
	final static boolean evaluateInfo = true;

    /* false: no se muestra info adicional | true: sí */
    final static boolean statsInfo = true;

    /* false: no se muestra el mapa de recorridos | true: sí */
	final static boolean matrizRecorridoInfo = true;

	/* Parámetros generales */
	public static final List<String> ciudades = Arrays.asList("Albacete", "Bercianos del Paramo", "Calatayud", "Don Benito", "Escobar de Campos");
	private static final int populationSize = 5;
	private static final double probMutation = 0.1;
	private static final double probCrossover = 0.85;
	private static final int maxNumGenerations = 9;
	private static final int maxStableGenerations = 3;

	private static final Planisferio mapa = new Planisferio();
	private static Phenotype<EnumGene<String>, Integer> result;

    /**
     * Evaluamos la aptitud de la población entera.
     *
     * @param individuo - cromosoma a evaluar
     * @return longitud de cada viaje (cromosoma) evaluado
     */
	public static int evaluate(final Genotype<EnumGene<String>> individuo) {
		StringBuilder aux = new StringBuilder();
		List<String> listaAux = new ArrayList<>();

		String[] viaje = new String[ciudades.size()];

		for (int i = 0; i < individuo.chromosome().length(); i++) {
			viaje[i] = individuo.get(0).get(i).toString();
			listaAux.add(viaje[i]);
		}

		if(mapa.longitudMaxMapa()<mapa.longitudViaje(viaje)){
			MainGA.evaluateInfoPrint(aux.append("Cromosoma: ").append(listaAux).append(" * Longitud --> ").append("[no hay camino]"));
		}else{
			MainGA.evaluateInfoPrint(aux.append("Cromosoma: ").append(listaAux).append(" * Longitud --> ").append(mapa.longitudViaje(viaje)).append(" Km"));
		}

		return mapa.longitudViaje(viaje);
     }

	public static void main(String[] args) {
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
		if (numGenerationsLimit) {
			result = engine.stream()
					/* La evolución parará cuando llegue a "maxNumGenerations" generaciones */
					.limit(byFixedGeneration(maxNumGenerations))
					/* Actualizamos las estadísticas por cada generación */
					.peek(stats)
					/* Seleccionamos el mejor fenotipo de todos */
					.collect(toBestPhenotype());

		} else {
			result = engine.stream()
					/* La evolución parará cuando llegue a "maxStableGenerations" generaciones estables (iguales) */
					.limit(bySteadyFitness(maxStableGenerations))
					.peek(stats)
					.collect(toBestPhenotype());

		}

		System.out.println(MainGA.toStringResults());
		MainGA.statsInfoPrint("\n\t\t\t\t\t\tEstadísticas de la población \n"+stats);

		if (mapa.obtenerMinimoLong()>=mapa.longitudMaxMapa()){
			System.out.println("\n****************************************************************************************************");
			System.out.println("ERROR! El mapa no ha encontrado un camino correcto! Puede que no tenga solución...");
			System.out.println("Compruebe que el mapa generado tiene un camino completo o aumente en numero de generaciones de este");
			System.out.println("******************************************************************************************************");
		}
	}

    /**
     * Permite mostrar información sobre el método evaluar.
     *
     * @param info - String con la info a mostrar
     */
	private static void statsInfoPrint(String info) {
		if(MainGA.statsInfo){
			System.out.print(info);
            System.out.println();
		}
	}

    /**
     * Permite mostrar información sobre el método evaluar.
     *
     * @param info - String con la info a mostrar
     */
    public static void evaluateInfoPrint(StringBuilder info) {
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
		System.out.println();
		aux.append("Camino mínimo y aptitud mínima: ");
		aux.append(result).append(" puntos.\n");
		aux.append("Longitud del viaje de: ").append(mapa.obtenerMinimoLong()).append("Km.");

		return aux.toString();
	}


}
