package com.vinhos.bd.controller;

import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.model.Compras;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value="/compras")
public class ComprasController {

    DAO<Compras, Integer> dao;

    @PostMapping(value = "/create")
    public void createCompra(@RequestBody Compras compra, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();

            dao.create(compra);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            response.getWriter().write("Compra criada com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro ao criar compra: " + ex.getMessage());
        }
    }

    @GetMapping(value = "/read")
    public Compras getCompra(@RequestBody Compras compra, HttpServletResponse response) throws ServletException, IOException {
        Compras compraAux;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();

            compraAux = dao.read(compra.getNumero());

            if (compraAux == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Compra não encontrada.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro ao buscar compra: " + ex.getMessage());
            return null;
        }

        return compraAux;
    }

    @GetMapping(value = "/all")
    public List<Compras> getAllCompras(HttpServletResponse response) throws ServletException, IOException {
        List<Compras> listaCompras;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();

            listaCompras = dao.all();

            if (listaCompras.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Ainda não há compras registradas.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro ao listar compras: " + ex.getMessage());
            return null;
        }

        return listaCompras;
    }

    @PostMapping(value = "/update")
    public void updateCompra(@RequestBody Compras compra, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();

            dao.update(compra);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Compra atualizada com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro ao atualizar compra: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public void deleteCompra(@RequestBody Compras compra, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();

            dao.delete(compra.getNumero());
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Compra deletada com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro ao deletar compra: " + ex.getMessage());
        }
    }
}
