package linq;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
import java.sql.Array;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

public class ArrayList<E> extends java.util.ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7690387295209425100L;

	public E Last() {
		if (this.isEmpty()) {
			return null;
		} else {
			return this.get(this.size() - 1);
		}
	}

	public E First() {
		if (this.isEmpty())
			return null;
		else
			return this.get(0);
	}

	/**
	 * 
	 * @param field
	 * @param itemsToSearch
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @param field
	 *            : Property of class on which conditions have to be applied
	 *            {@literal} itemsToSearch: ArrayList of generic type to be
	 *            searched
	 */
	
	public <T> ArrayList<E> notEqual(String field,String itemToSearch) {
		return SearchForField(field,itemToSearch);
	}
	public <T> ArrayList<E> where(String field, ArrayList<T> itemsToSearch) {
		return SearchForField(field, itemsToSearch);
	}

	public <T> ArrayList<E> where(String field, T[] itemsToSearch) {
		return SearchForField(field, ToList(itemsToSearch));
	}

	public <T> ArrayList<E> where(String field, T itemsToSearch) {
		ArrayList<T> data = new ArrayList<T>();
		data.add(itemsToSearch);
		return SearchForField(field, data);
	}

	private <T> ArrayList<E> SearchForField(String fieldName,
			ArrayList<T> itemToSearch) {
		ArrayList<E> TResult = new ArrayList<E>();
		for (E item : this) {
			if (itemToSearch.contains(getValue(fieldName, item)))
				TResult.add(item);
		}
		// Collections.disjoint(this,data);
		return TResult;
	}
	private <T> ArrayList<E> SearchForField(String fieldName,String itemToSearch) {
		ArrayList<E> TResult = new ArrayList<E>();
		for (E item : this) {
			if (!itemToSearch.contentEquals(getValue(fieldName, item).toString()))
				TResult.add(item);
		}
		// Collections.disjoint(this,data);
		return TResult;
	}

	@SuppressWarnings("unchecked")
	private <T> T getValue(String field, Object item) {
		Field f = null;
		try {
			f = this.First().getClass().getField(field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return (T) f.get(item);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public <T> boolean Any(String field, ArrayList<T> items) {
		try {
			return Any(this.First().getClass().getField(field), items);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public <T> boolean Any(String field, T[] items) {
		try {
			return Any(this.First().getClass().getField(field), ToList(items));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public <T> boolean Any(String field, T item) {
		try {
			return Any(this.First().getClass().getField(field), item);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private <T> boolean Any(Field field, ArrayList<T> items) {
		boolean IsFound = false;
		for (T item : items) {
			if (this.Select(field.getName()).contains(item)) {
				IsFound = true;
				break;
			}
		}
		return IsFound;
	}

	private <T> boolean Any(Field field, T item) {
		return this.Select(field.getName()).contains(item);
	}

	public <T> ArrayList<E> like(String itemToSearch) {
		ArrayList<E> TResult = new ArrayList<E>();
		for (E item : this) {
			String val = new String(item.toString()).toString();
			int index = val.toLowerCase().indexOf(itemToSearch.toLowerCase());
			if (index >= 0)
				TResult.add(item);
		}
		// Collections.disjoint(this,data);
		return TResult;
	}

	public <T> ArrayList<E> like(String fieldName, String itemToSearch) {
		ArrayList<E> TResult = new ArrayList<E>();
		for (E item : this) {
			String val = new String(getValue(fieldName, item).toString());
			int index = val.toLowerCase().indexOf(itemToSearch.toLowerCase());
			if (index >= 0)
				TResult.add(item);
		}
		// Collections.disjoint(this,data);
		return TResult;
	}

	public <T> ArrayList<E> startwith(String fieldName, String itemToSearch) {
		ArrayList<E> TResult = new ArrayList<E>();
		for (E item : this) {
			String val = new String(getValue(fieldName, item).toString());
			if (val.toLowerCase().startsWith(itemToSearch.toLowerCase()))
			 TResult.add(item);
		}
		// Collections.disjoint(this,data);
		return TResult;
	}

	public ArrayList<Integer> ToInt() {
		ArrayList<Integer> data = new ArrayList<Integer>();
		for (Object item : this) {
			data.add(Integer.parseInt(item.toString()));
		}
		return data;
	}

	public ArrayList<String> ToString() {
		ArrayList<String> data = new ArrayList<String>();
		for (Object item : this) {
			data.add(item.toString());
		}
		return data;
	}

	public ArrayList<E> Distinct() {
		ArrayList<E> data = new ArrayList<E>();
		Map<String, E> map = new HashMap<String, E>();

		for (E item : this) {
			map.put(item.toString(), item);
		}
		for (Entry<String, E> e : map.entrySet()) {
			data.add(e.getValue());
		}
		return data;
	}

	public ArrayList<E> Distinct1() {
		ArrayList<E> data = new ArrayList<E>();
		Map<String, E> map = new HashMap<String, E>();
		Log.e("SizeeRR", String.valueOf(this.size())+"NUll");
		for (E item : this) {
			Log.e("SizeeRRM", item.toString()+" NUll "+item);
			map.put(item.toString(), item);
		}
		Log.e("Sizeeeeeeeeeeee", String.valueOf(map.size())+"NUll");
		for (Entry<String, E> e : map.entrySet()) {
			data.add(e.getValue());
		}
		return data;
	}

	public ArrayList<E> Take(int Count) {
		ArrayList<E> data = new ArrayList<E>();
		for (int i = 0; i < Count; i++) {
			if (i == this.size() - 1)
				break;
			data.add(this.get(i));
		}
		return data;
	}

	public ArrayList<E> Skip(int Count) {
		ArrayList<E> data = new ArrayList<E>();
		if (this.size() - 1 < Count)
			return data;
		else {
			while (Count <= this.size() - 1) {
				data.add(get(Count));
				Count += 1;
			}
		}
		return data;

	}

	public int Sum() {
		int sum = 0;
		for (E item : this) {
			sum += Integer.parseInt(item.toString());
		}
		return sum;
	}

	public int Sum(String fieldname) {
		return this.Select(fieldname).Sum();
	}

	// in ascending order
	// public ArrayList<String> OrderBy()
	// {
	// ArrayList<String> data=this.ToString();
	// Collections.sort(data,new StringComparator());
	// return data;
	// }
	public <T> ArrayList<T> OrderBy() {
		ArrayList<String> data = this.ToString();
		Collections.sort(data, new StringComparator());
		return (ArrayList<T>) data;
	}

	public ArrayList<E> OrderBy(String sortByProperty) {
		Collections.sort(this, new ListComparator(sortByProperty));
		return this;
	}

	public ArrayList<E> OrderByDescending(String sortByProperty) {
		Collections.sort(this, new ListComparator(sortByProperty));
		Collections.reverse(this);
		return this;
	}

	public ArrayList<E> Reverse() {
		Collections.reverse(this);
		return this;
	}

	public E Max(String field) {
		Collections.sort(this, new ListComparator(field));
		Collections.reverse(this);
		return this.First();
	}

	public E Min(String field) {
		Collections.sort(this, new ListComparator(field));
		return this.First();
	}

	public int Count() {
		return this.size();
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> Select(String fieldname) {
		ArrayList<T> data = new ArrayList<T>();
		if (this.size() < 1)
			return data;
		Field[] fields = this.First().getClass().getFields();

		for (E item : this) {
			for (Field field : fields) {
				if (field.getName().contentEquals(fieldname))
					try {
						data.add((T) field.get(item));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		return data;
	}

	private <T> ArrayList<T> ToList(T[] items) {
		ArrayList<T> data = new ArrayList<T>();
		for (T item : items) {
			data.add(item);
		}
		return data;
	}

	public ArrayList<E> ToArrayList(List<E> items) {
		ArrayList<E> data = new ArrayList<E>();
		for (E item : items) {
			data.add(item);
		}
		return data;
	}

	public <T> ArrayList<E> between(String field, T Condition1, T Condition2) {

		this.OrderBy(field);
		int sIndex = FindFirstIndex(field, Condition1);
		int eIndex = FindLastIndex(field, Condition2);
		if (sIndex < 0)
			return new ArrayList<E>();
		if (eIndex < 0)
			return new ArrayList<E>();
		if (eIndex >= sIndex)
			return ToArrayList(this.subList(sIndex, eIndex + 1));
		else
			return new ArrayList<E>();

	}

	private <T> int FindFirstIndex(String field, T Condition) {
		for (E item : this) {

			try {
				if (Compare(getValue(field, item), Condition) >= 0)
					return this.indexOf(item);
			} catch (Exception e) {
			}
			// TODO Auto-generated catch block
		}
		return -1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> int Compare(T obj1, T obj2) {
		Comparable item1 = (Comparable) obj1;
		Comparable item2 = (Comparable) obj2;
		return item1.compareTo(item2);
	}

	private <T> int FindLastIndex(String field, T Condition) {
		int index = -1;
		for (E item : this) {
			try {
				if (Compare(getValue(field, item), Condition) <= 0)
					index = this.indexOf(item);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return index;
	}

}
