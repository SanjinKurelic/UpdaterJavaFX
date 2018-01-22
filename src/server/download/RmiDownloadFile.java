package server.download;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Sanjin Kurelić
 */
public interface RmiDownloadFile extends Remote {

    public byte[] downloadFileFromServer(String programName) throws RemoteException;

}
