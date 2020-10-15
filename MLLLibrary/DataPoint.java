package project1;

public class DataPoint {

	  public double f1;
	  public double f2;
	  public String label;
	  public String type;
	  
	  public DataPoint(){
	    this.f1=0;
	    this.f2=0;
	    this.label=null;
	    this.type=null;
	  }
	  
	  public DataPoint(double f1, double f2, String label, String type){
	    this.f1=f1;
	    this.f2=f2;
	    this.label=label;
	    this.type=type;
	  }
	  
	  public DataPoint(double f1, double f2, String type){
		    this.f1=f1;
		    this.f2=f2;
		    this.type=type;
		  }
	  
	  public double getF1(){
	    return this.f1;
	    }
	  public void setF1(double f1){
	    this.f1=f1;
	    }
	  public double getF2(){
	    return this.f2;
	    }
	  public void setF2(double f2){
	    this.f2=f2;
	    }
	  public String getLabel(){
	    return this.label;
	   }
	  public void setLabel(String label){
	    this.label=label;
	  }
	  public String getType(){
	    return this.type;
	   }
	  public void setType(String type){
	    this.type=type;
	    }
	  public double differanceMath() {
		  return this.f1-this.f2;
	  }
	  public String toString() {
		  return this.type + " "+this.label+ " "+ this.f1 + " "+ this.f2+"\n";
	  }
}
