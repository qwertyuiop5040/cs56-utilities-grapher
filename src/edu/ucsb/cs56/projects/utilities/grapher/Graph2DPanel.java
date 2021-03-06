package edu.ucsb.cs56.projects.utilities.grapher;
import javax.swing.*; // JPanel
import java.awt.*; // Graphics, Graphics2D
import java.awt.geom.*; // GeneralPath
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.*;
import java.awt.event.*;
//import java.lang.Math.*;
/**
   A JPanel subclass that draws a function to the screen.
   @author Ryan Halbrook
   @version CS56, Spring 2013
 */
public class Graph2DPanel extends JPanel implements ActionListener {
    private ArrayList<FunctionR1R1DisplayData> functions = new ArrayList<FunctionR1R1DisplayData>();
    private FunctionR1R1DisplayDataList fnsdd;
    private Bounds2DFloat bounds;

    private Color background = Color.BLACK;
    private Color foreground = Color.WHITE;
    
    // Used to only redraw the graph if necessary.
    private boolean graphIsValid = false;
    // A path of the graph that can be drawn
    private GeneralPath graph = null;

    private float xScale = 10.0f;
    private float yScale = 10.0f;

    private boolean debug = true;

    /**
       Constructs a new object that will graph the supplied function
       within the given bounds on the function. When the function is
       redrawn it will use this same object.
       @param function the function to graph.
       @param b the rectangular boundary that determines how much of the graph will be shown.
     */
    public Graph2DPanel(FunctionR1R1 function, Bounds2DFloat b, FunctionR1R1DisplayDataList fnsdd) {
	super();
	this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	this.fnsdd = fnsdd; 
	functions.add(new FunctionR1R1DisplayData(function, Color.GREEN));
	this.bounds = b;
	MouseResponder mr = new MouseResponder();
	this.addMouseListener(mr);
	this.addMouseMotionListener(mr);
	
	setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	
	if(this.bounds == null) {
	    this.bounds = new Bounds2DFloat();
	}
	this.bounds.addActionListener(this);
    }

    /**
       Tell the graph to redraw itself, usually because the
       rendering information, such as the bounds has changed.
     */
    public void refresh() {
	graphIsValid = false;
	repaint();
    }

    /**
       Paints the component onscreen. Only re-renders from scratch if
       graphIsValid is false. This should not be called directly. 
       Use repaint().
       @param g the graphics object which will perform onscreen drawing.
     */
    public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D)g; // Cast for more features.
	g2.setColor(background);
	g2.fillRect(0, 0, ((int)this.getSize().getWidth()),
		    (int)(this.getSize().getHeight()));
	g2.setColor(foreground);
	drawAxes(g2);
	
	if (!graphIsValid) updatePaths();

	for (FunctionR1R1DisplayData fdd : fnsdd) {
	    
	    if (fdd == null) System.out.println("fdd was null");
	    double height = this.getSize().getHeight();
	    AffineTransform at = new AffineTransform();
	    at.translate(0, (height / 2));
	    
	    g2.setStroke(new BasicStroke(3));
	    g2.setColor(fdd.getColor());
	    g2.draw(fdd.getPath().createTransformedShape(at));
	}
    }

    /**
       Draws a set of axes with the graphics object g.
       @param g the destination for drawing.
     */
    private void drawAxes(Graphics g) {
	
        float width = (float)this.getSize().getWidth();
	float height = (float)this.getSize().getHeight();
	
	// Draw the x axis
	g.drawLine(0, (int)(height / 2)+ (int)bounds.getYMin(), (int)width, (int)(height / 2)+(int)bounds.getYMin());
	
	// Draw the y axis
	//int xScale = (int)(width / (bounds.getXMax() - bounds.getXMin()));
	//g.drawLine(-1*(int)bounds.getXMin()*(int)xScale, 0, -1*(int)bounds.getXMin()*(int)xScale, (int)height);

     }
    
        /**
        Updates the paths representing each graph.
        */
    private void updatePaths() {
        double width = this.getSize().getWidth();
	double height = this.getSize().getHeight();
	int i = 0;
	for (FunctionR1R1DisplayData fdd : fnsdd) {
	    fdd.buildPath(bounds, width, height);
	    i++;
	}
	repaint();
    }
    
    /**
       Call back for the event that the bounds are changed.
       @param e the ActionEvent object.
     */
    public void actionPerformed(ActionEvent e) {
	    this.refresh();
    }
    
    public class MouseResponder extends MouseAdapter {
	public static final float SCALE_FACTOR_PER_WHEEL_NOTCH=0.8f;

        double prevX = -1;
        double prevY = -1;
        private boolean controlHeld = false;

        public void mouseDragged(MouseEvent e) {
            if (prevY == -1) {
                prevX = e.getX();
                prevY = e.getY();
            } else {
		float xScale = (float)(getSize().getWidth() / (bounds.getXMax() - bounds.getXMin()));
                double deltaX = -((e.getX() - prevX) / xScale);
                double deltaY = (e.getY() - prevY);
                //if (deltaX < 1) deltaX = 0;
                bounds.translate(deltaX, deltaY);
                prevX = e.getX(); prevY = e.getY();
            }
        }
        public void mousePressed(MouseEvent e) {
            prevX = e.getX(); prevY = e.getY();
        }
	 @Override
        public void mouseWheelMoved(MouseWheelEvent e){
                int notches=e.getWheelRotation();
                System.out.println("hi the mouse wheel moved");
                float finalScaleFactor=(float)Math.pow(SCALE_FACTOR_PER_WHEEL_NOTCH,(double)notches);
                bounds.scale(finalScaleFactor);
        }

    }

}
