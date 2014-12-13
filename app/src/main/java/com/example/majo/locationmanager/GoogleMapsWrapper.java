package com.example.majo.locationmanager;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by majo on 13-Dec-14.
 */
public class GoogleMapsWrapper {

    private GoogleMap map;

    private Marker marker;

    private Polyline polyLine;
    LatLng firstTravelPoint;


    public GoogleMapsWrapper(GoogleMap map){
        this.map = map;
    }




    public void clearAllTravelPoints(){
        clearLastTravelingMarker();
        clearPolyLine();
        map.clear();
    }

    public void addTravelPointLoc(Location travelPoint){
        if (travelPoint == null) return;
        addTravelPoint(new LatLng(travelPoint.getLatitude(), travelPoint.getLongitude()));
    }

    public void addTravelPointsLoc(ArrayList<Location> travelPoints){
        if (travelPoints == null) return;

        ArrayList<LatLng> latLon = new ArrayList<>();
        for (Location travelPoint : travelPoints){
            latLon.add(new LatLng(travelPoint.getLatitude(), travelPoint.getLongitude()));
        }
        addTravelPoints(latLon);
    }

    public void addTravelPoint(LatLng travelPoint){
        if (travelPoint == null) return;
        setNewTravelingMarker(travelPoint);
        addPolyLinePoint(travelPoint);
        centerMap(travelPoint, 13);
    }

    public void addTravelPoints(ArrayList<LatLng> travelPoints){
        if (travelPoints == null) return;
        if (travelPoints.size() <= 0) return;

        for (LatLng travelPoint : travelPoints){
            addPolyLinePoint(travelPoint);
        }

        LatLng travelPoint = travelPoints.get(travelPoints.size() - 1);
        setNewTravelingMarker(travelPoint);
        centerMap(travelPoint, 13);
    }







    private void centerMap(LatLng mapCenter, int mapZoom){
        CameraUpdate newCamera = CameraUpdateFactory.newLatLngZoom(mapCenter, mapZoom);
        map.moveCamera(newCamera);
    }







    private void clearLastTravelingMarker() {
        if (marker != null){
            marker.remove();
        }
    }

    private void setNewTravelingMarker(LatLng markPosition){
        clearLastTravelingMarker();
        marker = map.addMarker(new MarkerOptions().position(markPosition));
    }







    private void clearPolyLine(){
        if (this.polyLine != null){
            this.polyLine.remove();
            this.polyLine = null;
            this.firstTravelPoint = null;
        }
    }

    private void addPolyLinePoint(LatLng point){
        if (this.firstTravelPoint == null){
            // first point
            this.firstTravelPoint = point;
        }else {
            if (this.polyLine == null){
                // fist two points, stating the line
                this.polyLine = map.addPolyline(new PolylineOptions().add(this.firstTravelPoint, point)
                        .width(5).color(Color.BLUE).geodesic(true));
            } else {
                // continuing the line
                List<LatLng> points = this.polyLine.getPoints();
                points.add(point);
                this.polyLine.setPoints(points);
            }
        }
    }


}
