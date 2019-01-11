package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.Secretaria;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;

@SessionScoped
@ManagedBean
public class CertificadoSecretariaMB {
	private Certificado certificado;
	private DAOGenerico dao;
	private UsuarioSessaoMB usuarioSessao;
	private List<Certificado> certificadosPendentes;
	private List<Certificado> certificadosInvalidos;
	private List<Certificado> certificadosAutenticados;

	public CertificadoSecretariaMB() {
		certificado = new Certificado();
		dao = new DAOGenerico();
		usuarioSessao = new UsuarioSessaoMB();
		certificadosPendentes = new ArrayList<>();
		certificadosInvalidos = new ArrayList<>();
		certificadosAutenticados = new ArrayList<>();
		preencherListaPendentes();
		preencherListaAutenticados();
		preencherListaInvalidos();
	}

	public void autenticar() {
		try {
			certificado.setSituacao(1);
			Secretaria secretariaRecuperarSesao = (Secretaria) usuarioSessao.recuperarSecretaria();
			certificado.setAutenticadoSecretaria(
					secretariaRecuperarSesao.getNome() + ", " + secretariaRecuperarSesao.getUsuario());

			dao.alterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoAutenticarSecretaria();
			preencherListaPendentes();
			preencherListaAutenticados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void invalidar() {
		try {
			certificado.setSituacao(2);
			Secretaria secretariaRecuperarSesao = (Secretaria) usuarioSessao.recuperarSecretaria();
			certificado.setAutenticadoSecretaria(
					secretariaRecuperarSesao.getNome() + ", " + secretariaRecuperarSesao.getUsuario());
			dao.alterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoInvalidarSecretaria();
			preencherListaPendentes();
			preencherListaAutenticados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void pender() {
		try {
			certificado.setSituacao(0);
			Secretaria secretariaRecuperarSesao = (Secretaria) usuarioSessao.recuperarSecretaria();
			certificado.setAutenticadoSecretaria(
					secretariaRecuperarSesao.getNome() + ", " + secretariaRecuperarSesao.getUsuario());
			dao.alterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoPenderSecretaria();
			preencherListaPendentes();
			preencherListaAutenticados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	private void criarNovoCertificado() {
		certificado = new Certificado();
	}

	private void preencherListaPendentes() {
		certificadosPendentes = dao.listar(Certificado.class, " situacao = 0 ");
	}

	private void preencherListaAutenticados() {
		certificadosAutenticados = dao.listar(Certificado.class, " situacao = 1 ");
	}

	private void preencherListaInvalidos() {
		certificadosInvalidos = dao.listar(Certificado.class, " situacao = 2 ");
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public List<Certificado> getCertificadosPendentes() {
		return certificadosPendentes;
	}

	public void setCertificadosPendentes(List<Certificado> certificadosPendentes) {
		this.certificadosPendentes = certificadosPendentes;
	}

	public List<Certificado> getCertificadosInvalidos() {
		return certificadosInvalidos;
	}

	public void setCertificadosInvalidos(List<Certificado> certificadosInvalidos) {
		this.certificadosInvalidos = certificadosInvalidos;
	}

	public List<Certificado> getCertificadosAutenticados() {
		return certificadosAutenticados;
	}

	public void setCertificadosAutenticados(List<Certificado> certificadosAutenticados) {
		this.certificadosAutenticados = certificadosAutenticados;
	}
}
