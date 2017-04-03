import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

/**
 * 
 *
 * @author zhangyu
 * @date 2017.4.3
 */
public class KdTree
{
    /**
     * construct an empty set of points 
     */
    public KdTree()
    {
          
    }
    
    /**
     * is the set empty? 
     * 
     * @return
     */
    public boolean isEmpty()
    {
        return false;
    }
    
    /**
     * number of points in the set 
     * 
     * @return
     */
    public int size()
    {
         return 0;
    }
    
    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param p
     */
    public void insert(Point2D p)
    {
         
    }
    
    /**
     * does the set contain point p? 
     * 
     * @param p
     * @return
     */
    public boolean contains(Point2D p)
    {
         return false;
    }
    
    /**
     * draw all points to standard draw 
     * 
     */
    public void draw()
    {
         
    }
    
    /**
     * all points that are inside the rectangle 
     * 
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect)
    {
         return new Queue<Point2D>();
    }
    
    /**
     * a nearest neighbor in the set to point p; null if the set is empty  
     * 
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p)
    {
         return new Point2D(1.0, 1.0);
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
