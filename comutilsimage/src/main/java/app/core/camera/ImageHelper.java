package app.core.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHelper {

    public static final byte ROTATE_CW = 1;
    public static final byte ROTATE_ACW = 2;

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
            if(cameraUsed==CameraActivity.BACK_CAMERA) {
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
                if(cameraUsed==CameraActivity.FRONT_CAMERA)
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

