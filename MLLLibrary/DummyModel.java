package project1;

import java.util.ArrayList;

public class DummyModel extends Model{

	@Override
	void train(ArrayList<DataPoint> data) {
		double sum=0.0;
		for (int i=0; i< data.size(); i++) {
			sum= sum + data.get(i).differanceMath();
		}
		double average=0.0;
		average=sum/data.size();
		System.out.println(average);
	}

	@Override
	String test(ArrayList<DataPoint> data) {
		return "DONE!";
	}

	@Override
	Double getAccuracy(ArrayList<DataPoint> data) {
		return 1.0;
	}

	@Override
	Double getPrecision(ArrayList<DataPoint> data) {
		return 1.0;
	}

}
