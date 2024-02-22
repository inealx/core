package id.ineal.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class __ {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static String getOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            os = "window";
        } else if (os.contains("mac")) {
            os = "mac";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            os = "unix";
        } else if (os.contains("sunos")) {
            os = "solaris";
        }
        return os;
    }

    public static boolean isEmpty(Object val) {
        try {
            if(val != null) {
                if(val instanceof List) {
                    List<?> v = (List<?>) val;
                    return (!v.isEmpty() && v.size() > 0) ? false : true;
                } else if(val instanceof Map) {
                    Map<?,?> v = (Map<?,?>) val;
                    return (!v.isEmpty() && v.size() > 0) ? false : true;
                }
                return false;
            } 
        } catch(NullPointerException e){
            return true;
        }
        return true;
    }

    // Kondisi if startWith, ada di method yg memanggil
    public static String removeStart(String data, String removeChar) {
        try {
            return (data.startsWith(removeChar)) ? data.substring(removeChar.length()).trim() : data;
        } catch(NullPointerException ignored) {
            return null;
        }
    }

    public static String removeLast(String data, String removeChar) {
        try {
            return (data.endsWith(removeChar)) ? data.substring(0,data.length() - removeChar.length()).trim() : data;
        } catch(NullPointerException ignored) {
            return null;
        }
    }

    public static String base64Encode(String val) {
        try {
            byte[] v = val.getBytes(StandardCharsets.US_ASCII);
            return new String(Base64.getEncoder().encode(v));
        } catch(NullPointerException ignored) {
            return null;
        }
    }

    public static String base64Decode(String val) {
        try {
            byte[] v = val.getBytes(StandardCharsets.US_ASCII);
            return new String(Base64.getDecoder().decode(v));
        } catch(NullPointerException ignored) {
            return null;
        }
        
    }

    public static int toInt(Object val) {
        try {
            String str = String.valueOf(val);
            if(str != null) {
                if(str.contains(".")) {
                    str = String.valueOf(val).replaceAll("\\.", "");
                }
                return Integer.parseInt(str);
            } 
        } catch(NumberFormatException e) {
            return 0;
        }
        return 0;
    }

    public static Long toLong(Object val) {
        try {
            String str = String.valueOf(val);
            if(str != null) {
                if(str.contains(".")) {
                    str = String.valueOf(val).replaceAll("\\.", "");
                }
                return Long.parseLong(str);
            } 
        } catch(NumberFormatException e) {
            return 0L;
        }
        return 0L;
    }

    public static int countChar(String src,String character) {
        try {
            return src.length() - src.replaceAll(character, "").length();
        } catch (NullPointerException ignored) {
            return 0;
        }
    }
    
    public static String replaceMent(String src,String keys,String value) {
        if(src != null) {
            StringBuffer buff = new StringBuffer();
            Matcher m = Pattern.compile("\\{\\{([^{}]+)\\}\\}").matcher(src);

            while (m.find()) {
                String val = m.group(1);
                if(val.equals(keys)) {
                    m.appendReplacement(buff, value);
                }
            }
            m.appendTail(buff);
            return buff.toString();
       }
       return "";
    }

    public static String replaceMent(String src,Map<String,Object> map) {
        if(src != null) {
            Matcher m = Pattern.compile("\\{\\{([^{}]+)\\}\\}").matcher(src);
            StringBuffer buff = new StringBuffer();
            
            while (m.find()) {
                String val = m.group(1);
                if(map.get(val) != null) {
                    m.appendReplacement(buff, String.valueOf(map.get(val)));
                }
            }
            m.appendTail(buff);
            return buff.toString();
       }
       return "";
    }

    public static int length(Object val) {
        if(val != null) {
            if(val instanceof List || val instanceof Map) {
                List<?> s = (List<?>) val;
                return s.size();
            } else if (val instanceof Map) {
                Map<?,?> s = (Map<?,?>) val;
                return s.size();
            } else {
                return Objects.toString(val).trim().length();
            }
        }
        return 0;
    }
    
    public static String toString(Object val) {
        if(val instanceof Map) {
            return String.valueOf(toJson(val));
        } else if (val instanceof JsonNode) {
            try {
                return mapper.writeValueAsString(val);
            } catch (JsonProcessingException e) {
                return String.valueOf(val);
            }
        } else if (val instanceof List) {
            return String.valueOf(toJson(val));
        }
        return String.valueOf(val);
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Object> toMap(Object val) {
        if(val != null) {
            try {
                if(val instanceof String) {
                    try {
                        return mapper.readValue(String.valueOf(val),new TypeReference<Map<String,Object>>() {});
                    } catch(Exception e) {
                        JsonNode x = toJson(val);
                        return mapper.convertValue(x, new TypeReference<Map<String,Object>>() {});
                    }
                } else if(val instanceof List) {
                    List<Object> x = (List<Object>)(val);
                    Map<String,Object> res = new LinkedHashMap<>();
                    for(int i = 0;i < x.size();i++) {
                        res.put(Objects.toString(i),x.get(i));
                    }
                    return res;    
                } else {
                    return mapper.convertValue(val, new TypeReference<Map<String,Object>>() {});
                }
            } catch(IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    public static Map<String,Object> toMap(Object val,Object...aliases) {
        if(val != null && aliases != null) {
            return toMap(String.format(String.valueOf(val), aliases));
        }
        return null;
    }

    public static <T> T toObject(Object val,Class<T> ret) {
        if(val != null && ret != null) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return (T)mapper.convertValue(toJson(val), ret);
        }
        return null;
    }

    public static JsonNode toJson(Object val) {
        if(val != null) {
            try { 
                if(val instanceof String) {
                    String x = String.valueOf(val);
                    if(x != null) {
                        if(x.charAt(0) == '{') {
                            x = val.toString().replaceAll("\\'", "\"");
                        }
                        try {
                            return mapper.readTree(x);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    }
                } else {
                    return mapper.convertValue(val,new TypeReference<JsonNode>() {});
                }
            } catch(StringIndexOutOfBoundsException e) {
                return null;
            }
        }
        return null;
    }
    
    public static JsonNode toJson(Object val,Object...aliases) {
        if(val != null && aliases != null) {
            return toJson(String.format(String.valueOf(val), aliases));
        }
        return null;
    }
}
