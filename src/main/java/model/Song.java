package model;

import javafx.beans.property.SimpleStringProperty;

public class Song implements interfaces.Song {

    private SimpleStringProperty path, title, album, interpret;
    private long id;



    public void setAlbum(String album) {
        this.album.set(album);
    }

    public String getAlbum(){
        return album.get();
    }


    public void setInterpret(String interpret) {
        this.interpret.set(interpret);
    }

    public String getInterpret() {
        return interpret.get();
    }


    public void setPath(String path) {
        this.path.set(path);
    }

    public String getPath() {
        return path.get();
    }


    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getTitle() {
        return title.get();
    }



    public SimpleStringProperty albumProperty() {
        return album;
    }

    public SimpleStringProperty interpretProperty() {
        return interpret;
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String toString(){
        return getTitle()+getAlbum()+getInterpret()+getPath();
    }


    //
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}