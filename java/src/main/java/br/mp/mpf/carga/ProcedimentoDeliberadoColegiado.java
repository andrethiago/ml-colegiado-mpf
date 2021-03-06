package br.mp.mpf.carga;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProcedimentoDeliberadoColegiado {

	private Long id;
	private String procedimento;
	private Long classe;
	private Long areaAtuacao;
	private Long itemCnmp;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date dataAutuacao;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date dataEntrada;
	private Integer urgente;
	private Integer prioritario;
	private Long municipio;
	@JsonIgnore
	private PecaPedidoColegiado pecaPromocao;
	private Long membroResponsavel;
	private Long identificadorPecaPromocao;
	private Integer quantidadeConversoes;
	private Integer quantidadeProvidencias;
	private Integer homologado;

	@JsonIgnore
	private Set<String> providenciasExecutadas = new HashSet<>();

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

	public Date getDataAutuacao() {
		return dataAutuacao;
	}

	public void setDataAutuacao(Date dataAutuacao) {
		this.dataAutuacao = dataAutuacao;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
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

	public PecaPedidoColegiado getPecaPromocao() {
		return pecaPromocao;
	}

	public void setPecaPromocao(PecaPedidoColegiado pecaPedido) {
		this.pecaPromocao = pecaPedido;
		this.membroResponsavel = pecaPedido.getMembroResponsavel();
		this.identificadorPecaPromocao = pecaPedido.getId();
	}

	public Long getMembroResponsavel() {
		return membroResponsavel;
	}

	public void setMembroResponsavel(Long membroResponsavel) {
		this.membroResponsavel = membroResponsavel;
	}

	public Long getIdentificadorPecaPromocao() {
		return identificadorPecaPromocao;
	}

	public void setIdentificadorPecaPromocao(Long identificadorPecaPromocao) {
		this.identificadorPecaPromocao = identificadorPecaPromocao;
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

	public Set<String> getProvidenciasExecutadas() {
		return providenciasExecutadas;
	}

	public void setProvidenciasExecutadas(Set<String> providenciasExecutadas) {
		this.providenciasExecutadas = providenciasExecutadas;
	}

	public void adicionarProvidencia(String providencia) {
		this.providenciasExecutadas.add(providencia);
	}

	public String getTextosProvidencias() {
		return StringUtils.join(providenciasExecutadas, ',');
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataEntrada == null) ? 0 : dataEntrada.hashCode());
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
		ProcedimentoDeliberadoColegiado other = (ProcedimentoDeliberadoColegiado) obj;
		if (dataEntrada == null) {
			if (other.dataEntrada != null) {
				return false;
			}
		} else if (!dataEntrada.equals(other.dataEntrada)) {
			return false;
		}
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
		builder.append("ProcedimentoDeliberadoColegiado [");
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
		if (pecaPromocao != null) {
			builder.append("pecaPromocao=");
			builder.append(pecaPromocao);
			builder.append(", ");
		}
		if (membroResponsavel != null) {
			builder.append("membroResponsavel=");
			builder.append(membroResponsavel);
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
		if (getTextosProvidencias() != null) {
			builder.append("textosProvidencias=");
			builder.append(getTextosProvidencias());
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
