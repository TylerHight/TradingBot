package monitoring;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.List;

public class FourierPlotter {

    public static void plotFourierTransform(List<Double[]> result, String title) {
        // Initialize dataset for the chart
        XYSeries series = new XYSeries("Frequency vs Magnitude");

        // Add the values to the chart dataset
        for (Double[] pair : result) {
            double frequency = pair[0];
            double magnitude = pair[1];
            series.add(frequency, magnitude);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,           // Title
                "Frequency (Hz)", // X-Axis label
                "Magnitude",      // Y-Axis label
                dataset,          // Data
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Display the chart in a JFrame
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}