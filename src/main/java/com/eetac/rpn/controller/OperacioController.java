package com.eetac.rpn.controller;

import com.eetac.rpn.dto.OperacioRequestDTO;
import com.eetac.rpn.dto.OperacioResponseDTO;
import com.eetac.rpn.model.Operacio;
import com.eetac.rpn.service.MathManager;
import com.eetac.rpn.service.MathManagerImpl;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/operacions")
public class OperacioController {

    private final MathManager manager = MathManagerImpl.getInstance();

    @PostMapping
    public ResponseEntity<?> requerirOperacio(
            @RequestBody OperacioRequestDTO dto) {
        Operacio op = new Operacio(
                dto.getId(), dto.getIdAlumne(), dto.getExpressio());
        try {
            manager.requerirOperacio(op);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(toResponseDTO(op));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/processar")
    public ResponseEntity<?> processarOperacio() {
        try {
            Operacio op = manager.processarOperacio();
            return ResponseEntity.ok(toResponseDTO(op));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/institut/{id}")
    public ResponseEntity<?> operacionsPerInstitut(
            @PathVariable String id) {
        try {
            List<Operacio> ops = manager.llistatOperacionsPerInstitut(id);
            return ResponseEntity.ok(ops.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/alumne/{id}")
    public ResponseEntity<?> operacionsPerAlumne(
            @PathVariable String id) {
        try {
            List<Operacio> ops = manager.llistatOperacionsPerAlumne(id);
            return ResponseEntity.ok(ops.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    private OperacioResponseDTO toResponseDTO(Operacio op) {
        OperacioResponseDTO dto = new OperacioResponseDTO();
        dto.setId(op.getId());
        dto.setIdAlumne(op.getIdAlumne());
        dto.setExpressio(op.getExpressio());
        dto.setResultat(op.getResultat());
        dto.setEstat(op.getEstat());
        return dto;
    }
}