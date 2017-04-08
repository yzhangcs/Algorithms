import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * The {@code KdTree} class represents a set of points 
 * in the unit square. It supports efficient 
 * <em>range search</em> (find all of the points contained 
 * in a query rectangle) and <em>nearest neighbor search</em> 
 * (find a closest point to a query point) by using a 2d-tree.
 *
 * @author zhangyu
 * @date 2017.4.3
 */
public class KdTree
{
    private Node root;
    private int size;
    
    private static class Node
    {
        private Point2D p;           // the point
        private RectHV rect;         // the axis-aligned rectangle corresponding to this node
        private Node lb;             // the left/bottom subtree
        private Node rt;             // the right/top subtree
        private boolean isEvenLevel; // is the node at even level
        
        public Node(Point2D p, RectHV rect, boolean isEvenLevel)
        {
            this.p = p;
            this.rect = rect;
            this.isEvenLevel = isEvenLevel;
        }
     }
    
    /**
     * Initializes an empty 2d-tree.
     */
    public KdTree() { }
    
    /**
     * Returns true if the 2d-tree is empty.
     * 
     * @return true if the 2d-tree is empty; 
     *         false otherwise
     */
    public boolean isEmpty()
    {
        return size == 0;
    }
    
    /**
     * Returns the number of nodes in the 2d-tree.
     * 
     * @return the number of nodes in the 2d-tree 
     */
    public int size()
    {
        return size;
    }
    
    /**
     * Inserts point into the 2d-tree.
     *
     * @param  p the point
     * @throws NullPointerException if the point is null
     */
    public void insert(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");
        
        root = insert(root, null, p, 0);
    }
    
    private Node insert(Node x, Node parent, Point2D p, int direction) 
    {
        if (x == null)
        {
            // if 2d-tree is null, then insert Node with a unit rectangle
            if (size++ == 0) return new Node(p, new RectHV(0, 0, 1, 1), true);
            
            RectHV rectOfX = parent.rect; // rectangle of Node x
            
            if (direction < 0) // go left sub-tree
            {
                if (parent.isEvenLevel) // left sub-rectangle
                    rectOfX = new RectHV(parent.rect.xmin(), parent.rect.ymin(), 
                                         parent.p.x(),       parent.rect.ymax());
                else  // bottom sub-rectangle
                    rectOfX = new RectHV(parent.rect.xmin(), parent.rect.ymin(), 
                                         parent.rect.xmax(), parent.p.y());
            }
            else if (direction > 0) // go right sub-tree
            {
                if (parent.isEvenLevel)  // right sub-rectangle
                    rectOfX = new RectHV(parent.p.x(),       parent.rect.ymin(), 
                                         parent.rect.xmax(), parent.rect.ymax());
                else // top sub-rectangle
                    rectOfX = new RectHV(parent.rect.xmin(), parent.p.y(), 
                                         parent.rect.xmax(), parent.rect.ymax());
            }
            return new Node(p, rectOfX, !parent.isEvenLevel);
        }
        
        int cmp = compare(p, x.p, x.isEvenLevel);

        if      (cmp < 0) x.lb = insert(x.lb, x, p, cmp);
        else if (cmp > 0) x.rt = insert(x.rt, x, p, cmp);
        return x;
    }
    
    private int compare(Point2D p, Point2D q, boolean isEvenLevel) 
    {
        if (p == null || q == null) throw new NullPointerException("Null point");
        if (p.equals(q)) return 0;
        if (isEvenLevel) return p.x() < q.x() ? -1 : 1;
        else             return p.y() < q.y() ? -1 : 1;
    }
    
    /**
     * Does the 2d-tree contain point p? 
     * 
     * @param p the point
     * @return true if the 2d-tree contains p;
     *         false otherwise
     * @throws NullPointerException if the point is null
     */
    public boolean contains(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");
        
        return contains(root, p);
    }
    
