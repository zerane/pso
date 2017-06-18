package tool;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

/**
 * Created by admin on 2017/6/12.
 */
public class ChartDrawer {
    public static void main(String[] args){
    }

    public void draw(java.util.List<TestData> data){
        for(TestData td:data){
            for(int i=0;i<td.data.size();i++){
                if(td.data.get(i)==0){
                    td.data.set(i,1D);
                }
            }
        }
        DefaultCategoryDataset ds = prepareDataSet(data);
        JFreeChart mChart = ChartFactory.createLineChart("graph","iteration","fitnessValue",ds, PlotOrientation.VERTICAL,true,true,false);

        CategoryPlot mPlot = (CategoryPlot)mChart.getPlot();
        mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
        mPlot.setRangeGridlinePaint(Color.BLUE);//背景底部横虚线
        mPlot.setOutlinePaint(Color.RED);//边界线

        ChartFrame mChartFrame = new ChartFrame("折线图", mChart);
        mChartFrame.pack();
        mChartFrame.setVisible(true);
    }

    public DefaultCategoryDataset prepareDataSet(java.util.List<TestData> data){
        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        for (TestData line: data) {
            for(Integer i=0;i<line.data.size();i++){
                ds.addValue(process(line.data.get(i)),line.category,(Integer)i);
            }
        }

        return ds;
    }

    public double process(double in){
        return Math.log(in);
    }
}
