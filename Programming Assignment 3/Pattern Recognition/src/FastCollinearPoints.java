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
    private Point[] pointSet;                 // copy of the give array points
    
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
            int cnt = 1, subLen = len - i - 1;
            double slope = Double.NEGATIVE_INFINITY;
            double lastSlope = Double.NEGATIVE_INFINITY;
            Point[] subPointSet = new Point[subLen];
            
            for (int j = 0; j < subLen; ++j) subPointSet[j] = pointSet[i + j + 1];
            // points that have equal slopes with respect to p are collinear, 
            // and sorting brings such points together
            Arrays.sort(subPointSet, pointSet[i].slopeOrder());
            for (int j = 0; j < subLen; ++j) 
            {
                checkForDuplicates(pointSet[i], subPointSet[j]);
                slope = pointSet[i].slopeTo(subPointSet[j]);
                if (lastSlope != slope) // if current slope isn't equal to the last
                {
                    // 4 or more points are collinear, then add them to the sgmts set.
                    if (cnt >= 3) addSgmt(pointSet[i], subPointSet[j - 1]); 
                    cnt = 1;
                }
                else cnt++;
                lastSlope = slope;
            }
            // the last sgmt can be left out when the loop terminates
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
    
    // add new segment to the segment set if it is a maximal
    // line segment containing the two endpoints.
    private void addSgmt(Point srtPoint, Point endPoint)
    {
        // calculate the slope of current line segment.
        double slope = srtPoint.slopeTo(endPoint);
        
        // check if current sgmt is part of the previous ones
        for (Point p : pointSet)
        {
            if (p == srtPoint) break;
            // if srtPoint, endPoint and previous one are collinear, that is 
            // to say current sgmt has been contained because pointSet is sorted
            if (p.slopeTo(endPoint) == slope) return;
        }
        lineSgmts.add(new LineSegment(srtPoint, endPoint));
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
        return (LineSegment[]) lineSgmts.toArray(new LineSegment[numberOfSegments()]);
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
