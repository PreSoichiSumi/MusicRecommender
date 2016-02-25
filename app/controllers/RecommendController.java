package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Account;
import play.mvc.Controller;
import play.mvc.Result;
import util.recommend.MusicRecommender;
import util.recommend.SimpleRecommender;
import util.recommend.MockRecommender;

public class RecommendController extends Controller { 
    public MusicRecommender recommender = new SimpleRecommender();
	
	public Result recommend(Long id) {
    	Account user = Account.find.byId(id);
    	if(user == null){
    		return notFound();
    	}
    	ObjectMapper om = new ObjectMapper();
    	JsonNode n = om.valueToTree(recommender.recommend(user));
    	
        return ok(n);
    }
}
