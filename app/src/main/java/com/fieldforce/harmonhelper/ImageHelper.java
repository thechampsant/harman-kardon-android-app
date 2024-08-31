package com.fieldforce.harmonhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import linq.ArrayList;

public class ImageHelper {

    public static final byte ROTATE_CW = 1;
    public static final byte ROTATE_ACW = 2;
    public static final int BACK_CAMERA  = 3;
    public static final int FRONT_CAMERA  = 4;

    /**
     * Rotates image
     * @param fileBytes
     * @param phoneOrientationDegrees
     * @param cameraUsed
     * @return
     */
    public static byte[] rotateImageIfRequired(byte[] fileBytes,int phoneOrientationDegrees ,byte cameraUsed) {

        if((phoneOrientationDegrees+"").equalsIgnoreCase(""))
            return fileBytes;

        byte[] data = null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);

        ByteArrayOutputStream outputStream = null;

        try {
            if(cameraUsed== BACK_CAMERA) {
                switch (phoneOrientationDegrees) {
                    case 0:
                        bitmap = rotateImage(bitmap, 360);
                        break;
                    case 90:
                        bitmap = rotateImage(bitmap, 270);
                        break;
                    case 180:
                        bitmap = rotateImage(bitmap, 180);
                        break;
                    case 270:
                        bitmap = rotateImage(bitmap, 90);
                        break;
                }
            }
            else
                if(cameraUsed==FRONT_CAMERA)
                {
                    switch (phoneOrientationDegrees) {
                        case 0:
                            bitmap = rotateImage(bitmap, 0);
                            break;
                        case 90:
                            bitmap = rotateImage(bitmap, 90);
                            break;
                        case 180:
                            bitmap = rotateImage(bitmap, 180);
                            break;
                        case 270:
                            bitmap = rotateImage(bitmap, 270);
                            break;
                    }
                }
            outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            data = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // Intentionally blank
            }
        }

        return data;
    }


    public static boolean saveTo(String fileFullPath, Bitmap bitmapToSave) {

        FileOutputStream out = null;
        File file = new File(fileFullPath);
        try {
            out = new FileOutputStream(file);
            bitmapToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return false;
    }


    public static Bitmap scaleImageForThumbNail(Bitmap imageToScale) {

        ByteArrayOutputStream bytearrayoutputstream;
        byte[] BYTE;

        final long targetArea = 76800;
        final int targetHeightPortrait = 320;
        final int targetHeightLandscape = 240;

        float targetWidthPortrait;
        float targetWidthLandscape;

        long currentArea = 0;
        float currentToTargetRatio;

        if (imageToScale == null) {
            return null;
        } else {
            bytearrayoutputstream = new ByteArrayOutputStream();
            currentArea = imageToScale.getWidth() * imageToScale.getHeight();

            if (currentArea < targetArea)
                return imageToScale;
            else {
                Bitmap tempBitmap;
                if (imageToScale.getHeight() > imageToScale.getWidth()) {
                    //Height is more than width hence picture was clicked in portrait mode
                    currentToTargetRatio = (float) imageToScale.getHeight() / targetHeightPortrait;
                    targetWidthPortrait = imageToScale.getWidth() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, (int) targetWidthPortrait, targetHeightPortrait, false);
                } else {
                    //Height is more than width hence picture was clicked in landscape mode
                    currentToTargetRatio = (float) imageToScale.getWidth() / targetHeightLandscape;
                    targetWidthLandscape = imageToScale.getHeight() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, targetHeightLandscape, (int) targetWidthLandscape, false);

                }

                tempBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytearrayoutputstream);

                BYTE = bytearrayoutputstream.toByteArray();

                return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
            }

        }

    }


    public static Bitmap scaleImageAtOptimum(Bitmap imageToScale) {

        ByteArrayOutputStream bytearrayoutputstream;
        byte[] BYTE;

        final long targetArea = 1228800;
        final int targetHeightPortrait = 1280;
        final int targetHeightLandscape = 960;

        float targetWidthPortrait;
        float targetWidthLandscape;

        long currentArea = 0;
        float currentToTargetRatio;

        if (imageToScale == null) {
            return null;
        } else {
            bytearrayoutputstream = new ByteArrayOutputStream();
            currentArea = imageToScale.getWidth() * imageToScale.getHeight();

            if (currentArea < targetArea)
                return imageToScale;
            else {
                Bitmap tempBitmap;
                if (imageToScale.getHeight() > imageToScale.getWidth()) {
                    //Height is more than width hence picture was clicked in portrait mode
                    currentToTargetRatio = (float) imageToScale.getHeight() / targetHeightPortrait;
                    targetWidthPortrait = imageToScale.getWidth() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, (int) targetWidthPortrait, targetHeightPortrait, false);
                } else {
                    //Height is more than width hence picture was clicked in landscape mode
                    currentToTargetRatio = (float) imageToScale.getWidth() / targetHeightLandscape;
                    targetWidthLandscape = imageToScale.getHeight() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, targetHeightLandscape, (int) targetWidthLandscape, false);

                }

                tempBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytearrayoutputstream);

                BYTE = bytearrayoutputstream.toByteArray();

                return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
            }

        }

    }

    public static void showClickedImage(Context context, String imagePath) {

        Bitmap imageClicked;
        try
        {
         imageClicked = BitmapFactory.decodeFile(imagePath);
        }
        catch (Exception e)
        {
            return;
        }

        android.app.Dialog builder = new android.app.Dialog(context);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(imageClicked);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }


    public static void showClickedImage(Context context,Bitmap imageClicked) {

        if (imageClicked == null)
            return;

        android.app.Dialog builder = new android.app.Dialog(context);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(imageClicked);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    public static  Bitmap stampImageWithText(Bitmap image , ArrayList<String> textToStamp ) {

        double finalTextHeight = image.getHeight() * 0.025d;
        final int TEXT_SIZE = (int) finalTextHeight;
        Bitmap dest = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();

        tPaint.setTextSize(TEXT_SIZE);
        tPaint.setColor(Color.RED);
        tPaint.setStyle(Paint.Style.FILL);

        cs.drawBitmap(image, 0f, 0f, null);

        float posY = TEXT_SIZE;
        for(String text : textToStamp)
        {
            cs.drawText(text, 40f, posY, tPaint);
            posY+=TEXT_SIZE;
        }

        return dest;

    }

    /**
     * Rotates the image using postRotate on a matrix
     * @param img image to roatate
     * @param degree Degree by which image should be rotated
     * @return rotated image
     */
    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static boolean rotateImage90degreesPresentAt(String imagePath,byte rotateClockwiseOrAntiClockwise)
    {
        try {
            Bitmap imageToRotate = BitmapFactory.decodeFile(imagePath);
            if (imageToRotate == null)
                return false;

            if (rotateClockwiseOrAntiClockwise == ROTATE_CW)
                imageToRotate = rotateImage(imageToRotate, -90);
            else if (rotateClockwiseOrAntiClockwise == ROTATE_ACW)
                imageToRotate = rotateImage(imageToRotate, 90);

            saveTo(imagePath, imageToRotate);
            imageToRotate.recycle();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

}

