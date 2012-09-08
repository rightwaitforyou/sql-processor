package org.sqlproc.dsl.ui.templates;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sqlproc.dsl.processorDsl.Artifacts;
import org.sqlproc.dsl.property.ModelProperty;
import org.sqlproc.dsl.property.PojoAttrType;
import org.sqlproc.dsl.property.PojoAttribute;
import org.sqlproc.dsl.resolver.DbColumn;
import org.sqlproc.dsl.resolver.DbExport;
import org.sqlproc.dsl.resolver.DbImport;

public class TablePojoConverter {

    public enum PrimitiveType {
        BOOLEAN, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, BYTE_ARRAY, CHAR_ARRAY;

        public String getName() {
            if (this == BYTE_ARRAY)
                return "byte []";
            if (this == CHAR_ARRAY)
                return "char []";
            return name().toLowerCase();
        }
    }

    private Map<String, PojoAttrType> sqlTypes = new HashMap<String, PojoAttrType>();
    private Map<String, Map<String, PojoAttrType>> tableTypes = new HashMap<String, Map<String, PojoAttrType>>();
    private Map<String, Map<String, PojoAttrType>> columnTypes = new HashMap<String, Map<String, PojoAttrType>>();
    private Map<String, String> tableNames = new HashMap<String, String>();
    private Map<String, Map<String, String>> columnNames = new HashMap<String, Map<String, String>>();
    private Set<String> ignoreTables = new HashSet<String>();
    private Map<String, Set<String>> ignoreColumns = new HashMap<String, Set<String>>();
    private Map<String, Map<String, Map<String, String>>> ignoreExports = new HashMap<String, Map<String, Map<String, String>>>();
    private Map<String, Map<String, Map<String, String>>> ignoreImports = new HashMap<String, Map<String, Map<String, String>>>();

    private Map<String, Map<String, PojoAttribute>> pojos = new HashMap<String, Map<String, PojoAttribute>>();

    public TablePojoConverter() {
    }

    public TablePojoConverter(ModelProperty modelProperty, Artifacts artifacts) {

        Map<String, PojoAttrType> sqlTypes = modelProperty.getSqlTypes(artifacts);
        if (sqlTypes != null) {
            for (PojoAttrType sqlType : sqlTypes.values()) {
                this.sqlTypes.put(sqlType.getName() + sqlType.getSize(), sqlType);
            }
        }
        Map<String, Map<String, PojoAttrType>> tableTypes = modelProperty.getTableTypes(artifacts);
        if (tableTypes != null) {
            for (Map.Entry<String, Map<String, PojoAttrType>> tableType : tableTypes.entrySet()) {
                String table = tableType.getKey(); // tableToCamelCase(tableType.getKey());
                if (!this.tableTypes.containsKey(table))
                    this.tableTypes.put(table, new HashMap<String, PojoAttrType>());
                for (PojoAttrType sqlType : tableType.getValue().values()) {
                    this.tableTypes.get(table).put(sqlType.getName() + sqlType.getSize(), sqlType);
                }
            }
        }
        Map<String, Map<String, PojoAttrType>> columnTypes = modelProperty.getColumnTypes(artifacts);
        if (columnTypes != null) {
            for (Map.Entry<String, Map<String, PojoAttrType>> columnType : columnTypes.entrySet()) {
                String table = columnType.getKey(); // tableToCamelCase(columnType.getKey());
                if (!this.columnTypes.containsKey(table))
                    this.columnTypes.put(table, new HashMap<String, PojoAttrType>());
                for (PojoAttrType sqlType : columnType.getValue().values()) {
                    this.columnTypes.get(table).put(sqlType.getName(), sqlType);
                }
            }
        }
        Map<String, String> tableNames = modelProperty.getTableNames(artifacts);
        if (tableNames != null) {
            this.tableNames.putAll(tableNames);
        }
        Map<String, Map<String, String>> columnNames = modelProperty.getColumnNames(artifacts);
        if (columnNames != null) {
            for (Map.Entry<String, Map<String, String>> columnName : columnNames.entrySet()) {
                String table = columnName.getKey(); // tableToCamelCase(columnName.getKey());
                if (!this.columnNames.containsKey(table))
                    this.columnNames.put(table, new HashMap<String, String>());
                this.columnNames.get(table).putAll(columnName.getValue());
            }
        }
        Set<String> ignoreTables = modelProperty.getIgnoreTables(artifacts);
        if (ignoreTables != null) {
            this.ignoreTables.addAll(ignoreTables);
        }
        Map<String, Set<String>> ignoreColumns = modelProperty.getIgnoreColumns(artifacts);
        if (ignoreColumns != null) {
            for (Map.Entry<String, Set<String>> ignoreColumn : ignoreColumns.entrySet()) {
                this.ignoreColumns.put(ignoreColumn.getKey(), new HashSet<String>());
                this.ignoreColumns.get(ignoreColumn.getKey()).addAll(ignoreColumn.getValue());
            }
        }
        Map<String, Map<String, Map<String, String>>> ignoreExports = modelProperty.getIgnoreExports(artifacts);
        if (ignoreExports != null) {
            for (Map.Entry<String, Map<String, Map<String, String>>> ignoreExport : ignoreExports.entrySet()) {
                String table = ignoreExport.getKey(); // tableToCamelCase(columnName.getKey());
                if (!this.ignoreExports.containsKey(table))
                    this.ignoreExports.put(table, new HashMap<String, Map<String, String>>());
                this.ignoreExports.get(table).putAll(ignoreExport.getValue());
            }
        }
        Map<String, Map<String, Map<String, String>>> ignoreImports = modelProperty.getIgnoreImports(artifacts);
        if (ignoreImports != null) {
            for (Map.Entry<String, Map<String, Map<String, String>>> ignoreImport : ignoreImports.entrySet()) {
                String table = ignoreImport.getKey(); // tableToCamelCase(columnName.getKey());
                if (!this.ignoreImports.containsKey(table))
                    this.ignoreImports.put(table, new HashMap<String, Map<String, String>>());
                this.ignoreImports.get(table).putAll(ignoreImport.getValue());
            }
        }

        System.out.println("sqlTypes " + this.sqlTypes);
        System.out.println("tableTypes " + this.tableTypes);
        System.out.println("columnTypes " + this.columnTypes);
        System.out.println("tableNames " + this.tableNames);
        System.out.println("columnNames " + this.columnNames);
        System.out.println("ignoreTables " + this.ignoreTables);
        System.out.println("ignoreColumns " + this.ignoreColumns);
        System.out.println("ignoreExports " + this.ignoreExports);
        System.out.println("ignoreImports " + this.ignoreImports);
    }

