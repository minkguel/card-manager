package com.mikkelpedersen.pokemonmanager;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<Void> deleteCard(@PathVariable String id) {
        service.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}