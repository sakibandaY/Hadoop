package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperty {
	// For Loading Data From Properties File
			public static String getValue(String key,String filename) {
				Properties prop = new Properties();
				InputStream input = null;
				try {

					input = LoadProperty.class.getClassLoader().getResourceAsStream(filename);
					if (input == null) {
						System.out.println("Sorry, unable to find " + filename);
						return null;
					}

					// load a properties file
					prop.load(input);

					return prop.getProperty(key);

				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return null;
			}
}
