package org.jesse.game.util;

import java.awt.*;

public final class JagexColor {
    public static final double BRIGHTNESS_MAX = .6;
    public static final double BRIGHTNESS_HIGH = .7;
    public static final double BRIGHTNESS_LOW = .8;
    public static final double BRIGHTNESS_MIN = .9;

    private static final double HUE_OFFSET = (.5 / 64.D);
    private static final double SATURATION_OFFSET = (.5 / 8.D);

    private JagexColor() {
    }

    public static short packHSL(int hue, int saturation, int luminance) {
        return (short) ((short) (hue & 63) << 10
                | (short) (saturation & 7) << 7
                | (short) (luminance & 127));
    }

    public static int packHSLFull(int hue, int saturation, int luminance) {
        return (hue & 0xFF) << 16
                | (saturation & 0xFF) << 8
                | (luminance & 0xFF);

    }

    public static int unpackHue(short hsl) {
        return hsl >> 10 & 63;
    }

    public static int unpackSaturation(short hsl) {
        return hsl >> 7 & 7;
    }

    public static int unpackLuminance(short hsl) {
        return hsl & 127;
    }

    public static int unpackHueFull(int hsl) {
        return (hsl >> 16 & 0xFF);
    }

    public static int unpackSaturationFull(int hsl) {
        return (hsl >> 8 & 0xFF);
    }

    public static int unpackLuminanceFull(int hsl) {
        return (hsl & 0xFF);
    }

    public static String formatHSL(short hsl) {
        return String.format("%02Xh%Xs%02Xl", unpackHue(hsl), unpackSaturation(hsl), unpackLuminance(hsl));
    }

    public static int HSLtoRGB(short hsl, double brightness) {
        double hue = (double) unpackHue(hsl) / 64.D + HUE_OFFSET;
        double saturation = (double) unpackSaturation(hsl) / 8.D + SATURATION_OFFSET;
        double luminance = (double) unpackLuminance(hsl) / 128.D;

        // This is just a standard hsl to rgb transform
        // the only difference is the offsets above and the brightness transform below
        double chroma = (1.D - Math.abs((2.D * luminance) - 1.D)) * saturation;
        double x = chroma * (1 - Math.abs(((hue * 6.D) % 2.D) - 1.D));
        double lightness = luminance - (chroma / 2);

        double r = lightness, g = lightness, b = lightness;
        switch ((int) (hue * 6.D)) {
            case 0:
                r += chroma;
                g += x;
                break;
            case 1:
                g += chroma;
                r += x;
                break;
            case 2:
                g += chroma;
                b += x;
                break;
            case 3:
                b += chroma;
                g += x;
                break;
            case 4:
                b += chroma;
                r += x;
                break;
            default:
                r += chroma;
                b += x;
                break;
        }

        int rgb = ((int) (r * 256.0D) << 16)
                | ((int) (g * 256.0D) << 8)
                | (int) (b * 256.0D);

        rgb = adjustForBrightness(rgb, brightness);

        if (rgb == 0) {
            rgb = 1;
        }
        return rgb;
    }

    public static int HSLtoRGBFull(int hsl) {
        double hue = (double) unpackHueFull(hsl) / 256.D;
        double saturation = (double) unpackSaturationFull(hsl) / 256.D;
        double luminance = (double) unpackLuminanceFull(hsl) / 256.D;

        // This is just a standard hsl to rgb transform
        // the only difference is the offsets above and the brightness transform below
        double chroma = (1.D - Math.abs((2.D * luminance) - 1.D)) * saturation;
        double x = chroma * (1 - Math.abs(((hue * 6.D) % 2.D) - 1.D));
        double lightness = luminance - (chroma / 2);

        double r = lightness, g = lightness, b = lightness;
        switch ((int) (hue * 6.D)) {
            case 0:
                r += chroma;
                g += x;
                break;
            case 1:
                g += chroma;
                r += x;
                break;
            case 2:
                g += chroma;
                b += x;
                break;
            case 3:
                b += chroma;
                g += x;
                break;
            case 4:
                b += chroma;
                r += x;
                break;
            default:
                r += chroma;
                b += x;
                break;
        }

        int rgb = ((((int) (r * 256.0D)) & 255) << 16)
                | ((((int) (g * 256.0D)) & 255) << 8)
                | ((int) (b * 256.0D)) & 255;

        if (rgb == 0) {
            rgb = 1;
        }
        return rgb;
    }

    public static int adjustForBrightness(int rgb, double brightness) {
        double r = (double) (rgb >> 16) / 256.0D;
        double g = (double) (rgb >> 8 & 255) / 256.0D;
        double b = (double) (rgb & 255) / 256.0D;

        r = Math.pow(r, brightness);
        g = Math.pow(g, brightness);
        b = Math.pow(b, brightness);

        return ((int) (r * 256.0D) << 16)
                | ((int) (g * 256.0D) << 8)
                | (int) (b * 256.0D);
    }

    public static int[] createPalette(double brightness) {
        int[] colorPalette = new int[65536];
        for (int i = 0; i < colorPalette.length; i++) {
            colorPalette[i] = HSLtoRGB((short) i, brightness);
        }
        return colorPalette;
    }

    public static int getRGBFull(int hsl) {
        return HSLtoRGBFull(hsl);
    }

    public static short rgbToHSL(int rgb, double brightness)
    {
        if (rgb == 1)
        {
            return 0;
        }

        brightness = 1.D / brightness;

        double r = (double) (rgb >> 16 & 255) / 256.0D;
        double g = (double) (rgb >> 8 & 255) / 256.0D;
        double b = (double) (rgb & 255) / 256.0D;

        r = Math.pow(r, brightness);
        g = Math.pow(g, brightness);
        b = Math.pow(b, brightness);

        float[] hsv = Color.RGBtoHSB((int) (r * 256.D), (int) (g * 256.D), (int) (b * 256.D), null);
        double hue = hsv[0];
        double luminance = hsv[2] - ((hsv[2] * hsv[1]) / 2.F);
        double saturation = (hsv[2] - luminance) / Math.min(luminance, 1 - luminance);

        return packHSL((int) (Math.ceil(hue * 64.D) % 63.D),
                (int) Math.ceil(saturation * 7.D),
                (int) Math.ceil(luminance * 127.D));
    }
}