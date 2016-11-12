package its.lp2.admin.main;

/**
 * Created by KBSE on 22/05/2015.
 */

import android.graphics.Color;

public class HSV {
    public float H = 0;
    public float S = 0;
    public float V = 0;

    public HSV(int color)
    {
        float colorRed = Color.red(color);
        float colorGreen = Color.green(color);
        float colorBlue = Color.blue(color);

        float max = maxRGB(colorRed,colorGreen,colorBlue);
        float min = minRGB(colorRed,colorGreen,colorBlue);

        V=max;
        float r = (max-colorRed)/(max-min);
        float g = (max-colorGreen)/(max-min);
        float b = (max-colorBlue)/(max-min);
        float tempH = 0;

        if (max == 0){
            S = 0;
            H = 180; //degrees
        }
        else if (max != 0)
            S = (max-min)/max;
        if (colorRed == max)
            tempH = 60 * (b-g);
        else if (colorGreen == max)
            tempH = 60 * (2+r-b);
        else if (colorBlue == max)
            tempH = 60 * (4+g-r);

        H= tempH;
        if (H >= 360)
            H = H - 360;
        else if (H < 0)
            H = H + 360;

    }
    public static float maxRGB(float colorRed, float colorGreen, float colorBlue)
    {
        float max;
        if(colorRed>colorGreen && colorRed>colorBlue)
            max = colorRed;
        else if (colorGreen > colorBlue)
            max = colorGreen;
        else
            max = colorBlue;

        return max;

    }
    public static float minRGB(float colorRed, float colorGreen, float colorBlue)
    {
        float min;
        if(colorRed<colorGreen && colorRed<colorBlue)
            min = colorRed;
        else if (colorGreen < colorBlue)
            min = colorGreen;
        else
            min = colorBlue;

        return min;

    }
}
