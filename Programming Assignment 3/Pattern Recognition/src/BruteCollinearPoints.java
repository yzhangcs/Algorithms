import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Quick;

public class BruteCollinearPoints
{
    private LinkedQueue<LineSegment> lineSgmts;
    
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null) throw new NullPointerException();
        
        int len = points.length;
        
        lineSgmts = new LinkedQueue<LineSegment>();
        for (int i = 0; i < len; ++i)
        {
            for (int j = i + 1; j < len; ++j)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Repeated point");
            for (int j = i + 1; j < len; ++j)
            {
                for (int k = j + 1; k < len; ++k)
                {
                    if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]))
                    {
                        for (int l = k + 1; l < len; ++l)
                        {
                            if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[l]))
                            {
                                Point[] pointSet = {points[i], points[j], points[k], points[l]};
                                
                                Quick.sort(pointSet);
                                lineSgmts.enqueue(new LineSegment(pointSet[0], pointSet[3]));                   
                            }
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments()        // the number of line segments
    {
        return lineSgmts.size(); 
    }
    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] sgmts = new LineSegment[numberOfSegments()];
        int i = 0;
        
        for (LineSegment ls : lineSgmts) sgmts[i++] = ls;
        return sgmts;
    }
    public static void main(String[] args)
    {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) 
        {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
