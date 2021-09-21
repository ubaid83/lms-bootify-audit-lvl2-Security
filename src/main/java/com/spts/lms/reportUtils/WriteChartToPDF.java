package com.spts.lms.reportUtils;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;

import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class WriteChartToPDF {

	public String convertChartToPDF(JFreeChart chart, int width, int height,
			String fileName, String folderPath) {

		PdfWriter writer = null;

		Document document = new Document();
		document.setPageSize(new Rectangle(1500,850));
		//File file = new File(filePath);

		try {
			writer = PdfWriter.getInstance(document,
					new FileOutputStream(folderPath + fileName));
			document.open();
			PdfContentByte contentByte = writer.getDirectContent();
			PdfTemplate template = contentByte.createTemplate(width, height);
			Graphics2D graphics2d = template.createGraphics(width, height,
					new DefaultFontMapper());
			Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
					height);

			chart.draw(graphics2d, rectangle2d);

			graphics2d.dispose();
			contentByte.addTemplate(template, 0, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		document.close();
		
		
		return folderPath + fileName;

	}

}
