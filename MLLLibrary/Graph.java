package project1;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Graph extends JPanel {
	
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

    private static final long serialVersionUID = 1L;
    private int labelPadding = 40;
    private Color lineColor = new Color(255, 255, 254);

    // TODO: Add point colors for each type of data point
    private Color pointColor = new Color(255, 0, 255);

    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);

    // TODO: Change point width as needed
    private static int pointWidth = 10;

    // Number of grids and the padding width
    private int numXGridLines = 6;
    private int numYGridLines = 6;
    private int padding = 40;

    private List<DataPoint> data;
    
    public double truePositive = 0; 
	public double falsePositive = 0;
	public double trueNegative = 0;
	public double falseNegative = 0;

    // TODO: Add a private KNNModel variable
    private static KNNModel KNNModel;//o
    
    	
	/**
	 * Constructor method
	 */
    public Graph(List<DataPoint> testData, List<DataPoint> trainData) {
        this.data = testData;
        // TODO: instantiate a KNNModel variable //o
        KNNModel = new KNNModel(4);
        // TODO: Run train with the trainData
        KNNModel.train((ArrayList<DataPoint>) trainData);
    }
    
    public KNNModel getModel () {
    	return this.KNNModel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double minF1 = getMinF1Data();
        double maxF1 = getMaxF1Data();
        double minF2 = getMinF2Data();
        double maxF2 = getMaxF2Data();

        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - 
        		labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLUE);

        double yGridRatio = (maxF2 - minF2) / numYGridLines;
        for (int i = 0; i < numYGridLines + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 -
            		labelPadding)) / numYGridLines + padding + labelPadding);
            int y1 = y0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = String.format("%.2f", (minF2 + (i * yGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 6, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        double xGridRatio = (maxF1 - minF1) / numXGridLines;
        for (int i = 0; i < numXGridLines + 1; i++) {
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;
            int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numXGridLines) + padding + labelPadding;
            int x1 = x0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                g2.setColor(Color.BLACK);
                String xLabel = String.format("%.2f", (minF1 + (i * xGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // Draw the main axis
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() -
        		padding, getHeight() - padding - labelPadding);

        // Draw the points
        paintPoints(g2, minF1, maxF1, minF2, maxF2);
    }

    private void paintPoints(Graphics2D g2, double minF1, double maxF1, double minF2, double maxF2) {
        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        double xScale = ((double) getWidth() - (3 * padding) - labelPadding) /(maxF1 - minF1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxF2 - minF2);
        g2.setStroke(oldStroke);
        for (int i = 0; i < data.size(); i++) {
            int x1 = (int) ((data.get(i).getF1() - minF1) * xScale + padding + labelPadding);
            int y1 = (int) ((maxF2 - data.get(i).getF2()) * yScale + padding);
            int x = x1 - pointWidth / 2;
            int y = y1 - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;

            // TODO: Depending on the type of data and how it is tested, change color here.
            // You need to test your data here using the model to obtain the test value 
            // and compare against the true label.
            DataPoint datapoint = data.get(i);
            String result = KNNModel.test(datapoint);
            if (result.equals("1") && datapoint.getLabel().equals("1")) {
            	pointColor = new Color(0,0,255);
            	truePositive++;
            	System.out.println(truePositive);
            }
            if (result.equals("1") && datapoint.getLabel().equals("0")) {
            	pointColor = new Color(0,255,255);
            	falsePositive ++; 
            }
            if (result.equals("0") && datapoint.getLabel().equals("1")) {
            	pointColor = new Color(255,255,0);
            	falseNegative++;
            }
            if (result.equals("0") && datapoint.getLabel().equals("0")) {
            	pointColor = new Color(255,0,0);
            	trueNegative++;
            }
            
            g2.setColor(pointColor);
            g2.fillOval(x, y, ovalW, ovalH);
        }

    }

    /*
     * @Return the min values
     */
    private double getMinF1Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF1());
        }
        return minData;
    }

    private double getMinF2Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF2());
        }
        return minData;
    }


    /*
     * @Return the max values;
     */
    private double getMaxF1Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF1());
        }
        return maxData;
    }

    private double getMaxF2Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF2());
        }
        return maxData;
    }

    /* Mutator */
    public void setData(List<DataPoint> data) {
        this.data = data;
        invalidate();
        this.repaint();
    }

    /* Accessor */
    public List<DataPoint> getData() {
        return data;
    }
    
    
    public double getAccuracy() {
    	double truePositive = 0; 
    	double falsePositive = 0;
    	double trueNegative = 0;
    	double falseNegative = 0;
    	for (int i = 0; i < data.size(); i++) {
    		DataPoint datapoint = data.get(i);
    		String result = KNNModel.test(datapoint);
    		if (result.equals("1") && datapoint.getLabel().equals("1")) {	
    			truePositive++;
    		}
    		if (result.equals("1") && datapoint.getLabel().equals("0")) {
    			falsePositive ++; 
    		}
    		if (result.equals("0") && datapoint.getLabel().equals("1")) {
    			falseNegative++;
    		}
    		if (result.equals("0") && datapoint.getLabel().equals("0")) {
    			trueNegative++;
    		}
    	}
    	return (truePositive + trueNegative)/(truePositive + trueNegative + falsePositive + falseNegative);
    }
    
    public double getPrecision() {
    	double truePositive = 0; 
    	double falsePositive = 0;
    	double trueNegative = 0;
    	double falseNegative = 0;
    	for (int i = 0; i < data.size(); i++) {
    		DataPoint datapoint = data.get(i);
    		String result = KNNModel.test(datapoint);
    		if (result.equals("1") && datapoint.getLabel().equals("1")) {	
    			truePositive++;
    		}
    		if (result.equals("1") && datapoint.getLabel().equals("0")) {
    			falsePositive ++; 
    		}
    		if (result.equals("0") && datapoint.getLabel().equals("1")) {
    			falseNegative++;
    		}
    		if (result.equals("0") && datapoint.getLabel().equals("0")) {
    			trueNegative++;
    		}
    	}
    	return truePositive/(truePositive + falseNegative);
    }

    /*  Run createAndShowGui in the main method, where we create the frame too and pack it in the panel*/
    private static void createAndShowGui(List<DataPoint> testData, List<DataPoint> trainData) {


	    /* Main panel */
        Graph mainPanel = new Graph(testData, trainData);

        // Feel free to change the size of the panel
        mainPanel.setPreferredSize(new Dimension(500, 400));

        /* creating the frame */
        JFrame frame = new JFrame("CS 112 Lab Part 4");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height *2/3;
        int width = screenSize.width * 1/3;
        frame.setPreferredSize(new Dimension(width,height));
    
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /* Create Slider*/
        JSlider slider = new JSlider(2,25,4);
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        
        JLabel label = new JLabel ("Choose the majority value:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton button = new JButton ("Run Test");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new FlowLayout());
        
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        frame.getContentPane().add(label);
        frame.getContentPane().add(slider);
        frame.getContentPane().add(button);
        
        double Accuracy = mainPanel.getAccuracy();
		double Precision = mainPanel.getPrecision();


		String label1 = "Accuracy:" + Accuracy;
		String label2 = "Precision:" + Precision;


		JLabel myText1 = new JLabel(label1);
		JLabel myText2 = new JLabel(label2);

	
		frame.getContentPane().add(myText1);
		frame.getContentPane().add(myText2);
        
        
       button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		int value = slider.getValue() *2 +1;
        		KNNModel = new KNNModel(value);
            	KNNModel.train((ArrayList<DataPoint>) trainData);
            	double Accuracy = mainPanel.getAccuracy();
         		double Precision = mainPanel.getPrecision();
         		String label1 = "Accuracy:" + Accuracy;
        		String label2 = "Precision:" + Precision;
        		
        		myText1.setText(label1);
        		myText2.setText(label2);
        	

        	}
        });
   
        slider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		JSlider source =(JSlider)e.getSource();
 
        	}
        });
    
    }
    
   
    
      
    /* The main method runs createAndShowGui*/
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // Generate random data point
        	 
            List<DataPoint> data = new ArrayList<>();
            List<DataPoint> testData = new ArrayList<>();
            List<DataPoint> trainData = new ArrayList<>();
            
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
    					trainData.add(dp);
    				} else {
    					DataPoint dp = new DataPoint(age, fare, label, "test");
    					data.add(dp);
    					testData.add(dp);
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
            Random random = new Random();
            int maxDataPoints = 20;
            // Max value of f1 and f2 that is arbitrarily set
            int maxF1 = 8;
            int maxF2 = 300;

            // Generates random DataPoints
            for (int i = 0; i < maxDataPoints; i++) {
                double f1 = (random.nextDouble() * maxF1);
                double f2 = (random.nextDouble() * maxF2);
                data.add(new DataPoint(f1, f2, "Random1", "Random"));
            }
            // TODO: Change the above logic retrieve the data from titanic.csv
            // Split the data to test and training o
            
            // TODO: Pass in the testData and trainData separately 
            // Be careful with the order of the variables. --> don't get 
            createAndShowGui(testData, trainData);
            
            
         }
      });
    }
}
