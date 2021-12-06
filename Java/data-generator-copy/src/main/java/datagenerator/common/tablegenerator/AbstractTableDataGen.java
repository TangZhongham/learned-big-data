package datagenerator.common.tablegenerator;

public class AbstractTableDataGen implements TableDataGen {
    private String tableName;

    public AbstractTableDataGen(String tableName) {
        this.tableName = tableName;
    }

    public void init() {}

    public String nextRecord() {
        return null;
    }

    public String getTableName() {
        return this.tableName;
    }
}

