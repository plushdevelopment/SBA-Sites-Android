package com.sbasite.sbasites.util;

import android.util.Log;

import com.google.android.maps.GeoPoint;
 
public class DistanceCalculator {
 
	//earth’s radius (mean radius = 6,371km)
   private static final double radius = 6371;
   private static final String TAG = DistanceCalculator.class.getSimpleName();
 
   public static double calculateDistance(GeoPoint startP, GeoPoint endP) {
 
	  double distance = 0;
 
	  if(startP != null && endP != null) {
		  double lat1 = startP.getLatitudeE6()/1E6;
		  double lat2 = endP.getLatitudeE6()/1E6;
		  double lon1 = startP.getLongitudeE6()/1E6;
		  double lon2 = endP.getLongitudeE6()/1E6;
		  double dLat = Math.toRadians(lat2-lat1);
		  double dLon = Math.toRadians(lon2-lon1);
		  double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		  Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		  Math.sin(dLon/2) * Math.sin(dLon/2);
		  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		  distance = radius * c;
	  }
	  
	  Log.d(TAG, String.format("Distance = %f", distance));
	  
	  return distance;
   }
}
