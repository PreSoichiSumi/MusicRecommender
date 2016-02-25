package util.recommend;

import javax.persistence.Entity;

import com.avaje.ebean.annotation.Sql;

@Entity  
@Sql  
public class Record{
	public Long account_id;
	public Long room_id;
	public Long music_id;
	public String artist;
	public String title;
}
