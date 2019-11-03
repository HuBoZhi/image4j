package net.sf.image4j.use;

import java.io.*;
import java.awt.Color;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.sf.image4j.codec.ico.ICOEncoder;

/**
 * author: hubz
 * datetime: 2019/11/2 12:14
 */

public class Image4j {
    private static File tempFile = null;

    public static boolean ConvertIamgeToICO(String strInputFilePath, String strOutputFilePath){
        String strInFile = strInputFilePath;
        String strOutFile = strOutputFilePath;

        java.io.InputStream in = null;
		try {
            java.util.List<java.awt.image.BufferedImage> images;
            in = new FileInputStream(strInFile);

            if (!(strInFile.endsWith(".ico") || strInFile.endsWith(".ICO"))) {
                images = new ArrayList<java.awt.image.BufferedImage>(1);
                images.add(ImageIO.read(in));

                /***** encode images and save as ICO *****/
                File outFile = new File(strOutFile);

                ICOEncoder.write(images, outFile);
                return true;
            }
            else{
                throw new IOException("strInputFilePath is not expectation endsWith .ico/.ICO");
            }
        }
		catch (IOException ex) {
		    ex.printStackTrace();
            return false;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {}
        }
    }

    public static boolean ConvertICOToImage(String strInputFilePath, String strOutputFilePath){
        // BMP, JPG, PNG
        String strInFile = strInputFilePath;
        String strOutFile = strOutputFilePath;

        java.io.InputStream in = null;
        try {
            java.util.List<java.awt.image.BufferedImage> images;
            in = new FileInputStream(strInFile);

            if (strInFile.endsWith(".ico") || strInFile.endsWith(".ICO")) {
                images = net.sf.image4j.codec.ico.ICODecoder.read(in);

                if(strOutFile.endsWith(".jpg") || strOutFile.endsWith(".JPG")){
                    java.awt.image.BufferedImage img = images.get(0);
                    java.io.File pngFile = new java.io.File(strOutFile);
                    javax.imageio.ImageIO.write(img, "jpeg", pngFile);

                }
                else if(strOutFile.endsWith(".png") || strOutFile.endsWith(".PNG")){
                    // write PNG
                    java.awt.image.BufferedImage img = images.get(0);
                    java.io.File pngFile = new java.io.File(strOutFile);
                    javax.imageio.ImageIO.write(img, "png", pngFile);

                }
                else if(strOutFile.endsWith(".bmp") || strOutFile.endsWith(".BMP")){
                    // write BMP
                    java.awt.image.BufferedImage img = images.get(0);
                    java.io.File bmpFile = new java.io.File(strOutFile);
                    net.sf.image4j.codec.bmp.BMPEncoder.write(img, bmpFile);
                }
                else{
                    throw new IOException("con't convert this fileType");
                }
                return true;
            }
            else {
                throw new IOException("strInputFilePath must be endsWith .ico/.ICO");
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {}
        }
    }

    public static boolean ConvertImageToImage(String strInputFilePath, String strOutputFilePath){
        String strInFile = strInputFilePath;
        String strOutFile = strOutputFilePath;

        java.io.InputStream in = null;
        try {
            java.util.List<java.awt.image.BufferedImage> images;
            in = new FileInputStream(strInFile);
            if(isImage(strInFile))
            {
                    images = new ArrayList<java.awt.image.BufferedImage>(1);
                    images.add(ImageIO.read(in));

                    // create temp ico file
                    // png/jpg/tmp -> ico -> png/jpg/tmp
                    net.sf.image4j.codec.ico.ICOEncoder.write(images, tempFile);
                    ConvertICOToImage(tempFile.toString(), strOutFile);
                    return true;
            }
            else{
                throw new IOException("strInputFilePath must be endsWith png/jpg/tmp");
            }
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException e){}
        }
    }

