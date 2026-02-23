package com.mikkelpedersen.h2export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Locale;

public class ExportH2ToJson {

    // Usage:
    // mvn -f tools/h2-exporter/pom.xml compile exec:java
    // -Dexec.args="/absolute/path/to/pokemonDB.mv.db pokemon_cards_export.json
    // POKEMONCARD"
    // Or run the built jar with the same args.
    // Args: 1) path to .mv.db file or H2 database file (required)
    // 2) output json filename (optional, default: pokemon_cards_export.json)
    // 3) table name (optional, default: POKEMONCARD)

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java -jar h2-exporter.jar <path-to-.mv.db> [output.json] [TABLE_NAME]");
            System.exit(2);
        }

        String dbFile = args[0];
        String outFile = args.length >= 2 ? args[1] : "pokemon_cards_export.json";
        String tableName = args.length >= 3 ? args[2] : "POKEMONCARD";

        // normalize the path: H2 jdbc url expects the path without the .mv.db extension
        String jdbcPath = dbFile;
        if (jdbcPath.endsWith(".mv.db")) {
            jdbcPath = jdbcPath.substring(0, jdbcPath.length() - ".mv.db".length());
        }

        // Build JDBC URL
        String jdbcUrl = "jdbc:h2:file:" + jdbcPath + ";IFEXISTS=TRUE";

        System.out.println("Connecting to H2 DB: " + jdbcUrl);

        // load driver implicitly via dependency
        try (Connection conn = DriverManager.getConnection(jdbcUrl, "sa", "")) {
            DatabaseMetaData meta = conn.getMetaData();
            // check table existence (case-insensitive)
            try (ResultSet rs = meta.getTables(null, null, tableName.toUpperCase(Locale.ROOT), null)) {
                if (!rs.next()) {
                    // try lowercase
                    try (ResultSet rs2 = meta.getTables(null, null, tableName.toLowerCase(Locale.ROOT), null)) {
                        if (!rs2.next()) {
                            System.err.println(
                                    "Table '" + tableName + "' not found in the H2 database. Available tables:");
                            try (ResultSet all = meta.getTables(null, null, "%", new String[] { "TABLE" })) {
                                while (all.next()) {
                                    System.err.println("  - " + all.getString("TABLE_NAME"));
                                }
                            }
                            System.exit(3);
                        }
                    }
                }
            }

            String query = "SELECT * FROM " + tableName;
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                try (ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int colCount = rsmd.getColumnCount();

                    ObjectMapper om = new ObjectMapper();
                    ArrayNode arr = om.createArrayNode();

                    while (rs.next()) {
                        ObjectNode obj = om.createObjectNode();
                        for (int i = 1; i <= colCount; i++) {
                            String colName = rsmd.getColumnLabel(i);
                            int type = rsmd.getColumnType(i);
                            Object value = rs.getObject(i);

                            if (value == null) {
                                obj.putNull(colName);
                                continue;
                            }

                            switch (type) {
                                case Types.BINARY:
                                case Types.VARBINARY:
                                case Types.LONGVARBINARY:
                                case Types.BLOB:
                                    // Read blob as bytes and encode as base64 string
                                    byte[] bytes = rs.getBytes(i);
                                    if (bytes != null) {
                                        obj.put(colName, java.util.Base64.getEncoder().encodeToString(bytes));
                                    } else {
                                        obj.putNull(colName);
                                    }
                                    break;
                                case Types.INTEGER:
                                case Types.SMALLINT:
                                case Types.TINYINT:
                                case Types.BIGINT:
                                    obj.put(colName, ((Number) value).longValue());
                                    break;
                                case Types.FLOAT:
                                case Types.DOUBLE:
                                case Types.REAL:
                                case Types.NUMERIC:
                                case Types.DECIMAL:
                                    obj.put(colName, ((Number) value).doubleValue());
                                    break;
                                case Types.DATE:
                                case Types.TIMESTAMP:
                                    // convert to ISO string
                                    Timestamp ts = rs.getTimestamp(i);
                                    if (ts != null)
                                        obj.put(colName, ts.toInstant().toString());
                                    else
                                        obj.putNull(colName);
                                    break;
                                case Types.BOOLEAN:
                                    obj.put(colName, (Boolean) value);
                                    break;
                                default:
                                    obj.put(colName, value.toString());
                                    break;
                            }
                        }
                        arr.add(obj);
                    }

                    // write to file
                    File out = new File(outFile);
                    try (Writer w = new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8)) {
                        om.writerWithDefaultPrettyPrinter().writeValue(w, arr);
                    }

                    System.out.println("Exported " + arr.size() + " rows to " + out.getAbsolutePath());
                }
            }
        }
    }
}
