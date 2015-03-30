package com.example.ahmadfauzi__.test;

/**
 * Created by Ahmad Fauzi on 3/9/2015.
 */
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

public class ColorFinder {
    private static final String TAG = ColorFinder.class.getSimpleName();

    private CallbackInterface callback;

    public ColorFinder(CallbackInterface callback) {
        this.callback = callback;
    }

    public void findDominantColor(Bitmap bitmap) {
        new GetDominantColor().execute(bitmap);
    }

    private int getDominantColorFromBitmap(Bitmap bitmap) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        Map <Integer, PixelObject> pixelList = getMostDominantPixelList(pixels);
        return getDominantPixel(pixelList);
    }

    private Map<Integer, PixelObject> getMostDominantPixelList(int [] pixels) {
        Map<Integer, PixelObject> pixelList = new HashMap<>();

        for (int pixel : pixels) {
            if (pixelList.containsKey(pixel)) {
                pixelList.get(pixel).pixelCount++;
            } else {
                pixelList.put(pixel, new PixelObject(pixel, 1));
            }
        }

        return pixelList;
    }

    private int getDominantPixel(Map<Integer, PixelObject> pixelList) {
        int dominantColor = 0;
        int largestCount = 0;
        for (Map.Entry<Integer, PixelObject> entry : pixelList.entrySet()) {
            PixelObject pixelObject = entry.getValue();

            if (pixelObject.pixelCount > largestCount) {
                largestCount = pixelObject.pixelCount;
                dominantColor = pixelObject.pixel;
            }
        }

        return dominantColor;
    }

    private class GetDominantColor extends AsyncTask<Bitmap, Integer, Integer> {

        @Override
        protected Integer doInBackground(Bitmap... params) {
            int dominantColor = getDominantColorFromBitmap(params[0]);
            return dominantColor;
        }

        @Override
        protected void onPostExecute(Integer dominantColor) {
            String hexColor = colorToHex(dominantColor);
            if (callback != null)
                callback.onCompleted(hexColor);
        }

        private String colorToHex(int color) {
            return String.format("#%06X", (0xFFFFFF & color));
        }
    }

    public interface CallbackInterface {
        public void onCompleted(String dominantColor);
    }
}
