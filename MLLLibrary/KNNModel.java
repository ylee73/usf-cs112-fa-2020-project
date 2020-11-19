package project1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.lang.Math;
import java.io.File;
import java.util.Scanner;

public class KNNModel extends Model {

	private int passnumb1 = 0;
	private int passnumb0 = 0;
	private int K;
	private ArrayList<DataPoint> trainSet;


	public KNNModel(int K) {
		this.K = K;
		this.trainSet = new ArrayList<DataPoint>();
	}

	private void trainSet(ArrayList<DataPoint> data) {
		ArrayList<DataPoint> trainset[];
	}

	double distance = 0;

	private double getDistance(DataPoint p1, DataPoint p2) {
		double p1f1 = p1.getF1();
		double p1f2 = p1.getF2();
		double p2f1 = p2.getF1();
		double p2f2 = p2.getF2();
		distance = Math.sqrt((p2f2 - p1f2) * (p2f2 - p1f2) + (p2f1 - p1f1) * (p2f1 - p1f1));
		return distance;
	}

//e
	void train(ArrayList<DataPoint> data) {

		for (int i = 0; i < data.size(); i++) {
			DataPoint datatrain = data.get(i);
			if (datatrain.getType().equals("train")) {
				if (datatrain.getLabel().equals("0")) {
					passnumb0 = passnumb0 + 1;
				}
				if (datatrain.getLabel().equals("1")) {
					passnumb1 = passnumb1 + 1;
				}
				trainSet.add(datatrain);
			}
		}
	}

//f
	public String test(ArrayList<DataPoint> data) {
		DataPoint datatest = data.get(0);
		Double[][] array = new Double[trainSet.size()][2];
		for (int j=0; j<data.size(); j++) {
			DataPoint findtest = data.get(j);
			if (findtest.getType().equals("test")) {
				datatest = data.get(j);
			}
		}
		
		for (int i = 0; i < array.length; i++) {
			array[i][0] = getDistance(datatest, trainSet.get(i));
			array[i][1] = Double.parseDouble(trainSet.get(i).getLabel());
		}

		Arrays.sort(array, new Comparator<Double[]>() {
			public int compare(Double[] a, Double[] b) {
				return a[0].compareTo(b[0]);
			}
		});
		// v.
		int count1 = 0;
		int count0 = 0;
		for (int i = 0; i < K; i++) {
			if (array[i][1].equals(0.0)) {
				count0++;
			} else {
				count1++;
			}
		}
		//System.out.println("c1"+count1);
		//System.out.println("c0"+count0);
		if (count1>count0){
			return "1";
		}
		else {
			return "0";
		}


	}
	
	public String test(DataPoint data) {
		DataPoint datatest = data;
		Double[][] array = new Double[trainSet.size()][2];
		
		
		
		/*for (int j=0; j<trainSet.size(); j++) {
			DataPoint findtest = trainSet.get(j);
			if (findtest.getType().equals("test")) {
				datatest = trainSet.get(j);
			}
		}*/
		
		for (int i = 0; i < trainSet.size(); i++) {
			array[i][0] = getDistance(datatest, trainSet.get(i));
			array[i][1] = Double.parseDouble(trainSet.get(i).getLabel());
		}

		Arrays.sort(array, new Comparator<Double[]>() {
			public int compare(Double[] a, Double[] b) {
				return a[0].compareTo(b[0]);
			}
		});
		// v.
		int count1 = 0;
		int count0 = 0;
		for (int i = 0; i < K; i++) {
			if (array[i][1].equals(0.0)) {
				count0++;
			} else {
				count1++;
			}
		}
		//System.out.println("c1"+count1);
		//System.out.println("c0"+count0);
		if (count1>count0){
			return "1";
		}
		else {
			return "0";
		}


	}
	
	@Override
	Double getAccuracy(ArrayList<DataPoint> data) {
		double truePositive = 0; 
		double falsePositive = 0;
		double trueNegative = 0;
		double falseNegative = 0;
		for (int i=0; i<data.size(); i++) {
			DataPoint dataAcc = data.get(i);
			String result = test(data);
			
			if (result.equals("1") && dataAcc.getLabel().equals("1")) {
				truePositive ++;
			}
			if (result.equals("1") && dataAcc.getLabel().equals("0")) {
				falsePositive ++; 
			}
			if (result.equals("0") && dataAcc.getLabel().equals("1")) {
				falseNegative++; 
			}
			if (result.equals("0") && dataAcc.getLabel().equals("0")) {
				trueNegative++;
			}
		}
		return (truePositive + trueNegative)/(truePositive + trueNegative + falsePositive + falseNegative);
	}
		

	@Override
	Double getPrecision(ArrayList<DataPoint> data) {
		double truePositive = 0; 
		double falsePositive = 0;
		double trueNegative = 0;
		double falseNegative = 0;
		for (int i=0; i<data.size(); i++) {
			DataPoint dataAcc = data.get(i);
			String result = test(data);
			if (result.equals("1") && dataAcc.getLabel().equals("1")) {
				truePositive ++;
			}
			if (result.equals("1") && dataAcc.getLabel().equals("0")) {
				falsePositive ++; 
			}
			if (result.equals("0") && dataAcc.getLabel().equals("1")) {
				falseNegative++; 
			}
			if (result.equals("0") && dataAcc.getLabel().equals("0")) {
				trueNegative++;
			}
		}
		System.out.println("tP" +truePositive);
		System.out.println("fn"+falseNegative);
		System.out.println("tn"+trueNegative);
		System.out.println("fp"+falsePositive);
		return truePositive/(truePositive + falseNegative);
	}
	

}
