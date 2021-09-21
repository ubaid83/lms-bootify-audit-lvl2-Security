package com.spts.lms.reportUtils;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.plot.PlotOrientation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.test.StudentTest;

@Component
public class ReportUtils {

	@Autowired
	PieChartUtil pieChartUtil;

	@Autowired
	BarChartUtil barChartUtil;

	@Autowired
	LineChartUtil lineChartUtil;

	@Autowired
	StackedBarChartUtil stackedBarChartUtil;

	/*
	 * public String createBarChart(MultiMap dataMap, String rowKey1, String
	 * rowKey2, String chartTitle, String domainAxisLabel, String
	 * rangeAxisLabel, PlotOrientation orientation, Boolean includeLegend,
	 * Boolean tooltips, Boolean urls) {
	 * 
	 * return barChartUtil.getBarChart(dataMap, rowKey1, rowKey2, chartTitle,
	 * domainAxisLabel, rangeAxisLabel, orientation, includeLegend, tooltips,
	 * urls); }
	 */

	public String createSimpleBarChart(List<StudentAssignment> assignmentList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		return barChartUtil.getSimpleBarChart(assignmentList, rowKey,
				chartTitle, domainAxisLabel, rangeAxisLabel, orientation,
				includeLegend, tooltips, urls);
	}

	public String createStackedBarChartForAssignment(
			List<StudentAssignment> assignmentList, String rowKey,
			String chartTitle, String domainAxisLabel, String rangeAxisLabel,
			PlotOrientation orientation, Boolean includeLegend,
			Boolean tooltips, Boolean urls) {
		return stackedBarChartUtil.getStackedBarChartForAssignment(
				assignmentList, rowKey, chartTitle, domainAxisLabel,
				rangeAxisLabel, orientation, includeLegend, tooltips, urls);
	}

	public String createStackedBarChartForTest(List<StudentTest> testList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {
		return stackedBarChartUtil.getStackedBarChartForTest(testList, rowKey,
				chartTitle, domainAxisLabel, rangeAxisLabel, orientation,
				includeLegend, tooltips, urls);
	}

	public String createSimplePieChart(Map<String, String> dataMap,
			String chartTitle, Boolean includeLegend, Boolean tooltips,
			Boolean urls) {

		return pieChartUtil.getPieChart(dataMap, includeLegend, tooltips, urls,
				chartTitle);

	}

	public String create3DPieChart(Map<String, String> dataMap,
			String chartTitle, Boolean includeLegend, Boolean tooltips,
			Boolean urls) {

		return pieChartUtil.get3DPieChart(dataMap, includeLegend, tooltips,
				urls, chartTitle);
	}

	public String createSimpleLineChartForAssignment(String chartTitle,
			List<StudentAssignment> assignmentList, String rowKey,
			String domainAxisLabel, String rangeAxisLabel,
			PlotOrientation orientation, Boolean includeLegend,
			Boolean tooltips, Boolean urls) {

		return lineChartUtil.getSimpleLineChartForAssignment(chartTitle,
				assignmentList, rowKey, domainAxisLabel, rangeAxisLabel,
				orientation, includeLegend, tooltips, urls);

		// chart.pack();
		// RefineryUtilities.centerFrameOnScreen(chart);
		// chart.setVisible(true);
	}

	public String createSimpleLineChartForTest(String chartTitle,
			List<StudentTest> testList, String rowKey, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		return lineChartUtil.getSimpleLineChartForTest(chartTitle, testList,
				rowKey, domainAxisLabel, rangeAxisLabel, orientation,
				includeLegend, tooltips, urls);

		// chart.pack();
		// RefineryUtilities.centerFrameOnScreen(chart);
		// chart.setVisible(true);
	}

	public String createLineChartForStdDeviationForAssignment(
			String chartTitle, List<StudentAssignment> assignmentList,
			Integer stdDevValue, String series2, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			Boolean includeLegend, Boolean tooltips, Boolean urls) {

		return lineChartUtil.createLineChartForStdDeviationForAssignment(
				chartTitle, assignmentList, stdDevValue, series2,
				domainAxisLabel, rangeAxisLabel, orientation, includeLegend,
				tooltips, urls);

	}

	public String createLineChartForStdDeviationForTest(String chartTitle,
			List<StudentTest> testList, Integer stdDevValue, String series2,
			String domainAxisLabel, String rangeAxisLabel,
			PlotOrientation orientation, Boolean includeLegend,
			Boolean tooltips, Boolean urls) {

		return lineChartUtil.createLineChartForStdDeviationForTest(chartTitle,
				testList, stdDevValue, series2, domainAxisLabel,
				rangeAxisLabel, orientation, includeLegend, tooltips, urls);

	}

	public String create3DBarChart(List<StudentAssignment> assignmentList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			boolean includeLegend, boolean tooltips, boolean urls) {
		return barChartUtil.get3DBarChart(assignmentList, rowKey, chartTitle,
				domainAxisLabel, rangeAxisLabel, orientation, includeLegend,
				tooltips, urls);
	}

	public String create3DBarChartForTest(List<StudentTest> testList,
			String rowKey, String chartTitle, String domainAxisLabel,
			String rangeAxisLabel, PlotOrientation orientation,
			boolean includeLegend, boolean tooltips, boolean urls) {
		return barChartUtil.get3DBarChartForTest(testList, rowKey, chartTitle,
				domainAxisLabel, rangeAxisLabel, orientation, includeLegend,
				tooltips, urls);
	}

}
