import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.jfree.ui.RefineryUtilities;


public class Graph {
	JFrame frame;
	ArrayList<Double> datatemp = new ArrayList<Double>();
	ArrayList<Date> datadate = new ArrayList<Date>();

	Connection conn;
	Statement stmt;
	ResultSet rs;
	GraphPanel gp;
	DynamicDataDemo1 dynamicdatademo1;
	DynamicDataDemo2 dynamicdatademo2;
	public static int i;

	ActionListener UIUpdateAL = new ActionListener() {
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {

			int mb = 1024*1024;

			//Getting the runtime reference from system
			Runtime runtime = Runtime.getRuntime();

			System.out.println("##### Heap utilization statistics [MB] #####");

			//Print used memory
			System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);

			//Print free memory
			System.out.println("Free Memory:" + runtime.freeMemory() / mb);

			try {
				rs = stmt.executeQuery("SELECT * FROM core_temp_tracker.CoreTemps;");

				while(rs.next()){
					datatemp.add(rs.getDouble("Temp"));
					datadate.add(rs.getDate("Time"));
				}


			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	


			dynamicdatademo1.addValue(datatemp.get(datatemp.size()-1));
			dynamicdatademo2.addValue(datatemp.get(datatemp.size()-1));
			System.out.println("added");

		}
	};
	static int UIUpdateInterval = 5000;

	Timer UIupdate = new Timer(UIUpdateInterval, UIUpdateAL);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Graph();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Graph() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		System.out.println("Start Loading");
		
		dynamicdatademo1 = new DynamicDataDemo1("Dynamic Data Demo");
		dynamicdatademo1.pack();
		RefineryUtilities.positionFrameOnScreen(dynamicdatademo1, 0, 100);
		dynamicdatademo1.setVisible(true);



		dynamicdatademo2 = new DynamicDataDemo2("Dynamic Data Demo");
		dynamicdatademo2.pack();
		RefineryUtilities.positionFrameOnScreen(dynamicdatademo2, 0, 0);
		dynamicdatademo2.setVisible(true);

		try {
			conn = DriverManager.getConnection("jdbc:mysql://64.151.8.247:3306", "admin02", "Qzecwxad1");
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("End Loading");


		UIupdate.start();
	}
}