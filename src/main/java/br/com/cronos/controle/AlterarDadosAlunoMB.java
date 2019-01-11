package br.com.cronos.controle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.util.CriptografiaSenha;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.ValidacoesGerirUsuarios;

@SessionScoped
@ManagedBean
public class AlterarDadosAlunoMB {

	private Aluno aluno;
	private DAOGenerico dao;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;
	private UsuarioSessaoMB usuarioSessao;
	private String senha;
	private String senha2;

	public AlterarDadosAlunoMB() {
		aluno = new Aluno();
		dao = new DAOGenerico();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
		usuarioSessao = new UsuarioSessaoMB();
		senha = "";
		senha2 = "";
		preencherAluno();
	}

	public void alterarAluno() {
		try {
			aluno.setUsuario(aluno.getUsuario().replace("'", "").replace("=", "").replace("<", "").replace("&", ""));
			if ((validacoesGerirUsuarios.buscarUsuarios(aluno))
					&& (validacoesGerirUsuarios.buscarUsuarioAlterar(aluno))) {
				ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
			} else {
				if (senha != "") {
					aluno.setSenha(senha);
					aluno.setSenha(CriptografiaSenha.criptografar(aluno.getSenha()));
				}
				dao.alterar(aluno);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherAluno();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro alterarAluno");
			e.printStackTrace();
		}
	}

	public void preencherAluno() {
		try {
			aluno = (Aluno) usuarioSessao.recuperarAluno();
		} catch (Exception e) {
			System.err.println("Erro preencherAluno");
			e.printStackTrace();
		}
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}
}
