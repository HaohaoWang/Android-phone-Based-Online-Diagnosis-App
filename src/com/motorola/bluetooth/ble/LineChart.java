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

public class LineChart {
	  private GraphicalView view;
	  private Timer timer = new Timer();  
	  private TimerTask task;  
	  private String title = "Accelerometer"; 
	  public static final int slength = 100;
	  private TimeSeries series1,series2,series3;  
	  private XYMultipleSeriesDataset mDataset;   
	  private XYMultipleSeriesRenderer renderer;  
	  
	  private Context context;  
	  private int addX = -1, addY1,addY2,addY3;  
	  private java.util.Date datesec;
	    
	  int[] h1 = new int[slength],h2 = new int[slength],h3 = new int[slength];  
	  int[] v1 = new int[slength],v2 = new int[slength],v3 = new int[slength]; 
	  
	public LineChart() {
		//get the MultipleSeriesDataset
		series1 = new TimeSeries("axis_x");
		series2 = new TimeSeries("axis_y");
		series3 = new TimeSeries("axis_z");
		mDataset = new XYMultipleSeriesDataset(); 
		mDataset.addSeries(series1);
		mDataset.addSeries(series2);
		mDataset.addSeries(series3);
		//get the MultipleSeriesRenderer
		renderer = buildRenderer(Color.rgb(51, 51, 204),Color.rgb(255, 255, 255),Color.rgb(255, 102, 0), PointStyle.CIRCLE, true); 
		setChartSettings(renderer, "time", "acc", 0, 100, 0, 255, Color.WHITE, Color.WHITE);  
	  
	}
	
	 protected XYMultipleSeriesRenderer buildRenderer(int color1,int color2,int color3, PointStyle style, boolean fill) {  
	      XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
	        
	      //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等  
	      XYSeriesRenderer r1 = new XYSeriesRenderer();  //how many lines
	      r1.setColor(color1);  
	      r1.setPointStyle(style);  
	      r1.setFillPoints(fill);  
	      r1.setLineWidth(3);  
	      
	      XYSeriesRenderer r2 = new XYSeriesRenderer();  //how many lines
	      r2.setColor(color2);  
	      r2.setPointStyle(style);  
	      r2.setFillPoints(fill);  
	      r2.setLineWidth(3);
	      
	      XYSeriesRenderer r3 = new XYSeriesRenderer();  //how many lines
	      r3.setColor(color3);  
	      r3.setPointStyle(style);  
	      r3.setFillPoints(fill);  
	      r3.setLineWidth(3);
	      renderer.addSeriesRenderer(r1);
	      renderer.addSeriesRenderer(r2);
	      renderer.addSeriesRenderer(r3);
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
		renderer.setYTitle("Gravity acceleration * 10");  
		renderer.setYLabelsAlign(Align.RIGHT);  
		renderer.setPointSize((float) 2);  
		renderer.setShowLegend(false);  
	  }
	  
	  public void updateChart(int x,int y, int z) {  
	        
	      //设置好下一个需要增加的节点  
		  
		  Long base = new Date().getTime() - 3 * TimeChart.DAY; 
		  System.out.println("BASE:"+base);
		  datesec =new Date();
		  System.out.println("BASE:"+base+"datesec:"+datesec);
		  
	      addX = 100;  
	      addY1 = x;//(int)(Math.random() * 255);  
	      addY2 = y;
	      addY3 = z;
	      //移除数据集中旧的点集  
	      mDataset.removeSeries(series1);  
	      mDataset.removeSeries(series2);  
	      mDataset.removeSeries(series3);  
	        
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
	      for (int i = 0; i < length; i++) {  
	    	  h2[i] = (int) series2.getX(i) - 1;  
	          v2[i] = (int) series2.getY(i);  
	      }
	      for (int i = 0; i < length; i++) {  
	    	  h3[i] = (int) series3.getX(i) - 1;  
	          v3[i] = (int) series3.getY(i);  
	      }
	        
	      /*long value = new Date().getTime() - 3 * TimeChart.DAY;
	        for (int i = 0; i < 100; i++) {
	            time_series.add(new Date(value + i * TimeChart.DAY / 4), i);*/
	      //点集先清空，为了做成新的点集而准备  
	      series1.clear();  
	      series2.clear();
	      series3.clear();
	      //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中  
	      //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点  
	      series1.add(addX, addY1); 
	      series2.add(addX, addY2);
	      series3.add(addX, addY3);
	      for (int k = 0; k < length; k++) {  
	          series1.add(h1[k], v1[k]);  
	      }  
	      for (int k = 0; k < length; k++) {  
	          series2.add(h2[k], v2[k]);  
	      }  
	      for (int k = 0; k < length; k++) {  
	          series3.add(h3[k], v3[k]);  
	      }  
	        
	      //在数据集中添加新的点集  
	      mDataset.addSeries(series1);  
	      mDataset.addSeries(series2);
	      mDataset.addSeries(series3);
	      //视图更新，没有这一步，曲线不会呈现动态  
	      //如果在非UI主线程中，需要调用postInvalidate()，具体参考api  
	       
	  }  
	  public GraphicalView getView(Context context) 
		{
			view =  ChartFactory.getLineChartView(context, mDataset, renderer);
			return view;
		}
}
