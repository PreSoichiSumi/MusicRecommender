package util.recommend;

import java.util.*;

import javax.persistence.Entity;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Sql;

import models.Account;
import models.Music;
import models.SungMusic;

public class SimpleRecommender implements MusicRecommender {

	@Override
	public List<Music> recommend(Account account) {
		List<Account> accs = Account.find.where().eq("room_id", account.room_id).findList();
		Set<Long> idlist = new HashSet<>(accs.size());
		
		for(Account a : accs){
			System.out.println(a.account_id);
			idlist.add(a.account_id);
		}
		if(idlist.size() == 0){
			return Collections.emptyList();
		}
	
		StringBuilder ids = new StringBuilder();
		Iterator<Long> ite = idlist.iterator();
		ids.append(ite.next());
		while(ite.hasNext()){
			ids.append(",");
			ids.append(ite.next());
		}
		
		String sql = "SELECT b.account_account_id, b.room_room_id, b.music_music_id, m.artist, m.title " +
					 "FROM sung_music b, music m " +
					 "WHERE b.music_music_id = m.music_id AND b.room_room_id in (" +
					 	"SELECT c.room_room_id " +
					 	"FROM sung_music c " +
					 	"WHERE c.account_account_id in (" + ids +
					 ")) ";

		/*
		RawSql rawSql = RawSqlBuilder
		        .parse(sql)
		        .create();
		*/

		HashMap<Long, MusicScore> map = new HashMap<>();
		List<SqlRow> list = Ebean.createSqlQuery(sql).findList();
		HashMap<Long, HashSet<Long>> aidToRid = new HashMap<>();
		for(Long aid : idlist){
			aidToRid.put(aid, new HashSet<>());
		}
		
		ArrayList<MusicScore> ans = new ArrayList<>();
		for(SqlRow row : list){
			Long mid = row.getLong("music_music_id");
			Long rid = row.getLong("room_room_id");
			Long aid = row.getLong("account_account_id");
			String artist = row.getString("artist");
			String title = row.getString("title");
			if(idlist.contains(aid)){
				aidToRid.get(aid).add(rid);
			}
		}
			
		for(SqlRow row : list){
			Long mid = row.getLong("music_music_id");
			Long rid = row.getLong("room_room_id");
			Long aid = row.getLong("account_account_id");
			String artist = row.getString("artist");
			String title = row.getString("title");
			
			MusicScore mscore = null;
			if(map.containsKey(mid)){
				mscore = map.get(mid);
		
			}else{
				mscore = new MusicScore(new Music(mid, artist, title), idlist.size());
				map.put(mid, mscore);
				ans.add(mscore);
			}
			
			if(aid == account.account_id){
				mscore.mysing++;
			}
			if(idlist.contains(aid)){
				mscore.singers.add(aid);
			}
			
			for(Long aaid : idlist){
				if(aidToRid.get(aaid).contains(rid)){
					mscore.listeners.add(aaid);
				}
			}
		}
		Collections.sort(ans);
		
		ArrayList<Music> ret = new ArrayList<>();
		int idx = 0;
		for(MusicScore ms : ans){
			System.out.println(ms.music.artist + " " + ms.music.title + " " + ms.getScore());
			ret.add(ms.music);
			idx++;
			if(idx >= 20) break;
		}
		
		return ret;
	}
	
	class MusicScore implements Comparable<MusicScore>{
		Music music;
		int mysing;
		HashSet<Long> singers = new HashSet<>();
		HashSet<Long> listeners = new HashSet<>();
		int roomsize;
		
		public MusicScore(Music music, int roomsize){
			this.music = music;
			this.roomsize = roomsize;
		}
		
		public double getScore(){
			double ret = 0;
			if(mysing >= 1){
				ret = 0.5;
			}
			ret += singers.size() * 0.3 / roomsize;
			ret += listeners.size() * 0.2 / roomsize;
			return ret;
		}
		
		@Override
		public int compareTo(MusicScore arg0) {
			return Double.compare(arg0.getScore(), getScore());
		}
	}
}

