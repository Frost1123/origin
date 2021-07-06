package web.servlet;

import service.ProvinceService;
import service.serviceImpl.ProvinceServiceSelect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/provinceSelect")
public class ProvinceSelectServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProvinceService ps = new ProvinceServiceSelect();
        String json = ps.findAllJson();
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }
}
