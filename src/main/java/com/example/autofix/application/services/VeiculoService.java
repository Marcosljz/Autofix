
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.VeiculoRequest;
import com.example.autofix.application.DTOs.VeiculoResponse;
import com.example.autofix.application.DTOs.EnumStatusVeiculo;
import com.example.autofix.domain.entities.Mecanica;
import com.example.autofix.domain.entities.Usuario;
import com.example.autofix.domain.entities.Veiculo;
import com.example.autofix.domain.repository.VeiculoRepository;
import com.example.autofix.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContextoSegurancaService contexto;

    public List<VeiculoResponse> listarTodosVeiculosTable() {

        List<Veiculo> veiculos;

        if (contexto.isAdmin()) {

            veiculos = veiculoRepository.findAll();

        } else if (contexto.isAdminMecanica()) {

            Long mecanicaId = contexto.getMecanicaId();

            if (mecanicaId == null) {
                return List.of();
            }

            veiculos = veiculoRepository.findByMecanica_Id(mecanicaId);

        } else {

            Long usuarioId = contexto.getUsuarioLogadoId();

            if (usuarioId == null) {
                return List.of();
            }

            veiculos = veiculoRepository.findByUsuarioCadastro_Id(usuarioId);
        }

        return veiculos.stream()
                .map(VeiculoResponse::new)
                .toList();
    }

    public VeiculoResponse buscarPorId(Long id) {

        var veiculo = veiculoRepository.findById(id).orElse(null);

        if (veiculo == null) {
            return null;
        }

        if (contexto.isAdmin()) {
            return new VeiculoResponse(veiculo);
        }

        if (contexto.isAdminMecanica() &&
                contexto.podeAcessar(veiculo.getMecanica())) {
            return new VeiculoResponse(veiculo);
        }

        Long usuarioId = contexto.getUsuarioLogadoId();

        if (usuarioId != null &&
                veiculo.getUsuarioCadastro() != null &&
                veiculo.getUsuarioCadastro().getId().equals(usuarioId)) {
            return new VeiculoResponse(veiculo);
        }

        return null;
    }

    public VeiculoResponse salvarVeiculo(VeiculoRequest veiculo, Long id) {

        if (id == 0L) {

            Mecanica mecanica = contexto.getMecanicaDoLogado();

            if (mecanica == null) {
                throw new RuntimeException(
                        "Seu perfil de administrador permite visualizar" +
                                " todas as mecânicas, sem permissão para realizar operações."
                );
            }

            Long usuarioId = contexto.getUsuarioLogadoId();

            Usuario usuario = usuarioRepository.getReferenceById(usuarioId);

            Veiculo veiculoBanco = new Veiculo(veiculo);

            veiculoBanco.setMecanica(mecanica);
            veiculoBanco.setUsuarioCadastro(usuario);

            veiculoRepository.save(veiculoBanco);

            return new VeiculoResponse(veiculoBanco);

        } else {

            Veiculo veiculoBanco = veiculoRepository.findById(id).orElse(null);

            if (veiculoBanco != null &&
                    !contexto.isAdmin() &&
                    contexto.podeAcessar(veiculoBanco.getMecanica()) &&
                    (contexto.isAdminMecanica() ||
                            (veiculoBanco.getUsuarioCadastro() != null &&
                                    veiculoBanco.getUsuarioCadastro().getId()
                                            .equals(contexto.getUsuarioLogadoId())))) {

                veiculoBanco.setPlaca(veiculo.placa());
                veiculoBanco.setModelo(veiculo.Modelo());
                veiculoBanco.setAno(veiculo.ano());
                veiculoBanco.setCor(veiculo.cor());
                veiculoBanco.setQuilometragem(veiculo.quilometragem());

                veiculoRepository.save(veiculoBanco);

                return new VeiculoResponse(veiculoBanco);
            }
        }

        return null;
    }

    public boolean atualizarStatus(EnumStatusVeiculo status, Long id) {

        Veiculo veiculoBanco = veiculoRepository.findById(id).orElse(null);

        if (veiculoBanco != null &&
                !contexto.isAdmin() &&
                contexto.podeAcessar(veiculoBanco.getMecanica()) &&
                (contexto.isAdminMecanica() ||
                        (veiculoBanco.getUsuarioCadastro() != null &&
                                veiculoBanco.getUsuarioCadastro().getId()
                                        .equals(contexto.getUsuarioLogadoId())))) {

            veiculoBanco.setStatusVeiculo(status);

            veiculoRepository.save(veiculoBanco);

            return true;
        }

        return false;
    }
}
