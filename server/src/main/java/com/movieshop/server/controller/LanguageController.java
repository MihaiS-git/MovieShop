package com.movieshop.server.controller;

import com.movieshop.server.model.LanguageRequestDTO;
import com.movieshop.server.model.LanguageResponseDTO;
import com.movieshop.server.service.ILanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/languages")
public class LanguageController {

    private final ILanguageService languageService;

    public LanguageController(ILanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponseDTO>> getAllLanguages() {
        List<LanguageResponseDTO> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponseDTO> getLanguageById(@PathVariable Integer id) {
        LanguageResponseDTO language = languageService.getLanguageById(id);
        return ResponseEntity.ok(language);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<LanguageResponseDTO> getLanguageByName(@PathVariable String name) {
        LanguageResponseDTO language = languageService.getLanguageByName(name);
        return ResponseEntity.ok(language);
    }

    @PostMapping
    public ResponseEntity<LanguageResponseDTO> createLanguage(@RequestBody LanguageRequestDTO languageRequestDTO) {
        LanguageResponseDTO createdLanguage = languageService.createLanguage(languageRequestDTO);
        return ResponseEntity.status(201).body(createdLanguage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageResponseDTO> updateLanguage(@PathVariable Integer id, @RequestBody LanguageRequestDTO languageRequestDTO) {
        LanguageResponseDTO updatedLanguage = languageService.updateLanguage(id, languageRequestDTO);
        return ResponseEntity.ok(updatedLanguage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Integer id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
