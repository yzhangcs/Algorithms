import java.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class FastCollinearPoints
{
    private ArrayList<LineSegment> lineSgmts;
//    private ArrayList<Point> endPointOfSgmts;
//    private ArrayList<Double> slopeOfSgmts;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points)     
    {
        if (points == null) throw new NullPointerException();
        
        Point[] pointSet = Arrays.copyOf(points, points.length);
        
        lineSgmts = new ArrayList<LineSegment>();
//        endPointOfSgmts = new ArrayList<Point>();
//        slopeOfSgmts = new ArrayList<Double>();
        findCollinearPoints(pointSet);
    }
    
    private void findCollinearPoints(Point[] pointSet)
    {
        int len = pointSet.length;
        
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
                    if (cnt >= 3) addSgmt(pointSet, i, subPointSet[j - 1]);
                    cnt = 1;
                }
                else cnt++;
                lastSlope = slope;
            }
            if (cnt >= 3) addSgmt(pointSet, i, subPointSet[subLen - 1]);
        }
    }
    
    private void checkForDuplicates(Point p, Point q)
    {
        if (p == null || q == null) throw new NullPointerException();
        if (p.compareTo(q) == 0)    throw new IllegalArgumentException("Duplicate point");
    }
    
    private void addSgmt(Point[] pointSet, int idx, Point endPoint)
    {
        double slope = pointSet[idx].slopeTo(endPoint);
        
        for (int i = 0; i < idx; ++i)
            if (pointSet[i].slopeTo(endPoint) == slope) return;
//        for (Point ep : endPointOfSgmts)
//        {
//            if (ep.compareTo(endPoint) == 0)
//                if (slopeOfSgmts.get(i) == srtPoint.slopeTo(endPoint))
//                    return;
//            i++;
//        }
        lineSgmts.add(new LineSegment(pointSet[idx], endPoint));
//        endPointOfSgmts.add(endPoint);
//        slopeOfSgmts.add(srtPoint.slopeTo(endPoint));
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
        Stopwatch timer = new Stopwatch();
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments())
        {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdOut.println(timer.elapsedTime());
    }
}
