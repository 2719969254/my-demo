package com.example.demo.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @program: mifi-mp-common
 * @description: Gson工具类，简化Gson的使用
 * @author: tianzuo
 * @created: 2020/08/27
 */
public class GsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(GsonUtils.class);

    /**
     * 空的 JSON 数据 - "{}"。
     */
    private static final String EMPTY_JSON = "{}";
    /**
     * 空的 JSON 数组(集合)数据 - "[]"。
     */
    private static final String EMPTY_JSON_ARRAY = "[]";
    /**
     * 默认的 JSON 日期/时间字段的格式化模式 - "yyyy-MM-dd HH:mm:ss"。
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将给定的目标对象根据指定的条件参数转换成 JSON 格式的字符串。
     * <br>
     * <strong>不忽略以set开头属性</strong>
     *
     * @param target 目标对象。
     * @return 目标对象的 JSON 格式的字符串。
     */
    public static String toJson(Object target) {
        return toJson(target, null, null, false);
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成 JSON 格式的字符串。
     * <br>
     * <strong>忽略以set开头属性</strong>
     *
     * @param target 目标对象。
     * @return 目标对象的 JSON 格式的字符串。
     */
    public static String toJsonSkipSet(Object target) {
        return toJson(target, null, null, true);
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成 JSON 格式的字符串。
     *
     * @param target      目标对象。
     * @param targetType  目标对象的类型。
     * @param datePattern 日期字段的格式化模式。
     * @return 目标对象的 JSON 格式的字符串。
     */
    public static String toJson(Object target, Type targetType, String datePattern, boolean skipStartWithSet) {
        if (target == null) {
            return EMPTY_JSON;
        }
        if (datePattern == null || datePattern.length() < 1) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return "__isset_bit_vector".equals(f.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        if (skipStartWithSet) {
            builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getName().startsWith("set");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
        }
        builder.setDateFormat(datePattern);
        builder.disableHtmlEscaping();
        // 处理 string 转 int时，“” 导致崩溃问题
        builder.registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter());
        Gson gson = builder.create();
        String result = EMPTY_JSON;
        try {
            if (targetType == null) {
                result = gson.toJson(target);
            } else {
                result = gson.toJson(target, targetType);
            }
        } catch (Exception ex) {
            logger.error("目标对象 " + target.getClass().getName() + " 转换 JSON 字符串时，发生异常！", ex);
            throw ex;
        }
        return result;
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成  JSON 格式的字符串。
     *
     * @param target     目标对象。
     * @param targetType 目标对象的类型。
     * @return 目标对象的 JSON 格式的字符串。
     */
    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, null, false);
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成  JSON 格式的字符串。
     *
     * @param target     目标对象。
     * @param datePattern 日期字段的格式化模式。
     * @return 目标对象的 JSON 格式的字符串。
     */
    public static String toJson(Object target,String datePattern) {
        return toJson(target, null, datePattern, false);
    }

    /**
     * 将给定的 JSON 字符串转换成指定的类型对象。
     *
     * @param <T>         要转换的目标类型。
     * @param json        给定的 JSON 字符串。
     * @param token       com.google.gson.reflect.TypeToken 的类型指示类对象。
     * @param datePattern 日期格式模式。
     * @return 给定的  JSON 字符串表示的指定的类型对象。
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        if (json == null || json.length() < 1) {
            return null;
        }
        if (datePattern == null || datePattern.length() < 1) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return "__isset_bit_vector".equals(f.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        builder.setDateFormat(datePattern);
        builder.disableHtmlEscaping();
        // 处理 string 转 int时，“” 导致崩溃问题
        builder.registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter());
        Gson gson = builder.create();
        try {
            return (T) gson.fromJson(json, token.getType());
        } catch (Exception ex) {
            logger.error(json + " 无法转换为 " + token.getRawType().getName() + " 对象!", ex);
            throw ex;
        }
    }

    /**
     * 将给定的  JSON 字符串转换成指定类型的对象
     *
     * @param json        给定的 JSON 字符串
     * @param type        指定的类型对象
     * @param datePattern 日期格式模式
     * @return 给定 JSON 字符串表示的指定类型对象
     */
    public static Object fromJson(String json, Type type, String datePattern) {
        if (json == null || json.length() < 1) {
            return null;
        }
        if (datePattern == null || datePattern.length() < 1) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return "__isset_bit_vector".equals(f.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        builder.setDateFormat(datePattern);
        builder.disableHtmlEscaping();
        // 处理 string 转 int时，“” 导致崩溃问题
        builder.registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter());
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, type);
        } catch (Exception ex) {
            logger.error(json + " 无法转换为对象!", ex);
            throw ex;
        }
    }

    /**
     * 将给定的 JSON 字符串转换成指定类型的对象
     *
     * @param json 给定的 JSON 字符串
     * @param type 指定的类型对象
     * @return 给定 JSON 字符串表示的指定类型对象
     */
    public static Object fromJson(String json, Type type) {
        return fromJson(json, type, null);
    }

    /**
     * 将给定的 JSON 字符串转换成指定的类型对象。
     *
     * @param <T>   要转换的目标类型。
     * @param json  给定的 JSON 字符串。
     * @param token com.google.gson.reflect.TypeToken 的类型指示类对象。
     * @return 给定的 JSON 字符串表示的指定的类型对象。
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token, null);
    }

    /**
     * 将给定的 JSON 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 JavaBean 对象。</strong>
     *
     * @param <T>         要转换的目标类型。
     * @param json        给定的 JSON 字符串。
     * @param clazz       要转换的目标类。
     * @param datePattern 日期格式模式。
     * @return 给定的 JSON 字符串表示的指定的类型对象。
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        if (json == null || json.trim().length() < 1) {
            return null;
        }
        if (datePattern == null || datePattern.trim().length() < 1) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return "__isset_bit_vector".equals(f.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        builder.setDateFormat(datePattern);
        builder.disableHtmlEscaping();
        // 处理 string 转 int时，“” 导致崩溃问题
        builder.registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter());
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception ex) {
            logger.error(json + " 无法转换为 " + clazz.getName() + " 对象!", ex);
            throw ex;
        }
    }

    /**
     * 将给定的 JSON 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 JavaBean 对象。</strong>
     *
     * @param <T>   要转换的目标类型。
     * @param json  给定的 JSON 字符串。
     * @param clazz 要转换的目标类。
     * @return 给定 JSON 字符串表示的指定的类型对象。
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }

    public static boolean isEmpty(JsonObject json) {
        return null == json || 0 == json.size();
    }

    public static boolean nonEmpty(JsonObject json) {
        return !isEmpty(json);
    }

    public static boolean nonEmpty(JsonArray array) {
        return !isEmpty(array);
    }

    public static boolean isEmpty(JsonArray array) {
        for (JsonElement element : array) {
            if (element.isJsonObject()) {
                if (nonEmpty(element.getAsJsonObject())) {
                    return false;
                }
            } else if (element.isJsonArray()) {
                if (nonEmpty(element.getAsJsonArray())) {
                    return false;
                }
            } else if (element.isJsonPrimitive()) {
                if (!element.getAsJsonPrimitive().isJsonNull()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否有对应的属性存在
     *
     * @param json jsonObject
     * @param key  key
     * @return true - 存在对应的key   false - 不存在对应的key
     */
    public static boolean hasProperty(JsonObject json, String key) {
        return json.has(key);
    }

    /**
     * 根据key获取对应值 当value为null时返回defaultValue
     *
     * @param json         jsonObject
     * @param key          key
     * @param defaultValue 当值不存在时，需要返回的参数
     * @return value
     */
    public static String getStringIfNull(JsonObject json, String key, String defaultValue) {
        return getString(json, key).orElse(defaultValue);
    }

    /**
     * 根据key获取对应值 当value长度为0时返回defaultValue
     *
     * @param json         jsonObject
     * @param key          key
     * @param defaultValue 当值不存在时，需要返回的参数
     * @return value
     */
    public static String getStringIfEmpty(JsonObject json, String key, String defaultValue) {
        String value = getStringIfNull(json, key, defaultValue);
        return 0 == value.length() ? defaultValue : value;
    }

    /**
     * 根据key获取对应值 当value为空时返回defaultValue
     *
     * @param json         jsonObject
     * @param key          key
     * @param defaultValue 当值不存在时，需要返回的参数
     * @return value
     */
    public static String getStringIfBlank(JsonObject json, String key, String defaultValue) {
        String value = getStringIfNull(json, key, defaultValue);
        return StringUtils.isBlank(value) ? defaultValue : value;
    }

    /**
     * Get int value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return {@link Optional#empty()} if key not exist, or value is null.
     */
    public static Optional<Integer> getInteger(JsonObject json, String key) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return Optional.empty();
            }
            return Optional.of(Integer.parseInt(element.getAsString().trim()));
        }
        return Optional.empty();
    }

    /**
     * Get string value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return {@link Optional#empty()} if key not exist, or value is null.
     */
    public static Optional<String> getString(JsonObject json, String key) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return Optional.empty();
            } else if (element.isJsonObject()) {
                return Optional.ofNullable(toJson(element.getAsJsonObject()));
            } else {
                return Optional.ofNullable(element.getAsString());
            }
        }
        return Optional.empty();
    }

    public static int getIntegerIfNull(JsonObject json, String key, int defaultValue) {
        return getInteger(json, key).orElse(defaultValue);
    }

    public static int getIntegerIfEmpty(JsonObject json, String key, int defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value.trim());
            }
        }
        return defaultValue;
    }

    public static int getIntegerIfBlank(JsonObject json, String key, int defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isBlank(value) ? defaultValue : Integer.parseInt(value.trim());
            }
        }
        return defaultValue;
    }

    /**
     * Get long value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return {@link Optional#empty()} if key not exist, or value is null.
     */
    public static Optional<Long> getLong(JsonObject json, String key) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return Optional.empty();
            }
            return Optional.of(Long.parseLong(element.getAsString().trim()));
        }
        return Optional.empty();
    }

    public static long getLongIfNull(JsonObject json, String key, long defaultValue) {
        return getLong(json, key).orElse(defaultValue);
    }

    public static long getLongIfEmpty(JsonObject json, String key, long defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isEmpty(value) ? defaultValue : Long.parseLong(value.trim());
            }
        }
        return defaultValue;
    }

    public static long getLongIfBlank(JsonObject json, String key, long defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isBlank(value) ? defaultValue : Long.parseLong(value.trim());
            }
        }
        return defaultValue;
    }

    /**
     * Get float value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return {@link Optional#empty()} if key not exist, or value is null.
     */
    public static Optional<Float> getFloat(JsonObject json, String key) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return Optional.empty();
            }
            return Optional.of(Float.parseFloat(element.getAsString().trim()));
        }
        return Optional.empty();
    }

    /**
     * Get float value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return float value, {@code null} if key not exists.
     */
    public static Float getFloat2(JsonObject json, String key) {
        return getFloat(json, key).orElse(null);
    }

    public static float getFloatIfNull(JsonObject json, String key, float defaultValue) {
        return getFloat(json, key).orElse(defaultValue);
    }

    public static float getFloatIfEmpty(JsonObject json, String key, float defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isEmpty(value) ? defaultValue : Float.parseFloat(value.trim());
            }
        }
        return defaultValue;
    }

    public static float getFloatIfBlank(JsonObject json, String key, float defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isBlank(value) ? defaultValue : Float.parseFloat(value.trim());
            }
        }
        return defaultValue;
    }

    /**
     * Get double value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return {@link Optional#empty()} if key not exist, or value is null.
     */
    public static Optional<Double> getDouble(JsonObject json, String key) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return Optional.empty();
            }
            return Optional.of(Double.parseDouble(element.getAsString().trim()));
        }
        return Optional.empty();
    }

    /**
     * Get double value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return double value, {@code null} if key not exists.
     */
    public static Double getDouble2(JsonObject json, String key) {
        return getDouble(json, key).orElse(null);
    }

    public static double getDoubleIfNull(JsonObject json, String key, double defaultValue) {
        return getDouble(json, key).orElse(defaultValue);
    }

    public static double getDoubleIfEmpty(JsonObject json, String key, double defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isEmpty(value) ? defaultValue : Double.parseDouble(value.trim());
            }
        }
        return defaultValue;
    }

    public static double getDoubleIfBlank(JsonObject json, String key, double defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isBlank(value) ? defaultValue : Double.parseDouble(value.trim());
            }
        }
        return defaultValue;
    }

    /**
     * Get boolean value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return {@link Optional#empty()} if key not exist, or value is null.
     */
    public static Optional<Boolean> getBoolean(JsonObject json, String key) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return Optional.empty();
            }
            return Optional.of(Boolean.parseBoolean(element.getAsString().trim()));
        }
        return Optional.empty();
    }

    /**
     * Get boolean value by key.
     *
     * @param json jsonObject
     * @param key  key
     * @return boolean value, {@code null} if key not exists.
     */
    public static Boolean getBoolean2(JsonObject json, String key) {
        return getBoolean(json, key).orElse(null);
    }

    public static boolean getBooleanIfNull(JsonObject json, String key, boolean defaultValue) {
        return getBoolean(json, key).orElse(defaultValue);
    }

    public static boolean getBooleanIfEmpty(JsonObject json, String key, boolean defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isEmpty(value) ? defaultValue : Boolean.parseBoolean(value.trim());
            }
        }
        return defaultValue;
    }

    public static boolean getBooleanIfBlank(JsonObject json, String key, boolean defaultValue) {
        if (hasProperty(json, key)) {
            JsonElement element = json.get(key);
            if (element.isJsonNull()) {
                return defaultValue;
            } else {
                final String value = element.getAsString();
                return StringUtils.isBlank(value) ? defaultValue : Boolean.parseBoolean(value.trim());
            }
        }
        return defaultValue;
    }

    /**
     * 将指定对象序列化为与为其等效表示形式的 JsonElements 树
     *
     * @param javaObject javaObject
     * @return JsonElement
     */
    public static JsonElement toJsonTreeSkipSet(Object javaObject) {
        return toJsonTreeSkipSet(javaObject, null, false);
    }

    /**
     * 将指定对象序列化为与为其等效表示形式的 JsonElements 树,并指定日期字段的格式化模式
     *
     * @param javaObject  javaObject
     * @param datePattern 指定日期字段的格式化模式
     * @return JsonElement
     */
    public static JsonElement toJsonTreeSkipSet(Object javaObject, String datePattern) {
        return toJsonTreeSkipSet(javaObject, datePattern, false);
    }

    /**
     * 将指定对象序列化为与为其等效表示形式的 JsonElements 树,并决定是否忽略set开头字段
     *
     * @param javaObject       javaObject
     * @param skipStartWithSet true - 忽略set开头字段  false - 不忽略set开头字段（默认）
     * @return JsonElement
     */
    public static JsonElement toJsonTreeSkipSet(Object javaObject, boolean skipStartWithSet) {
        return toJsonTreeSkipSet(javaObject, null, skipStartWithSet);
    }

    /**
     * 将指定对象序列化为与为其等效表示形式的 JsonElements 树
     *
     * @param javaObject       javaObject
     * @param datePattern      指定日期字段的格式化模式
     * @param skipStartWithSet 是否忽略set开头字段
     * @return JsonElement
     */
    public static JsonElement toJsonTreeSkipSet(Object javaObject, String datePattern, boolean skipStartWithSet) {
        if (javaObject == null) {
            return null;
        }
        if (datePattern == null || datePattern.length() < 1) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        GsonBuilder builder = new GsonBuilder();
        builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return "__isset_bit_vector".equals(f.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        if (skipStartWithSet) {
            builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getName().startsWith("set");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
        }
        builder.setDateFormat(datePattern);
        builder.disableHtmlEscaping();
        // 处理 string 转 int时，“” 导致崩溃问题
        builder.registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter());
        Gson gson = builder.create();
        return gson.toJsonTree(javaObject);
    }
}
class IntTypeAdapter extends TypeAdapter<Number> {

    @Override
    public void write(JsonWriter out, Number value)
            throws IOException {
        out.value(value);
    }

    @Override
    public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            String result = in.nextString();
            if ("".equals(result)) {
                return null;
            }
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}