package models;

import javax.persistence.*;

import com.avaje.ebean.annotation.Index;
import com.avaje.ebean.Model;

@Entity
public class SungMusic extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long sung_id;
	
	@Index
	@ManyToOne(fetch=FetchType.EAGER)
	public Account account;
	
	@Index
	@ManyToOne(fetch=FetchType.EAGER)
	public Room room;
	
	@ManyToOne(fetch=FetchType.EAGER)
	public Music music;

	public SungMusic(){}
	
	public SungMusic(Long sung_id, Account account, Room room, Music music) {
		this.sung_id = sung_id;
		this.account = account;
		this.room = room;
		this.music = music;
	}


	public static Finder<Long, SungMusic> find = new Finder<>(SungMusic.class);	
}