    public void addTableTefinition(String table, List<DbColumn> dbColumns, List<DbExport> dbExports,
            List<DbImport> dbImports) {
        if (table == null || dbColumns == null)
            return;
        Map<String, PojoAttribute> attributes = new LinkedHashMap<String, PojoAttribute>();
        for (DbColumn dbColumn : dbColumns) {
            PojoAttribute attribute = convertDbColumnDefinition(table, dbColumn);
            if (attribute != null) {
                attributes.put(dbColumn.getName(), attribute);
            } else {
                attribute = convertDbColumnDefault(table, dbColumn);
                if (attribute != null)
                    attributes.put(dbColumn.getName(), attribute);
            }
        }
        pojos.put(table, attributes);
        for (DbImport dbImport : dbImports) {
            if (ignoreImports.containsKey(table) && ignoreImports.get(table).containsKey(dbImport.getFkColumn())
                    && ignoreImports.get(table).get(dbImport.getFkColumn()).containsKey(dbImport.getPkTable()))
                continue;
            PojoAttribute attribute = attributes.get(dbImport.getFkColumn());
            attribute.setPkTable(dbImport.getPkTable());
        }
        for (DbExport dbExport : dbExports) {
            if (ignoreExports.containsKey(table) && ignoreExports.get(table).containsKey(dbExport.getPkColumn())
                    && ignoreExports.get(table).get(dbExport.getPkColumn()).containsKey(dbExport.getFkTable()))
                continue;
            PojoAttribute attribute = attributes.get(dbExport.getPkColumn());
            attribute.getFkTables().add(dbExport.getFkTable());
        }
    }

    public void resolveReferencesOnKeys() {
        for (String pojo : pojos.keySet()) {
            Map<String, PojoAttribute> newAttributes = new HashMap<String, PojoAttribute>();
            for (PojoAttribute attribute : pojos.get(pojo).values()) {
                if (attribute.getPkTable() != null) {
                    if (pojos.containsKey(attribute.getPkTable())) {
                        attribute.setDependencyClassName(tableToCamelCase(attribute.getPkTable()));
                        if (attribute.getName().length() >= 3) {
                            if (attribute.getName().startsWith("id")) {
                                attribute.setName(lowerFirstChar(attribute.getName().substring(2)));
                            } else if (attribute.getName().endsWith("Id")) {
                                attribute.setName(attribute.getName().substring(0, attribute.getName().length() - 2));
                            }
                        }
                    }
                }
                for (String fkTable : attribute.getFkTables()) {
                    if (pojos.containsKey(fkTable)) {
                        String referName = lowerFirstChar(tableToCamelCase(fkTable));
                        if (!referName.endsWith("s")) {
                            if (referName.endsWith("y")) {
                                referName = referName.substring(0, referName.length() - 1);
                                referName += "ies";
                            } else {
                                referName += "s";
                            }
                        }
                        PojoAttribute attrib = new PojoAttribute();
                        attrib.setName(referName);
                        attrib.setClassName("java.util.List <:" + tableToCamelCase(fkTable) + ">");
                        newAttributes.put(attrib.getName(), attrib);
                    }
                }
            }
            if (!newAttributes.isEmpty())
                pojos.get(pojo).putAll(newAttributes);
        }
    }

