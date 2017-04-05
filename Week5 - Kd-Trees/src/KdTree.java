import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

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
        
        RectHV rectOfChild;
        
        if (x.isEvenLevel && p.x() < x.p.x()) 
        {
            rectOfChild = new RectHV(x.rect.xmin(), x.rect.ymin(), 
                                     x.p.x(),       x.rect.ymax());
            x.lb = insert(x.lb, p, rectOfChild, !x.isEvenLevel);
        }
        else if (x.isEvenLevel && p.x() >= x.p.x()) 
        {
            rectOfChild = new RectHV(x.p.x(),       x.rect.ymin(), 
                                     x.rect.xmax(), x.rect.ymax());
            x.rt = insert(x.rt, p, rectOfChild, !x.isEvenLevel);
        }
        else if (!x.isEvenLevel && p.y() < x.p.y()) 
        {
            rectOfChild = new RectHV(x.rect.xmin(), x.rect.ymin(), 
                                     x.rect.xmax(), x.p.y());
            x.lb = insert(x.lb, p, rectOfChild, !x.isEvenLevel);
        }
        else if (!x.isEvenLevel && p.y() >= x.p.y()) 
        {
            rectOfChild = new RectHV(x.rect.xmin(), x.p.y(), 
                                     x.rect.xmax(), x.rect.ymax());
            x.rt = insert(x.rt, p, rectOfChild, !x.isEvenLevel);
        }
        
        return x;
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
        
        if ((x.isEvenLevel && p.x() < x.p.x()) || (!x.isEvenLevel && p.y() < x.p.y())) 
            return contains(x.lb, p);
        else if ((x.isEvenLevel && p.x() >= x.p.x()) || (!x.isEvenLevel && p.y() >= x.p.y())) 
            return contains(x.rt, p);
        else return true;
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
        
        Node cur = root;
        Point2D nearest = cur.p;
        double dist = p.distanceTo(nearest);
        
        while (cur != null)
        {
            if      (cur.lb != null && p.distanceTo(cur.lb.p) <= dist) cur = cur.lb;
            else if (cur.rt != null && p.distanceTo(cur.rt.p) <= dist) cur = cur.rt;
            else break;
            nearest = cur.p;
        }
        return nearest;
    }
    
    /**
     * unit testing of the methods (optional) 
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        StdDraw.enableDoubleBuffering();

        KdTree kdtree = new KdTree();
        while (!StdIn.isEmpty())
        {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            kdtree.draw();
            StdDraw.show();
        }
    }
}
