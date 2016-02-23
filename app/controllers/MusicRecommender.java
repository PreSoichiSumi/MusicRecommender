package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Account;
import play.mvc.Controller;
import play.mvc.Result;

public class MusicRecommender extends Controller { 
    public Result recommend(Long id) {
    	Account user = Account.find.byId(id);
    	if(user == null){
    		return notFound();
    	}
    	ObjectMapper om = new ObjectMapper();
    	JsonNode n = om.valueToTree(om);
    	
        return ok(n);
    }

}
