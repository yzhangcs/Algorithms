import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 *
 * @author zhangyu
 * @date 2017.3.19
 */
public class FastCollinearPoints
{
    private ArrayList<LineSegment> lineSgmts;
    private Point[] pointSet;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points)     
    {
        if (points == null) throw new NullPointerException("Null Point array");
        
        int len = points.length;
        
        pointSet = Arrays.copyOf(points, points.length);
        lineSgmts = new ArrayList<LineSegment>();
        Merge.sort(pointSet);
        for (int i = 0; i < len - 1; ++i)
        {
            int cnt = 1, subLen = len - i - 1;
            double slope = Double.NEGATIVE_INFINITY;
            double lastSlope = Double.NEGATIVE_INFINITY;
            Point[] subPointSet = new Point[subLen];
            
            for (int j = 0; j < subLen; ++j) subPointSet[j] = pointSet[i + j + 1];
            Arrays.sort(subPointSet, pointSet[i].slopeOrder());
            for (int j = 0; j < subLen; ++j) 
            {
                checkForDuplicates(pointSet[i], subPointSet[j]);
                slope = pointSet[i].slopeTo(subPointSet[j]);
                if (lastSlope != slope)
                {
                    if (cnt >= 3) addSgmt(pointSet[i], subPointSet[j - 1]);
                    cnt = 1;
                }
                else cnt++;
                lastSlope = slope;
            }
            if (cnt >= 3) addSgmt(pointSet[i], subPointSet[subLen - 1]);
        }
    }
    
    // check whether or not duplicate point exists
    private void checkForDuplicates(Point p, Point q)
    {
        // ensure each point is not null
        if (p == null || q == null) throw new NullPointerException("Null Point element");
        if (p.compareTo(q) == 0)    throw new IllegalArgumentException("Duplicate point");
    }
    
    private void addSgmt(Point srtPoint, Point endPoint)
    {
        double slope = srtPoint.slopeTo(endPoint);
        
        for (Point p : pointSet)
        {
            if (p == srtPoint) break;
            if (p.slopeTo(endPoint) == slope) return;
        }
        lineSgmts.add(new LineSegment(srtPoint, endPoint));
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
