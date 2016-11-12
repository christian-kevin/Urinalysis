package its.lp2.admin.main;

/**
 * Created by KBSE on 07/05/2015.
 */

import android.graphics.Color;

public class CieLAB {
    public float LValue =0;
    public float aValue = 0;
    public float bValue = 0;
    public  CieLAB(int color)
    {
        float colorRed = Color.red(color);
        float colorGreen = Color.green(color);
        float colorBlue = Color.blue(color);
        float eps = 0.008856F;
        float k = 7.787F;

        float Xr = 95.047F;
        float Yr = 100.0F;
        float Zr = 108.883F;

        float r = (float)(colorRed / 255.0D);
        float g = (float)(colorGreen / 255.0D);
        float b = (float)(colorBlue / 255.0D);
        if (r <= 0.04045D) {
            r /= 12.92F;
        } else {
            r = (float)Math.pow((r + 0.055D) / 1.055D, 2.4D);
        }
        if (g <= 0.04045D) {
            g /= 12.92F;
        } else {
            g = (float)Math.pow((g + 0.055D) / 1.055D, 2.4D);
        }
        if (b <= 0.04045D) {
            b /= 12.92F;
        } else {
            b = (float)Math.pow((b + 0.055D) / 1.055D, 2.4D);
        }
        r *= 100.0F;
        g *= 100.0F;
        b *= 100.0F;

        float X = 0.4124F * r + 0.3576F * g + 0.1805F * b;
        float Y = 0.2126F * r + 0.7152F * g + 0.0722F * b;
        float Z = 0.0193F * r + 0.1192F * g + 0.9505F * b;

        float xr = X / Xr;
        float yr = Y / Yr;
        float zr = Z / Zr;
        float fx;
        if (xr > eps) {
            fx = (float)Math.pow(xr, 0.3333333333333333D);
        } else {
            fx = (float)(k * xr + 0.13793103448275862D);
        }
        float fy;
        if (yr > eps) {
            fy = (float)Math.pow(yr, 0.3333333333333333D);
        } else {
            fy = (float)(k * yr + 0.13793103448275862D);
        }
        float fz;
        if (zr > eps) {
            fz = (float)Math.pow(zr, 0.3333333333333333D);
        } else {
            fz = (float)(k * zr + 0.13793103448275862D);
        }
        LValue = 116.0F * fy - 16.0F;
        aValue = 500.0F * (fx - fy);
        bValue = 200.0F * (fy - fz);

    }

}
