package net.dwild.ets.log320.GameLogic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import net.dwild.ets.log320.ClientData.Square;

public class DistanceCache {
	
	private static DistanceCache instance = null;
	private Map<String, Map<String, Double>> distanceAllPoints = new Hashtable<String, Map<String, Double>>();
	
	private DistanceCache(int dimensionXMax, int dimensionYMax) {
		ArrayList<Point> coords = new ArrayList<Point>();
		for (int x = 0; x < dimensionXMax; ++x) {
			for (int y = 0; y < dimensionYMax; ++y) { 
				coords.add(new Point(x, y));	
			}
		}
	
		for (Point from : coords) {
			Map<String, Double> distancePoints =  new Hashtable<String, Double>();
			for (Point to : coords) {	
				double distance = Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2) );
				distancePoints.put(toKey(to.getX(), to.getY()), distance);
			}
			distanceAllPoints.put(toKey(from.getX(), from.getY()), distancePoints);
		}
	} 
	
	public static DistanceCache getInstance() {
	      if (instance == null) {
	         instance = new DistanceCache(8, 8);
	      }
	      
	      return instance;
	   }
	
	private String toKey(Double val1, Double val2) {
		return Integer.toString(val1.intValue()) + Integer.toString(val2.intValue());
	}
	
	public double getDistance(Square from, Square to) {
		String keyFrom = Integer.toString(from.getX()) + Integer.toString(from.getY());
		String keyTo = Integer.toString(to.getX()) + Integer.toString(to.getY());
		return distanceAllPoints.get(keyFrom).get(keyTo);
	}
}
