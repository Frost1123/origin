package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Mingge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        System.out.println(req.getParameter("m1"));
        System.out.println(req.getParameter("m2"));
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(new Mingge("铭神","撸圣"));
        resp.getWriter().write(s);
    }
}
