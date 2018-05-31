package demo;
import java.util.Date;

   /**
    * note  µÃÂ¿‡
    * Mon Apr 02 16:14:41 CST 2018 ljl
    */ 


public class Note{
	private String name;
	private String title;
	private String message;
	private Date addTime;
	private Date editTime;
	private String audioPath;
	public void setName(String name){
	this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setTitle(String title){
	this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setMessage(String message){
	this.message=message;
	}
	public String getMessage(){
		return message;
	}
	public void setAddTime(Date addTime){
	this.addTime=addTime;
	}
	public Date getAddTime(){
		return addTime;
	}
	public void setEditTime(Date editTime){
	this.editTime=editTime;
	}
	public Date getEditTime(){
		return editTime;
	}
	public void setAudioPath(String audioPath){
	this.audioPath=audioPath;
	}
	public String getAudioPath(){
		return audioPath;
	}
}

