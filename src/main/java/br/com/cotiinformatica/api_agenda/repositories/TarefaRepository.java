package br.com.cotiinformatica.api_agenda.repositories;

import br.com.cotiinformatica.api_agenda.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    List<Tarefa> findByDataInicioBetween(LocalDate dataInicio, LocalDate dataFim);

    List<Tarefa> findByDataFimBetween(LocalDate dataInicio, LocalDate dataFim);

    @Query("""
        SELECT t FROM Tarefa t
        WHERE t.dataInicio BETWEEN :dataInicio AND :dataFim
           OR t.dataFim BETWEEN :dataInicio AND :dataFim
           OR (:dataInicio BETWEEN t.dataInicio AND t.dataFim)
           OR (:dataFim BETWEEN t.dataInicio AND t.dataFim)
        ORDER BY t.dataInicio, t.horaInicio, t.id
    """)
    List<Tarefa> consultarPorPeriodo(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
