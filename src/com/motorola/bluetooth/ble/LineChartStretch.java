package com.motorola.bluetooth.ble;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Message;

public class LineChartStretch {
	  private GraphicalView view;
	  private Timer timer = new Timer();  
	  private TimerTask task;  
	  private String title = "Stretch Sensor"; 
	  public static final int slength = 100;
	  private TimeSeries series1;  
	  private XYMultipleSeriesDataset mDataset;   
	  private XYMultipleSeriesRenderer renderer;  
	  
	  private Context context;  
	  private int addX = -1, addY1;  
	  private java.util.Date datesec;
	    
	  int[] h1 = new int[slength];  
	  int[] v1 = new int[slength]; 
	  
	public LineChartStretch() {
		//get the MultipleSeriesDataset
		series1 = new TimeSeries("axis_x");

		mDataset = new XYMultipleSeriesDataset(); 
		mDataset.addSeries(series1);

		//get the MultipleSeriesRenderer
		renderer = buildRenderer(Color.rgb(255, 255, 255), PointStyle.CIRCLE, true);//white 
		setChartSettings(renderer, "time", "acc", 0, 100, 0, 100, Color.WHITE, Color.WHITE);  
	  
	}
	
	 protected XYMultipleSeriesRenderer buildRenderer(int color1, PointStyle style, boolean fill) {  
	      XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
	        
	      //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等  
	      XYSeriesRenderer r1 = new XYSeriesRenderer();  //how many lines
	      r1.setColor(color1);  
	      r1.setPointStyle(style);  
	      r1.setFillPoints(fill);  
	      r1.setLineWidth(3);  

	      renderer.addSeriesRenderer(r1);
	      return renderer;  
	  }  
	  protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,  
              double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {  
		//有关对图表的渲染可参看api文档  
		renderer.setChartTitle(title);  
		renderer.setXTitle(xTitle);  
		renderer.setYTitle(yTitle);  
		renderer.setXAxisMin(xMin);  
		renderer.setXAxisMax(xMax);  
		renderer.setYAxisMin(yMin);  
		renderer.setYAxisMax(yMax);  
		renderer.setAxesColor(axesColor);  
		renderer.setLabelsColor(labelsColor);  
		renderer.setShowGrid(true);  
		renderer.setGridColor(Color.GREEN);  
		renderer.setXLabels(20);  
		renderer.setYLabels(10);  
		renderer.setXTitle("Time/sec");  
		renderer.setYTitle("Dimension CM");  
		renderer.setYLabelsAlign(Align.RIGHT);  
		renderer.setPointSize((float) 2);  
		renderer.setShowLegend(false);  
	  }
	  
	  public void updateChart(int x) {  
	        
	      //设置好下一个需要增加的节点  
		  
		  Long base = new Date().getTime() - 3 * TimeChart.DAY; 
		  System.out.println("BASE:"+base);
		  datesec =new Date();
		  System.out.println("BASE:"+base+"datesec:"+datesec);
		  
	      addX = 100;  
	      addY1 = x;//(int)(Math.random() * 255);  
	      //移除数据集中旧的点集  
	      mDataset.removeSeries(series1);   
	        
	      //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100  
	      int length = series1.getItemCount();  
	      if (length > slength) {  
	          length = slength;  
	      }  
	        
	      //将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果  
	      for (int i = 0; i < length; i++) {  
	    	  h1[i] = (int) series1.getX(i) - 1;  
	          v1[i] = (int) series1.getY(i);  
	      }

	      /*long value = new Date().getTime() - 3 * TimeChart.DAY;
	        for (int i = 0; i < 100; i++) {
	            time_series.add(new Date(value + i * TimeChart.DAY / 4), i);*/
	      //点集先清空，为了做成新的点集而准备  
	      series1.clear();  
	      //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中  
	      //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点  
	      series1.add(addX, addY1); 
	      for (int k = 0; k < length; k++) {  
	          series1.add(h1[k], v1[k]);  
	      }  

	        
	      //在数据集中添加新的点集  
	      mDataset.addSeries(series1);  

	      //视图更新，没有这一步，曲线不会呈现动态  
	      //如果在非UI主线程中，需要调用postInvalidate()，具体参考api  
	       
	  }  
	  public GraphicalView getView(Context context) 
		{
			view =  ChartFactory.getLineChartView(context, mDataset, renderer);
			return view;
		}
}
