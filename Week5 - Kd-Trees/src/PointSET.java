import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 
 *
 * @author zhangyu
 * @date 2017.4.3
 */
public class PointSET
{
    private SET<Point2D> points;
    
    /**
     * construct an empty set of points 
     */
    public PointSET()
    {
        points = new SET<Point2D>();
    }
    
    /**
     * is the set empty? 
     * 
     * @return
     */
    public boolean isEmpty()
    {
        return points.isEmpty();
    }
    
    /**
     * number of points in the set 
     * 
     * @return
     */
    public int size()
    {
        return points.size();
    }
    
    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param p
     */
    public void insert(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");
        points.add(p);
    }
    
    /**
     * does the set contain point p? 
     * 
     * @param p
     * @return
     */
    public boolean contains(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");       
        return points.contains(p);
    }
    
    /**
     * draw all points to standard draw 
     * 
     */
    public void draw()
    {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : points)
            point.draw();
    }
    
    /**
     * all points that are inside the rectangle 
     * 
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null) throw new NullPointerException("Null rectangle");   
        
        Queue<Point2D> pointsInRect = new Queue<Point2D>();

        for (Point2D point : points)
            if (rect.contains(point)) pointsInRect.enqueue(point); 
        return pointsInRect;
    }
    
    /**
     * a nearest neighbor in the set to point p; null if the set is empty  
     * 
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");  
        if (points.isEmpty()) return null;
        
        Point2D nearestPoint = points.min();
        double minDist = Double.POSITIVE_INFINITY;
        
        for (Point2D point : points)
        {
            double dist = p.distanceTo(point);
            
            if (minDist > dist) 
            {
                nearestPoint = point;
                minDist = dist;
            }
        }
        return nearestPoint;
    }
    
    /**
     * unit testing of the methods (optional) 
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        
    }
}