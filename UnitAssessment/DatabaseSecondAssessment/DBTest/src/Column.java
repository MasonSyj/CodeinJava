import java.util.ArrayList;
import java.util.List;

public class Column<T> {
	private String columnName;
	private List<T> columnBody;

	public Column(String columnName){
		this.columnName = columnName;
		this.columnBody = new ArrayList<T>();
	}

	public List<T> getColumnBody() {
		return columnBody;
	}

	public String getColumnName() {
		return columnName;
	}

	public void addValue(T value){
		columnBody.add(value);
	}
}
