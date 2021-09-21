package com.spts.lms.reportUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.test.StudentTest;

@Component
public class StackedBarChartUtil {
	@Value("${reports}")
	private String reportsFolder;
	
	@Value("${workStoreDir:''}")
	private String workDir;
	
	private static final Logger logger = Logger
			.getLogger(StackedBarChartUtil.class);

	public String getStackedBarChartForAssignment(
			List<StudentAssignment> assignmentList, String rowKey,
			String chartTitle, String domainAxisLabel, String rangeAxisLabel,
			PlotOrientation orientation, Boolean includeLegend,
			Boolean tooltips, Boolean urls) {

		final CategoryDataset dataset = createDatasetForAssignment(
				assignmentList, rowKey);

		return createChart(dataset, chartTitle, orientation, domainAxisLabel,
				rangeAxisLabel, includeLegend, tooltips, urls);

	}

	public String getStackedBarChartForTest(List<StudentTest> testList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		final CategoryDataset dataset = createDatasetForTest(testList, rowKey);

		return createChart(dataset, chartTitle, orientation, domainAxisLabel,
				rangeAxisLabel, includeLegend, tooltips, urls);

	}

	/*
	 * private CategoryDataset createDatasetForAssignment(
	 * List<StudentAssignment> assignmentList, String rowKey) {
	 * 
	 * DefaultCategoryDataset defaultcategorydataset = new
	 * DefaultCategoryDataset(); for (StudentAssignment sa : assignmentList) {
	 * defaultcategorydataset.addValue(Integer.valueOf(sa.getScore()),
	 * "Scored Marks", sa.getAssignmentName()); Integer diff =
	 * Integer.valueOf(sa.getMaxScore()) - Integer.valueOf(sa.getScore());
	 * defaultcategorydataset.addValue(diff, "Out of Marks",
	 * sa.getAssignmentName()); }
	 * 
	 * return defaultcategorydataset; }
	 * 
	 * private CategoryDataset createDatasetForTest(List<StudentTest> testList,
	 * String rowKey) {
	 * 
	 * DefaultCategoryDataset defaultcategorydataset = new
	 * DefaultCategoryDataset(); for (StudentTest sa : testList) {
	 * defaultcategorydataset.addValue(Integer.valueOf(sa.getScore()),
	 * "Scored Marks", sa.getTestName()); Integer diff =
	 * Integer.valueOf(sa.getMaxScore()) - Integer.valueOf(sa.getScore());
	 * defaultcategorydataset.addValue(diff, "Out of Marks", sa.getTestName());
	 * }
	 * 
	 * return defaultcategorydataset; }
	 */

	private CategoryDataset createDatasetForAssignment(
			List<StudentAssignment> assignmentList, String rowKey) {

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		for (StudentAssignment sa : assignmentList) {
			if (sa.getScore() != null) {
				defaultcategorydataset.addValue(Integer.valueOf(sa.getScore()),
						"Scored Marks", sa.getAssignmentName());
				Integer diff = Integer.valueOf(sa.getMaxScore())
						- Integer.valueOf(sa.getScore());
				defaultcategorydataset.addValue(diff, "Out of Marks",
						sa.getAssignmentName());

			}

			else {
				defaultcategorydataset.addValue(0, "Scored Marks",
						sa.getAssignmentName());
				Integer diff = Integer.valueOf(sa.getMaxScore()) - 0;
				defaultcategorydataset.addValue(diff, "Out of Marks",
						sa.getAssignmentName());

			}

		}

		return defaultcategorydataset;
	}

	private CategoryDataset createDatasetForTest(List<StudentTest> testList,
			String rowKey) {

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		for (StudentTest sa : testList) {
			if (sa.getScore() != null) {
				defaultcategorydataset.addValue(Double.valueOf(sa.getScore()),
						"Scored Marks", sa.getTestName());
				Double diff = Double.valueOf(sa.getMaxScore())
						- Double.valueOf(sa.getScore());
				defaultcategorydataset.addValue(diff, "Out of Marks",
						sa.getTestName());

			}

			else {
				defaultcategorydataset.addValue(0, "Scored Marks",
						sa.getTestName());
				Integer diff = Integer.valueOf(sa.getMaxScore()) - 0;
				defaultcategorydataset.addValue(diff, "Out of Marks",
						sa.getTestName());

			}

		}

		return defaultcategorydataset;
	}

	private String createChart(final CategoryDataset dataset,
			String chartTitle, PlotOrientation orientation,
			String domainAxisLabel, String rangeAxisLabel,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		final JFreeChart chart = ChartFactory.createStackedBarChart(chartTitle,
				domainAxisLabel, rangeAxisLabel, dataset, orientation,
				includeLegend, tooltips, urls);

		chart.setBackgroundPaint(Color.white);

		Font font3 = new Font("Dialog", Font.BOLD, 15);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setNoDataMessage("No Data");
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.black);
		plot.setDrawingSupplier(new ChartDrawingSupplier());

		plot.setNoDataMessagePaint(Color.BLACK);
		CategoryAxis axis = plot.getDomainAxis();
		axis.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		axis.setTickLabelFont(font3);

		ValueAxis numAxis = plot.getRangeAxis();
		numAxis.setTickLabelFont(font3);
		/*
		 * plot.getRenderer().setSeriesPaint(0, new Color(30, 100, 175));
		 * plot.getRenderer().setSeriesPaint(1, new Color(90, 190, 110));
		 * plot.getRenderer().setSeriesPaint(2, new Color(225, 45, 45));
		 */
		// plot.setBackgroundPaint(Color.white);

		plot.setDrawingSupplier(new ChartDrawingSupplier());
		/*
		 * String imgPath = reportsFolder + File.separator +
		 * "StackedBarChartForStudent" +
		 * RandomStringUtils.randomAlphanumeric(10) +
		 * "TestStackedBarChart.jpeg";
		 * 
		 * File file = new File(imgPath); // downloadChartImage(imgPath,
		 * request, response);
		 * 
		 * try { //ChartUtilities.saveChartAsJPEG(file, chart, 1500, 840);
		 * ChartUtilities.saveChartAsJPEG(file, chart, 500, 400);
		 * System.out.print("Image saved"); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * return imgPath;
		 */
		String fileName = "StackedBarChart"
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
	}

}
