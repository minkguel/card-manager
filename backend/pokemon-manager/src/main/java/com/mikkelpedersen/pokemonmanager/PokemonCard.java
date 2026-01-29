package com.mikkelpedersen.pokemonmanager;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type; // e.g. Grass, Fire, Water
    private String rarity; // e.g. Common, Uncommon, Rare, Ultra Rare

    @Lob
    private byte[] image; // Stores image data

    private LocalDate dateAdded;

    // Sets the date added to the current date when the entity is created
    @PrePersist
    protected void onCreate() {
        this.dateAdded = LocalDate.now();
    }
}
