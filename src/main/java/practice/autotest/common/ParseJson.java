package practice.autotest.common;

import practice.autotest.exception.CustomException;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ParseJson {


    public static Map<String, String> parseJsonDomainList(String json) {
        TreeMap<String, String> listDomainAddress = new TreeMap<>();
        json = json.replaceAll("\\s", "");
        String startJson = "{\"addresses\":[{";

        String domain = "";
        String ip = "";
        int index = startJson.length() + 1;

        if (json.startsWith(startJson)) {
            for (; index < json.length(); ) {
                int end = json.indexOf('"', index);
                if (end == -1) break;
                String key = json.substring(index, end);
                index = end;
                if (key.equals("domain")) {
                    if (json.startsWith("\":\"", index)) {
                        index = json.indexOf("\":\"", index) + 3;
                        end = json.indexOf('"', index);
                        domain = json.substring(index, end);
                    }
                }
                if (key.equals("ip")) {
                    if (json.startsWith("\":\"", index)) {
                        index = json.indexOf("\":\"", index) + 3;
                        end = json.indexOf('"', index);
                        ip = json.substring(index, end);
                    }
                }
                listDomainAddress.put(domain, ip);
                index = end + 1;
            }
        } else {
            System.out.println("Строка не имеет начала с - '{\\\"addresses\\\":[{\"'");
        }
        return listDomainAddress;
    }

    public static void printDomainAddressesList(String json) {
        Map<String, String> list = new TreeMap<>(parseJsonDomainList(json));
        StringBuilder sb = new StringBuilder();
        System.out.println("Список всех доменов и ip адресов:");
        for (Map.Entry<String, String> s : list.entrySet()) {
            sb.append("Domain: " + s.getKey() + ", ip: " + s.getValue() + "\n");
        }
        System.out.println(sb);
    }

    public static String createJsonDomainList(Map<String, String> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("{" + "\n" + "  \"addresses\": [" + "\n");

        Iterator<Map.Entry<String, String>> iterator = list.entrySet().iterator();
        for (Map.Entry<String, String> l : list.entrySet()) {
            iterator.next();
            sb.append("    {" + "\n");
            sb.append("      \"domain\": " + "\"" + l.getKey() + "\",");
            sb.append("\n" + "      \"ip\": " + "\"" + l.getValue() + "\"");
            sb.append("\n" + "    }");

            if (iterator.hasNext()) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ]" + "\n" + "}");
        return sb.toString();
    }

    public static Map<String, String> deleteAddresses(String domain, String ip, Map<String, String> list) {
        int size = list.size();
        for (Map.Entry<String, String> d : list.entrySet()) {
            if (d.getKey().equals(domain) || d.getValue().equals(ip)) {
                list.remove(d.getKey());
                list.remove(d.getValue());
                break;
            }
        }
        if (list.size() == size) {
            throw new CustomException("Такого домена или ip адреса нет в списке. Попробуйте заново.");
        }
        return list;
    }
}
