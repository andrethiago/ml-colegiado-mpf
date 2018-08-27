package br.gov.mpf.unico.scriptsdb.autoadministrativo;

public class ExpedienteDeliberadoColegiado {

	private Long id;
	private String procedimento;
	private Long classe;
	private Long areaAtuacao;
	private Long itemCnmp;
	private String dataAutuacao;
	private String dataEntrada;
	private Integer urgente;
	private Integer prioritario;
	private Long municipio;
	private Integer homologado;

	private Integer quantidadeConversoes;
	private Integer quantidadeProvidencias;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public Long getClasse() {
		return classe;
	}

	public void setClasse(Long classe) {
		this.classe = classe;
	}

	public Long getAreaAtuacao() {
		return areaAtuacao;
	}

	public void setAreaAtuacao(Long areaAtuacao) {
		this.areaAtuacao = areaAtuacao;
	}

	public Long getItemCnmp() {
		return itemCnmp;
	}

	public void setItemCnmp(Long itemCnmp) {
		this.itemCnmp = itemCnmp;
	}

	public String getDataAutuacao() {
		return dataAutuacao;
	}

	public void setDataAutuacao(String dataAutuacao) {
		this.dataAutuacao = dataAutuacao;
	}

	public String getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Integer getUrgente() {
		return urgente;
	}

	public void setUrgente(Integer urgente) {
		this.urgente = urgente;
	}

	public Integer getPrioritario() {
		return prioritario;
	}

	public void setPrioritario(Integer prioritario) {
		this.prioritario = prioritario;
	}

	public Long getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Long municipio) {
		this.municipio = municipio;
	}

	public Integer getQuantidadeConversoes() {
		return quantidadeConversoes;
	}

	public void setQuantidadeConversoes(Integer quantidadeConversoes) {
		this.quantidadeConversoes = quantidadeConversoes;
	}

	public Integer getQuantidadeProvidencias() {
		return quantidadeProvidencias;
	}

	public void setQuantidadeProvidencias(Integer quantidadeProvidencias) {
		this.quantidadeProvidencias = quantidadeProvidencias;
	}

	public Integer getHomologado() {
		return homologado;
	}

	public void setHomologado(Integer homologado) {
		this.homologado = homologado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ExpedienteDeliberadoColegiado other = (ExpedienteDeliberadoColegiado) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExpedienteDeliberadoColegiado [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (procedimento != null) {
			builder.append("procedimento=");
			builder.append(procedimento);
			builder.append(", ");
		}
		if (classe != null) {
			builder.append("classe=");
			builder.append(classe);
			builder.append(", ");
		}
		if (areaAtuacao != null) {
			builder.append("areaAtuacao=");
			builder.append(areaAtuacao);
			builder.append(", ");
		}
		if (itemCnmp != null) {
			builder.append("itemCnmp=");
			builder.append(itemCnmp);
			builder.append(", ");
		}
		if (dataAutuacao != null) {
			builder.append("dataAutuacao=");
			builder.append(dataAutuacao);
			builder.append(", ");
		}
		if (dataEntrada != null) {
			builder.append("dataEntrada=");
			builder.append(dataEntrada);
			builder.append(", ");
		}
		if (urgente != null) {
			builder.append("urgente=");
			builder.append(urgente);
			builder.append(", ");
		}
		if (prioritario != null) {
			builder.append("prioritario=");
			builder.append(prioritario);
			builder.append(", ");
		}
		if (municipio != null) {
			builder.append("municipio=");
			builder.append(municipio);
			builder.append(", ");
		}
		if (quantidadeConversoes != null) {
			builder.append("quantidadeConversoes=");
			builder.append(quantidadeConversoes);
			builder.append(", ");
		}
		if (quantidadeProvidencias != null) {
			builder.append("quantidadeProvidencias=");
			builder.append(quantidadeProvidencias);
			builder.append(", ");
		}
		if (homologado != null) {
			builder.append("homologado=");
			builder.append(homologado);
		}
		builder.append("]");
		return builder.toString();
	}

}
