
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import play.libs.Json;
import play.libs.Yaml;
import play.mvc.Result;
import play.test.Helpers;

import static play.test.Helpers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.Music;
import models.Room;
import models.SungMusic;
import models.Account;

public class DatabaseTest {

	private Account u1;
	private Account u2;
	private Music music1;
	private Music music2;
	private Music music3;
	private Music music4;
	private Music music5;
	private Room room1;
	private Room room2;
	private SungMusic sung1;
	private SungMusic sung2;
	private SungMusic sung3;
	private SungMusic sung4;
	
	private ArrayList<Music> l1;
	private ArrayList<Music> l2;

	@Before
	public void setUp() throws Exception {
		start(fakeApplication(inMemoryDatabase()));

		music1 = new Music(1l,"A","a");
		music1.save();
		music2 = new Music(2l,"B","b");
		music2.save();
		music3 = new Music(3l,"C","c");
		music3.save();
		music4 = new Music(4l,"D","d");
		music4.save();
		music5 = new Music(5l,"E","e");
		music5.save();

		
		u1 = new Account(1l, "Alice");
		u2 = new Account(2l, "Bob");
		u1.save();
		u2.save();
		
		room1 = new Room(1l, "ROOM1");
		room2 = new Room(2l, "ROOM2");
		room1.save();
		room2.save();

		sung1 = new SungMusic(1l, u1, room1, music1);
		sung2 = new SungMusic(2l, u1, room1, music2);
		sung3 = new SungMusic(3l, u1, room2, music3);
		sung4 = new SungMusic(4l, u2, room2, music1);
		sung1.save();
		sung2.save();
		sung3.save();
		sung4.save();
		//Ebean.save(list);
	}

	@Test
	public void test() {
		List<SungMusic> list = SungMusic.find.fetch("account").fetch("music").fetch("room").where("account_id=1").findList();

		assertThat("1", list.get(0).account.account_id, is(u1.account_id));
		assertThat("2", list.get(0).room.room_id, is(room1.room_id));
		assertThat("3", list.get(0).music.music_id, is(music1.music_id));
		
		assertThat("4", list.get(1).account.account_id, is(u1.account_id));
		assertThat("5", list.get(1).room.room_id, is(room1.room_id));
		assertThat("6", list.get(1).music.music_id, is(music2.music_id));
		
		assertThat("7", list.get(2).account.account_id, is(u1.account_id));
		assertThat("8", list.get(2).room.room_id, is(room2.room_id));
		assertThat("9", list.get(2).music.music_id, is(music3.music_id));
	}

}
