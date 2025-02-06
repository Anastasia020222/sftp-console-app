package practice.autotest.sftp;

import practice.autotest.exception.CustomException;
import practice.autotest.sftp.files.AbsFiles;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static practice.autotest.common.ParseJson.*;

public class DomainFile extends AbsFiles {

    private final String remoteFilePath = "/home/sftpuser/files/domain.json";

    public void readRemoteJsonDomainFile() {
        printDomainAddressesList(getListAddress());
    }

    public String getIpByDomainName(String domain) {
        Map<String, String> list = parseJsonDomainList(getListAddress());
        String nameIP = "";
        for (Map.Entry<String, String> l : list.entrySet()) {
            if (domain.equals(l.getKey())) {
                nameIP = l.getValue();
            }
        }
        if (nameIP.isEmpty()) {
            return "Домен с именем: " + domain + ", не был найден";
        }
        return "ip для домена " + domain + ": " + nameIP;
    }

    public String getDomainByIP(String ip) {
        Map<String, String> list = parseJsonDomainList(getListAddress());
        String nameIP = "";
        for (Map.Entry<String, String> l : list.entrySet()) {
            if (ip.equals(l.getValue())) {
                nameIP = l.getKey();
            }
        }
        if (nameIP.isEmpty()) {
            return "Домен с " + ip + " адресом не был найден";
        }
        return "Домен с ip адресом " + ip + ": " + nameIP;
    }

    private String getListAddress() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getRemoteFile(remoteFilePath), StandardCharsets.UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sb.append(line);
        }
        return sb.toString();
    }

    public void addNewAddressDomain(String domain, String ip) {
        Map<String, String> list = parseJsonDomainList(getListAddress());
        if (searchMatchesAddresses(list, domain, ip)) {
            list.put(domain, ip);
        }
        String updatedJson = createJsonDomainList(list);

        try {
            File tempFile = File.createTempFile("updated-json", ".json");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(updatedJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            putRemoteFile(tempFile.getAbsolutePath(), remoteFilePath);
            System.out.println("Домен " + domain + " успешно добавлен.");
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обновить файл: " + e);
        }
    }

    public void deleteDomainAndIp(String domain, String ip) {
        Map<String, String> list = parseJsonDomainList(getListAddress());
        try {
            deleteAddresses(domain, ip, list);
        } catch (RuntimeException e) {
            throw new CustomException(e);
        }
        String updatedJson = createJsonDomainList(list);

        try {
            File tempFile = File.createTempFile("updated-json", ".json");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(updatedJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            putRemoteFile(tempFile.getAbsolutePath(), remoteFilePath);
            System.out.println("Данные с доменом " + domain + " успешно удалены.");
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обновить файл: " + e);
        }
    }

    private boolean searchMatchesAddresses(Map<String, String> list, String domain, String ip) {
        for (Map.Entry<String, String> s : list.entrySet()) {
            if (s.getKey().equals(domain)) {
                throw new CustomException("Домен " + domain + " уже есть, попробуйте заново.");
            }
            if (s.getValue().equals(ip)) {
                throw new CustomException("Ip адрес " + ip + " уже есть, попробуйте заново.");
            }
        }
        return true;
    }
}
