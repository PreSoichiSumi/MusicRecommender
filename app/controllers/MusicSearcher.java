package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.mvc.Controller;
import play.mvc.Result;
import util.GracenoteMetadata;
import util.GracenoteWebAPI;

import com.google.gson.Gson;

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

	private String convertResToJson(GracenoteMetadata data){
		List<Map<String,String>> res=new ArrayList<>();
    	for(Map<String, Object> o:data.getAlbums()){
    		Map<String,String> tmp=new HashMap<>();
    		tmp.put("music_name", (String)o.get("track_title"));
    		tmp.put("artist_name", (String)o.get("album_artist_name"));
    		res.add(tmp);
    	}
    	return new Gson().toJson(res);
	}
}
