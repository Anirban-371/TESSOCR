package com.OCR.Read;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

public class ReadImages {
	public static float scalefactor;
	public static float offset;
	public static int count =0;
	public static String imageType="";
	public static void main(String[] args) {
		String filePath= "/home/anirban/Pictures/tess11.jpeg";
		File f = new File(filePath);
		imageType = filePath.substring(filePath.lastIndexOf(".")+1);
		System.out.println("Image type:"+imageType);
		BufferedImage ipimage;
		
		try {
			ipimage = ImageIO.read(f);
			//try to find the dpi and classify image quality
			// getting RGB content of the whole image file 
	        double d  = ipimage.getRGB(ipimage.getTileWidth() / 2,ipimage.getTileHeight() / 2); 
	        System.out.println("Double: "+d);
	        if (d >= -1.4211511E7 && d < -7254228) { 
	        	System.out.println("Process 1");
	        	scalefactor = 3f;
	        	offset = -10f; 
	        } 
	        else if (d >= -7254228 && d < -2171170) { 
	        	System.out.println("Process 2");
	        	scalefactor = 1.455f;
	        	offset = -47f;
	        } 
	        else if (d >= -2171170 && d < -1907998) { 
	        	System.out.println("Process 3");
	        	scalefactor = 1.35f;
	        	offset = -10f;
	        } 
	        else if (d >= -1907998 && d < -257) { 
	        	System.out.println("Process 4");
	        	scalefactor = 1.19f;
	        	offset = 0.5f;
	        } 
	        else if (d >= -257 && d < -1) { 
	        	System.out.println("Process 5");
	        	scalefactor = 1f;
	        	offset = 0.5f;
	        } 
	        else if (d >= -1 && d < 2) { 
	        	System.out.println("Process 6");
	        	scalefactor = 1f;
	        	offset = 0.35f;
	        } 
	        
	        System.out.println("ProcessImage:");
	        //processImg(ipimage, scalefactor, offset);
	        System.out.println("______________________________________________________________________");
	        System.out.println("NormalScaling:");
	        //normalScaling(ipimage);
	        System.out.println("______________________________________________________________________");
	        System.out.println("ProcessImageAffineTransform:");
	        //processImageAffineTransform(ipimage);
	        System.out.println("______________________________________________________________________");
	        System.out.println("ProcessImage:");
	        processImageScalr(ipimage);
	        System.out.println("______________________________________________________________________");
	        
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public static int getImageWidth(BufferedImage ipimage){
		return ipimage.getWidth();
	}
	public static int getImageheight(BufferedImage ipimage){
		return ipimage.getHeight();
	}
	public static void processImg(BufferedImage ipimage, float scaleFactor, float offset) throws IOException {		
		BufferedImage opimage = new BufferedImage(1050, 1024, ipimage.getType());
		// creating a 2D platform 
        // on the buffer image 
        // for drawing the new image 
        Graphics2D graphic = opimage.createGraphics(); 
        // drawing new image starting from 0 0 
        // of size 1050 x 1024 (zoomed images) 
        // null is the ImageObserver class object 
        graphic.drawImage(ipimage, 0, 0, 1050, 1024, null);
        graphic.dispose(); 
        // rescale OP object 
        // for gray scaling images 
        RescaleOp rescale = new RescaleOp(scaleFactor, offset, null); 
  
        // performing scaling 
        // and writing on a .png file 
        BufferedImage fopimage =null;
        if (ipimage.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
        	System.out.println("Inside indexed image");
        	fopimage = toRGB(opimage);
        	fopimage = rescale.filter(fopimage, null);
        }else {
        	System.out.println("Inside non-indexed image");
        	fopimage = rescale.filter(opimage, null); 
        }
        resultImage(fopimage);
	}
	public static void normalScaling(BufferedImage ipimage) {
		Image src=null;
		try {
			src = javax.imageio.ImageIO.read(new File("/home/anirban/Pictures/tess5.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int newImageWidth = ipimage.getWidth()*5 ;
		int newImageHeight = ipimage.getHeight()*5;
		BufferedImage opimage = new BufferedImage(newImageWidth , newImageHeight, ipimage.getType());
		//Graphics2D g = opimage.createGraphics();
		opimage.getGraphics().drawImage(src.getScaledInstance(newImageWidth, newImageHeight, Image.SCALE_SMOOTH), 0, 0, null);
		//g.drawImage(ipimage, 0, 0, newImageWidth , newImageHeight , null);
		//g.dispose();
		//RescaleOp rescale = new RescaleOp(scalefactor, offset, null); 
		  
        // performing scaling 
        // and writing on a .png file 
       /* BufferedImage fopimage =null;
        if (ipimage.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
        	System.out.println("Inside indexed image");
        	fopimage = toRGB(opimage);
        	fopimage = rescale.filter(fopimage, null);
        }else {
        	System.out.println("Inside non-indexed image");
        	fopimage = rescale.filter(opimage, null); 
        }*/
        resultImage(ipimage);
	}
	public static void processImageAffineTransform(BufferedImage ipimage) {
		BufferedImage fopimage = new BufferedImage(1050, 1024, ipimage.getType());
		AffineTransform af = new AffineTransform();
		af.scale(2, 2);

		AffineTransformOp operation = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		operation.filter(ipimage, fopimage);
		resultImage(fopimage);
	}
	public static void processImageScalr(BufferedImage ipimage) {
		
		BufferedImage resizeMe;
		try {
			ImageIO.write(ipimage,imageType, new File("/home/anirban/Pictures/processImageScalrinitialimage."+imageType));	
			resizeMe = ipimage;
			int dimension=4; //4
			System.out.println("Width:"+(int)(getImageWidth(ipimage) *dimension)+ " Height:"+(int)(getImageheight(ipimage)*dimension));
			Dimension newMaxSize = new Dimension((int)(getImageWidth(ipimage) *dimension), (int)(getImageheight(ipimage)*dimension));
			BufferedImage resizedImg = Scalr.resize(resizeMe,Method.ULTRA_QUALITY,Mode.AUTOMATIC,newMaxSize.width, newMaxSize.height);
			//BufferedImage fopimage = new BufferedImage(newMaxSize.width, newMaxSize.height, ipimage.getType());
			BufferedImage fopimage = resizedImg;
			System.out.println("Going to grayscale");
			ImageIO.write(fopimage,imageType, new File("/home/anirban/Pictures/postprocessImageScalrinitialimage."+imageType));	
			fopimage = grayScaleImage(fopimage);
			System.out.println(" grayscale image reading done");
			resultImage(fopimage);
			System.out.println("______________________________________________________________________");
			
			//AffineTransform af = new AffineTransform();
			//af.scale(1, 1);

			//AffineTransformOp operation = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			//operation.filter(resizedImg, fopimage);
			//resultImage(fopimage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void resultImage(BufferedImage resizedImg) {
		ITesseract it = new Tesseract();
		it.setTessVariable("load_system_dawg", "0");
		try {
			ImageIO.write(resizedImg,resizedImg.getType()+"",new File("/home/anirban/Pictures/ex"+count+"."+imageType));
			String str = it.doOCR(resizedImg);
			//str = str.replaceAll("[^a-zA-Z0-9]"," ");
			System.out.println("Data from image : "+str);
			str =null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO Exception:"+e.getMessage());
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			System.out.println("Tess Exception:"+e.getMessage());
		}
		count++;
		if(count==1) {
			count =0;
		}
	}
	public static BufferedImage toRGB(Image i) {
	    BufferedImage rgb = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
	    rgb.createGraphics().drawImage(i, 0, 0, null);
	    return rgb;
	}
	/*public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }

	    return new Dimension(new_width, new_height);
	}*/
	public static BufferedImage grayScale(BufferedImage ipimage) {
		BufferedImage  image=ipimage;
	      try {
	         //File input = new File("/home/anirban/Pictures/tess9.jpeg");
	         //image = ImageIO.read(input);
	         float width = image.getWidth();
	         float height = image.getHeight();
	         
	         for(int i=0; i<height; i++) {
	         
	            for(int j=0; j<width; j++) {
	            
	               Color c = new Color(image.getRGB(j, i));
	               int red = (int)(c.getRed() * 0.299);
	               int green = (int)(c.getGreen() * 0.587);
	               int blue = (int)(c.getBlue() *0.114);
	               Color newColor = new Color(red+green+blue,
	               
	               red+green+blue,red+green+blue);
	               
	               image.setRGB(j,i,newColor.getRGB());
	            }
	         }
	         return image;	         
	      } catch (Exception e) {
	    	  return null;
	      }
	   }
		public static BufferedImage grayScaleImage(BufferedImage image) {
			  try {
				  System.out.println(" grayscale image reading starts");
				  ImageIO.write(image, imageType, new File("/home/anirban/Pictures/initialimage."+imageType));	
			     //File input = new File("/home/anirban/Pictures/tess9.jpeg");
			     //image = ImageIO.read(input);
			     float width = image.getWidth();
			     float height = image.getHeight();
			     
			     for(int i=0; i<height; i++) {
			        for(int j=0; j<width; j++) {
			           Color c = new Color(image.getRGB(j, i));
			           int red = (int)(c.getRed() * 0.299);
			           int green = (int)(c.getGreen() * 0.587);
			           int blue = (int)(c.getBlue() *0.114);
			           Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
			           image.setRGB(j,i,newColor.getRGB());
			        }
			     }
			     RescaleOp rescaleOp = new RescaleOp(1.5f, -1f, null); 
			     rescaleOp.filter(image, image);
			     System.out.println(" grayscale image reading starts");
			     ImageIO.write(image, imageType, new File("/home/anirban/Pictures/grap1."+imageType));	
			     
			     return image;
			    // File ouptut = new File("grayscale.jpg");
		        //          
			  } catch (Exception e) {
				  System.out.println(e.getMessage());
				  return null;
			  }
		   }
}
