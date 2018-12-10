package datasourse.queryField;

import java.util.ArrayList;
import java.util.List;

import datasourse.type.FieldType;
import datasourse.type.LinkType;
/**
 * 查询字段基类
 * @param <E> 字段数据类型
 */
public class FieldQuery<E> {
	protected String fieldName;
	protected String sql;//查询条件
	protected List<E> value = new ArrayList<>();
	private LinkType linkType = LinkType.AND;
	private FieldType fieldType = FieldType.STRING;
	private boolean selected;//是否需要查询出来
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
//			StringBuilder s = new StringBuilder();
//			s.append(fieldName).append(sql).append(" ");
//			return s.toString();
			return sql;
		}else {
			return "";
		}
	}
	
	public void setSql(String sql) {
		this.value.clear();
		this.sql = sql;
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
		this.sql = fieldName + " = ? ";
	};
	
	public void setIsNot(E value) {
		setValue(value);
		this.sql = fieldName + " != ? ";
	};
	
	public void setIsNull() {
		this.value.clear();
		this.sql = fieldName + " is null "; 
	};
	
	
	public void setIsNotNull() {
		this.value.clear();
		this.sql = fieldName + " is not null "; 
	};
	
	public void setLike(E value) {
		setValue(value);
		this.sql = fieldName + " like ? "; 
	};
	
	public void setNotLike(E value) {
		setValue(value);
		this.sql = fieldName + " not like ? "; 
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
			builder.append(") ");
			this.sql = fieldName + builder.toString();
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
			builder.append(") ");
			this.sql = fieldName + builder.toString();
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
			builder.append(") ");
			this.sql = fieldName + builder.toString();
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
			builder.append(") ");
			this.sql = fieldName + builder.toString();
		}else {
			this.sql = "";
		}
	};
	
	
}
