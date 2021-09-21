package com.spts.lms.reportUtils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.test.StudentTest;

@Component
public class LineChartUtil {

	@Value("${reports}")
	private String reportsFolder;
	
	@Value("${workStoreDir:''}")
	private String workDir;

	private static final Logger logger = Logger.getLogger(LineChartUtil.class);

	public String getSimpleLineChartForAssignment(String chartTitle,
			List<StudentAssignment> assignmentList, String rowKey,
			String domainAxisLabel, String rangeAxisLabel,
			PlotOrientation orientation, Boolean includeLegend,
			Boolean tooltips, Boolean urls) {
		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle,
				domainAxisLabel, rangeAxisLabel,
				createDatasetForAssignment(assignmentList, rowKey),
				orientation, includeLegend, tooltips, urls);

		lineChart.setBackgroundPaint(Color.white);
		final CategoryPlot plot = lineChart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		plot.setNoDataMessagePaint(Color.RED);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer();
		renderer.setBaseItemLabelGenerator(new CustomLabelGenerator());
		plot.setRenderer(renderer);

		int seriesCount = plot.getDomainAxisCount();
		for (int i = 0; i < seriesCount; i++) {
			plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
		}

		Font font3 = new Font("Dialog", Font.BOLD, 15);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 16));
		// rangeAxis.setTickLabelFont(font3);

		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// rangeAxis.setTickLabelFont(font3);
		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 20));
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 12));
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		File folderPath = new File(workDir);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}

		String fileName = "LineChart"
				+ RandomStringUtils.randomAlphanumeric(10) + ".pdf";
		String imgPath = workDir + File.separator + fileName;
		String folderPath1 = workDir + File.separator;

		String outputFile = "";

		WriteChartToPDF writeChartToPDF = new WriteChartToPDF();

		outputFile = writeChartToPDF.convertChartToPDF(lineChart, 1500, 830,
				fileName, folderPath1);

		File file = new File(outputFile);
		System.out.println("File path is " + file.getAbsolutePath());

		return file.getAbsolutePath();
	}

	public String getSimpleLineChartForTest(String chartTitle,
			List<StudentTest> testList, String rowKey, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {
		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle,
				domainAxisLabel, rangeAxisLabel,
				createDatasetForTest(testList, rowKey), orientation,
				includeLegend, tooltips, urls);

		lineChart.setBackgroundPaint(Color.white);
		final CategoryPlot plot = lineChart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		plot.setNoDataMessagePaint(Color.RED);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer();
		renderer.setBaseItemLabelGenerator(new CustomLabelGenerator());
		plot.setRenderer(renderer);
		int seriesCount = plot.getDomainAxisCount();
		for (int i = 0; i < seriesCount; i++) {
			plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
		}

		Font font3 = new Font("Dialog", Font.BOLD, 15);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(font3);
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(font3);
		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 20));

		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		File folderPath = new File(workDir);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		/*
		 * String imgPath = reportsFolder + File.separator + "LineChart" +
		 * RandomStringUtils.randomAlphanumeric(10) + ".jpeg";
		 * 
		 * File file = new File(imgPath);
		 * 
		 * try { ChartUtilities.saveChartAsJPEG(file, lineChart, 1500, 840);
		 * System.out.print("Image saved"); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * return imgPath;
		 */

		String fileName = "LinChart" + RandomStringUtils.randomAlphanumeric(10)
				+ ".pdf";
		String imgPath = workDir + File.separator + fileName;
		String folderPath1 = workDir + File.separator;

		String outputFile = "";

		WriteChartToPDF writeChartToPDF = new WriteChartToPDF();

		outputFile = writeChartToPDF.convertChartToPDF(lineChart, 1500, 830,
				fileName, folderPath1);

		File file = new File(outputFile);
		System.out.println("File path is " + file.getAbsolutePath());

		return file.getAbsolutePath();
	}

	private DefaultCategoryDataset createDatasetForAssignment(
			List<StudentAssignment> assignmentList, String rowKey) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		ArrayList<String> valueList = new ArrayList<String>();

		for (StudentAssignment sa : assignmentList) {
			logger.info("Score is " + sa.getScore());
			if (sa.getScore() != null) {
				dataset.addValue(Integer.valueOf(sa.getScore()), rowKey,
						sa.getUsername());
			} else {
				dataset.addValue(0, rowKey, sa.getUsername());
			}

		}

		return dataset;
	}

	public String createLineChartForStdDeviationForAssignment(
			String chartTitle, List<StudentAssignment> assignmentList,
			Integer stdDevValue, String series2, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {
		JFreeChart lineChart = ChartFactory.createLineChart(
				chartTitle,
				domainAxisLabel,
				rangeAxisLabel,
				createDataSetForStdDeviationForAssignment(assignmentList,
						stdDevValue, series2));

		lineChart.setBackgroundPaint(Color.white);
		final CategoryPlot plot = lineChart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		plot.setNoDataMessagePaint(Color.RED);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer();
		plot.setRenderer(renderer);

		int seriesCount = plot.getDomainAxisCount();
		for (int i = 0; i < seriesCount; i++) {
			plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
		}

		Font font3 = new Font("Dialog", Font.BOLD, 15);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 16));
		// rangeAxis.setTickLabelFont(font3);

		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// rangeAxis.setTickLabelFont(font3);
		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 20));
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 12));
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		File folderPath = new File(workDir);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}

		String fileName = "LineChart"
				+ RandomStringUtils.randomAlphanumeric(10) + ".pdf";
		String imgPath = workDir + File.separator + fileName;
		String folderPath1 = workDir + File.separator;

		String outputFile = "";

		WriteChartToPDF writeChartToPDF = new WriteChartToPDF();

		outputFile = writeChartToPDF.convertChartToPDF(lineChart, 1500, 830,
				fileName, folderPath1);

		File file = new File(outputFile);
		System.out.println("File path is " + file.getAbsolutePath());

		return file.getAbsolutePath();
	}

	public String createLineChartForStdDeviationForTest(
			String chartTitle, List<StudentTest> testList,
			Integer stdDevValue, String series2, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {
		JFreeChart lineChart = ChartFactory.createLineChart(
				chartTitle,
				domainAxisLabel,
				rangeAxisLabel,
				createDataSetForStdDeviationForTest(testList,
						stdDevValue, series2));

		lineChart.setBackgroundPaint(Color.white);
		final CategoryPlot plot = lineChart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		plot.setNoDataMessagePaint(Color.RED);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer();
		renderer.setBaseItemLabelGenerator(new CustomLabelGenerator());
		plot.setRenderer(renderer);

		int seriesCount = plot.getDomainAxisCount();
		for (int i = 0; i < seriesCount; i++) {
			plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
		}

		Font font3 = new Font("Dialog", Font.BOLD, 15);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 16));
		// rangeAxis.setTickLabelFont(font3);

		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// rangeAxis.setTickLabelFont(font3);
		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 20));
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 12));
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		File folderPath = new File(workDir);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}

		String fileName = "LineChart"
				+ RandomStringUtils.randomAlphanumeric(10) + ".pdf";
		String imgPath = workDir + File.separator + fileName;
		String folderPath1 = workDir + File.separator;

		String outputFile = "";

		WriteChartToPDF writeChartToPDF = new WriteChartToPDF();

		outputFile = writeChartToPDF.convertChartToPDF(lineChart, 1500, 830,
				fileName, folderPath1);

		File file = new File(outputFile);
		System.out.println("File path is " + file.getAbsolutePath());

		return file.getAbsolutePath();
	}

	private CategoryDataset createDataSetForStdDeviationForAssignment(
			List<StudentAssignment> assignmentList, Integer stdDevValue,
			String series2) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		String series1 = "Standard Deviation";
		ArrayList<String> valueList = new ArrayList<String>();

		for (StudentAssignment sa : assignmentList) {
			logger.info("Score is " + sa.getScore());
			if (sa.getScore() != null) {
				dataset.addValue(Integer.valueOf(sa.getScore()), series2,
						sa.getUsername());
			} else {
				dataset.addValue(0, series2, sa.getUsername());
			}

		}

		for (StudentAssignment sa : assignmentList) {
			dataset.addValue(stdDevValue, series1, sa.getUsername());
		}

		return dataset;

	}

	private CategoryDataset createDataSetForStdDeviationForTest(
			List<StudentTest> testList, Integer stdDevValue, String series2) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		String series1 = "Standard Deviation";
		ArrayList<String> valueList = new ArrayList<String>();

		for (StudentTest sa : testList) {
			logger.info("Score is " + sa.getScore());
			if (sa.getScore() != null) {
				dataset.addValue(Double.valueOf(sa.getScore()), series2,
						sa.getUsername());
			} else {
				dataset.addValue(0, series2, sa.getUsername());
			}

		}

		for (StudentTest sa : testList) {
			dataset.addValue(stdDevValue, series1, sa.getUsername());
		}

		return dataset;

	}

	private DefaultCategoryDataset createDatasetForTest(
			List<StudentTest> testList, String rowKey) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

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

	public String imgToPDF(String folderPath, String outputFile,
			String inputFile) {
		// File root = new File("C:/Users/Asmita.P/Documents/Reports");
		// String outputFile = "output.pdf";
		List<String> files = new ArrayList<String>();
		files.add(inputFile);
		// files.add("page2.jpg");

		File file = new File(outputFile);

		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(new File(
					folderPath, outputFile)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document.open();
		try {

			document.newPage();
			document.setPageSize(new Rectangle(500, 400));
			Image image = Image.getInstance(new File(inputFile)
					.getAbsolutePath());
			image.setAbsolutePosition(0, 1000);
			image.setBorderWidth(0);
			// /=image.scaleAbsolute(PageSize.A4);
			image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
			// image.scaleToFit(500, 400);
			// image.scaleAbsolute(500, 400);
			document.add(image);
		} catch (Exception e) {
			e.printStackTrace();
		}

		document.close();

		return outputFile;
	}
}
