/**
 * 
 */
package com.wordcloud;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.wordcloud.output.PrintWordCloud;

/**
 * @author pc
 * 
 */
public class ClientBuilder {

	private static final String path = System.getProperty("user.home")
			+ File.separator;

	private WebTarget target;

	public ClientBuilder() {
		Client c = javax.ws.rs.client.ClientBuilder.newClient();
		c.register(JacksonFeature.class);
		target = c.target("http://localhost:8080/wordcloud");
	}

	public static void main(String args[]) throws Exception {
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			ClientBuilder clientBuilder = new ClientBuilder();
			while (true) {
				System.out.println("1. Save a hashtag or rss link");
				System.out.println("2. Generate word cloud");
				int option = scanner.nextInt();
				switch (option) {
				case 1:
					System.out.println("Enter the hastag or rss link");
					clientBuilder.saveHashTags(scanner.next());
					break;
				case 2:
					Map<String, Integer> map = clientBuilder.getWordCount(100,
							clientBuilder.getTime());
					System.out.println("Enter the outputfile");
					String outputFile = scanner.next();
					System.out.println("Enter the max font weight");
					int maxFontWeight = scanner.nextInt();
					System.out.println("Enter the min font weight");
					int minFontweight = scanner.nextInt();
					PrintWordCloud.print(map, path + outputFile, maxFontWeight,
							minFontweight);
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

	}

	public void saveHashTags(String str) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH");
		DateTime dt = DateTime.now();
		long time = fmt.parseMillis(dt.toString(fmt));
		Map<String, String> formData = new HashMap<String, String>();
		formData.put("time", String.valueOf(time));
		formData.put("text", str);
		Entity<Map<String, String>> entity = Entity.entity(formData,
				MediaType.APPLICATION_JSON);
		target.request().post(entity);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getWordCount(int limit, long time) {
		Map<String, Integer> map = target.path("/count")
				.queryParam("time", String.valueOf(time))
				.queryParam("limit", limit).request()
				.accept(MediaType.APPLICATION_JSON).get(Map.class);
		return map;
	}

	
	private long getTime(){
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH");
		DateTime dt = DateTime.now();			
		long time =  fmt.parseMillis(dt.toString(fmt));
		System.out.println(time);
		return time;
		
	}
}
