package node.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import util.Util;

public class OpenServer extends Thread {

	private String directory;
	private Socket socket;

	public OpenServer(Socket socket, String directory) {
		this.directory = directory;
		this.socket = socket;
	}

	public void run() {
		try {
			while (true) {
				String fileName;
				long fileSize;
				DataInputStream dIn = new DataInputStream(
						socket.getInputStream());
				DataOutputStream dOut  = null;

				byte option = dIn.readByte();
				switch (option) {
				case 0:
					// download
					
					fileName = dIn.readUTF();
					//System.out.println("fileName read " + fileName);
					File file = new File(directory + "/"+ fileName);
					InputStream in = new FileInputStream(file);
					fileSize = file.length();
					//System.out.println("trying");
					dOut = new DataOutputStream(socket.getOutputStream());
					//System.out.println("writing");
					dOut.writeLong(fileSize);
					dOut.flush();
					//System.out.println("fileSize writen");

					Util.copy(in, dOut, fileSize);
					in.close();

					break;
				case 1:
					// replicating files
					//System.out.println("here");
					int peerId = dIn.readInt();
					fileName = dIn.readUTF();
					//System.out.println("fileName read " + fileName);
					fileSize = dIn.readLong();
					//System.out.println("fileSize read = " + fileSize);
					String folder = "replication-peer" + peerId + "/";
					File f = new File(folder);
					Boolean created = false;
					if (!f.exists()) {
						try {
							created = f.mkdir();
						} catch (Exception e) {
							System.out
							.println("Couldn't create the folder, the file will be saved in the current directory!");
						}
					} else {
						created = true;
					}

					OutputStream out = (created) ? new FileOutputStream(
							f.toString() + "/" + fileName)
					: new FileOutputStream(fileName);
					Util.copy(dIn, out, fileSize);
					out.close();
					//System.out.println("File " + fileName + " received from peer " + peerId);
					dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeUTF("File recieved");
					break;
				default:
					throw new Exception();
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}

	}
}
