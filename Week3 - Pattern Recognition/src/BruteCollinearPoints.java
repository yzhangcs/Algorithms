import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Insertion;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

/**
 * The {@code BruteCollinearPoints} class examines 4 points at a time 
 * and checks whether they all lie on the same line segment, returning 
 * all such line segments. 
 * <p>
 * To check whether the 4 points p, q, r, and s are collinear, 
 * check whether the three slopes between p and q, between p and r, 
 * and between p and s are all equal.
 *
 * @author zhangyu
 * @date 2017.3.19
 */
public class BruteCollinearPoints
{
    private ArrayList<LineSegment> lineSgmts; // ArrayList to save line segments
    private Point[] pointSet;                 // copy of the give array points
    
    /**
     * Finds all line segments containing 4 points.
     * 
     * @param points the given Point array
     * @throws NullPointerException if the Point array is null 
     */
    public BruteCollinearPoints(Point[] points)
    {
        if (points == null) throw new NullPointerException("Null Point array");
        
        int len = points.length;

        pointSet = Arrays.copyOf(points, points.length); // copy original array
        lineSgmts = new ArrayList<LineSegment>();
        for (int i = 0; i < len; ++i)
        {
            for (int j = i + 1; j < len; ++j)
            {
                checkForDuplicates(pointSet[i], pointSet[j]); // avoid duplicate or empty point
                for (int k = j + 1; k < len; ++k)
                { 
                    // only when three points are collinear, can the forth point be compared
                    if (pointSet[i].slopeTo(pointSet[j]) == pointSet[i].slopeTo(pointSet[k]))
                    {
                        for (int l = k + 1; l < len; ++l)
                        {
                            // if four points are collinear
                            if (pointSet[i].slopeTo(pointSet[k]) == pointSet[i].slopeTo(pointSet[l]))
                            {
                                Point[] tuple = {pointSet[i], pointSet[j], 
                                                 pointSet[k], pointSet[l]};
                                // sort the array to find two end points of the segment
                                Insertion.sort(tuple); // Insertion sort is suitable for this small array
                                lineSgmts.add(new LineSegment(tuple[0], tuple[3]));
                            }
                        }
                    }
                }
            }
        }
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
    public LineSegment[] segments()
    {
        return lineSgmts.toArray(new LineSegment[numberOfSegments()]);
    }

    /**
     * Unit tests the {@code BruteCollinearPoints} data type.
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) 
        {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
