package br.com.cronos.controle;

import br.com.cronos.dao.DAOUsuario;
import br.com.cronos.modelo.Administrador;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.Pessoa;
import br.com.cronos.modelo.Professor;
import br.com.cronos.modelo.Secretaria;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@SessionScoped
@ManagedBean
public class UsuarioSessaoMB {
	private Pessoa pessoa;
	private Aluno usuarioAluno;
	private Secretaria secretaria;
	private Administrador administrador;
	private Professor professor;
	private DAOUsuario daoUsuario;

	public UsuarioSessaoMB() {
		pessoa = new Pessoa();
		SecurityContext context = SecurityContextHolder.getContext();
		if (context instanceof SecurityContext) {
			Authentication authentication = context.getAuthentication();
			if (authentication instanceof Authentication) {
				pessoa.setUsuario(((User) authentication.getPrincipal()).getUsername());
			}
		}
	}

	public Pessoa recuperarAluno() {
		usuarioAluno = new Aluno();
		daoUsuario = new DAOUsuario();
		try {
			usuarioAluno = (Aluno) daoUsuario.retornarLogado(Aluno.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		return usuarioAluno;
	}

	public Secretaria recuperarSecretaria() {
		secretaria = new Secretaria();
		daoUsuario = new DAOUsuario();
		try {
			secretaria = (Secretaria) daoUsuario.retornarLogado(Secretaria.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		return secretaria;
	}

	public Professor recuperarProfessor() {
		professor = new Professor();
		daoUsuario = new DAOUsuario();
		try {
			professor = (Professor) daoUsuario.retornarLogado(Professor.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		return professor;
	}

	public Administrador recuperarAdmisnitrador() {
		administrador = new Administrador();
		daoUsuario = new DAOUsuario();
		try {
			administrador = (Administrador) daoUsuario.retornarLogado(Administrador.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		return administrador;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
