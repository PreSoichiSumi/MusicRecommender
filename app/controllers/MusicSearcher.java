package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.mvc.Controller;
import play.mvc.Result;
import util.GracenoteMetadata;
import util.GracenoteWebAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import models.Music;

public class MusicSearcher extends Controller{
	private static String clientID  = "9474304-62797A30715D3B296EFB736240C6925E"; // Put your clientID here.
    private static String clientTag = "25991130737085962-F25002E58FCF3FB570826AC4E0100C2F"; // Put your clientTag here.

    public Result searchMusicFromMusicName(String musicName) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok(convertResToJson(api.searchTrack("","",musicName)));
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
	}
    public Result searchMusicFromMusicNameRanged(String musicName, Integer pageLength, Integer pageNumber) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok( convertResToJson( api.searchTrackRanged("","",musicName,pageLength,pageNumber) ) );
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
	}

    public Result searchMusicFromArtistName(String artistName) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok(convertResToJson( api.searchTrack(artistName,"","")));
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
    }
    public Result searchMusicFromArtistNameRanged(String artistName, Integer pageLength, Integer pageNumber) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok( convertResToJson( api.searchTrackRanged(artistName,"","",pageLength,pageNumber) ) );
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
	}

	private JsonNode convertResToJson(GracenoteMetadata data){
		List<Music> res=new ArrayList<>();
    	for(Map<String, Object> o:data.getAlbums()){
    		res.add(getMusic((String)o.get("album_artist_name"), (String)o.get("track_title")));
    	}
    	ObjectMapper om = new ObjectMapper();
    	return om.valueToTree(res);
	}
	
	private Music getMusic(String artist, String title){
		List<Music> list = Music.find.where().eq("title", title).eq("artist", artist).findList();
		if(list.size() >= 1){
			return list.get(0);
		}else{
			Music music = new Music(0l, artist, title);
			music.save();
			return music;
		}
		
	}
}
