package br.com.cronos.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

/**
 * Entity implementation class for Entity: Curso
 *
 */
@Entity
@Table(name = "tab_curso")
public class Curso implements Serializable {

	public Curso() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_curso")
	private Long id;

	@NotNull(message = "O campo nome não pode ser nulo")
	@Column(nullable = false)
	private String nome;

	@NotNull(message = "O campo data abertura não deve ser nula")
	@Past(message = "A data de abertura deve ser menor ou igual a data atual")
	@Column(name = "data_abertura", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataAbertura;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "data_cadastro")
	private Date dataCadastro;

	@Column(nullable = false)
	private Boolean status;

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
