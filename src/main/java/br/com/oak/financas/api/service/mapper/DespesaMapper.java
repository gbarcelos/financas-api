package br.com.oak.financas.api.service.mapper;

import br.com.oak.financas.api.entity.Categoria;
import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;
import br.com.oak.financas.api.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static br.com.oak.financas.api.util.Constants.ID_DESPESA_OUTROS;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DespesaMapper {

  private final ModelMapper modelMapper;
  private final CategoriaRepository categoriaRepository;

  public Lancamento map(DespesaInput source) {

    Lancamento lancamento = modelMapper.map(source, Lancamento.class);
    lancamento.setTipo(TipoLancamento.DESPESA);

    Optional<Categoria> categoriaOptional = Optional.empty();
    if (Objects.nonNull(source.getCategoriaId())) {
      categoriaOptional = categoriaRepository.findById(source.getCategoriaId());
    }

    if (!categoriaOptional.isPresent()) {
      categoriaOptional = categoriaRepository.findById(ID_DESPESA_OUTROS);
    }

    categoriaOptional.ifPresent(lancamento::setCategoria);

    return lancamento;
  }

  public DespesaDto unmap(Lancamento source) {
    return modelMapper.map(source, DespesaDto.class);
  }

  public List<DespesaDto> unmap(List<Lancamento> lancamentos) {

    List<DespesaDto> list = new ArrayList<>(lancamentos.size());

    lancamentos.forEach(
        lan -> {
          list.add(unmap(lan));
        });

    return list;
  }
}
