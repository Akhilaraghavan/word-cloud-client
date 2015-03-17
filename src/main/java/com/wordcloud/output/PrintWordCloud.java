/**
 * 
 */
package com.wordcloud.output;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.rendersnake.HtmlAttributes;
import org.rendersnake.HtmlCanvas;

/**
 * @author pc
 * 
 */
public class PrintWordCloud {

	public static Logger logger = Logger.getLogger(PrintWordCloud.class);

	public static void print(Map<String, Integer> map, String outputFile,double maxFontWeight,double minFontweight) {
		HtmlCanvas html = new HtmlCanvas();
		FileWriter writer = null;
		try {
			html.html().head().title().write("Word Cloud")._title()._head().body().div(new HtmlAttributes("style","margin: auto; width: 60%; word-wrap: break-word;"));
			double max =0;
			for(String s : map.keySet()){
				if(max ==0){
					max = map.get(s); 
				}
				double d = (map.get(s)/max)*(maxFontWeight-minFontweight)+minFontweight;
				HtmlAttributes attr = new HtmlAttributes();
				attr.add("href", "");
				attr.add("style","font-size:"+d+"px;color:"+getColor()+";padding-right:10px;");
				html.a(attr).write(s)._a();
			}
			html._div()._body()._html();
			File file = new File(outputFile);
			writer = new FileWriter(file);
			writer.write(html.toHtml());			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	
	private static String getColor(){
		int R = (int)(Math.random()*256);
		int G = (int)(Math.random()*256);
		int B= (int)(Math.random()*256);
		Color color = new Color(R, G, B); //random color, but can be bright or dull
		
		//to get rainbow, pastel colors
		Random random = new Random();
		final float hue = random.nextFloat();
		final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		color = Color.getHSBColor(hue, saturation, luminance);
		String hex = "#"+Integer.toHexString(color.getRGB()).substring(2);
		return hex;
	}
}
