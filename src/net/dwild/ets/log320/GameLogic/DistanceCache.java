package net.dwild.ets.log320.GameLogic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import net.dwild.ets.log320.ClientData.Square;

public class DistanceCache {
	
	private static DistanceCache instance = null;
	private double[][] distanceAllPoints = new double[64][64];
	
	private DistanceCache(int dimensionXMax, int dimensionYMax) {
		ArrayList<Point> coords = new ArrayList<Point>();
		for (int x = 0; x < dimensionXMax; ++x) {
			for (int y = 0; y < dimensionYMax; ++y) { 
				coords.add(new Point(x, y));	
			}
		}
	
		for (Point from : coords) {
			for (Point to : coords) {	
				int distanceX = Math.abs((int)(from.getX() - to.getX()));
				int distanceY = Math.abs((int)(from.getY() - to.getY()));
				double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2) );
				distanceAllPoints[distanceX][distanceY] = distance;
			}
		}	
	} 
	
	public static DistanceCache getInstance() {
	      if (instance == null) {
	         instance = new DistanceCache(8, 8);
	      }
	      
	      return instance;
	   }
	
	public double getDistance(Square from, Square to) {
		int distanceX = Math.abs(from.getX() - to.getX());
		int distanceY = Math.abs(from.getY() - to.getY());
		return distanceAllPoints[distanceX][distanceY];
	}
}
