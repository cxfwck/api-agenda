package br.com.cotiinformatica.api_agenda.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    @Column(name = "datahoracriacao", nullable = false)
    private LocalDateTime dataHoraCriacao;

    @OneToMany(mappedBy = "usuario")
    private List<Tarefa> tarefas;
}
