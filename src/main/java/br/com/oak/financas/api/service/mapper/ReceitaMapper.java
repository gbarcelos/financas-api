package br.com.oak.financas.api.service.mapper;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReceitaMapper {

  private final ModelMapper modelMapper;

  public Lancamento map(ReceitaInput source) {

    Lancamento lancamento = modelMapper.map(source, Lancamento.class);
    lancamento.setTipo(TipoLancamento.RECEITA);

    return lancamento;
  }

  public ReceitaDto unmap(Lancamento source) {
    return modelMapper.map(source, ReceitaDto.class);
  }

  public List<ReceitaDto> unmap(List<Lancamento> lancamentos) {

    List<ReceitaDto> list = new ArrayList<>(lancamentos.size());

    lancamentos.forEach(
        lan -> {
          list.add(unmap(lan));
        });

    return list;
  }
}
