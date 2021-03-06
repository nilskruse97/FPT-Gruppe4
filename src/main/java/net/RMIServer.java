package net;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import interfaces.ClientRemote;
import interfaces.IController;
import interfaces.ServerRemote;
import javafx.application.Platform;

public class RMIServer extends UnicastRemoteObject implements ServerRemote {
	private IController contr;
	private ArrayList<String> clientlist;

	public RMIServer(IController contr, ArrayList<String> clientlist) throws RemoteException {
		super();
		this.contr = contr;
		this.clientlist = clientlist;
	}

	@Override
	public void play() throws RemoteException {
		contr.play();
	}

	@Override
	public void next() throws RemoteException {
		contr.next();
	}

	@Override
	public void pause() throws RemoteException {
		contr.pause();
	}

	@Override
	public void addSongToPlaylist(int index) throws RemoteException {
		Platform.runLater(() -> {
			contr.addToPlaylist(index);
		});

	}

	@Override
	public void deleteSongFromPlaylist(int index) throws RemoteException {
		Platform.runLater(() -> {
			contr.deleteSongFromPlaylist(index);
		});

	}

	@Override
	public void addAllToPlaylist() throws RemoteException {
		Platform.runLater(() -> {
			contr.addAllToPlaylist();
		});
	}

	@Override
	public void changeSongProperties(int libindex, String title, String album, String interpret)
			throws RemoteException {
		Platform.runLater(() -> contr.changeSongProperties(libindex, title, album, interpret));

	}

	@Override
	public void load() throws RemoteException {
		Platform.runLater(() -> contr.load());

	}

	@Override
	public void save() throws RemoteException {
		contr.save();
	}

	@Override
	public void update() throws RemoteException {
		for (String clientname : clientlist) {
			ClientRemote client = null;
			try {
				client = (ClientRemote) Naming.lookup("//localhost/" + clientname);
				client.updateView(contr.getLibrary(), contr.getPlaylist(),
						contr.getLibraryView().getSelectionModel().getSelectedIndex(),
						contr.getPlaylistView().getSelectionModel().getSelectedIndex());
			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void selectStrategy(String strat) throws RemoteException {
		Platform.runLater(() -> contr.selectStrategy(strat));

	}
}
