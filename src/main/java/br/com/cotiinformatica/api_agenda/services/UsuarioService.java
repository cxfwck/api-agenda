package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.dtos.AutenticarUsuarioRequest;
import br.com.cotiinformatica.api_agenda.dtos.AutenticarUsuarioResponse;
import br.com.cotiinformatica.api_agenda.dtos.CriarUsuarioRequest;
import br.com.cotiinformatica.api_agenda.dtos.CriarUsuarioResponse;
import br.com.cotiinformatica.api_agenda.entities.Usuario;
import br.com.cotiinformatica.api_agenda.exceptions.AcessoNegadoException;
import br.com.cotiinformatica.api_agenda.exceptions.EmailJaCadastradoException;
import br.com.cotiinformatica.api_agenda.repositories.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
        Método para autenticar um usuário no sistema
     */
    public AutenticarUsuarioResponse autenticarUsuario(AutenticarUsuarioRequest request) throws Exception {

        //Procurar o usuário no banco de dados através do email
        var usuario = usuarioRepository.findByEmail(request.email());

        //Verificar se o usuário não foi encontrado
        if(usuario == null) {
            throw new AcessoNegadoException("Acesso negado. Usuário não encontrado.");
        }

        //Criptografando a senha enviada pelo usuário
        var senhaCriptografada = criptografarSenha(request.senha());

        //Verificar a senha enviada não confere com a senha cadastrada no banco
        if( ! usuario.getSenha().equals(senhaCriptografada)) {
            throw new AcessoNegadoException("Acesso negado. Credenciais inválidas.");
        }

        //Retornar os dados do usuário autenticado
        return new AutenticarUsuarioResponse(
          "Usuário autenticado com sucesso.",
          LocalDateTime.now(),
          usuario.getId(),
          usuario.getNome(),
          usuario.getEmail(),
          gerarToken(usuario.getEmail())
        );
    }

    /*
        Método para criar um usuário no sistema
     */
    public CriarUsuarioResponse criarUsuario(CriarUsuarioRequest request) throws Exception {

        //Buscar o usuário no banco de dados através do email
        if(usuarioRepository.findByEmail(request.email()) != null) {
            throw new EmailJaCadastradoException("O email informado já está cadastrado, tente outro.");
        }

        //Criando um objeto da entidade Usuario
        var usuario = new Usuario();

        //Preencher os dados do usuário
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(criptografarSenha(request.senha()));
        usuario.setDataHoraCriacao(LocalDateTime.now());

        //Salvar o usuário no banco de dados
        usuarioRepository.save(usuario);

        //Retornar os dados (resposta)
        return new CriarUsuarioResponse(
                "Usuário cadastrado com sucesso.",
                LocalDateTime.now(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    /*
        Método para gerar o token JWT com a credencial do usuário autenticado
     */
    private String gerarToken(String email) throws Exception {

        //Chave para criptografar o token
        var chaveAssinatura = "e2b38b1e-123a-4276-870f-32706418de8c";

        //Gerando e retornando o TOKEN
        return Jwts.builder()
                .setSubject(email) //Identificação do usuário
                .setIssuedAt(new Date()) //Data e hora de geração
                .signWith(SignatureAlgorithm.HS256, chaveAssinatura)
                .compact();
    }

    /*
        Método para criptografar a senha do usuário
     */
    private String criptografarSenha(String senha) throws Exception {
        //Instanciando o algoritmo SHA-256
        var messageDigest = MessageDigest.getInstance("SHA-256");
        //Gerando o hash da senha
        var hash = messageDigest.digest(
                senha.getBytes(StandardCharsets.UTF_8)
        );

        //Convertendo o hash para hexadecimal
        var hexadecimal = new StringBuilder();
        for (byte b : hash) {
            hexadecimal.append(String.format("%02x", b));
        }

        return hexadecimal.toString();
    }

}
