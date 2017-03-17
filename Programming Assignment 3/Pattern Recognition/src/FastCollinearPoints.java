import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.Shell;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints
{
    private LinkedQueue<LineSegment> lineSgmts;
    
    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) throw new NullPointerException();
        
        int len = points.length;
        
        Shell.sort(points);
        lineSgmts = new LinkedQueue<LineSegment>();
        for (int i = 0; i < len; ++i)
        {
            int cnt = 1;
            int subLen = len - i - 1;
            Point[] subPoints = new Point[subLen];
            
            for (int j = 0; j < subLen; ++j) 
            {
                subPoints[j] = points[j + i + 1];
                if (points[i].compareTo(subPoints[j]) == 0)
                    throw new IllegalArgumentException("Repeated point");
            }
            Arrays.sort(subPoints, points[i].slopeOrder());
            for (int j = 0; j < subLen - 1; ++j) 
            {
                if (points[i].slopeTo(subPoints[j]) != points[i].slopeTo(subPoints[j + 1]))
                {
                    if (cnt >= 3) lineSgmts.enqueue(new LineSegment(points[i], subPoints[j]));
                    cnt = 1;
                }
                else cnt++;
            }
            if (cnt >= 3) lineSgmts.enqueue(new LineSegment(points[i], subPoints[subLen - 1]));
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
        for (Point p : points) 
        {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments())
        {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
