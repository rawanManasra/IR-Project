package com.query;

import java.util.ArrayList;

public class bestAnswers {
	String id;
	
	ArrayList<Answer> answers;

	public bestAnswers(String id,ArrayList<Answer> ans) {
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
