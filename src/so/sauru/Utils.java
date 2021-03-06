package so.sauru;

import java.util.ArrayList;
import java.util.HashMap;

public final class Utils {

	public static Object toNumIfNum(String str) {
		/* is boolean? */
		if (str.equalsIgnoreCase("true")) {
			return true;
		} else if (str.equalsIgnoreCase("false")) {
			return false;
		}
		/* is numeric? */
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e1) {
			try {
				return Long.parseLong(str);
			} catch (NumberFormatException e2) {
				try {
					return Double.parseDouble(str);
				} catch (NumberFormatException e3) {
					return str;
				}

			}
		}
	}

	/**
	 * replacement of casting as from Object to
	 * <tt>HashMap&lt;String, Object&gt;</tt> without annoying warning.
	 * 
	 * @param object
	 * @return object it self which is casted as return type.
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object>
			toHashMapStrObj(Object object) {
		if (object == null) {
			return null;
		}
		return (HashMap<String, Object>) object;
	}

	/**
	 * replacement of casting as from Object to
	 * <tt>HashMap&lt;String, String&gt;</tt> without annoying warning.
	 * 
	 * @param object
	 * @return object it self which is casted as return type.
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String>
			toHashMapStrStr(Object object) {
		if (object == null) {
			return null;
		}
		return (HashMap<String, String>) object;
	}

	/**
	 * replacement of casting as from Object to
	 * <tt>ArrayList&lt;HashMap&lt;String, Object&gt;&gt;</tt> without annoying
	 * warning.
	 * 
	 * @param object
	 * @return object it self which is casted as return type.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>>
			toArrayListHashMapStrObj(Object object) {
		if (object == null) {
			return null;
		}
		return (ArrayList<HashMap<String, Object>>) object;
	}

	/**
	 * replacement of casting as from Object to
	 * <tt>ArrayList&lt;HashMap&lt;String, * String&gt;&gt;</tt> without
	 * annoying warning.
	 * 
	 * @param object
	 * @return object it self which is casted as return type.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, String>>
			toArrayListHashMapStrStr(Object object) {
		if (object == null) {
			return null;
		}
		return (ArrayList<HashMap<String, String>>) object;
	}
}
