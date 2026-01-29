package com.mikkelpedersen.pokemonmanager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from React frontend
@RequiredArgsConstructor
public class PokemonCardController {

    private final PokemonCardService service;

    // GET /api/cards?sortBy=name&search=charizard
    @GetMapping
    public ResponseEntity<List<PokemonCard>> getCards(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String search) {

        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(service.searchCards(search));
        }
        return ResponseEntity.ok(service.getAllCards(sortBy));
    }

    // POST /api/cards (multipart/form-data)
    @PostMapping
    public ResponseEntity<PokemonCard> createCard(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String rarity,
            @RequestParam(required = false) MultipartFile image) {

        try {
            PokemonCard card = service.saveCard(name, type, rarity, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(card);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE /api/cards/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        service.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}