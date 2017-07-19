package com.dataofbank.gsxt;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImgUtil {

	public static byte[] cutImage(byte[] src, int x, int y, int w, int h) throws IOException {
		Iterator<?> iterator = ImageIO.getImageReadersByFormatName("png");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new ByteArrayInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		Rectangle rect = new Rectangle(x, y, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", byteArrayOutputStream);
		
		byte[] result = byteArrayOutputStream.toByteArray();
		return result;
	}

	/**
	 * 
	 * dest = src1 - src
	 * 
	 * @param src
	 * @param src1
	 * @param dest
	 */
	public static int getX(byte[] src, byte[] src1) {

		try {
			BufferedImage imageSrc = ImageIO.read(new ByteArrayInputStream(src));
			BufferedImage imageSrc1 = ImageIO.read(new ByteArrayInputStream(src1));
			
			int w = imageSrc.getWidth();
			int h = imageSrc.getHeight();

			int[] average = new int[w];//每一列 灰度平均值

			for (int x = 0; x < w; x++) {
				int sum = 0;
				for (int y = 0; y < h; y++) {
					Color color = new Color(imageSrc.getRGB(x, y));
					Color color1 = new Color(imageSrc1.getRGB(x, y));

					int gray = (color.getRed() * 30 + color.getGreen() * 59 + color.getBlue() * 11 + 50) / 100;
					int gray1 = (color1.getRed() * 30 + color1.getGreen() * 59 + color1.getBlue() * 11 + 50) / 100;

					int val = gray - gray1;
					sum += val;
				}

				average[x] = sum / h;

			}
			int[] deviation = new int[w];//每一列 灰度方差
			
			for (int x = 0; x < w; x++) {
				int sum = 0;
				for (int y = 0; y < h; y++) {
					Color color = new Color(imageSrc.getRGB(x, y));
					Color color1 = new Color(imageSrc1.getRGB(x, y));

					int gray = (color.getRed() * 30 + color.getGreen() * 59 + color.getBlue() * 11 + 50) / 100;
					int gray1 = (color1.getRed() * 30 + color1.getGreen() * 59 + color1.getBlue() * 11 + 50) / 100;

					int value = gray - gray1;
					
					int avg = average[x];
					sum = (int) Math.pow(value-avg, 2);
				}
				deviation[x] = sum / h;

			}
			
			//在50列之后 连续40列的方差之和最大的 起始点
			int begain = 40;
			int max = 0;
			int maxI = begain;
			for(int i=begain;i<deviation.length-40;i++){
				int sum = 0;
				for(int j=i;j<i+40;j++){
					sum += deviation[j];
				}
				if(sum>max){
					max = sum;
					maxI = i;
				}
				
			}
			
			return maxI;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}



}
