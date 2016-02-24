package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Index;

@Entity
public class NowRoom  extends Model{

	@Index
	public Long room_id;

	@Id
	public String room_name;

	public NowRoom(){}

	public NowRoom(Long room_id, String roomName){
		this.room_id=room_id;
		this.room_name=roomName;
	}

	public static Finder<Long, NowRoom> find = new Finder<>(NowRoom.class);
}
