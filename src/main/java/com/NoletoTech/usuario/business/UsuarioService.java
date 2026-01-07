package com.NoletoTech.usuario.business;

import com.NoletoTech.usuario.business.converter.UsuarioConverter;
import com.NoletoTech.usuario.business.dto.UsuarioDTO;
import com.NoletoTech.usuario.infrastructure.entity.Usuario;
import com.NoletoTech.usuario.infrastructure.exceptions.ConflictException;
import com.NoletoTech.usuario.infrastructure.exceptions.ResourceNotFoundByException;
import com.NoletoTech.usuario.infrastructure.repository.UsuarioRepository;
import com.NoletoTech.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
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

        public Usuario buscaUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundByException("Email não encontrado" + email));
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


    }

