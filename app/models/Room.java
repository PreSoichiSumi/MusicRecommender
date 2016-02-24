package models;

import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Index;

@Entity
public class Room extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long room_id;
	
	@Index
	public String name;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="room", fetch=FetchType.LAZY)
	public List<SungMusic> sung;

	public Room(){}
	
	public Room(Long room_id, String name) {
		this.room_id = room_id;
		this.name = name;
	}
	
	public static Finder<Long, Room> find = new Finder<>(Room.class);
}
