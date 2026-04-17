package com.eetac.rpn.service;

import com.eetac.rpn.model.Alumne;
import com.eetac.rpn.model.Institut;
import com.eetac.rpn.model.Operacio;

import java.util.List;

public interface MathManager {


    void afegirInstitut(Institut institut);


    void afegirAlumne(Alumne alumne) throws Exception;


    void requerirOperacio(Operacio operacio) throws Exception;


    Operacio processarOperacio() throws Exception;


    List<Operacio> llistatOperacionsPerInstitut(String idInstitut)
            throws Exception;


    List<Operacio> llistatOperacionsPerAlumne(String idAlumne)
            throws Exception;


    List<Institut> llistatInstitutsPorOperacions();
}