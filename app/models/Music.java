package models;

import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Music extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long music_id;
	
	public String artist;
	
	public String title;
	
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="sung", fetch=FetchType.EAGER)
	@JsonBackReference
	public List<Account> account;

	
	public Music(){}
	
	public Music(Long music_id, String artist, String title) {
		this.music_id = music_id;
		this.artist = artist;
		this.title = title;
	}
	
	public static Finder<Long, Music> find = new Finder<>(Music.class);
}
