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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JavaClient {
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    String host = "192.168.1.6";
    int port = 65432;

    TimeSeries series1 = new TimeSeries("Temperatur");
    TimeSeries series2 = new TimeSeries("Luftfugtighed");

    public static void main(String[] args) {
        new JavaClient();
    }

    public JavaClient() {
        SwingUtilities.invokeLater( () -> {
            Graf ex = new Graf();
            ex.setVisible(true);
        });

        try {
            while (true) {
                Socket socket = new Socket(host, port);
                fromServer = new DataInputStream(socket.getInputStream());
                toServer = new DataOutputStream(socket.getOutputStream());

                // Get temperature
                byte[] data = fromServer.readAllBytes();
                fromServer.close();
                String dataStr = new String(data, StandardCharsets.UTF_8);
                String tempStr = dataStr.substring(0, 4);
                String humStr = dataStr.substring(4, 8);
                double temp = Double.parseDouble(tempStr);
                double hum = Double.parseDouble(humStr);
                System.out.println(temp);
                System.out.println(hum);

                Date date = new Date();
                Second second = new Second(date);
                series1.add(second, temp);
                series2.add(second, hum);

                Thread.sleep(2000);
            }
        } catch (IOException | InterruptedException ex1) {
            ex1.printStackTrace();
        }
    }

    class Graf extends JFrame {
        public Graf() {
            initUI();
        }

        private void initUI() {
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
}

