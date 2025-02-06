package practice.autotest.sftp.files;

import net.schmizz.sshj.sftp.RemoteFile;

public interface IFiles {

    void putRemoteFile(String json, String remoteFilePath);

    RemoteFile.RemoteFileInputStream getRemoteFile(String remoteFilePath);
}
