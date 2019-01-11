package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.cronos.dao.DAOFiltros;
import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.util.ChamarRelatorio;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.Mensagem;

@ViewScoped
@ManagedBean
public class GraficoCertificadoAlunoMB {
	private BarChartModel grafico;
	private AlunoTurma alunoTurma;
	private DAOFiltros daoFiltros;
	private UsuarioSessaoMB usuarioSessao;
	private List<Certificado> certificados;
	private List<Long> idGrupos;
	private Set<Long> idGruposSelecionados;
	private DAOGenerico dao;
	private ChartSeries totalGrupo;
	private ChartSeries totalCertificado;
	private List<Long> gruposTurmas;

	public GraficoCertificadoAlunoMB() {
		grafico = new BarChartModel();
		alunoTurma = new AlunoTurma();
		daoFiltros = new DAOFiltros();
		usuarioSessao = new UsuarioSessaoMB();
		certificados = new ArrayList<>();
		idGrupos = new ArrayList<>();
		idGruposSelecionados = new HashSet<>();
		dao = new DAOGenerico();
		totalGrupo = new ChartSeries();
		totalCertificado = new ChartSeries();
		gruposTurmas = new ArrayList<>();
		calcularHoras();
	}

	private Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		return alunoTurma.getTurma().getId();
	}

	private void recuperarCertificados() {
		certificados = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 3);
	}

	private void ordenarIdGrupoTurma() {
		recuperarCertificados();
		for (Certificado c : certificados) {
			idGrupos.add(c.getIdGrupoTurma());
		}
		Collections.sort(idGrupos);
	}

	private void escolherGruposTurmas() {
		ordenarIdGrupoTurma();
		for (Long i : idGrupos) {
			idGruposSelecionados.add(i);
		}

	}

	private void calcularHoras() {
		Double somaCertificados = 0.0;
		escolherGruposTurmas();
		GrupoTurma grupoTurma = new GrupoTurma();
		GrupoTurma auxGrupoTurma = new GrupoTurma();
		gruposTurmas = new ArrayList<>();
		gruposTurmas.addAll(idGruposSelecionados);
		certificados = new ArrayList<>();
		for (int i = 0; i <= gruposTurmas.size() - 1; i++) {
			certificados = dao.listar(Certificado.class, " situacao = 3 and aluno = "
					+ usuarioSessao.recuperarAluno().getId() + " and idGrupoTurma = " + gruposTurmas.get(i));

			for (Certificado c : certificados) {
				somaCertificados = somaCertificados + c.getHoraComputada();
				grupoTurma = (GrupoTurma) dao.listar(GrupoTurma.class, " id = " + c.getIdGrupoTurma()).get(0);
			}
			if (somaCertificados > 0.0) {
				criarGrafico(somaCertificados, grupoTurma);
				somaCertificados = 0.0;
			}
			grupoTurma = new GrupoTurma();
			certificados = new ArrayList<>();
		}

	}

	public void criarGrafico(Double somaCertificado, GrupoTurma grupoTurma) {
		totalGrupo.setLabel("Horas do Grupo " + grupoTurma.getGrupo().getDescricao());
		totalGrupo.set("Grupos", grupoTurma.getMaximoHoras());

		totalCertificado.setLabel("Horas dos Certificados " + grupoTurma.getGrupo().getDescricao());
		totalCertificado.set("Grupos", somaCertificado);

		grafico.addSeries(totalGrupo);
		grafico.addSeries(totalCertificado);

		grafico.setLegendPosition("ne");
		grafico.setAnimate(true);

		Axis yAxis = grafico.getAxis(AxisType.Y);
		yAxis.setLabel("Horas");

		totalGrupo = new ChartSeries();
		totalCertificado = new ChartSeries();

	}

	public BarChartModel getGrafico() {
		return grafico;
	}

	public void setGrafico(BarChartModel grafico) {
		this.grafico = grafico;
	}

	public void imprimirCertificadoGrupo() {
		try {
			List<Certificado> certificados = dao.listar(Certificado.class,
					" situacao = 3 and aluno = " + usuarioSessao.recuperarAluno().getId());
			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				ChamarRelatorio ch = new ChamarRelatorio();
				HashMap parametro = new HashMap<>();
				parametro.put("ALUNO", usuarioSessao.recuperarAluno().getId());

				ch.imprimeRelatorio("grupo.jasper", parametro, "Relatório situação nos grupos");
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

}