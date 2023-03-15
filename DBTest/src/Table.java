import java.util.ArrayList;
import java.util.List;

public class Table {
	private String tableName;
	private List<String> attributesName;
	private List<Column> columns;

	private int numofAttributes;
	private int numofItems;

	public Table(String tableName){
		this.tableName = tableName;
		attributesName = new ArrayList<String>();
		columns = new ArrayList<Column>();
		numofAttributes = 0;
		numofItems = 0;
	}

	public String getTableName() {
		return tableName;
	}

	public List<String> getAttributesName(){
		return attributesName;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public Column getColumnbyNames(String name) {
		for (Column col: columns){
			if (col.getColumnName().equals(name)){
				return col;
			}
		}
		return null;
	}

	public void addNewColumn(Column col){
		attributesName.add(col.getColumnName());
		columns.add(col);
		numofAttributes++;
	}

	public void addColumns(List<String> attributes){
		for (String attribute: attributes){
			addNewColumn(new Column<>(attribute));
		}
	}

	public void printTable(){
		int row = columns.get(0).getColumnBody().size();
		int col = columns.size();
		for (String attribute: attributesName){
			System.out.print(attribute + "\t");
		}
		System.out.println();

		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
				System.out.print(columns.get(j).getColumnBody().get(i) + "\t");
			}
			System.out.println();
		}
	}

	public void addValue(List value) {
		numofItems++;

		if (value.size() != this.numofAttributes){
			return;
		}

		for (int j = 0; j < numofAttributes; j++){
			columns.get(j).addValue(value.get(j));
		}
	}
}
