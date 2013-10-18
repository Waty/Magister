package com.wart.magister;

import java.util.Comparator;
import java.util.Date;

public class DataRowComparer implements Comparator<DataRow> {

	private final boolean ascending;
	private final String[] fields;

	public DataRowComparer(String[] var1) {
		fields = var1;
		ascending = true;
	}

	public DataRowComparer(String[] var1, boolean var2) {
		fields = var1;
		ascending = var2;
	}

	private int getAffinity(Object var1) {
		if (var1 != null) {
			Class<? extends Object> var2 = var1.getClass();
			if (var2 == Date.class) return 5;

			if (var2 == Integer.class || var2 == Long.class || var2 == Long.TYPE || var2 == Integer.TYPE) return 2;

			if (var2 == Boolean.TYPE || var2 == Boolean.class) return 4;

			if (var2 == String.class) return 1;

			if (var2 == Double.class || var2 == Float.class || var2 == Double.TYPE || var2 == Float.TYPE) return 3;

		}

		return 0;
	}

	@Override
	public int compare(DataRow var1, DataRow var2) {
		return 0;
	}
}
