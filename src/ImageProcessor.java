import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

public class ImageProcessor {
	
	// Create a clone of a buffered image
	// (The BufferedImage class describes an Image with an accessible buffer of image data.)
	public static BufferedImage copy(BufferedImage img) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return bi;
	}

	// Create a clone of a buffered image
	// (Another implementation)
/*
	public static BufferedImage copy(BufferedImage img) {
		 ColorModel cm = img.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = img.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
*/
	
	// Convert an input color image to grayscale image
	public static BufferedImage convertToGrayScale(BufferedImage src) {
		// Make a copy of the source image as the target image
		BufferedImage target = copy(src);
		int width = target.getWidth();
		int height = target.getHeight();
		
		// Scan through each row of the image
		for (int j = 0; j < height; j++) {
			// Scan through each column of the image
			for (int i = 0; i < width; i++) {
				// Get an integer pixel in the default RGB color model
				int pixel = target.getRGB(i, j);
				// Convert the single integer pixel value to RGB color
				Color oldColor = new Color(pixel);

				int red = oldColor.getRed(); 	// get red value
				int green = oldColor.getGreen();	// get green value
				int blue = oldColor.getBlue(); 	// get blue value

				// Convert RGB to grayscale using formula
				// gray = 0.299 * R + 0.587 * G + 0.114 * B
				double grayVal = 0.299 * red + 0.587 * green + 0.114 * blue;

				// Assign each channel of RGB with the same value
				Color newColor = new Color((int) grayVal, (int) grayVal, (int) grayVal);

				// Get back the integer representation of RGB color and assign it back to the original position
				target.setRGB(i, j, newColor.getRGB());
			}
		}
		// return the resulting image in BufferedImage type
		return target;
	}

	// Invert the color of an input image
	public static BufferedImage invertColor(BufferedImage src) {
		BufferedImage target = copy(src);
		int width = target.getWidth();
		int height = target.getHeight();
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = target.getRGB(i, j);
				Color oldColor = new Color(pixel);

				int red = oldColor.getRed();
				int green = oldColor.getGreen();
				int blue = oldColor.getBlue();
				
				// invert the color of each channel
				Color newColor = new Color(255 - red, 255 - green, 255 - blue);
				
				target.setRGB(i, j, newColor.getRGB());
			}
		}
		return target;
	}
	// Adjust the brightness of an input image
	public static BufferedImage adjustBrightness(BufferedImage src, int amount) {
		BufferedImage target = copy(src);
		int width = target.getWidth();
		int height = target.getHeight();
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = target.getRGB(i, j);
				Color oldColor = new Color(pixel);

				int red = oldColor.getRed();
				int green = oldColor.getGreen();
				int blue = oldColor.getBlue();

				int newRed = (red + amount > 255) ? 255 : red + amount;
				int newGreen = (green + amount > 255) ? 255 : green + amount;
				int newBlue = (blue + amount > 255) ? 255 : blue + amount;

				newRed = (newRed < 0) ? 0 : newRed;
				newGreen = (newGreen < 0) ? 0 : newGreen;
				newBlue = (newBlue < 0) ? 0 : newBlue;

				Color newColor = new Color(newRed, newGreen, newBlue);

				target.setRGB(i, j, newColor.getRGB());
			}
		}
		return target;
	}

	// Apply a blur effect to an input image by random pixel movement
	public static BufferedImage blur(BufferedImage src, int offset) {
		
		// TODO: add your implementation
		// New code added
		Random woody = new Random();
		int offsetX = woody.nextInt(offset);
		int offsetY = woody.nextInt(offset);
		//set the offset of weight and height randomly
		BufferedImage target = copy(src);
		int width = target.getWidth();
		int height = target.getHeight();
		// find the width and height of the image
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				offsetY = woody.nextInt(offset);
				offsetX = woody.nextInt(offset);
				// random the offset of weight and height every time
		
				if(i+offsetX < width && j+offsetY < height) {
					// set a limit for the image
					//if the pixel ramdonly done and still in the area of the image
					//get the pixel after changed
					int pixel = target.getRGB(i+offsetX, j+offsetY);
					Color oldColor = new Color(pixel);
					//put the pixel into target
					target.setRGB(i, j, oldColor.getRGB());
				}
			}
		}
		return target; //Output the result
	}	

	// Scale (resize) an image
	public static BufferedImage scale(BufferedImage src, int tWidth, int tHeight) {

		// TODO: add your implementation
		BufferedImage resizeImage=new BufferedImage(tWidth,tHeight,BufferedImage.TYPE_INT_RGB);
		//create buffered image
		Graphics g = resizeImage.getGraphics();
		g.drawImage(src,0,0,tWidth,tHeight,null);//draw the image
		g.dispose();		
		return resizeImage; //output the result
	}
	
	// Rotate an image by angle degrees clockwise
	public static BufferedImage rotate(BufferedImage src, double angle) {

		// TODO: add your implementation
		//Copy the image to "original"
		BufferedImage original = copy(src);
		//Get the width and height of the image
		int width = original.getWidth();
		int height = original.getHeight();
		int i, j;
		//find the center of the image
		int center_width = width / 2;
		int center_height = height / 2;
		angle = Math.toRadians(-angle);	//turn the angle to clockwise
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		//Create a image source for the final result
		BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    
		for (i=0;i<width;i++) {
	    	for (j=0;j<height;j++) {
	    		//rotate the image to the right angle
	    		//Find the target pixel of the original pixel by the formula below
	            int a = i - center_width;
	            int b = j - center_height;
	            int target_width = (int)(a*cos - b*sin + center_width);
	            int target_height = (int)(a*sin +b*cos + center_height);
	            
	            if (target_width >= 0 && target_width < width && target_height >= 0 && target_height < height) {
		            //if the pixel is still in the area of the original image
	            	//then get those original pixels into the target pixel
	            	target.setRGB(i, j, original.getRGB(target_width, target_height));
	            }
	    	}	
	    }
		return target; //Output the result
	}
	
	// Apply a swirl effect to an input image
	public static BufferedImage swirl(BufferedImage src, double degree) {
		
		// TODO: add your implementation
		BufferedImage target = copy(src);//Copy the image to "original"
		//Get the width and height of the image
		int width = target.getWidth();
		int height = target.getHeight();
		//find the center of the image
		double midX = width /2 ;
		double midY = height /2;
		
		//Scan through each row and column of the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double dx = x-midX;
				double dy=  y-midY;
				double theta =Math.atan2(dy,dx);
				double radius =Math.sqrt(dx*dx + dy*dy);
				int newx= (int)(midX + radius * (Math.cos(theta+degree*radius)));
				int newy= (int)(midY + radius * (Math.sin(theta+degree*radius)));
		
		
				if(newx>0 &&newy>0&&newx<width &&newy<height) {
					target.setRGB(x, y, src.getRGB(newx,newy));
				}
			}
		}
		
		return target; // output result
	}

	// Apply an effect to preserve partial colors of an image 
	public static BufferedImage preserveColor(BufferedImage src, boolean[][] mask, int colorVal, 
			int rgValue, int gbValue, int brValue) {
		
			BufferedImage target=copy(src);
			int height =src.getHeight();
			int width =src.getWidth();
			// to get image dimensions 
		    int blueP = colorVal &0xff;
		    int greenP = (colorVal>>8) &0xff;
		    int redP = (colorVal>>16) &0xff;
			int Alpha = (colorVal>>24) &0xff;
			/* As the pixel has 4 components(Alpha,red,green,blue) so the total number of bits required to 
			 * store the value of all the four components ARGB is equal to 4x8 = 32 bits.The bit position of the
			 * alpha is 31-24 , red is 23-16 , green is 15-8 , blue is 7-0. In this way , to get the ALPHA bits we 
			 * have to first shift the 32 bits of the pixels by 24 position and then bitwise add it with oxFF.And
			 * so on.
			 * 
			 */
			int diffRG =  redP - greenP;
			int diffGB = greenP - blueP;
			int diffBR = blueP - redP;
			int RGlow = diffRG - rgValue;
			int RGhigh = diffRG + rgValue;
			int GBlow = diffGB - gbValue;
			int GBhigh = diffGB + gbValue;
			int BRlow = diffBR - brValue;
			int BRhigh = diffBR + brValue;
			
			for(int x=0 ; x<width ; x++){
				for(int y=0 ; y<height ; y++){
					int pixel = src.getRGB(x,y);
					Color color = new Color(pixel);
					int red = color.getRed();
					int blue =color.getBlue();
					int green =color.getGreen();
					// to get the red , blue and green value of each coordinate
					
					if (red - green > RGlow && red - green < RGhigh 
							&& green - blue > GBlow && green - blue < GBhigh 
							&& blue - red > BRlow && blue - red < BRhigh){
							mask[x][y]=true;	
							/* to set a condition when the color of pixel range is similar to the chosen colorVal.
							 * it will return the boolean mask is true and preserve the color
							 */
					}
					else {
						double grayVal = 0.299 * red + 0.587 * green + 0.114 * blue;
						Color newColor = new Color((int) grayVal, (int) grayVal, (int) grayVal);
						target.setRGB(x, y, newColor.getRGB());
						// else the remain color will become gray
							
					}
				}
			}
		return target; // output result
	}

	// Perform edge detection for an input image
	public static BufferedImage detectEdges(BufferedImage src) {
		
		// TODO: add your implementation
		
		return null; // temporary for passing compilation (remove it after added your code)
	}
}