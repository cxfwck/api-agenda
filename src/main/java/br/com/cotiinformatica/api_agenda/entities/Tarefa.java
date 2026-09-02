package br.com.cotiinformatica.api_agenda.entities;

import br.com.cotiinformatica.api_agenda.enums.Prioridade;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tarefas")
@Data
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "datainicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "horainicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "datafim")
    private LocalDate dataFim;

    @Column(name = "horafim")
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false)
    private Prioridade prioridade;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
