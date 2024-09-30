package monitoring;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JFrame;
import java.util.List;

public class MultiTimeSeriesPlotter extends JFrame {

    public MultiTimeSeriesPlotter(String title, List<List<Double>> seriesData, List<String> seriesNames) {
        super(title);

        // Create dataset
        XYSeriesCollection dataset = createDataset(seriesData, seriesNames);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Multi Time Series Plot",
                "Time",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Customize plot
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        // Create Panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    // Creates the dataset for the plotter
    private XYSeriesCollection createDataset(List<List<Double>> seriesData, List<String> seriesNames) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < seriesData.size(); i++) {
            XYSeries series = new XYSeries(seriesNames.get(i));
            List<Double> data = seriesData.get(i);
            for (int j = 0; j < data.size(); j++) {
                series.add(j, data.get(j));
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    public static void main(String[] args) {
        // Sample data
        List<Double> series1 = List.of(1.0, 4.0, 3.0, 5.0, 4.5);
        List<Double> series2 = List.of(2.0, 3.0, 2.5, 6.0, 7.0);
        List<List<Double>> seriesData = List.of(series1, series2);
        List<String> seriesNames = List.of("Series 1", "Series 2");

        // Create and display the plot
        MultiTimeSeriesPlotter example = new MultiTimeSeriesPlotter("Multi Time Series Chart", seriesData, seriesNames);
        example.setSize(800, 600);
        example.setLocationRelativeTo(null);
        example.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        example.setVisible(true);
    }
}
