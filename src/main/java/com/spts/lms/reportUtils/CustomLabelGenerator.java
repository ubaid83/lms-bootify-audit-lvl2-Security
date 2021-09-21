package com.spts.lms.reportUtils;

import java.text.NumberFormat;

import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;

public class CustomLabelGenerator extends AbstractCategoryItemLabelGenerator
		implements CategoryItemLabelGenerator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new generator that only displays labels that are greater than
	 * or equal to the threshold value.
	 *
	 * @param threshold
	 *            the threshold value.
	 */
	public CustomLabelGenerator() {
		super("", NumberFormat.getInstance());

	}

	/**
	 * Generates a label for the specified item. The label is typically a
	 * formatted version of the data value, but any text can be used.
	 *
	 * @param dataset
	 *            the dataset (<code>null</code> not permitted).
	 * @param series
	 *            the series index (zero-based).
	 * @param category
	 *            the category index (zero-based).
	 *
	 * @return the label (possibly <code>null</code>).
	 */
	public String generateLabel(CategoryDataset dataset, int series,
			int category) {

		String result = null;

		Number value = dataset.getValue(series, category);

		result = value.toString(); // could apply formatting here

		return result;
	}

	/*
	 * (non Javadoc)
	 * 
	 * @see
	 * org.jfree.chart.labels.CategoryItemLabelGenerator#generateRowLabel(org
	 * .jfree.data.category.CategoryDataset, int)
	 */
	public String generateRowLabel(CategoryDataset arg0, int arg1) {
		// TODO Stub del metodo generado automaticamente
		return null;
	}

	/*
	 * (non Javadoc)
	 * 
	 * @see
	 * org.jfree.chart.labels.CategoryItemLabelGenerator#generateColumnLabel
	 * (org.jfree.data.category.CategoryDataset, int)
	 */
	public String generateColumnLabel(CategoryDataset arg0, int arg1) {
		// TODO Stub del metodo generado automaticamente
		return null;
	}
}
