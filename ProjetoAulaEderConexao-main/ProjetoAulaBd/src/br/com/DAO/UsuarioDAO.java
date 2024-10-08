package br.com.DAO;

import br.com.DTO.UsuarioDTO;
import java.sql.*;
import javax.swing.JOptionPane;

public class UsuarioDAO {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public void inserirUsuario(UsuarioDTO objUsuarioDTO) {
        String sql = "INSERT INTO tb_usuarios(id_usuario, usuario, login, senha) VALUES(?, ?, ?, ?)";
        
        conexao = new ConexaoDAO().conector();
        
        try {
            // Verifica se o ID do usuário é menor que 0
            if (objUsuarioDTO.getId_usuario() < 0) {
                JOptionPane.showMessageDialog(null, "Erro: ID do usuário não pode ser menor que 0.");
                return;
            }
            
            // Verifica se os campos obrigatórios estão preenchidos
            if (objUsuarioDTO.getNomeUsuario().isEmpty() || 
                objUsuarioDTO.getLoginUsuario().isEmpty() || 
                objUsuarioDTO.getSenhaUsuario().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Erro: Todos os campos são obrigatórios.");
                return;
            }

            pst = conexao.prepareStatement(sql);
            pst.setInt(1, objUsuarioDTO.getId_usuario());
            pst.setString(2, objUsuarioDTO.getNomeUsuario());
            pst.setString(3, objUsuarioDTO.getLoginUsuario());
            pst.setString(4, objUsuarioDTO.getSenhaUsuario());
            
            int resultado = pst.executeUpdate();
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Usuário inserido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro: Usuário já existe ou não foi inserido.");
            }
            
            pst.close();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Código de erro para entrada duplicada
                JOptionPane.showMessageDialog(null, "Erro: Usuário já existe.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao inserir usuário: " + e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
