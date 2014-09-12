import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.jfree.ui.RefineryUtilities;


public class Graph {
	JFrame frame;
	public static LoadingPanel loading = new LoadingPanel();
	public static Connection conn;
	public static Statement stmt;
	public static ResultSet rs;
	GraphPanel gp;
	public static DynamicDataDemo1 dynamicdatademo1;
	public static DynamicDataDemo2 dynamicdatademo2;

	public static Thread t = new MyThread1();
	public static Thread t2 = new MyThread2();

	static ActionListener UIUpdateAL = new ActionListener() {
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {
			try {
				rs = stmt.executeQuery("SELECT * FROM core_temp_tracker.CoreTemps WHERE time > DATE_SUB(NOW(), INTERVAL 20 Second)");
				rs.last();
				dynamicdatademo1.addValue(rs.getDouble("Temp"));
				dynamicdatademo2.addValue(rs.getDouble("Temp"));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();  
			}	
		}
	};
	static int UIUpdateInterval = 2000;

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
	@SuppressWarnings("deprecation")
	public void run(){

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

		Graph.dynamicdatademo1.setVisible(true);
		Graph.dynamicdatademo2.setVisible(true);
		Graph.loading.setVisible(false);

		Graph.UIupdate.start();
		Graph.t.stop();
		Graph.t2.stop();
	}
}
