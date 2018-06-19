package com.query;

public class Answer {
	String answer;
	String score;
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public Answer(String ans,double d){
		answer = ans;
		this.score = String.valueOf(d);
	}
}
