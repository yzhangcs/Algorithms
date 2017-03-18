import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 
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
     */
    public BruteCollinearPoints(Point[] points)    // 
    {
        if (points == null) throw new NullPointerException("Null Point array");
        
        int len = points.length;
        
        pointSet = Arrays.copyOf(points, points.length);
        lineSgmts = new ArrayList<LineSegment>();
        for (int i = 0; i < len; ++i)
        {
            for (int j = i + 1; j < len; ++j)
            {
                checkForDuplicates(pointSet[i], pointSet[j]);
                for (int k = j + 1; k < len; ++k)
                { 
                    if (pointSet[i].slopeTo(pointSet[j]) == pointSet[i].slopeTo(pointSet[k]))
                    {
                        for (int l = k + 1; l < len; ++l)
                        {
                            if (pointSet[i].slopeTo(pointSet[k]) 
                                    == pointSet[i].slopeTo(pointSet[l]))
                            {
                                Point[] tuple = {pointSet[i], pointSet[j], 
                                                 pointSet[k], pointSet[l]};
                                
                                Quick.sort(tuple);
                                lineSgmts.add(new LineSegment(tuple[0], tuple[3]));
                            }
                        }
                    }
                }
            }
        }
    }
    
    // check whether or not duplicate point exists
    private void checkForDuplicates(Point p, Point q)
    {
        // ensure each point is not null
        if (p == null || q == null) throw new NullPointerException("Null Point element");
        if (p.compareTo(q) == 0)    throw new IllegalArgumentException("Duplicate point");
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
