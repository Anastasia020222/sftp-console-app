package practice.autotest.sftp.files;

import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.SFTPClient;
import practice.autotest.sftp.SftpConnect;

import java.io.IOException;

public abstract class AbsFiles implements IFiles {

    protected final SftpConnect sftpConnect;

    public AbsFiles() {
        this.sftpConnect = new SftpConnect();
    }

    public void getConnect(String host, int port, String userName, String password) {
        sftpConnect.openSession(host, port, userName, password);
    }

    @Override
    public RemoteFile.RemoteFileInputStream getRemoteFile(String remoteFilePath) {
        try {
            RemoteFile r = sftpConnect.getSFTPClient(remoteFilePath).open(remoteFilePath);
            RemoteFile.RemoteFileInputStream remoteFileInputStresm = r.new RemoteFileInputStream();
            return remoteFileInputStresm;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putRemoteFile(String json, String remoteFilePath) {
        try {
            SFTPClient client = sftpConnect.getSFTPClient(remoteFilePath);
            client.getFileTransfer().setPreserveAttributes(false);
            client.put(json, remoteFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        sftpConnect.closeSession();
    }
}
