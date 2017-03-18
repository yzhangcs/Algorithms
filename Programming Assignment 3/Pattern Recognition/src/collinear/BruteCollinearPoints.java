import java.util.Arrays;

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
        
        Point[] pointSet = Arrays.copyOf(points, points.length);
        
        lineSgmts = new LinkedQueue<LineSegment>();
        findCollinearPoints(pointSet);
    }
    
    private void findCollinearPoints(Point[] pointSet)
    {
        int len = pointSet.length;
        
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
                                lineSgmts.enqueue(new LineSegment(tuple[0], tuple[3]));
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void checkForDuplicates(Point p, Point q)
    {
        if (p == null || q == null) throw new NullPointerException();
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
