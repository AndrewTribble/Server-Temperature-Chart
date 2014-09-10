
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

@SuppressWarnings("all")
public class DynamicDataDemo1 extends ApplicationFrame
{
	public static JButton jbutton = new JButton("Add New Data Item");
	public static double addVal = 0;
	static JFreeChart jfreechart;
	static XYPlot xyplot;
	static ValueAxis valueaxis;
	private static String mode = "run";
	private static double minAxis = 15D, maxAxis = 90D;
	private static TimeSeries series;
	static TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(series);
	static JLabel lbl = new JLabel("Current Temperature: ");

	static class DemoPanel extends JPanel
	implements ActionListener
	{


		private double lastValue;

		private JFreeChart createChart(XYDataset xydataset)
		{
			jfreechart = ChartFactory.createTimeSeriesChart("Core Temperature", "Time", "Value", xydataset, true, true, false);
			xyplot = (XYPlot)jfreechart.getPlot();
			valueaxis = xyplot.getDomainAxis();
			valueaxis.setAutoRange(true);
			valueaxis.setFixedAutoRange(85000000D);
			valueaxis = xyplot.getRangeAxis();
			valueaxis.setRange(minAxis, maxAxis);
			return jfreechart;
		}
		public void actionPerformed(ActionEvent actionevent)
		{
			if (actionevent.getActionCommand().equals("ADD_DATA"))
			{
				series.add(new Millisecond(), addVal);
			}
		}


		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW

		private static TimeSeriesCollection createDataset()
		{
			TimeSeries timeseries = new TimeSeries("Per Minute Data", org.jfree.data.time.Millisecond.class);

			try
			{
				Statement stmt;
				ResultSet resultset;

				// Register the JDBC driver for MySQL.
				Class.forName("com.mysql.jdbc.Driver");

				// Define URL of database server for
				// database named mysql on the localhost
				// with the default port number 3306.
				String url = "jdbc:mysql://64.151.8.247:3306";

				// Get a connection to the database for a
				// user named root with a blank password.
				// This user is the default administrator
				// having full privileges to do anything.
				Connection con = DriverManager.getConnection(url, "admin02", "Qzecwxad1");

				// Display URL and connection information
				System.out.println("URL: " + url);
				System.out.println("Connection: " + con);

				// Get a Statement object
				stmt = con.createStatement();
				int count = 0;
				resultset = stmt.executeQuery("SELECT * FROM core_temp_tracker.CoreTemps;");
				resultset.first();
				SimpleDateFormat standardDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// (Define your formatter only once, then reuse)

				java.util.Date myDate;
				// (you may want to catch a ParseException)

				do
				{
					System.out.println(Graph.i);
					Graph.i++;
					String time = resultset.getString(1);
					myDate = standardDateFormat.parse(resultset.getString(1));
					//System.out.println(new Day(myDate));
					String[] datas = time.split(" ");
					datas = datas[1].split(":");
					int hourV = Integer.parseInt(datas[0]);
					int minV = Integer.parseInt(datas[1]);
					int index = datas[2].indexOf(".");
					int secV = Integer.parseInt(datas[2].substring(0, index));
					int milsecV = Integer.parseInt(datas[2].substring(index + 1));
					count++;
					double value = resultset.getDouble(2);
					timeseries.add(new Millisecond(myDate), value);				
				}
				while (resultset.next());

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeseries);
			return timeseriescollection;
		}

		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
		//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW



		public DemoPanel()
		{
			super(new BorderLayout());
			lastValue = 100D;
			series = new TimeSeries("Ubuntu00001", DynamicDataDemo1.class$org$jfree$data$time$Millisecond != null ? DynamicDataDemo1.class$org$jfree$data$time$Millisecond : (DynamicDataDemo1.class$org$jfree$data$time$Millisecond = DynamicDataDemo1.class$("org.jfree.data.time.Millisecond")));

			timeseriescollection = createDataset();
			ChartPanel chartpanel = new ChartPanel(createChart(timeseriescollection));
			chartpanel.setPreferredSize(new Dimension(1000, 270));
			JPanel jpanel = new JPanel();
			jpanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			add(chartpanel);
			lbl = new JLabel("Current Temperature: " + timeseriescollection.getSeries(0).getValue(timeseriescollection.getSeries(0).getItemCount()-1));

			jpanel.add(lbl);
			add(jpanel, "South");
		}
	}

	static Class class$org$jfree$data$time$Millisecond; /* synthetic field */

	public DynamicDataDemo1(String s)
	{
		super(s);
		DemoPanel demopanel = new DemoPanel();
		setContentPane(demopanel);
	}

	public static JPanel createDemoPanel()
	{
		return new DemoPanel();
	}

	public static void main(String args[])
	{
		DynamicDataDemo1 dynamicdatademo1 = new DynamicDataDemo1("Dynamic Data Demo");
		dynamicdatademo1.pack();
		RefineryUtilities.centerFrameOnScreen(dynamicdatademo1);
		dynamicdatademo1.setVisible(true);
	}

	public static void addValue(double val){
		series.clear();
		timeseriescollection.getSeries(0).add(new Millisecond(), val);
		lbl.setText("Current Temperature: " + timeseriescollection.getSeries(0).getValue(timeseriescollection.getSeries(0).getItemCount()-1));

	}

	static Class class$(String s)
	{
		Class  clazz=null;
		try {
			clazz= Class.forName(s);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
}