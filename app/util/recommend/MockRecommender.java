package util.recommend;

import java.util.ArrayList;
import java.util.List;

import models.Account;
import models.Music;
import models.SungMusic;

public class MockRecommender implements MusicRecommender {

	@Override
	public List<Music> recommend(Account account) {
		List<SungMusic> list = SungMusic.find.fetch("account").
				fetch("music").fetch("room").where().eq("account_id", account.account_id).findList();
		ArrayList<Music> ans = new ArrayList<>(list.size());
		int idx = 0;
		for(SungMusic sm : list){
			ans.add(sm.music);
		}
		
		return ans;
		
	}

}