    private String lowerFirstChar(String s) {
        if (s == null)
            return null;
        String ss = s.substring(0, 1).toLowerCase();
        if (s.length() == 1)
            return ss;
        ss += s.substring(1);
        return ss;
    }

    public void resolveReferencesOnConvention() {
        for (String pojo : pojos.keySet()) {
            for (PojoAttribute attribute : pojos.get(pojo).values()) {
                if (attribute.getName().startsWith("id") && attribute.getName().length() > 2) {
                    String className = attribute.getName().substring(2);
                    if (pojo.equals(className))
                        continue;
                    Map<String, PojoAttribute> referPojoAttr = pojos.get(className);
                    if (referPojoAttr != null) {
                        String name = className.substring(0, 1).toLowerCase();
                        if (className.length() > 1)
                            name += className.substring(1);
                        attribute.setName(name);
                        attribute.setPrimitive(false);
                        attribute.setDependencyClassName(className);
                        // reverse dependency
                        String referName = pojo.substring(0, 1).toLowerCase();
                        if (className.length() > 1)
                            referName += pojo.substring(1);
                        PojoAttribute attrib = new PojoAttribute();
                        attrib.setName(referName + "s");
                        attrib.setClassName("java.util.List <:" + pojo + ">");
                        referPojoAttr.put(attrib.getName(), attrib);
                    }
                }
            }
        }
    }

    public String getPojoDefinitions() {
        // System.out.println("PPP " + pojos);
        StringBuilder buffer = new StringBuilder();
        for (String pojo : pojos.keySet()) {
            if (ignoreTables.contains(pojo))
                continue;
            String pojoName = tableNames.get(pojo);
            if (pojoName == null)
                pojoName = pojo;
            buffer.append("\n  pojo ").append(tableToCamelCase(pojoName)).append(" {");
            for (Map.Entry<String, PojoAttribute> pentry : pojos.get(pojo).entrySet()) {
                if (ignoreColumns.containsKey(pojo) && ignoreColumns.get(pojo).contains(pentry.getKey()))
                    continue;
                PojoAttribute attribute = pentry.getValue();
                String name = (columnNames.containsKey(pojo)) ? columnNames.get(pojo).get(pentry.getKey()) : null;
                if (name == null)
                    name = attribute.getName();
                else
                    name = columnToCamelCase(name);
                // System.out
                // .println("PPP " + pojo + " " + attribute.getDbName() + " " + attribute.getName() + " " + name);
                buffer.append("\n    ").append(name).append(' ');
                if (attribute.isPrimitive()) {
                    buffer.append('_').append(attribute.getClassName());
                } else if (attribute.getDependencyClassName() != null) {
                    buffer.append(":: ").append(attribute.getDependencyClassName());
                } else {
                    buffer.append(": ").append(attribute.getClassName());
                }
                if (attribute.isRequired()) {
                    buffer.append(" required");
                }
            }
            buffer.append("\n  }\n");
        }
        return buffer.toString();
    }

