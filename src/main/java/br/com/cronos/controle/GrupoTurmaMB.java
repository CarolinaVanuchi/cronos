package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Grupo;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.modelo.Turma;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.PermiteInativar;
import br.com.cronos.util.RecuperarRelacoesProfessor;

@SessionScoped
@ManagedBean
public class GrupoTurmaMB {
	private GrupoTurma grupoTurma;
	private List<Turma> turmas;
	private List<GrupoTurma> grupoTurmas;
	private List<Grupo> grupos;
	private DAOGenerico dao;
	private RecuperarRelacoesProfessor relacoesProfessor;
	private PermiteInativar permiteInativar;

	public GrupoTurmaMB() {
		dao = new DAOGenerico();
		criarNovoObjetoGrupoTurma();
		turmas = new ArrayList<>();
		grupoTurmas = new ArrayList<>();
		grupos = new ArrayList<>();
		dao = new DAOGenerico();
		preencherListaGrupoTurma();
	}

	public void salvar() {
		try {

			if (grupoTurma.getId() == null) {
				if (verificarGrupoTurmaIguais(grupoTurma)) {
					ExibirMensagem.exibirMensagem(Mensagem.GRUPO_TURMA_CADASTRADO);
				} else {
					if (grupoTurma.getMaximoHoras() >= 0 && grupoTurma.getMinimoHoras() >= 0) {
						if (grupoTurma.getMaximoHoras() >= grupoTurma.getMinimoHoras()) {
							grupoTurma.setDataCadastro(new Date());
							grupoTurma.setStatus(true);
							dao.inserir(grupoTurma);
							ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
							criarNovoObjetoGrupoTurma();
							preencherListaGrupoTurma();
							FecharDialog.fecharDialogGrupoTurma();

						} else {
							ExibirMensagem.exibirMensagem(Mensagem.MAXIMO_DE_HORAS);
						}
					} else {
						ExibirMensagem.exibirMensagem(Mensagem.CAMPOS_NUMERICOS);
					}
				}
			} else {
				if ((verificarGrupoTurmaIguais(grupoTurma)) && (verificarGrupoTurmaIguaisAlterar(grupoTurma))) {
					ExibirMensagem.exibirMensagem(Mensagem.GRUPO_TURMA_CADASTRADO);
				} else {
					if (grupoTurma.getMaximoHoras() >= 0 && grupoTurma.getMinimoHoras() >= 0) {
						if (grupoTurma.getMaximoHoras() >= grupoTurma.getMinimoHoras()) {
							dao.alterar(grupoTurma);
							ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
							criarNovoObjetoGrupoTurma();
							preencherListaGrupoTurma();
							FecharDialog.fecharDialogGrupoTurma();
						} else {
							ExibirMensagem.exibirMensagem(Mensagem.MAXIMO_DE_HORAS);
						}
					} else {
						ExibirMensagem.exibirMensagem(Mensagem.CAMPOS_NUMERICOS);
					}
				}
			}

		} catch (

		Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(GrupoTurma grupoTurma) {
		permiteInativar = new PermiteInativar();
		try {
			if (permiteInativar.verificarGrupoTurmaComAtividadeTurma(grupoTurma)) {
				grupoTurma.setStatus(false);
				dao.alterar(grupoTurma);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaGrupoTurma();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.GRUPO_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}

	}

	public void criarNovoObjetoGrupoTurma() {
		grupoTurma = new GrupoTurma();
	}

	public void preencherListaGrupoTurma() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		grupoTurmas = relacoesProfessor.recuperarGrupoTurmasProfessor();
	}

	public void preencherListaTurma() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		turmas = relacoesProfessor.recuperarTurmasProfessor();
	}

	public void preencherListaGrupo() {
		grupos = dao.listaComStatus(Grupo.class);
	}

	public Boolean verificarGrupoTurmaIguais(GrupoTurma grupoTurma) {
		try {
			List<GrupoTurma> verificador = new ArrayList<>();
			verificador = dao.listar(GrupoTurma.class,
					" turma = " + grupoTurma.getTurma().getId() + " and grupo = " + grupoTurma.getGrupo().getId());
			if (verificador.isEmpty())
				return false;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean verificarGrupoTurmaIguaisAlterar(GrupoTurma grupoTurma) {
		try {
			List<GrupoTurma> verificador = new ArrayList<>();
			verificador = dao.listar(GrupoTurma.class, " turma = " + grupoTurma.getTurma().getId() + " and grupo = "
					+ grupoTurma.getGrupo().getId() + " and id = " + grupoTurma.getId());
			if (verificador.isEmpty())
				return true;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return false;
	}

	public List<Turma> completarTurma(String str) {
		preencherListaTurma();
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma t : turmas) {
			if (t.getDescricao().startsWith(str)) {
				turmasSelecionadas.add(t);
			}
		}
		return turmasSelecionadas;
	}

	public List<Grupo> completarGrupos(String str) {
		preencherListaGrupo();
		List<Grupo> gruposSelecionados = new ArrayList<>();
		for (Grupo gru : grupos) {
			if (gru.getDescricao().toLowerCase().startsWith(str)) {
				gruposSelecionados.add(gru);
			}
		}
		return gruposSelecionados;
	}

	public GrupoTurma getGrupoTurma() {
		return grupoTurma;
	}

	public void setGrupoTurma(GrupoTurma grupoTurma) {
		this.grupoTurma = grupoTurma;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<GrupoTurma> getGrupoTurmas() {
		return grupoTurmas;
	}

	public void setGrupoTurmas(List<GrupoTurma> grupoTurmas) {
		this.grupoTurmas = grupoTurmas;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

}
