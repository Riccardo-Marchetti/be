package piastrellista.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import piastrellista.entities.Blog;
import piastrellista.exceptions.BadRequestException;
import piastrellista.payloads.BlogDTO;
import piastrellista.services.BlogService;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    // This method is used to get all blog
    @GetMapping
    public Page<Blog> getAllBlogs (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "creationDate") String sortBy){
        return this.blogService.getAllBlog(page, size, sortBy);
    }

    // This method is used to get a blog by its ID
    @GetMapping ("/{blogId}")
    public Blog getBlogById (@PathVariable long blogId){
        return this.blogService.getBlogById(blogId);
    }

    // This method is used to save a new blog
    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Blog saveBlog (@RequestBody @Validated BlogDTO payload, BindingResult validation){
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return this.blogService.saveBlog(payload);
    }

    // This method is used to update an existing blog
    @PutMapping ("/{blogId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Blog updateBlog (@PathVariable long blogId, @RequestBody @Validated BlogDTO payload , BindingResult validation ){
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return this.blogService.updateBlog(blogId, payload);
    }

    //  This method is used to delete a blog
    @DeleteMapping ("/{blogId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteBlog (@PathVariable long blogId){
        this.blogService.deleteBlog(blogId);
    }


}