    private String tableToCamelCase(String value) {
        if (value == null)
            return null;
        String[] parts = value.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            if (camelCaseString.length() == 0)
                if (part.length() > 1)
                    camelCaseString = camelCaseString + part.substring(0, 1).toUpperCase()
                            + part.substring(1).toLowerCase();
                else
                    camelCaseString = camelCaseString + part.toUpperCase();
            else if (part.length() == 1)
                camelCaseString = camelCaseString + part.toUpperCase();
            else if (part.length() > 1)
                camelCaseString = camelCaseString + part.substring(0, 1).toUpperCase()
                        + part.substring(1).toLowerCase();
        }
        return camelCaseString;
    }

    private String columnToCamelCase(String value) {
        if (value == null)
            return null;
        String[] parts = value.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            if (camelCaseString.length() == 0)
                camelCaseString = camelCaseString + part.toLowerCase();
            else if (part.length() == 1)
                camelCaseString = camelCaseString + part.toUpperCase();
            else if (part.length() > 1)
                camelCaseString = camelCaseString + part.substring(0, 1).toUpperCase()
                        + part.substring(1).toLowerCase();
        }
        return camelCaseString;
    }

    private PojoAttribute convertDbColumnDefinition(String table, DbColumn dbColumn) {
        if (dbColumn == null)
            return null;
        PojoAttrType sqlType = columnTypes.containsKey(table) ? columnTypes.get(table).get(dbColumn.getName()) : null;
        if (sqlType == null)
            sqlType = tableTypes.containsKey(table) ? tableTypes.get(table)
                    .get(dbColumn.getType() + dbColumn.getSize()) : null;
        if (sqlType == null)
            sqlType = sqlTypes.get(dbColumn.getType() + dbColumn.getSize());
        if (sqlType == null)
            return null;
        PojoAttribute attribute = new PojoAttribute();
        attribute.setDbTable(table);
        attribute.setDbName(dbColumn.getName());
        attribute.setName(columnToCamelCase(dbColumn.getName()));
        attribute.setRequired(!dbColumn.isNullable());
        if (sqlType.getNativeType() != null) {
            attribute.setPrimitive(true);
            attribute.setClassName(sqlType.getNativeType().substring(1) + (sqlType.isArray() ? " []" : ""));
        } else if (sqlType.getRef() != null) {
            attribute.setPrimitive(false);
            attribute.setDependencyClassName(sqlType.getRef().getName());
        } else {
            attribute.setPrimitive(false);
            attribute.setClassName(sqlType.getType().getIdentifier());
        }
        return attribute;
    }

    private PojoAttribute convertDbColumnDefault(String table, DbColumn dbColumn) {
        if (dbColumn == null)
            return null;
        PojoAttribute attribute = new PojoAttribute();
        attribute.setDbTable(table);
        attribute.setDbName(dbColumn.getName());
        attribute.setName(columnToCamelCase(dbColumn.getName()));
        attribute.setRequired(!dbColumn.isNullable());
        switch (dbColumn.getSqlType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            if (!dbColumn.isNullable()) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.BOOLEAN.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(Boolean.class.getName());
            }
            break;
        case Types.TINYINT:
            if (!dbColumn.isNullable()) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.BYTE.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(Byte.class.getName());
            }
            break;
        case Types.SMALLINT:
            if (!dbColumn.isNullable()) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.SHORT.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(Short.class.getName());
            }
            break;
        case Types.INTEGER:
            if (!dbColumn.isNullable()) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.INT.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(Integer.class.getName());
            }
            break;
        case Types.BIGINT:
            if (!dbColumn.isNullable()) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.LONG.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(Long.class.getName());
            }
            break;
        case Types.FLOAT:
        case Types.REAL:
            if (!dbColumn.isNullable()) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.FLOAT.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(Float.class.getName());
            }
            break;
        case Types.DOUBLE:
            if (!dbColumn.isNullable()) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.DOUBLE.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(Double.class.getName());
            }
            break;
        case Types.NUMERIC:
            attribute.setPrimitive(false);
            attribute.setClassName(BigDecimal.class.getName());
            break;
        case Types.DECIMAL:
            attribute.setPrimitive(false);
            if (dbColumn.getSize() < 3)
                attribute.setClassName(Byte.class.getName());
            else if (dbColumn.getSize() < 5)
                attribute.setClassName(Short.class.getName());
            else if (dbColumn.getSize() < 10)
                attribute.setClassName(Integer.class.getName());
            else if (dbColumn.getSize() < 20)
                attribute.setClassName(Long.class.getName());
            else
                attribute.setClassName(BigInteger.class.getName());
            break;
        case Types.CHAR:
        case Types.NCHAR:
            if (!dbColumn.isNullable() && dbColumn.getSize() == 1) {
                attribute.setPrimitive(true);
                attribute.setClassName(PrimitiveType.CHAR.getName());
            } else {
                attribute.setPrimitive(false);
                attribute.setClassName(String.class.getName());
            }
            break;
        case Types.VARCHAR:
        case Types.LONGNVARCHAR:
        case Types.NVARCHAR:
            attribute.setPrimitive(false);
            attribute.setClassName(String.class.getName());
            break;
        case Types.DATE:
            // java.util.Date or java.sql.Date ?
            attribute.setPrimitive(false);
            attribute.setClassName(java.util.Date.class.getName());
            break;
        case Types.TIME:
            attribute.setPrimitive(false);
            attribute.setClassName(java.sql.Time.class.getName());
            break;
        case Types.TIMESTAMP:
            // java.util.Date or java.sql.Timestamp ?
            attribute.setPrimitive(false);
            attribute.setClassName(java.sql.Timestamp.class.getName());
            break;
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
            attribute.setPrimitive(true);
            attribute.setClassName(PrimitiveType.BYTE_ARRAY.getName());
            break;
        case Types.CLOB:
            attribute.setPrimitive(false);
            attribute.setClassName(String.class.getName());
            break;
        default:
            // todo what type?
            attribute.setPrimitive(false);
            if (dbColumn.getType().indexOf("TIMESTAMP") == 0 || dbColumn.getType().indexOf("timestamp") == 0)
                attribute.setClassName(java.sql.Timestamp.class.getName());
            else
                attribute.setClassName("java.lang.Object");
        }
        return attribute;
    }
}
