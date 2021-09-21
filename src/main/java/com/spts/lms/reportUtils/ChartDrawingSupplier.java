package com.spts.lms.reportUtils;



import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.plot.DefaultDrawingSupplier;



public class ChartDrawingSupplier extends DefaultDrawingSupplier  {

    public Paint[] paintSequence;
    public int paintIndex;
    public int fillPaintIndex;

    {
        paintSequence =  new Paint[] {
                
                new Color(255,0,0),
                new Color(0,255,0),
                new Color(0,0,255),
                new Color(255,255,0),
                new Color(0,255,255),
                new Color(255,0,255),
                new Color(128,128,128),
                new Color(128,0,0),
                new Color(0,0,128),
                new Color(0,128,0),
                new Color(0,0,0),
        };
    }

    @Override
    public Paint getNextPaint() {
        Paint result
        = paintSequence[paintIndex % paintSequence.length];
        paintIndex++;
        return result;
    }


    @Override
    public Paint getNextFillPaint() {
        Paint result
        = paintSequence[fillPaintIndex % paintSequence.length];
        fillPaintIndex++;
        return result;
    }   
}
