import java.io.*;
import java.util.TreeSet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class ImageProcesser{
    
    //************************************
    // List of the options(Original, Negative); correspond to the cases:
    //************************************
  
    String descs[] = {
        "Original", 
        "Negative",
        "Compare",
        "Re-Scaling",
        "Shifting",
        "Shifting & Re-Scaling",
        "+",
        "-",
        "*",
        "/",
        "Not",
        "And",
        "Or",
        "XOR",
        "ROI",
        "Log",
        "Power",
        "RND LUT",
        "Bit Plane Slice",
        "Histogram",
        "Histogram Normalised",
        "Convolution",
        "Salt and Pepper Noise",
        "Min Filter",
        "Max Filter",
        "Midpoint Filter",
        "Median Filter",
        "Mean and Standered Diviation",
        "Simple Threshold",
        "Automated Threshold"
    };

    public ImageProcesser(){
        return;
    }

    //************************************
    //  Convert the Buffered Image to Array
    //************************************
    public int[][][] convertToArray(BufferedImage image){
      int width = image.getWidth();
      int height = image.getHeight();

      int[][][] result = new int[width][height][4];

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            int p = image.getRGB(x,y);
            int a = (p>>24)&0xff;
            int r = (p>>16)&0xff;
            int g = (p>>8)&0xff;
            int b = p&0xff;

            result[x][y][0]=a;
            result[x][y][1]=r;
            result[x][y][2]=g;
            result[x][y][3]=b;
         }
      }
      return result;
    }

    //************************************
    //  Convert the  Array to BufferedImage
    //************************************
    public BufferedImage convertToBimage(int[][][] TmpArray){

        int width = TmpArray.length;
        int height = TmpArray[0].length;

        BufferedImage tmpimg=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for(int y=0; y<height; y++){
            for(int x =0; x<width; x++){
                int a = TmpArray[x][y][0];
                int r = TmpArray[x][y][1];
                int g = TmpArray[x][y][2];
                int b = TmpArray[x][y][3];
                
                //set RGB value

                int p = (a<<24) | (r<<16) | (g<<8) | b;
                tmpimg.setRGB(x, y, p);

            }
        }
        return tmpimg;
    }


    //************************************
    //  Example:  Image Negative
    //************************************
    public BufferedImage ImageNegative(BufferedImage timg){
        int width = timg.getWidth();
        int height = timg.getHeight();

        int[][][] ImageArray = convertToArray(timg);          //  Convert the image to array

        // Image Negative Operation:
        for(int y=0; y<height; y++){
            for(int x =0; x<width; x++){
                ImageArray[x][y][1] = 255-ImageArray[x][y][1];  //r
                ImageArray[x][y][2] = 255-ImageArray[x][y][2];  //g
                ImageArray[x][y][3] = 255-ImageArray[x][y][3];  //b
            }
        }
        
        return convertToBimage(ImageArray);  // Convert the array to BufferedImage
    }

    //************************************************************************************************************
    //************************************************LAB 1*******************************************************
    //************************************************************************************************************

    public BufferedImage compare(BufferedImage og, BufferedImage newImage){
        int[][][] ogA = convertToArray(og);
        int[][][] newA = convertToArray(newImage);
        int[][][] comb;
        int totalLength = ogA[0].length + newA[0].length;

        if(ogA.length >= newA.length){
            comb = new int[ogA.length][totalLength][4];
        }else{
            comb = new int[newA.length][totalLength][4];
            int[][][] temp = ogA;
            ogA = newA;
            newA = temp;
        }

        for(int x = 0; x < ogA.length; x++){
            if(x < newA.length){
                for(int y = 0; y < totalLength; y++){
                    if(y < ogA[0].length){
                        comb[x][y] = ogA[x][y];
                    }else{
                        comb[x][y] = newA[x][y- ogA.length];
                    }
                }
            }else{
                for(int y = 0; y < totalLength; y++){
                    if(y < ogA[0].length){
                        comb[x][y] = ogA[x][y];
                    }else{
                        comb[x][y] = new int[]{255,255,255,255};
                    }
                }
            }
        }

        return convertToBimage(comb);

    }

    //************************************************************************************************************
    //************************************************LAB 2*******************************************************
    //************************************************************************************************************

    //Re-Scaling:
    public BufferedImage reScale(BufferedImage rimg){
        double factor = 0.34;
        int[][][] imageArray = convertToArray(rimg);
        for(int y = 0; y < rimg.getHeight(); y++){
            for(int x =0; x< rimg.getWidth(); x++){
                imageArray[x][y][1] = (int) factor * imageArray[x][y][1];  //r
                imageArray[x][y][2] = (int) factor * imageArray[x][y][2];  //g
                imageArray[x][y][3] = (int) factor * imageArray[x][y][3];  //b

                if(imageArray[x][y][1] < 0) imageArray[x][y][1] = 0;
                if(imageArray[x][y][2] < 0) imageArray[x][y][2] = 0;
                if(imageArray[x][y][3] < 0) imageArray[x][y][3] = 0;
                if(imageArray[x][y][1] > 255) imageArray[x][y][1] = 255;
                if(imageArray[x][y][2] > 255) imageArray[x][y][2] = 255;
                if(imageArray[x][y][3] > 255) imageArray[x][y][3] = 255;
            }
        }

        return convertToBimage(imageArray);
    }

    //Shifting:
    public BufferedImage Shift(BufferedImage simg){
        int shiftFactor = 69;
        int[][][] imageArray = convertToArray(simg);
        for(int y = 0; y < simg.getHeight(); y++){
            for(int x =0; x< simg.getWidth(); x++){
                imageArray[x][y][1] = (int) imageArray[x][y][1] + shiftFactor;  //r
                imageArray[x][y][2] = (int) imageArray[x][y][2] + shiftFactor;  //g
                imageArray[x][y][3] = (int) imageArray[x][y][3] + shiftFactor;  //b

                if(imageArray[x][y][1] < 0) imageArray[x][y][1] = 0;
                if(imageArray[x][y][2] < 0) imageArray[x][y][2] = 0;
                if(imageArray[x][y][3] < 0) imageArray[x][y][3] = 0;
                if(imageArray[x][y][1] > 255) imageArray[x][y][1] = 255;
                if(imageArray[x][y][2] > 255) imageArray[x][y][2] = 255;
                if(imageArray[x][y][3] > 255) imageArray[x][y][3] = 255;
            }
        }
        return convertToBimage(imageArray);
    }

    //Re-Scale and Shifting
    public BufferedImage rndShiftReScale(BufferedImage bimg){
        int[][][] imageArray = convertToArray(bimg);
        Random rnd = new Random();
        for(int y = 0; y < bimg.getHeight(); y++){
            for(int x =0; x < bimg.getWidth(); x++){
                imageArray[x][y][1] = (int) imageArray[x][y][1] + rnd.nextInt(255);  //r
                imageArray[x][y][2] = (int) imageArray[x][y][2] + rnd.nextInt(255);  //g
                imageArray[x][y][3] = (int) imageArray[x][y][3] + rnd.nextInt(255);  //b
            }
        }
        int s = 255;
        int t = 1;

        int rmin = s*(imageArray[0][0][1]+t); int rmax = rmin;
        int gmin = s*(imageArray[0][0][2]+t); int gmax = gmin;
        int bmin = s*(imageArray[0][0][3]+t); int bmax = bmin;
        for(int y=0; y < bimg.getHeight(); y++){
            for(int x=0; x < bimg.getWidth(); x++){
                imageArray[x][y][1] = s*(imageArray[x][y][1]+t); //r
                imageArray[x][y][2] = s*(imageArray[x][y][2]+t); //g
                imageArray[x][y][3] = s*(imageArray[x][y][3]+t); //b
                if (rmin>imageArray[x][y][1]) { rmin = imageArray[x][y][1]; }
                if (gmin>imageArray[x][y][2]) { gmin = imageArray[x][y][2]; }
                if (bmin>imageArray[x][y][3]) { bmin = imageArray[x][y][3]; }
                if (rmax<imageArray[x][y][1]) { rmax = imageArray[x][y][1]; }
                if (gmax<imageArray[x][y][2]) { gmax = imageArray[x][y][2]; }
                if (bmax<imageArray[x][y][3]) { bmax = imageArray[x][y][3]; }
            }
        }
        for(int y=0; y < bimg.getHeight(); y++){
            for(int x=0; x < bimg.getWidth(); x++){
                imageArray[x][y][1]=255*(imageArray[x][y][1]-rmin)/(rmax-rmin);
                imageArray[x][y][2]=255*(imageArray[x][y][2]-gmin)/(gmax-gmin);
                imageArray[x][y][3]=255*(imageArray[x][y][3]-bmin)/(bmax-bmin);
            }
        }
        return convertToBimage(imageArray);
    }

    public BufferedImage ShiftReScale(int[][][] imageArray){
        int s = 255;
        int t = 1;

        int rmin = s*(imageArray[0][0][1]+t); int rmax = rmin;
        int gmin = s*(imageArray[0][0][2]+t); int gmax = gmin;
        int bmin = s*(imageArray[0][0][3]+t); int bmax = bmin;
        for(int y=0; y < imageArray.length; y++){
            for(int x=0; x < imageArray[0].length; x++){
                imageArray[x][y][1] = s*(imageArray[x][y][1]+t); //r
                imageArray[x][y][2] = s*(imageArray[x][y][2]+t); //g
                imageArray[x][y][3] = s*(imageArray[x][y][3]+t); //b
                if (rmin>imageArray[x][y][1]) { rmin = imageArray[x][y][1]; }
                if (gmin>imageArray[x][y][2]) { gmin = imageArray[x][y][2]; }
                if (bmin>imageArray[x][y][3]) { bmin = imageArray[x][y][3]; }
                if (rmax<imageArray[x][y][1]) { rmax = imageArray[x][y][1]; }
                if (gmax<imageArray[x][y][2]) { gmax = imageArray[x][y][2]; }
                if (bmax<imageArray[x][y][3]) { bmax = imageArray[x][y][3]; }
            }
        }
        for(int y=0; y < imageArray.length; y++){
            for(int x=0; x < imageArray[0].length; x++){
                imageArray[x][y][1]=255*(imageArray[x][y][1]-rmin)/(rmax-rmin);
                imageArray[x][y][2]=255*(imageArray[x][y][2]-gmin)/(gmax-gmin);
                imageArray[x][y][3]=255*(imageArray[x][y][3]-bmin)/(bmax-bmin);
            }
        }
        return convertToBimage(imageArray);
    }

    //************************************************************************************************************
    //************************************************LAB 3*******************************************************
    //************************************************************************************************************

    public BufferedImage Addition(BufferedImage image1, BufferedImage image2){
        int[][][] im1 = convertToArray(image1);
        int[][][] im2 = convertToArray(image2);
        int[][][] newImg = new int[im1.length][im1[0].length][4];

        for(int y=0; y < image1.getHeight(); y++){
            for(int x=0; x < image1.getWidth(); x++){
                for(int p = 1; p < 4; p++){
                    newImg[x][y][p] = im1[x][y][p] + im2[x][y][p];
                }
            }
        }

        return ShiftReScale(newImg);
    }

    public BufferedImage Subtraction(BufferedImage image1, BufferedImage image2){
        int[][][] im1 = convertToArray(image1);
        int[][][] im2 = convertToArray(image2);
        int[][][] newImg = new int[im1.length][im1[0].length][4];

        for(int y=0; y < image1.getHeight(); y++){
            for(int x=0; x < image1.getWidth(); x++){
                for(int p = 1; p < 4; p++){
                    newImg[x][y][p] = im1[x][y][p] - im2[x][y][p];
                    //if(newImg[x][y][p] < 0) newImg[x][y][p] = 0;
                }
            }
        }

        return ShiftReScale(newImg);
    }

    public BufferedImage Multiplication(BufferedImage image1, BufferedImage image2){
        int[][][] im1 = convertToArray(image1);
        int[][][] im2 = convertToArray(image2);
        int[][][] newImg = new int[im1.length][im1[0].length][4];

        for(int y=0; y < image1.getHeight(); y++){
            for(int x=0; x < image1.getWidth(); x++){
                for(int p = 1; p < 4; p++){
                    newImg[x][y][p] = im1[x][y][p] * im2[x][y][p];
                }
            }
        }

        return ShiftReScale(newImg);
    }
    public BufferedImage Division(BufferedImage image1, BufferedImage image2){
        int[][][] im1 = convertToArray(image1);
        int[][][] im2 = convertToArray(image2);
        int[][][] newImg = new int[im1.length][im1[0].length][4];

        for(int y=0; y < image1.getHeight(); y++){
            for(int x=0; x < image1.getWidth(); x++){
                for(int p = 1; p < 4; p++){
                    newImg[x][y][p] = im1[x][y][p] / (im2[x][y][p]+1);
                }
            }
        }

        return ShiftReScale(newImg);
    }

    public BufferedImage Not(BufferedImage img){
        int[][][] image = convertToArray(img);

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                int r = image[x][y][1];
                int g = image[x][y][2];
                int b = image[x][y][3];
                image[x][y][1] = (~r)& 0xFF; 
                image[x][y][2] = (~g)& 0xFF; 
                image[x][y][3] = (~b)& 0xFF; 
            }
        }
        return convertToBimage(image);
    }

    public BufferedImage And(BufferedImage img, BufferedImage img2){
        int[][][] image = convertToArray(img);
        int[][][] image2 = convertToArray(img2);

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                image[x][y][1] = (image[x][y][1] & image2[x][y][1])& 0xFF; 
                image[x][y][2] = (image[x][y][2] & image2[x][y][2])& 0xFF;
                image[x][y][3] = (image[x][y][3] & image2[x][y][3])& 0xFF;
            }
        }
        return convertToBimage(image);
    }

    public BufferedImage Or(BufferedImage img, BufferedImage img2){
        int[][][] image = convertToArray(img);
        int[][][] image2 = convertToArray(img2);

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                image[x][y][1] = (image[x][y][1] | image2[x][y][1])& 0xFF; 
                image[x][y][2] = (image[x][y][2] | image2[x][y][2])& 0xFF;
                image[x][y][3] = (image[x][y][3] | image2[x][y][3])& 0xFF;
            }
        }
        return convertToBimage(image);
    }

    public BufferedImage XOr(BufferedImage img, BufferedImage img2){
        int[][][] image = convertToArray(img);
        int[][][] image2 = convertToArray(img2);

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                image[x][y][1] = (image[x][y][1] ^ image2[x][y][1])& 0xFF; 
                image[x][y][2] = (image[x][y][2] ^ image2[x][y][2])& 0xFF;
                image[x][y][3] = (image[x][y][3] ^ image2[x][y][3])& 0xFF;
            }
        }
        return convertToBimage(image);
    }

    public BufferedImage ROI(BufferedImage img, BufferedImage msk){
        
        //return XOr(img, msk);
        return Or(img, msk);
        //return And(img, msk);
    }

    //************************************************************************************************************
    //************************************************LAB 4*******************************************************
    //************************************************************************************************************

    //Exercise 1 has been done (default negative)

    public BufferedImage Logfn(BufferedImage img){
        int[][][] image = convertToArray(img);
        double c = 255/(Math.log(256));
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                image[x][y][1] = (int)(c * Math.log(image[x][y][1])); 
                image[x][y][2] = (int)(c * Math.log(image[x][y][2]));
                image[x][y][3] = (int)(c * Math.log(image[x][y][3]));
            }
        }
        return convertToBimage(image);
    }

    public BufferedImage Power(BufferedImage img){
        int[][][] image = convertToArray(img);
        double r = 2.5;
        double c = Math.pow(255, 1-r);
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                image[x][y][1] = (int)(c * Math.pow(image[x][y][1],r)); 
                image[x][y][2] = (int)(c * Math.pow(image[x][y][2],r));
                image[x][y][3] = (int)(c * Math.pow(image[x][y][3],r));
            }
        }
        return convertToBimage(image);
    }

    public BufferedImage RandomLookUp(BufferedImage img){
        int[][][] image = convertToArray(img);
        int[] lut = new int[256];
        Random rnd = new Random();
        for(int i = 0; i < lut.length; i++){
            lut[i] = rnd.nextInt(256);
        }

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                image[x][y][1] = lut[image[x][y][1]]; 
                image[x][y][2] = lut[image[x][y][2]];
                image[x][y][3] = lut[image[x][y][3]];
            }
        }
        return convertToBimage(image);
    }

    public BufferedImage BitPlaneSlice(BufferedImage img){
        int[][][] image = convertToArray(img);
        int bit = 7;

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                image[x][y][1] = ((image[x][y][1] >> bit) &1)* 255; 
                image[x][y][2] = ((image[x][y][2] >> bit) &1)* 255; 
                image[x][y][3] = ((image[x][y][3] >> bit) &1)* 255; 
            }
        }

        // for(int y = 0; y < img.getHeight(); y++){
        //     for(int x = 0; x < img.getWidth(); x++){
        //         System.out.println(image[x][y][1] + " " + image[x][y][2] + " "+image[x][y][3] );
        //     }
        // }
        return convertToBimage(image);
    }

    //************************************************************************************************************
    //************************************************LAB 5*******************************************************
    //************************************************************************************************************

    //Lecture Slides method
    public int[][] HistogramLS(BufferedImage img){
        int[][][] image = convertToArray(img);
        int[] histogramR = new int[256];
        int[] histogramG = new int[256];
        int[] histogramB = new int[256];
        
        for(int x = 0; x <= 255; x++){
            histogramR[x] = 0;
            histogramG[x] = 0;
            histogramB[x] = 0;
        }

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                int r = image[x][y][1]; //r
                int g = image[x][y][2]; //g
                int b = image[x][y][3]; //b
                histogramR[r]++;
                histogramG[g]++;
                histogramB[b]++;

            }
        }

        return new int[][] {histogramR,histogramG,histogramB};
    }

    public double[][] HistogramNormalisedLS(BufferedImage img){
        int[][] ans = HistogramLS(img);
        double[] histogramR = new double[256];
        double[] histogramG = new double[256];
        double[] histogramB = new double[256];
        for(int x = 0; x< 256; x++){
            histogramR[x] = ans[0][x];
            histogramG[x] = ans[1][x];
            histogramB[x] = ans[2][x];
        }
        
        for(int x = 0; x <= 255; x++){
            histogramR[x] = histogramR[x]/(img.getWidth() * img.getHeight());
            histogramG[x] = histogramG[x]/(img.getWidth() * img.getHeight());
            histogramB[x] = histogramB[x]/(img.getWidth() * img.getHeight());
        }

        return new double[][] {histogramR,histogramG,histogramB};
        
    }

    public void printHistogramArray(int[][] array){
        System.out.println("Red:");
        for(int x = 0; x< 256; x++){
            if(array[0][x] != 0) System.out.println(x+": "+array[0][x]);
        }
        System.out.println("Green:");
        for(int x = 0; x< 256; x++){
            if(array[1][x] != 0) System.out.println(x+": "+array[1][x]);
        }
        System.out.println("Blue:");
        for(int x = 0; x< 256; x++){
            if(array[2][x] != 0) System.out.println(x+": "+array[2][x]);
        }
    }

    public void printHistogramArray(double[][] array){
        System.out.println("Red:");
        for(int x = 0; x< 256; x++){
            if(array[0][x] != 0) System.out.println(x+": "+array[0][x]);
        }
        System.out.println("Green:");
        for(int x = 0; x< 256; x++){
            if(array[1][x] != 0) System.out.println(x+": "+array[1][x]);
        }
        System.out.println("Blue:");
        for(int x = 0; x< 256; x++){
            if(array[2][x] != 0) System.out.println(x+": "+array[2][x]);
        }
    }

    //************************************************************************************************************
    //************************************************LAB 6*******************************************************
    //************************************************************************************************************

    public int[][][] Masks = {
        {//Averaging
            {1,1,1},
            {1,1,1},
            {1,1,1}
        },
        {//Weighted averaging
            {1,2,1},
            {2,4,2},
            {1,2,1}
        },
        {//4-neighbour Laplacian
            {0,-1,0},
            {-1,4,-1},
            {0,-1,0}
        },
        {//8-neighbour Laplacian
            {-1,-1,-1},
            {-1,8,-1},
            {-1,-1,-1}
        },
        {//4-neighbour Laplacian Enhancement
            {0,-1,0},
            {-1,5,-1},
            {0,-1,0}
        },
        {//8-neighbour Laplacian Enhancement
            {-1,-1,-1},
            {-1,8,-1},
            {-1,-1,-1}
        },
        {//These masks are for lab 8
            {-1,-2,-1},
            {0,0,0},
            {1,2,1}
        },
        {
            {-1,0,1},
            {-2,0,2},
            {-1,0,1}
        }

    };
    public double[] weights = {(1.0/9.0),(1.0/16.0),1,1,1,1,1,1};

    public BufferedImage convolution(BufferedImage img, int mask){
        int m = mask;
        int[][][] image = convertToArray(img);
        int[][][] newImage = new int[image.length][image[0].length][image[0][0].length];
        for(int y=1; y<img.getHeight()-1; y++){
            for(int x=1; x<img.getWidth()-1; x++){
                double r,g,b;
                r = 0; g = 0; b = 0;
                for(int s=-1; s<=1; s++){
                    for(int t=-1; t<=1; t++){
                        r += Masks[m][1-s][1-t]*image[x+s][y+t][1]; //r
                        g += Masks[m][1-s][1-t]*image[x+s][y+t][2]; //g
                        b += Masks[m][1-s][1-t]*image[x+s][y+t][3]; //b
                    }
                }
                newImage[x][y][1] =(int) Math.round(r*weights[m]); //r
                newImage[x][y][2] =(int) Math.round(g*weights[m]); //g
                newImage[x][y][3] =(int) Math.round(b*weights[m]); //b
            }
        }
        //return convertToBimage(image);
        return ShiftReScale(newImage);
    }

    //************************************************************************************************************
    //************************************************LAB 7*******************************************************
    //************************************************************************************************************

    public BufferedImage SaltAndPepper(BufferedImage img){
        Random rnd = new Random();
        int[][][] image = convertToArray(img);
        for(int y=1; y<img.getHeight()-1; y++){
            for(int x=1; x<img.getWidth()-1; x++){
                if(rnd.nextInt(100) > 89){
                    int value = 0;
                    if(rnd.nextInt(100)>49) value = 255;

                    image[x][y][1] = value; //r
                    image[x][y][2] = value; //g
                    image[x][y][3] = value; //b
                }
            }
        }
        
        return convertToBimage(image);
    }

    public BufferedImage MinFilter(BufferedImage img){
        int[][][] image = convertToArray(img);
        int[][][] newImage = new int[image.length][image[0].length][image[0][0].length];
        int[] rWindow = new int[9];
        int[] gWindow = new int[9];
        int[] bWindow = new int[9];
        for(int y=1; y<img.getHeight()-1; y++){
            for(int x=1; x<img.getWidth()-1; x++){
                int k = 0;
                for(int s = -1; s<=1;s++){
                    for(int t = -1; t<=1; t++){
                        rWindow[k] = image[x+s][y+s][1];
                        gWindow[k] = image[x+s][y+s][2];
                        bWindow[k] = image[x+s][y+s][3];
                        k++;
                    }
                }
                Arrays.sort(rWindow);
                Arrays.sort(gWindow);
                Arrays.sort(bWindow);
                newImage[x][y][1] = rWindow[0];
                newImage[x][y][2] = gWindow[0];
                newImage[x][y][3] = bWindow[0];
                
            }
        }
        return convertToBimage(newImage);
    }

    public BufferedImage MaxFilter(BufferedImage img){
        int[][][] image = convertToArray(img);
        int[][][] newImage = new int[image.length][image[0].length][image[0][0].length];
        int[] rWindow = new int[9];
        int[] gWindow = new int[9];
        int[] bWindow = new int[9];
        for(int y=1; y<img.getHeight()-1; y++){
            for(int x=1; x<img.getWidth()-1; x++){
                int k = 0;
                for(int s = -1; s<=1;s++){
                    for(int t = -1; t<=1; t++){
                        rWindow[k] = image[x+s][y+s][1];
                        gWindow[k] = image[x+s][y+s][2];
                        bWindow[k] = image[x+s][y+s][3];
                        k++;
                    }
                }
                Arrays.sort(rWindow);
                Arrays.sort(gWindow);
                Arrays.sort(bWindow);
                newImage[x][y][1] = rWindow[rWindow.length - 1];
                newImage[x][y][2] = gWindow[gWindow.length - 1];
                newImage[x][y][3] = bWindow[bWindow.length - 1];
                
            }
        }
        return convertToBimage(newImage);
    }

    public BufferedImage MidpointFilter(BufferedImage img){
        int[][][] image = convertToArray(img);
        int[][][] newImage = new int[image.length][image[0].length][image[0][0].length];
        int[] rWindow = new int[9];
        int[] gWindow = new int[9];
        int[] bWindow = new int[9];
        for(int y=1; y<img.getHeight()-1; y++){
            for(int x=1; x<img.getWidth()-1; x++){
                int k = 0;
                for(int s = -1; s<=1;s++){
                    for(int t = -1; t<=1; t++){
                        rWindow[k] = image[x+s][y+s][1];
                        gWindow[k] = image[x+s][y+s][2];
                        bWindow[k] = image[x+s][y+s][3];
                        k++;
                    }
                }
                Arrays.sort(rWindow);
                Arrays.sort(gWindow);
                Arrays.sort(bWindow);
                newImage[x][y][1] = (rWindow[rWindow.length - 1] + rWindow[0])/2;
                newImage[x][y][2] = (gWindow[gWindow.length - 1] + gWindow[0])/2;
                newImage[x][y][3] = (bWindow[bWindow.length - 1] + bWindow[0])/2;
                
            }
        }
        return convertToBimage(newImage);
    }

    public BufferedImage MedianFilter(BufferedImage img){
        int[][][] image = convertToArray(img);
        int[][][] newImage = new int[image.length][image[0].length][image[0][0].length];
        int[] rWindow = new int[9];
        int[] gWindow = new int[9];
        int[] bWindow = new int[9];
        for(int y=1; y<img.getHeight()-1; y++){
            for(int x=1; x<img.getWidth()-1; x++){
                int k = 0;
                for(int s = -1; s<=1;s++){
                    for(int t = -1; t<=1; t++){
                        rWindow[k] = image[x+s][y+s][1];
                        gWindow[k] = image[x+s][y+s][2];
                        bWindow[k] = image[x+s][y+s][3];
                        k++;
                    }
                }
                Arrays.sort(rWindow);
                Arrays.sort(gWindow);
                Arrays.sort(bWindow);
                newImage[x][y][1] = rWindow[rWindow.length/2];
                newImage[x][y][2] = gWindow[gWindow.length/2];
                newImage[x][y][3] = bWindow[bWindow.length/2];
                
            }
        }
        return convertToBimage(newImage);
    }

    //************************************************************************************************************
    //************************************************LAB 8*******************************************************
    //************************************************************************************************************

    public void MeanStandered (BufferedImage img){
        double[][] values = HistogramNormalisedLS(img);
        double[] means = new double[values.length];
        double[] standerdDiv = new double[values.length];
        for(int x = 0; x<values.length; x++){
            means[x] = Mean(values[x]);
        }
        for(int x = 0; x<values.length; x++){
            standerdDiv[x] = Standerd(means[x], values[x]);
        }

        System.out.println("Red mean: "+means[0] + " SD: " + standerdDiv[0]);
        System.out.println("Green mean: "+means[1] + " SD: " + standerdDiv[1]);
        System.out.println("Blue mean: "+means[2] + " SD: " + standerdDiv[2]);

    }
    private double Mean(double[] values){
        double total = 0.0;
        for(int i = 0; i < values.length; i++){
            total += i*values[i];
        }
        return (total);
    }
    private double Standerd(double mean, double[] values){
        double total = 0.0;
        for(int i = 0; i < values.length; i++){
            total += Math.pow(i - mean, 2) * values[i];
        }
        return Math.sqrt(total);
    }

    private BufferedImage EdgeDetecttion(BufferedImage img){
        BufferedImage img1 = convolution(img, 6);
        BufferedImage img2 = convolution(img, 7);
        return Addition(img1, img2);
    }

    public BufferedImage SThreshold(BufferedImage img){
        int thresh = 160;
        int[][][] image = convertToArray(EdgeDetecttion(img));

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){

                //red bounderies
                if(image[x][y][1] >= thresh){
                    image[x][y][1] = 0;
                    image[x][y][2] = 0;
                    image[x][y][3] = 0;
                }
                else {
                    image[x][y][1] = 255;
                    image[x][y][2] = 255;
                    image[x][y][3] = 255;
                }


                //green bounderies
                // if(image[x][y][2] >= thresh){
                //     image[x][y][1] = 0;
                //     image[x][y][2] = 0;
                //     image[x][y][3] = 0;
                // }
                // else {
                //     image[x][y][1] = 255;
                //     image[x][y][2] = 255;
                //     image[x][y][3] = 255;
                // }

                //blue bounderies
                // if(image[x][y][3] >= thresh){
                //     image[x][y][1] = 0;
                //     image[x][y][2] = 0;
                //     image[x][y][3] = 0;
                // }
                // else {
                //     image[x][y][1] = 255;
                //     image[x][y][2] = 255;
                //     image[x][y][3] = 255;
                // }

            }
        }

        return convertToBimage(image);
    }

    public BufferedImage AThreshold(BufferedImage img){
        
        double[][] values = HistogramNormalisedLS(img);
        double[] means = new double[values.length];
        for(int x = 0; x<values.length; x++){
            means[x] = Mean(values[x]);
        }

        //change this according to relavent rgb detection being used.
        int thresh = (int)Math.round(means[0]);

        int[][][] image = convertToArray(EdgeDetecttion(img));

        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){

                //red bounderies
                if(image[x][y][1] >= thresh){
                    image[x][y][1] = 0;
                    image[x][y][2] = 0;
                    image[x][y][3] = 0;
                }
                else {
                    image[x][y][1] = 255;
                    image[x][y][2] = 255;
                    image[x][y][3] = 255;
                }


                //green bounderies
                // if(image[x][y][2] >= thresh){
                //     image[x][y][1] = 0;
                //     image[x][y][2] = 0;
                //     image[x][y][3] = 0;
                // }
                // else {
                //     image[x][y][1] = 255;
                //     image[x][y][2] = 255;
                //     image[x][y][3] = 255;
                // }

                //blue bounderies
                // if(image[x][y][3] >= thresh){
                //     image[x][y][1] = 0;
                //     image[x][y][2] = 0;
                //     image[x][y][3] = 0;
                // }
                // else {
                //     image[x][y][1] = 255;
                //     image[x][y][2] = 255;
                //     image[x][y][3] = 255;
                // }

            }
        }

        return convertToBimage(image);
    }

}