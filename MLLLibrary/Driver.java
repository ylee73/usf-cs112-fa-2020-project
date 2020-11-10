package project1;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Driver {
	// Scanner
	private static List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}
	
	
	
	public static void main(String arg[]) {
		
		//Running the JFrame
		SwingUtilities.invokeLater(
			new Runnable() { public void run() { initAndShowGUI(); } }        
			);}

	private static void initAndShowGUI() {
		JFrame myFrame = new JFrame("Titanic: Accuracy and Percision");
		myFrame.pack();
		myFrame.setVisible(true);
		myFrame.setBounds(300, 200, 700, 400);

		ArrayList<DataPoint> data = new ArrayList<DataPoint>();
		
		try (Scanner scanner = new Scanner(
				new File("/Users/ashleylee/eclipseCS112/project1/src/project1/titanic.csv"));) {
			while (scanner.hasNextLine()) {
			List<String> records = getRecordFromLine(scanner.nextLine());
			
			try {
				Double fare = Double.valueOf(records.get(records.size() - 1));
				Double age = Double.valueOf(records.get(records.size() - 2));
				String label = records.get(records.size() - 6);
				
				
				
				Random rand = new Random();
				double randNum = rand.nextDouble();
				// 90% of the data is reserved for training
				if (randNum < 0.9) {
					DataPoint dp = new DataPoint(age, fare, label, "train");
					data.add(dp);
				} else {
					DataPoint dp = new DataPoint(age, fare, label, "test");
					data.add(dp);
					}
				}
		
			catch(NumberFormatException e) {
				
					System.out.println("Something wrong with string");
			}		
		}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return ;
		}
	
		System.out.println("sd");
		KNNModel KNNModel = new KNNModel(3);
		KNNModel.train(data);
		KNNModel.test(data);
		
		double Accuracy = KNNModel.getAccuracy(data);
		double Precision = KNNModel.getPrecision(data);
		System.out.println("Accuracy" + Accuracy);
		System.out.println("Precision" + Precision);

		String label1 = "Accuracy:" + Accuracy;
		String label2 = "Precision:" + Precision;
		
		System.out.println(label1);


		JLabel myText1 = new JLabel(label1);
		JLabel myText2 = new JLabel(label2);
		Container contentPane = myFrame.getContentPane();
		contentPane.setLayout(new GridLayout(2, 2));
		myFrame.getContentPane().add(new JButton(label1));
		myFrame.getContentPane().add(myText1, BorderLayout.CENTER);
		myFrame.getContentPane().add(myText2, BorderLayout.CENTER);
	}

}
