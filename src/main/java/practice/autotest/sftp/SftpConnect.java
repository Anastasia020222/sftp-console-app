package practice.autotest.sftp;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.IOException;

public class SftpConnect implements ISftpConnect {

    private static final SSHClient sshClient = new SSHClient();

    @Override
    public void open(String host, int port, String userName, String password) {
        sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        try {
            sshClient.connect(host, port);
            sshClient.authPassword(userName, password);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось установить соединение в sftp-сервером: " + e);
        }
    }

    @Override
    public void close() {
        try {
            sshClient.close();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось закрыть соединение с sftp-сервером: " + e);
        }
    }

    @Override
    public SFTPClient getSFTPClient(String remoteFilePath) {
        try {
            return sshClient.newSFTPClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openSession(String host, int port, String userName, String password) {
        this.open(host, port, userName, password);
    }

    public void closeSession() {
        this.close();
    }
}
