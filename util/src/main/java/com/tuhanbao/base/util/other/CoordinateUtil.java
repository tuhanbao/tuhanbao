package com.tuhanbao.base.util.other;

import java.util.List;

import com.tuhanbao.base.util.exception.MyException;

/**
 * 坐标查询工具
 * 
 * @author zhihongp
 *
 */
public class CoordinateUtil {
    /**
     * 地球的半径
     */
    private static final double EARTH_RADIUS = 6378137;

    /**
     * 获取两个坐标点的直线距离
     * 
     * @param lng1 坐标1经度
     * @param lat1 坐标1纬度
     * @param lng2 坐标2经度
     * @param lat2 坐标2纬度
     * @return 距离
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 获取离指定坐标最近的一个坐标
     * 
     * @param lng 指定坐标经度
     * @param lat 指定坐标纬度
     * @param coordinateList 目标坐标集
     * @return 
     * @return 最近的一个坐标
     */
    @SuppressWarnings("unchecked")
    public static <T extends Coordinate> T getShortestCoordinate(double lng, double lat, List<? extends Coordinate> coordinateList) {
        if (coordinateList.isEmpty()) {
            throw new MyException("There is no coordinate, because the coordinateList is empty");
        }

        T shortestCoordinate = null;
        double shortestDistance = 0.0d;

        for (Coordinate coordinate : coordinateList) {
            double distance = getDistance(lng, lat, coordinate.getLng(), coordinate.getLat());

            if (shortestCoordinate == null) {
                shortestDistance = distance;
                shortestCoordinate = (T) coordinate;
            } else {
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    shortestCoordinate = (T) coordinate;
                }
            }
        }

        return shortestCoordinate;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

//    public static void main(String[] args) {
//        // 104.064296,30.665344 少城大厦
//        // 104.064328,30.663154 人民公园
//        // 104.072227,30.663456 天府广场
//        // 104.074947,30.560878 世纪城
//        // 104.080489,30.701631 火车北站
//        double distance = getDistance(104.064296, 30.665344, 104.074947, 30.560878);
//        System.out.println("Distance is:" + distance);
//
//        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
//        coordinateList.add(new Coordinate(104.074947, 30.560878, "世纪城地铁站"));
//        coordinateList.add(new Coordinate(104.072227, 30.663456, "天府广场"));
//        coordinateList.add(new Coordinate(104.064328, 30.663154, "人民公园"));
//        coordinateList.add(new Coordinate(104.080489, 30.701631, "火车北站"));
//        Coordinate CCoordinate = getShortestCoordinate(104.064296, 30.665344, coordinateList);
//        System.out.println("Coordinate is:" + CCoordinate);
//    }
}
