package com.vinhos.bd.controller;

import com.vinhos.bd.dao.CompraCarrinhoVinhoDAO;
import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.dto.ComprasPorUsuarioDTO;
import com.vinhos.bd.dto.VinhosVendidosPorPeriodoDTO;
import com.vinhos.bd.model.CompraCarrinhoVinho;
import com.vinhos.bd.model.CompraCarrinhoVinhoID;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/compraCarrinhoVinho")
public class CompraCarrinhoVinhoController {

    DAO<CompraCarrinhoVinho, CompraCarrinhoVinhoID> dao;
    CompraCarrinhoVinhoDAO compraCarrinhoVinhoDAO;

    @GetMapping(value = "/list")
    public List<CompraCarrinhoVinho> listCompraCarrinhoVinho (@RequestBody CompraCarrinhoVinho compraCarrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        List<CompraCarrinhoVinho> compraCarrinhoVinhoList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            compraCarrinhoVinhoDAO = daoFactory.getCompraCarrinhoVinhoDAO();

            compraCarrinhoVinhoList = compraCarrinhoVinhoDAO.listaCompra(compraCarrinhoVinho.getId().getNumeroCompra());

            if (compraCarrinhoVinhoList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Compra não encontrada.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return compraCarrinhoVinhoList;
    }

    @GetMapping(value = "/read")
    public CompraCarrinhoVinho readCompraCarrinhoVinho (@RequestBody CompraCarrinhoVinho compraCarrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        CompraCarrinhoVinho carrinhoVinho;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCompraCarrinhoVinhoDAO();

            carrinhoVinho = dao.read(compraCarrinhoVinho.getId());
            if (carrinhoVinho == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);   // 404 Not found
                response.getWriter().write("Item da compra não encontrado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);   // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return carrinhoVinho;
    }

    @GetMapping(value = "/all")
    public List<CompraCarrinhoVinho> getAllCompraCarrinhoVinho (@RequestBody CompraCarrinhoVinho compraCarrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        List<CompraCarrinhoVinho> compraCarrinhoVinhoList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCompraCarrinhoVinhoDAO();

            compraCarrinhoVinhoList = dao.all();

            if (compraCarrinhoVinhoList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Ainda não tem itens em compras.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException| IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return compraCarrinhoVinhoList;
    }

    @PostMapping(value = "/create")
    public void createItemCompra (@RequestBody CompraCarrinhoVinho compraCarrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCompraCarrinhoVinhoDAO();

            dao.create(compraCarrinhoVinho);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Item adicionado à compra, com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);   // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public void udpateItemCompra (@RequestBody CompraCarrinhoVinho compraCarrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCompraCarrinhoVinhoDAO();

            dao.update(compraCarrinhoVinho);
            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            response.getWriter().write("Relação item na compra atualizada com sucesso.");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public void deleteItemCompra (@RequestBody CompraCarrinhoVinho compraCarrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCompraCarrinhoVinhoDAO();

            dao.delete(compraCarrinhoVinho.getId());
            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            response.getWriter().write("Vinho deletado da compra com sucesso.");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @GetMapping(value = "/vendidosPorData")
    public List<VinhosVendidosPorPeriodoDTO> getVendidosData (@RequestParam String periodo, HttpServletResponse response) throws ServletException, IOException {
        List<VinhosVendidosPorPeriodoDTO> vinhosVendidosPorPeriodoDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            compraCarrinhoVinhoDAO = daoFactory.getCompraCarrinhoVinhoDAO();
            vinhosVendidosPorPeriodoDTOList = compraCarrinhoVinhoDAO.fetchWinesSoldByData(periodo);

            if (vinhosVendidosPorPeriodoDTOList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Não há compras registradas para esse período.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
        return vinhosVendidosPorPeriodoDTOList;
    }

    @GetMapping(value = "/vendidosPorPeriodo")
    public List<VinhosVendidosPorPeriodoDTO> getVendidosPeriodo (@RequestParam Date data_ini, @RequestParam Date data_fim, HttpServletResponse response) throws ServletException, IOException {
        List<VinhosVendidosPorPeriodoDTO> vinhosVendidosPorPeriodoDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            compraCarrinhoVinhoDAO = daoFactory.getCompraCarrinhoVinhoDAO();
            vinhosVendidosPorPeriodoDTOList = compraCarrinhoVinhoDAO.fetchWinesSoldByPeriod(data_ini, data_fim);

            if (vinhosVendidosPorPeriodoDTOList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Não há compras registradas para esse período.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
        return vinhosVendidosPorPeriodoDTOList;
    }
}
