package ru.job4j.dreamjob.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.job4j.dreamjob.store.PsqlStore;
import ru.job4j.dreamjob.store.Store;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(UploadServlet.class.getName());
    private final Store store = PsqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<String> images = new ArrayList<>();
        File folder = new File("images");
        if (!folder.exists()) {
            folder.mkdir();
        }
        for (File name : folder.listFiles()) {
            images.add(name.getName());
        }
        req.setAttribute("id", req.getParameter("id"));
        req.setAttribute("images", images);
        req.getRequestDispatcher("/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    int photoId = store.savePhoto();
                    int candidateId = Integer.parseInt(req.getParameter("id"));
                    store.updateCandidatePhotoId(photoId, candidateId);
                    File file = new File(folder + File.separator + "photo_" + photoId);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                }
            }
        } catch (FileUploadException e) {
            LOG.error(e);
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
