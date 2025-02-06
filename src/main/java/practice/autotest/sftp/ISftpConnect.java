package practice.autotest.sftp;

import net.schmizz.sshj.sftp.SFTPClient;

public interface ISftpConnect {

    void open(String host, int port, String userName, String password);

    void close();

    SFTPClient getSFTPClient(String remoteFilePath);
}
