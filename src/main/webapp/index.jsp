<%@ page import="launch.Conexion" %>
<%
    if(request.getParameter("accion") != null){
        if(request.getParameter("accion").equals("login")){
            Conexion objConexion = new Conexion();    
            out.println(objConexion.executeQuery("SELECT id_nivel FROM public.\"Usuario\" WHERE correo='"+request.getParameter("user")+"' AND password='"+request.getParameter("password")+"';", request.getParameter("accion")));
            objConexion.Close();
        }else if(request.getParameter("accion").equals("RegCliente")){
            Conexion objConexion = new Conexion();
            out.println(objConexion.executeInsert("INSERT INTO public.\"Cliente\" (nombre, apellido, estado, municipio, colonia, calle, num_int, num_ext, codigo_postal, rfc, correo_electronico) VALUES ('"+
                        request.getParameter("nombre")+"','"+
                        request.getParameter("apellido")+"','"+
                        request.getParameter("estado")+"','"+
                        request.getParameter("municipio")+"','"+
                        request.getParameter("colonia")+"','"+
                        request.getParameter("calle")+"','"+
                        request.getParameter("numInt")+"','"+
                        request.getParameter("numExt")+"','"+
                        request.getParameter("CP")+"','"+
                        request.getParameter("RFC")+"','"+
                        request.getParameter("email")+"');"));
            objConexion.Close();
        }else{
            out.println("{\""+request.getParameter("accion")+"\":[]}");
        }
    }else{
        out.println("Accion es null");
    }
    
%>
