package practice.autotest.menu;

public enum CommandMenu {

    LIST,
    GET_IP,
    GET_DOMAIN,
    ADD,
    DELETE,
    EXIT;

    public static CommandMenu getCommandMenu(String command) {
        return CommandMenu.valueOf(command.trim().toUpperCase());
    }
}
