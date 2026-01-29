package com.mikkelpedersen.pokemonmanager;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonCardService {

    private final PokemonCardRepository repository;

    // Get all cards with optional sorting
    public List<PokemonCard> getAllCards(String sortBy) {
        if (sortBy != null && !sortBy.isEmpty()) {
            return repository.findAll(Sort.by(sortBy));
        }
        return repository.findAll();
    }

    // Search cards by name or type
    public List<PokemonCard> searchCards(String query) {
        if (query == null || query.isEmpty()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(query, query);
    }

    // Save a new card with image
    public PokemonCard saveCard(String name, String type, String rarity, MultipartFile imageFile) throws IOException {
        PokemonCard card = new PokemonCard();
        card.setName(name);
        card.setType(type);
        card.setRarity(rarity);

        if (imageFile != null && !imageFile.isEmpty()) {
            card.setImage(imageFile.getBytes());
        }

        return repository.save(card);
    }

    // Delete a card by ID
    public void deleteCard(Long id) {
        repository.deleteById(id);
    }
}
