package com.query;

import java.util.ArrayList;

public class bestAnswers {
	ArrayList<Answer> answers;
	String id;
	
	public bestAnswers(ArrayList<Answer> ans,String id) {
		answers = ans;
		this.id = id;
	}

	public ArrayList<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
