package tech.huqi.quicknote.util;

/**
 * Created by hzhuqi on 2019/4/15
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图像操作相关的工具类
 */
public class BitmapUtil {
    /**
     * 将相机图片存储到磁盘上的压缩比
     */
    private static final int COMPRESS_QUALITY = 80;

    /**
     * 调整拍照后图像的显示方向，在某些机型上，比如三星拍照后图像是横着的，调整为竖直的
     *
     * @param context
     * @param photoPath
     * @return
     */
    public static String adjustPhotoImageOrientation(Context context, String photoPath) {
        int degree = getPhotoDegree(photoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (degree != 0) {
            Bitmap newBitmap = rotatePhotoByDegree(degree, bitmap);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(photoPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return photoPath;
            }
            newBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fos);
        }
        return photoPath;
    }

    private static int getPhotoDegree(String photoPath) {
        int degree = 0;
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90: {
                degree = 90;
            }
            break;
            case ExifInterface.ORIENTATION_ROTATE_180: {
                degree = 180;
            }
            break;
            case ExifInterface.ORIENTATION_ROTATE_270: {
                degree = 270;
            }
            break;
            default: {
                degree = 0;
            }
            break;
        }
        return degree;
    }

    private static Bitmap rotatePhotoByDegree(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (result == null) {
            return bitmap;
        }
        bitmap.recycle();
        return result;
    }

    public static boolean saveBitmap(Bitmap pBitmap, File file) {
        if (file == null) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (fos != null) {
                pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            }
        } catch (IOException io) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static void saveBitmap(Bitmap pBitmap, String savePath, String fileName, Bitmap.CompressFormat format) {
        if (format == null) {
            format = Bitmap.CompressFormat.JPEG;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(savePath, fileName));
            if (fos != null) {
                pBitmap.compress(format, 100, fos);
                fos.flush();
            }
        } catch (IOException io) {

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
