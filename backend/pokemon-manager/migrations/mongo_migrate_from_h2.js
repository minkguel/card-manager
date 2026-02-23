/*
Migration script for mongosh / MongoDB
Migrates exported H2 pokemon_cards data into MongoDB.

The JSON export produced by tools/h2-exporter uses uppercase column names:
  ID, NAME, TYPE, RARITY, IMAGE (base64), DATE_ADDED (ISO string)

This script maps them to the fields used by the Spring Data MongoDB model:
  _id (String), name, type, rarity, image (BinData), dateAdded (ISODate)

Usage:
  1. Copy pokemon_cards_export.json into this directory (migrations/).
  2. Connect mongosh to your target database:
       mongosh "mongodb://localhost:27017/pokemon_manager" --file migrations/mongo_migrate_from_h2.js
     or inside an interactive mongosh session:
       load('migrations/mongo_migrate_from_h2.js');

-- Begin Script --
*/

// CONFIG
const EXPORT_FILENAME = 'pokemon_cards_export.json';
const TARGET_COLLECTION = 'pokemon_cards';

// Helper: parse date string -> ISODate
function parseDate(val) {
  if (!val) return null;
  if (val instanceof Date) return val;
  if (typeof val === 'number') return new Date(val);
  const parsed = new Date(val);
  if (!isNaN(parsed.getTime())) return parsed;
  const m = /^\s*(\d{4})-(\d{2})-(\d{2})\s*$/.exec(String(val));
  if (m) return new Date(Number(m[1]), Number(m[2]) - 1, Number(m[3]));
  return null;
}

// Read export file
let raw;
try {
  raw = cat(EXPORT_FILENAME);
} catch (e) {
  throw new Error(`Could not read '${EXPORT_FILENAME}'. Run mongosh from the migrations/ directory. Error: ${e}`);
}

let rows;
try {
  rows = JSON.parse(raw);
  if (!Array.isArray(rows)) throw new Error('Expected a JSON array');
} catch (e) {
  throw new Error(`Failed to parse JSON: ${e}`);
}

if (rows.length === 0) {
  print('No rows in export file; nothing to migrate.');
} else {
  print(`Migrating ${rows.length} rows into ${db.getName()}.${TARGET_COLLECTION} ...`);
}

const coll = db.getCollection(TARGET_COLLECTION);
let migrated = 0;
let skipped = 0;

rows.forEach((row, idx) => {
  try {
    const doc = {};

    // Map H2 numeric ID -> string _id
    const rawId = row['ID'] ?? row['id'];
    if (rawId != null && rawId !== '') {
      doc._id = String(rawId);
    }

    // Basic string fields (H2 exporter uses uppercase column names)
    const name = row['NAME'] ?? row['name'];
    if (name != null) doc.name = name;

    const type = row['TYPE'] ?? row['type'];
    if (type != null) doc.type = type;

    const rarity = row['RARITY'] ?? row['rarity'];
    if (rarity != null) doc.rarity = rarity;

    // Image: stored as base64 string in export -> convert to BSON Binary
    // so it matches the Java byte[] field Spring Data MongoDB uses
    const imgVal = row['IMAGE'] ?? row['image'];
    if (imgVal != null && imgVal !== '') {
      // BinData(subtype 0 = generic binary, base64String)
      doc.image = BinData(0, String(imgVal));
    }

    // Date: stored as ISO string in export -> ISODate
    const dateVal = row['DATE_ADDED'] ?? row['date_added'] ?? row['dateAdded'];
    const parsedDate = parseDate(dateVal);
    if (parsedDate) {
      doc.dateAdded = parsedDate;
    }

    // Upsert by _id if present, otherwise plain insert
    if (doc._id) {
      const idVal = doc._id;
      const toSet = Object.assign({}, doc);
      delete toSet._id;
      coll.updateOne({ _id: idVal }, { $set: toSet }, { upsert: true });
    } else {
      coll.insertOne(doc);
    }

    migrated++;
  } catch (err) {
    printjson({ index: idx, error: String(err) });
    skipped++;
  }
});

print(`Migration complete. migrated=${migrated}, skipped=${skipped}`);

