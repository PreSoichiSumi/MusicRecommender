package controllers;

import models.Account;
import models.NowRoom;
import models.Room;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.ExpressionList;

public class RoomInOut extends Controller{
	public Result in(Long account_id, String room_name){
		Account account = Account.find.byId(account_id);
		if(account.room_id != 0){
			leaveRoom(account);
		}
		return ok();
	}
	
	public Result out(Long account_id){
		
		return ok();
	}
	
	public int leaveRoom(Account account){
		if(account.room_id == 0){
			return -1;
		}
		
		Long rid = account.room_id;
		account.room_id = 0l;
		account.update();
		
		/*
		if(Account.find.where().eq("room_id", rid).findRowCount() == 0){
			NowRoom.find.where().eq("room_id", rid).
		}
		*/
		return 0;
	}
	
}
