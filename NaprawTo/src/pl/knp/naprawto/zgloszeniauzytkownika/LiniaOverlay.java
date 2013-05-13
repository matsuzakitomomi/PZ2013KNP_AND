package pl.knp.naprawto.zgloszeniauzytkownika;

import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LiniaOverlay extends Overlay {
	
	private List<GeoPoint> m_areas;
    private Paint m_paintFill;
    private Paint m_paintStroke;
    private static final int ALPHA = 0x20ffffff; // 48 out of 255 transparent
    private static int COLORS = Color.BLUE;

    private boolean dodawanie;
    Path areaPaths;
    Projection projection;
    static {LiniaOverlay.COLORS &= LiniaOverlay.ALPHA;}
        
    
    
    public LiniaOverlay(final List<GeoPoint> points,boolean dodawanie) {
    	
    	this.dodawanie=dodawanie;
    	
        m_areas = points;

        // prepare paints
        m_paintFill = new Paint();
        m_paintFill.setStyle(Paint.Style.FILL);
        m_paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_paintStroke.setStyle(Style.STROKE);
        //dopisane
        m_paintStroke.setFilterBitmap(true);
        m_paintStroke.setAntiAlias(true);
        m_paintStroke.setStrokeWidth(3);
    }
    
    @Override
    public void draw(final Canvas canvas, final MapView mapView, final boolean shadow) {
        super.draw(canvas, mapView, shadow);
        if (shadow) 
        {
            return;
        }
        int lonSpanLevel = mapView.getZoomLevel();
        
        if(lonSpanLevel<14)
        {
        	if(projection==null)
                projection = mapView.getProjection();
               	areaPaths = getPath(projection, m_areas);
               	drawPaths(canvas, areaPaths);
        }     
    }
    
    private Path getPath(final Projection projection, final List<GeoPoint> areas) {
           
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        Iterator<GeoPoint> it = areas.iterator();
            
        Point point = nextDrawPoint(projection, it);
        path.moveTo(point.x, point.y);
            
        while (it.hasNext()) 
        {
                point = nextDrawPoint(projection, it);
                path.lineTo(point.x, point.y);
        }
        path.close();
        
        return path;
    }
    
    private void drawPaths(final Canvas canvas, final Path path) {

        
       int currentColor = Color.DKGRAY;
       m_paintStroke.setColor(currentColor);
       canvas.drawPath(path, m_paintStroke);
       
       
       if(!dodawanie)
       {
	       int currentColor2 = LiniaOverlay.COLORS;
	       m_paintFill.setColor(currentColor2);
	       canvas.drawPath(path, m_paintFill);
       }
    }
    
    private Point nextDrawPoint(final Projection projection, final Iterator<GeoPoint> it) {
        GeoPoint geo = it.next();
        Point p = new Point();
        projection.toPixels(geo, p);
        return p;
    }

}
