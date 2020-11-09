import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;

public class MyServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2 style='text-align:center'>This is my first servlet ~</h2>");
        out.println("</body></html>");
        response.getWriter().append("Served at: ").append(request.getContextPath());
        String path = request.getRealPath("/upload");
        response.getWriter().print(path);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        doGet(request, response);
        response.setContentType("text/html;charset=utf-8");
        //请求解决乱码
        request.setCharacterEncoding("utf-8");
        //响应解决乱码
        response.setCharacterEncoding("utf-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        String path = request.getRealPath("/upload");
        File file=new File(path);
        if(!file.exists()){
            file.mkdir();
        }

        factory.setRepository(new File(path));
        factory.setSizeThreshold(1024*1024*10) ;//设置 缓存的大小
        ServletFileUpload upload = new ServletFileUpload(factory);
//        upload.setHeaderEncoding("UTF-8");


        try {
            List<FileItem> list = upload.parseRequest( request);

            for(FileItem item : list)
            {
                String name = item.getFieldName();
                /*if(item.isFormField())
                {

                    String value = item.getString() ;

                    request.setAttribute(name, value);
                }*/
                if (name.contains("file"))
                {

                    /*String value = item.getName() ;

                    int start = value.lastIndexOf("\\");*/

//                    String filename = list.indexOf(item)+value.substring(start+1);
                    long time = System.currentTimeMillis();
                    String filename =  time+".jpg";
                    request.setAttribute(name, filename);
                    item.write( new File(path,filename) );//第三方提供的
                    System.out.println("上传成功："+filename);
                    response.getWriter().print(filename);
                }
            }
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println("上传失败");

            response.getWriter().print("上传失败："+e.getMessage());
        }
    }
}