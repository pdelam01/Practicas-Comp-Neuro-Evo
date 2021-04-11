package pdelam.galg;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author pdelam01
 */
public class Graph extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final Color COLOR_FONDO_GRAFICA = new Color(163, 218, 216);
	
	public Graph(ArrayList<Double> apt) {
		this.initPopulation(apt);
	}
	
	public void initPopulation(ArrayList<Double> apt) {
		DefaultCategoryDataset datos = new DefaultCategoryDataset();
		for (int i = 0; i < apt.size(); i++) {
			//1�: valor, 2�: id del grupo de datos, 3�: etiqueta columna del gr�fico
			datos.addValue(apt.get(i), "Aptitudes Totales por Poblaci�n", " "+i);
		}
		
		/* *
		 * 1�: Titulo, 2�: etiqueta nombre abcisas, 3�: etiqueta valor abcisas, 
		 * 4�: dataset, 5�: orientaci�n, y dem�s true-false
		 */
		JFreeChart grafica = ChartFactory.createLineChart("Grafica de aptitudes de la Poblaci�n", "Generaci�n",
				"Aptitud", datos, this.getOrientationV(), true, false, false);

		grafica.setBackgroundPaint(COLOR_FONDO_GRAFICA);
		
		ChartPanel panelChart = new ChartPanel(grafica);
		JFrame ventana = new JFrame("JFreeChart");
		ventana.getContentPane().add(panelChart);
		ventana.pack();
		ventana.setSize(800, 600);
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public PlotOrientation getOrientationV() {
		return PlotOrientation.VERTICAL;
	}
	
	public PlotOrientation getOrientationH() {
		return PlotOrientation.HORIZONTAL;
	}
	
}
