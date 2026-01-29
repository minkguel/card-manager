package com.mikkelpedersen.pokemonmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonCardRepository extends JpaRepository<PokemonCard, Long> {
    // Spring Data JPA will automatically implement this method
    // It filters where name contains the text or type contains the text
    // It will automatically generate a matching SQL query
    List<PokemonCard> findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(String name, String type);

}
