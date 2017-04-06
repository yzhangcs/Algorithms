import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
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
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean isEvenLevel; 
        
        public Node(Point2D p, RectHV rect, boolean isEvenLevel)
        {
            this.p = p;
            this.rect = rect;
            this.isEvenLevel = isEvenLevel;
        }
     }
    
    /**
     * construct an empty set of points 
     */
    public KdTree() { }
    
    /**
     * is the set empty? 
     * 
     * @return
     */
    public boolean isEmpty()
    {
        return size == 0;
    }
    
    /**
     * number of points in the set 
     * 
     * @return
     */
    public int size()
    {
        return size;
    }
    
    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param p
     */
    public void insert(Point2D p)
    {
        if (p == null) throw new NullPointerException("Null point");
        
        root = insert(root, p, new RectHV(0.0, 0.0, 1.0, 1.0), true);
    }
    
    private Node insert(Node x, Point2D p, RectHV rect, boolean isEvenLevel) 
    {
        if (x == null)
        {
            size++;
            return new Node(p, rect, isEvenLevel);
        }
        
        int cmp = compare(x, p);
        RectHV rectOfChild;
        
        if (cmp < 0) 
        {
            if (x.isEvenLevel)
                rectOfChild = new RectHV(x.rect.xmin(), x.rect.ymin(), 
                                         x.p.x(),       x.rect.ymax());
            else 
                rectOfChild = new RectHV(x.rect.xmin(), x.rect.ymin(), 
                                         x.rect.xmax(), x.p.y());
            x.lb = insert(x.lb, p, rectOfChild, !x.isEvenLevel);
        }
        else if (cmp > 0) 
        {
            if (x.isEvenLevel)
                rectOfChild = new RectHV(x.p.x(),       x.rect.ymin(), 
                                         x.rect.xmax(), x.rect.ymax());
            else
                rectOfChild = new RectHV(x.rect.xmin(), x.p.y(), 
                                         x.rect.xmax(), x.rect.ymax());
            x.rt = insert(x.rt, p, rectOfChild, !x.isEvenLevel);
        }
        return x;
    }
    
    public int compare(Node x, Point2D p) 
    {
        if (x.p.equals(p)) return 0;
        if (x.isEvenLevel) 
        {
            if (p.x() < x.p.x()) return -1;
            else                 return 1;
        }
        else
        {
            if (p.y() < x.p.y()) return -1;
            else                 return 1;
        }
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
        
        return contains(root, p);
    }
    
    private boolean contains(Node x, Point2D p) 
    {
        if (x == null) return false;
        
        int cmp = compare(x, p);
        
        if      (cmp < 0) return contains(x.lb, p);
        else if (cmp > 0) return contains(x.rt, p);
        else              return true;
    }
    
    /**
     * draw all points to standard draw 
     * 
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
     * all points that are inside the rectangle 
     * 
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null) throw new NullPointerException("Null rectangle");
        
        Queue<Point2D> pointsInRect = new Queue<Point2D>();
        
        range(root, pointsInRect, rect);
        return pointsInRect;
    }
    
    private void range(Node x, Queue<Point2D> pointsInRect, RectHV rect) 
    { 
        if (x == null) return; 
        if (x.lb != null && rect.intersects(x.lb.rect)) range(x.lb, pointsInRect, x.lb.rect);
        if (rect.contains(x.p)) pointsInRect.enqueue(x.p);
        if (x.rt != null && rect.intersects(x.rt.rect)) range(x.rt, pointsInRect, x.rt.rect);
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
        
        Node nearest = nearest(root, p);
        
        return nearest.p;
    }
    
    private Node nearest(Node x, Point2D p)
    {
        Node nearest = x;
        double dist = p.distanceTo(nearest.p);
        
        if (x.lb != null) 
        {
            if (p.distanceTo(x.lb.p) < p.distanceTo(nearest.p) && isUnique(x, x.lb, p)) 
                return nearest(x.lb, p);
            else 
            {
                Node nearestLB = nearest(x.lb, p);
                
                if (p.distanceTo(nearestLB.p) < p.distanceTo(nearest.p)) 
                    nearest = nearestLB;
            }
        }
        if (x.rt != null)
        {
            if (p.distanceTo(x.rt.p) < dist && isUnique(x, x.rt, p)) 
                return nearest(x.rt, p);
            else 
            {
                Node nearestRT = nearest(x.rt, p);
                
                if (p.distanceTo(nearestRT.p) < p.distanceTo(nearest.p)) 
                    nearest = nearestRT;
            }
        }
        return nearest;
    }
    
    private boolean isUnique(Node x, Node child, Point2D p)
    {
        if (compare(x, p) != compare(x, child.p)) return false;
        if (x.isEvenLevel) 
            return Math.abs(p.x() - x.p.x()) > Math.abs(child.p.x() - x.p.x());
        else
            return Math.abs(p.y() - x.p.y()) > Math.abs(child.p.y() - x.p.y());
    }
    
    /**
     * unit testing of the methods (optional) 
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        String filename = args[0];
        In in = new In(filename);

        StdDraw.enableDoubleBuffering();

        // initialize the two data structures with point from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) 
        {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            kdtree.draw();
            StdDraw.show();
        }
        while (true)
        {
            if (StdDraw.mousePressed()) 
            {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                p.draw();
                p.drawTo(kdtree.nearest(p));
                StdDraw.show();
            }
            StdDraw.pause(50);
        }
    }
}
