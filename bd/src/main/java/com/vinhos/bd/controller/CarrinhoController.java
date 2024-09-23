package com.vinhos.bd.controller;

import com.vinhos.bd.dao.CarrinhoDAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.model.Carrinho;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value="/carrinho")
public class CarrinhoController {

    private CarrinhoDAO dao;

    @GetMapping(value = "/read")
    public Carrinho getCarrinho(@RequestBody Carrinho carrinho, HttpServletResponse response) throws ServletException, IOException {
        Carrinho carrinhoAux;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoDAO();

            carrinhoAux = dao.read(carrinho.getNumero());

            if (carrinhoAux == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Carrinho não encontrado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return carrinhoAux;
    }

    @GetMapping(value = "/all")
    public List<Carrinho> getAllCarrinhos(HttpServletResponse response) throws ServletException, IOException {
        List<Carrinho> carrinhosList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoDAO();

            carrinhosList = dao.all();

            if (carrinhosList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Ainda não há carrinhos.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return carrinhosList;
    }

    @PostMapping(value = "/create")
    public void createCarrinho(@RequestBody Carrinho carrinho, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Usuário não logado!");
            return;
        }

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoDAO();

            dao.create(carrinho);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            response.getWriter().write("Carrinho criado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public void updateCarrinho(@RequestBody Carrinho carrinho, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Usuário não logado!");
            return;
        }

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoDAO();

            dao.update(carrinho);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Carrinho atualizado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public void deleteCarrinho(@RequestBody Carrinho carrinho, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Usuário não logado!");
            return;
        }

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoDAO();

            dao.delete(carrinho.getNumero());
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Carrinho deletado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @GetMapping(value = "/usuario")
    public List<Carrinho> findCarrinhoByUsuario(@RequestBody Carrinho carrinho, String usuarioEmail, HttpServletResponse response) throws ServletException, IOException {
        List<Carrinho> carrinhosList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoDAO();

            carrinhosList = dao.findCarrinhoByUsuario(carrinho);

            if (carrinhosList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Nenhum carrinho encontrado para o usuário especificado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return carrinhosList;
    }
}
