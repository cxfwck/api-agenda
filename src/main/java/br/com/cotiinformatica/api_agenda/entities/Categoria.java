package br.com.cotiinformatica.api_agenda.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "categorias")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 50, nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "categoria")
    private List<Tarefa> tarefas;
}
