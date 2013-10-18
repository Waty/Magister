package com.wart.magister;

import android.app.*;
import android.net.*;
import android.content.*;
import android.widget.*;
import android.view.*;
import java.util.*;

public class MenuItem
{
	/*private static HashMap<Integer, MenuItem> all;
	public static ImageView[] check;
	private static HashMap<Integer, MenuItem> onlineItems;
	public static TextView[] status;
	public static TextView[] titles;
	public String Adapter;
	public String CacheKey;
	public String ClassName;
	public int CloseOnSelection;
	public int CurrentKey;
	public boolean Debugmode;
	public String Description;
	public String DisplayFields;
	public int Headerkolom;
	public String Help;
	public int IdDDLijst;
	public int IdDDScreen;
	public int Image;
	public String ImageName;
	public boolean LastColumnRightAlignment;
	public int MenuID;
	public String Name;
	public String OnlineMenu;
	public int ParentID;
	public boolean ReadOnly;
	public String Rights;
	public String SectionHeader;
	public HashMap<String, String> Settings;
	public String Sleutelveld;
	public String SortKey;
	public ArrayList<MenuItem> Subitems;
	public String TableName;
	public boolean Visible;
	public String VisibleCondition;
	public int VolgNR;
	private AlertDialog dialog;
	public String loaderTask;
	public String openAction;
	public boolean sectioned;
	public boolean showImage;
	public boolean sort;
	private CheckConnection task;
	public String[] titleIcon;

	static {
		MenuItem.all = null;
		MenuItem.onlineItems = new HashMap<Integer, MenuItem>();
		MenuItem.titles = new TextView[3];
		MenuItem.status = new TextView[3];
		MenuItem.check = new ImageView[3];
	}

	public MenuItem() {
		super();
		this.titleIcon = new String[] { "", "", "", "" };
		this.Headerkolom = -1;
		this.CloseOnSelection = 0;
		this.showImage = true;
		this.sectioned = false;
		this.sort = true;
		this.openAction = null;
		this.loaderTask = null;
		this.Adapter = "";
		this.task = null;
		this.Subitems = new ArrayList<MenuItem>();
		this.Visible = true;
		this.VisibleCondition = null;
	}

	public MenuItem(final String name, final String description) {
		super();
		this.titleIcon = new String[] { "", "", "", "" };
		this.Headerkolom = -1;
		this.CloseOnSelection = 0;
		this.showImage = true;
		this.sectioned = false;
		this.sort = true;
		this.openAction = null;
		this.loaderTask = null;
		this.Adapter = "";
		this.task = null;
		this.Name = name;
		this.Description = description;
	}

	public MenuItem(final HashMap<String, Object> hashMap) {
		super();
		this.titleIcon = new String[] { "", "", "", "" };
		this.Headerkolom = -1;
		this.CloseOnSelection = 0;
		this.showImage = true;
		this.sectioned = false;
		this.sort = true;
		this.openAction = null;
		this.loaderTask = null;
		this.Adapter = "";
		this.task = null;
		this.MenuID = Global.DBInt(hashMap.get("idddonderdeel"));
		this.LastColumnRightAlignment = false;
		this.Visible = true;
		if (this.MenuID == 0) {
			this.MenuID = (Global.localids += 1);
		}
		this.Description = Global.DBString(hashMap.get("hint"));
		this.ParentID = Global.DBInt(hashMap.get("parentid"));
		this.TableName = Global.DBString(hashMap.get("tabelnaam"));
		this.Name = Global.DBString(hashMap.get("naam"));
		this.IdDDScreen = Global.DBInt(hashMap.get("idddscreen"));
		this.IdDDLijst = Global.DBInt(hashMap.get("idddlijsten"));
		this.Image = Global.DBInt(hashMap.get("image"));
		this.ImageName = Global.DBString(hashMap.get("imagename"));
		this.titleIcon[0] = Global.DBString(hashMap.get("icon")).toLowerCase();
		this.Rights = Global.DBString(hashMap.get("recht")).toLowerCase();
		this.VolgNR = Global.DBInt(hashMap.get("volgnr"));
		this.Sleutelveld = Global.DBString(hashMap.get("sleutelveld"));
		this.ReadOnly = Global.DBBool(hashMap.get("readonly"));
		this.ReadOnly = MaestroRechten.readOnly(this.Rights);
		this.Debugmode = Global.DBBool(hashMap.get("bdebugmode"));
		this.ClassName = Global.DBString(hashMap.get("viewclass"));
		if (!this.ClassName.equals("")) {
			this.ClassName = ResourceManager.getFullClassName(this.ClassName);
		}
		if (hashMap.containsKey("bvisible")) {
			this.Visible = Global.DBBool(hashMap.get("bvisible"));
		}
		this.Visible = MaestroRechten.canRead(this.Rights);
		if (hashMap.containsKey("visiblecondition")) {
			this.VisibleCondition = Global.DBString(hashMap.get("visiblecondition"));
		}
		this.Help = Global.DBString(hashMap.get("uitleg"));
		this.SortKey = Global.DBString(hashMap.get("sortkey"));
		this.DisplayFields = Global.DBString(hashMap.get("displayfields"));
		this.OnlineMenu = Global.DBString(hashMap.get("onlinemenu"));
		if (hashMap.containsKey("headerkolom")) {
			this.Headerkolom = Global.DBInt(hashMap.get("headerkolom"));
		}
		if (hashMap.containsKey("sectionheader")) {
			this.SectionHeader = Global.DBString(hashMap.get("sectionheader"));
		}
		if (hashMap.containsKey("lastcolumnrightalignment")) {
			this.LastColumnRightAlignment = Global.DBBool(hashMap.get("lastcolumnrightalignment"));
		}
		this.Adapter = Global.DBString(hashMap.get("adapter"));
		this.CloseOnSelection = Global.DBInt(hashMap.get("closeonselection"));
		if (hashMap.containsKey("showimage")) {
			this.showImage = Global.DBBool(hashMap.get("showimage"));
		}
		if (hashMap.containsKey("sectioned")) {
			this.sectioned = Global.DBBool(hashMap.get("sectioned"));
		}
		if (hashMap.containsKey("sort")) {
			this.sort = Global.DBBool(hashMap.get("sort"));
		}
		this.Settings = hashMap.get("settings");
		if (Global.DBString(hashMap.get("viewclass")).equals("")) {
			if (this.IdDDLijst > 0) {
				this.ClassName = "nl.schoolmaster.common.BaseList";
				this.openAction = "openLijst";
				this.loaderTask = "DataTasks$OpenLijst";
			}
			if (this.IdDDLijst > 0 && this.IdDDScreen > 0) {
				final MenuItem clone = this.clone();
				clone.Name = String.format("%sDetail", this.Name);
				clone.IdDDLijst = 0;
				clone.openAction = "openScreen";
				clone.loaderTask = "DataTasks$OpenScreen";
				clone.ClassName = "nl.schoolmaster.common.BaseSchermSchaaf";
				this.IdDDScreen = 0;
				this.OnlineMenu = "";
				if (this.Subitems == null) {
					this.Subitems = new ArrayList<MenuItem>();
				}
				this.Subitems.clear();
				this.Subitems.add(clone);
			}
			else if (this.IdDDScreen > 0) {
				this.ClassName = "nl.schoolmaster.common.BaseSchermSchaaf";
				this.openAction = "openScreen";
				this.loaderTask = "DataTasks$OpenScreen";
			}
		}
		final String dbString = Global.DBString(hashMap.get("openaction"));
		if ("" != dbString) {
			this.openAction = dbString;
		}
		final String dbString2 = Global.DBString(hashMap.get("loadertask"));
		if ("" != dbString2) {
			this.loaderTask = dbString2.replace('.', '$');
		}
	}

	public static void Add(final MenuItem menuItem) {
		if (MenuItem.all == null) {
			MenuItem.all = new HashMap<Integer, MenuItem>();
		}
		MenuItem.all.put(menuItem.MenuID, menuItem);
	}

	public static MenuItem Find(final int n) {
		if (n >= 0 && MenuItem.all != null) {
			if (MenuItem.all.containsKey(n)) {
				return MenuItem.all.get(n);
			}
			if (MenuItem.onlineItems != null && MenuItem.onlineItems.containsKey(n)) {
				return MenuItem.onlineItems.get(n);
			}
		}
		return null;
	}

	public static MenuItem FindByNameGlobal(final String s) {
		if (MenuItem.all == null) {
			return null;
		}
		for (final MenuItem menuItem : MenuItem.all.values()) {
			if (menuItem.Name.equalsIgnoreCase(s)) {
				return menuItem;
			}
		}
		return null;
	}

	public static MenuItem MenuItemWithEnumerable(final List<HashMap<String, Object>> list) {
		final Iterator<HashMap<String, Object>> iterator = list.iterator();
		final MenuItem menuItem = new MenuItem("root", "");
		Add(menuItem);
		while (iterator.hasNext()) {
			Add(new MenuItem(iterator.next()));
		}
		for (final HashMap<String, Object> hashMap : list) {
			MenuItem find = menuItem;
			final int dbInt = Global.DBInt(hashMap.get("parentid"));
			final boolean containsKey = hashMap.containsKey("idddonderdeel");
			int dbInt2 = 0;
			if (containsKey) {
				dbInt2 = Global.DBInt(hashMap.get("idddonderdeel"));
			}
			final MenuItem find2 = Find(dbInt2);
			if (dbInt != 0) {
				find = Find(dbInt);
			}
			if (find != null) {
				if (find.Subitems == null) {
					find.Subitems = new ArrayList<MenuItem>();
				}
				find.Subitems.add(find2);
			}
		}
		return menuItem;
	}

	public static MenuItem MenuItemWithName(final String s, final String s2) {
		final MenuItem menuItem = new MenuItem(s, s2);
		Add(menuItem);
		return menuItem;
	}

	public static void Remove(final MenuItem menuItem) {
		if (MenuItem.all == null || menuItem == null) {
			return;
		}
		final Iterator<MenuItem> iterator = menuItem.Subitems.iterator();
		while (iterator.hasNext()) {
			Remove(iterator.next());
		}
		MenuItem.all.remove(menuItem.MenuID);
	}

	static /* synthetic  void access$2(final MenuItem menuItem, final CheckConnection task) {
		menuItem.task = task;
	}

	public static void addBranch(final MenuItem menuItem) {
		if (MenuItem.all == null || menuItem == null) {
			return;
		}
		if (menuItem.Subitems != null) {
			final Iterator<MenuItem> iterator = menuItem.Subitems.iterator();
			while (iterator.hasNext()) {
				addBranch(iterator.next());
			}
		}
		Add(menuItem);
	}

	private static List<HashMap<String, Object>> convertDataTableToMenuItems(final DataTable dataTable) {
		final ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (final DataRow dataRow : dataTable) {
			final HashMap<String, Object> hashMap = new HashMap<String, Object>();
			for (final DataColumn dataColumn : dataTable.Columns) {
				hashMap.put(dataColumn.Name.toLowerCase(), dataRow.get(dataColumn.Name));
			}
			list.add(hashMap);
		}
		return list;
	}

	public static MenuItem initWithTable(final DataTable dataTable) {
		return MenuItemWithEnumerable(convertDataTableToMenuItems(dataTable));
	}

	public static MenuItem initWithXml(final List<HashMap<String, Object>> list) {
		final ArrayList<HashMap<String, Object>> list2 = new ArrayList<HashMap<String, Object>>();
		for (final HashMap<String, Object> hashMap : list) {
			final HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
			for (final String s : hashMap.keySet()) {
				hashMap2.put(s.toLowerCase(), hashMap.get(s));
			}
			list2.add(hashMap2);
		}
		return MenuItemWithEnumerable(list2);
	}

	public void AfspraakVerwijderen(final Context context, final int n, final String title) {
		final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
		alertDialog$Builder.setMessage((CharSequence)"Weet u zeker dat u deze afspraak wilt verwijderen?").setCancelable(false).setTitle((CharSequence)title).setPositiveButton((CharSequence)"Ja", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
			public void onClick(final DialogInterface dialogInterface, final int n) {
				dialogInterface.dismiss();
				MediusCall.agendaitemVerwijderen(n, false);
				((Activity)context).setResult(-1);
				((Activity)context).finish();
			}
		}).setNegativeButton((CharSequence)"Nee", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
			public void onClick(final DialogInterface dialogInterface, final int n) {
				dialogInterface.dismiss();
			}
		});
		alertDialog$Builder.create().show();
	}

	public boolean CloseOnSelection() {
		return this.CloseOnSelection == 1;
	}

	public void ConnectionTest() {
		this.buildNetwerkTestDialog();
		(this.task = new CheckConnection()).execute((Object[])new Context[] { (Activity)Global.GetSharedValue("context") });
		this.dialog.show();
	}

	public MenuItem FindByName(final String s) {
		if (this.Subitems == null || Global.IsNullOrEmpty(s)) {
			return null;
		}
		for (final MenuItem menuItem : this.Subitems) {
			if (menuItem.Name.equalsIgnoreCase(s)) {
				return menuItem;
			}
		}
		return null;
	}

	public void Help() {
		((Activity)Global.GetSharedValue("context")).startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.schoolmaster.nl/metahelp")));
	}

	public MenuItem Parent() {
		if (this.ParentID < 1) {
			return null;
		}
		return Find(this.ParentID);
	}

	public void SetCurrentDocentId() {
		this.CurrentKey = Data.GetidPers();
	}

	public ArrayList<MenuItem> VisibleItems() {
		final ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		for (final MenuItem menuItem : this.Subitems) {
			if (menuItem.getVisible()) {
				list.add(menuItem);
			}
		}
		return list;
	}

	public int VisibleItemsCount() {
		int n = 0;
		final Iterator<MenuItem> iterator = this.Subitems.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getVisible()) {
				++n;
			}
		}
		return n;
	}

	public void appendMenu(final MenuItem menuItem) {
		if (menuItem.Subitems == null || menuItem.Subitems.size() < 1) {
			return;
		}
		if (Global.IsNullOrEmpty(this.TableName) && !Global.IsNullOrEmpty(menuItem.TableName)) {
			this.TableName = menuItem.TableName;
		}
		if (Global.IsNullOrEmpty(this.Sleutelveld) && !Global.IsNullOrEmpty(menuItem.Sleutelveld)) {
			this.Sleutelveld = menuItem.Sleutelveld;
		}
		if (!this.TableName.equalsIgnoreCase(menuItem.TableName)) {
			for (final MenuItem menuItem2 : this.Subitems) {
				if (Global.IsNullOrEmpty(menuItem2.TableName)) {
					menuItem2.TableName = this.TableName;
				}
			}
		}
		final ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		int n = 0;
		for (final MenuItem menuItem3 : menuItem.Subitems) {
			if (menuItem3 != null) {
				final Iterator<MenuItem> iterator3 = this.Subitems.iterator();
				while (iterator3.hasNext()) {
					if (menuItem3.Name.equalsIgnoreCase(iterator3.next().Name)) {
						n = 1;
					}
				}
				if (n == 0) {
					list.add(menuItem3.clone());
				}
				n = 0;
			}
		}
		this.Subitems.addAll(list);
		final Iterator<MenuItem> iterator4 = this.Subitems.iterator();
		while (iterator4.hasNext()) {
			iterator4.next().ParentID = this.MenuID;
		}
		Collections.sort(this.Subitems, new Comparator() {
			public int compare(final Object o, final Object o2) {
				return String.valueOf(((MenuItem)o).VolgNR).compareToIgnoreCase(String.valueOf(((MenuItem)o2).VolgNR));
			}
		});
	}

	void buildNetwerkTestDialog() {
		final LinearLayout view = new LinearLayout((Context)Global.GetSharedValue("context"));
		view.setOrientation(1);
		view.setBackgroundColor(-1);
		final LayoutInflater layoutInflater = ((Activity)Global.GetSharedValue("context")).getLayoutInflater();
		for (int i = 0; i < MenuItem.status.length; ++i) {
			final View inflate = layoutInflater.inflate(R.layout.menurow, (ViewGroup)null);
			view.addView(inflate);
			MenuItem.titles[i] = (TextView)inflate.findViewById(R.id.name);
			MenuItem.status[i] = (TextView)inflate.findViewById(R.id.description);
			MenuItem.check[i] = (ImageView)inflate.findViewById(R.id.image);
		}
		(this.dialog = new AlertDialog$Builder((Context)Global.GetSharedValue("context")).setNeutralButton((CharSequence)"Sluiten", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
			public void onClick(final DialogInterface dialogInterface, final int n) {
				dialogInterface.dismiss();
				if (MenuItem.this.task != null) {
					MenuItem.this.task.cancel(true);
					MenuItem.access$2(MenuItem.this, null);
				}
			}
		}).setCancelable(true).setTitle((CharSequence)"Netwerk test").setView((View)view).create()).setCanceledOnTouchOutside(true);
	}

	protected MenuItem clone() {
		final MenuItem menuItem = new MenuItem();
		menuItem.Name = this.Name;
		menuItem.Description = this.Description;
		if (this.Subitems != null) {
			menuItem.Subitems = new ArrayList<MenuItem>(this.Subitems);
		}
		menuItem.IdDDScreen = this.IdDDScreen;
		menuItem.IdDDLijst = this.IdDDLijst;
		menuItem.TableName = this.TableName;
		menuItem.CurrentKey = this.CurrentKey;
		menuItem.Image = this.Image;
		menuItem.ImageName = this.ImageName;
		int n = -1;
		for (final String s : this.titleIcon) {
			final String[] titleIcon2 = menuItem.titleIcon;
			++n;
			titleIcon2[n] = s;
		}
		menuItem.Rights = this.Rights;
		menuItem.ReadOnly = this.ReadOnly;
		menuItem.VolgNR = this.VolgNR;
		menuItem.Sleutelveld = this.Sleutelveld;
		menuItem.Debugmode = this.Debugmode;
		menuItem.Visible = this.Visible;
		menuItem.VisibleCondition = this.VisibleCondition;
		menuItem.Help = this.Help;
		menuItem.SortKey = this.SortKey;
		menuItem.DisplayFields = this.DisplayFields;
		menuItem.ClassName = this.ClassName;
		menuItem.CacheKey = this.CacheKey;
		menuItem.OnlineMenu = this.OnlineMenu;
		menuItem.openAction = this.openAction;
		menuItem.loaderTask = this.loaderTask;
		menuItem.Headerkolom = this.Headerkolom;
		menuItem.SectionHeader = this.SectionHeader;
		if (this.Settings != null) {
			menuItem.Settings = new HashMap<String, String>(this.Settings);
		}
		menuItem.LastColumnRightAlignment = this.LastColumnRightAlignment;
		menuItem.makeParent(this.MenuID);
		MenuItem.onlineItems.put(menuItem.MenuID, menuItem);
		return menuItem;
	}

	public String dumpHierarchy() {
		final StringBuilder sb = new StringBuilder();
		this.dumpHierarchyInternal(0, sb);
		return sb.toString();
	}

	protected void dumpHierarchyInternal(final int n, final StringBuilder sb) {
		sb.append(String.format("\n[%d-%d]", this.MenuID, this.ParentID));
		for (int i = 0; i < n; ++i) {
			sb.append('.');
		}
		if (this.Name == null) {
			sb.append("No name");
		}
		else {
			sb.append(this.Name);
		}
		if (this.Subitems != null) {
			final int n2 = n + 1;
			final Iterator<MenuItem> iterator = this.Subitems.iterator();
			while (iterator.hasNext()) {
				iterator.next().dumpHierarchyInternal(n2, sb);
			}
		}
	}

	public int getActiveKey() {
		MenuItem parent = this;
		while (parent.CurrentKey == 0) {
			parent = parent.Parent();
			if (parent == null) {
				return 0;
			}
		}
		return parent.CurrentKey;
	}

	public String[] getActiveTitleIcon() {
		MenuItem parent = this;
		while (Global.IsNullOrEmpty(parent.titleIcon[0])) {
			parent = parent.Parent();
			if (parent == null) {
				return this.titleIcon;
			}
		}
		return parent.titleIcon;
	}

	public Intent getIntent(final Context context) {
		Intent intent = null;
		Intent intent2 = null;
		try {
			final Intent intent3;
			intent2 = (intent3 = new Intent(context, (Class)ResourceManager.getClass(this.ClassName)));
			final String s = "menuitemid";
			final MenuItem menuItem = this;
			final int n = menuItem.MenuID;
			intent3.putExtra(s, n);
			return intent2;
		}
		catch (Exception ex2) {}
		while (true) {
			try {
				final Intent intent3 = intent2;
				final String s = "menuitemid";
				final MenuItem menuItem = this;
				final int n = menuItem.MenuID;
				intent3.putExtra(s, n);
				return intent2;
				final Exception ex;
				Log.Trace(ex.getMessage());
				return intent;
			}
			catch (Exception ex) {
				intent = intent2;
				continue;
			}
			break;
		}
	}

	public DataTable getLeerlingData() {
		final DataTable openQuery = database.OpenQuery(String.format("SELECT l.telefoon1, l.telefoon2, l.gsm, trim(l.roepnaam) || (case when (trim(l.tussenvoeg) = null or trim(l.tussenvoeg) = '') then ' ' else (' ' || trim(l.tussenvoeg) || ' ') end) || l.achternaam as naam_vol, l.mentor, p.naam as mentornaam FROM leerling l LEFT JOIN persoon p ON p.id=l.mentor WHERE l.id = %d", this.CurrentKey));
		if (openQuery != null && openQuery.size() > 0) {
			final DataRow dataRow = openQuery.get(0);
			Global.SetSharedValue("leerling.mentor", Global.DBString(dataRow.get("mentor")));
			Global.SetSharedValue("mentor", Global.DBString(dataRow.get("mentornaam")));
			for (final Object next : ((HashMap<K, Object>)dataRow).values()) {
				if ((Global.DBString(next).equals("telefoon1") || Global.DBString(next).equals("telefoon2") || Global.DBString(next).equals("gsm")) && !Global.IsNullOrEmpty(Global.DBString(next))) {
					Global.SetSharedValue("leerling.NAW", next);
					break;
				}
			}
			this.Name = Global.DBString(dataRow.get("naam_vol")).trim();
		}
		return null;
	}

	public String getName() {
		return Global.resolveToken(this.Name);
	}

	public DataTable getPersoonData() {
		final DataTable openQuery = database.OpenQuery(String.format("SELECT telefoon1, telefoon2, trim(roepnaam) || (case when (trim(tussenvoegsel) = null or trim(tussenvoegsel) = '') then ' ' else (' ' || trim(tussenvoegsel) || ' ') end) || achternaam as naam_vol FROM persoon WHERE id = %d", this.CurrentKey));
		if (openQuery != null && openQuery.size() > 0) {
			final DataRow dataRow = openQuery.get(0);
			for (final Object next : ((HashMap<K, Object>)dataRow).values()) {
				if ((Global.DBString(next).equals("telefoon1") || Global.DBString(next).equals("telefoon2")) && !Global.IsNullOrEmpty(Global.DBString(next))) {
					Global.SetSharedValue("persoon.NAW", next);
					break;
				}
			}
			this.Name = Global.DBString(dataRow.get("naam_vol")).trim();
		}
		return null;
	}

	public String getSleutelveld() {
		if (this.Sleutelveld != null && this.Sleutelveld.length() > 0) {
			return this.Sleutelveld;
		}
		if (this.getTableName().equalsIgnoreCase("sis_leer")) {
			return "stamnr";
		}
		if (this.TableName.startsWith("sis_")) {
			return String.format("id%s", this.getTableName().substring(4));
		}
		return String.format("id%s", this.getTableName());
	}

	public String getTableName() {
		if (this.TableName != null && this.TableName.length() > 0) {
			return this.TableName;
		}
		if (this.MenuID < 1 && this.ParentID < 1) {
			return "";
		}
		final MenuItem find = Find(this.ParentID);
		if (find != null) {
			return Global.DBString(find.getTableName());
		}
		return "";
	}

	public boolean getVisible() {
		if (!MaestroRechten.canRead(this.Rights)) {
			return false;
		}
		if (this.VisibleCondition != null) {
			return Global.DBBool(Global.GetSharedValue(this.VisibleCondition));
		}
		return this.Visible;
	}

	public DataTable loadAanwezigheidDag() {
		return (DataTable)Global.GetSharedValue("leerlingAbsenties");
	}

	public void loadBerichten() {
		Global.SetSharedValue("berichtenPage", 1);
	}

	public DataTable loadHuiswerk() {
		final DataTable openQuery = database.OpenQuery("SELECT idagendaitem, dstart, bericht, aantekeningleer FROM agendaitem");
		final Calendar instance = Calendar.getInstance();
		final Date time = instance.getTime();
		instance.add(5, 7);
		final Date time2 = instance.getTime();
		String string = "";
		for (int i = 0; i < openQuery.size(); ++i) {
			if (openQuery.get(i).get("dstart") != null && Global.DBDate(openQuery.get(i).get("dstart")).compareTo(time) >= 0 && Global.DBDate(openQuery.get(i).get("dstart")).compareTo(time2) < 0 && ((openQuery.get(i).get("bericht") != null && !Global.DBString(openQuery.get(i).get("bericht")).equalsIgnoreCase("")) || (openQuery.get(i).get("aantekeningleer") != null && !Global.DBString(openQuery.get(i).get("aantekeningleer")).equalsIgnoreCase("")))) {
				string = String.valueOf(string) + openQuery.get(i).get("idagendaitem") + ",";
			}
		}
		final int length = string.length();
		DataTable openQuery2 = null;
		if (length > 0) {
			openQuery2 = database.OpenQuery("SELECT a.*, h.bgemaakt FROM agendaitem a LEFT JOIN agendahuiswerk h ON h.idagendaitem = @a.idagendaitem WHERE a.idagendaitem IN ( " + string.substring(0, -1 + string.length()) + " ) AND idAgendaLessoort IN (1,2,3,4,5) AND idtype IN (2,7,13) AND idagendastatus NOT IN (4,5)");
			openQuery2.Sort("dstart,lesuurvan", true);
		}
		if (openQuery2 != null) {
			for (final DataRow dataRow : openQuery2) {
				dataRow.put("datum", Global.FormatDate((Date)dataRow.get("dstart"), "EEEE d MMMM"));
			}
		}
		return openQuery2;
	}

	public DataTable loadMapStructuur() {
		return database.OpenQuery("SELECT * from mapstructuur");
	}

	public DataTable loadMededelingen() {
		if (Global.GetSharedValue("localMededelingenTable") == null) {
			Global.MededelingenCount();
		}
		return (DataTable)Global.GetSharedValue("localMededelingenTable");
	}

	public DataTable loadRoosterwijzigingen() {
		if (Global.GetSharedValue("localRoosterwijzigingenTable") == null) {
			Global.RoosterwijzigingenCount();
		}
		return (DataTable)Global.GetSharedValue("localRoosterwijzigingenTable");
	}

	public DataTable loadToetsen() {
		if (Global.GetSharedValue("localToetsenTable") == null) {
			Global.ToetsenCount();
		}
		return (DataTable)Global.GetSharedValue("localToetsenTable");
	}

	public void loadVandaag() {
		Global.RoosterwijzigingenCount();
		Global.MededelingenCount();
		Global.BerichtenCount();
		Global.ToetsenCount();
	}

	protected void makeParent(final int parentID) {
		this.MenuID = (Global.localids += 1);
		this.ParentID = parentID;
		if (this.Subitems != null) {
			final Iterator<MenuItem> iterator = this.Subitems.iterator();
			while (iterator.hasNext()) {
				iterator.next().ParentID = this.MenuID;
			}
		}
	}

	public void merge(final MenuItem menuItem) {
		if (menuItem != null) {
			final MenuItem findByName = menuItem.FindByName("Maestro");
			if (findByName != null) {
				final MenuItem findByName2 = findByName.FindByName("Maestro");
				if (findByName2 == null) {
					Log.Error("Menu 'Maestro' niet gevonden.");
					return;
				}
				for (final MenuItem menuItem2 : findByName2.Subitems) {
					if (!Global.IsNullOrEmpty(menuItem2.Name)) {
						final Iterator<MenuItem> iterator2 = MenuItem.all.values().iterator();
						int n = 0;
						while (iterator2.hasNext()) {
							final MenuItem menuItem3 = iterator2.next();
							if (!Global.IsNullOrEmpty(menuItem3.OnlineMenu) && menuItem2.Name.equalsIgnoreCase(menuItem3.OnlineMenu)) {
								for (final MenuItem menuItem4 : menuItem2.Subitems) {
									if (menuItem4.Name.equalsIgnoreCase("Ouders")) {
										menuItem4.Rights = "maestrollouders";
										menuItem4.ReadOnly = MaestroRechten.readOnly(menuItem4.Rights);
										menuItem4.Visible = MaestroRechten.canRead(menuItem4.Rights);
									}
									else {
										if (!menuItem4.Name.equalsIgnoreCase("Medisch")) {
											continue;
										}
										menuItem4.Rights = "maestrollmedisch";
										menuItem4.ReadOnly = MaestroRechten.readOnly(menuItem4.Rights);
										menuItem4.Visible = MaestroRechten.canRead(menuItem4.Rights);
									}
								}
								menuItem3.appendMenu(menuItem2);
								n = 1;
							}
						}
						if (n == 0) {
							continue;
						}
						addBranch(menuItem2);
					}
				}
			}
		}
	}

	public void openLijst() {
		Global.SetSharedValue("idLijst", this.IdDDLijst);
		Global.SetSharedValue("tablename", this.getTableName());
		Global.SetSharedValue("keyField", this.getSleutelveld());
		Global.SetSharedValue("keyValue", String.format("%d", this.getActiveKey()));
	}

	public void openScreen() {
		Global.SetSharedValue("idScreen", this.IdDDScreen);
		Global.SetSharedValue("tablename", this.getTableName());
		Global.SetSharedValue("keyField", this.getSleutelveld());
		Global.SetSharedValue("keyValue", String.format("%d", this.getActiveKey()));
	}

	public DataTable passHuiswerk() {
		return database.OpenQuery(this.resolveString("SELECT * "));
	}

	public DataTable queryDB() {
		final String s = this.Settings.get("query");
		if (s == null) {
			return null;
		}
		return database.OpenQuery(this.resolveString(s));
	}

	public String resolveString(final String s) {
		if (s == null || !s.contains("[")) {
			return s;
		}
		final StringBuilder sb = new StringBuilder();
		int n = s.indexOf(91);
		int i = s.indexOf(93);
		sb.append(s.substring(0, n));
		while (i > n) {
			final String substring = s.substring(n + 1, i);
			if (substring.equalsIgnoreCase("currentkey")) {
				sb.append(this.getActiveKey());
			}
			else if (substring.startsWith("@")) {
				sb.append(Global.GetSharedValue(substring.substring(1)));
			}
			n = s.indexOf(91, i);
			if (n <= i) {
				sb.append(s.substring(i + 1));
				break;
			}
			sb.append(s.substring(i + 1, n));
			i = s.indexOf(93, n);
		}
		return sb.toString();
	}

	public void setSleutelveld(final String sleutelveld, final boolean b) {
		if (this.Sleutelveld != null && this.Sleutelveld.length() > 0 && !b) {
			return;
		}
		this.Sleutelveld = sleutelveld;
	}

	public void setTableName(final String tableName, final boolean b) {
		if (this.TableName != null && this.TableName.length() > 0 && !b) {
			return;
		}
		this.TableName = tableName;
	}

	public void setTitleIcons(final String... array) {
		for (int i = 0; i < this.titleIcon.length; ++i) {
			if (i < array.length) {
				this.titleIcon[i] = array[i].toLowerCase();
			}
			else {
				this.titleIcon[i] = "";
			}
		}
	}*/
}
