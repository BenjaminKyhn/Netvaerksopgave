import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

class GraphDemo {
    // driver that actually runs the thing
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
            LineChartEx ex = new LineChartEx();
            ex.setVisible(true);
        });
    }
}

class LineChartEx extends JFrame {
    public LineChartEx() {
        initUI();
    }

    private void initUI() {
        //create the series - add some dummy data
        TimeSeries series1 = new TimeSeries("Temperatur");
        TimeSeries series2 = new TimeSeries("Luftfugtighed");

        for (int i = 0; i < 3; i++) {
            Date date = new Date(2020, 9, 1,9,0,i*10);
            Second second = new Second(date);
            series1.add(second, i*1000);
        }

        for (int i = 0; i < 3; i++) {
            Date date = new Date(2020, 9, 1,9,0,i*100);
            Second second = new Second(date);
            series2.add(second, i*10000);
        }

        //create the datasets
        TimeSeriesCollection dataset1 = new TimeSeriesCollection();
        TimeSeriesCollection dataset2 = new TimeSeriesCollection();
        dataset1.addSeries(series1);
        dataset2.addSeries(series2);

        //construct the plot
        XYPlot plot = new XYPlot();
        plot.setDataset(0, dataset1);
        plot.setDataset(1, dataset2);

        //customize the plot with renderers and axis
        plot.setRenderer(0, new XYSplineRenderer());//use default fill paint for first series
        XYSplineRenderer splinerenderer = new XYSplineRenderer();
        splinerenderer.setSeriesFillPaint(0, Color.BLUE);
        plot.setRenderer(1, splinerenderer);
        plot.setRangeAxis(0, new NumberAxis("Grader (°)(°)"));
        plot.setRangeAxis(1, new NumberAxis("Procent (%)"));
        plot.setDomainAxis(new DateAxis("Tid"));

        //Map the data to the appropriate axis
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);

        //generate the chart
        JFreeChart chart = new JFreeChart("Temperatur & Luftfugtighed", getFont(), plot, true);
        chart.setBackgroundPaint(Color.WHITE);

        // NEW PART THAT MAKES IT WORK
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Temperatur & Luftfugtighed");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}