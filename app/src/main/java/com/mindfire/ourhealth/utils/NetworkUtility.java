package com.mindfire.ourhealth.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.MessageBean;
import com.mindfire.ourhealth.beans.SignInResponseBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ImageResponseListener;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.registration.RegistrationActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This is a utility to manage network connection and operations.
 */
public class NetworkUtility {

    private static final String TAG = NetworkUtility.class.getSimpleName();

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String REQUEST_PROPERTY_VALUE = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String LINE_END = "\r\n";
    private static final String NAME_FORMAT = "Content-Disposition: form-data; name=\"%s\"" + LINE_END;
    private static final String BOUNDARY = "---------------------------14737809831466499882746641449";
    private static final String TWO_HYPHENS = "--";
    private static final String START_TAG = TWO_HYPHENS + BOUNDARY;
    private static final String MULTIPART_CONTENT_TYPE = "multipart/form-data;boundary=";
    private static final String IMAGE_CONTENT_TYPE = "Content-Type: image/jpg";
    private static final String IMAGE_NAME_FORMAT = "Content-Disposition: form-data; name=\"imageUpload\"; filename=\"%s\"" + LINE_END;
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";

    /**
     * @return true if device is connected or connecting to network, false otherwise.
     */
    public static boolean isInternetConnected(Context iContext) {
        if (iContext != null) {
            ConnectivityManager manager = (ConnectivityManager) iContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    /**
     * This method is used to make post http request.
     * Http GET and POST requests are made according to the request method supplied.
     *
     * @param iUrl           url to which request is to be made.
     * @param iRequestMethod the http request method.
     * @param iRequestString the json string for request.
     */
    public static String makeHttpRequest(String iUrl, String iRequestMethod, String iRequestString,
                                         ServerResponseListener iListener, int iCallPurpose) {
        LogUtils.debug(TAG, MyConstants.REQUEST + iRequestString);
        Log.d(MyConstants.TAG_TEST, "request_method" + iRequestMethod);
        if (iRequestMethod != null) {
            if (MyConstants.HTTP_DELETE.equals(iRequestMethod)) {
                Log.d(MyConstants.TAG_TEST, "Request for okClient ");
                String response_string = "null.";
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(iUrl)
                            .delete(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), iRequestString))
                            .build();

                    Response response = client.newCall(request).execute();
                    response_string = response.body().string();
                    // TODO use your response

                    LogUtils.debug(TAG, MyConstants.RESPONSE_CODE + response.code());
                    LogUtils.debug(TAG, MyConstants.RESPONSE_MESSAGE + response.message());

                    Log.d(MyConstants.TAG_TEST, "response_string" + response_string);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(MyConstants.TAG_TEST, "exception occur " + e.getMessage());
                }
                return response_string;
            }

            HttpURLConnection connection = null;
            DataOutputStream outputStream;
            URL url;
            int responseCode = 0;
            Exception exception = null;
            try {
                url = new URL(iUrl);
                connection = (HttpURLConnection) url.openConnection();
                if (iRequestMethod.equals(MyConstants.HTTP_POST) && iRequestString != null) {
                    connection.setRequestMethod(MyConstants.HTTP_POST);
                    connection.setRequestProperty(CONTENT_TYPE, REQUEST_PROPERTY_VALUE);
                    SignInResponseBean signInResponse = DataManager.getSoleInstance().getSignInResponse();
                    if (signInResponse != null) {
                        connection.setRequestProperty(AUTHORIZATION, signInResponse.getAuthorizationToken());
                    }
                    outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(iRequestString);
                    outputStream.flush();
                } else if (iRequestMethod.equals(MyConstants.HTTP_GET)) {
                    connection.setRequestMethod(MyConstants.HTTP_GET);
                }

                responseCode = connection.getResponseCode();
                LogUtils.debug(TAG, MyConstants.RESPONSE_CODE + responseCode);
                LogUtils.debug(TAG, MyConstants.RESPONSE_MESSAGE + connection.getResponseMessage());

                // Checking if there is any error details in response
                InputStream errorStream = connection.getErrorStream();
/*
                if (errorStream != null) {
                    String errorMessage = toString(errorStream);
                    Log.e(MyConstants.TAG_TEST, "Error message : " + errorMessage);
                }
*/

                String response = toString(connection.getInputStream());
                LogUtils.debug(TAG, MyConstants.RESPONSE + response);
                return response;
            } catch (IOException e) {
                exception = e;
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                iListener.errorOccurred(responseCode, exception, iCallPurpose);
            }
        }

