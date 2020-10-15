package project1;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;
public class Driver {
	public static void main(String arg[]) {
		//Place to test before running under JFrame
		
		
	//Running the JFrame
	SwingUtilities.invokeLater(
		new Runnable() { public void run() { initAndShowGUI(); } }        
		);
	}
//Create JFrame 
	private static void initAndShowGUI() {
		JFrame myFrame = new JFrame("Dummy Model: Accuracy and Percision");
		myFrame.setVisible(true);
		myFrame.setBounds(300,200,700,400);
			
		Random rand= new Random();

		//Create Random double for f1 and f2 in trainingData
		ArrayList<DataPoint> trainingData = new ArrayList<>();
		//public DataPoint(double f1, double f2, String label, String type){
		for (int i=0; i<5; i++) {
			double f1 = rand.nextDouble();
			double f2 = rand.nextDouble();
				
			String label = "label_"+i;
			String type = "type_"+i;
				
			DataPoint tempPoint = new DataPoint(f1,f2,label,type);
			trainingData.add( tempPoint);
		}
		//Create Set double for f1 and f2 in testData
		ArrayList<DataPoint> testData = new ArrayList<>();
		for (int i=0; i<5; i++) {
			double f1 = i+0.5;
			double f2 = i+0.3;
				
			String label = "label_"+i;
			String type = "type_"+i;
				
			DataPoint tempPoint = new DataPoint(f1, f2, label,type);
			testData.add(tempPoint);
		}
		
		//Test DummyModel	
		DummyModel dModel = new DummyModel();
		dModel.train(trainingData);
		System.out.println("test:"+ dModel.test(testData));
			
		//Get Accuracy of Dummy
		double Accuracy = dModel.getAccuracy(trainingData);
		String label1 = "Accuracy:"+Accuracy;
		System.out.println(label1);
			
		//Get Precision of Dummy
		double Precision = dModel.getPrecision(trainingData);
		String label2 = "Precision:"+ Precision;
		
		
		JLabel myText1 = new JLabel(label1);
		JLabel myText2 = new JLabel(label2);
		Container contentPane = myFrame.getContentPane();
		contentPane.setLayout(new GridLayout(2,2));
		myFrame.getContentPane().add(myText1, BorderLayout.CENTER);
		myFrame.getContentPane().add(myText2, BorderLayout.CENTER);	
	}
		}

