package qa.qcri.aidr.trainer.pybossa.util;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/26/14
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class LatLngUtils {

    public static void computeDistanceInMile(double lat1, double lon1,
                                       double lat2, double lon2,
                                       double distanceInMiles[]) {

        LatLng point1 = new LatLng(lat1, lon1);
        LatLng point2 = new LatLng(lat2, lon2);

        distanceInMiles[0] = LatLngTool.distance(point1, point2, LengthUnit.MILE);

    }
    /**
     * @param lat1
     *          Initial latitude
     * @param lon1
     *          Initial longitude
     * @param lat2
     *          destination latitude
     * @param lon2
     *          destination longitude
     * @param results
     *          To be populated with the distance, initial bearing and final
     *          bearing
     */

    public static void computeDistanceAndBearing(double lat1, double lon1,
                                                 double lat2, double lon2,
                                                 double results[]) {
        // Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
        // using the "Inverse Formula" (section 4)

        int MAXITERS = 20;
        // Convert lat/long to radians
        lat1 *= Math.PI / 180.0;
        lat2 *= Math.PI / 180.0;
        lon1 *= Math.PI / 180.0;
        lon2 *= Math.PI / 180.0;

        double a = 6378137.0; // WGS84 major axis
        double b = 6356752.3142; // WGS84 semi-major axis
        double f = (a - b) / a;
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

        double L = lon2 - lon1;
        double A = 0.0;
        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;

        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha = 0.0;
        double cos2SM = 0.0;
        double cosSigma = 0.0;
        double sinSigma = 0.0;
        double cosLambda = 0.0;
        double sinLambda = 0.0;

        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = Math.atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda
                    / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2
                    / cosSqAlpha; // (18)

            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1 + (uSquared / 16384.0) * // (3)
                    (4096.0 + uSquared * (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                    (256.0 + uSquared * (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B
                    * sinSigma
                    * // (6)
                    (cos2SM + (B / 4.0)
                            * (cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0) * cos2SM
                            * (-3.0 + 4.0 * sinSigma * sinSigma)
                            * (-3.0 + 4.0 * cos2SMSq)));

            lambda = L
                    + (1.0 - C)
                    * f
                    * sinAlpha
                    * (sigma + C * sinSigma
                    * (cos2SM + C * cosSigma * (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0e-12) {
                break;
            }
        }

        double distance = (b * A * (sigma - deltaSigma));
        results[0] = distance;
        if (results.length > 1) {
            double initialBearing = Math.atan2(cosU2 * sinLambda, cosU1 * sinU2
                    - sinU1 * cosU2 * cosLambda);
            initialBearing *= 180.0 / Math.PI;
            results[1] = initialBearing;
            if (results.length > 2) {
                double finalBearing = Math.atan2(cosU1 * sinLambda, -sinU1 * cosU2
                        + cosU1 * sinU2 * cosLambda);
                finalBearing *= 180.0 / Math.PI;
                results[2] = finalBearing;
            }
        }
    }

  /*
   * Vincenty Direct Solution of Geodesics on the Ellipsoid (c) Chris Veness
   * 2005-2012
   *
   * from: Vincenty direct formula - T Vincenty, "Direct and Inverse Solutions
   * of Geodesics on the Ellipsoid with application of nested equations", Survey
   * Review, vol XXII no 176, 1975 http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
   */

    /**
     * Calculates destination point and final bearing given given start point,
     * bearing & distance, using Vincenty inverse formula for ellipsoids
     *
     * @param lat1
     *          start point latitude
     * @param lon1
     *          start point longitude
     * @param brng
     *          initial bearing in decimal degrees
     * @param dist
     *          distance along bearing in metres
     * @returns an array of the desination point coordinates and the final bearing
     */

    public static void computeDestinationAndBearing(double lat1, double lon1,
                                                    double brng, double dist,
                                                    double results[])
    {
        double a = 6378137, b = 6356752.3142, f = 1 / 298.257223563; // WGS-84
        // ellipsiod
        double s = dist;
        double alpha1 = toRad(brng);
        double sinAlpha1 = Math.sin(alpha1);
        double cosAlpha1 = Math.cos(alpha1);

        double tanU1 = (1 - f) * Math.tan(toRad(lat1));
        double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
        double sigma1 = Math.atan2(tanU1, cosAlpha1);
        double sinAlpha = cosU1 * sinAlpha1;
        double cosSqAlpha = 1 - sinAlpha * sinAlpha;
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384
                * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double sinSigma = 0, cosSigma = 0, deltaSigma = 0, cos2SigmaM = 0;
        double sigma = s / (b * A), sigmaP = 2 * Math.PI;

        while (Math.abs(sigma - sigmaP) > 1e-12) {
            cos2SigmaM = Math.cos(2 * sigma1 + sigma);
            sinSigma = Math.sin(sigma);
            cosSigma = Math.cos(sigma);
            deltaSigma = B
                    * sinSigma
                    * (cos2SigmaM + B
                    / 4
                    * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6
                    * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma)
                    * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
            sigmaP = sigma;
            sigma = s / (b * A) + deltaSigma;
        }

        double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
        double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
                (1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
        double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1
                * sinSigma * cosAlpha1);
        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        double L = lambda
                - (1 - C)
                * f
                * sinAlpha
                * (sigma + C * sinSigma
                * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        double lon2 = (toRad(lon1) + L + 3 * Math.PI) % (2 * Math.PI) - Math.PI; // normalise
        // to
        // -180...+180

        double revAz = Math.atan2(sinAlpha, -tmp); // final bearing, if required

        results[0] = toDegrees(lat2);
        results[1] = toDegrees(lon2);
        results[2] = toDegrees(revAz);
    }

    private static double toRad(double angle) {
        return angle * Math.PI / 180;
    }

    private static double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }


    public static void geoMidPointFor3Points(double lat1, double lon1,
                                     double lat2, double lon2,
                                     double lat3, double lon3,
                                     double results[]) {

        // Convert lat/long to radians
        lat1 = toRad(lat1);
        lat2 = toRad(lat2);
        lat3 = toRad(lat3);
        lon1 = toRad(lon1);
        lon2 = toRad(lon2);
        lon3 = toRad(lon3);

        double x1 = Math.cos(lat1) * Math.cos(lon1);
        double y1 = Math.cos(lat1) * Math.sin(lon1);
        double z1 = Math.sin(lat1);

        double x2 = Math.cos(lat2) * Math.cos(lon2);
        double y2 = Math.cos(lat2) * Math.sin(lon2);
        double z2 = Math.sin(lat2);


        double x3 = Math.cos(lat3) * Math.cos(lon3);
        double y3 = Math.cos(lat3) * Math.sin(lon3);
        double z3 = Math.sin(lat3);
        // no consideration on weight. so, put on 1 on 3 loc on weight assumption.
        double w = 1;
        double totWeight = 3;

        double x = (x1 + x2 + x3)/totWeight;
        double y = (y1 + y2 + y3) /totWeight;
        double z = (z1 + z2 + z3) / totWeight;

        double lon = Math.atan2(y,x);
        double hyp = Math.sqrt(x*x+y*y);
        double lat = Math.atan2(z,hyp);

        lat = toDegrees(lat);
        lon = toDegrees(lon);

        results[0] = lon;
        results[1] = lat;

       // System.out.println(lat + " " + lon);

    }

    public static void geoMidPointFor2Points(double lat1,double lon1,double lat2,double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        //print out in degrees
        System.out.println(Math.toDegrees(lat3) + " " + Math.toDegrees(lon3));
    }

}