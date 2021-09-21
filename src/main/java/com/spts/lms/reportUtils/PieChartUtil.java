package com.spts.lms.reportUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class PieChartUtil {

	private static final Logger logger = Logger.getLogger(PieChartUtil.class);

	@Value("${reports}")
	private String reportsFolder;
	
	@Value("${workStoreDir:''}")
	private String workDir;

	public String getPieChart(Map<String, String> dataMap,
			Boolean includeLegend, Boolean tooltips, Boolean urls,
			String chartTitle) {

		final DefaultPieDataset dataset = createDataset(dataMap);
		return createChart(dataset, chartTitle, includeLegend, tooltips, urls);

	}

	public String get3DPieChart(Map<String, String> dataMap,
			Boolean includeLegend, Boolean tooltips, Boolean urls,
			String chartTitle) {

		final DefaultPieDataset dataset = createDataset(dataMap);
		return create3dChart(dataset, chartTitle, includeLegend, tooltips, urls);

	}

	private DefaultPieDataset createDataset(Map<String, String> dataMap) {

		DefaultPieDataset dataset = new DefaultPieDataset();

		for (String key : dataMap.keySet()) {
			logger.info("Key = " + key);
			String value = dataMap.get(key);
			logger.info("Value = " + value);
			dataset.setValue(key, Integer.valueOf(value));

		}
		return dataset;

	}

	private String createChart(final DefaultPieDataset dataset,
			String chartTitle, Boolean includeLegend, Boolean tooltips,
			Boolean urls) {
		File folderPath = new File(reportsFolder);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}

		String imgPath = reportsFolder + File.separator + "PieChart"
				+ RandomStringUtils.randomAlphanumeric(10);

		JFreeChart chart = ChartFactory.createPieChart(chartTitle, dataset,
				includeLegend, // legend?
				tooltips, // tooltips?
				urls // URLs?
				);

		chart.setBackgroundPaint(Color.white);

		File file = new File(imgPath);

		try {
			ChartUtilities.saveChartAsJPEG(file, chart, 640, 480);

		} catch (IOException e) {
			logger.error("Exception", e);
		}

		return imgPath;

	}

	private String create3dChart(final DefaultPieDataset dataset,
			String chartTitle, Boolean includeLegend, Boolean tooltips,
			Boolean urls) {
		File folderPath = new File(workDir);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}

		// String imgPath = reportsFolder + File.separator + "PieChart"
		// + RandomStringUtils.randomAlphanumeric(10) + ".jpeg";

		JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, dataset,
				includeLegend, tooltips, urls);

		logger.info("Chart Created");
		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
				"{0}: ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));

		((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);

		chart.setBackgroundPaint(Color.white);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setLabelPaint(Color.black);
		plot.setLabelBackgroundPaint(Color.LIGHT_GRAY);
		plot.setAutoPopulateSectionOutlinePaint(true);
		plot.setAutoPopulateSectionOutlineStroke(true);
		plot.setAutoPopulateSectionPaint(true);
		plot.setLabelGap(0);
		Font font3 = new Font("Dialog", Font.PLAIN, 25);
		plot.setLabelFont(font3);
		plot.setInteriorGap(0.05);
		plot.setDrawingSupplier(new ChartDrawingSupplier());

		/*
		 * File file = new File(imgPath);
		 * 
		 * try { ChartUtilities.saveChartAsJPEG(file, chart, 1500, 840);
		 * logger.info("Image Created"); imgToPDF(reportsFolder, "PieChart" +
		 * RandomStringUtils.randomAlphanumeric(10) + ".pdf", imgPath);
		 * 
		 * } catch (IOException e) { logger.error("Exception", e); }
		 * 
		 * return imgPath;
		 */

		String fileName = "3DPieChart"
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

	public File imgToPDF(String folderPath, String outputFile, String inputFile) {
		File root = new File("C:/Users/Asmita.P/Documents/Reports");
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

			for (String f : files) {
				document.newPage();
				document.setPageSize(new Rectangle(500, 400));
				Image image = Image.getInstance(new File(root, f)
						.getAbsolutePath());
				image.setAbsolutePosition(0, 1000);
				image.setBorderWidth(0);
				// /=image.scaleAbsolute(PageSize.A4);
				image.scaleToFit(PageSize.A4.getWidth(),
						PageSize.A4.getHeight());
				// image.scaleToFit(500, 400);
				// image.scaleAbsolute(500, 400);
				document.add(image);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		document.close();

		return file;
	}
}
