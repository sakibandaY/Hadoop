package com.util;

import java.util.ArrayList;
import java.util.List;

public class ListTransform {
	/*// Transform List M*N to N*M in other list
		public static List<List<String>> transform_MN_To_NM(List<List<String>> str) {
			List<List<String>> transformedList = new ArrayList<List<String>>();
			try {
				for (int i = 0; i <= str.size(); i++) {
					List<String> tl = new ArrayList<String>();
					for (int j = 0; j < str.size(); j++) {
						List<String> list = str.get(j);
						tl.add(list.get(i));
					}
					transformedList.add(tl);
				}
			} catch (Exception e) {
			}
			return transformedList;
		}*/

	// Transform List M*N to N*M in other list
		public static List<List<String>> transform_MN_To_NM(List<List<String>> str) {
			List<List<String>> transformedList = new ArrayList<List<String>>();
			try {
				int size=0;
				for(List<String> ls:str){
					size=ls.size();
					break;
				}
				for (int i = 0; i < size; i++) {
					List<String> tl = new ArrayList<String>();
					for (int j = 0; j < str.size(); j++) {
						List<String> list = str.get(j);
						try{
							tl.add(j,list.get(i));
						}catch(Exception e){
							tl.add("");

						}
						}
					transformedList.add(tl);
				}
			} catch (Exception e) {
			}
			return transformedList;
		}

}
