package com.vinhos.bd.controller;

import com.google.gson.Gson;
import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.dto.ComprasPorDiaDaSemanaDTO;
import com.vinhos.bd.dto.ComprasPorPeriodoDTO;
import com.vinhos.bd.dto.ComprasPorUsuarioDTO;
import com.vinhos.bd.dto.ValorCompradoPorDiaDTO;
import com.vinhos.bd.model.Compras;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import com.vinhos.bd.dao.ComprasDAO;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value="/compras")
public class ComprasController {
    ComprasDAO dao;
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

    @GetMapping(value = "/porPeriodo")
    public List<ComprasPorPeriodoDTO> getComprasPorPeriodo(@RequestParam String periodo, HttpServletResponse response) throws ServletException, IOException {
        List<ComprasPorPeriodoDTO> comprasPorPeriodoDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            comprasPorPeriodoDTOList = dao.fetchBuysPerDay(periodo);

            if (comprasPorPeriodoDTOList.isEmpty()) {
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
        return comprasPorPeriodoDTOList;
    }

    @GetMapping(value = "/porData")
    public List<ComprasPorPeriodoDTO> getComprasPorData (@RequestParam Date data_ini, @RequestParam Date data_fim, HttpServletResponse response) throws SQLException, IOException {
        List<ComprasPorPeriodoDTO> comprasPorPeriodoDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            comprasPorPeriodoDTOList = dao.fetchBuysByDate(data_ini, data_fim);

            if (comprasPorPeriodoDTOList.isEmpty()) {
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
        return comprasPorPeriodoDTOList;
    }

    @GetMapping(value = "/diaSemanaPeriodo")
    public List<ComprasPorDiaDaSemanaDTO> getComprasByDiaSemanaPeriodo (@RequestParam String periodo, HttpServletResponse response) throws ServletException, IOException {
        List<ComprasPorDiaDaSemanaDTO> comprasPorDiaDaSemanaDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            comprasPorDiaDaSemanaDTOList = dao.fetchBuysByWeekdayPeriodo(periodo);

            if (comprasPorDiaDaSemanaDTOList.isEmpty()) {
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
        return comprasPorDiaDaSemanaDTOList;
    }

    @GetMapping(value = "/diaSemanaData")
    public List<ComprasPorDiaDaSemanaDTO> getComprasByDiaSemanaData (@RequestParam Date data_ini, @RequestParam Date data_fim, HttpServletResponse response) throws ServletException, IOException {
        List<ComprasPorDiaDaSemanaDTO> comprasPorDiaDaSemanaDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            comprasPorDiaDaSemanaDTOList = dao.fetchBuysByWeekdayData(data_ini, data_fim);

            if (comprasPorDiaDaSemanaDTOList.isEmpty()) {
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
        return comprasPorDiaDaSemanaDTOList;
    }

    @GetMapping(value = "/porUsuarioPeriodo")
    public List<ComprasPorUsuarioDTO> getComprasByUsuarioPeriodo (@RequestParam String periodo, HttpServletResponse response) throws ServletException, IOException {
        List<ComprasPorUsuarioDTO> comprasPorUsuarioDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            comprasPorUsuarioDTOList = dao.fetchBuysByUserPeriodo(periodo);

            if (comprasPorUsuarioDTOList.isEmpty()) {
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
        return comprasPorUsuarioDTOList;
    }

    @GetMapping(value = "/porUsuarioData")
    public List<ComprasPorUsuarioDTO> getComprasByUsuarioData (@RequestParam Date data_ini, @RequestParam Date data_fim, HttpServletResponse response) throws ServletException, IOException {
        List<ComprasPorUsuarioDTO> comprasPorUsuarioDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            comprasPorUsuarioDTOList = dao.fetchBuysByUserData(data_ini, data_fim);

            if (comprasPorUsuarioDTOList.isEmpty()) {
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
        return comprasPorUsuarioDTOList;
    }

    @GetMapping(value = "/valorCompradoDiaPeriodo")
    public List<ValorCompradoPorDiaDTO> getCompradoDiaPeriodo (@RequestParam String periodo, HttpServletResponse response) throws ServletException, IOException {
        List<ValorCompradoPorDiaDTO> valorCompradoPorDiaDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            valorCompradoPorDiaDTOList = dao.fetchBuysPerDayByPeriodo(periodo);

            if (valorCompradoPorDiaDTOList.isEmpty()) {
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
        return valorCompradoPorDiaDTOList;
    }

    @GetMapping(value = "/valorCompradoDiaData")
    public List<ValorCompradoPorDiaDTO> getCompradoDiaData (@RequestParam Date data_ini, @RequestParam Date data_fim, HttpServletResponse response) throws ServletException, IOException {
        List<ValorCompradoPorDiaDTO> valorCompradoPorDiaDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getComprasDAO();
            valorCompradoPorDiaDTOList = dao.fetchBuysPerDayByData(data_ini, data_fim);

            if (valorCompradoPorDiaDTOList.isEmpty()) {
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
        return valorCompradoPorDiaDTOList;
    }
}