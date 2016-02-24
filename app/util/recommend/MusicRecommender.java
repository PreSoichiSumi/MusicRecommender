package util.recommend;

import java.util.List;

import models.Account;
import models.Music;

public interface MusicRecommender {
	public List<Music> recommend(Account accout);
}
