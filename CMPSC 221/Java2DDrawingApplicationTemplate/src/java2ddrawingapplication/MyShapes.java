/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;

/**
 *
 * @author al
 */
public abstract class MyShapes {
    private Point startPoint = new Point();
    private Point endPoint = new Point();
    private Paint paint;
    private Stroke stroke;
    
    public MyShapes() {
        stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        paint = Color.BLACK;
    }
    
    public MyShapes(Point pntA, Point pntB, Paint paint, Stroke strk) {
        startPoint = pntA;
        endPoint = pntB;
        this.paint = paint;
        stroke = strk;
    }
    
    public abstract void draw(Graphics2D g2d);

    /**
     * @return the startPoint
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * @param _startPoint the startPoint to set
     */
    public void setStartPoint(Point _startPoint) {
        startPoint = _startPoint;
    }

    /**
     * @return the endPoint
     */
    public Point getEndPoint() {
        return endPoint;
    }

    /**
     * @param _endPoint the endPoint to set
     */
    public void setEndPoint(Point _endPoint) {
        endPoint = _endPoint;
    }

    /**
     * @return the paint
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * @param _paint the paint to set
     */
    public void setPaint(Paint _paint) {
        paint = _paint;
    }

    /**
     * @return the stroke
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * @param _stroke the stroke to set
     */
    public void setStroke(Stroke _stroke) {
        stroke = _stroke;
    }
}