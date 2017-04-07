import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * The {@code PointSET} class represents a set of points 
 * in the unit square. It supports <em>range search</em> 
 * (find all of the points contained in a query rectangle) 
 * and <em>nearest neighbor search</em> (find a closest 
 * point to a query point) by Brute-force methods.
 *
 * @author zhangyu
 * @date 2017.4.3
 */
public class PointSET
{
    private SET<Point2D> points; // the collection of points
    
    /**
     * Constructs an empty set of points.
     */
    public PointSET()
    {
        points = new SET<Point2D>();
    }
    
    /**
     * Returns true if the set of points is empty.
     * 
     * @return true if the set is empty; 
     *         false otherwise
     */
    public boolean isEmpty()
    {
        return points.isEmpty();
    }
    
    /**
     * Returns the number of points in the set.
     * 
     * @return the number of points in the set 
     */
    public int size()
    {
        return points.size();
    }
    
    /**
     * Adds the point to the set (if it is not already in the set)
     * 
     * @param p the point to add
     * @throws NullPointerException if the point is null
     */
    public void insert(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");
        points.add(p);
    }
    
    /**
     * Does the set contain point p? 
     * 
     * @param p the point
     * @return true if the set contains p;
     *         false otherwise
     * @throws NullPointerException if the point is null
     */
    public boolean contains(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");       
        return points.contains(p);
    }
    
    /**
     * Draws all points to standard draw.
     */
    public void draw()
    {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : points)
            point.draw();
    }
    
    /**
     * Returns all points that are inside the rectangle as an {@code Iterable}.
     * 
     * @param rect the rectangle 
     * @return all points inside rect
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
     * Returns a nearest neighbor in the set to point p; 
     * null if the set is empty.
     * 
     * @param p the point
     * @return a nearest neighbor in the set to p
     */
    public Point2D nearest(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");  
        if (points.isEmpty()) return null;
        
        Point2D nearestPoint = points.min();
        double minDist = Double.POSITIVE_INFINITY;
        
        for (Point2D point : points)
        {
            double dist = p.distanceSquaredTo(point);
            
            if (minDist > dist) 
            {
                nearestPoint = point;
                minDist = dist;
            }
        }
        return nearestPoint;
    }
    
    /**
     * Unit tests the {@code PointSET} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        double timeOfInsert = 0.0;
        double timeOfNearest = 0.0;
        double timeOfRange = 0.0;
        PointSET brute = new PointSET();
        Stopwatch timer;
        Point2D p;
        
        for (int i = 0; i < 1000000; i++)
        {
            p = new Point2D(StdRandom.uniform(0.0, 1.0), 
                            StdRandom.uniform(0.0, 1.0));
            timer = new Stopwatch();
            brute.insert(p);
            timeOfInsert += timer.elapsedTime();
        }
        StdOut.print("time cost of insert(random point)(1M times)    : ");
        StdOut.println(timeOfInsert);
        
        for (int i = 0; i < 100; i++)
        {
            p = new Point2D(StdRandom.uniform(0.0, 1.0), 
                            StdRandom.uniform(0.0, 1.0));
            timer = new Stopwatch();
            brute.nearest(p);
            timeOfNearest +=  timer.elapsedTime();
        }
        StdOut.print("time cost of nearest(random point)(100 times)  : ");
        StdOut.println(timeOfNearest);
        
        for (int i = 0; i < 100; i++)
        {
            double xmin = StdRandom.uniform(0.0, 1.0);
            double ymin = StdRandom.uniform(0.0, 1.0);
            double xmax = StdRandom.uniform(0.0, 1.0);
            double ymax = StdRandom.uniform(0.0, 1.0);
            RectHV rect;
            
            if (xmin > xmax) 
            {
                double swap = xmin;
                
                xmin = xmax;
                xmax = swap;
            }
            if (ymin > ymax) 
            {
                double swap = ymin;
                
                ymin = ymax;
                ymax = swap;
            }
            rect = new RectHV(xmin, ymin, xmax, ymax);
            timer = new Stopwatch();
            brute.range(rect);
            timeOfRange += timer.elapsedTime();
        }
        StdOut.print("time cost of range(random rectangle)(100 times): ");
        StdOut.println(timeOfRange);
    }
}