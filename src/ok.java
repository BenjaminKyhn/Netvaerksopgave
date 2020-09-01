import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class ok {
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
        TimeSeries series1 = new TimeSeries("series1");
        TimeSeries series2 = new TimeSeries("series2");
        series1.add(new Second(new Date(2020, 9, 1,9,0,0)), 1000);
        series1.add(new Second(new Date(2020, 9, 1,9,0,5)), 1150);
        series1.add(new Second(new Date(2020, 9, 1,9,0,10)), 1250);

        series2.add(new Second(new Date(2020, 9, 1,9,0,0)), 111250);
        series2.add(new Second(new Date(2020, 9, 1,9,0,5)), 211250);
        series2.add(new Second(new Date(2020, 9, 1,9,0,10)), 311250);

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
        plot.setRangeAxis(0, new NumberAxis("Series 1"));
        plot.setRangeAxis(1, new NumberAxis("Series 2"));

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));


        plot.setDomainAxis(axis);

        //Map the data to the appropriate axis
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);

        //generate the chart
        JFreeChart chart = new JFreeChart("MyPlot", getFont(), plot, true);
        chart.setBackgroundPaint(Color.WHITE);
        //JPanel jpanel = new ChartPanel(chart);


        // NEW PART THAT MAKES IT WORK
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}