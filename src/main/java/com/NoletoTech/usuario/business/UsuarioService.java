package com.NoletoTech.usuario.business;

import com.NoletoTech.usuario.business.converter.UsuarioConverter;
import com.NoletoTech.usuario.business.dto.EnderecoDTO;
import com.NoletoTech.usuario.business.dto.TelefoneDTO;
import com.NoletoTech.usuario.business.dto.UsuarioDTO;
import com.NoletoTech.usuario.infrastructure.entity.Endereco;
import com.NoletoTech.usuario.infrastructure.entity.Telefone;
import com.NoletoTech.usuario.infrastructure.entity.Usuario;
import com.NoletoTech.usuario.infrastructure.exceptions.ConflictException;
import com.NoletoTech.usuario.infrastructure.exceptions.ResourceNotFoundByException;
import com.NoletoTech.usuario.infrastructure.repository.EnderecoRepository;
import com.NoletoTech.usuario.infrastructure.repository.TelefoneRepository;
import com.NoletoTech.usuario.infrastructure.repository.UsuarioRepository;
import com.NoletoTech.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final TelefoneRepository telefoneRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UsuarioDTO salvaUsuario (UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExiste(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado" + email);
            }
        }catch (ConflictException e){
            throw new ConflictException("Email já cadastrado"+ e.getCause());
        }

        }
        public boolean verificaEmailExiste(String email){
        return usuarioRepository.existsByEmail(email);
        }

        public UsuarioDTO buscaUsuarioPorEmail(String email) {
            try {
                return usuarioConverter.paraUsuarioDTO(
                        usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundByException("Email não encontrado" + email)));
            }catch(ResourceNotFoundByException e){
                throw new ResourceNotFoundByException("Email não encontrado" + email);
            }
        }
        public void deletaUsuarioPorEmail (String email){
        usuarioRepository.deleteByEmail(email);
        }

        public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundByException("Email não encontrado"));
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
        }

        public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(()-> new ResourceNotFoundByException("Id não encontrado" + idEndereco));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
        }

        public TelefoneDTO atualizaTelefone (Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(()-> new ResourceNotFoundByException("Id não encontrado" + idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));

        }


        public EnderecoDTO cadastraEndereco (String token, EnderecoDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundByException("Email não encontrado"));
        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
        }


        public TelefoneDTO cadastraTelefone (String token, TelefoneDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundByException("email não encontrado"));
        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
        }

    }

