package controller;

import model.Model;
import model.Song;
import view.View;

public class Controller{
    private Model model;
    public void link(Model model, View view){
        this.model = model;
        view.setList(model.getAllPlaylist());
        view.addController(this);
    }
    public void add(Song s){
        model.getAllPlaylist().addSong(s);
    }

}