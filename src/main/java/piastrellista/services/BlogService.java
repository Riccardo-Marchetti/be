package piastrellista.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import piastrellista.entities.Blog;
import piastrellista.exceptions.NotFoundException;
import piastrellista.payloads.BlogDTO;
import piastrellista.repositories.BlogDAO;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogDAO blogDAO;

    // Returns a paginated list of all blog
    public Page<Blog> getAllBlog(int page, int size, String sortBy){
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.blogDAO.findAll(pageable);
    }

    // Returns all blog
    public List<Blog> findAll() {
        return this.blogDAO.findAll();
    }

    // Returns a blog by its id
    public Blog getBlogById (long id){
        return this.blogDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // Saves a new blog to the database
    public Blog saveBlog (BlogDTO payload){
        Blog blog = new Blog(payload.title(), payload.previewImageUrl(), payload.description(), payload.creationDate(), payload.texts(), payload.imagesUrl());
        return this.blogDAO.save(blog);
    }

    // Saves a blog to the database
    public Blog saveBlog (Blog blog){
        Blog newBlog = new Blog(blog.getTitle(), blog.getPreviewImageUrl(), blog.getDescription(), blog.getCreationDate(), blog.getTexts(), blog.getImagesUrl());
        return this.blogDAO.save(blog);
    }

    // Updates a blog in the database
    public Blog updateBlog (long id, BlogDTO payload){
        Blog blog = this.blogDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        blog.setTitle(payload.title());
        blog.setPreviewImageUrl(payload.previewImageUrl());
        blog.setDescription(payload.description());
        blog.setCreationDate(payload.creationDate());
        blog.setTexts(payload.texts());
        blog.setImagesUrl(payload.imagesUrl());
        return this.blogDAO.save(blog);
    }

    // Deletes a blog from the database
    public void deleteBlog (long id){
        Blog blog = this.blogDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        this.blogDAO.delete(blog);
    }

}
