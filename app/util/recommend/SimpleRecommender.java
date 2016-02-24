package util.recommend;

import java.util.List;

import models.Account;
import models.Music;

public class SimpleRecommender implements MusicRecommender {

	@Override
	public List<Music> recommend(Account account) {
		return account.sung;
	}

}
