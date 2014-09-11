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
	public static ArrayList<Double> datatemp = new ArrayList<Double>();
	public static ArrayList<Date> datadate = new ArrayList<Date>();
	public static LoadingPanel loading = new LoadingPanel();
	public static Connection conn;
	public static Statement stmt;
	public static ResultSet rs;
	GraphPanel gp;
	public static DynamicDataDemo1 dynamicdatademo1;
	public static DynamicDataDemo2 dynamicdatademo2;
	public static int i;
	
	public static Thread t = new MyThread1();
	public static Thread t2 = new MyThread2();

	static ActionListener UIUpdateAL = new ActionListener() {
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {
			System.out.println("##### Heap utilization statistics [MB] #####");

			int mb = 1024*1024;

			//Getting the runtime reference from system
			Runtime runtime = Runtime.getRuntime();


			//Print used memory
			System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);

			//Print free memory
			System.out.println("Free Memory:" + runtime.freeMemory() / mb);

			try {
				rs = stmt.executeQuery("SELECT * FROM core_temp_tracker.CoreTemps;");

				rs.last();
				
				dynamicdatademo1.addValue(rs.getDouble("Temp"));
				dynamicdatademo2.addValue(rs.getDouble("Temp"));



			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	



			System.out.println("added");

		}
	};
	static int UIUpdateInterval = 5000;

	public static Timer UIupdate = new Timer(UIUpdateInterval, UIUpdateAL);

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
		t.start();
		t2.start();
	}
}


class MyThread1 extends Thread {
    public void run(){

       Graph.loading.setVisible(true);
       Graph.loading.repaint();
    }
  }

class MyThread2 extends Thread {
    public void run(){

		System.out.println("Start Loading");
		
		Graph.dynamicdatademo1 = new DynamicDataDemo1("Dynamic Data Demo");
		Graph.dynamicdatademo1.pack();
		RefineryUtilities.positionFrameOnScreen(Graph.dynamicdatademo1, 0, 100);

		Graph.dynamicdatademo2 = new DynamicDataDemo2("Dynamic Data Demo");
		Graph.dynamicdatademo2.pack();
		RefineryUtilities.positionFrameOnScreen(Graph.dynamicdatademo2, 0, 0);


		try {
			Graph.conn = DriverManager.getConnection("jdbc:mysql://64.151.8.247:3306", "admin02", "Qzecwxad1");
			Graph.stmt = Graph.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("End Loading");


		
		Graph.dynamicdatademo1.setVisible(true);
		Graph.dynamicdatademo2.setVisible(true);
		Graph.loading.setVisible(false);

		Graph.UIupdate.start();
		Graph.t.stop();
		Graph.t2.stop();
    }
  }
