package controllers;

import models.Account;
import models.Music;
import models.Room;
import models.SungMusic;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

public class MusicRegisterController extends Controller {

	/**
	 * POSTによる歌った曲登録
	 */
	public Result register() {
		long account_id = 0;
		long music_id = 0;
		try {
			DynamicForm param = new DynamicForm().bindFromRequest();
			String aid = param.get("account_id");
			if (aid == null)
				return badRequest();
			account_id = Long.parseLong(aid);

			String mid = param.get("music_id");
			if (mid == null)
				return badRequest();
			music_id = Long.parseLong(mid);
		} catch (NumberFormatException e) {
			return badRequest();
		}
		return registerGet(account_id, music_id);
	}

	/**
	 * GETによる歌った曲登録
	 */
	public Result registerGet(long account_id, long music_id) {

		Account ac = Account.find.byId(account_id);
		if (ac == null) return badRequest("Invalid account_id : " + account_id);
		
		Music music = Music.find.byId(music_id);
		if (music == null) return badRequest("Invalid music_id : " + music_id);

		// 現在のルームを確認する。現在ルームに属していない場合null
		Room room = null;
		if (ac.room_id != null && ac.room_id != 0l) {
			room = Room.find.byId(ac.room_id);
		}

		SungMusic sm = new SungMusic(0l, ac, room, music);
		try {
			sm.save();
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError();
		}

		return ok();
	}

}
