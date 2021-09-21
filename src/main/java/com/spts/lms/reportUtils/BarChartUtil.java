package com.spts.lms.reportUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.test.StudentTest;

@Component
public class BarChartUtil {
	@Value("${reports}")
	private String reportsFolder;
	
	@Value("${workStoreDir:''}")
	private String workDir;

	private static final Logger logger = Logger.getLogger(BarChartUtil.class);

	/*
	 * public String getBarChart(MultiMap dataMap, String rowKey1, String
	 * rowKey2, String chartTitle, String domainAxisLabel, String
	 * rangeAxisLabel, PlotOrientation orientation, Boolean includeLegend,
	 * Boolean tooltips, Boolean urls) {
	 * 
	 * final CategoryDataset dataset = createDataset(dataMap, rowKey1, rowKey2);
	 * 
	 * return createChart(dataset, chartTitle, orientation, domainAxisLabel,
	 * rangeAxisLabel, includeLegend, tooltips, urls);
	 * 
	 * }
	 */

	public String get3DBarChart(List<StudentAssignment> assignmentList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		final CategoryDataset dataset = createDataset(assignmentList, rowKey);

		return create3DChart(dataset, chartTitle, orientation, domainAxisLabel,
				rangeAxisLabel, includeLegend, tooltips, urls);

	}

	public String get3DBarChartForTest(List<StudentTest> testList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		final CategoryDataset dataset = createDatasetForTest(testList, rowKey);

		return create3DChart(dataset, chartTitle, orientation, domainAxisLabel,
				rangeAxisLabel, includeLegend, tooltips, urls);

	}

	public String getSimpleBarChart(List<StudentAssignment> assignmentList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {
		final CategoryDataset dataset = createDataset(assignmentList, rowKey);
		return createChart(dataset, chartTitle, orientation, domainAxisLabel,
				rangeAxisLabel, includeLegend, tooltips, urls);
	}

	public CategoryDataset createDataset(
			List<StudentAssignment> assignmentList, String rowKey) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		ArrayList<String> valueList = new ArrayList<String>();

		for (StudentAssignment sa : assignmentList) {
			logger.info("SA Score is " + sa.getScore());
			if (sa.getScore() != null) {
				dataset.addValue(Integer.valueOf(sa.getScore()), rowKey,
						sa.getUsername());

			}

			else {
				dataset.addValue(0, rowKey, sa.getUsername());

			}

		}

		return dataset;

	}

	public CategoryDataset createDatasetForTest(List<StudentTest> testList,
			String rowKey) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		ArrayList<String> valueList = new ArrayList<String>();

		for (StudentTest sa : testList) {
			logger.info("Score is " + sa.getScore());
			if (sa.getScore() != null) {
				dataset.addValue(Double.valueOf(sa.getScore()), rowKey,
						sa.getUsername());

			} else {
				dataset.addValue(0, rowKey, sa.getUsername());
			}

		}

		return dataset;

	}

	/*
	 * public CategoryDataset createDataset(MultiMap dataMap, String rowKey1,
	 * String rowKey2) {
	 * 
	 * final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	 * 
	 * Set<String> keys = dataMap.keySet(); // iterate through the key set and
	 * display key and values for (String key : keys) { logger.info("Key = " +
	 * key); logger.info("Values = " + dataMap.get(key) + "n");
	 * ArrayList<Integer> values = (ArrayList<Integer>) dataMap.get(key);
	 * dataset.addValue(Integer.valueOf(values.get(0)), rowKey1, key);
	 * dataset.addValue(Integer.valueOf(values.get(1)), rowKey2, key); }
	 * 
	 * return dataset;
	 * 
	 * }
	 */
	public String createChart(final CategoryDataset dataset, String chartTitle,
			PlotOrientation orientation, String domainAxisLabel,
			String rangeAxisLabel, Boolean includeLegend, Boolean tooltips,
			Boolean urls) {

		final JFreeChart chart = ChartFactory.createBarChart(chartTitle, // chart
				// title
				domainAxisLabel, // domain axis label
				rangeAxisLabel, // range axis label
				dataset, // data
				orientation, // orientation
				includeLegend, // include legend
				tooltips, // tooltips?
				urls // URLs?
				);

		chart.setBackgroundPaint(Color.white);

		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setNoDataMessage("No Data");
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.black);
		plot.setDrawingSupplier(new ChartDrawingSupplier());

		plot.setNoDataMessagePaint(Color.RED);
		Font font3 = new Font("Dialog", Font.PLAIN, 15);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(font3);

		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		renderer.setItemMargin(0.10f);
		renderer.setBaseItemLabelFont(new Font("Dialog", Font.PLAIN, 25));
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		domainAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		// domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		File folderPath = new File(reportsFolder);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		String imgPath = reportsFolder + File.separator + "BarChart"
				+ RandomStringUtils.randomAlphanumeric(10) + ".jpeg";

		File file = new File(imgPath);

		try {
			ChartUtilities.saveChartAsJPEG(file, chart, 1750, 840);
			System.out.print("Image saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imgPath;

	}

	public String create3DChart(final CategoryDataset dataset,
			String chartTitle, PlotOrientation orientation,
			String domainAxisLabel, String rangeAxisLabel,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		final JFreeChart chart = ChartFactory.createBarChart3D(chartTitle,
				domainAxisLabel, rangeAxisLabel, dataset, orientation,
				includeLegend, tooltips, urls);
		chart.setBackgroundPaint(Color.white);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		final CategoryPlot plot = chart.getCategoryPlot();

		plot.setNoDataMessage("No Data");
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.black);
		plot.setDrawingSupplier(new ChartDrawingSupplier());

		plot.setNoDataMessagePaint(Color.BLACK);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 16));
		//rangeAxis.setFixedDimension(450.00);
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		renderer.setItemMargin(.1);

		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.white);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 12));
		chartPanel.setMaximumDrawWidth(10000);  
		// The second step is to set the width of a chart panel.
		chartPanel.setPreferredSize(new Dimension(2000, 2000));
		
		File folderPath = new File(workDir);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}

		/*
		 * String imgPath = reportsFolder + File.separator + "3DBarChart" +
		 * RandomStringUtils.randomAlphanumeric(10) + ".jpeg";
		 * 
		 * File file = new File(imgPath);
		 * 
		 * try { ChartUtilities.saveChartAsJPEG(file, chart, 1750, 840);
		 * System.out.print("Image saved"); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		String fileName = "3DBarChart"
				+ RandomStringUtils.randomAlphanumeric(10) + ".pdf";
		String imgPath = workDir + File.separator + fileName;
		String folderPath1 = workDir + File.separator;

		String outputFile = "";

		WriteChartToPDF writeChartToPDF = new WriteChartToPDF();

		outputFile = writeChartToPDF.convertChartToPDF(chart, 1500, 830,
				fileName, folderPath1);

		File file = new File(outputFile);
		System.out.println("File path is " + file.getAbsolutePath());

		return file.getAbsolutePath();

		// return imgPath;

	}

}
