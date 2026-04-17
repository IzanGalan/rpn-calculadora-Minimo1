package com.eetac.rpn.controller;

import com.eetac.rpn.dto.AlumneDTO;
import com.eetac.rpn.dto.InstitutDTO;
import com.eetac.rpn.model.Alumne;
import com.eetac.rpn.model.Institut;
import com.eetac.rpn.service.MathManager;
import com.eetac.rpn.service.MathManagerImpl;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instituts")
public class InstitutController {

    private final MathManager manager = MathManagerImpl.getInstance();


    @PostMapping
    public ResponseEntity<InstitutDTO> afegirInstitut(
            @RequestBody InstitutDTO dto) {
        Institut i = new Institut(dto.getId(), dto.getNom());
        manager.afegirInstitut(i);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @PostMapping("/alumnes")
    public ResponseEntity<?> afegirAlumne(
            @RequestBody AlumneDTO dto) {
        Alumne a = new Alumne(
                dto.getId(), dto.getNom(),
                dto.getCognoms(), dto.getIdInstitut());
        try {
            manager.afegirAlumne(a);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/ranking")
    public ResponseEntity<List<InstitutDTO>> ranking() {
        List<InstitutDTO> dtos = manager.llistatInstitutsPorOperacions()
                .stream().map(i -> {
                    InstitutDTO dto = new InstitutDTO();
                    dto.setId(i.getId());
                    dto.setNom(i.getNom());
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}