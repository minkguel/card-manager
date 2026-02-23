package com.mikkelpedersen.pokemonmanager;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonCardRepository extends MongoRepository<PokemonCard, String> {
    // Spring Data MongoDB will automatically implement this method
    List<PokemonCard> findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(String name, String type);

}
