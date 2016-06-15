package qa.qcri.aidr.collector.beans;


import facebook4j.Event;
import facebook4j.Group;
import facebook4j.Page;
import facebook4j.Post;

public enum FacebookEntityType {

	PAGE(Page.class),
	EVENT(Event.class),
	GROUP(Group.class),
	POST(Post.class);
	
	private Class clazz;

	private FacebookEntityType(Class clazz) {
		this.setClazz(clazz);
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