        return null;
    }


    // This method is used to upload report in the form of image through multipart.
    public static String multipartHttpRequest(String iFilePath, String iTitle, String iDescription) {
        if (iFilePath != null) {
            HttpURLConnection connection = null;
            try {
                int BUFFER_SIZE = 1024 * 1024;

                URL url = new URL(MyUrls.PATIENT_UPLOAD_REPORT);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(MyConstants.HTTP_POST);
                connection.setRequestProperty(CONTENT_TYPE, MULTIPART_CONTENT_TYPE + BOUNDARY);
                connection.setRequestProperty(AUTHORIZATION, DataManager.getSoleInstance().getSignInResponse().getAuthorizationToken());

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                // Write report image to output stream.
                File file = new File(iFilePath);
                outputStream.writeBytes(START_TAG + LINE_END);
                outputStream.writeBytes(String.format(IMAGE_NAME_FORMAT, file.getName()));
                outputStream.writeBytes(IMAGE_CONTENT_TYPE + LINE_END);
                outputStream.writeBytes(LINE_END);

                FileInputStream fileInputStream = new FileInputStream(file);
                int bytesAvailable = fileInputStream.available();
                int bufferSize = Math.min(bytesAvailable, BUFFER_SIZE);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, BUFFER_SIZE);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(LINE_END);

                outputStream.writeBytes(START_TAG + LINE_END);
                outputStream.writeBytes(String.format(NAME_FORMAT, TITLE));
                outputStream.writeBytes(LINE_END);
                outputStream.writeBytes(iTitle);
                outputStream.writeBytes(LINE_END);

                outputStream.writeBytes(START_TAG + LINE_END);
                outputStream.writeBytes(String.format(NAME_FORMAT, DESCRIPTION));
                outputStream.writeBytes(LINE_END);
                outputStream.writeBytes(iDescription);
                outputStream.writeBytes(LINE_END);

                outputStream.writeBytes(START_TAG + TWO_HYPHENS + LINE_END);
                outputStream.flush();

                Log.d(TAG, "Response Message: " + connection.getResponseMessage());
                Log.d(TAG, "Response Code: " + connection.getResponseCode());
                return toString(connection.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return null;
    }

    /**
     * Fetches image from the server and returns byte array.
     */
    public static byte[] fetchImageFromServer(String iUrl, String iRequest, ImageResponseListener iListener) {
        Log.d(TAG, MyConstants.REQUEST + iRequest);
        Exception exception = null;
        int responseCode = -1;
        try {
            URL url = new URL(iUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(MyConstants.HTTP_POST);
            connection.setRequestProperty(CONTENT_TYPE, REQUEST_PROPERTY_VALUE);
            connection.setRequestProperty(AUTHORIZATION, DataManager.getSoleInstance().getSignInResponse().getAuthorizationToken());

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(iRequest);
            outputStream.flush();

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];

            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                byteArrayOutputStream.write(data, 0, bytesRead);
            }
            responseCode = connection.getResponseCode();
            Log.d(TAG, MyConstants.RESPONSE_CODE + connection.getResponseCode());
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        } finally {
            iListener.errorOccurredForImageRequest(exception, responseCode);
        }
        return null;
    }

    /**
     * This method is used to read data from the input stream and
     * build a string using StringBuilder.
     *
     * @return returns string of data fetched from the url.
     * @throws IOException
     */
    public static String toString(InputStream iInputStream) throws IOException {
        if (iInputStream != null) {
            // Convert supplied stream to string data
            BufferedReader reader = new BufferedReader(new InputStreamReader(iInputStream));
            StringBuilder response = new StringBuilder();
            String line;

            // Read whole input stream and make string
            while ((line = reader.readLine()) != null) response.append(line);
            return response.toString();
        }
        return null;
    }

}
