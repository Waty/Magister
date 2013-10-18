package com.wart.magister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

public class DataTable extends ArrayList<DataRow> {

	public String TableName = "";
	public List<DataColumn> Columns = new ArrayList<DataColumn>();

	public DataTable() {
	}

	public DataTable(ArrayList<DataRow> rows) {
		if (rows.size() > 0) {
			Iterator<String> var6 = ((HashMap<String, Object>) rows.get(0))
					.keySet().iterator();

			while (var6.hasNext()) {
				String var7 = var6.next();
				this.Columns.add(new DataColumn(var7));
			}
		}

		Iterator<DataRow> var2 = rows.iterator();

		while (var2.hasNext()) {
			DataRow oldRow = var2.next();
			DataRow newRow = new DataRow();
			newRow.putAll(oldRow);
			this.add(newRow);
		}

	}

	public DataTable(Cursor var1) {
		if (var1 != null && var1.moveToFirst()) {
			String[] columnNames = var1.getColumnNames();
			int length = columnNames.length;

			for (int i = 0; i < length; ++i)
				this.Columns.add(new DataColumn(columnNames[i]));

			do {
				DataRow row = new DataRow();

				for (int i = 0; i < length; ++i) {
					String name = columnNames[i];
					row.put(name, var1.getString(var1.getColumnIndex(name)));
				}

				this.add(row);
			} while (var1.moveToNext());
		}

	}

	public DataTable(String[] strings) {
		int length = strings.length;

		for (int i = 0; i < length; i++)
			this.Columns.add(new DataColumn(strings[i]));
	}

	public Map<String, Integer> getColumnMapping() {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		for (int i = 0; i < this.Columns.size(); ++i)
			hashMap.put(this.Columns.get(i).Name, Integer.valueOf(i));
		return hashMap;
	}

	public String[] getColumnNames() {
		ArrayList<String> var1 = new ArrayList<String>();

		for (int i = 0; i < this.Columns.size(); i++)
			var1.add(this.Columns.get(i).Name);

		return (String[]) var1.toArray();
	}

	public ArrayList<DataRow> getValuesForColumn(String name) {
		ArrayList<DataRow> var2 = new ArrayList<DataRow>();
		Iterator<DataRow> var3 = this.iterator();

		while (var3.hasNext())
			var2.add((DataRow) (var3.next()).get(name));

		return var2;
	}

	public DataRow newRow() {
		DataRow var1 = new DataRow();
		Iterator<DataColumn> var2 = this.Columns.iterator();

		while (var2.hasNext())
			var1.put(var2.next().Name, "");

		return var1;
	}

	public void Sort(String var1) {
		Collections.sort(this, new DataRowComparer(var1.split(","), true));
	}

	public void Sort(String var1, boolean var2) {
		Collections.sort(this, new DataRowComparer(var1.split(","), var2));
	}

	public ArrayList<DataRow> toSerializebleArray() {
		return new ArrayList<DataRow>(this);
	}
}
