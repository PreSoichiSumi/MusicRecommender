package models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class SungMusic extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long sung_id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	public Account account;
	
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
