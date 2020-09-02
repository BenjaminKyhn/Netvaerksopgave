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
import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaClient {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private String host = "192.168.1.6";
    private int port = 65432;
    private TimeSeries series1 = new TimeSeries("Temperatur");
    private TimeSeries series2 = new TimeSeries("Luftfugtighed");

    public static void main(String[] args) {
        new JavaClient();
    }

    public JavaClient() {
        SwingUtilities.invokeLater(() -> {
            Graf ex = new Graf();
            ex.setVisible(true);
        });

        try {
            while (true) {
                Socket socket = new Socket(host, port);
                fromServer = new DataInputStream(socket.getInputStream());
                toServer = new DataOutputStream(socket.getOutputStream());

                // Hent data fra serveren
                byte[] data = fromServer.readAllBytes();
                fromServer.close();

                // Formater data
                String dataStr = new String(data, StandardCharsets.UTF_8);
                String tempStr = dataStr.substring(0, 4);
                String humStr = dataStr.substring(4, 8);
                String timeStr = dataStr.substring(8);
                String[] time = timeStr.split(":");
                double temp = Double.parseDouble(tempStr);
                double hum = Double.parseDouble(humStr);
                int hours = Integer.parseInt(time[0]);
                int mins = Integer.parseInt(time[1]);
                int secs = Integer.parseInt(time[2]);

                // Print data til konsollen
                System.out.println(temp);
                System.out.println(hum);
                for (int i = 0; i < time.length; i++) {
                    System.out.println(time[i]);
                }

                // Tilføj punktet til grafen
                Date date = new Date();
                date.setHours(hours);
                date.setMinutes(mins);
                date.setSeconds(secs);

                Second second = new Second(date);
                series1.add(second, temp);
                series2.add(second, hum);

                // Sleep tråden i en halv time
                Thread.sleep(1800000);
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
            // Opret datasæt
            TimeSeriesCollection dataset1 = new TimeSeriesCollection();
            TimeSeriesCollection dataset2 = new TimeSeriesCollection();
            dataset1.addSeries(series1);
            dataset2.addSeries(series2);

            // Lav kurven
            XYPlot plot = new XYPlot();
            plot.setDataset(0, dataset1);
            plot.setDataset(1, dataset2);

            plot.setRenderer(0, new XYSplineRenderer());//use default fill paint for first series
            XYSplineRenderer splinerenderer = new XYSplineRenderer();
            splinerenderer.setSeriesFillPaint(0, Color.BLUE);
            plot.setRenderer(1, splinerenderer);

            // Lav venstre Y-akse
            NumberAxis degreeAxis = new NumberAxis("Grader (°)");
            degreeAxis.setRange(0, 30);
            plot.setRangeAxis(0, degreeAxis);

            // Lav højre Y-akse
            NumberAxis percentAxis = new NumberAxis("Procent (%)");
            percentAxis.setRange(0, 100);
            plot.setRangeAxis(1, percentAxis);

            DateAxis timeAxis = new DateAxis("Tid");
            timeAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm"));
            plot.setDomainAxis(timeAxis);

            // Forbind plot med den rigtige Y-akse
            plot.mapDatasetToRangeAxis(0, 0);
            plot.mapDatasetToRangeAxis(1, 1);

            JFreeChart chart = new JFreeChart("Temperatur & Luftfugtighed", getFont(), plot, true);
            chart.setBackgroundPaint(Color.WHITE);

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