    private static boolean isImage(String file){
        if((file.endsWith(".jpg") || file.endsWith(".JPG")) ||
                (file.endsWith(".png") || file.endsWith(".PNG")) ||
                (file.endsWith(".bmp") || file.endsWith(".bmp"))){
            return true;
        }
        else{
            return false;
        }
    }

    private static boolean ImageConvert(String strInputFilePath, String strOutputFilePath,String formatName){
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(strInputFilePath));
            BufferedImage newBufferedImage = new BufferedImage(
                    bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                    Color.WHITE, null);
            ImageIO.write(newBufferedImage, formatName, new File(strOutputFilePath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean Convert(String strInputFilePath, String strOutputFilePath){
        boolean res;
        if(strInputFilePath.endsWith(".ico") || strInputFilePath.endsWith(".ICO")){
            //ico -> png -> jpg
            if(strOutputFilePath.endsWith(".jpg")||strOutputFilePath.endsWith(".JPG")){
                try {
                    tempFile = File.createTempFile("temp", ".png");
                    Convert(strInputFilePath,tempFile.toString());
                    res = ImageConvert(tempFile.toString(),strOutputFilePath,"jpg");
                    tempFile.delete();
                } catch (IOException e) {
                    res = false;
                }
            }
            else {
                res = ConvertICOToImage(strInputFilePath, strOutputFilePath);
            }
        }
        else if(isImage(strInputFilePath))
        {
            if((strInputFilePath.endsWith(".png")||strInputFilePath.endsWith(".PNG")) &&
                    (strOutputFilePath.endsWith(".jpg")||strOutputFilePath.endsWith(".JPG"))){
                return ImageConvert(strInputFilePath,strOutputFilePath,"jpg");
            }
            else if(strOutputFilePath.endsWith(".ico")||strOutputFilePath.endsWith(".ICO")){
                //png -> jpg -> ico
                if(strInputFilePath.endsWith(".png")||strInputFilePath.endsWith(".PNG")){
                    try {
                        tempFile = File.createTempFile("temp", ".jpg");
                        ImageConvert(strInputFilePath,tempFile.toString(),"jpg");
                        res = Convert(tempFile.toString(),strOutputFilePath);
                        tempFile.delete();
                    } catch (IOException e) {
                        res = false;
                    }
                }
                else {
                    try {
                        tempFile = File.createTempFile("temp", ".ico");
                        res = ConvertIamgeToICO(strInputFilePath, strOutputFilePath);
                        tempFile.delete();
                    } catch (IOException e) {
                        res = false;
                    }
                }
            }
            else if(isImage(strOutputFilePath)){
                res = ConvertImageToImage(strInputFilePath,strOutputFilePath);
            }
            else{
                res = false;
            }
        }
        else{
            res = false;
        }
        return res;
    }

    public static void main(String[] args) {

        //jpg -> ico
//        Convert("D:\\Temp\\timg.jpg","D:\\Temp\\timg.ico");

        //jpg -> png
//        Convert("D:\\Temp\\timg.jpg","D:\\Temp\\timg_jpg.png");

        //jpg -> bmp
//        Convert("D:\\Temp\\timg.jpg","D:\\Temp\\timg_jpg.bmp");

        //png -> jpg -> ico
//        Convert("D:\\Temp\\timg.png","D:\\Temp\\timg_png.ico");


        //png -> jpg
//        Convert("D:\\Temp\\timg.png","D:\\Temp\\timg_png.jpg");

        //png -> bmp
//        Convert("D:\\Temp\\timg.png","D:\\Temp\\timg_png.bmp");


        //ico -> png
//        Convert("D:\\Temp\\timg.ico","D:\\Temp\\timg_ico.png");

        //ico -> png -> jpg
//        Convert("D:\\Temp\\timg.ico","D:\\Temp\\timg_ico.jpg");

        //ico -> bmp
//        Convert("D:\\Temp\\timg.ico","D:\\Temp\\timg_ico.bmp");

    }
}
