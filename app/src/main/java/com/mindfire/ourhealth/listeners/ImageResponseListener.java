package com.mindfire.ourhealth.listeners;

/**
 * This is an interface for providing the response from the server.
 */
public interface ImageResponseListener {
    /**
     * Fetches image from the server in the form of byte array.
     *
     * @param iImageByteArray image in byte array.
     */
    void imageFromServer(byte[] iImageByteArray);

    /**
     * Retrieves the exception and response code while fetching image from server.
     *
     * @param iException exception.
     * @param iResponseCode http response code.
     */
    void errorOccurredForImageRequest(Exception iException, int iResponseCode);
}
