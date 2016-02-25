package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Music;
import play.mvc.Controller;
import play.mvc.Result;
import util.gracenote.GracenoteMetadata;
import util.gracenote.GracenoteWebAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 楽曲情報検索API用Controller
 * @author sumi
 */
public class MusicSearchController extends Controller{
	private static String clientID  = "9474304-62797A30715D3B296EFB736240C6925E"; //GracenoteAPI用アプリケーションID
    private static String clientTag = "25991130737085962-F25002E58FCF3FB570826AC4E0100C2F";
    //曲名から楽曲を検索ためのメソッド(曲数指定をしない場合)
    public Result searchMusicFromMusicName(String musicName) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok(convertResToJson(api.searchTrack("","",musicName)));
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
	}
  //曲名から楽曲を検索ためのメソッド(曲数指定をする場合)
    public Result searchMusicFromMusicNameRanged(String musicName, Integer pageLength, Integer pageNumber) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok( convertResToJson( api.searchTrackRanged("","",musicName,pageLength,pageNumber) ) );
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
	}
    
    public Result searchMusicFromMusicNameRangedFast(String musicName, Integer pageLength, Integer pageNumber) {
    		List<Music> list = Music.find.where().
    		like("title", "%" + musicName + "%").
    		setFirstRow(Math.max(0, (pageNumber - 1)) * pageLength).
    		setMaxRows(pageLength).
    		findList();

        	ObjectMapper om = new ObjectMapper();
        	JsonNode node = om.valueToTree(list);
        	return ok(node);
    }
    
    //アーティスト名から楽曲を検索ためのメソッド(曲数指定をしない場合)
    public Result searchMusicFromArtistName(String artistName) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok(convertResToJson( api.searchTrack(artistName,"","")));
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
    }
    //アーティスト名から楽曲を検索ためのメソッド(曲数指定をする場合)
    public Result searchMusicFromArtistNameRanged(String artistName, Integer pageLength, Integer pageNumber) {
    	try{
	    	GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag);
	    	return ok( convertResToJson( api.searchTrackRanged(artistName,"","",pageLength,pageNumber) ) );
    	}catch(Exception e){
    		e.printStackTrace();
    		return internalServerError();
    	}
	}
    
    public Result searchMusicFromArtistNameRangedFast(String artistName, Integer pageLength, Integer pageNumber) {
		List<Music> list = Music.find.where().
		like("artist", "%" + artistName + "%").
		setFirstRow(Math.max(0, (pageNumber - 1)) * pageLength).
		setMaxRows(pageLength).
		findList();

    	ObjectMapper om = new ObjectMapper();
    	JsonNode node = om.valueToTree(list);
    	return ok(node);
    }
    
    //検索結果からJsonへ変換．変換時に検索結果をキャッシュする
	private JsonNode convertResToJson(GracenoteMetadata data){
		List<Music> res=new ArrayList<>();
    	for(Map<String, Object> o:data.getAlbums()){
    		res.add(getMusic((String)o.get("album_artist_name"), (String)o.get("track_title")));
    	}
    	ObjectMapper om = new ObjectMapper();
    	return om.valueToTree(res);
	}
	//楽曲情報を登録する
	private Music getMusic(String artist, String title){
		List<Music> list = Music.find.where().eq("title", title).eq("artist", artist).findList();
		if(list.size() >= 1){
			list.get(0).artist = artist;
			list.get(0).title = title;
			return list.get(0);
		}else{
			Music music = new Music(0l, artist, title);
			music.save();
			return music;
		}

	}
}
