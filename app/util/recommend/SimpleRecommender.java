package util.recommend;

import java.util.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import models.*;

/**
 * 同じルームにいる人の歌ったことのある曲・聞いたことのある曲に基づいて推薦曲を決定するRecommender
 * score = WEIGHT_OWN * (a * WEIGHT_SUNG * b / c + WEIGHT_LISTEND * d / c)
 * a : 自分が歌ったことのある曲の場合1、そうでなければ0
 * b : 現在のルームの中でその曲を歌ったことのある人数
 * c : 現在のルームの人数
 * d : 現在のルームの中でその曲を聴いたことがある人数(歌った、もしくは過去のカラオケで同じルームの人が歌った)
 */
public class SimpleRecommender implements MusicRecommender {
	private static final double WEIGHT_SUNG = 0.7;
	private static final double WEIGHT_LISTEND = 1 - WEIGHT_SUNG;
	private static final double WEIGHT_OWN = 1.0;
	
	/** 推薦する曲の最大数 */
	private static final int MAX_RESULT = 20;
	
	private static final String sql = 
			"SELECT b.account_account_id, b.room_room_id, b.music_music_id, m.artist, m.title " +
			"FROM sung_music b, music m " +
			"WHERE b.music_music_id = m.music_id AND b.room_room_id IN ( " +
				"SELECT c.room_room_id " +
				"FROM sung_music c " +
				"WHERE c.account_account_id IN (%s) " +
			")";
	
	@Override
	public List<Music> recommend(Account account) {
		List<Account> accs = Account.find.where().eq("room_id", account.room_id).findList();
		Set<Long> accountIdSet = new HashSet<>(accs.size());
		
		for(Account a : accs){
			accountIdSet.add(a.account_id);
		}
		
		List<SqlRow> resultRows = executeSQL(accountIdSet);

		List<MusicScore> scores = calculateScore(resultRows, accountIdSet, account);
		Collections.sort(scores);
		
		ArrayList<Music> returnMusic = new ArrayList<>();
		int idx = 0;
		for(MusicScore ms : scores){
			returnMusic.add(ms.music);
			System.out.print("(" + ms.getScore() + "," + ms.mysing + "," + ms.singers.size() + "," + ms.listeners.size()+")");
			idx++;
			//if(idx >= MAX_RESULT) break;
		}
		System.out.println();
		
		return returnMusic;
	}
	
	
	private List<SqlRow> executeSQL(Set<Long> accountIdSet){
		if(accountIdSet.size() == 0){
			return Collections.emptyList();
		}
	
		StringBuilder idStr = new StringBuilder();
		Iterator<Long> ite = accountIdSet.iterator();
		idStr.append(ite.next());
		while(ite.hasNext()){
			idStr.append(",");
			idStr.append(ite.next());
		}

		return  Ebean.createSqlQuery(String.format(sql, idStr.toString())).findList();
	}		
	
	private Map<Long, ? extends Set<Long>> createAidToRidMap(List<SqlRow> resultRows, Set<Long> accountIdSet){
		HashMap<Long, HashSet<Long>> aidToRid = new HashMap<>();
		for(Long aid : accountIdSet){
			aidToRid.put(aid, new HashSet<>());
		}
		
		for(SqlRow row : resultRows){
			Long rid = row.getLong("room_room_id");
			Long aid = row.getLong("account_account_id");
			if(accountIdSet.contains(aid)){
				aidToRid.get(aid).add(rid);
			}
		}
		return aidToRid;
	}
	
	private List<MusicScore> calculateScore(List<SqlRow> resultRows, Set<Long> accountIdSet, Account myAccount){
		HashMap<Long, MusicScore> midToMScore = new HashMap<>();
		Map<Long, ? extends Set<Long>> aidToRid = createAidToRidMap(resultRows, accountIdSet);
		
		for(SqlRow row : resultRows){
			Long mid = row.getLong("music_music_id");
			Long rid = row.getLong("room_room_id");
			Long aid = row.getLong("account_account_id");
			String artist = row.getString("artist");
			String title = row.getString("title");
			
			MusicScore mscore = null;
			if(!midToMScore.containsKey(mid)){
				mscore = new MusicScore(new Music(mid, artist, title), accountIdSet.size());
				midToMScore.put(mid, mscore);
			}
				
			mscore = midToMScore.get(mid);
			
			if(aid == myAccount.account_id){
				mscore.mysing++;
			}
			if(accountIdSet.contains(aid)){
				mscore.singers.add(aid);
			}
			
			for(Long aaid : accountIdSet){
				if(aidToRid.get(aaid).contains(rid)){
					mscore.listeners.add(aaid);
				}
			}
		}
		return new ArrayList<>(midToMScore.values());
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
				ret = WEIGHT_OWN;
			}
			ret *= (singers.size() * WEIGHT_SUNG / roomsize) + 
					(listeners.size() * WEIGHT_LISTEND / roomsize);
			return ret;
		}
		
		@Override
		public int compareTo(MusicScore arg0) {
			return Double.compare(arg0.getScore(), getScore());
		}
	}
}

