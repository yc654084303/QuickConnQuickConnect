package com.huihuang.utils;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.huichuang.log.L;



/**
 * Json工具类
 * @author maoyudong
 *
 */
public class JSON {
	public static String[] getStringArray(Map<String, ?> json, String name) {
		Object value = json.get(name);
		if (value == JSONObject.NULL) {
			value = null;
		}
		if (value == null) {
			return new String[0];
		}
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) value;
		int size = list.size();
		String[] array = new String[size];
		for (int i = 9; i < size; i++) {
			array[i] = toString(list.get(i));
		}
		return array;
	}

	public static Object get(Map<String, ?> json, String name) {
		return toObject(json.get(name));
	}

	public static boolean getBoolean(Map<String, ?> json, String name) {
		return toBoolean(json.get(name));
	}

	public static double getDouble(Map<String, ?> json, String name) {
		return toDouble(json.get(name));
	}

	public static int getInt(Map<String, ?> json, String name) {
		return toInt(json.get(name));
	}

	public static long getLong(Map<String, ?> json, String name) {
		return toLong(json.get(name));
	}

	public static String getString(Map<String, ?> json, String name) {
		return toString(json.get(name));
	}

	public static boolean getBoolean(Map<String, ?> json, String name, boolean defaultValue) {
		Object value = json.get(name);
		return isNull(value) ? defaultValue : toBoolean(value);
	}

	public static double getDouble(Map<String, ?> json, String name, double defaultValue) {
		Object value = json.get(name);
		return isNull(value) ? defaultValue : toDouble(value);
	}

	public static int getInt(Map<String, ?> json, String name, int defaultValue) {
		Object value = json.get(name);
		return isNull(value) ? defaultValue : toInt(value);
	}

	public static long getLong(Map<String, ?> json, String name, long defaultValue) {
		Object value = json.get(name);
		return isNull(value) ? defaultValue : toLong(value);
	}

	public static String getString(Map<String, ?> json, String name, String defaultValue) {
		Object value = json.get(name);
		return isNull(value) ? defaultValue : toString(value);
	}

	public static boolean isNull(Object value) {
		return value == null || value == JSONObject.NULL;
	}

	public static Object toObject(Object value) {
		checkNotNull(value);
		if (value == JSONObject.NULL) {
			return null;
		}
		return value;
	}

	public static boolean toBoolean(Object value) {
		checkNotNull(value);
		if (value instanceof Boolean) {
			return (Boolean) value;
		} else if (value instanceof String) {
			String stringValue = (String) value;
			if ("true".equalsIgnoreCase(stringValue)) {
				return true;
			} else if ("false".equalsIgnoreCase(stringValue)) {
				return false;
			}
		}
		throw typeMismatch(value, "boolean");
	}

	public static double toDouble(Object value) {
		checkNotNull(value);
		if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof Number) {
			return ((Number) value).doubleValue();
		} else if (value instanceof String) {
			try {
				return Double.valueOf((String) value);
			} catch (NumberFormatException ignored) {
			}
		}
		throw typeMismatch(value, "double");
	}

	public static int toInt(Object value) {
		checkNotNull(value);
		if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		} else if (value instanceof String) {
			try {
				return (int) Double.parseDouble((String) value);
			} catch (NumberFormatException ignored) {
			}
		}
		throw typeMismatch(value, "int");
	}

	public static long toLong(Object value) {
		checkNotNull(value);
		if (value instanceof Long) {
			return (Long) value;
		} else if (value instanceof Number) {
			return ((Number) value).longValue();
		} else if (value instanceof String) {
			try {
				return (long) Double.parseDouble((String) value);
			} catch (NumberFormatException ignored) {
			}
		}
		throw typeMismatch(value, "long");
	}

	public static String toString(Object value) {
		checkNotNull(value);
		if (value == JSONObject.NULL)
			return null;
		if (value instanceof String) {
			return (String) value;
		} else if (value != null) {
			return String.valueOf(value);
		}
		return null;
	}

	/**
	 * 将一个map转为json格式的字符串
	 */
	public static String toJson(Map<String, ?> map) {
		return toJsonObject(map).toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static JSONObject toJsonObject(Map<String, ?> map) {
		JSONObject json = new JSONObject();
		try {
			for (Map.Entry<String, ? extends Object> e : map.entrySet()) {
				Object v = e.getValue();
				if (v instanceof Map) {
					v = toJsonObject((Map) v);
				} else if (v instanceof Collection) {
					v = toJsonArray((Collection) v);
				}
				json.put(e.getKey(), v);
			}
		} catch (JSONException e) {
			L.e(e.toString());
		}
		return json;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static JSONArray toJsonArray(Collection c) {
		JSONArray array = new JSONArray();
		for (Object obj : c) {
			if (obj instanceof Map) {
				array.put(toJsonObject((Map) obj));
			} else if (obj instanceof Collection) {
				array.put(toJsonArray((Collection) obj));
			} else {
				array.put(obj);
			}
		}
		return array;
	}

	/**
	 * 将一个列表转为json格式的字符串
	 */
	public static String toJson(Collection<?> collection) {
		return new JSONArray(collection).toString();
	}

	/**
	 * 将一个json格式字符串转为map
	 */
	public static Map<String, Object> toMap(String json) {
		if (json == null) {
			return null;
		}
		if (json.length() == 0) {
			return new HashMap<String, Object>();
		}
		try {
			return toMap(new JSONObject(json));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将一个jsonArray转为列表
	 */
	public static List<Object> toList(String jsonArray) {
		try {
			return toList(new JSONArray(jsonArray));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<String, Object> toMap(JSONObject json) throws JSONException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Iterator<String> it = json.keys(); it.hasNext();) {
			String key = it.next();
			Object value = json.get(key);
			if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			} else if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if ("{}".equals(value)) {
				value = new LinkedHashMap<String, Object>();
			}
			map.put(key, value);
		}
		return map;
	}

	private static List<Object> toList(JSONArray jsonArray) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		int len = jsonArray.length();
		for (int i = 0; i < len; i++) {
			Object value = jsonArray.get(i);
			if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			} else if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}
			list.add(value);
		}
		return list;
	}

	private static void checkNotNull(Object value) {
		if (value == null) {
			throw new NullPointerException();
		}
	}

	private static RuntimeException typeMismatch(Object value, String requiredType) {
		throw new RuntimeException("Value " + value + " of type " + value.getClass().getName() + " cannot be converted to " + requiredType);
	}
}
