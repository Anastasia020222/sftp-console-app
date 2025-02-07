package practice.autotest;

import practice.autotest.menu.CommandMenu;
import practice.autotest.sftp.DomainFile;

import java.util.Scanner;

public class MenuConsoleApp {

    private static final DomainFile domainFile = new DomainFile();
    private static final Scanner scanner = new Scanner(System.in);
    public static String path;

    public static void main(String[] args) {
        if (args.length != 0) {
            path = args[0];
        } else {
            throw new RuntimeException("Путь до файла .json с доменами на sftp-сервере не был передан.");
        }
        menuConnectSftp();

        CommandMenu com;
        boolean exit = false;

        while (!exit) {

            System.out.println("Введите команду: list/get_ip/get_domain/add/delete/exit");
            String input = scanner.next();
            try {
                com = CommandMenu.getCommandMenu(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("Вы ввели неверную команду.");
                continue;
            }

            switch (com) {
                case LIST:
                    domainFile.readRemoteJsonDomainFile();
                    break;
                case GET_IP:
                    System.out.println("Введите имя домена:");
                    String nameDomain = scanner.next();
                    System.out.println(domainFile.getIpByDomainName(nameDomain));
                    break;
                case GET_DOMAIN:
                    String ip = checkingValidityIpAddress("Введите ip адрес:");
                    System.out.println(domainFile.getDomainByIP(ip));
                    break;
                case ADD:
                    System.out.println("Введите имя домена:");
                    String domain = scanner.next();
                    String newIp = checkingValidityIpAddress("Введите ip домена:");
                    try {
                        domainFile.addNewAddressDomain(domain, newIp);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    break;
                case DELETE:
                    System.out.println("Введите имя домена:");
                    String deleteDomain = scanner.next();
                    String deleteIp = checkingValidityIpAddress("Введите ip домена:");
                    try {
                        domainFile.deleteDomainAndIp(deleteDomain, deleteIp);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    break;
                case EXIT:
                    domainFile.close();
                    exit = true;
                    break;
            }
        }
    }

    public static void menuConnectSftp() {
        String host = checkingValidityIpAddress("Введите хост сервера:");

        int port = getNumberData("Введите порт сервера:");

        System.out.println("Введите имя пользователя:");
        String userName = scanner.next();

        System.out.println("Введите пароль:");
        String password = scanner.next();

        domainFile.getConnect(host, port, userName, password);
    }

    public static int getNumberData(String msg) {
        int number;
        while (true) {
            System.out.println(msg);
            try {
                number = Integer.parseInt(scanner.next());
                if (number >= 0) {
                    return number;
                } else {
                    System.out.println("Ведено отрицательное число.");
                }
            } catch (Exception ex) {
                System.out.println("Введен неверный формат данных. Введите число.");
            }
        }
    }

    public static String checkingValidityIpAddress(String msg) {
        String ip;
        while (true) {
            System.out.println(msg);
            ip = scanner.next();

            String[] ipAdr = ip.split("\\.");
            if (ipAdr.length != 4) {
                System.out.println("Неверный формат. ip адрес должен состоять из четырех цифр разделенных 3-мя точками.");
                continue;
            }

            boolean isValid = true;

            for (String s : ipAdr) {
                int number;
                try {
                    number = Integer.parseInt(s);
                } catch (NumberFormatException ex) {
                    System.out.println("Введен неверный формат данных. Должны быть только числа.");
                    isValid = false;
                    break;
                }
                if (number < 0 || number > 255) {
                    System.out.println("Число " + number + " выходит за пределы диапазона от 0 до 255.");
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                return ip;
            }
        }
    }
}