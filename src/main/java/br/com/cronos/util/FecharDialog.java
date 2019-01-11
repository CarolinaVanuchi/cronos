package br.com.cronos.util;

import org.primefaces.context.RequestContext;

public class FecharDialog {

	public static void fecharDialogCursoFechamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgCursoFechar').hide();");
	}

	public static void fecharDialogCurso() {
		RequestContext.getCurrentInstance().execute("PF('dlgCurso').hide();");
	}

	public static void fecharDialogTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgTurma').hide();");
	}

	public static void fecharDialogGrupo() {
		RequestContext.getCurrentInstance().execute("PF('dlgGrupo').hide();");
	}

	public static void fecharDialogAtividade() {
		RequestContext.getCurrentInstance().execute("PF('dlgAtividade').hide();");
	}

	public static void fecharDialogGrupoTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgGrupoTurma').hide();");
	}

	public static void fecharDialogAtividadeTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgAtividadeTurma').hide();");
	}

	public static void fecharDialogAdministrador() {
		RequestContext.getCurrentInstance().execute("PF('dlgAdministrador').hide();");
	}

	public static void fecharDialogSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgSecretaria').hide();");
	}

	public static void fecharDialogProfessor() {
		RequestContext.getCurrentInstance().execute("PF('dlgProfessor').hide();");
	}

	public static void fecharDialogAluno() {
		RequestContext.getCurrentInstance().execute("PF('dlgAluno').hide();");
	}

	public static void fecharDialogAlunoCancelar() {
		RequestContext.getCurrentInstance().execute("PF('dlgCancelar').hide();");
	}

	public static void fecharDialogAlunoDestrancar() {
		RequestContext.getCurrentInstance().execute("PF('dlgAtivar').hide();");
	}

	public static void fecharDialogAlunoTrancamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgInativar').hide();");
	}

	public static void fecharDialogCertificado() {
		RequestContext.getCurrentInstance().execute("PF('dlgCertificado').hide();");
	}

	public static void fecharDialogCertificadoAutenticarSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgAutenticar').hide();");
	}

	public static void fecharDialogCertificadoInvalidarSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgInvalidar').hide();");
	}

	public static void fecharDialogCertificadoPenderSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgPender').hide();");
	}

	public static void fecharDialogCertificadoValidarProfessor() {
		RequestContext.getCurrentInstance().execute("PF('dlgValidar').hide();");
	}

	public static void fecharDialogCertificadoInvalidarProfessor() {
		RequestContext.getCurrentInstance().execute("PF('dlgInvalidar').hide();");
	}

	public static void fecharDialogCertificadoFileUpload() {
		RequestContext.getCurrentInstance().execute("PF('dlgUpload').hide();");
	}

	public static void fecharDialogAlunoTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoTurma').hide();");
	}

	public static void fecharDialogPendente() {
		RequestContext.getCurrentInstance().execute("PF('dlgPender').hide();");
	}
}
