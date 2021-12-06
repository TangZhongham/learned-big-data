package datagenerator.common.columngenerator;

public abstract class AbstractColumnDataGen implements ColumnDataGen {
    private String columnName;

    public AbstractColumnDataGen(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnData() {
        return null;
    }

    public void init() {}

    public String getColumnName() {
        return this.columnName;
    }
}
