package br.com.oak.financas.api.service.mapper;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DespesaMapper {

  private ModelMapper modelMapper;

  @Autowired
  public DespesaMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public Lancamento map(DespesaInput source) {
    Lancamento lancamento = modelMapper.map(source, Lancamento.class);
    lancamento.setTipo(TipoLancamento.DESPESA);
    return lancamento;
  }

  public DespesaDto unmap(Lancamento source) {
    return modelMapper.map(source, DespesaDto.class);
  }

  public List<DespesaDto> unmap(List<Lancamento> lancamentos) {

    List<DespesaDto> list = new ArrayList<>();

    lancamentos.forEach(
        lan -> {
          list.add(unmap(lan));
        });

    return list;
  }
}
