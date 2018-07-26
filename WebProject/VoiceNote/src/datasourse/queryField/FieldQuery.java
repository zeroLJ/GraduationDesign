package datasourse.queryField;

import java.util.ArrayList;
import java.util.List;

import datasourse.type.FieldType;
import datasourse.type.LinkType;
/**
 * ��ѯ�ֶλ���
 * @param <E> �ֶ���������
 */
public class FieldQuery<E> {
	protected String fieldName;
	protected String sql;//��ѯ����
	protected List<E> value = new ArrayList<>();
	private LinkType linkType = LinkType.AND;
	private FieldType fieldType = FieldType.STRING;
	private boolean selected;
	public void outField() {
		selected = true;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
	public FieldType getFieldType() {
		return fieldType;
	}
	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}
	public LinkType getLinkType() {
		return linkType;
	}
	
	
	public String getFieldName() {
		return fieldName;
	}
	public FieldQuery(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getSQL() {
		if (sql!=null && !sql.equals("")) {
			StringBuilder s = new StringBuilder();
			s.append(fieldName).append(sql).append(" ");
			return s.toString();
		}else {
			return "";
		}
	}
	
	public List<E> getValue() {
		return value;
	}
	
	private void setValue(List<E> value) {
		this.value.clear();
		this.value.addAll(value);
	}
	private void setValue(E value) {
		this.value.clear();
		this.value.add(value);
	}
	
	private void setValue(E[] value) {
		this.value.clear();
		for(E e : value) {
			this.value.add(e);
		}
	}
	
	public void setIs(E value) {
		setValue(value);
		this.sql = " = ?";
	};
	
	public void setIsNot(E value) {
		setValue(value);
		this.sql = " != ?";
	};
	
	public void setIsNull() {
		this.value.clear();
		this.sql = " is null"; 
	};
	
	
	public void setIsNotNull() {
		this.value.clear();
		this.sql = " is not null"; 
	};
	
	public void setLike(E value) {
		setValue(value);
		this.sql = " like ?"; 
	};
	
	public void setNotLike(E value) {
		setValue(value);
		this.sql = " not like ?"; 
	};
	
//	public void setLikeStart(E value) {
//		setValue(value);
//		this.sql = " like ?"; 
//	};
//	
//	public void setNotLikeStart(E value) {
//		setValue(value);
//		this.sql = " not like ?"; 
//	};
//	
//	public void setLikeEnd(E value) {
//		setValue(value);
//		this.sql = " like ?"; 
//	};
//	
//	public void setNotLikeEnd(E value) {
//		setValue(value);
//		this.sql = " not like ?'"; 
//	};
	
	public void setIn(E[] value) {
		setValue(value);
		if (this.value.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(" in (");
			for(int i = 0; i < this.value.size(); i++) {
				builder.append("?,");
			}
			builder.deleteCharAt(builder.length()-1);
			builder.append(")");
			this.sql = builder.toString();
		}else {
			this.sql = "";
		}
	};
	
	public void setIn(List<E> value) {
		setValue(value);
		if (this.value.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(" in (");
			for(int i = 0; i < this.value.size(); i++) {
				builder.append("?,");
			}
			builder.deleteCharAt(builder.length()-1);
			builder.append(")");
			this.sql = builder.toString();
		}else {
			this.sql = "";
		}
	};
	
	public void setNotIn(E[] value) {
		setValue(value);
		if (this.value.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(" not in (");
			for(int i = 0; i < this.value.size(); i++) {
				builder.append("?,");
			}
			builder.deleteCharAt(builder.length()-1);
			builder.append(")");
			this.sql = builder.toString();
		}else {
			this.sql = "";
		}
	};
	
	public void setNotIn(List<E> value) {
		setValue(value);
		if (this.value.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(" not in (");
			for(int i = 0; i < this.value.size(); i++) {
				builder.append("?,");
			}
			builder.deleteCharAt(builder.length()-1);
			builder.append(")");
			this.sql = builder.toString();
		}else {
			this.sql = "";
		}
	};
	
	
}
