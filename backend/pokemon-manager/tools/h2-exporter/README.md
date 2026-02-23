H2 exporter tool

This small tool reads an H2 database file (.mv.db) and exports the contents of a table to a JSON array file.

Prerequisites
- Java 17+ (or compatible as configured in the mini-pom).
- Maven (or you can use the project's Maven wrapper `mvnw`).

Build and run (from repo root)

# Using the project's wrapper (recommended)
cd /Users/mikkelpedersen/pokemon-manager/backend/pokemon-manager
chmod +x mvnw
./mvnw -f tools/h2-exporter/pom.xml compile exec:java -Dexec.args="/absolute/path/to/pokemonDB.mv.db pokemon_cards_export.json POKEMONCARD"

# Or build and run the jar
mvn -f tools/h2-exporter/pom.xml package
java -cp tools/h2-exporter/target/h2-exporter-0.0.1.jar com.mikkelpedersen.h2export.ExportH2ToJson /absolute/path/to/pokemonDB.mv.db pokemon_cards_export.json POKEMONCARD

Arguments
1) Path to H2 .mv.db file. Example: `/Users/mikkelpedersen/pokemon-manager/backend/pokemon-manager/data/pokemonDB.mv.db`
2) Output JSON filename (optional). Defaults to `pokemon_cards_export.json` in the current directory.
3) Table name to export (optional). Defaults to `POKEMONCARD`.

Notes
- Binary blob columns (images) are exported as base64 strings.
- Date/timestamps are exported as ISO strings.
- The produced JSON is directly consumable by `migrations/mongo_migrate_from_h2.js` created earlier.

If you want, I can also run the export for you if you allow me to run commands here — otherwise follow the steps above and paste the exported `pokemon_cards_export.json` here if you want me to review or run the mongo migration script against it.