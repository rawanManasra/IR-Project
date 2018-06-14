package com.query;

public class Query {
	public Query(String id, String question) {
		this.id = id;
		String[] splitQuery = question.split("\\?");
		for(String s:splitQuery) {
			this.question+= s;
		}
	}
	String id;
	String question = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Boolean equal(Query q) {
		return this.id.equals(q.getId());
	}

	@Override
	public String toString() {
		return "id: " + id + "\n" +"Question: " + question;
	}

}
