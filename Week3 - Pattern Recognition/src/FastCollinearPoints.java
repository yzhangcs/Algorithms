import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code FastCollinearPoints} class examines 4 or more points 
 * at a time and checks whether they all lie on the same line segment, 
 * returning all such line segments. 
 *
 * @author zhangyu
 * @date 2017.3.19
 */
public class FastCollinearPoints
{
    private ArrayList<LineSegment> lineSgmts; // ArrayList to save line segments
    private Point[] pointSet;                 // copy of the give Point array
    
    /**
     * Finds all line segments containing 4 or more points.
     * 
     * @param points the given Point array
     * @throws NullPointerException if the Point array is null 
     */
    public FastCollinearPoints(Point[] points)     
    {
        if (points == null) throw new NullPointerException("Null Point array");
        
        int len = points.length;
        
        pointSet = Arrays.copyOf(points, points.length);
        lineSgmts = new ArrayList<LineSegment>();
        Merge.sort(pointSet); // sort the array by compareTo() method in Point class
        for (int i = 0; i < len - 1; ++i)
        {
            double[] slopesBefore = new double[i]; // slopes of previous points to pointSet[i]
            Point[] pointsAfter = new Point[len - i - 1]; // points after pointSet[i]
            
            for (int j = 0; j < i; ++j) 
                slopesBefore[j] = pointSet[i].slopeTo(pointSet[j]);
            for (int j = 0; j < len - i - 1; ++j) pointsAfter[j] = pointSet[i + j + 1];
            Arrays.sort(slopesBefore); // for binary search
            // sorting brings points collinear with pointSet[i] together
            Arrays.sort(pointsAfter, pointSet[i].slopeOrder());
            addNewSgmts(slopesBefore, pointSet[i], pointsAfter);   
        }
    }
    
    // add segments to the set if they are not sub-segments
    private void addNewSgmts(double[] slopesBefore, Point srtPoint, Point[] pointsAfter)
    {
        int cnt = 1;
        int lenOfSub = pointsAfter.length;
        double slope = Double.NEGATIVE_INFINITY;
        double lastSlope = Double.NEGATIVE_INFINITY;
        
        for (int j = 0; j < lenOfSub; ++j) 
        {
            checkForDuplicates(srtPoint, pointsAfter[j]);
            slope = srtPoint.slopeTo(pointsAfter[j]);
            if (lastSlope != slope) // if current slope isn't equal to the last
            {
                // 4 or more points are collinear, then add them to the sgmts set.
                if (cnt >= 3 && !isSubSgmt(slopesBefore, lastSlope)) 
                    lineSgmts.add(new LineSegment(srtPoint, pointsAfter[j - 1])); 
                cnt = 1;
            }
            else cnt++;
            lastSlope = slope;
        }
        // the last sgmt can be left out when the loop terminates
        if (cnt >= 3 && !isSubSgmt(slopesBefore, lastSlope)) 
            lineSgmts.add(new LineSegment(srtPoint, pointsAfter[lenOfSub - 1]));
    }
    
    // determine if the segment is a sub-segment of the previous segments
    private boolean isSubSgmt(double[] slopesBefore, double slope)
    {
        int lo = 0;
        int hi = slopesBefore.length - 1;
        
        // use binary search
        while (lo <= hi) 
        {
            int mid = lo + (hi - lo) / 2;
            if      (slope < slopesBefore[mid]) hi = mid - 1;
            else if (slope > slopesBefore[mid]) lo = mid + 1;
            else return true;
        }
        return false;
    }
    
    // check whether duplicate point exists
    private void checkForDuplicates(Point p, Point q)
    {
        // ensure each point is not null
        if (p == null || q == null) throw new NullPointerException("Null Point element");
        if (p.compareTo(q) == 0)    throw new IllegalArgumentException("Duplicate point");
    }
    
    /**
     * Returns the number of line segments.
     * 
     * @return the number of line segments
     */
    public int numberOfSegments()
    {
        return lineSgmts.size(); 
    }

    /**
     * Returns the line segments in array.
     * 
     * @return a LineSegment array
     */
    public LineSegment[] segments()                // the line segments
    {
        return lineSgmts.toArray(new LineSegment[numberOfSegments()]);
    }
    
    /**
     * Unit tests the {@code FastCollinearPoints} data type.
     *
     * @param args the command-line arguments
     */
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
        for (Point p : points) { p.draw(); }
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