    private boolean contains(Node x, Point2D p) 
    {
        if (x == null) return false;
        
        int cmp = compare(p, x.p, x.isEvenLevel);
        
        if      (cmp < 0) return contains(x.lb, p);
        else if (cmp > 0) return contains(x.rt, p);
        else              return true;
    }
    
    /**
     * Draws all points to standard draw.
     */
    public void draw()
    {
        draw(root);
    }

    private void draw(Node x)
    {
        if (x == null) return; 
        draw(x.lb);
        draw(x.rt);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        StdDraw.setPenRadius();
        // draw the splitting line segment
        if (x.isEvenLevel) 
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());   
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());   
        }
    } 
    
    /**
     * Returns all points that are inside the rectangle as an {@code Iterable}.
     * 
     * @param rect the rectangle 
     * @return all points inside the rectangle 
     */
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null) throw new NullPointerException("Null rectangle");
        
        Queue<Point2D> pointQueue = new Queue<Point2D>();
        
        range(root, pointQueue, rect);
        return pointQueue;
    }
    
    private void range(Node x, Queue<Point2D> pointQueue, RectHV rect) 
    { 
        if (x == null) return; 
        if (rect.contains(x.p)) pointQueue.enqueue(x.p);
        // if the left sub-rectangle intersects rect, then search the left-tree
        if (x.lb != null && rect.intersects(x.lb.rect)) range(x.lb, pointQueue, rect);
        if (x.rt != null && rect.intersects(x.rt.rect)) range(x.rt, pointQueue, rect);
    } 
    
    /**
     * Returns a nearest neighbor in the 2d-tree to point p; 
     * null if the 2d-tree is empty.
     * 
     * @param p the point
     * @return a nearest neighbor in the 2d-tree to p
     */
    public Point2D nearest(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");
        if (root == null) return null;
        return nearest(root, root.p, p);
    }
    
    private Point2D nearest(Node x, Point2D nearest, Point2D p)
    {
        if (x == null) return nearest;
        
        int cmp = compare(p, x.p, x.isEvenLevel);
        
        if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(nearest)) nearest = x.p;
        if (cmp < 0)
        {
            nearest = nearest(x.lb, nearest, p);
            // compare the current nearest to the possible nearest in the other side
            if (x.rt != null)
                if (nearest.distanceSquaredTo(p) > x.rt.rect.distanceSquaredTo(p))
                    nearest = nearest(x.rt, nearest, p);
        }
        else if (cmp > 0)
        {
            nearest = nearest(x.rt, nearest, p);
            if (x.lb != null)
                if (nearest.distanceSquaredTo(p) > x.lb.rect.distanceSquaredTo(p))
                    nearest = nearest(x.lb, nearest, p);
        }
        return nearest;
    }
    
    /**
     * Unit tests the {@code KdTree} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        double timeOfInsert = 0.0;
        double timeOfNearest = 0.0;
        double timeOfRange = 0.0;
        KdTree kdtree = new KdTree();
        Stopwatch timer;
        Point2D p;
        
        for (int i = 0; i < 1000000; i++)
        {
            p = new Point2D(StdRandom.uniform(0.0, 1.0), 
                            StdRandom.uniform(0.0, 1.0));
            timer = new Stopwatch();
            kdtree.insert(p);
            timeOfInsert += timer.elapsedTime();
        }
        StdOut.print("time cost of insert(random point)(1M times)    : ");
        StdOut.println(timeOfInsert);
        
        for (int i = 0; i < 100; i++)
        {
            p = new Point2D(StdRandom.uniform(0.0, 1.0), 
                            StdRandom.uniform(0.0, 1.0));
            timer = new Stopwatch();
            kdtree.nearest(p);
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
            kdtree.range(rect);
            timeOfRange += timer.elapsedTime();
        }
        StdOut.print("time cost of range(random rectangle)(100 times): ");
        StdOut.println(timeOfRange);
    }
}
