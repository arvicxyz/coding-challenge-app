package com.codingchallenge.app.utils;

public class ImageUtil {

    public static String getHighDefArtworkUrl(String artworkUrl, int dimension) {

        String result = null;

        String imageFilename = String.format(AppSettings.LOCALE, "%dx%dbb.jpg", dimension, dimension);
        String defaultImgFilename = "100x100bb.jpg";

        if (artworkUrl.contains(defaultImgFilename)) {
            String urlSubString = artworkUrl
                    .substring(0, artworkUrl.length() - defaultImgFilename.length());
            result = urlSubString + imageFilename;
        }

        return result;
    }
}
