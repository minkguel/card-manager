package com.mikkelpedersen.pokemonmanager;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "pokemon_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonCard {

    @Id
    private String id;

    private String name;
    private String type; // e.g. Grass, Fire, Water
    private String rarity; // e.g. Common, Uncommon, Rare, Ultra Rare

    private byte[] image; // Stores image data

    private LocalDate dateAdded;

}
